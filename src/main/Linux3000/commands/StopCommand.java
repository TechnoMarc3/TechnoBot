package main.Linux3000.commands;

import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.commands.types.AudioCommand;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class StopCommand implements AudioCommand {

	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {

		GuildVoiceState state;
		if ((state = m.getVoiceState()) != null) {
			VoiceChannel vc;
			if ((vc = (VoiceChannel) state.getChannel()) != null) {


				final GuildMusicManager musicManager;
				if(DiscordBot.INSTANCE.getPremiumManager().hasPremium(channel.getGuild())) {
					musicManager = PremiumPlayerManager.getInstance().getMusicManager(channel.getGuild());
				}else {
					musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
				}
		        musicManager.scheduler.getAudioPlayer().stopTrack();
				musicManager.scheduler.clearPlaylist();

				if(!musicManager.isPlayingMusic) {
					channel.sendMessage("Ich spiele aktuell keine Musik, es gibt also auch nichts zu stoppen :blush:").queue();
				}else {

		        channel.sendMessage("Ich wurde gestoppt, Musik zu spielen").queue();
				musicManager.setPlayingMusic(false);
				musicManager.setupTask();
				}

		        
			}
		}
	}

	@Override
	public String help() {
		return "Stops the player from playing and clears the playlist";
	}

	@Override
	public String name() {
		return "stop";
	}
}
