/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands;


import java.util.ArrayList;
import java.util.List;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Avatar
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
    	User author = event.getAuthor();
        ArrayList<User> mentioned = new ArrayList<User>();
        
        if (args.size() >= 1) {
            try {
            	mentioned.add(FilterUtil.getMemberByQuery(String.join(" ", args), event, 0).getUser());
            	} catch (NullPointerException e) {
            		
            	}
            
            if (event.getMessage().getMentions(new Message.MentionType[0]).size() >= 1 && mentioned.size() == 0) {
            	
                mentioned.add(event.getJDA().getUserById((event.getMessage().getMentions(new Message.MentionType[0]).get(0).getId())));
                
            }
            }
        
        if (mentioned.size() == 0) {
        	if (args.size() != 0) {
        		event.getChannel().sendMessage("No User found under the query `" + args.toString() + "`").queue();
       	} else {
       		mentioned.add(author);
       	}
        }
        User user = mentioned.get(0);
        MessageEmbed builder = EmbedUtils.getDefaultEmbed().setTitle(String.valueOf(user.getName()) + "'s Avatar").setImage(user.getEffectiveAvatarUrl()).build();
        event.getChannel().sendMessage(builder).queue();
    }

    @Override
    public String getHelp() {
        return "Gives you the avatar of the specified user";
    }

    @Override
    public String getInvoke() {
        return "avatar";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<@User>`";
    }
}

