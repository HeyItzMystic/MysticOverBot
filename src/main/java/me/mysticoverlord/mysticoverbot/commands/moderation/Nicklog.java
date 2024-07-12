package me.mysticoverlord.mysticoverbot.commands.moderation;

import java.util.List;

import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Nicklog implements ICommand{

	@SuppressWarnings("unchecked")
	@Override
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		Member author = event.getMember();
		
		if (!author.hasPermission(Permission.ADMINISTRATOR)) {
			event.getChannel().sendMessage("This command requires the `Administrator` Permission!").queue();
		return;
		}
		
		if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_SERVER)) {
			event.getChannel().sendMessage("I don't have the `Manage Server` Permission!").queue();
			return;
		}

		if (args.isEmpty()) {

	                        Boolean bool = SQLiteUtil.getBoolean(event.getGuild().getId(), "nicklog");
	                        event.getChannel().sendMessage("Nicklog " + String.valueOf(bool == true ? "is Aktive!" : "is currently deactivated!")).queue();

		 return;
		}
		
		if (args.get(0).equals("disable")) {

				SQLiteUtil.updateBoolean(event.getGuild().getId(), "nicklog", false, event);
				event.getChannel().sendMessage("Nicklog has been disabled!").queue();


		} else if (args.get(0).equals("enable")) {

			SQLiteUtil.updateBoolean(event.getGuild().getId(), "nicklog", true, event);
				event.getChannel().sendMessage("Nickname changes will now be logged!").queue();
				
				try {
					event.getGuild().getTextChannelsByName("nick-log", true).get(0).sendMessage("Nicklog has been Activated!\nAll occurences will be logged here!").queue();
				} catch (IndexOutOfBoundsException e) {
				  try {
					  event.getGuild().getTextChannelsByName("log", true).get(0).sendMessage("Nicklog has been Activated!\nAll occurences will be logged here!\nIf you wish to have nickname changes logged elsewhere please make a channel named #nick-log!").queue();
					  
				  } catch (IndexOutOfBoundsException e1) {

					    
				  }
				} 
			

		 }
		else {

                Boolean bool = SQLiteUtil.getBoolean(event.getGuild().getId(), "nicklog");
                event.getChannel().sendMessage("Nicklog " + String.valueOf(bool == true ? "is Aktive!" : "is currently deactivated!")).queue();

		}
		
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Turns the nicklog on/off";
	}

	@Override
	public String getInvoke() {
		// TODO Auto-generated method stub
		return "nicklog";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return Constants.PREFIX + getInvoke() + " [enable/disable]";
	}

}
