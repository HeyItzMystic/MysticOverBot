package me.mysticoverlord.mysticoverbot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import me.mysticoverlord.mysticoverbot.commands.fun.*;
import me.mysticoverlord.mysticoverbot.commands.giveaway.*;
import me.mysticoverlord.mysticoverbot.commands.interactive.*;
import me.mysticoverlord.mysticoverbot.commands.memes.*;
import me.mysticoverlord.mysticoverbot.commands.moderation.*;
import me.mysticoverlord.mysticoverbot.commands.music.*;
import me.mysticoverlord.mysticoverbot.commands.owner.*;
import me.mysticoverlord.mysticoverbot.commands.*;
import me.mysticoverlord.mysticoverbot.commands.Invite;
import me.mysticoverlord.mysticoverbot.config.Config;
import me.mysticoverlord.mysticoverbot.database.SQLiteDataSource;
import me.mysticoverlord.mysticoverbot.objects.ExceptionHandler;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.IMusic;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import org.jetbrains.annotations.NotNull;
public class CommandManager {
	
    private Map<String, ICommand> commands;
    private Map<String, IMusic> music;

    public CommandManager() {
        if (Config.getInstance().getBoolean("loadcommands")) {
            System.out.println("Loading Commands...");
            commands = new HashMap<String, ICommand>();
            music = new HashMap<String, IMusic>();
            addCommand(new Ping()); 
            addCommand(new Dice()); 
            addCommand(new Help(this)); 
            addCommand(new UserInfo()); 
            addCommand(new Say()); 
            addCommand(new _8ball()); 
            addCommand(new Loveship()); // query match broken
            addCommand(new Cat()); 
            addCommand(new Dog()); 
            addCommand(new Meme()); 
            addCommand(new Joke()); 
            addCommand(new Ban()); 
            addCommand(new Prune()); 
            addCommand(new Kick()); 
            addCommand(new Unban()); 
            addCommand(new Reportbug()); // TODO add cooldown to prevent spam
            addCommand(new Prefix()); // TODO make a reset prefix command
            addCommand(new Reply()); 
            addCommand(new Suggestion()); 
            addCommand(new Battle()); // TODO make it check if another instance of this command is still running in one server to preserve memory
            addCommand(new Google()); 
            addCommand(new Choose());
            addCommand(new Cry()); 
            addCommand(new Facepalm()); 
            addCommand(new Punch()); // no error when no query, displays query as array
            addCommand(new Run()); 
            addCommand(new Shoot()); // no error when no query, displays query as array 
            addCommand(new Smile());
            addCommand(new Fortune()); 
            addCommand(new toBinary()); 
            addCommand(new Pause());
            addCommand(new Dankmemes()); 
            addCommand(new AdviceAnimals()); 
            addCommand(new AssholeDesign()); 
            addCommand(new ComedyHeaven()); 
            addCommand(new CrappyDesign()); 
           // addCommand(new DeepfriedMemes()); 
            addCommand(new Funny()); 
            addCommand(new GateKeeping()); 
            addCommand(new HistoryMemes()); 
            addCommand(new IHadAStroke());
            addCommand(new IHaveReddit());
            addCommand(new MeIRL());
            addCommand(new MemeEconomy());
            addCommand(new MildlyInfuriating());
            addCommand(new OneJob());
            addCommand(new RareInsult());
            addCommand(new WholesomeMemes()); //
            addCommand(new rules()); //
            addCommand(new Avatar()); //
            addCommand(new DailyFact()); //
            addCommand(new RandomFact()); //
            addCommand(new ServerInfo()); //
            addCommand(new Update()); //
            addCommand(new BlursedImages());
            addCommand(new ComedyCemetery());
            addCommand(new Discordmemes()); // 
            addCommand(new Replay()); //
            addCommand(new Mute()); //
            addCommand(new MuteSetup()); //
            addCommand(new Nickname()); //
            addCommand(new UnMute()); //
            addCommand(new Embed()); //
            addCommand(new Invite()); //
            addCommand(new Nicklog()); //
            addCommand(new AntiInvite()); // not always functional
            addCommand(new Tempmute()); //
            addCommand(new MessageLog()); //
            addCommand(new Swearfilter()); //
            addCommand(new Warn()); //
            addCommand(new Warnings()); //
         //   addCommand(new Lockdown());
         //   addCommand(new Open());
            addCommand(new Clear());
            addCommand(new Disconnect());
            addCommand(new Join());
            addCommand(new Play());
            addCommand(new Playing());
            addCommand(new Queue());
            addCommand(new Skip());
            addCommand(new Remove());
            addCommand(new Loop());
            addCommand(new Loopqueue());
            addCommand(new Shuffle());
            addCommand(new Cancel());
            addCommand(new End());
            addCommand(new Reroll());
            addCommand(new Start());
            addCommand(new Botinfo(commands.size()+ 1));

            // TODO add an alias option for all commands
            
            addMusic(new Disconnect());
            addMusic(new Clear());
            addMusic(new Join());
            addMusic(new Play());
            addMusic(new Playing());
            addMusic(new Queue());
            addMusic(new Skip());
            addMusic(new Remove());
            addMusic(new Loop());
            addMusic(new Loopqueue());
            addMusic(new Shuffle());
            System.out.println("Loading Complete!");
        }
    }

    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
           commands.put(command.getInvoke(), command);
           System.out.println("Loaded " + command.getClass().getSimpleName() + "!");
        }   
        
        
    }
    
    private void addMusic(IMusic m) {
    	if (!music.containsKey(m.getAlias())) {
    		music.put(m.getAlias(), m);
    		System.out.println("Loaded Alias of " + m.getClass().getSimpleName() + "!");
    	}
    }

    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    public ICommand getCommand(@NotNull String name) {
        return commands.get(name);
    }

    @SuppressWarnings({ "resource" })
	void handleCommand(GuildMessageReceivedEvent event, String prefix) {
    	if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) {
    		return;
    	}
        String[] split = null;
        // TODO if the prefix lowercase is faulty change it
            split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(prefix), "").split("\\s+");

            String invoke = split[0].toLowerCase();
            List<String> args = Arrays.asList(split).subList(1, split.length);
            
            if (getCommand(invoke) != null) {
            event.getChannel().sendTyping().queue();
            try {
                getCommand(invoke).handle(args, event);
                } catch (Exception e) {
                	ExceptionHandler.handle(e);
                }
                
            
            try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                    // language=SQLite
                    .prepareStatement("SELECT boolean FROM update_log WHERE user_id = ?")) {
        		
        		preparedStatement.setString(1, event.getAuthor().getId());
        		try (final ResultSet resultSet = preparedStatement.executeQuery()) {
        			if (resultSet.next()) {
        				if (resultSet.getBoolean("boolean")) {
        					return;
        				}
        			}
        		}

        		preparedStatement.getConnection().close();
        	} catch (SQLException e) {
        	e.printStackTrace();
        	}
            
             	try (Connection con = SQLiteDataSource.getConnection();final PreparedStatement preparedStatement = con
                         // language=SQLite
                         .prepareStatement("INSERT INTO update_log(user_id,boolean) VALUES(?,?)")) {
             		
             		preparedStatement.setString(1, event.getAuthor().getId());
             		preparedStatement.setBoolean(2, true);
             		preparedStatement.execute();

             		preparedStatement.getConnection().close();
             	} catch (SQLException e) {
             	e.printStackTrace();
             	}
             	BufferedReader read;
				try {
					read = new BufferedReader(new FileReader("/home/pi/Bot/Update.txt"));
				 
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				String ls = System.getProperty("line.separator");
					while ((line = read.readLine()) != null) {
						stringBuilder.append(line).append(ls);
					}
					
					String content = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
						
	                event.getAuthor().openPrivateChannel().queue((channel) -> {

	                    	channel.sendMessage(content).queue();

	                });
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
             	 return;  
             }
            
            
            if (music.get(invoke) != null) {
            	event.getChannel().sendTyping().queue();
            	try {
					music.get(invoke).handle(args, event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ExceptionHandler.handle(e);
				}   
            }
            
    }
    }


