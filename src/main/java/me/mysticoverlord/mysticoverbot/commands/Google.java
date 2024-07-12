/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands;

import java.util.List;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Google
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("I can't search nothing mate!\nGimme something to search for!").queue();
            return;
        }
        event.getChannel().sendMessage("http://lmgtfy.com/?q=" + args.toString().replaceAll(",", "").replace("[", "").replace("]", "").replaceAll(" ", "+")).queue();
    }

    @Override
    public String getHelp() {
        return "i google something for you!";
    }

    @Override
    public String getInvoke() {
        return "google";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + "`<search>`";
    }
}

