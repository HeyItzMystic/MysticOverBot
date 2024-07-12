/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.music.GuildMusicManager;
import me.mysticoverlord.mysticoverbot.music.PlayerManager;
import me.mysticoverlord.mysticoverbot.objects.FormatUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.IMusic;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Queue
implements IMusic {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
      
       
        AudioPlayer player = musicManager.player;
        if (queue.isEmpty()  || queue.size() <= 0) {
            channel.sendMessage("The queue is empty!").queue();
            return;
        }
        double page;
        try {
        page = (double) Integer.parseInt(args.get(0));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
        	page = 1;
        }
        double a = 10;
        int pages = (int) Math.ceil(queue.size() / a);
        if (page > pages) {
        	channel.sendMessage("There aren't that many Pages!\nPlease request a page between 1 and " + pages).queue();
        	return;
        }
        int trackCount = queue.size();
        String string = "";
        ArrayList<AudioTrack> tracks = new ArrayList<AudioTrack>(queue);
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
        		.setTitle("Queue")
        		.setFooter("Page: " + page + ", Total: " + queue.size(), event.getAuthor().getEffectiveAvatarUrl());
     
        for (int i = (int)(page - 1) * 10; i < trackCount && i < page * 10; ++i) {
            AudioTrack track = (AudioTrack)tracks.get(i);
            AudioTrackInfo info = track.getInfo();
            String user;
            try {
            	user = (String) track.getUserData();
            } catch (NullPointerException e) {
            	user = "Not registered!";
            }
            try {
            string = String.format("`%s` - Requested By: %s\n", FormatUtil.formatTime(info.length), event.getJDA().getUserById(user).getAsMention());
            } catch (NumberFormatException e) {
            	string = String.format("`%s` - Requested By: %s\n", FormatUtil.formatTime(info.length), user);
            }
            builder.addField(String.valueOf(i + 1) + ". " + track.getInfo().title, string, false);
        }
        AudioTrackInfo in;
        String author;
        try {
        	in = player.getPlayingTrack().getInfo();
        	 try {
        	     author = event.getGuild().getMemberById((String)player.getPlayingTrack().getUserData()).getAsMention();
        	 } catch (Exception e) {
        	    author = player.getPlayingTrack().getUserData().toString();
        	 }
        	 builder.addField("**Now Playing**", String.valueOf(in.title) + "\n`" + FormatUtil.formatTime(in.length) + "` - Requested by: " + author, false);
        } catch (NullPointerException e) {
        	
        }
      
        channel.sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Displays current queue";
    }

    @Override
    public String getInvoke() {
        return "queue";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke();
    }

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "q";
	}
}

