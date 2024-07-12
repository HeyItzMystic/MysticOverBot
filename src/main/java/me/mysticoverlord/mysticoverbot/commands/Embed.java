package me.mysticoverlord.mysticoverbot.commands;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Embed implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String input = event.getMessage().getContentDisplay();
        if (input.contains("@")) {
        	input = input.replaceAll("@", "");
        }
        
        if (input.startsWith("O!")) {
        	input = input.replaceFirst("O!" + getInvoke(), "");
        } else if(input.startsWith("o!")){
        	input = input.replaceFirst("o!" + getInvoke(), "");
        }else {
        	input = input.replaceFirst(Constants.PREFIX + getInvoke(), "");
        }
        
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
        		.setDescription(input)
        		.setAuthor(event.getAuthor().getName(), event.getAuthor().getEffectiveAvatarUrl(), event.getAuthor().getEffectiveAvatarUrl());
        
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Embeds your message";
    }

    @Override
    public String getInvoke() {
        return "embed";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<statement>`";
    }
}

