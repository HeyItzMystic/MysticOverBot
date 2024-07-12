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
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Kick
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
        String reason = String.join((CharSequence)" ", args.subList(1, args.size()));
        if (target == member) {
            channel.sendMessage("You can't kick yourself.").queue();
            channel.sendMessage("Why would you want to kick yourself?").queueAfter(2L, TimeUnit.SECONDS);
            return;
        }
        if (!member.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("You don't have the required Permission to use this Command!\n`kick members`").queue();
            return;
        }
        if (!member.canInteract(target)) {
            channel.sendMessage("You can't kick members of a higher rank than your own!").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("I don't have permission to kick members!").queue();
            return;
        }
        if (!selfMember.canInteract(target)) {
            channel.sendMessage("I can't kick members with a higher/same rank as my own!").queue();
            return;
        }
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setColor(color).setTitle("Kicked " + target.getUser().getAsTag() + "!").addField("Modetator", event.getAuthor().getAsMention(), false).addField("Reason", reason, false);
        event.getGuild().kick(target, String.format("By: %s\n Reason: %s", event.getAuthor(), reason)).queue();
        target.getUser().openPrivateChannel().queue(channe -> channe.sendMessage("You have been kicked from " + event.getGuild().getName() + "\nReason: " + (!reason.isBlank() ? reason : "No Reason Provided") + "\nInvite back to the server: " + event.getGuild().retrieveInvites().complete().get(0).getUrl()).queue());
        channel.sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "kicks selected user";
    }

    @Override
    public String getInvoke() {
        return "kick";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + "`<@User#0001/UserID>`  `<reason>`";
    }
}

