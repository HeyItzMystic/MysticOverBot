/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Prune
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        if (!member.hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("You don't have the required Permission to use this command!\n`Manage Messages`").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("Bot doesn't have the required Permission to activate this command!\n`Manage Messages`").queue();
            return;
        }
        if (args.isEmpty()) {
            channel.sendMessage(this.getUsage()).queue();
            return;
        }
        int amount = 0;
        String arg = args.get(0);
        try {
            amount = Integer.parseInt(arg);
        }
        catch (NumberFormatException ignored) {
            channel.sendMessageFormat("'%s' is not a valid number", arg).queue();
        }
        if (amount < 2 || amount > 100) {
            channel.sendMessage("Can only delete 2 to 100 messages!\nNo more, No less!").queue();
            return;
        }
        
        event.getMessage().delete().queue();
        
            channel.getIterableHistory().takeAsync(amount).thenApplyAsync(messages -> {
            	
            List<Message> goodMessages = messages.stream().filter(m3 -> !m3.getTimeCreated().isAfter(OffsetDateTime.now().plus(2L, ChronoUnit.WEEKS))).collect(Collectors.toList());
            channel.purgeMessages(goodMessages);
            return goodMessages.size();
            
        }).whenCompleteAsync((count, thr) -> channel.sendMessageFormat("Deleted '%d' messages", count).queue(message -> {
        	message.delete().queueAfter(5L, TimeUnit.SECONDS);     
        })).exceptionally(thr -> {
            String cause = "";
            if (thr.getCause() != null) {
                cause = "caused by: " + thr.getCause().getMessage();
            }
            channel.sendMessageFormat("Error %s%s", thr.getMessage(), cause).queue();
            return 0;
        });
    }

    @Override
    public String getHelp() {
        return "deletes the specified amount of messages";
    }

    @Override
    public String getInvoke() {
        return "prune";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + "`2-100`\nNote: only deletes messages which are younger than 2 weeks!";
    }
}

