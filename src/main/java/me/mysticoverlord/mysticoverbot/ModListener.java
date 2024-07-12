package me.mysticoverlord.mysticoverbot;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.config.Config;
import me.mysticoverlord.mysticoverbot.database.SQLiteDataSource;
import me.mysticoverlord.mysticoverbot.objects.ModUtil;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;


import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModListener extends ListenerAdapter{
	

    Logger logger = LoggerFactory.getLogger(ModListener.class);
    Config config;
    
	public void onReady(ReadyEvent event) {    
        try {
			this.config = new Config(new File(Constants.configpath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Automod Operational!");
		
		Timer timer = new Timer();
		logger.info("Timer Created!");
	        timer.schedule(new TimerTask(){

	        	
	            @Override
	            public void run() {
	            	ArrayList<String> entries;
	            	try {
	            	entries = SQLiteUtil.getMutes(Instant.now().getEpochSecond());
	            	if (!entries.isEmpty() && entries != null) {
	            		
	            		for (int x = 0; x < entries.size(); x++) {
	            			String string = entries.get(x);
	            			int index = string.indexOf("-");
	    	            	
		            		String userId = string.substring(0, index);
		            		String guildId = string.substring(index + 1);
		            
		            		SQLiteUtil.clearMuted(guildId, userId);
		            		event.getJDA().getGuildById(guildId)
		            		.removeRoleFromMember(userId, event.getJDA()
		            				.getGuildById(guildId)
		            				.getRolesByName("Muted", true).get(0)).queue();
	            		}    	
	            	}
	            	} catch (NullPointerException e) {
	            	}
	           
	            	
	            	
	            }
	        }, 0L, 60000L); 
	        	
	        
	        Timer t = new Timer();
	        
	        t.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub	       

			            	SQLiteUtil.DeleteMessagesByTime();
				
				}
	        }, 0, 86400000);

		
	}
	
	
	
	public static Color RED = new Color(150, 0, 0);
	public static Color DARK_RED = new Color(100,0,0);
	public static Color YELLOW = new Color(150,150,0);
	public static Color DARK_YELLOW = new Color(150, 100, 0);
	public static Color GREEN = new Color(0, 150, 0);

    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
    	String messageId = event.getMessageId();
    	String guildId = event.getGuild().getId();
    	
    	if (SQLiteUtil.getBoolean(guildId, "message_log") == false) {
    		return;
    	}
    	
    	String entry = SQLiteUtil.getMessage(messageId);
    	
    
    	EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
    			.setTitle("Message Deleted")
    			.addField("In Channel", event.getChannel().getAsMention(), true);
    	if (entry != null) {
    	int index = entry.indexOf("-");
    	
    	String userId = entry.substring(0, index);
    	SQLiteUtil.deleteMessage(messageId);
    	builder.addField("Author", event.getJDA().getUserById(userId).getAsMention(), true);
    	
    	String message = entry.substring(index + 1);
    	
    	if (message.contains("https://discord.gg/") && SQLiteUtil.getBoolean(guildId, "antiinvite") == true) {
    		return;
    	}
    	
    	if (ModUtil.containsSwear(message) && SQLiteUtil.getBoolean(guildId, "antiinvite") == true) {
    		return;
    	}
    	
    	builder.addField("Message", message, false);
    	} else {
    		builder.addField("Message", "Unknown", false)
    		.addField("Author", "Unknown", false);
    	}
    	
    	builder.setFooter(event.getMessageId());
    	TextChannel channel = event.getGuild().getTextChannelsByName("log", true).get(0);
    	if (channel != null && event.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE)) {
    		channel.sendMessageEmbeds(builder.build()).timeout(10, TimeUnit.MILLISECONDS).queue();
    	}
    }
    
    @SuppressWarnings("unused")
	public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
    	if (event.getAuthor().isBot()) {
    		return;
    	}
    	String newmessage= event.getMessage().getContentDisplay();
    	String guildId = event.getGuild().getId();
    	String messageId = event.getMessageId();
    	String oldmessage = SQLiteUtil.getUpdatedMessage(messageId, newmessage);
    	if (SQLiteUtil.getBoolean(event.getGuild().getId(), "swear_filter")) {
    		if (ModUtil.containsSwear(event.getMessage().getContentDisplay()) && event.getGuild().getSelfMember().canInteract(event.getMember())) {
    			ModUtil.doWarn(event, "Swearing (Edited Message)", newmessage, oldmessage);
    			return;
    		}
    	}

    	
    	if (SQLiteUtil.getBoolean(guildId, "message_log") == false) {
    		return;
    	}
    	
    	
    	if (oldmessage.equals(newmessage)) {
    		return;
    	}
    	
    	EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
    			.setTitle("Edited Message")
    			.addField("In Channel", event.getChannel().getAsMention(), true)
    			.addField("Author", event.getAuthor().getAsMention(), true);
    	
    	if (oldmessage == null) {
    		builder.addField("Old Message", "Unknown", false);
    	} else {
    		builder.addField("Old Message", oldmessage, false);
    	}
    	
    	builder.addField("New Message", newmessage, false);
    	        
			try {
			event.getGuild().getTextChannelsByName("log", true).get(0).sendMessageEmbeds(builder.build()).queue();
			} catch (Exception e1) {
				
			}
		}
    
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
    	if (event.getAuthor().isBot()) {
    		return;
    	}
    	logMessages(event);
    	Antiinvite(event);
    	SwearFilter(event);
    }
	
	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
		String id = event.getGuild().getId();
		
		Boolean bool = SQLiteUtil.getBoolean(id, "nicklog");
                        if (bool == true) {
                        
                        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                        		.setColor(GREEN)
            					.setTitle("Nickname Change");
                       
                        if (event.getOldNickname() != null) {
                        	builder.addField("Old Nickname", event.getOldNickname().toString(), true);
                        } else {
                        	builder.addField("Old Nickname", event.getUser().getName(), true);
                        }
                        
                        if (event.getNewNickname() != null) {
                        	builder.addField("New Nickname", event.getNewNickname().toString(), true);
                        } else {
                        	builder.addField("New Nickname", event.getUser().getName(), true);
                        }
            					
            					builder.addField("Member ID + Mention", event.getUser().getId() + "(" + event.getUser().getAsMention() + ")", false);
                        

               			try {
                			event.getGuild().getTextChannelsByName("log", true).get(0).sendMessageEmbeds(builder.build()).queue();
                			} catch (Exception e1) {
                			}
                		}
    }
	
	public void onUserUpdateName(UserUpdateNameEvent event) {
		event.getUser().getMutualGuilds().forEach((guild) -> {
			String id = guild.getId();
			
			Boolean bool = SQLiteUtil.getBoolean(id, "nicklog");
	                        if (bool == true) {
	                        
	                        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
	                        		.setColor(GREEN)
	            					.setTitle("Username Change");
	                        	builder.addField("Old Username", event.getOldName(), true);
	                        
	                        	builder.addField("New Username", event.getNewName().toString(), true);	
	            					builder.addField("Member ID + Mention", event.getUser().getId() + "(" + event.getUser().getAsMention() + ")", false);
	                        

	               			try {
	                			guild.getTextChannelsByName("log", true).get(0).sendMessageEmbeds(builder.build()).queue();
	                			} catch (Exception e1) {
	                			}
	                		}
		});
	
    }

	
	public void Antiinvite(GuildMessageReceivedEvent event) {
		
    	if ((event.getMessage().getContentRaw().contains("https://discord.gg/") || !event.getMessage().getInvites().isEmpty()) && !event.getMember().isOwner()) {    		
		String id = event.getGuild().getId();
		
		if (!ModUtil.isFromThisGuild(event) && !event.getAuthor().isBot()) {
		
			Boolean bool= SQLiteUtil.getBoolean(id, "antiinvite");
                        if (bool == true && event.getGuild().getSelfMember().canInteract(event.getMember())) {
                        	
                        ModUtil.doWarn(event, "Unauthorized Invite");
                        }
		}
		}
    	
    	
    	}
    	
	
	
   public void SwearFilter(GuildMessageReceivedEvent event) {
    	if (SQLiteUtil.getBoolean(event.getGuild().getId(), "swear_filter")) {
    		if (!ModUtil.containsSwear(event.getMessage().getContentDisplay()) || !event.getGuild().getSelfMember().canInteract(event.getMember())) {
    			return;
    		}
    		
    		ModUtil.doWarn(event, "Swearing");
    	}

    }

    public void logMessages(GuildMessageReceivedEvent event) {
    	String guildId = event.getGuild().getId();
    	String userId = event.getAuthor().getId();
    	String messageId = event.getMessageId();
    	String message = event.getMessage().getContentDisplay();
    	
    	Boolean bool = SQLiteUtil.getBoolean(guildId, "message_log");
    	
    	if (event.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong() || message.length() == 0) {
    		return;
    		
    	}
    	
    	if (bool) {
    		SQLiteUtil.insertMessage(userId, messageId, message);
    	}
    }
   
         
   /* private void releasePreviousMutes(ReadyEvent event) {    	
    	try {
        	ArrayList<String> entries = new ArrayList<String>();
    		try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
        			// language=SQLite
        			.prepareStatement("SELECT guild_id, user_id FROM moderation WHERE mutedate NOT NULL")) {
		
        		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			    while (resultSet.next()) {
            				String user = resultSet.getString("user_id");
            				String guild = resultSet.getString("guild_id");        			
            				entries.add(user + "-" + guild);		
        			}

    			    resultSet.close();
        		}
        		
        	} catch (SQLException e) {
        	e.printStackTrace();
        	}
    	if (!entries.isEmpty()) {
        	String pattern = "yyyy-MM-dd-HH-mm";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        	Date date = new Date();
        	
    		
    		for (int x = 0; x < entries.size(); x++) {
    			String string = entries.get(x);
    			int index = string.indexOf("-");
            	
        		String userId = string.substring(0, index);
        		String guildId = string.substring(index + 1);
        		
        		String sentence = SQLiteUtil.getMuteFromUser(guildId, userId);
        		
        		if(sentence != null) {
        		Date sent = simpleDateFormat.parse(sentence);
        		
        		if (date.after(sent)) {
        			SQLiteUtil.clearMuted(guildId, userId);
        			event.getJDA().getGuildById(guildId).removeRoleFromMember(userId, event.getJDA().getGuildById(guildId).getRolesByName("Muted", true).get(0)).queue();
        			}
        		}
    		}    	
    	}
    	logger.info("Previous expired mutes have been unmuted!");
    	} catch (Exception e) {
    		logger.error("Something went wrong while releasing previous mutes!");
    		e.printStackTrace();
    	}
		
    }
*/

    
}
    

    

