/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.database.SQLiteDataSource;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Prefix
implements ICommand {
	
	@Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        TextChannel channel = event.getChannel();
        String id = event.getGuild().getId();
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage("You don't have the required permission! `Administrator`").queue();
            return;
        }
        if (args.isEmpty()) {
            channel.sendMessage(this.getUsage()).queue();
            return;
        }
        String newPrefix = args.get(0);
        
        SQLiteUtil.updatePrefix(id, newPrefix, event);
        
        channel.sendMessage("Prefix has been set to " + newPrefix).queue();
    }

    @Override
    public String getHelp() {
        return "Set's prefix for this server";
    }

    @Override
    public String getInvoke() {
        return "prefix";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `<prefix>`";
    }
}

