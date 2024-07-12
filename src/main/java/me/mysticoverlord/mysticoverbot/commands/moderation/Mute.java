package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.awt.Color;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Mute implements ICommand {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub

		Color color = new Color(255, 255, 0);
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        Role role = event.getGuild().getRolesByName("Muted", false).get(0);    
		
		
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
        try {
         reason = String.join((CharSequence)" ", args.subList(1, args.size()));   
        } catch (Exception e) {
        	reason = "not specified";
        }
        
        
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
		
		if (!roleExists(event) == true) {
			channel.sendMessage("No \"Muted\" role available!\nTo use this command create a \"Muted\" role or use o!mutesetup to make me do it for you!").queue();
			return;
		}
		
        event.getGuild().addRoleToMember(target, role).queue();
        
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
        		.setTitle("Muted " + target.getUser().getAsTag() + "!")
        		.setColor(color)
        		.addField("Moderator", member.getAsMention(), true)
        		.addField("Reason", reason, true);
        
        event.getChannel().sendMessage(builder.build()).queue();
        
		
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Mutes specified user";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "mute";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return Constants.PREFIX + getInvoke() + " <@user> [reason]" ;
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
