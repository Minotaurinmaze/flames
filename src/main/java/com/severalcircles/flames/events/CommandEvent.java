/*
 * Copyright (c) 2021 Several Circles.
 */

package com.severalcircles.flames.events;

import com.severalcircles.flames.command.FlamesCommand;
import com.severalcircles.flames.data.base.ConsentException;
import com.severalcircles.flames.data.base.FlamesDataManager;
import com.severalcircles.flames.system.Flames;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandEvent extends ListenerAdapter implements FlamesDiscordEvent {

    public void register(JDA api) {
        api.addEventListener(new CommandEvent());
    }
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        System.out.println("Slash command detected");
        System.out.println(event.getName());
        for (Map.Entry<String, FlamesCommand> entry: Flames.commandMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getKey().contains(event.getName()));
            if (entry.getKey().contains(event.getName())) {
                try {
                    System.out.println(Flames.commandMap.get(entry.getKey()));
                    Flames.commandMap.get(entry.getKey()).execute(event, FlamesDataManager.readUser(event.getUser()));
                } catch (IOException e) {
                    e.printStackTrace();
                    Flames.incrementErrorCount();
                } catch (IllegalStateException e) {
                    Logger.getGlobal().log(Level.INFO, "Somebody pressed a button :3");
                } catch (ConsentException e) {
                    event.reply("Hey bestie, looks like you opted not to have your data collected by Flames. You can't have your cake but then not eat it, so would you mind eating the cake first? Thanks!");
                }
            }
        }
    }
}
