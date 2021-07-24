package com.saqfish.spdnet.net.events;

public class Events {
    public static final String MOTD = "motd";
    public static final String MESSAGE = "message";
    public static final String ACTION = "action";
    public static final String PLAYERLISTREQUEST = "playerlistrequest";
    public static final String TRANSFER = "transfer";

    public static class Error{
        public String message;
    }
}
