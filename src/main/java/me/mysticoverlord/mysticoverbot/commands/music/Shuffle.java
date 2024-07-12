package me.mysticoverlord.mysticoverbot.commands.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.music.GuildMusicManager;
import me.mysticoverlord.mysticoverbot.music.PlayerManager;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.IMusic;
import me.mysticoverlord.mysticoverbot.objects.MusicUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class Shuffle implements IMusic {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        AudioManager audio = event.getGuild().getAudioManager();        
        VoiceChannel vc = audio.getConnectedChannel();
       
        AudioPlayer player = musicManager.player;
        
        if (!vc.getMembers().contains(event.getMember())) {
            channel.sendMessage("You have to be in my voice channel to remove songs!").queue();
            return;
        }
        if (player.getPlayingTrack().equals(null)) {
        	channel.sendMessage("No song is currently being played").queue();
        	return;
        }
        AudioTrackInfo in = player.getPlayingTrack().getInfo();
        if (queue.isEmpty()) {
            channel.sendMessage("The queue is empty!").queue();
            return;
        }
        
        if (MusicUtil.isDJ(event.getMember()) || event.getMember().hasPermission(Permission.MANAGE_SERVER) || vc.getMembers().stream().distinct().filter(u -> !u.getUser().isBot()).count() < 2) {
        	ArrayList<AudioTrack> tracks = new ArrayList<AudioTrack>(queue);
        	Collections.shuffle(tracks);
        	
        	musicManager.scheduler.getQueue().clear();
        	event.getChannel().sendMessage("Shuffling...").queue();
        	tracks.forEach((track) -> {
        		playerManager.loadAndPlay(event.getGuild(), track.getInfo().uri, (String)track.getUserData());
        	});
        	
        	try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	event.getChannel().sendMessage("Queue Shuffled! :twisted_rightwards_arrows: ").queue();
        } else {
        	channel.sendMessage("You can't shuffle the queue!\nOnly people with a `DJ` role or `Manage Server` Permission can do that\nBeing alone works too though!").queue();
            
        }
        
	}


	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Shuffles the Current Queue";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "shuffle";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage " + Constants.PREFIX + getInvoke();
	}


	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "sh";
	}

}
