package main.Linux3000.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.commands.types.AudioCommand;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class SkipCommand implements AudioCommand {

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
