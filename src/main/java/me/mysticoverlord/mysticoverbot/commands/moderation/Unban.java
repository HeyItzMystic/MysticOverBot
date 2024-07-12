/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.util.List;
import java.util.stream.Collectors;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Unban
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("You do not have the permission to unban members!").queue();
            return;
        }
        if (!event.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I do not have the permission to unban members!").queue();
            return;
        }
        if (args.isEmpty()) {
            channel.sendMessage("Please specify the user you want to unban!\n" + this.getUsage()).queue();
            return;
        }
        String argsJoined = String.join((CharSequence)" ", args);
        event.getGuild().retrieveBanList().queue(bans -> {
            List<?> goodUser = (List<?>)bans.stream().filter(ban -> this.isCorrectUser((Guild.Ban)ban, argsJoined)).map(Guild.Ban::getUser).collect(Collectors.toList());
            if (goodUser.isEmpty()) {
                channel.sendMessage("User is not banned!").queue();
                return;
            }
            User target = (User)goodUser.get(0);
            String mod = String.format("%#s", event.getAuthor());
            String bannedUser = String.format("%#s", target);
            event.getGuild().unban(target).reason("By " + mod).queue();
            EmbedUtils.getDefaultEmbed().setTitle("Unbanned " + bannedUser)
            .addField("Moderator", event.getAuthor().getAsMention(), false)
            .build();
            channel.sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Unbanned " + bannedUser)
                    .addField("Moderator", event.getAuthor().getAsMention(), false)
                    .build()).queue();
  });
    }
    
   

    @Override
    public String getHelp() {
        return "Unbans specified user!";
    }

    @Override
    public String getInvoke() {
        return "unban";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<User#0001/User Id>`";
    }

    private boolean isCorrectUser(Guild.Ban ban, String arg) {
        User bannedUser = ban.getUser();
        return bannedUser.getName().equalsIgnoreCase(arg) || bannedUser.getId().equals(arg);
    }
}

