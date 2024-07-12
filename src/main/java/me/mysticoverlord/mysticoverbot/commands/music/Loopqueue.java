package me.mysticoverlord.mysticoverbot.commands.music;

import java.util.List;

import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.IMusic;
import me.mysticoverlord.mysticoverbot.objects.MusicUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class Loopqueue implements IMusic {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		TextChannel channel = event.getChannel();
		AudioManager audio = event.getGuild().getAudioManager();
        VoiceChannel vc = audio.getConnectedChannel();
        if (!vc.getMembers().contains(event.getMember())) {
            channel.sendMessage("You have to be in my voice channel to loop queue!").queue();
            return;
        }
        
        
		if (MusicUtil.isDJ(event.getMember()) || event.getMember().hasPermission(Permission.MANAGE_SERVER) || vc.getMembers().stream().distinct().filter(u -> !u.getUser().isBot()).count() < 2) {
			if (!Constants.loopqueue.containsKey(event.getGuild().getIdLong())) {
				Constants.loopqueue.put(event.getGuild().getIdLong(), false);
			}
			
			if (!Constants.loopqueue.get(event.getGuild().getIdLong())) {
				Constants.loopqueue.replace(event.getGuild().getIdLong(), true);
				if (Constants.loop.containsKey(event.getGuild().getIdLong())) {
					Constants.loop.replace(event.getGuild().getIdLong(), false);
				}
	             channel.sendMessage(":repeat: Queue Looped!").queue();
			} else {
				Constants.loopqueue.replace(event.getGuild().getIdLong(), false);
	             channel.sendMessage(":arrow_forward: Queue no longer Looped!").queue();
			}
			
        } else {
        	channel.sendMessage("You can't loop queue!\nOnly people with a `DJ` role or `Manage Server` Permission can do that\nBeing alone works too though!").queue();
        }
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Loops the current queue!\n" + getUsage();
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "loopqueue";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: " + Constants.PREFIX + getInvoke();
	}

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "lq";
	}

}
