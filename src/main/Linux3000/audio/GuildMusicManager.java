package main.Linux3000.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
	 public final AudioPlayer audioPlayer;

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

	public AudioPlayerSendHandler getSendHandler() {
	        return sendHandler;
	    }

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
}