package me.mysticoverlord.mysticoverbot.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.slf4j.LoggerFactory;

import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.database.SQLiteDataSource;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SQLiteUtil {
	
	public static Encryption encryption;

    public static Boolean getBoolean(String guildId, String setting) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("SELECT "+ setting + " FROM guild_settings WHERE guild_id = ?")) {
    		
    		preparedStatement.setString(1, guildId);
    		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			if (resultSet.next()) {
    				Boolean bool = resultSet.getBoolean(setting);
    				return bool;
    			}
    		}
    		
    	} catch (SQLException e) {
			ExceptionHandler.handle(e);
    	}
    	return false;
    }   
    
    public static void updateBoolean(String guildId, String setting, Boolean bool, GuildMessageReceivedEvent event) {
    	
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("UPDATE guild_settings SET "+ setting + " = ? WHERE guild_id = ?")) {
    		preparedStatement.setBoolean(1, bool);
    		preparedStatement.setString(2, guildId);
    		
    		preparedStatement.executeUpdate();
    		
    		
    	} catch (SQLException e) {
    		event.getChannel().sendMessage("Failed to update ``" + setting + "``!\nIf this problem persists pleas contact the Owner at my hideout or send a bug report with o!reportbug").queue();
			ExceptionHandler.handle(e);
    	}
    	
    }
    
    public static void updateWarnings(String guildId, String userId, int num, TextChannel channel) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("UPDATE moderation SET warnings = ? WHERE guild_id = ? AND user_Id = ?")) {
    		preparedStatement.setInt(1, num);
    		preparedStatement.setString(2, guildId);
    		preparedStatement.setString(3, userId);
    		
    		preparedStatement.executeUpdate();
    		
    		
    	} catch (SQLException e) {
    		channel.sendMessage("Failed to update ``warnings``!\nIf this problem persists pleas contact the Owner at my hideout or send a bug report with o!reportbug").queue();
			ExceptionHandler.handle(e);
    	}
    }
    
    public static Integer getWarnings(String guildId, String userId) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("SELECT warnings FROM moderation WHERE guild_id = ? AND user_id = ?")) {
    		
    		preparedStatement.setString(1, guildId);
    		preparedStatement.setString(2, userId);
    		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			if (resultSet.next()) {
    				Integer num = resultSet.getInt("warnings");
    				return num;
    			}
    		}
    		
    		try (Connection con1 = SQLiteDataSource.getConnection();final PreparedStatement insertStatement = con1
                    // language=SQLite
                    .prepareStatement("INSERT INTO moderation(guild_id,user_id) VALUES(?,?)")) {
    			
    			insertStatement.setString(1, guildId);
    			insertStatement.setString(2, userId);
    			
    			insertStatement.execute();
    			
    			
    		}
    		
    	} catch (SQLException e) {
			ExceptionHandler.handle(e);
    	}
    	return 0;
    }
    
   public static String getMuteFromUser(String guildId, String userId) {
   	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("SELECT mutedate FROM moderation WHERE guild_id = ? AND user_id = ?")) {
    		
    		preparedStatement.setString(1, guildId);
    		preparedStatement.setString(2, userId);
    		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			if (resultSet.next()) {
    				String date = resultSet.getString("mutedate");
    				return date;
    			}
    		}
    		
    	} catch (SQLException e) {
			ExceptionHandler.handle(e);
    	}
    	return null;
    } 
    
    
    public static ArrayList<String> getMutes(Long epoch) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("SELECT guild_id, user_id FROM moderation WHERE mutedate <= ?")) {

    		preparedStatement.setLong(1, epoch);
    		//TODO convert to epoch
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
			    ArrayList<String> entries = new ArrayList<String>();
			    while (resultSet.next()) {
        				String user =  resultSet.getString("user_id");
        				String guild =  resultSet.getString("guild_id");
        			
        				entries.add(user + "-" + guild);
    				
    			}
			    return entries;
    		}
    		
    	} catch (SQLException e) {
			ExceptionHandler.handle(e);
    	}
    	return null;
    }
    
    public static void updateMuted(String guildId, String userId, String date, TextChannel channel) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("UPDATE moderation SET mutedate = ? WHERE guild_id = ? AND user_Id = ?")) {
    		preparedStatement.setString(1, date);
    		preparedStatement.setString(2,  guildId);
    		preparedStatement.setString(3,  userId);
    		
    		preparedStatement.executeUpdate();
    		
    	} catch (SQLException e) {
    		channel.sendMessage("Failed to set a mute date!\nThis member is now muted indefinetely!\nIf this problem persists please contact the Owner at my hideout or send a bug report with o!reportbug").queue();
			ExceptionHandler.handle(e);
    	}
    }
    
    public static void clearMuted(String guildId, String userId) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("UPDATE moderation SET mutedate = ? WHERE guild_id = ? AND user_Id = ?")) {
    		preparedStatement.setNull(1, java.sql.Types.NULL);
    		preparedStatement.setString(2,  guildId);
    		preparedStatement.setString(3,  userId);
    		
    		preparedStatement.executeUpdate();
    		
    	} catch (SQLException e) {
			ExceptionHandler.handle(e);
    	}
    }
    
    public static void deleteUserFromModeration(String userId, String guildId) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
               .prepareStatement("DELETE FROM moderation WHERE guild_id = ? AND user_Id = ?")) {
    		preparedStatement.setString(1,  guildId);
   		preparedStatement.setString(2,  userId);
    		
    		preparedStatement.executeUpdate();
    		
    	} catch (SQLException e) {
			ExceptionHandler.handle(e);
    	}
    }
    
    public static void deleteAllFromGuild(String guildId) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("DELETE FROM moderation WHERE guild_id = ?")) {
    		preparedStatement.setString(1,  guildId);
    		
    		preparedStatement.executeUpdate();

    		LoggerFactory.getLogger(SQLiteUtil.class).debug("Moderation Data from Guild " + guildId + " has been deleted!");
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
    		LoggerFactory.getLogger(SQLiteUtil.class).error("Something went wrong deleting Mod Data form " + guildId +"!");
			ExceptionHandler.handle(e);
		} 
    	
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("DELETE FROM guild_settings WHERE guild_id = ?")) {
    		
    		preparedStatement.setString(1, guildId);
    		
    		preparedStatement.executeUpdate();

    		LoggerFactory.getLogger(SQLiteUtil.class).debug("Settings from Guild " + guildId + " has been deleted!");
    			}
    	catch (SQLException e) {
    		LoggerFactory.getLogger(SQLiteUtil.class).error("Something went wrong deleting Guild_Settings from guild_id " + guildId + "!");
			ExceptionHandler.handle(e);
    	}
    	
    }
    
    public static void insertMessage(String userId, String messageId, String message) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement insertStatement = con
                // language=SQLite
                .prepareStatement("INSERT INTO messages(message_id,author_id,message,timestamp) VALUES(?,?,?,?)")) {

		    Date date = new Date();
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		    date = format.parse(FormatUtil.dateCalculator("14", "0", "0"));
		    format = new SimpleDateFormat("yyyy-MM-dd");
		    date = format.parse(format.format(date));
		    
			insertStatement.setString(1,  messageId);
			insertStatement.setString(2,  userId);
			insertStatement.setString(3,  encryption.encrypt(message));
			insertStatement.setString(4, date.toString());
			
			insertStatement.execute();
			
			
			
		} catch (SQLException e) {
			ExceptionHandler.handle(e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			ExceptionHandler.handle(e);
		}
    }
    
    public static ArrayList<String> DeleteMessagesByTime() {
    	ArrayList<String> result = new ArrayList<String>();
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("Delete FROM messages WHERE timestamp = ?")) {
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    Date date = format.parse(format.format(new Date()));
    		preparedStatement.setString(1, date.toString());
    		preparedStatement.executeUpdate();
    	    LoggerFactory.getLogger(SQLiteUtil.class).info("Messages deleted");
    	
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			ExceptionHandler.handle(e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			ExceptionHandler.handle(e);
		}
    	
		return result;
    }
    
    public static String getMessage(String messageId) {
    	String result = null;
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("SELECT author_id, message FROM messages WHERE message_Id = ?")) {

    		preparedStatement.setString(1,  messageId);
    	
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
			    if (resultSet.next()) {
        				String user =  resultSet.getString("author_id");
        				String message =  encryption.decrypt(resultSet.getString("message"));
        			
        				result = (user + "-" + message);
    				
        			    
    			}
    		}
    	
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			ExceptionHandler.handle(e);
		}
		return result;
    }
    
    public static void deleteMessage(String messageId) {
    	try (Connection con1 = SQLiteDataSource.getConnection();final PreparedStatement deleteStatement = con1
                // language=SQLite
                .prepareStatement("DELETE FROM messages WHERE message_id = ?")) {
    		
    		deleteStatement.setString(1,  messageId);
    		
    		deleteStatement.execute();
    		
    			} catch (SQLException e) {
    				ExceptionHandler.handle(e);
					}
    }
    
    public static String getUpdatedMessage(String messageId, String message) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("SELECT message FROM messages WHERE message_Id = ?")) {
    		preparedStatement.setString(1, messageId);
    		
    		String result = null;
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
			    if (resultSet.next()) {
        				String oldmessage =  encryption.decrypt(resultSet.getString("message"));
        			   result = oldmessage;
    			}
    		}
    		
    		try (Connection con1 = SQLiteDataSource.getConnection();final PreparedStatement updateStatement = con1
                    // language=SQLite
                    .prepareStatement("UPDATE messages SET message = ? WHERE message_Id = ?")) {
        		
    			updateStatement.setString(1,  encryption.encrypt(message));
        		updateStatement.setString(2,  messageId);
        		
        		updateStatement.executeUpdate();

        			}
    		return result;
    	} catch (SQLException e) {
			ExceptionHandler.handle(e);
    	}
    return null;
    }
    
    public static String getPrefix(String guildId) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?");) {
    		preparedStatement.setString(1, guildId);
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			if (resultSet.next()) {
    				String str = resultSet.getString("prefix");
    				return str;
    			}
    		}
    		
    		try (Connection con1 = SQLiteDataSource.getConnection();final PreparedStatement insertStatement = con1
                    // language=SQLite
                    .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {
    			
    			insertStatement.setString(1, guildId);
    			insertStatement.execute();
    			
    			
    		}
    		
    	} catch (SQLException e) {
			ExceptionHandler.handle(e);
    	}
    	return Constants.PREFIX;
    }

      public static void deleteIds() {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                // language=SQLite
                .prepareStatement("DELETE FROM update_log")) {
    		preparedStatement.execute();

    		preparedStatement.getConnection().close();
    	} catch (SQLException e) {
			ExceptionHandler.handle(e);
    	}
    }
      
      public static void updatePrefix(String guildId, String prefix, GuildMessageReceivedEvent event) {
      	
      	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {
      		preparedStatement.setString(1, prefix);
      		preparedStatement.setString(2, guildId);
      		
      		preparedStatement.executeUpdate();
      		
      	} catch (SQLException e) {
      		event.getChannel().sendMessage("Failed to update the Prefix!\nThe Owner has been notified of this error!\nIf this problem persists pleas contact the Owner at my hideout or send a bug report with o!reportbug").queue();
			ExceptionHandler.handle(e);
      	}
      	
      }
      
    public static void insertGiveaway(String channelId, String messageId, long timestamp, GuildMessageReceivedEvent event) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("INSERT INTO giveaways(channel_id,message_id,timestamp) VALUES(?,?,?)")) {
      		preparedStatement.setString(1, channelId);
      		preparedStatement.setString(2, messageId);
      		preparedStatement.setLong(3, timestamp);
      		preparedStatement.executeUpdate();
      		
      	} catch (SQLException e) {
      		event.getChannel().sendMessage("Failed to Start Giveaway!\nThe Owner has been notified of this error!\nIf this problem persists pleas contact the Owner at my hideout or send a bug report with o!reportbug").queue();
			ExceptionHandler.handle(e);
      	}
    }
    
    public static HashMap<String, Long> retrieveGiveaways() {
		HashMap<String, Long> map = new HashMap<String, Long>();
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("SELECT timestamp, message_id, channel_id FROM giveaways")) {
      		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			while (resultSet.next()) {
    				long date = resultSet.getLong("timestamp");
    				String messageId = resultSet.getString("message_id");
    				String channelId = resultSet.getString("channel_id");
    				map.put(channelId + "-" + messageId, date);
    			}
    		}
      		
      	} catch (SQLException e) {
      		ExceptionHandler.handle(e);
      	}
		return map;
    }
    
    public static HashMap<String, Long> getBefore(long epoch) {
    	HashMap<String, Long> map = new HashMap<String, Long>();
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("SELECT timestamp, message_id, channel_id FROM giveaways WHERE timestamp <= ?")) {
    		
    		preparedStatement.setLong(1, epoch);
      		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			while (resultSet.next()) {
    				long date = resultSet.getLong("timestamp");
    				String messageId = resultSet.getString("message_id");
    				String channelId = resultSet.getString("channel_id");
    				map.put(channelId + "-" + messageId, date);
    			}
    		}
      		
      	} catch (SQLException e) {
      		ExceptionHandler.handle(e);
      	}
		return map;
    }
    
    public static HashMap<String, Long> getBetween(long before, long after) {
    	HashMap<String, Long> map = new HashMap<String, Long>();
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("SELECT timestamp, message_id, channel_id FROM giveaways WHERE timestamp >= ? AND timestamp <= ?")) {
    		
    		preparedStatement.setLong(1, after);
    		preparedStatement.setLong(2, before);
      		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			while (resultSet.next()) {
    				long date = resultSet.getLong("timestamp");
    				String messageId = resultSet.getString("message_id");
    				String channelId = resultSet.getString("channel_id");
    				map.put(channelId + "-" + messageId, date);
    			}
    		}
      		
      	} catch (SQLException e) {
      		ExceptionHandler.handle(e);
      	}
		return map;
    }
    
    public static HashMap<String, Long> getAfter(long epoch) {
    	HashMap<String, Long> map = new HashMap<String, Long>();
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("SELECT timestamp, message_id, channel_id FROM giveaways WHERE timestamp >= ?")) {
    		
    		preparedStatement.setLong(1, epoch);
      		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			while (resultSet.next()) {
    				long date = resultSet.getLong("timestamp");
    				String messageId = resultSet.getString("message_id");
    				String channelId = resultSet.getString("channel_id");
    				map.put(channelId + "-" + messageId, date);
    			}
    		}
      		
      	} catch (SQLException e) {
      		ExceptionHandler.handle(e);
      	}
		return map;
    }
    
    public static String getGiveawayByID(String messageId) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("SELECT channel_id FROM giveaways WHERE message_id = ?")) {
    		preparedStatement.setString(1, (messageId));
      		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			if (resultSet.next()) {
    				String channelId = resultSet.getString("channel_id");
    				return channelId;
    			}
    		}
      		
      	} catch (SQLException e) {
      		ExceptionHandler.handle(e);
      	}
		return null;
    }
    
    public static void deleteGiveaway(String messageId) {
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
    			.prepareStatement("DELETE FROM giveaways WHERE message_id = ?")) {
    	
    		preparedStatement.setString(1, messageId);
    		preparedStatement.execute();
    		
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}

