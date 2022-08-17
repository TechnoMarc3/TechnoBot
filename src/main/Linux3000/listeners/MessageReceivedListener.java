package main.Linux3000.listeners;


import main.Linux3000.DiscordBot;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashMap;


public class MessageReceivedListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
			// commands

		String message1 = event.getMessage().getContentRaw();
		if (event.isFromType(ChannelType.TEXT)) {
			TextChannel channel1 = event.getChannel().asTextChannel();
			if (message1.startsWith("!")) {
				String[] args1 = message1.substring(1).split(" ");
				if (args1.length > 0) {
					try {
						if (!DiscordBot.INSTANCE.getCmdMan().perform(args1[0], event.getMember(), channel1,
								event.getMessage())) {
							channel1.sendMessage("``Unbekanntes Commando``").queue();
						}
					} catch (InterruptedException interruptedException) {
						interruptedException.printStackTrace();
					}


				}
			}

		}
		}}