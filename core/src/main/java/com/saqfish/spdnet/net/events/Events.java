/*
 * Pixel Dungeon
 * Copyright (C) 2021 saqfish
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.saqfish.spdnet.net.events;

public class Events {
    public static final String MOTD = "motd";
    public static final String ACTION = "action";
    public static final String PLAYERLISTREQUEST = "playerlistrequest";
    public static final String TRANSFER = "transfer";
    public static final String CHAT = "chat";
    public static final String RECORDS = "records";

    public static class Error{
        public String message;
    }
}
