package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MuteSetup implements ICommand {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		Member member = event.getMember();
		try {
		if (!member.hasPermission(Permission.MANAGE_ROLES)) {
			event.getChannel().sendMessage("You don't have the required permission: `Manage Roles`").queue();
			return;
		}
		} catch (Exception e) {
			
		}
		
		if (!event.getGuild().getMember(event.getJDA().getSelfUser()).hasPermission(Permission.MANAGE_ROLES)) {
			event.getChannel().sendMessage("I am missing the following permission: `Manage Roles`").queue();
			return;
		}
		
		
		if (roleExists(event) == true) {
			event.getChannel().sendMessage("A Muted role already exists!").queue();
			return;
		}
		
		List<TextChannel> channels = event.getGuild().getTextChannels();
		List<VoiceChannel> vc = event.getGuild().getVoiceChannels();
		double ping = (int) event.getJDA().getGatewayPing() / 100;
		int estimated = (int) Math.ceil((((vc.size() + channels.size()) / 30)) * ping) + 2;
		
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setDescription("Estimated Setup Time: " + estimated + " seconds");
		
		event.getChannel().sendMessage(builder.build()).queue();
		//int count = vc.size() + channels.size();
		event.getGuild().createRole().setName("Muted").setColor(0).setPermissions(Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY).queue();
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Role role = event.getGuild().getRolesByName("Muted", false).get(0);
		
		try {
		for (int x = 0; x < channels.size(); x++) {
			TextChannel channel = channels.get(x);
			try {
		    if (event.getGuild().getSelfMember().hasPermission(channel, Permission.MANAGE_PERMISSIONS) ) {
		    channel.createPermissionOverride(role).deny(Permission.MESSAGE_WRITE).queue();
		    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for (int x = 0; x < vc.size(); x++) {
			VoiceChannel channel = vc.get(x);
			try {
			    if (event.getGuild().getSelfMember().hasPermission(channel, Permission.MANAGE_PERMISSIONS) ) {
			channel.createPermissionOverride(role).deny(Permission.VOICE_SPEAK).queue();
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		builder.setDescription("Role " + role.getAsMention() + " has been setup!");
		event.getChannel().sendMessage(builder.build()).queue();
	
		} catch (Exception e) {
			e.printStackTrace();
			event.getChannel().sendMessage("Something went wrong while setting up the Role!\nPlease check if i have all needed permissions and try again!\n`Manage Roles`  `Manage Channels`").queue();
		}
			}
		

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Setup a \"Muted\" role if none exists already.";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "mutesetup";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: " + Constants.PREFIX + getInvoke();
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
