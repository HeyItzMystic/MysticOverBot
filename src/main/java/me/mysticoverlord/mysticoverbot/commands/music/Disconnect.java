/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.music;

import java.util.List;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.music.PlayerManager;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.IMusic;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class Disconnect
implements IMusic {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        AudioManager audio = event.getGuild().getAudioManager();
        if (!audio.isConnected()) {
            channel.sendMessage("i'm not connecte to a voice channel").queue();
            return;
        }
        VoiceChannel vc = audio.getConnectedChannel();
        if (!vc.getMembers().contains(event.getMember())) {
            channel.sendMessage("You have to be in my voice channel to make me leave").queue();
            return;
        }
        if (isDJ(event.getMember()) || event.getMember().hasPermission(Permission.MANAGE_SERVER) || vc.getMembers().stream().distinct().filter(u -> !u.getUser().isBot()).count() < 2) {
            Constants.loop.replace(event.getGuild().getIdLong(), false);
    		Constants.loopqueue.replace(event.getGuild().getIdLong(), false);
    		PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).scheduler.getQueue().clear();
    		PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).scheduler.nextTrack();
            audio.closeAudioConnection();
            channel.sendMessage("Successfully disconnected!").queue();
        } else {
        	channel.sendMessage("You need a role name DJ or the `Manage Server` permission to use this command.\nOr simply be alone!").queue();
        }

    }

    @Override
    public String getHelp() {
        return "I'll leave the channel";
    }

    @Override
    public String getInvoke() {
        return "disconnect";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke();
    }
    
	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "d";
	}
    
    private Boolean isDJ(Member member) {
    for (int x = 0; x < member.getRoles().size(); x++) {
    	if (member.getRoles().get(x).getName().equalsIgnoreCase("DJ")) {
    		return true;
    	}
    }
    	return false;
    }

}

