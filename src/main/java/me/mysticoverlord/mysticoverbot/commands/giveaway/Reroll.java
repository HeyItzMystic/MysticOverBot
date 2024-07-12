package me.mysticoverlord.mysticoverbot.commands.giveaway;

import java.util.List;

import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import me.mysticoverlord.mysticoverbot.objects.GiveawayUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import java.util.concurrent.TimeUnit;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Reroll implements ICommand{

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
		TextChannel channel = FilterUtil.getChannelByData(args.get(0), event, 0);
		if (channel == null) {
			event.getChannel().sendMessage("Mention or ID yielded no results for Channels").queue();
			return;
		}
		Message message = channel.retrieveMessageById(args.get(1)).complete();
		if (message == null) {
			event.getChannel().sendMessage("MessageID yielded no results").queue();
			return;
		}
		try {
		MessageEmbed embed = message.getEmbeds().get(0);
		String prize = embed.getTitle();
		event.getChannel().sendMessage("Rerolling winner for " + prize + "...").queue((m) -> {
			m.delete().queueAfter(2L, TimeUnit.SECONDS);
		});
		String winner = GiveawayUtil.selectWinners(message, 1).get(0).getAsMention();
		event.getChannel().sendMessage("The new winner of " + prize + " is " + winner).queue();
		} catch (Exception e) {
			event.getChannel().sendMessage("Something went wrong while rerolling a new winner.\nPlease check if you have entered the correct messageId.").queue();
		}
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "greroll";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: " + Constants.PREFIX + getInvoke() + " `<channelId>` `<messageId>`";
	}


	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "rerolls a new winner for the selected giveaway";
	}

}
