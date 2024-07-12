/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.music;

import java.util.List;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.IMusic;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class Join
implements IMusic {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        AudioManager audio = event.getGuild().getAudioManager();
        if (audio.isConnected()) {
            channel.sendMessage("I'm already in a vc mate!").queue();
            return;
        }
        GuildVoiceState voicestate = event.getMember().getVoiceState();
        if (!voicestate.inVoiceChannel()) {
            channel.sendMessage("Join a voice channel first!").queue();
            return;
        }
        VoiceChannel vc = voicestate.getChannel();
        Member selfMember = event.getGuild().getSelfMember();
        if (!selfMember.hasPermission((GuildChannel)vc, Permission.VOICE_CONNECT)) {
            channel.sendMessage("I'm missing the permission `Voice Connect`!").queue();
            return;
        }
        audio.openAudioConnection(vc);
        channel.sendMessage("Joining the voicechannel " + vc.getName()).queue();
    }

    @Override
    public String getHelp() {
        return "I'll join your vc";
    }

    @Override
    public String getInvoke() {
        return "join";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke();
    }

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "j";
	}
}

