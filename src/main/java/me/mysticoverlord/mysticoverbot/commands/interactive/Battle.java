/*
 * Decompiled with CFR 0.145.
 */
package me.mysticoverlord.mysticoverbot.commands.interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.mysticoverlord.mysticoverbot.Constants;
import me.mysticoverlord.mysticoverbot.objects.ICommand;
import me.mysticoverlord.mysticoverbot.objects.FilterUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Battle
implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String challanger;
        String challanged;
        TextChannel channel = event.getChannel();
        ArrayList<String> mentioned = new ArrayList<String>();
        Random r1 = new Random();
        
        if (args.size() >= 1) {
        	try {
            	mentioned.add(FilterUtil.getMemberByQuery(args.get(0), event, 0).getUser().getName());
            	} catch (NullPointerException e) {
            		
            	}
          if (args.size() >= 2) {
        	  try {
              	mentioned.add(FilterUtil.getMemberByQuery(args.get(1), event, 1).getUser().getName());
              	} catch (NullPointerException e) {
              		
              	}
          }
        if (event.getMessage().getMentions(new Message.MentionType[0]).size() >= 1 && mentioned.size() == 0) {
        	
            mentioned.add(((User)event.getMessage().getMentions(new Message.MentionType[0]).get(0)).getName().toString());
            
        }
        if (event.getMessage().getMentions(new Message.MentionType[0]).size() >= 2 && mentioned.size() == 1) {
        	
        }
        }
        
        if (mentioned.size() == 0) {
        	challanger = event.getAuthor().getName();
        	challanged = event.getGuild().getMembers().get(r1.nextInt(event.getGuild().getMembers().size())).getEffectiveName();
        } else if (mentioned.size() >= 2) {
            challanger = mentioned.get(0);
            challanged = mentioned.get(1);
        }
        else {
            challanger = event.getAuthor().getName();
            challanged = mentioned.get(0);
           
        }
        ArrayList<String> challangeractions = new ArrayList<String>();
        
        challangeractions.add(":crossed_swords:" + challanger + " has given " + challanged + " a wet willy for ");
        challangeractions.add(":crossed_swords:" + challanger + " bites " + challanged + " in the ear for ");
        challangeractions.add(":crossed_swords:" + challanger + " slaps " + challanged + " for ");
        challangeractions.add(":crossed_swords:" + challanger + " kicked " + challanged + " for ");
        challangeractions.add(":crossed_swords:" + challanger + " hit " + challanged + " with a baseball bat for ");
        challangeractions.add(":crossed_swords:" + challanger + " poisoned " + challanged + "'s food for ");
        challangeractions.add(":crossed_swords:" + challanger + " whips " + challanged + " for ");
        challangeractions.add(":crossed_swords:" + challanger + " splits " + challanged + "'s head with an axe for ");
        challangeractions.add(":crossed_swords:" + challanger + " stabs " + challanged + " with a knife for ");
        challangeractions.add(":crossed_swords:" + challanger + " shoots " + challanged + " for ");
        challangeractions.add(":crossed_swords:" + challanger + " runs " + challanged + " over with a car for ");
        challangeractions.add(":crossed_swords:" + challanger + " smashed " + challanged + " with a ban hammer for ");
        challangeractions.add(":crossed_swords:" + challanged + " has activated " + challanger + "'s trapcard and took ");
        challangeractions.add(":crossed_swords:" + challanger + " put on some earrape on " + challanged + "'s headphones for ");
        challangeractions.add(":crossed_swords:" + challanger + " released the hounds on " + challanged + " for ");
        challangeractions.add(":crossed_swords:" + challanger + " burns " + challanged + " for ");
        challangeractions.add(":crossed_swords:" + challanger + " has thrown a bomb on " + challanged + " for ");
        challangeractions.add(":crossed_swords:" + challanger + " has smitten " + challanged + " for ");
        
        
        ArrayList<String> challangedactions = new ArrayList<String>();
        
        challangedactions.add(":shield:" + challanged + " has given " + challanger + " a wet willy for ");
        challangedactions.add(":shield:" + challanged + " bites " + challanger + " in the ear for ");
        challangedactions.add(":shield:" + challanged + " slaps " + challanger + " for ");
        challangedactions.add(":shield:" + challanged + " kicked " + challanger + " for ");
        challangedactions.add(":shield:" + challanged + " hit " + challanger + " with a baseball bat for ");
        challangedactions.add(":shield:" + challanged + " has poisoned " + challanger + "'s food for ");
        challangedactions.add(":shield:" + challanged + " whips " + challanger + " for ");
        challangedactions.add(":shield:" + challanged + " splits " + challanger + "'s head with an axe for ");
        challangedactions.add(":shield:" + challanged + " stabs " + challanger + " with a knife for ");
        challangedactions.add(":shield:" + challanged + " shoots " + challanger + " for ");
        challangedactions.add(":shield:" + challanged + " runs " + challanger + " over with a car for ");
        challangedactions.add(":shield:" + challanged + " smashed " + challanger + " with a ban hammer for ");
        challangeractions.add(":shield:" + challanger + " has activated " + challanged + "'s trapcard and took ");
        challangedactions.add(":shield:" + challanged + " put on some earrape on " + challanger + "'s headphones for ");
        challangedactions.add(":shield:" + challanged + " released the hounds on " + challanger + " for ");
        challangedactions.add(":shield:" + challanged + " burns " + challanger + " for ");
        challangedactions.add(":shield:" + challanged + " has thrown a bomb on " + challanger + " for ");
        challangedactions.add(":shield:" + challanged + " has smitten " + challanger + " for ");
        
        
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setTitle(String.valueOf(challanger) + " vs " + challanged);
        channel.sendMessage(embed.build()).queue(message -> {
        	
            Timer timer = new Timer();
            String s1s = ":boom: Battle started!\n";
            int player_1 = 100;
            int player_2 = 100;
            
            EmbedBuilder builder1 = embed.appendDescription(s1s);
            builder1.addField(challanger, String.valueOf(player_1) + "HP", true)
            .addField(challanged, String.valueOf(player_2) + "HP", true);
        
            try {
                TimeUnit.SECONDS.sleep(2L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            message.editMessage(builder1.build()).queue();
            
            int action = r1.nextInt(challangeractions.size());
            int damage = action + 5;
            String s2s = String.valueOf((String)challangeractions.get(action)) + damage + "dmg" + "\n";
            builder1.appendDescription(s2s);
            player_2 = (player_2 -= damage) <= 0 ? 0 : player_2;
            builder1.clearFields()
            .addField(challanger, String.valueOf(player_1) + "HP", true)
            .addField(challanged, String.valueOf(player_2) + "HP", true);
            
            try {
                TimeUnit.SECONDS.sleep(2L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            message.editMessage(builder1.build()).queue();
            action = r1.nextInt(challangedactions.size());
            damage = action + r1.nextInt(9);
            String s3s = String.valueOf((String)challangedactions.get(action)) + damage + "dmg" + "\n";
            builder1.appendDescription(s3s);
            player_1 = (player_1 -= damage) <= 0 ? 0 : player_1;
            builder1.clearFields()
            .addField(challanger, String.valueOf(player_1) + "HP", true)
            .addField(challanged, String.valueOf(player_2) + "HP", true);
           
            try {
                TimeUnit.SECONDS.sleep(2L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            message.editMessage(builder1.build()).queue();
            try { 
            	TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
            String p1 = Integer.toString(player_1);
            String p2 = Integer.toString(player_2);
            timer.schedule(new TimerTask(){
                EmbedBuilder builder = builder1;
                int damage;
                int action;
                int player_1 = Integer.parseInt(p1);
                int player_2 = Integer.parseInt(p2);
                Random r = r1;
                String s1 = s1s;
                String s2 = s2s;
                String s3 = s3s;
            

                @Override
                public void run() {
                	if (player_1 <= 0) {
                        builder.getDescriptionBuilder().replace(0, s1.length(), "");
                        s1 = ":trophy:" + challanged + " has won!";
                        builder.getDescriptionBuilder().append(s1);
                        message.editMessage(builder.build()).queue();
                        timer.cancel();
                        return;
                    }
                	builder.getDescriptionBuilder().replace(0, s1.length(), "");
                    action = r.nextInt(challangeractions.size());
                    damage = action + r.nextInt(9);
                    s1 = challangeractions.get(action) + damage + "dmg" + "\n";
                    builder.getDescriptionBuilder().append(s1);
                    player_2 -= damage;
                    player_2 = player_2 <= 0 ? 0 : player_2;
                    builder.clearFields()
                    .addField(challanger, player_1 + "HP", true)
                    .addField(challanged, player_2 + "HP", true);
                    message.editMessage(builder.build()).queue();
                    try {
                        TimeUnit.SECONDS.sleep(2L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (player_2 <= 0) {
                       builder.getDescriptionBuilder().replace(0, s2.length(), "");
                        s2 = ":trophy:" + challanger + " has won!";
                        builder.getDescriptionBuilder().append(s2);
                        message.editMessage(builder.build()).queue();
                        timer.cancel();
                        return;
                    }
                    builder.getDescriptionBuilder().replace(0, s2.length(), "");
                    action = r.nextInt(challangedactions.size());
                    damage = action + r.nextInt(9);
                    s2 = challangedactions.get(action) + damage + "dmg" + "\n";
                    builder.getDescriptionBuilder().append(s2);
                    player_1 -= damage;
                    player_1 = player_1 <= 0 ? 0 : player_1;
                    builder.clearFields().addField(challanger, player_1 + "HP", true)
                    .addField(challanged, player_2 + "HP", true);
                    message.editMessage(builder.build()).queue();
                    try {
                        TimeUnit.SECONDS.sleep(2L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (player_1 <= 0) {
                        builder.getDescriptionBuilder().replace(0, s3.length(), "");
                        s1 = ":trophy:" + challanged + " has won!";
                        builder.getDescriptionBuilder().append(s1);
                        message.editMessage(builder.build()).queue();
                        timer.cancel();
                        return;
                    }
                    builder.getDescriptionBuilder().replace(0, s3.length(), "");
                    action = r.nextInt(challangeractions.size());
                    damage = action + r.nextInt(9);
                    s3 = challangeractions.get(action) + damage + "dmg" + "\n";
                    builder.getDescriptionBuilder().append(s3);
                    player_2 -= damage;
                    player_2 = player_2 <= 0 ? 0 : player_2;
                    builder.clearFields()
                    .addField(challanger, player_1 + "HP", true)
                    .addField(challanged, player_2 + "HP", true);
                    message.editMessage(builder.build()).queue();
                    try {
                        TimeUnit.SECONDS.sleep(2L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (player_2 <= 0) {
                         builder.getDescriptionBuilder().replace(0, s1.length(), "");
                         s2 = ":trophy:" + challanger + " has won!";
                         builder.getDescriptionBuilder().append(s2);
                         message.editMessage(builder.build()).queue();
                         timer.cancel();
                         return;
                     }
                    builder.getDescriptionBuilder().replace(0, s1.length(), "");
                    action = r.nextInt(challangedactions.size());
                    damage = action + r.nextInt(9);
                    s1 = challangedactions.get(action) + damage + "dmg" + "\n";
                    builder.getDescriptionBuilder().append(s1);
                    player_1 -= damage;
                    player_1 = player_1 <= 0 ? 0 : player_1;
                    builder.clearFields()
                    .addField(challanger, player_1 + "HP", true)
                    .addField(challanged, + player_2 + "HP", true);
                    message.editMessage(builder.build()).queue();
                    
                    try {
                        TimeUnit.SECONDS.sleep(2L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    if (player_1 <= 0) {
                    	builder.getDescriptionBuilder().replace(0, s2.length(), "");
                        s1 = ":trophy:" + challanged + " has won!";
                        builder.getDescriptionBuilder().append(s1);
                        message.editMessage(builder.build()).queue();
                        timer.cancel();
                        return;
                    }
                    builder.getDescriptionBuilder().replace(0, s2.length(), "");
                    action = r.nextInt(challangedactions.size());
                    damage = action + r.nextInt(9);
                    s2 = challangeractions.get(action) + damage + "dmg" + "\n";
                    builder.getDescriptionBuilder().append(s2);
                    player_2 -= damage;
                    player_2 = player_2 <= 0 ? 0 : player_2;
                    builder.clearFields()
                    .addField(challanger, player_1 + "HP", true)
                    .addField(challanged, player_2 + "HP", true);
                    message.editMessage(builder.build()).queue();
                    try {
                        TimeUnit.SECONDS.sleep(2L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    if (player_2 <= 0) {
                         builder.getDescriptionBuilder().replace(0, s3.length(), "");
                         s2 = ":trophy:" + challanger + " has won!";
                         builder.getDescriptionBuilder().append(s2);
                         message.editMessage(builder.build()).queue();
                         timer.cancel();
                         return;
                     }
                    builder.getDescriptionBuilder().replace(0, s3.length(), "");
                    action = r.nextInt(challangedactions.size());
                    damage = action + r.nextInt(9);
                    s3 = challangedactions.get(action) + damage + "dmg" + "\n";
                    builder.getDescriptionBuilder().append(s3);
                    player_1 -= damage;
                    player_1 = player_1 <= 0 ? 0 : player_1;
                    builder.clearFields()
                    .addField(challanger, player_1 + "HP", true)
                    .addField(challanged, player_2 + "HP", true);
                    message.editMessage(builder.build()).queue();
                }
            }, 0L, 12000L);
        });
    }

    @Override
    public String getHelp() {
        return "Start an epic battle with any guild member";
    }

    @Override
    public String getInvoke() {
        return "battle";
    }

    @Override
    public String getUsage() {
        return "Usage: " + Constants.PREFIX + this.getInvoke() + " `[@user1] [@user2]`";
    }
    
}

