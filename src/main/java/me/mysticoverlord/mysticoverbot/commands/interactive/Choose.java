/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.interactive;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
public class Choose
implements ICommand {
    @Override
    public void handle(List<String> args, final GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please add at least 2 objects out of which i can choose!").queue();
            event.getChannel().sendMessage(this.getUsage());
            return;
        }
        String raw = event.getMessage().getContentDisplay();
        String[] message = raw.replace(String.valueOf(Constants.PREFIX) + this.getInvoke() + " ", "").split(" or ");
        if (message.length < 2) {
            event.getChannel().sendMessage("Please add at least 2 objects out of which i can choose!").queue();
            return;
        }
        Random r = new Random();
        event.getChannel().sendMessage(String.valueOf(event.getAuthor().getAsMention()) + "!\nI have chosen **" + message[r.nextInt(message.length)] + "**!").queue();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            int x = 0;
            long id = event.getMessageIdLong();
            

            @Override
            public void run() {
                ++this.x;
                if (event.getChannel().retrieveMessageById(this.id) != null) {
                    event.getChannel().retrieveMessageById(this.id).queue(message -> {
                        if (message.isEdited()) {
                            event.getChannel().sendMessage(String.valueOf(event.getAuthor().getAsMention()) + " you have edited your message!\nYour o!choose input is no longer valid!").queue();
                            timer.cancel();
                        }
                    });
                } else {
                    timer.cancel();
                }
                if (this.x == 60) {
                    timer.cancel();
                }
            }
        }, 0L, 1000L);
    }

    @Override
    public String getHelp() {
        return "Give me objects and i choose one";
    }

    @Override
    public String getInvoke() {
        return "choose";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<obj1> <or> <obj2> [or] [obj3] etc.`";
    }

}

