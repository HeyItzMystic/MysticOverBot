/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.CommandManager;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Help
implements ICommand {
    private CommandManager manager;

    public Help(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            DefaultEmbed(event);
            return;
        }
        switch (args.get(0)) {
        case "fun":
        	FunEmbed(event);
        	break;
        case "util":
        	UtilEmbed(event);
        	break;
        case "music":
        	MusicEmbed(event);
        	break;
        case "mod":
        	ModEmbed(event);
        	break;
        case "interactive":
        	InteractiveEmbed(event);
        	break;
        case "memes":
        	MemeEmbed(event);
        	break;
        case "giveaway":
        	GiveawayEmbed(event);
        	break;
        default: 
        	ICommand command = manager.getCommand(String.join((CharSequence)"", args));
            if (command == null) {
                event.getChannel().sendMessage("Dude...").queue();
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("What are you talking about?").queue();
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Read the command list before asking for details mate!").queue();
                return;
            }
            String message = "Usage for " + command.getInvoke() + "\n" + command.getUsage() + "\n" + command.getHelp();
            event.getChannel().sendMessage(message).queue();
        }  
    }
    
    void UtilEmbed(GuildMessageReceivedEvent event) {
    	String prefix = getPrefix(event.getGuild().getId());
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
        		.setTitle("Utility Commands")
        		.addField("Ping", "Shows you how long I take to respond.\nUsage: " + prefix + "ping", false)
        		.addField("Bot Info", "Gives you bot info, Server link, Bot invite and the link to my patreon page.\nUsage: " + prefix + "botinfo", false)
        		.addField("Invite", "Displays bot and server invite along with the patreon page.\nUsage: " + prefix + "invite", false)
        		.addField("ServerInfo", "Gives you all the numbers and data of your server.\nUsage: " + prefix + "serverinfo", false)
        		.addField("Userinfo", "Get the userinfo of a specific guild member.\nUsage: " + prefix + "uinfo `<@User>`", false)
        		.addField("Update", "Gives you the latest changes.\nUsage: " + prefix + "update", false)
        		.addField("Suggestions", "Suggest new Features to the hideout.\nUsage: " + prefix + "suggest `<suggestion>`", false)
        		.addField("Embed", "Embeds your message.\nUsage: " + prefix + "embed `<message>`", false)
        		.addField("Avatar", "Get user avatar.\nUsage: " + prefix + "avatar `<@User>`", false).setFooter("Issued by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(builder.build()).queue();
    }

    void FunEmbed(GuildMessageReceivedEvent event) {
    	String prefix = getPrefix(event.getGuild().getId());
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
        		.setTitle("Fun Commands")
        		.addField("8ball", "Ask me a question and i'll answer.\nUsage: " + prefix + "8ball `<question>`", false)
        		.addField("Google", "Give me something to google.\nUsage: " + prefix + "google `<search>`", false)
        		.addField("Dice", "Throw 2 dice for a number of 2-12.\nUsage: " + prefix + "dice", false)
        		.addField("Cat", "Gives you some cute kitties to look at.\nUsage: " + prefix + "cat", false)
        		.addField("Dog", "Releases the hounds.\nUsage: " + prefix + "dog", false)
        		.addField("Joke", "I'll tell you a joke from reddit.\nUsage: " + prefix + "joke", false)
        		.addField("ToBinary", "Converts a number to binary\nUsage: " + prefix + "tobinary `<number>`", false)
        		.addField("Fortune Cookie", "Get a fortune cookie.\nUsage: " + prefix + "fortune", false)
        	    .addField("Random Fact", "Get any random *likely useless* fact.\nUsage: " + prefix + "randomfact", false)
        	    .addField("Daily Fact", "Get the fact of the day which is also *useless*.\nUsage: " + prefix + "dailyfact", false)
        		.setFooter("Issued by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(builder.build()).queue();
    }

    void MusicEmbed(GuildMessageReceivedEvent event) {
    	String prefix = getPrefix(event.getGuild().getId());
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("Music Commands")
        		.addField("Join", "I'll join your voice channel.\nUsage: " + prefix + "join", false)
        		.addField("Disconnect", "I'll leave the current voice channel.\nUsage: " + prefix + "disconnect", false)
        		.addField("Play", "Searches and plays the given song from youtube.\nUsage: " + prefix + "play `<query>`", false)
        		.addField("Clear", "Clears the queue.\nUsage: " + prefix + "clear", false)
        		.addField("Pause", "Pauses/Resumes the queue playback.\nUsage: " + prefix + "pause", false)
        		.addField("Queue", "Displays the queue.\nUsage: " + prefix + "queue", false)
        		.addField("Skip", "Skips the current song.\nUsage: " + prefix + "skip", false)
        		.addField("Now Playing", "Displays the song which is currently playing.\nUsage: " + prefix + "np", false)
        		.addField("Remove", "Removes the given song from the queue.\nUsage: " + prefix + "remove `<number in queue>`", false)
        		.addField("Replay", "Resets the song playback to 00:00.\nUsage: " + prefix + "replay", false).setFooter("Issued by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl())
        		.addField("Shuffle", "Shuffles the current queue.\nUsage: " + prefix + "shuffle", false)
        		.addField("Loop", "Loops the current song.\nUsage: " + prefix + "loop", false)
        		.addField("Loopqueue", "Loops the queue.\nUsage: " + prefix + "loopqueue", false);
        event.getChannel().sendMessage(builder.build()).queue();
    }

    void ModEmbed(GuildMessageReceivedEvent event) {
    	String prefix = getPrefix(event.getGuild().getId());
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("Mod Commands")
        		.addField("Kick", "Kicks the specified user.\nUsage: " + prefix + "kick `<@user>`", false)
        		.addField("Ban", "Bans the specified user.\nUsage: " + prefix + "ban `<@user>`", false)
        		.addField("Unban", "Unbans the specified user or userid.\nUsage: " + prefix + "unban `<@User/userid>`", false)
        		.addField("Prefix", "Sets a custom prefix for your server.\nUsage: " + prefix + "prefix `<prefix>`", false)
        		.addField("Prune", "Clears the specified amount of messages.\nUsage: " + prefix + "prune `<ammount of messages>`", false)
        		.addField("Report Bug", "Reports a bug to the hideout.\nUsage: " + prefix + "reportbug `<report>`", false)
        		.addField("MuteSetup", "Sets up a \"Muted\" role.\nUsage: " + prefix + "mutesetup", false)
        		.addField("Mute", "Mutes the mentioned user.\nUsage: " + prefix + "mute `<@user>`", false)
        		.addField("Tempmute", "Mutes the mentiones user for the specified amount of time.\nUsage: " + prefix + "tempmute <@user> <d# h# m#>", false)
        		.addField("Nickname", "Assigns a new nickname to the mentioned user.\nUsage: " + prefix + "nick `<@user> <nickname>`", false)
        		.addField("NickLog", "Makes log entries for all nickname changes.\nUsage: " + prefix + "nicklog `[enable/disable]`", false)
        		.addField("AntiInvite", "Activates invite preventio.\nUsage: " + prefix + "antiinvite `[enable/disable]`", false)
        		.addField("Swear Filter", "Activates the swear filter.\nUsage: " + prefix + "swearfilter `[enable/disable]`", false)
        		.addField("Message Log", "Logs deleted and edited messages.\nUsage: " + prefix + "log `[enable/disable]`", false)
        		.addField("Warn", "Warns the specified user.\nUsage: " + prefix + "warn `<@user> <reason>", false)
        		.addField("Warnings", "Views, clears or sets someones warning count.\nUsage: " + prefix + "warnings <@user> [clear,set,show]", false)
        		.setFooter("Issued by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(builder.build()).queue();
    }

    void InteractiveEmbed(GuildMessageReceivedEvent event) {
    	String prefix = getPrefix(event.getGuild().getId());
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("Interactive Commands")
        		.addField("Battle", "Have a fight to the death with another member.\nUsage: " + prefix + "battle `[@User1] [@User2]`", false)
        		.addField("Choose", "Give me any amount of objects and I will choose one.\nUsage: " + prefix + "choose `<object1> <or> <object2> [or] [object3] etc...`", false)
        		.addField("Cry", "Show how sad you feel.\nUsage: " + prefix + "cry", false)
        		.addField("Facepalm", "Show your disappointment.\nUsage: " + prefix + "facepalm", false)
        		.addField("Punch", "Punch anybody.\nUsage: " + prefix + "punch `<@User>`", false)
        		.addField("Run", "Run away! Run away!\nUsage: " + prefix + "run", false)
        		.addField("Shoot", "Shoot somebody!\nUsage: " + prefix + "shoot `<@User>`", false)
        		.addField("Smile", "Because you're happy!\nUsage: " + prefix + "smile", false)
        		.addField("Loveship", "See if it works out with the one you love!\nUsage: " + prefix + "loveship `<@User1> [@User2]`", false)
        		.addField("Say", "You be the human, I be the parrot\nUsage: " + prefix + "say `sentence`", false)
        		.setFooter("Issued by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(builder.build()).queue();
        
    }
    
    void MemeEmbed(GuildMessageReceivedEvent event) {
    	String prefix = getPrefix(event.getGuild().getId());
    	EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
    			.setTitle("Meme Commands")
    			.setDescription("To get any meme just use " + prefix + "meme."
    					+ "\nOr if you want memes from specific subreddits you can use one of the commands listed below:\n\n"
    					+ prefix + "adviceanimal -> r/AdviceAnimals\n"
    					+ prefix + "assholedesign -> r/assholedesign\n"
    					+ prefix + "comedyheaven -> r/comedyheaven\n"
    					+ prefix + "crappydesign -> r/CrappyDesign\n"
    					+ prefix + "dankmeme -> r/dankmemes\n"
    					+ prefix + "deepfriedmeme -> r/DeepFriedMemes\n" 
    					+ prefix + "funny -> r/funny\n"
    					+ prefix + "gatekeeping -> r/gatekeeping\n"
    					+ prefix +  "historymeme -> r/HistoryMemes\n"
    					+ prefix + "ihadastroke -> r/ihadastroke\n"
    					+ prefix + "ihavereddit -> r/ihavereddit\n"
    					+ prefix +  "meirl -> r/meirl\n"
    					+ prefix +  "memeeconomy -> r/MemeEconomy\n"
    					+ prefix + "mildlyinfuriating -> r/mildlyinfuriating\n"
    					+ prefix + "onejob -> r/OneJob\n"
    					+ prefix + "rareinsult -> r/rareinsults\n"
    					+ prefix + "wholesomememe -> r/wholesomememes\n"
    				    + prefix + "comedycemetery -> r/ComedyCemetery\n"
    				    		+ prefix + "discordmeme -> r/Discordmemes\n"
    				    				+ prefix + "blursedimage -> r/BlursedImages");
        event.getChannel().sendMessage(builder.build()).queue();
    }
    
    void GiveawayEmbed(GuildMessageReceivedEvent event) {
    	String prefix = getPrefix(event.getGuild().getId());
    	 EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("Giveaway Commands")
         		.addField("Start", "Starts a new Giveaway.\nUsage: " + prefix + "gstart `<#channel/channelID> <d# h# m#>` `<prize | amount>`", false)
         		.addField("Reroll", "Selects a new Winner for an ended Giveaway.\nUsage: " + prefix + "greroll `<channelId>` `<messageId>`", false)
         		.addField("End", "Ends a Giveaway and selects winners.\nUsage: " + prefix + "gend `<messageId>`", false)
         		.addField("Cancel", "Cancels a Giveaway\nUsage: " + prefix + "gcancel `<messageId>`", false)
         		.setFooter("Issued by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
         event.getChannel().sendMessage(builder.build()).queue();
    }

    void DefaultEmbed(GuildMessageReceivedEvent event) {
    	String prefix = getPrefix(event.getGuild().getId());
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
        		.setTitle("Bot Info")
        		.setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl().toString())
        		.setDescription("Welcome to MysticOverBot!\n\nThis command is categorized in the following sub-commands:\n\n" + 
        				prefix + "`help util`: gives you the Utility Commands\n\n" + 
        				prefix + "`help fun`: gives you the Fun Commands\n\n" + 
        				prefix + "`help music`: gives you the Music Commands  \n\n" + 
        				prefix + "`help mod`: gives you the Mod Commands\n\n" + 
        				prefix + "`help interactive`: give you the Interactive Commands\n\n" + 
        				prefix + "`help memes`: gives you the Meme Commands.\n\n" +
        				prefix + "`help giveaway`: gives you the Giveaway Commands.")//.addField("Extra Info", "This bot is running on limited capacity. Timeouts areexpected!", false)
        		.addField("Version", Constants.VERSION, false)
        		.setFooter("Have Fun using me!");
                
       try {
        builder.addField("Credits", event.getJDA().getUserById(508720470275653636L).getAsTag() + ": Server Icon Design", true);
       } catch (NullPointerException e) {
    	   builder.addField("Credits", "♡Beth♡#1213: Server Icon Design", true);
       }
       
       try {
       if (event.getGuild().getMembers().contains(event.getGuild().getMember(event.getJDA().getUserById(Constants.OWNER)))) {
    	   builder.addField("Owner", event.getJDA().getUserById(394066807926947850L).getAsMention(), true);
       } else { 
    	   builder.addField("Owner", event.getJDA().getUserById(394066807926947850L).getAsTag(), true);
       }
       } catch (Exception e) {
    	   e.printStackTrace();
    	   builder.addField("Owner", event.getJDA().getUserById(394066807926947850L).getAsTag(), true);
       }
        		
        		builder.setFooter("Issued by " + event.getAuthor().getAsTag(), event.getAuthor()
        				.getAvatarUrl());
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Shows this Message";
    }

    @Override
    public String getInvoke() {
        return "help";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + getInvoke() + " `<command>` *optional*";
    }
    
    private String getPrefix(String id) {
    	return SQLiteUtil.getPrefix(id);
    }
}

