/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.util.List;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Reportbug
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        String input = event.getMessage().getContentRaw();
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage("To avoid the bug reports getting spammed only administrators are allowed to report bugs!").queue();
            return;
        }
        
        if (args.isEmpty()) {
        	event.getChannel().sendMessage("No Arguments given! Please provide a bug description!").queue();
        	return;
        }
        
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
        		.setTitle("Bug Report")
        		.setDescription(input.replace(String.valueOf(Constants.PREFIX) + this.getInvoke(), ""))
        		.addField("User Information", "Author Id: " + event.getAuthor().getId(), false)
        		.addField("Guild Information", "Guild Name: " + event.getGuild().getName() + "\n" + "Guild Id: " + event.getGuild().getId(), false)
        		.setFooter("Suggested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
        event.getJDA().getTextChannelById(612375744345014274L).sendMessage(builder.build()).queue();
        event.getChannel().sendMessage("Bug reported!").queue();
    }

    @Override
    public String getHelp() {
        return "Report any bug to the hideout";
    }

    @Override
    public String getInvoke() {
        return "reportbug";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<report>`";
    }
}

