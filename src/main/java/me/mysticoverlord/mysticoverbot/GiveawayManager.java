package me.mysticoverlord.mysticoverbot;

import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.mysticoverlord.mysticoverbot.objects.ExceptionHandler;
import me.mysticoverlord.mysticoverbot.objects.FormatUtil;
import me.mysticoverlord.mysticoverbot.objects.GiveawayUtil;
import me.mysticoverlord.mysticoverbot.objects.SQLiteUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class GiveawayManager implements Runnable{
    Logger logger = LoggerFactory.getLogger(GiveawayManager.class);
    protected ArrayList<ScheduledFuture<?>> scheduledExecutorServices;
    protected JDA jda;
	
	public GiveawayManager(JDA jda) {
	    this.scheduledExecutorServices =  new ArrayList<ScheduledFuture<?>>();
		this.jda = jda;
	}

	@Override
	public void run() {
		Runnable finished = () -> {
			endFinished();
		};
		//TODO Somehow make them work with using just one Runnable instead of 4 because this is nasty
		Runnable updateTimeDay = () -> {
			HashMap<String, Long> map = SQLiteUtil.getAfter(Instant.now().toEpochMilli() + 86400000);
			for (Map.Entry<String, Long> entry : map.entrySet()) {
				String[] Ids = entry.getKey().split("-");
				Instant instant = Instant.ofEpochMilli(entry.getValue());
				updateTime(Ids, instant);
			}
		};
		Runnable updateTimeHour = () -> {
			HashMap<String, Long> map = SQLiteUtil.getBetween(Instant.now().toEpochMilli() + 86400000, Instant.now().toEpochMilli() + 3600000);
			for (Map.Entry<String, Long> entry : map.entrySet()) {
				String[] Ids = entry.getKey().split("-");
				Instant instant = Instant.ofEpochMilli(entry.getValue());
				updateTime(Ids, instant);
			}
		};
		Runnable updateTimeMinute = () -> {
			HashMap<String, Long> map = SQLiteUtil.getBetween(Instant.now().toEpochMilli() + 3600000, Instant.now().toEpochMilli() + 120000);
			for (Map.Entry<String, Long> entry : map.entrySet()) {
				String[] Ids = entry.getKey().split("-");
				Instant instant = Instant.ofEpochMilli(entry.getValue());
				updateTime(Ids, instant);
			}
		};
		Runnable updateTimeSeconds = () -> {
			HashMap<String, Long> map = SQLiteUtil.getBefore(Instant.now().toEpochMilli() + 120000);
			for (Map.Entry<String, Long> entry : map.entrySet()) {
				String[] Ids = entry.getKey().split("-");
				Instant instant = Instant.ofEpochMilli(entry.getValue());
				updateTime(Ids, instant);
			}
		};
		
      logger.info("GiveawayManager started!");
      endFinished();
      scheduledExecutorServices.add(Executors.newScheduledThreadPool(1).scheduleAtFixedRate(finished, 30, 30, TimeUnit.SECONDS));
      scheduledExecutorServices.add(Executors.newScheduledThreadPool(1).scheduleAtFixedRate(updateTimeSeconds, 0, 30, TimeUnit.SECONDS));
      scheduledExecutorServices.add(Executors.newScheduledThreadPool(1).scheduleAtFixedRate(updateTimeMinute, 0, 5, TimeUnit.MINUTES));
      scheduledExecutorServices.add(Executors.newScheduledThreadPool(1).scheduleAtFixedRate(updateTimeHour, 0, 90, TimeUnit.MINUTES));
      scheduledExecutorServices.add(Executors.newScheduledThreadPool(1).scheduleAtFixedRate(updateTimeDay, 0, 12, TimeUnit.HOURS));
      logger.info("Executor Services have been initiated!");
	}
	
	public void shutdownManager() {
		this.scheduledExecutorServices.forEach((service) -> {
			while(!service.isDone()) {
				service.cancel(true);
			}
		});
		endFinished();
	}
	
	private void endFinished() {
		HashMap<String, Long> map = SQLiteUtil.getBefore(Instant.now().toEpochMilli());
		for (Map.Entry<String, Long> entry : map.entrySet()) {
  			String[] Ids = entry.getKey().split("-");
  			Instant instant = Instant.ofEpochMilli(entry.getValue());
    		if (instant.isBefore(Instant.now())) {
    			TextChannel channel;
    			Message message;
    			try {
    			channel = jda.getTextChannelById(Ids[0]);
    			message = channel.retrieveMessageById(Ids[1]).complete();
    			} catch (Exception e) {
    				SQLiteUtil.deleteGiveaway(Ids[1]);
    				continue;
    			}
    			GiveawayUtil.endGiveaway(channel, message);
    		} else {
    			updateTime(Ids, instant);
    		}
    	}
	}
	
	private void updateTime(String[] Ids, Instant instant) {
		try {
			Message message = null;
			TextChannel channel = jda.getTextChannelById(Ids[0]);
			try {
		 message = channel.retrieveMessageById(Ids[1]).complete();
			} catch (ErrorResponseException | NullPointerException e) {
				SQLiteUtil.deleteGiveaway(Ids[1]);
				logger.debug("Giveaway " + Ids[0] + "-" + Ids[1] + " could not be retrieved and has been deleted!");
				return;
			}
		MessageEmbed embed = message.getEmbeds().get(0);
		EmbedBuilder builder = new EmbedBuilder(embed);
		Long seconds = instant.getEpochSecond() - Instant.now().getEpochSecond();
		String time = FormatUtil.formatDayTime(seconds);
		if (seconds < 20) {
			builder.setColor(Color.RED);
			time = "ends soon!";
		} else {
			builder.setColor(Main.PURPLE);
		}
		builder.setDescription("React with :gift: to Enter!\n\nTime Remaining: " + time);
		message.editMessageEmbeds(builder.build()).queue();
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

}
