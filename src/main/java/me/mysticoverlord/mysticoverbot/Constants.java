/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot;


import java.util.HashMap;
import java.util.Map;
import org.discordbots.api.client.DiscordBotListAPI;

import net.dv8tion.jda.api.entities.TextChannel;

public class Constants {
	public static final String testPath = "";
	public static final String homePath = "/home/pi/Bot/";
	public static final String path = testPath;
	public static final String configpath =  "botconfig.json";
	public static final String databasepath = "dbencrypted.db";
	public static final Map<Long, Map<Long, Boolean>> votes = new HashMap<Long, Map<Long, Boolean>>();
	public static final Map<Long, Boolean> loop = new HashMap<Long, Boolean>();
	public static final Map<Long, Boolean> loopqueue = new HashMap<Long, Boolean>();
	public static String VERSION = "V.2.4.0";
    public static final long OWNER = 394066807926947850L;
    public static TextChannel channel;
    public static String PREFIX = "o!";
    public static DiscordBotListAPI api = new DiscordBotListAPI.Builder().token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjU3NjA2MTEyNDA0MzIxMDc1OSIsImJvdCI6dHJ1ZSwiaWF0IjoxNTY1MTExNzgwfQ.P0OX3YN_waIKd1U6YoZURRcIrAsIRd61NuQ6oHOSYvw").botId("576061124043210759").build();
}

