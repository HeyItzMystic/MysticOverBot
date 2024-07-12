/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.music;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import me.duncte123.botcommons.web.WebUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.Main;
import me.mysticoverlord.mysticoverbot.music.PlayerManager;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.IMusic;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class Play implements IMusic {
    @SuppressWarnings("unchecked")
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
        
        VoiceChannel vc;
        if (!audio.isConnected()) {
            if (!voicestate.inVoiceChannel()) {
                channel.sendMessage("Join a voice channel first!").queue();
                return;
            }
            vc = voicestate.getChannel();
            if (!selfMember.hasPermission((GuildChannel)vc, Permission.VOICE_CONNECT)) {
                channel.sendMessage("I'm missing the permission to connect to " + vc.getName()).queue();
                return;
            }
            audio.openAudioConnection(vc);
        } else {
         vc = audio.getConnectedChannel();
        }

        
        
        
        if (!vc.getMembers().contains(event.getMember())) {
            channel.sendMessage("You have to be in my voice channel to play songs!").queue();
            return;
        }
        
        String input = String.join((CharSequence)" ", args);
        if (isURL(input)) {
        	
        	if (input.startsWith("https://www.youtube.com/watch?v=") || input.startsWith("https://youtu.be/")) {
            PlayerManager manager = PlayerManager.getInstance();
            
            
           manager.loadAndPlay(event.getChannel(), event.getGuild(), input, event.getAuthor().getId());
        	} else {
        		event.getChannel().sendMessage("The provided Url is not a valid youtube video Url!").queue();
        	}
        	
        } else {
        channel.sendMessage("Searching for `" + input + "` on youtube!").queue();
       
        WebUtils.ins.getJSONObject("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&q=" + input + "&type=video&key=" + Main.config.getString("youtube") + "&fields=items(id/videoId)").async((json) -> {
            try {
            	JsonNode item;
            	if (json.get("items").size() > 0) {         		
            			item = json.get("items").get(0);
            	} else {
            		event.getChannel().sendMessage("Your search query yielded no results").queue();
            		return;
            	}
	        	String id = item.get("id").get("videoId").asText();
	            String video = "https://youtu.be/" + id;
	            PlayerManager manager = PlayerManager.getInstance();
	              
	            manager.loadAndPlay(event.getChannel(), event.getGuild(), video, event.getAuthor().getId());
            
        	} catch(NullPointerException e) {
        		event.getChannel().sendMessage("Search queries are currently being ratelimited by Youtube Data API v3!\nSearch queries can be processed again at 00:00 Pacific time\nIn the mean time please try and use youtube URL's instead").queue();
        	}
        });		
        }

        File file = new File("/home/pi/Bot/PlayUsage.json");
		try {
	        if (!file.exists()) {
	        	try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
			FileReader filer = new FileReader(file);
			JSONParser json = new JSONParser();
			Object object1 = json.parse(filer);
            JSONObject obj = (JSONObject)object1;
            int count;
            try {
            	count = Integer.parseInt(obj.get("count").toString());
            } catch (NullPointerException e) {
            	count = 0;
            }
            count = count + 1;
            JSONObject object = new JSONObject();
            object.put("count", String.valueOf(count));
            FileWriter filew = new FileWriter(file);
                        filew.write(object.toJSONString());
                        filew.close();
                    

            
        } catch (Exception e) {
        	e.printStackTrace();
        }
		
        }
    
     
    private boolean isURL(String url) {
    	try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			
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


	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "p";
	}
}

