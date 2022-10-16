package com.travula.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;
}
