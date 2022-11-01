package com.travula.model;

public enum VoteType {
    UPVOTE(1),
    NOVOTE(0),
    DOWNVOTE(-1);


    private final int direction;

    VoteType(int direction) {
        this.direction = direction;
    }

    public Integer getDirection() {
        return direction;
    }
}
