package main.Linux3000.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import main.Linux3000.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Timer;
import java.util.TimerTask;

public class GuildMusicManager {
	 public final AudioPlayer audioPlayer;

		public boolean isPlayingMusic;



	    public final TrackScheduler scheduler;

		private AudioPlaylist playlist;

	    private final AudioPlayerSendHandler sendHandler;

	    public GuildMusicManager(AudioPlayerManager manager) {
	        this.audioPlayer = manager.createPlayer();
	        this.scheduler = new TrackScheduler(this.audioPlayer, 30, this);
	        this.audioPlayer.addListener(this.scheduler);
	        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
	    }

	public AudioPlaylist getPlaylist() {
		if(playlist == null) {
			playlist = new AudioPlaylist();
		}
		return playlist;
	}

	public TrackScheduler getScheduler() {
		return scheduler;
	}

	public void setPlayingMusic(boolean playingMusic) {
		isPlayingMusic = playingMusic;
	}

	public void setupTask() {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					sendDisconnect();
				}
			};
			timer.schedule(task,1000*300);
	}

	public void sendDisconnect() {
		if(this.isPlayingMusic) {
			System.out.println("wäre wenn");return;}
		Guild guild = DiscordBot.INSTANCE.getManagerController().getGuildByPlayer(audioPlayer);
		TextChannel channel = DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(guild);
		AudioManager manager = guild.getAudioManager();
		getScheduler().getAudioPlayer().stopTrack();
		getScheduler().clearPlaylist();
		manager.closeAudioConnection();
		channel.sendMessage("Die letzen 5 Minuten wurde keine Musik abgespielt. Deshalb habe ich mich disconnected! Du kannst mich aber wieder zurückholen, indem du !play <Titel, URL> eingibst ").queue();
		System.out.println("disconnected due inactivity : " + guild.getName());
	}

	public AudioPlayerSendHandler getSendHandler() {
	        return sendHandler;
	    }

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
}