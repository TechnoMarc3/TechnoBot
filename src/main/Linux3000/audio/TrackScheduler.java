package main.Linux3000.audio;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import main.Linux3000.DiscordBot;
import main.Linux3000.utils.NumberUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;


import javax.annotation.Nullable;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;


public class TrackScheduler extends AudioEventAdapter {
    private static final float[] BASS_BOOST = {-0.05f, 0.07f, 0.16f, 0.03f, -0.05f, -0.11f};

    public enum RepeatMode {
        NONE,
        SONG,
        PLAYLIST
    }
    //TODO: irgendwann: Previous
    private final AudioPlayer audioPlayer;


    private List<AudioTrack> hasAlreadyPlayed = new ArrayList<>();
    private RepeatMode repeatMode;
    private AudioTrack currentTrack;
    private EqualizerFactory equalizer;
    private int boostPercentage;

    private GuildMusicManager manager;

    public TrackScheduler(AudioPlayer audioPlayer, int defaultVolume, GuildMusicManager manager) {
        this.audioPlayer = audioPlayer;
        this.setRepeatMode(RepeatMode.NONE);
        this.setVolume(defaultVolume);
        this.manager = manager;
    }


    public void startOrQueue(AudioTrack track) {

        if (!audioPlayer.startTrack(track, true)) {
            this.manager.getPlaylist().addTrack(track);
        }else {
            this.manager.getPlaylist().addTrack(track);
            this.currentTrack = track;
            hasAlreadyPlayed.add(track);
        }
    }



    public boolean nextTrack() {
        int index = getPlaylist().indexOf(this.currentTrack);
        if(index == getPlaylist().size()-1) {
            if(this.repeatMode.equals(RepeatMode.PLAYLIST)) {
                this.skipTo(0);
                return true;
            }else {
                Guild guild = DiscordBot.INSTANCE.getManagerController().getGuildByPlayer(this.audioPlayer);
                AudioManager manager = guild.getAudioManager();
                clearPlaylist();
                this.audioPlayer.stopTrack();
                manager.closeAudioConnection();
                DiscordBot.INSTANCE.getManagerController().removeGuildFromCache(guild);
                return false;
            }
        }else {
        AudioTrack track = this.manager.getPlaylist().getAllTracks().get(this.manager.getPlaylist().getAllTracks().indexOf(this.currentTrack)+1);
        this.audioPlayer.startTrack(hasAlreadyPlayedTrack(track) ? track.makeClone() : track, false);
        this.currentTrack = track;
        this.hasAlreadyPlayed.add(track);
        return true; }

    }
    public boolean hasAlreadyPlayedTrack(AudioTrack track) {
        return hasAlreadyPlayed.contains(track);
    }

    public boolean repeatQueue() {
        if(!this.getRepeatMode().equals(RepeatMode.PLAYLIST)) {
            this.setRepeatMode(RepeatMode.PLAYLIST);
            return true;
        }
        else {
            this.setRepeatMode(RepeatMode.NONE);
            return false;
        }
    }

    public boolean repeatTrack() {
        if(this.getRepeatMode().equals(RepeatMode.SONG)) {
          this.setRepeatMode(RepeatMode.NONE);
          return false;
        }
        else {
        this.setRepeatMode(RepeatMode.SONG);
        return true;
       }
    }

    public boolean skipTo(int num) {
        AudioTrack track = null;
        try {
        track = manager.getPlaylist().getAllTracks().get(num);
        this.audioPlayer.playTrack(this.makeClone(track));
        this.currentTrack = track;
        return true;
        }
        catch (IndexOutOfBoundsException exception) {
            return false;}

    }

    public long changePosition(long time) {
        final AudioTrack track = this.audioPlayer.getPlayingTrack();
        final long newPosition = NumberUtils.truncateBetween(track.getPosition() + time, 0, track.getDuration() - 1);
        track.setPosition(newPosition);
        return newPosition;
    }

    public void shufflePlaylist() {

        Collections.shuffle(this.manager.getPlaylist().getAllTracks());

    }

    public void clearPlaylist() {
        this.manager.getPlaylist().getAllTracks().clear();
    }

    public void bassBoost(int percentage) {
        final int previousPercentage = this.boostPercentage;
        this.boostPercentage = percentage;

        // Disable filter factory
        if (previousPercentage > 0 && percentage == 0) {
            this.audioPlayer.setFilterFactory(null);
            return;
        }
        // Enable filter factory
        if (previousPercentage == 0 && percentage > 0) {
            if (this.equalizer == null) {
                this.equalizer = new EqualizerFactory();
            }
            this.audioPlayer.setFilterFactory(this.equalizer);
        }

        final float multiplier = percentage / 100.0f;
        for (int i = 0; i < BASS_BOOST.length; i++) {
            this.equalizer.setGain(i, BASS_BOOST[i] * multiplier);
        }

        this.boostPercentage = percentage;
    }

    public void destroy() {
        if (this.currentTrack != null && this.currentTrack.getState() == AudioTrackState.PLAYING) {
            this.currentTrack.stop();
        }
        this.audioPlayer.destroy();
        this.clearPlaylist();
    }

    @Nullable
    private AudioTrack makeClone(AudioTrack track) {
        return track == null ? null : track.makeClone();
    }

    public AudioPlayer getAudioPlayer() {
        return this.audioPlayer;
    }



    public RepeatMode getRepeatMode() {
        return this.repeatMode;
    }

    public boolean isPlaying() {
        return this.audioPlayer.getPlayingTrack() != null;
    }

    public boolean isStopped() {
        return !this.isPlaying();
    }

    public void setVolume(int volume) {
        this.audioPlayer.setVolume(volume);
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        System.out.println("ended succ");



        if (endReason.mayStartNext) {

            if (this.getRepeatMode().equals(RepeatMode.SONG)) {
                this.audioPlayer.startTrack(track.makeClone(), false);
                return;
            }

            if (!this.getPlaylist().get(this.getPlaylist().size()-1).equals(this.getCurrentTrack())) {
                nextTrack();


            } else {

                if(this.getRepeatMode().equals(RepeatMode.PLAYLIST)) {
                    this.skipTo(0);

                }
                else {

                    Guild guild = DiscordBot.INSTANCE.getManagerController().getGuildByPlayer(player);
                    TextChannel channel = DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(guild);
                    AudioManager manager = guild.getAudioManager();
                    manager.closeAudioConnection();
                    channel.sendMessage("Die Playlist ist nun leer :sob: . Deshalb habe ich mich disconnected! Du kannst mich aber wieder zurückholen, indem du !play <Titel, URL> eingibst ").queue();
                    System.out.println("playlist empty");
                    DiscordBot.INSTANCE.getManagerController().removeGuildFromCache(guild);
                }
            }


        }
    }

    public List<AudioTrack> getPlaylist() {
        return this.manager.getPlaylist().getAllTracks();
    }

    public AudioTrack getCurrentTrack() {
        return currentTrack;
    }
}

