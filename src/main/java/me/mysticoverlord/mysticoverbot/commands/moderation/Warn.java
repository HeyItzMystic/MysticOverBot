package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.awt.Color;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.ModUtil;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Warn implements ICommand {
	
	public static Color RED = new Color(150, 0, 0);
	public static Color DARK_RED = new Color(100,0,0);
	public static Color YELLOW = new Color(150,150,0);
	public static Color DARK_YELLOW = new Color(100, 100, 0);
	public static Color GREEN = new Color(0, 150, 0);

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		TextChannel channel = event.getChannel();
		Member member = event.getMember();
		Member selfmember = event.getGuild().getSelfMember();
		
        if (args.isEmpty()) {
            channel.sendMessage("Please specify the targeted user!\n" + this.getUsage()).queue();
            return;
        }
        Member target = FilterUtil.getMemberByData(args.get(0), event, 0);
        
        if (target == null) {
            channel.sendMessage("ID or Mention had no match!").queue();
            return;
        }
		String reason;
		if (args.size() < 2) {
			reason = "no reason provided";
		} else {
			reason = String.join((CharSequence)" ", args.subList(1, args.size()));   
		}

         
		
		if (!member.hasPermission(Permission.MANAGE_ROLES, Permission.KICK_MEMBERS, Permission.BAN_MEMBERS)){
			channel.sendMessage("You need following permissions to use this command:\n`Manage Roles, Kick Members, Ban Members`").queue();
			return;
		}
		
		if (!selfmember.hasPermission(Permission.MANAGE_ROLES, Permission.KICK_MEMBERS, Permission.BAN_MEMBERS)){
			channel.sendMessage("I need following permissions to run this command:\n`Manage Roles, Kick Members, Ban Members`").queue();
			return;
		}
        
        if (!member.canInteract(target)) {
        	channel.sendMessage("You can't warn anyone above or equal to your role!").queue();
        	return;
        }
        
        if (!selfmember.canInteract(target)) {
        	channel.sendMessage("I can't warn anyone above or equal to my role!").queue();
        	return;
        }
		
		int warnings = 1 + SQLiteUtil.getWarnings(event.getGuild().getId(), target.getId());
        SQLiteUtil.updateWarnings(event.getGuild().getId(), target.getId() , warnings, event.getChannel());
		String punishment =  ModUtil.warningPunishment(warnings, event.getGuild(), target, event.getChannel());
		
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.addField("User", target.getAsMention(), true)
				.addField("Moderator", event.getAuthor().getAsMention(), true)
	            .setTitle("Warned " + target.getUser().getAsTag())
				.setColor(YELLOW).addField("Reason", reason, true)
	            .addField("Current Warnings", String.valueOf(warnings), true)
	            .addField("In Channel", event.getChannel().getAsMention(), true);


	    channel.sendMessage(builder.build()).queue();
		
		
		builder.clear()
		.setAuthor(event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl(), event.getAuthor().getEffectiveAvatarUrl());
	/*	
		target.getUser().openPrivateChannel().queue((channel) -> {
			channel.sendMessage("You have been warned in **" + event.getGuild().getName() + "** for " + reason + "\nYou currently have " + warnings + " warnings").queue();
		});
*/
   		if (warnings < 9 && warnings >= 3) {
			
			builder.setTitle("Muted " + target.getUser().getAsTag())
			.setColor(DARK_YELLOW).setDescription(target.getAsMention() + " has been muted!\n\n**Reason:** " + warnings + " Warnings")
			.addField("Current Warnings", warnings + "warnings (" + reason + ")", true)
			.addField("Duration", punishment , true);
			try {
				event.getChannel().sendMessage(builder.build()).queue();
				event.getGuild().getTextChannelsByName("log", true).get(0).sendMessage(builder.build()).queue();
				} catch (Exception e1) {
					
				}
		} else if (warnings == 9) {			
			builder.setTitle("Kicked " + target.getUser().getAsTag())
			.setColor(RED).
			setDescription(target.getAsMention() + " has been kicked!\n\n**Reason:** 9th Warning")
			.addField("Current Warnings", String.valueOf("warnings"), true);
			try {
				channel.sendMessage(builder.build()).queue();
				event.getGuild().getTextChannelsByName("log", true).get(0).sendMessage(builder.build()).queue();
				} catch (Exception e1) {
					
				}
		} else if (warnings >= 10) {		
			builder.setTitle("Banned " + target.getUser().getAsTag())
			.setColor(DARK_RED).setDescription(target.getAsMention() + " has been banned!\n\n**Reason:** 10th Warning")
			.addField("Current Warnings", String.valueOf("warnings"), true);	
			try {
				channel.sendMessage(builder.build()).queue();
				event.getGuild().getTextChannelsByName("log", true).get(0).sendMessage(builder.build()).queue();
				} catch (Exception e1) {
					
				}
		}
		
    }
		
	

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Warns mentioned user and auto punishes them after a certain amount of warnings has been reached\n\n3rd Warning: 30 minute mute\n4th Warning: 1 hour mute\n 5th Warning: 6 hour mute\n6th Warning: 1 day mute\n7th Warning 7 day mute\n8th Warning 30 day mute\n 9th Warning: kick\n 10th Warning: ban";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "warn";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: " + Constants.PREFIX + getInvoke() + " <@User>";
	}

}
