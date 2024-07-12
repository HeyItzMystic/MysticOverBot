package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.util.List;

import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class AntiInvite implements ICommand{

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

	                        Boolean bool = SQLiteUtil.getBoolean(event.getGuild().getId(), "antiinvite");
	                        event.getChannel().sendMessage("AntiInvite " + String.valueOf(bool == true ? "is Active!" : "is currently deactivated!")).queue();

		 return;					
		}
		
		if (args.get(0).equals("disable")) {

				event.getChannel().sendMessage("AntiInvite has been disabled!").queue();
				SQLiteUtil.updateBoolean(event.getGuild().getId(), "antiinvite", false, event);

		} else if (args.get(0).equals("enable")) {

			SQLiteUtil.updateBoolean(event.getGuild().getId(), "antiinvite", true, event);
				event.getChannel().sendMessage("AntiInvite has been Activated!").queue();
				
				try {
					event.getGuild().getTextChannelsByName("invite-log", true).get(0).sendMessage("AntiInvite has been Activated!\nAll occurences will be logged here!").queue();
				} catch (IndexOutOfBoundsException e) {
				  try {
					  event.getGuild().getTextChannelsByName("log", true).get(0).sendMessage("AntiInvite has been Activated!\nAll occurences will be logged here!\nIf you wish to have invite occurrences logged elsewhere please make a channel named #invite-log!").queue();
					  
				  } catch (IndexOutOfBoundsException e1) {

					 event.getChannel().sendMessage("No #log or #invite-log channel has been detected!\nIf you wish for me to log all invite occurences please create a #log or #invite-log channel!").queue();
					
					}
				}


		} else {

                Boolean bool = SQLiteUtil.getBoolean(event.getGuild().getId(), "antiinvite");
                event.getChannel().sendMessage("AntiInvite " + String.valueOf(bool == true ? "is Active!" : "is currently deactivated!")).queue();

		}
		
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Turns the anti-Invite feature on/off";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "antiinvite";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return Constants.PREFIX + getInvoke() + " `[enable/disable]`";
	}

}
