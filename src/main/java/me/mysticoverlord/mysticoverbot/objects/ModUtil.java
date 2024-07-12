package me.mysticoverlord.mysticoverbot.objects;

import java.awt.Color;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;

public class ModUtil {



    /**
     * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
     * @param input
     * @return
     */
	public static String warningPunishment(int warnings, Guild guild, Member member, TextChannel channel) {	

    	if (!roleExists(guild) == true) {
			try {   			
				guild.getTextChannelsByName("log", true).get(0).sendMessage(member.getAsMention() + " has had over 3 Automod violations!\nDue to haveing detected no Muted role no Mute Punishments are possible!").queue();			
			} catch (IndexOutOfBoundsException e) {				
				guild.getTextChannelsByName("logs", true).get(0).sendMessage(member.getAsMention() + " has had over 3 Automod violations!\nDue to haveing detected no Muted role no Mute Punishments are possible!").queue();			
			}
			return null;
		}
		
		Role role = guild.getRolesByName("Muted", true).get(0);
		
    	switch(warnings) {
    	case 3:
    		guild.addRoleToMember(member, role).queue();
    		SQLiteUtil.updateMuted(guild.getId(), member.getId(), FormatUtil.dateCalculator("0", "0", "30"), channel);
    		return "30 Minutes";
    	case 4: guild.addRoleToMember(member, role).queue();
    	        SQLiteUtil.updateMuted(guild.getId(), member.getId(), FormatUtil.dateCalculator("0", "1", "0"), channel);
    	return "1 hour";
    	case 5: guild.addRoleToMember(member, role).queue();
    	        SQLiteUtil.updateMuted(guild.getId(), member.getId(), FormatUtil.dateCalculator("0", "6", "0"), channel);
    	return "6 hours";
    	case 6: guild.addRoleToMember(member, role).queue();
    			SQLiteUtil.updateMuted(guild.getId(), member.getId(), FormatUtil.dateCalculator("1", "0", "0"), channel);
    	return "1 day";
    	case 7: guild.addRoleToMember(member, role).queue();
				SQLiteUtil.updateMuted(guild.getId(), member.getId(), FormatUtil.dateCalculator("7", "0", "0"), channel);
				return "7 days";
    	case 8: guild.addRoleToMember(member, role).queue();
				SQLiteUtil.updateMuted(guild.getId(), member.getId(), FormatUtil.dateCalculator("30", "0", "0"), channel);
				return "30 days";
    	default:
    		return null;

    	}
    }
	
	
	
    public static boolean roleExists(Guild guild) {
		try {
			guild.getRolesByName("Muted", true).get(0);
			return true;
		} catch (Exception e) {
			return false;
		}
	
	}
    
    public static boolean isFromThisGuild(GuildMessageReceivedEvent event) {
		List<String> invites = event.getMessage().getInvites();
		List<Invite> inv = event.getGuild().retrieveInvites().complete();
		event.getJDA().getGuildById("573084332634406923").retrieveInvites().complete().forEach((invite) -> {
			try {
			inv.add(invite);
			} catch (UnsupportedOperationException e) {
				
			}
		});
		
		boolean bool = false;
		for (int x = 0; x < invites.size(); x++) {
			for (int y = 0; y < inv.size(); y++) {
				if (inv.get(y).getCode().equals(invites.get(x))) {
					bool = true;
					break;
				}
			}

		if (bool == false) {
			return false;
		}
    }
		
		return true;
    }
    
	public static Color RED = new Color(150, 0, 0);
	public static Color DARK_RED = new Color(100,0,0);
	public static Color YELLOW = new Color(150,150,0);
	public static Color DARK_YELLOW = new Color(150, 100, 0);
	
    public static void doWarn(GuildMessageReceivedEvent event, String reason) {
    	int warnings = 1 + SQLiteUtil.getWarnings(event.getGuild().getId(), event.getAuthor().getId());
        SQLiteUtil.updateWarnings(event.getGuild().getId(), event.getAuthor().getId() , warnings, event.getChannel());
		String punishment =  ModUtil.warningPunishment(warnings, event.getGuild(), event.getMember(), event.getChannel());
		if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			event.getMessage().delete().queue();
		}
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.addField("User", event.getAuthor().getAsMention(), true)
				.addField("Moderator", event.getJDA().getSelfUser().getAsMention(), true)
				.setTitle((warnings >= 10 ? "Banned" : warnings == 9 ? "Kicked" : warnings > 3 ? "Muted" : "Warned") + " " + event.getAuthor().getAsTag())
				.setColor((warnings >= 10 ? DARK_RED : warnings == 9 ? RED : warnings > 3 ? DARK_YELLOW : YELLOW))
				.addField("Reason", reason, true);
		
		if(warnings > 2 && warnings < 9) {
			builder.addField("Duration", punishment , true);
		}
			
		builder.addField("Current Warnings", String.valueOf(warnings), true)
		.addField("Message", event.getMessage().getContentDisplay(), true)
		.addField("In channel", event.getChannel().getAsMention(), true);
		
