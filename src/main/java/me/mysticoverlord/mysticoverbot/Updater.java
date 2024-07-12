package me.mysticoverlord.mysticoverbot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.mysticoverlord.mysticoverbot.database.SQLiteDataSource;
import me.mysticoverlord.mysticoverbot.objects.Encryption;
import me.mysticoverlord.mysticoverbot.objects.ExceptionHandler;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;

public class Updater {
	
	public Updater() throws SQLException  {
	    
}
	public void update() throws SQLException {
		Logger logger = LoggerFactory.getLogger(Updater.class);
		HashMap<String, Long> map = new HashMap<String, Long>();
		Encryption encryption = SQLiteUtil.encryption;
		logger.info("starting updater");
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("SELECT timestamp, message_id, channel_id FROM giveaways")) {
    		
      		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			while (resultSet.next()) {
    				logger.info(resultSet.toString());
    				long date = resultSet.getLong("timestamp");
    				String messageId = encryption.decrypt(resultSet.getString("message_id"));
    				String channelId = encryption.decrypt(resultSet.getString("channel_id"));
    				map.put(channelId + "-" + messageId, date);
    			}
    		}
      		
      	} catch (SQLException e) {
      		e.printStackTrace();
      		ExceptionHandler.handle(e);
      	}
    	map.forEach((Ids, date) -> {
    		String channelId = Ids.split("-")[0];
    		String messageId = Ids.split("-")[1];
    		String decryptedChannel = encryption.decrypt(channelId);
    		String decryptedMessage = encryption.decrypt(messageId);
    		try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
    				//language=SQLite
    				.prepareStatement("UPDATE giveaways SET message_id = ?, channel_id = ? WHERE message_id = ? AND channel_id = ?")){
    			preparedStatement.setString(1, decryptedMessage);
    			preparedStatement.setString(2, decryptedChannel);
    			preparedStatement.setString(3, messageId);
    			preparedStatement.setString(4, channelId);
    			
    			preparedStatement.execute();
    			con.close();
    			
    		} catch(SQLException e) {
    			ExceptionHandler.handle(e);
    			System.exit(500);
    		}
    	});
    	
    	ArrayList<String> guild_ids = new ArrayList<String>();
    	
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("SELECT guild_id FROM guild_settings")) {
    		
      		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			while (resultSet.next()) {
    				guild_ids.add(resultSet.getString("guild_id"));
    			}
    		}
      		
      	} catch (SQLException e) {
      		ExceptionHandler.handle(e);
      	}
    	
    	
    	for (int x = 0; x < guild_ids.size(); x++) {
			String	guildId = guild_ids.get(x);
		
			String decryptedGuild = encryption.decrypt(guildId);
			try (Connection con1 = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement1 = con1
					//language=SQLite
					.prepareStatement("UPDATE guild_settings SET guild_id = ? WHERE guild_id = ?")){
				preparedStatement1.setString(1, decryptedGuild);
				preparedStatement1.setString(2, guildId);
				
				preparedStatement1.execute();
				
			} catch(SQLException e) {
				ExceptionHandler.handle(e);
		  		e.printStackTrace();
				System.exit(500);
			}
		
}
			
    	
    	ArrayList<String> message_ids = new ArrayList<String>();
    	ArrayList<String> author_ids = new ArrayList<String>();
    	ArrayList<String> dates = new ArrayList<String>();
    	
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("SELECT message_id, author_id FROM messages")) {
    		
      		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			
    				while (resultSet.next()) {
    					message_ids.add(resultSet.getString("message_id"));
    					author_ids.add(resultSet.getString("author_id"));
    				}
    			}
    		}
      		
      	 catch (SQLException e) {
      		ExceptionHandler.handle(e);
      		e.printStackTrace();
      		System.exit(500);
      	}
    	
		for (int x = 0; x < message_ids.size(); x++) {
			String messageId = message_ids.get(x);
			String authorId = author_ids.get(x);
    		String decryptedMessage = encryption.decrypt(messageId);
    		String decryptedAuthor = encryption.decrypt(authorId);
    		String date = dates.get(x);
    		String pattern = "yyyy-MM-dd-HH-mm";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        	Long sent = null;
			try {
				sent = simpleDateFormat.parse(date).toInstant().getEpochSecond();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NullPointerException e2) {
				
			}
    		try (Connection con1 = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement1 = con1
    				//language=SQLite
    				.prepareStatement("UPDATE messages SET message_id = ?, author_id = ?, timestamp = ? WHERE message_id = ? AND author_id = ?")){
    			preparedStatement1.setString(1, decryptedMessage);
    			preparedStatement1.setString(2, decryptedAuthor);
    			preparedStatement1.setString(3, messageId);
    			preparedStatement1.setString(4, authorId);
    			
    			preparedStatement1.execute();
    			con1.close();
    			
    		} catch(SQLException e) {
    			ExceptionHandler.handle(e);
          		e.printStackTrace();
    			System.exit(500);
    		}
				
			}

		HashMap<Integer, String> date = new HashMap<Integer, String>();
   
    	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
      			// language=SQLite
      			.prepareStatement("SELECT guild_id, user_id, mutedate FROM moderation")) {
    		
      		
    		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
    			guild_ids.clear();
    			author_ids.clear();
    			int x = 0;
    			while (resultSet.next()) {
    				guild_ids.add(resultSet.getString("guild_id"));
    				author_ids.add(resultSet.getString("user_id"));
    				date.put(x, resultSet.getString("mutedate"));
    				x++;
    			}
    		}
      		
      	} catch (SQLException e) {
      		ExceptionHandler.handle(e);
      		e.printStackTrace();
      		System.exit(500);
      	}
    	
    	for (int x = 0; x < guild_ids.size(); x++) {
		    String guildId = guild_ids.get(x);
			String userId = author_ids.get(x);
			String mutedate = date.get(x);
    		String decryptedGuild = encryption.decrypt(guildId);
    		String decryptedUser = encryption.decrypt(userId);
        	String pattern = "yyyy-MM-dd-HH-mm";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        	Long sent = null;
			try {
				sent = simpleDateFormat.parse(mutedate).toInstant().getEpochSecond();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NullPointerException e2) {
				
			}
    		try (Connection con1 = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement1 = con1
    				//language=SQLite
    				.prepareStatement("UPDATE moderation SET guild_id = ?, user_id = ?, mutedate = ? WHERE guild_id = ? AND user_id = ?")){
    			preparedStatement1.setString(1, decryptedGuild);
    			preparedStatement1.setString(2, decryptedUser);
    			if (sent != null) {
    				preparedStatement1.setLong(3, sent);
    			} else {
    				preparedStatement1.setNull(3, java.sql.Types.NULL);
    			}
    			preparedStatement1.setString(4, guildId);
    			preparedStatement1.setString(5, userId);
    			
    			preparedStatement1.execute();
    			con1.close();
    			
    		} catch(SQLException e) {
    			ExceptionHandler.handle(e);
          		e.printStackTrace();
    			System.exit(500);

    		}
			}
    	
    	//System.exit(200);
    	
			}
	}
