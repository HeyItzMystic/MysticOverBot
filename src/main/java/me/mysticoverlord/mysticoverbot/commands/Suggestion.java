/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands;

import java.util.List;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Suggestion
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please add a suggestion!").queue();
            return;
        }
        String input = event.getMessage().getContentRaw();
        long channelid = 591705838327169024L;
        
        if (input.startsWith("O!")) {
        	input = input.replaceFirst("O!" + getInvoke(), "");
        } else if(input.startsWith("o!")){
        	input = input.replaceFirst("o!" + getInvoke(), "");
        }else {
        	input = input.replaceFirst(Constants.PREFIX + getInvoke(), "");
        }
        
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("Suggestion").setDescription(input).addField("User Information", "Author Id: " + event.getAuthor().getId(), false).addField("Guild Information", "Guild Name: " + event.getGuild().getName() + "\n" + "Guild Id: " + event.getGuild().getId(), false).setFooter("Suggested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
        event.getJDA().getTextChannelById(channelid).sendMessage(builder.build()).queue();
        event.getChannel().sendMessage("Suggestion Sent!").queue();
    }

    @Override
    public String getHelp() {
        return "Suggest new features to the hideout";
    }

    @Override
    public String getInvoke() {
        return "suggest";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<report>`";
    }
}

