package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.util.ArrayList;
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

public class Open implements ICommand {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		Member member = event.getMember();
		ArrayList<Role> roles = new ArrayList<Role>();
		try {
		if (!member.hasPermission(Permission.ADMINISTRATOR)) {
			event.getChannel().sendMessage("You don't have the required permission: `Administrator`").queue();
			return;
		}
		} catch (Exception e) {
			event.getChannel().sendMessage("Failed to check for the permission: `Administrator`\nIf this problem continues please contact the Owner!").queue();
			return;
		}
		
		if (event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
			event.getChannel().sendMessage("I don't have the required permission: `Administrator`").queue();
		}

		event.getGuild().getRoles().forEach((role) -> {
			try {
			if (event.getGuild().getSelfMember().canInteract(role)) {
				roles.add(role);
			}
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		List<TextChannel> channels = event.getGuild().getTextChannels();
		List<VoiceChannel> vc = event.getGuild().getVoiceChannels();
		double ping = (int) event.getJDA().getGatewayPing() / 100;
		int estimated = (int) Math.ceil((((vc.size() + channels.size()) / 30) * roles.size()) * ping) + 2;
		
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setDescription("Estimated Time Needed: " + estimated + " seconds");
		event.getGuild().getRoles().forEach((role) -> {
			if (event.getGuild().getSelfMember().canInteract(role)) {
				try {
					for (int x = 0; x < channels.size(); x++) {
						TextChannel channel = channels.get(x);
					
						channel.putPermissionOverride(role).clear(Permission.ALL_CHANNEL_PERMISSIONS).queue();

					}
					
					for (int x = 0; x < vc.size(); x++) {
						VoiceChannel channel = vc.get(x);
						try {
							try {
							if (channel.getParent().getPermissionOverride(event.getGuild().getPublicRole()).getDenied().contains(Permission.MESSAGE_WRITE)) {
								continue;
							} } catch (Exception e) {
								
							}
						channel.putPermissionOverride(role).clear(Permission.ALL_CHANNEL_PERMISSIONS).queue();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					builder.setDescription("Server has Opened!\nStaff channels have possibly been opened!");
					event.getChannel().sendMessage(builder.build()).queue();
				
					} catch (Exception e) {
						e.printStackTrace();
						event.getChannel().sendMessage("Something went wrong while opening the server!\nPlease check if i have all needed permissions and try again!\n`Administrator`").queue();
					}
			}
		});

	
		
		event.getChannel().sendMessage(builder.build()).queue();

		
			}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Opens the entire server";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "open";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: " + Constants.PREFIX + getInvoke();
	}

}

