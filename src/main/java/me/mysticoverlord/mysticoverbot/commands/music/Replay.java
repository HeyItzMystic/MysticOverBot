package me.mysticoverlord.mysticoverbot.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.music.GuildMusicManager;
import me.mysticoverlord.mysticoverbot.music.PlayerManager;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class Replay implements ICommand {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioManager audio = event.getGuild().getAudioManager();
        AudioPlayer player = musicManager.player;
        if (player.getPlayingTrack() == null) {
            channel.sendMessage("No song is currently being played").queue();
            return;
        }
        VoiceChannel vc = audio.getConnectedChannel();
        if (!vc.getMembers().contains(event.getMember())) {
            channel.sendMessage("You have to be in my voice channel to restart songs!").queue();
            return;
        }
        
        if (isDJ(event.getMember()) || event.getMember().hasPermission(Permission.MANAGE_SERVER) || vc.getMembers().stream().distinct().filter(u -> !u.getUser().isBot()).count() < 2) {
            musicManager.player.getPlayingTrack().setPosition(0);
            
            channel.sendMessage("Track Playback starting at 00:00").queue();
        } else {
        	channel.sendMessage("You can't restart the track!\nOnly people with a `DJ` role or `Manage Server` Permission can do that!\nBeing alone works too though!").queue();
        }
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Plays the current song from the beginning";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "replay";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return Constants.PREFIX + getInvoke();
	}
	
    private Boolean isDJ(Member member) {
    for (int x = 0; x < member.getRoles().size(); x++) {
    	if (member.getRoles().get(x).getName().equalsIgnoreCase("DJ")) {
    		return true;
    	}
    }
    	return false;
    }

}
