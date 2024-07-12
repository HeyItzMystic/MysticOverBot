package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.util.List;

import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MessageLog implements ICommand {

	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		Member author = event.getMember();
		
		if (!author.hasPermission(Permission.ADMINISTRATOR)) {
			event.getChannel().sendMessage("This command requires the `Administrator` Permission!").queue();
		return;
		}
		
		if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			event.getChannel().sendMessage("I don't have the `Manage Messages` Permission!").queue();
			return;
		}

		if (args.isEmpty()) {

	                        Boolean bool = SQLiteUtil.getBoolean(event.getGuild().getId(), "message_log");
	                        event.getChannel().sendMessage("Message Log " + String.valueOf(bool == true ? "is Active!" : "is currently deactivated!")).queue();

		 return;					
		}
		
		if (args.get(0).equals("disable")) {

				event.getChannel().sendMessage("Message Log has been disabled!").queue();
				SQLiteUtil.updateBoolean(event.getGuild().getId(), "message_log", false, event);

		} else if (args.get(0).equals("enable")) {


			event.getChannel().sendMessage("Message Log has been Activated!").queue();
			SQLiteUtil.updateBoolean(event.getGuild().getId(), "message_log", true, event);
				

				  try {
					  event.getGuild().getTextChannelsByName("log", true).get(0).sendMessage("Message Log has been Activated!\nAll deleted messages or message edits will be logged here!").queue();
				  }
					  
					catch (Exception e2) {
					 SQLiteUtil.updateBoolean(event.getGuild().getId(), "message_log", false, event);
					 event.getChannel().sendMessage("No #log channel has been detected!\nIf you wish for me to log messages please create a #log channel!").queue();
					}
					}
				


		 else {

                Boolean bool = SQLiteUtil.getBoolean(event.getGuild().getId(), "message_log");
                event.getChannel().sendMessage("Message Log " + String.valueOf(bool == true ? "is Active!" : "is currently deactivated!")).queue();

		}
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Enables/Disables Message log";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "log";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return Constants.PREFIX + getInvoke() + " [enable/disable]";
	}

}
