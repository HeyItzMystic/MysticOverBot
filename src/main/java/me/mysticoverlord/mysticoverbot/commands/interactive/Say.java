/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.interactive;

import java.util.List;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.ModUtil;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Say
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String input = event.getMessage().getContentDisplay();
        if (args.isEmpty()) {
        	event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you must include a message for me to repeat!\nI can't repeat nothing man!").queue();
        	return;
        }
        if (input.contains("@")) {
        	input = input.replaceAll("@", "");
        }
        if (input.startsWith("O!")) {
        	input = input.replaceAll("O!" + getInvoke(), "");
        } else if(input.startsWith("o!")){
        	input = input.replaceAll("o!" + getInvoke(), "");
        }else {
        	input = input.replaceAll(Constants.PREFIX + getInvoke(), "");
        }
        
 
        	if (ModUtil.containsSwear(input)) {
                return;
             }
        
        
        event.getChannel().sendMessage(input + "\n\n" + event.getAuthor().getAsTag()).queue();
    }

    @Override
    public String getHelp() {
        return "You be the human, i be the parrot";
    }

    @Override
    public String getInvoke() {
        return "say";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<statement>`";
    }
}

