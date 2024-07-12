/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Ban
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Color color = new Color(128, 0, 0);
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        
        if (args.isEmpty()) {
            channel.sendMessage("Please specify the targeted user!\n" + this.getUsage()).queue();
            return;
        }
        Member target = FilterUtil.getMemberByData(args.get(0), event, 0);
        
        if (target == null) {
            channel.sendMessage("ID or Mention had no match!").queue();
            return;
        }
        String reason;
        try {
         reason = String.join((CharSequence)" ", args.subList(1, args.size()));   
        } catch (Exception e) {
        	reason = "not specified";
        }
        
        if (target == member) {
            channel.sendMessage("You can't ban yourself.").queue();
            channel.sendMessage("Why would you want to ban yourself?").queueAfter(2L, TimeUnit.SECONDS);
        }
        if (!member.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("You don't have permission to ban members!").queue();
            return;
        }
        if (!member.canInteract(target)) {
            channel.sendMessage("You can't ban members of a higher rank than your own!").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I don't have permission to ban members!").queue();
            return;
        }
        if (!selfMember.canInteract(target)) {
            channel.sendMessage("I can't ban members with a higher/same rank as my own!").queue();
            return;
        }
        event.getGuild().ban(target, 7).reason(String.format("By: %s\n Reason: %s", event.getAuthor(), reason)).queue();
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setColor(color).setTitle("Banned " + target.getUser().getAsTag() + "!")
.addField("Moderator", event.getAuthor().getAsMention(), false).addField("Reason", reason, false);
        event.getGuild().kick(target, String.format("By: %s\n Reason: %s", event.getAuthor(), reason)).queue();
        target.getUser().openPrivateChannel().queue(channe -> channe.sendMessage("You have been banned from " + event.getGuild().getName()).queue());
        channel.sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "bans selected user and deletes their messages from upto 7 days ago";
    }

    @Override
    public String getInvoke() {
        return "ban";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + "`<@User#0001>`  `<reason>`";
    }
}

