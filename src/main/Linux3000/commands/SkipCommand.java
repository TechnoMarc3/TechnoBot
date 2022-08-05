package main.Linux3000.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager1;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class SkipCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
			GuildVoiceState state;
			if ((state = m.getVoiceState()) != null) {
				VoiceChannel vc;
				if ((vc = (VoiceChannel) state.getChannel()) != null) {
					
					final GuildMusicManager musicManager = PlayerManager1.getInstance().getMusicManager(channel.getGuild());
			        final AudioPlayer audioPlayer = musicManager.audioPlayer;

			        if (audioPlayer.getPlayingTrack() == null) {
			            channel.sendMessage("Ich spiele aktuell keine Musik").queue();
			            return;
			        }
			        if(musicManager.scheduler.nextTrack()) {
			        channel.sendMessage("Aktuellen Titel übersprungen").queue(); }else {
						channel.sendMessage("Das war der letzte Song in dieser Playlist").queue();
					}
			    }
					
				
			}
		}

	@Override
	public String help() {
		return "Skips the current track";
	}

	@Override
	public String name() {
		return "skip";
	}
}
