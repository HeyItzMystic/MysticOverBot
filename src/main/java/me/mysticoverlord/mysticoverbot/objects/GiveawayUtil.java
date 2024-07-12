package me.mysticoverlord.mysticoverbot.objects;

import java.awt.Color;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class GiveawayUtil {
	
	public static List<User> selectWinners(Message message, int ammount) {
		MessageReaction mr = message.getReactions().stream().filter(r -> r.getReactionEmote().getName().equals("\uD83C\uDF81")).findAny().orElse(null);
		 List<User> reactions = new LinkedList<User>();
        mr.retrieveUsers().stream().distinct().filter(u -> !u.isBot()).forEach(u -> reactions.add(u));
		List<User> winners = new LinkedList<User>();
		for (int x = 0; x < ammount; x++) {
			if (reactions.size() < 1) {
				break;
			}
			int num = new Random().nextInt(reactions.size());
			winners.add(reactions.get(num));
			reactions.remove(num);
		}
		return winners;
	}
	
	public static void endGiveaway(TextChannel channel, Message message) {
		try {
		MessageEmbed embed = message.getEmbeds().get(0);
		Integer amount = Integer.parseInt(embed.getFooter().getText().replace(" Winners | Ends at", ""));
		EmbedBuilder builder = new EmbedBuilder(embed);
		List<User> winners = selectWinners(message, amount);
		StringBuilder mentions = new StringBuilder();
		winners.forEach((winner) -> {
			mentions.append(winner.getAsMention() + "\n");
			channel.sendMessage("Congratulations " + winner.getAsMention() + " you've won the **" + embed.getTitle() + "**").queue();
		});
		builder.setColor(Color.DARK_GRAY)
		.setDescription("Winners: " + mentions.toString())
		.setTimestamp(Instant.now())
		.setFooter(amount.toString() + " Winners | Ended ");
		if (message.getMember().equals(channel.getGuild().getSelfMember())) {
			message.editMessageEmbeds(builder.build()).queue();
		}
		SQLiteUtil.deleteGiveaway(message.getId());
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

}
