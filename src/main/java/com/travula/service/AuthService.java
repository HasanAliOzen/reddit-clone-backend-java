package com.travula.service;

import com.travula.dto.AuthenticationResponse;
import com.travula.dto.LoginRequest;
import com.travula.dto.RegisterRequest;
import com.travula.exceptions.SpringRedditException;
import com.travula.model.NotificationEmail;
import com.travula.model.User;
import com.travula.model.VerificationToken;
import com.travula.repository.UserRepository;
import com.travula.repository.VerificationTokenRepository;
import com.travula.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(false);
        user.setCreatedDate(Instant.now());
        userRepository.save(user);

        generateVerificationToken(user);
    }
    private void sendVerifyToken(String email,String token){
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new SpringRedditException("No user with " +email+ " number!!!"));

        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(),
                "Thank you for signing up to Spring Reddit, " +
                        "please click on the below url to activate your account. " +
                        "Link has 30 minutes life then will be expired : " +
                        "http://localhost:8080/api/auth/account-verification/" + token));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public void generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setCreatedDate(Instant.now());
        verificationToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.MINUTES));

        sendVerifyToken(user.getEmail(),verificationTokenRepository.save(verificationToken).getToken());
    }
    public void updateVerificationToken(Long id){
        User user = userRepository.findById(id).orElseThrow(
                ()-> new SpringRedditException("No user with "+ id + " id number"));
        VerificationToken verificationToken = verificationTokenRepository.findByUser(user).orElseThrow(
                ()-> new SpringRedditException("No token for user "+ user.getUsername()));

        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setCreatedDate(Instant.now());
        verificationToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.MINUTES));
        sendVerifyToken(user.getEmail(),verificationTokenRepository.save(verificationToken).getToken());
    }

    @Transactional
    public void verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository
                .findByToken(token)
                .orElseThrow(()-> new SpringRedditException("No token found!!!"));

        if (verificationToken.getExpiryDate().isBefore(Instant.now())){
            throw new SpringRedditException("Token expired!!!");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        Authentication authenticate= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
