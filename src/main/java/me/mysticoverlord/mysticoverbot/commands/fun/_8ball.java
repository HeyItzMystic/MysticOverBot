/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class _8ball
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Random r = new Random();
        String[] message = event.getMessage().getContentRaw().split(" ");
        message[0] = null;
        int len = message.length;
        message = null;
        ArrayList<String> response = new ArrayList<String>();
        response.add("No!");
        response.add("Yes!");
        response.add("You know what?\nJust for you:\n No!");
        response.add("All signs point to no!");
        response.add("Come back later and maybe i'll give you an answer.");
        response.add("I'm not in the mood to answer this right now.");
        response.add("Do i look like i care?");
        response.add("Certainly!");
        response.add("Simply put:\nNo!");
        response.add("I have my doubts.");
        response.add("Without a doubt!");
        response.add("My buddy sais no so...\nNo!");
        response.add("Don't count on it buddy.");
        response.add("Come back later!");
        response.add("Ain't telling you now mate!");
        response.add("Maybe if you try asking a different question i might give you an answer!");
        response.add("Most certainly not!");
        response.add("Definetly.");
        response.add("You better rely on this man!");
        response.add("Is this important?");
        response.add("My reply is no.");
        response.add("Ok so you probably expect me to say:\n'Hmm... interesting... so by the looks of this the answer is'\nNo. ");
        response.add("Don't expect that to be true");
        response.add("Yeah thats about right");
        response.add("Unlikely.");
        response.add("Most likely.");
        response.add("As i see it, yes!");
        response.add("Yeh...\nI'm not gonna answer this one.");
        response.add("Ask a human!");
        response.add("Ask somebody else!");
        response.add("What do ya think?\nOf course not you idiot!");
        response.add("I sure hope not!");
        response.add("Of course you idiot!");
        response.add("I hope so.");
        if (len < 3) {
            int random = r.nextInt(3);
            switch (++random) {
                case 1: {
                    event.getChannel().sendMessage("You have to ask me a question!").queue();
                    break;
                }
                case 2: {
                    event.getChannel().sendMessage("I don't know what that is but it ain't a question!").queue();
                    break;
                }
                case 3: {
                    event.getChannel().sendMessage("May i advise you to ask a question with at least 3 words?").queue();
                }
            }
            return;
        }
        int random = r.nextInt(response.size());
        event.getChannel().sendMessage(":8ball:").queue();
        event.getChannel().sendMessage(response.get(random)).queue();
    }

    @Override
    public String getHelp() {
        return ":8ball:";
    }

    @Override
    public String getInvoke() {
        return "8ball";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<Question of at least 3 words>`";
    }
}

