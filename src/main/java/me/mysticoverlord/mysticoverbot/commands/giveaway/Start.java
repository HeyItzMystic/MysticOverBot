package me.mysticoverlord.mysticoverbot.commands.giveaway;

import java.time.Instant;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ExceptionHandler;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import me.mysticoverlord.mysticoverbot.objects.FormatUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Start implements ICommand{


	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			event.getChannel().sendMessage("You don't have the Administrator Permission!").queue();
			return;
		}
		if (args.size() < 5) {
			event.getChannel().sendMessage("No arguments were given!\n " + getUsage()).queue();
			return;
		}
	
		try {
		TextChannel selectedChannel = FilterUtil.getChannelByData(args.get(0), event, 0);
		if (selectedChannel == null) {
			event.getChannel().sendMessage("The channel you gave does not exist").queue();
			return;
		}
		
		if (!event.getGuild().getSelfMember().hasPermission(selectedChannel, Permission.MESSAGE_WRITE, Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY)) {
			event.getChannel().sendMessage("I need following permissions to be able to manage a giveaway in this channel: `Send Messages, View Channel, Read Message History`").queue();
			return;
		}
		
		
		Integer minutes = Integer.parseInt(args.get(3).replaceFirst("m", ""));		
		Integer hours = Integer.parseInt(args.get(2).replaceFirst("h", ""));		
		Integer days = Integer.parseInt(args.get(1).replaceFirst("d", ""));
		Integer seconds = 0;
		
		if (days != 0) {
			hours += days * 24;
		}
		if (hours != 0) {
			minutes += hours * 60;
		}
		if (minutes != 0) {
			seconds += minutes * 60;
		}
		
		String segment = String.join((CharSequence)" ", args.subList(4, args.size()));    
		Integer amount = Integer.parseInt(segment.substring(segment.indexOf("|") + 1).replaceAll("|", "").replaceAll(" ", ""));
		if (amount < 1) {
			event.getChannel().sendMessage("The amount of winners may not be below 1!").queue();
			return;
		}
		String prize = segment.replace(segment.substring(segment.indexOf("|")), "");
		
		Instant end = Instant.now().plusSeconds(seconds.longValue());
		String time = FormatUtil.formatDayTime(seconds.longValue());
		
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setTitle(prize)
				.setDescription("React with :gift: to Enter!\n\nTime Remaining: " + time)
				.setFooter(amount.toString() + " Winners | Ends at")
				.setTimestamp(end);
		selectedChannel.sendMessage("**GIVEAWAY** :gift:").queue();
		selectedChannel.sendMessage(builder.build()).queue((message) -> {
			try {
			long date = end.toEpochMilli();
			String channelID = message.getChannel().getId();
			String messageID = message.getId();
			SQLiteUtil.insertGiveaway(channelID, messageID, date, event);
			event.getChannel().sendMessage("Giveaway for " + amount.toString() + "x " + prize + " has been started in " + selectedChannel.getAsMention()).queue();
			message.addReaction("\uD83C\uDF81").queue();
			} catch (Exception e) {
				message.delete().queue();
				throw (e);
			}
		});
		
		} catch (Exception e) {
			ExceptionHandler.handle(e);
			event.getChannel().sendMessage("Something went wrong while checking the arguments!\nPlease check if you have provided all the arguments necessary!\n" + getUsage()).queue();
		}
		
		
	}
	
	@Override
	public String getHelp() {
		return "Starts a new Giveaway";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "gstart";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage:\n " + Constants.PREFIX + "gstart `<#channel/channelID> <d# h# m#>` `<prize | amount>`\nExample: "
		+ Constants.PREFIX + "gstart #giveaway d5 h0 m0 Discord Nitro Classic 1 Month | 3";
	}
}
