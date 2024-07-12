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
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
public class Punch
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        String author = event.getAuthor().getName();
        ArrayList<String> mentioned = new ArrayList<String>();
        
        if (!args.isEmpty()) {
        	try {
        	mentioned.add(FilterUtil.getMemberByQuery(String.join(" ", args), event, 0).getUser().getName());
        	} catch (NullPointerException e) {
        	}
           }
        if (mentioned.size() == 0) {
        	if (args.size() != 0) {
        		event.getChannel().sendMessage("No User found!").queue();
       	} else {
       		mentioned.add(author);
       	}
        }
        
        Random r = new Random();
        String victim = (String)mentioned.get(0);
        if (author == mentioned.get(0)) {
            victim = "themself";
        }
        ArrayList<String> punch = new ArrayList<String>();
        punch.add("https://media.giphy.com/media/l0K4bDEFflkY1S7XW/giphy.gif");
        punch.add("https://media1.giphy.com/media/NApLRN6pkNUeA/source.gif");
        punch.add("https://thumbs.gfycat.com/BigBackBonobo-size_restricted.gif");
        punch.add("https://media1.tenor.com/images/2cc9d75ef0338fb98de1c973aa12445a/tenor.gif?itemid=13641020");
        punch.add("https://media3.giphy.com/media/l3V0D3RzZSeVDKGFa/giphy.gif");
        punch.add("https://media1.tenor.com/images/4586b72e5e36fec52202b9c42d8b8dd6/tenor.gif?itemid=4953546");
        punch.add("https://media.tenor.com/images/82a9ce31039077df8dece57fe0150217/tenor.gif");
        punch.add("https://media1.tenor.com/images/ec83a6ad95a44059a9bc7dcfb1a0912a/tenor.gif?itemid=6117480");
        punch.add("https://media0.giphy.com/media/3o6ozy30BKlEGWLduw/giphy.gif");
        punch.add("https://media1.giphy.com/media/SzC42gUrhHopW/source.gif");
        punch.add("https://media0.giphy.com/media/dtGzpsBlZsI8r04IQQ/giphy.gif");
        punch.add("https://media0.giphy.com/media/Ax6tX3pd7PCms/giphy.gif");
        MessageBuilder message = new MessageBuilder();
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
        int random = r.nextInt(punch.size());
        embed.setImage((String)punch.get(random)).setTitle(String.valueOf(author) + " punched " + victim).setFooter("requested by " + author, event.getAuthor().getEffectiveAvatarUrl());
        message.setEmbed(embed.build());
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String getHelp() {
        return "punches selected user";
    }

    @Override
    public String getInvoke() {
        return "punch";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<user>`";
    }
}