		try { 
	    	TextChannel channel = event.getGuild().getTextChannelsByName("log", true).get(0);
	    	if (channel != null && event.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE)) {
	    		channel.sendMessageEmbeds(builder.build()).timeout(10, TimeUnit.MILLISECONDS).queue();
	    	}
		} catch (IndexOutOfBoundsException e) {
		}
    	if (!event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_WRITE)) {
    		return;
    	}
		builder.clear()
		.setAuthor(event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl(), event.getAuthor().getEffectiveAvatarUrl())
		.setTitle((warnings >= 10 ? "Banned" : warnings == 9 ? "Kicked" : warnings > 3 ? "Muted" : "Warned") + " " + event.getAuthor().getAsTag())
		.setColor((warnings >= 10 ? DARK_RED : warnings == 9 ? RED : warnings > 3 ? DARK_YELLOW : YELLOW))
		.setDescription(event.getAuthor().getAsMention() + " has been "+ 
		(warnings >= 10 ? "banned" : warnings == 9 ? "kicked" : warnings > 3 ? "muted" : "warned") 
		+ "!\n\n**Reason:** " + reason);
		
		if(warnings > 2 && warnings < 9) {
			builder.addField("Duration", punishment , true);
		}
		
		builder.addField("Current Warnings", String.valueOf(warnings), true);

		event.getChannel().sendMessageEmbeds(builder.build()).queue();
		return;
	}

    
    public static void doWarn(GuildMessageUpdateEvent event, String reason, String newmessage, String oldMessage) {
    	int warnings = 1 + SQLiteUtil.getWarnings(event.getGuild().getId(), event.getAuthor().getId());
        SQLiteUtil.updateWarnings(event.getGuild().getId(), event.getAuthor().getId() , warnings, event.getChannel());
		String punishment =  warningPunishment(warnings, event.getGuild(), event.getMember(), event.getChannel());
		if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			event.getMessage().delete().queue();
		}
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.addField("User", event.getAuthor().getAsMention(), true)
				.addField("Moderator", event.getJDA().getSelfUser().getAsMention(), true)
				.setTitle((warnings >= 10 ? "Banned" : warnings == 9 ? "Kicked" : warnings > 3 ? "Muted" : "Warned") + " " + event.getAuthor().getAsTag())
				.setColor((warnings >= 10 ? DARK_RED : warnings == 9 ? RED : warnings > 3 ? DARK_YELLOW : YELLOW))
				.addField("Reason", reason, true);
		
		if(warnings > 2 && warnings < 9) {
			builder.addField("Duration", punishment , true);
		}
			
		builder.addField("Current Warnings", String.valueOf(warnings), true)
			.addField("Previous Message", oldMessage , true)
			.addField("New Message", newmessage, true)
			.addField("In channel", event.getChannel().getAsMention(), true);
		
		try { 
	    	TextChannel channel = event.getGuild().getTextChannelsByName("log", true).get(0);
	    	if (channel != null && event.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE)) {
	    		channel.sendMessageEmbeds(builder.build()).timeout(10, TimeUnit.MILLISECONDS).queue();
	    	}
		} catch (IndexOutOfBoundsException e) {
		}
    	if (!event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_WRITE)) {
    		return;
    	}
		builder.clear()
		.setAuthor(event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl(), event.getAuthor().getEffectiveAvatarUrl())
		.setTitle((warnings >= 10 ? "Banned" : warnings == 9 ? "Kicked" : warnings > 3 ? "Muted" : "Warned") + " " + event.getAuthor().getAsTag())
		.setColor((warnings >= 10 ? DARK_RED : warnings == 9 ? RED : warnings > 3 ? DARK_YELLOW : YELLOW))
		.setDescription(event.getAuthor().getAsMention() + " has been "+ 
		(warnings >= 10 ? "banned" : warnings == 9 ? "kicked" : warnings > 3 ? "muted" : "warned") 
		+ "!\n\n**Reason:** " + reason);
		
		if(warnings > 2 && warnings < 9) {
			builder.addField("Duration", punishment , true);
		}
		
		builder.addField("Current Warnings", String.valueOf(warnings), true);

		event.getChannel().sendMessageEmbeds(builder.build()).queue();
		return;
	}

    
    
    public static boolean containsSwear(String message) {
        ArrayList<String> swears = new ArrayList<String>();	
    	swears.add("bastard");
    	swears.add("bitch");
    	swears.add("bollocks");
    	swears.add("bullshit");
    	swears.add("cunt");
    	swears.add("effing");
    	swears.add("frigger");
    	swears.add("fuck");
    	swears.add("goddamn");
    	swears.add("godsdamn");
    	swears.add("horseshit");
    	swears.add("nigga");
    	swears.add("nigger");
    	swears.add("dick");
    	swears.add("prick");
    	swears.add("shit");
    	swears.add("shitass");
    	swears.add("slut");
    	swears.add("twat");
    	swears.add("whore");
    	swears.add("faggot");

        message = message.replaceAll("-", "").replaceAll("\0", "").replaceAll("1", "i").replaceAll("!", "i").replaceAll("3", "e").replaceAll("4", "a")
                .replaceAll("@", "a").replaceAll("5", "s").replaceAll("7", "t").replaceAll("0", "o").replaceAll("9", "g");
        
    	String[] args = message.split(" ");
    	
    	
    for (int y = 0; y < args.length; y++) {
    	String word = args[y];
    	word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");

    	
    	for (int x = 0; x < swears.size(); x++) {
    		if (word.equalsIgnoreCase(swears.get(x)) || word.contains(swears.get(x))) {
    			return true;
    		}
    	}
    }
     return false;
    }

}
