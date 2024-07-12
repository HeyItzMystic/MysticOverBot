/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
public class UserInfo
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
       
        Member member = null;
        
        if (args.size() == 0) {
        	member = event.getMember();
        } else {
        	String input = String.join(" ", args);        
        	member = FilterUtil.getMemberByQuery(input, event, 0);
        }
        
        
        
        if (member == null) {
        	event.getChannel().sendMessage("Your query or ID had no match!").queue();
        	return;
        }
        
        User user = member.getUser();
        Color color = member.getColor();
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
        		.setThumbnail(user.getEffectiveAvatarUrl())
        		.setColor(color)
        		.setTitle("UserInfo " + user.getName())
        		.addField("Effective Name", member.getEffectiveName(), false)
        		.addField("User Id + Mention", String.format("%s (%s)", user.getId(), member.getAsMention()), false)
        		.addField("Account Creation", user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), false)
        		.addField("Join Date", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), false)
        		.addField("Mutual Guilds", String.valueOf(member.getUser().getMutualGuilds().size()), false)
        		.addField("Is Bot", user.isBot() ? "True" : "False", false)
        		.addField("Server Owner", member.isOwner() ? "True" : "False", false)
        		.setFooter("Issued by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(embed.build()).queue();
        }

    @Override
    public String getHelp() {
        return "Shows Userinfo";
    }

    @Override
    public String getInvoke() {
        return "uinfo";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<user>`";
    }
}

