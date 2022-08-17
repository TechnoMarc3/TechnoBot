package main.Linux3000.commands;

import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.commands.types.AudioCommand;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class ResumeCommand implements AudioCommand {

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

				if (musicManager.scheduler.getAudioPlayer().isPaused()) {
					musicManager.setPlayingMusic(true);
					musicManager.scheduler.getAudioPlayer().setPaused(false);

					channel.sendMessage("Ich spiele nun weiter Musik für dich ab! :thumbsup:").queue();

				} else {
					channel.sendMessage("Ich bin nicht pausiert!").queue();
				}

			}
		}
	}

	@Override
	public String help() {
		return "Resumes the player -> !pause";
	}

	@Override
	public String name() {
		return "resume";
	}
}
