package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.awt.Color;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UnMute implements ICommand {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub

		Color color = new Color(255, 255, 0);
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        Role role = event.getGuild().getRolesByName("Muted", false).get(0);
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();    
		
		
        if (args.isEmpty() || mentionedMembers.isEmpty()) {
            channel.sendMessage("Please specify the targeted user!\n" + this.getUsage()).queue();
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
			channel.sendMessage("You can't unmute members higher than or equal to your rank!").queue();
			return;
		}
		
		if (!selfMember.canInteract(target)) {
			channel.sendMessage("I can't unmute members higher than or equal to my own rank!").queue();
			return;
		}
		
		if (!roleExists(event) == true) {
			channel.sendMessage("No \"Muted\" role available!\nTo use this command create a \"Muted\" role or use o!mutesetup to make me do it for you!").queue();
			return;
		}
		
		if (!target.getRoles().contains(event.getGuild().getRolesByName("Muted", false).get(0))) {
		
			event.getChannel().sendMessage("The user isn't muted!").queue();
			return;
		}
		if (SQLiteUtil.getMuteFromUser(event.getGuild().getId(), target.getId()) != null) {
			SQLiteUtil.clearMuted(event.getGuild().getId(), event.getAuthor().getId());
		}
		
        event.getGuild().removeRoleFromMember(target, role).queue();
        
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
        		.setTitle("UnMuted " + target.getUser().getAsTag() + "!")
        		.setColor(color)
        		.addField("Moderator", member.getAsMention(), true);
        
        event.getChannel().sendMessage(builder.build()).queue();
        
		
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "unmute";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
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