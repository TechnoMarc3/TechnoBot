package main.Linux3000.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;


import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AudioPlayerSendHandler implements AudioSendHandler {

	private final AudioPlayer audioPlayer;
	private AudioFrame lastFrame;
	
	public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}
	
	
	@Override
	public boolean canProvide() {
		lastFrame = audioPlayer.provide();
		return lastFrame != null;
	}
	@Override
	public boolean isOpus() {
		return true;
	}
	@Override
	public ByteBuffer provide20MsAudio() {

		return ByteBuffer.wrap(lastFrame.getData());

}


}
