package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.util.List;


import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Warnings implements ICommand {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub

		Member member = event.getMember();
		Member selfmember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
	
		if (args.isEmpty()) {
			event.getChannel().sendMessage("Missing Arguments: `@user` `action`").queue();
			event.getChannel().sendMessage("Actions include:\nshow: shows their current warnings\nset: sets their new warning count\nclear: sets their warnings back to 0").queue();
			return;
		} 
        Member target = FilterUtil.getMemberByData(args.get(0), event, 0);
        
        if (target == null) {
            event.getChannel().sendMessage("ID or Mention had no match!").queue();
            return;
        }
		
		if (args.size() < 2) {
			event.getChannel().sendMessage("Missing Arguments: `action`").queue();
			event.getChannel().sendMessage("Actions include:\nshow: shows their current warnings\nset: sets their new warning count/nclear: sets their warnings back to 0").queue();
			return;
		}
		
		if (!member.hasPermission(Permission.MANAGE_ROLES, Permission.KICK_MEMBERS, Permission.BAN_MEMBERS)){
			event.getChannel().sendMessage("You need following permissions to use this command:\n`Manage Roles, Kick Members, Ban Members`").queue();
			return;
		}
		
		if (!selfmember.hasPermission(Permission.MANAGE_ROLES, Permission.KICK_MEMBERS, Permission.BAN_MEMBERS)){
			event.getChannel().sendMessage("I need following permissions to run this command:\n`Manage Roles, Kick Members, Ban Members`").queue();
			return;
		}
		
		if (mentionedMembers.isEmpty()) {
        	event.getChannel().sendMessage("Please mention a user\n" +  this.getUsage()).queue();
        	return;
        }
        
        try  {
        if (args.get(1).equals("clear")) {
            if (!member.canInteract(target)) {
            	event.getChannel().sendMessage("You can't remove warnings from anyone above or equal to your role!").queue();
            	return;
            }
        	String guildId = event.getGuild().getId();
        	String userId = target.getId();
        	SQLiteUtil.updateWarnings(guildId, userId, 0, event.getChannel());
        	event.getChannel().sendMessage("Removed all warnings from " + target.getAsMention()).queue();
        	return;
        	
        } else if (args.get(1).equals("show")) {
        	
        	int warnings = SQLiteUtil.getWarnings(event.getGuild().getId(), target.getId());
    		
    		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
    				.setTitle(target.getUser().getAsTag() + "'s Warnings")
    				.setDescription("Currently has " + warnings + " warnings");
    		
    		event.getChannel().sendMessage(builder.build()).queue();
        } else if (args.get(1).equals("set")) {
        	if (args.size() < 3) {
        		event.getChannel().sendMessage("No number has been specified!").queue();
        	    return;
        	}
        	
        	try {
        	   int warnings = Integer.parseInt(args.get(2));
        	   
        	   SQLiteUtil.updateWarnings(event.getGuild().getId(), target.getId(), warnings, event.getChannel());
        	   event.getChannel().sendMessage("Warnings for " + target.getAsMention() + " has been set to: " + warnings).queue();
        	    
        	} catch (NumberFormatException e) {
        		event.getChannel().sendMessage("The given argument is not a number!").queue();
        		return;
        	}
        } else {
        	event.getChannel().sendMessage("The given `action` argument is invalid!").queue();
        }
        
        } catch (IndexOutOfBoundsException e) {
        	
        }
	
		
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Views how many warnings a member has, sets his warnings or removes all his warnings";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "warnings";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return Constants.PREFIX + getInvoke() + " <@user> <show/clear/set>";
	}

}
