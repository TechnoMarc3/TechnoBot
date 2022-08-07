package main.Linux3000.commands;

import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class StopCommand implements ServerCommand {

	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {

		GuildVoiceState state;
		if ((state = m.getVoiceState()) != null) {
			VoiceChannel vc;
			if ((vc = (VoiceChannel) state.getChannel()) != null) {
				

				
				final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());

		        musicManager.scheduler.getAudioPlayer().stopTrack();
		        musicManager.scheduler.clearPlaylist();

		        channel.sendMessage("Ich wurde gestoppt, Musik zu spielen. Deshalb habe ich auch die Playlist gelöscht!").queue();
				DiscordBot.INSTANCE.playerManager.getMusicManager(channel.getGuild()).setOnCooldown(true);
		        
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
