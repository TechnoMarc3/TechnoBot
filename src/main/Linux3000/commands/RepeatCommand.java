package main.Linux3000.commands;

import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;

import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class RepeatCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {

		GuildVoiceState state;
		if ((state = m.getVoiceState()) != null) {
			VoiceChannel vc;
			if ((vc = (VoiceChannel) state.getChannel()) != null) {


				final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
				boolean newRepeat = musicManager.scheduler.repeatTrack();



				channel.sendMessageFormat("Das aktuelle Lied wird nun **%s**", newRepeat ? "wiederholt" : "nicht wiederholt" ).queue();
		    }

				
			}

		}

	@Override
	public String help() {
		return "Repeat the current track";
	}

	@Override
	public String name() {
		return "loop";
	}
}
