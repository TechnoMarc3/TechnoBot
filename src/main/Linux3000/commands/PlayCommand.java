package main.Linux3000.commands;


import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.commands.types.AudioCommand;
import main.Linux3000.commands.types.ServerCommand;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.net.URL;

public class PlayCommand implements AudioCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {

		String[] args = message.getContentRaw().split(" ");

		if(args.length == 0) {
			GuildVoiceState state;
			if ((state = m.getVoiceState()) != null) {
				VoiceChannel vc;
				if ((vc = (VoiceChannel) state.getChannel()) != null)  {
					AudioManager manager = vc.getGuild().getAudioManager();
					manager.openAudioConnection(vc);


					System.out.println(message.getAttachments().get(0).getUrl());

					if(DiscordBot.INSTANCE.getPremiumManager().hasPremium(channel.getGuild())) {
						PremiumPlayerManager.getInstance().loadAndPlay(channel, message.getAttachments().get(0).getUrl());
					}else {
						PlayerManager.getInstance().loadAndPlay(channel, message.getAttachments().get(0).getUrl());
					}



				}}}
		if (args.length > 1) {
			GuildVoiceState state;
			if ((state = m.getVoiceState()) != null) {
				VoiceChannel vc;
				if ((vc = (VoiceChannel) state.getChannel()) != null)  {
					AudioManager manager = vc.getGuild().getAudioManager();
					manager.openAudioConnection(vc);




					String link = String.join(" ",  message.getContentRaw().replace("!play ", ""));


			        if (!isUrl(link)) {
			            link = "ytmsearch:" + link;
			        }

					System.out.println(isUrl(link) + " : " + link);
					if(DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(channel.getGuild()) == null) {
						DiscordBot.INSTANCE.getManagerController().addChannelToGuild(manager.getGuild(), channel);
					}

					if(DiscordBot.INSTANCE.getPremiumManager().hasPremium(channel.getGuild())) {
						PremiumPlayerManager.getInstance().loadAndPlay(channel, link);
					}else {
						PlayerManager.getInstance().loadAndPlay(channel, link);
					}
					System.out.println("load and play" );


			    }else {
					EmbedBuilder bu = new EmbedBuilder();
					bu.setDescription("Du befindest dich nicht in einem VoiceChannel");
					bu.setColor(Color.decode("#fc0303"));
					channel.sendMessageEmbeds(bu.build()).queue();
				}


				} else {
				EmbedBuilder bu = new EmbedBuilder();
				bu.setDescription("Du befindest dich nicht in einem VoiceChannel");
				bu.setColor(Color.decode("#fc0303"));
				channel.sendMessageEmbeds(bu.build()).queue();
			}
				
			} else {
			EmbedBuilder bu = new EmbedBuilder();
			bu.setDescription("Bitte benutze !play <url/search query>");
			bu.setColor(Color.decode("#fc0303"));
			channel.sendMessageEmbeds(bu.build()).queue();
			}
		}

	@Override
	public String help() {
		return "Plays music from a playlist or url. Otherwise I will search in SoundCloud for this track and play it.";
	}

	@Override
	public String name() {
		return "play";
	}

	public  boolean isUrl(String url) {
		try {
			new URL(url);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	}


