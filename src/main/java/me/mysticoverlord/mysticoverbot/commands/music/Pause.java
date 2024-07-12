/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.music;

import java.util.List;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.music.GuildMusicManager;
import me.mysticoverlord.mysticoverbot.music.PlayerManager;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.MusicUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class Pause
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioManager audio = event.getGuild().getAudioManager();
        TextChannel channel = event.getChannel();
        VoiceChannel vc = audio.getConnectedChannel();
        if (MusicUtil.isDJ(event.getMember()) || event.getMember().hasPermission(Permission.MANAGE_SERVER) || vc.getMembers().stream().distinct().filter(u -> !u.getUser().isBot()).count() < 2) {
        	if (musicManager.player.isPaused()) {
                musicManager.player.setPaused(false);
                event.getChannel().sendMessage(":arrow_forward: Playing!").queue();
            } else {
            musicManager.player.setPaused(true);
            event.getChannel().sendMessage(":pause_button: Paused!").queue();
            }
        	
        } else {
        	channel.sendMessage("You can't pause or unpause me!\nOnly people with a `DJ` role or `Manage Server` Permission can do that!\nBeing alone works too though!").queue();            
        }
        
    }

    @Override
    public String getHelp() {
        return "Pauses/Resumes the queue playback";
    }

    @Override
    public String getInvoke() {
        return "pause";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke();
    }
}

