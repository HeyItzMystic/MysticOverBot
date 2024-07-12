/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands;

import java.util.List;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class toBinary
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please enter a number!").queue();
            return;
        }
        if (args.size() > 19) {
            event.getChannel().sendMessage("I can't convert numbers with more than 19 digits!").queue();
            return;
        }
        if (!toBinary.isNumeric(args.get(0))) {
            event.getChannel().sendMessage("The inserted argument is not a number!").queue();
            return;
        }
        event.getChannel().sendMessage(Long.toBinaryString(Long.parseLong(args.get(0)))).queue();
    }

    @Override
    public String getHelp() {
        return "converts decimal to binary";
    }

    @Override
    public String getInvoke() {
        return "tobinary";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<number>`";
    }

    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}

