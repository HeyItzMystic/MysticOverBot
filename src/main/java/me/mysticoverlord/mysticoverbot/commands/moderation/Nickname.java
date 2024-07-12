package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Nickname implements ICommand {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		Member self = event.getGuild().getSelfMember();
		Member author = event.getMember();
		TextChannel channel = event.getChannel();
		
        if (args.isEmpty()) {
            channel.sendMessage("Please specify the targeted user!\n" + this.getUsage()).queue();
            return;
        }
        Member target = FilterUtil.getMemberByData(args.get(0), event, 0);
        
        if (target == null) {
            channel.sendMessage("ID or Mention had no match!").queue();
            return;
        }
		
		
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
		if (mentionedMembers.isEmpty()) {
			event.getChannel().sendMessage("Please mention a user!").queue();
			return;
		}
		
		if (!self.hasPermission(Permission.NICKNAME_MANAGE)) {
			channel.sendMessage("I don't have the Permission: `Manage Nicknames").queue();
			return;
		}
		
		if (!author.hasPermission(Permission.NICKNAME_MANAGE)) {
			channel.sendMessage("You are missing the Permission: `Manage Nicknames`").queue();
			return;
		}
	
		if (!self.canInteract(target)) {
			channel.sendMessage("I can't interact with members higher than or equal to my own role!").queue();
			return;
		}
		
		if (!author.canInteract(target)) {
			channel.sendMessage("You can't interact with members higher than or equal to your own role!").queue();
			return;
		}
		
		String nickname = args.toString().replace(args.get(0), "").replace("[, ", "").replace("]", "").replaceAll(",", "");
	
		if (nickname.isBlank()) {
			channel.sendMessage("Missing parameter: `nickname`").queue();
			return;
		}
		
		if (nickname.length() > 32 || nickname.length() < 3) {
			channel.sendMessage("The nickname has to be between 3 and 32 characters in size").queue();
			return;
		}
		
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setTitle("Modified Nickname of " + event.getJDA().getUserById(target.getId()).getName())
				.addField("Moderator", author.getAsMention(), false)
				.addField("Old Nickname", target.getEffectiveName(), true);
		
		target.modifyNickname(nickname).queue();
		
		builder.addField("New Nickname", nickname, true);
		
		builder.addField("Member ID + Mention", target.getId() + "(" + target.getAsMention() + ")", false);
		
		channel.sendMessage(builder.build()).queue();
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "changes nickname of the specified target";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "nick";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return Constants.PREFIX + getInvoke() + " <@User> <nickname>";
	}

}
