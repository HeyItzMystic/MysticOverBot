package me.mysticoverlord.mysticoverbot.commands.music;

import java.util.List;

import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.music.GuildMusicManager;
import me.mysticoverlord.mysticoverbot.music.PlayerManager;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Seek implements ICommand {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
	       GuildMusicManager musicManager = new PlayerManager().getGuildMusicManager(event.getGuild());
	       
	       musicManager.player.getPlayingTrack().setPosition(0);
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Seeks the given location of the track";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "seek";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: " + Constants.PREFIX + getInvoke() + " `<hh:mm:ss>`";
	}

}
