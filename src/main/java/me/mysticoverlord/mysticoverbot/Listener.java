/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.config.Config;
import me.mysticoverlord.mysticoverbot.music.PlayerManager;
import me.mysticoverlord.mysticoverbot.objects.FormatUtil;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDA.Status;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener
extends ListenerAdapter {
    CommandManager manager;
    Logger logger = LoggerFactory.getLogger(Listener.class);
    GiveawayManager gmanager;
    Thread gthread;

    public Listener(CommandManager manager) {
        this.manager = manager;
    }
   
    

	@Override
    public void onReady(ReadyEvent event) {
    	logger.info("initiating error log");
    	Constants.channel = event.getJDA().getTextChannelById(731531369137700885L);    	
    	logger.info("error log initiated");
    	Constants.channel.sendMessage("Bot starting up!\nError log tied to this channel!").queue();
    	
    	event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE); 	
    	Boolean bool = false;
		Config config = Config.getInstance();
		bool = config.getBoolean("isUpdated");
	 this.gmanager =  new GiveawayManager(event.getJDA());
     this.gthread = new Thread(this.gmanager);
     if (bool) {
    	 config.put("isUpdated", false);
    	 SQLiteUtil.deleteIds();
     }
		
        logger.info(String.format("Logged in as %#s\n", event.getJDA().getSelfUser()));
        event.getJDA().getPresence().setActivity(Activity.playing("Preparing for relaunch"));
 /*      new Thread(new GiveawayManager(event.getJDA()), "GiveawayManager").start();
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){

            @Override
            public void run() {
               event.getJDA().getPresence().setActivity(Activity.watching(event.getJDA().getGuilds().size() + " Servers | o!help"));
                Constants.api.setStats(event.getJDA().getGuilds().size());
                try {
                    TimeUnit.SECONDS.sleep(30L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                event.getJDA().getPresence().setActivity(Activity.watching(Constants.VERSION));
                Constants.api.setStats(event.getJDA().getGuilds().size());
            }
        }, 0L, 60000L);
        this.gthread.start(); */
    }

	
	@Override
    public void onGuildJoin(GuildJoinEvent event) throws NullPointerException{
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
            		.setThumbnail(event.getJDA().getSelfUser()
            		.getEffectiveAvatarUrl())
            		.setTitle("Greetings!")
            		.setDescription("I am MysticOverBot!\n\n"
            				+ "To get started, send ``o!help`` for a list of commands!\n\n"
            				+ "If you have any suggestions to make me better send ``o!suggest <suggestion>``!")
            		.addField("Official Hideout", "https://discord.gg/kUVCU7z", true)
            		.addField("Creator", "MysticOverlord#7697", true)
            		.addField("Vote if you like me", FormatUtil.embedLink("https://top.gg/bot/576061124043210759/vote", "top.gg"), true)
            		.setFooter("Enjoy the Commands!", null);
            
            if (event.getGuild().getSystemChannel() != null && event.getGuild().getSystemChannel().canTalk()) {
                event.getGuild().getSystemChannel().sendMessageEmbeds(builder.build()).queue();
                return;
            } else if (event.getGuild().getDefaultChannel() != null && event.getGuild().getDefaultChannel().canTalk()){
                event.getGuild().getDefaultChannel().sendMessageEmbeds(builder.build()).queue();
                return;
            } else {
            	int x = 0;
            	while (x <= event.getGuild().getTextChannels().size()) {
            		if (event.getGuild().getTextChannels().get(x).canTalk()) {
                		event.getGuild().getTextChannels().get(x).sendMessageEmbeds(builder.build()).queue();
            			break;
            	}
            		x += 1;
            }

        	}
            

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)  {	
        User author = event.getAuthor();
        Message message = event.getMessage();
        String content = message.getContentDisplay();
        if (event.isFromType(ChannelType.TEXT)) {
            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            logger.info(String.format("(%s)[%s]<%#s>: %s\n", guild.getName(), textChannel.getName(), author, content));           
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            logger.info(String.format("[PRIV]<%#s>: %s", author, content));
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
    	if (event.getAuthor().isBot() || !event.getChannel().canTalk()) {
    		return;
    	}
    	
        String raw = event.getMessage().getContentRaw();     

        if (event.getJDA().getStatus() == Status.CONNECTED) {
        if (raw.equalsIgnoreCase(String.valueOf(Constants.PREFIX) + "shutdown") && event.getAuthor().getIdLong() == 394066807926947850L) {
            event.getChannel().sendMessage("shutting down...").queue();
            this.shutdown(event.getJDA());
            return;
        }
    
        String id = event.getGuild().getId();

       String prefix = SQLiteUtil.getPrefix(id);
        
        if (!event.getMessage().isWebhookMessage() && (raw.toLowerCase().startsWith(prefix.toLowerCase()))) {
        	if (event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_WRITE)) {
                this.manager.handleCommand(event, prefix);
        	}
        }
    }
    }
    
    public void onGuildLeave(GuildLeaveEvent event) {
    	String id = event.getGuild().getId();
    	
    	SQLiteUtil.deleteAllFromGuild(id);
      }
    


    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
    	 AudioManager audio = event.getGuild().getAudioManager();
         GuildVoiceState voicestate = event.getGuild().getSelfMember().getVoiceState();
         if (audio.isConnected()) {
             if (voicestate.inVoiceChannel()) {
            	 VoiceChannel vc = voicestate.getChannel();
            	 if (vc.getMembers().size() == 1) {
            		 Timer timer = new Timer();
            	        timer.schedule(new TimerTask(){

            	        	int x = 0;
            	            @Override
            	            public void run() {
            	            	
            	            	if (vc.getMembers().size() > 1) {
        	            			timer.cancel();
        	            			return;
        	            	    }

            	            	if (x == 10) {
        	            			audio.closeAudioConnection();
        	            			Constants.loop.replace(event.getGuild().getIdLong(), false);
        	            			Constants.loopqueue.replace(event.getGuild().getIdLong(), false);
                                    PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).scheduler.getQueue().clear();
                                    PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).scheduler.nextTrack();
                                    timer.cancel();
        	            		}
            	            	
            	            	x += 1;
            	            }
            	        }, 0L, 30000L);     
            	 }
             }
         }
    }
    
	@SuppressWarnings("deprecation")
	private void shutdown(JDA jda) {
		this.gmanager.shutdownManager();
	    if (this.gthread.isAlive()) {
		    this.gthread.stop();
	    }
	    jda.shutdown();

	   System.exit(0);
	}

}

