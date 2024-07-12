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
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Shoot
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        String author = event.getAuthor().getName();
        ArrayList<String> mentioned = new ArrayList<String>();
        
        try {         
            mentioned.add(event.getJDA().getUserById(event.getGuild().getMemberById(args.get(0)).getId()).getName());
            } catch (NullPointerException | NumberFormatException e) {
            	
            }
        
        if (mentioned.isEmpty()) {
        	try {
            	mentioned.add(FilterUtil.getMemberByQuery(String.join(" ", args), event, 0).getUser().getName());
            	} catch (NullPointerException e) {
            		
            	}
            
            if (event.getMessage().getMentions(new Message.MentionType[0]).size() >= 1 && mentioned.size() == 0) {
            	
                mentioned.add(((User)event.getMessage().getMentions(new Message.MentionType[0]).get(0)).getName().toString());
                
            }
            }
        
        if (mentioned.size() == 0) {
        	mentioned.add(author);
        }

        
        Random r = new Random();
        String victim = (String)mentioned.get(0);
        if (author == mentioned.get(0)) {
            victim = "themself";
        }
        ArrayList<String> shoot = new ArrayList<String>();
        shoot.add("http://i.imgur.com/GxsexjS.gif");
        shoot.add("https://i.makeagif.com/media/8-02-2015/18ZMj-.gif");
        shoot.add("https://media.giphy.com/media/xUA7b28Xco5JQNohOg/giphy.gif");
        shoot.add("https://media1.giphy.com/media/aqDXCH2M1ycEw/giphy.gif");
        shoot.add("https://thumbs.gfycat.com/ThatThriftyIrishwaterspaniel-size_restricted.gif");
        shoot.add("https://media.giphy.com/media/PnhOSPReBR4F5NT5so/giphy.gif");
        shoot.add("https://66.media.tumblr.com/a550aeba712f7e7ee6f1e897b57aa35d/tumblr_nsvbpeJgoJ1rob81ao7_r1_250.gif");
        shoot.add("https://i.imgur.com/0XKna6f.gif");
        shoot.add("https://media1.tenor.com/images/7cb48c9c92a13a7318714ffb4e54b8b6/tenor.gif?itemid=8162453");
        shoot.add("https://media1.tenor.com/images/2491477863f0c618bb44a320e581a87e/tenor.gif?itemid=9317192");
        shoot.add("https://media1.tenor.com/images/a83d41c02c2971629ac5a98a08db1fcd/tenor.gif?itemid=13384546");
        int random = r.nextInt(shoot.size());
        MessageBuilder message = new MessageBuilder();
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
        embed.setImage((String)shoot.get(random)).setTitle(String.valueOf(author) + " has shot " + victim).setFooter("requested by " + author, event.getAuthor().getEffectiveAvatarUrl());
        message.setEmbed(embed.build());
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String getHelp() {
        return "shoots selected user";
    }

    @Override
    public String getInvoke() {
        return "shoot";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<user>`";
    }
}

