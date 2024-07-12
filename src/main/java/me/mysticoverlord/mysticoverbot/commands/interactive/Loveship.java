/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Loveship
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        String author = event.getAuthor().getAsTag();
        ArrayList<String> mentioned = new ArrayList<String>();
        
        
        if (args.size() > 1) { 
            for (int i = 0; i < 2; i++) {
            	mentioned.add(FilterUtil.getMemberByQuery(args.get(i), event, 0).getEffectiveName());
            }
        } else {
        	event.getChannel().sendMessage("Please enter a user!").queue();
        	return;
        }

        if (mentioned.size() == 0) {
        	event.getChannel().sendMessage("No user has been found!\nTry mentioning the user or copy their name!").queue();
        	return;
        }
        
        if (mentioned.size() == 2) {
            Random r = new Random();
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setTitle("Loveship");
            int random = r.nextInt(101);
            if (random == 0) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2715\u2715\u2715\u2715\u2715\u2715\u2715\u2715\u2715\u2715| " + random + "% " + (String)mentioned.get(1), "***Do not, Under any circumstances,\n even dare to contact each other\nunless you want to start World War III!***", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 10) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + (String)mentioned.get(1), "This is not going to work!\nYou should avoid any relationship!\n Any on-going relationships should be ended before it turns into a disaster!", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 20) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + (String)mentioned.get(1), "You dudes are not gonna do well in a relationship.\nClose friendship is also gonna be tough so best to stop thinking about this.", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 30) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + (String)mentioned.get(1), "Hate to be the bearer of bad news:\nBut you guys are not gonna click!\nIt ain't gonna happen", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 40) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + (String)mentioned.get(1), "Allright, so the likelyhood of you going to have a relationship is basically 0.\nSo don't get your hopes up.", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 50) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + (String)mentioned.get(1), "You guys shouldn't go further than friendship.\nTrust me on this!", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 60) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + (String)mentioned.get(1), "There is a very ***very*** small chance this might work.\nIt has potential to go wrong!", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 70) {
                embed.addField(String.valueOf(String.valueOf(mentioned.get(0))) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u2588\u00a6\u00a6\u00a6\u00a6| " + random + "% " + (String)mentioned.get(1), "Something might be able to happen between you two!\nPut some work in it and it might turn out great!", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 80) {
                embed.addField(String.valueOf(String.valueOf(mentioned.get(0))) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u00a6\u00a6\u00a6| " + random + "% " + (String)mentioned.get(1), "I definetly see something happening here!\nYou guys seem great for each other!\nDon't mess up too much though.", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 90) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u00a6\u00a6| " + random + "% " + (String)mentioned.get(1), "Now this is what i call a cute couple!\nIt's almost like you're made for each other!", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 100) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u00a6| " + random + "% " + (String)mentioned.get(1), String.valueOf((String)mentioned.get(0)) + " and " + (String)mentioned.get(1) + " sitting in a tree!\nK-I-S-S-I-N-G!", false);
                channel.sendMessage(embed.build()).queue();
            } else {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588| " + random + "% " + (String)mentioned.get(1), "Match made in heaven!\nWhat more is there to say?", false);
                channel.sendMessage(embed.build()).queue();
            }
        } else if (mentioned.size() == 1) {
            Random r = new Random();
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setTitle("Loveship");
            int random = r.nextInt(101);
            if (random == 0) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2715\u2715\u2715\u2715\u2715\u2715\u2715\u2715\u2715\u2715| " + random + "% " + author, "***Do not,\nUnder any circumstances,\nconntact each other if you don't want to start a war!***", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 10) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + author, "This is not going to work!\nYou should avoid any relationship!\n Any on-going relationships should be ended before it turns into a disaster!", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 20) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + author, "You dudes are not gonna do well in a relationship.\nClose friendship is also gonna be tough so best to stop thinking about this.", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 30) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + author, "Hate to be the bearer of bad news:\nBut you guys are not gonna click!\nIt ain't gonna happen", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 40) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + author, "Allright, so the likelyhood of you going to have a relationship is basically 0.\nSo don't get your hopes up.", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 50) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u00a6\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + author, "You guys shouldn't go further than friendship.\nTrust me on this!", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 60) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u00a6\u00a6\u00a6\u00a6\u00a6| " + random + "% " + author, "There is a very ***very*** small chance this might work.\nIt has potential to go wrong!", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 70) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u2588\u00a6\u00a6\u00a6\u00a6| " + random + "% " + author, "Something might be able to happen between you two!\nPut some work in it and it might turn out great!", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 80) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u00a6\u00a6\u00a6| " + random + "% " + author, "I definetly see something happening here!\nYou guys seem great for each other!\nDon't mess up too much though.", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 90) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u00a6\u00a6| " + random + "% " + author, "Now this is what i call a cute couple!\nIt's almost like you're made for eachother!", false);
                channel.sendMessage(embed.build()).queue();
            } else if (random < 100) {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u00a6| " + random + "% " + author, String.valueOf(String.valueOf(mentioned.get(0))) + " and " + author + " sitting in a tree!\nK-I-S-S-I-N-G!", false);
                channel.sendMessage(embed.build()).queue();
            } else {
                embed.addField(String.valueOf((String)mentioned.get(0)) + " " + random + "%" + " |\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588| " + random + "% " + author, "Match made in heaven!\nWhat more is there to say?", false);
                channel.sendMessage(embed.build()).queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "See if it will work out with your true love";
    }

    @Override
    public String getInvoke() {
        return "loveship";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<user>` `<optional user>`";
    }
}

