package main.Linux3000.audio;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import main.Linux3000.DiscordBot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class MusicController {


	private Guild guild;
	private AudioPlayer player;
	private final EqualizerFactory equalizer;
	
	public MusicController(Guild guild) {
		this.equalizer = new EqualizerFactory();
		this.guild = guild;
		this.player = DiscordBot.INSTANCE.audioPlayerManager.createPlayer();
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));



		
	}




	public Guild getGuild() {
		return guild;
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	

}
