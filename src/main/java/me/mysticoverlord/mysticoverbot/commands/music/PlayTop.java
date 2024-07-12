package me.mysticoverlord.mysticoverbot.commands.music;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


import me.duncte123.botcommons.web.WebUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.music.PlayerManager;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayTop implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        AudioManager audio = event.getGuild().getAudioManager();
        GuildVoiceState voicestate = event.getMember().getVoiceState();
        Member selfMember = event.getGuild().getSelfMember();
        if (args.isEmpty()) {
            channel.sendMessage("Please provide a song argument").queue();
            return;
        }
        if (!audio.isConnected()) {
            if (!voicestate.inVoiceChannel()) {
                channel.sendMessage("Join a voice channel first!").queue();
                return;
            }
            VoiceChannel vc = voicestate.getChannel();
            if (!selfMember.hasPermission((GuildChannel)vc, Permission.VOICE_CONNECT)) {
                channel.sendMessage("I'm missing the permission to connect to " + vc.getName()).queue();
                return;
            }
            audio.openAudioConnection(vc);
        }
        String input = String.join((CharSequence)" ", args);
        if (isURL(input)) {
        	if (input.startsWith("https://www.youtube.com/watch?v=") || input.startsWith("https://youtu.be/")) {
            PlayerManager manager = PlayerManager.getInstance();
            
            manager.loadFromTop(event.getChannel(),event.getGuild(),  input, event.getAuthor().getId());
        	} else {
        		event.getChannel().sendMessage("The provided Url is not a valid youtube video Url!").queue();
        	}
        } else {
        channel.sendMessage("Searching for `" + input + "`").queue();
       
        WebUtils.ins.getJSONObject("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&q=" + input + "&type=video&key=AIzaSyAd_n5PX2TViv2neTk6y4xu-QDH1zFop48&fields=items(id)").async((json) -> {
            try {
            JsonNode item = json.get("items").get(0);
        	String id = item.get("id").get("videoId").asText();
            String video = "https://youtu.be/" + id;
            PlayerManager manager = PlayerManager.getInstance();
            
            manager.loadFromTop(event.getChannel(), event.getGuild(), video, event.getAuthor().getId());
        	} catch(NullPointerException e) {
        		event.getChannel().sendMessage("Search queries are currently being ratelimited by Youtube Data API v3!\nWe are currently looking for a replacement!\nIf you know any replacement feel free to put them in o!suggest or DM it to the Owner!\n\nIn the mean time please try and use youtube URL's instead").queue();
        	}
        }); 
        }


    }
     
    private boolean isURL(String url) {
    	try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }

    @Override
    public String getHelp() {
        return "Plays a song";
    }

    @Override
    public String getInvoke() {
        return "play";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<search query>`";
    }
}