package me.mysticoverlord.mysticoverbot.commands.giveaway;

import java.awt.Color;
import java.time.Instant;
import java.util.List;

import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ExceptionHandler;
import me.mysticoverlord.mysticoverbot.objects.GiveawayUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class End implements ICommand{
	
	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			event.getChannel().sendMessage("You don't have the Administrator Permission!").queue();
			return;
		}
		if (args.isEmpty()) {
			event.getChannel().sendMessage("No arguments were given.\n" + getUsage()).queue();
			return;
		}
		
		String Id = args.get(0);
		if (Id == null) {
			return;
		}
		TextChannel channel = event.getGuild().getTextChannelById(SQLiteUtil.getGiveawayByID(Id));
		Message message;
		
		try {
			message = channel.retrieveMessageById(Id).complete();
		} catch (Exception e) {
			event.getChannel().sendMessage("The given MessageID yielded no results").queue();
			return;
		}
		
		SQLiteUtil.deleteGiveaway(Id);
		try {
		GiveawayUtil.endGiveaway(channel, message);
		} catch (Exception e) {
			event.getChannel().sendMessage("Couldn't end the specified giveaway! Please check if you provided the correct Id").queue();
		}
	}
	
	@Override
	public String getUsage() {
		return "Usage:\n" + Constants.PREFIX + "giveaway end `messageId of the running giveaway`";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "gend";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Ends a running giveaway and selects the winners";
	}

}
