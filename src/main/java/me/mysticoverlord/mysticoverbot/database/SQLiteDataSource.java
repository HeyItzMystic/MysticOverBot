package me.mysticoverlord.mysticoverbot.database;

import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import me.mysticoverlord.mysticoverbot.Constants;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;

public class SQLiteDataSource {
    private static final Logger logger = LoggerFactory.getLogger(SQLiteDataSource.class);
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;
    
    static {
    	try {
    		final File dbFile = new File(Constants.path + Constants.databasepath);
    		if (!dbFile.exists()) {
    			if (dbFile.createNewFile()) {
    				logger.info("New Database was Created");
    			} else {
    				logger.info("Could not create new Database");
    			}
    		}
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	config.setJdbcUrl("jdbc:sqlite:" + Constants.path + Constants.databasepath);
    	config.setConnectionTestQuery("SELECT 1");
    	config.addDataSourceProperty("cachePrepStmts", true);
    	config.addDataSourceProperty("prepStmtCacheSize", "250");
    	config.addDataSourceProperty("prepStmtCacheSwlLimit", "2048");
    	config.setIdleTimeout(10000);
    	config.setValidationTimeout(30000);
    	config.setMaximumPoolSize(1000);
    	config.setMinimumIdle(10);
    	config.setAllowPoolSuspension(false);
    	
    	ds = new HikariDataSource(config);
    	
    	
    	try (final Statement statement = getConnection().createStatement()) {
    		final String defaultPrefix = "o!";
    		// language=SQLite
    		statement.execute("CREATE TABLE IF NOT EXISTS guild_settings(" + 
    		"id INTEGER PRIMARY KEY AUTOINCREMENT," + 
    		"guild_id VARCHAR(20) NOT NULL," + 
    		"prefix VARCHAR(255) NOT NULL DEFAULT '" + defaultPrefix +"'," + 
    		"antiinvite BOOLEAN NOT NULL DEFAULT FALSE," +
    		"nicklog BOOLEAN NOT NULL DEFAULT FALSE," +
    		"swear_filter BOOLEAN NOT NULL DEFAULT FALSE," +
    		"message_log BOOLEAN NOT NULL DEFAULT FALSE" +
    		");");
    		statement.execute("CREATE TABLE IF NOT EXISTS messages("
    				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
    				+ "message_id VARCHAR(20) NOT NULL,"
    				+ "author_id VARCHAR(20) NOT NULL,"
    				+ "message TEXT NOT NULL,"
    				+ "timestamp VARCHAR(20) NOT NULL"
    				+ ");");
    		statement.execute("CREATE TABLE IF NOT EXISTS moderation("
    				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
    				+ "guild_id VARCHAR(20) NOT NULL,"
    				+ "user_id VARCHAR(20) NOT NULL,"
    				+ "warnings INTEGER NOT NULL DEFAULT 0,"
    				+ "mutedate TEXT DEFAULT NULL"
    				+ ");");
    		statement.execute("CREATE TABLE IF NOT EXISTS update_log("
    				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
    				+ "user_id VARCHAR(20) NOT NULL,"
    				+ "boolean BOOLEAN"
    				+ ")");
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    }
	
    private SQLiteDataSource() {}
    
    public static Connection getConnection() throws SQLException {
    	return ds.getConnection();
    }
    
	
}
