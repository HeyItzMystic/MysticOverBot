package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.awt.Color;
import java.util.List;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.FormatUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Tempmute implements ICommand {

	int counter = 0;
	
	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
		

        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        String reason = String.join((CharSequence)" ", args.subList(4, args.size()));       
		
		
        if (args.isEmpty()) {
            channel.sendMessage("Missing Arguments!\n" + this.getUsage()).queue();
            return;
        }
        
        if (mentionedMembers.isEmpty()) {
        	channel.sendMessage("Please mention a user\n" +  this.getUsage()).queue();
        	return;
        }
        
        Member target = mentionedMembers.get(0);
        
		if (!member.hasPermission(Permission.MANAGE_ROLES)) {
			event.getChannel().sendMessage("You don't have the required Permission to use this Command!\n`Manage Roles`").queue();
			return;
		}
		
		if (!selfMember.hasPermission(Permission.MANAGE_ROLES)) {
			event.getChannel().sendMessage("I am missing following permission to execute this command:\n`Manage Roles`").queue();
			return;
		}
		
		if (!member.canInteract(target)) {
			channel.sendMessage("You can't mute members higher than or equal to your rank!").queue();
			return;
		}
		
		if (!selfMember.canInteract(target)) {
			channel.sendMessage("I can't mute members higher than or equal to my own rank!").queue();
			return;
		}
		
		if (!roleExists(event)) {
			channel.sendMessage("No \"Muted\" role available!\nTo use this command create a \"Muted\" role or use o!mutesetup to make me do it for you!").queue();
			return;
		}

        Role role = event.getGuild().getRolesByName("Muted", false).get(0);
        event.getGuild().addRoleToMember(target, role).queue();		
		
        String minutes;
        String hours;
        String days;
        
		try {
		minutes = args.get(3).replaceFirst("m", "");		
		hours = args.get(2).replaceFirst("h", "");		
		days = args.get(1).replaceFirst("d", "");
		} catch (Exception e) {
			event.getChannel().sendMessage("No correct time format has been given!\n" + getUsage()).queue();
			return;
		}
		
		SQLiteUtil.getWarnings(event.getGuild().getId(), target.getId());
		SQLiteUtil.updateMuted(event.getGuild().getId(), target.getId(), FormatUtil.dateCalculator(days, hours, minutes), event.getChannel());
		
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setColor(new Color(150,150,0))
				.setTitle("Muted " + target.getUser().getAsTag())
				.addField("Moderator", member.getAsMention(), true)
				.addField("Duration", duration(days, hours, minutes), true)
				.addField("Reason", reason, true);
		
		event.getChannel().sendMessageEmbeds(builder.build()).queue();

			try {
    			event.getGuild().getTextChannelsByName("log", true).get(0).sendMessageEmbeds(builder.build()).queue();
    			} catch (Exception e1) {
    				event.getGuild().getTextChannelsByName("logs", true).get(0).sendMessageEmbeds(builder.build()).queue();
    			}
		}
		
	

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "mute a user Temporarily";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "tempmute";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: " + Constants.PREFIX + getInvoke() + " <@user> <d# h# m#> [reason]\nReplace the \"#\" with any number!";
	}
	
	private String duration(String days, String hours, String minutes) {
		StringBuilder builder = new StringBuilder();
		
		if (!days.isBlank()) {
			builder.append(days + " days ");
		}
		
		if (!hours.isBlank()) {
			builder.append(hours + " hours ");
		}
		
		if (!minutes.isBlank()) {
			builder.append(minutes + " minutes");
		}
		
		return builder.toString();
		
	}
	
	private boolean roleExists(GuildMessageReceivedEvent event) {
		try {
			event.getGuild().getRolesByName("Muted", false).get(0);
			return true;
		} catch (Exception e) {
			return false;
		}
	
	}
	
}