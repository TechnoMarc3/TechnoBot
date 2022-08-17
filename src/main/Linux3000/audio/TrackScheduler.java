package main.Linux3000.audio;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import main.Linux3000.DiscordBot;
import main.Linux3000.audio.premium.PremiumPlaylist;
import main.Linux3000.premium.manager.MusicCommandManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;


import javax.annotation.Nullable;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;


public class TrackScheduler extends AudioEventAdapter {
    private static final float[] BASS_BOOST = {0.5f, 0.12f, 0.4f, 0.00f, -0.05f, -0.11f};

    public enum RepeatMode {
        NONE,
        SONG,
        PLAYLIST
    }
    //TODO: irgendwann: Previous
    private final AudioPlayer audioPlayer;


    private List<AudioTrack> hasAlreadyPlayed = new ArrayList<>();
    private boolean isBassBoosted;
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

    public void registerAndStartPlaylist(PremiumPlaylist playlist) {
        System.out.println("set");
        this.audioPlayer.stopTrack();
        System.out.println("stopped");
        for(AudioTrack track : playlist.getTracks()) {
            this.startOrQueue(track, false);
        }
        System.out.println("added");
    }


    public void startOrQueue(AudioTrack track, boolean isFromPlaylist) {
        System.out.println("start or queue");


        if (!audioPlayer.startTrack(hasAlreadyPlayedTrack(track) ? track.makeClone() : track, true)) {
            this.manager.getPlaylist().addTrack(track);
        }else {
            this.manager.getPlaylist().addTrack(track);
            this.currentTrack = track;
            hasAlreadyPlayed.add(track); }

        manager.setPlayingMusic(true);




    }



    public boolean nextTrack() {
        int index = getPlaylist().indexOf(this.currentTrack);
        this.audioPlayer.stopTrack();
        this.audioPlayer.setPaused(false);
        if(index == getPlaylist().size()-1) {
            if(this.repeatMode.equals(RepeatMode.PLAYLIST)) {
                this.skipTo(0);
                return true;
            }else {
                clearPlaylist();
                this.audioPlayer.stopTrack();
                manager.setPlayingMusic(false);
                manager.setupTask();
                return false;
            }
        }else {
        AudioTrack track = this.manager.getPlaylist().getAllTracks().get(this.manager.getPlaylist().getAllTracks().indexOf(this.currentTrack)+1);
        this.audioPlayer.startTrack(hasAlreadyPlayedTrack(track) ? track.makeClone() : track, false);
        this.currentTrack = track;
        this.hasAlreadyPlayed.add(track);
        manager.setPlayingMusic(true);
        return true; }

    }

    public boolean previousTrack() {
        int index = getPlaylist().indexOf(this.currentTrack);
        this.audioPlayer.stopTrack();
        this.audioPlayer.setPaused(false);
        if(index == 0) {
            if(this.repeatMode.equals(RepeatMode.PLAYLIST)) {
                this.skipTo(getPlaylist().size()-1);
                return true;
            }else {
                clearPlaylist();
                this.audioPlayer.stopTrack();
                manager.setPlayingMusic(false);
                manager.setupTask();
                return false;
            }
        }else {
            AudioTrack track = this.manager.getPlaylist().getAllTracks().get(this.manager.getPlaylist().getAllTracks().indexOf(this.currentTrack)-1);
            this.audioPlayer.startTrack(hasAlreadyPlayedTrack(track) ? track.makeClone() : track, false);
            this.currentTrack = track;
            this.hasAlreadyPlayed.add(track);
            manager.setPlayingMusic(true);
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
        AudioTrack track;
        try {
        track = manager.getPlaylist().getAllTracks().get(num);
        this.audioPlayer.stopTrack();
        this.audioPlayer.playTrack(this.makeClone(track));
        this.audioPlayer.setPaused(false);
        this.currentTrack = track;
        this.manager.setPlayingMusic(true);
        return true;
        }
        catch (IndexOutOfBoundsException exception) {
            return false;}

    }



    public void shufflePlaylist() {
        Collections.shuffle(this.manager.getPlaylist().getAllTracks());
    }

    public void clearPlaylist() {
        this.manager.getPlaylist().getAllTracks().clear();
    }
    private boolean hasDonePrevious;
    // in seconds
    public void changePosition(long time) {
        final AudioTrack track = this.audioPlayer.getPlayingTrack();
        final long newPosition = track.getPosition() + (time*1000);
        if(newPosition >= track.getDuration()) {
            nextTrack();
            return;
        }else if(newPosition < 0) {
            if(hasDonePrevious) {
                previousTrack();
                hasDonePrevious = false;
                return;
            }
            track.setPosition(0);
            hasDonePrevious = true;
            return;
        }
        track.setPosition(newPosition);

    }

    public boolean removeTrack(int index) {
        try{
            this.getPlaylist().remove(index);
            return true;}
        catch(Exception e) {return false;}
    }

    public void bassBoost(int percentage) {
        final int previousPercentage = this.boostPercentage;
        this.boostPercentage = percentage;

        // Disable filter factory
        if (previousPercentage > 0 && percentage == 0) {
            this.audioPlayer.setFilterFactory(null);
            isBassBoosted = false;
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
        isBassBoosted = true;
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

        System.out.println(endReason.name());

        Guild guild = DiscordBot.INSTANCE.getManagerController().getGuildByPlayer(audioPlayer);
        System.out.println(track.getInfo());
        System.out.println(guild);
        System.out.println("ended succ : " + track.getInfo().title + "  -> guild: " + guild.getName());



        if (endReason.mayStartNext) {

            if (this.getRepeatMode().equals(RepeatMode.SONG)) {
                this.audioPlayer.startTrack(track.makeClone(), false);
                return;
            }
            nextTrack();


        }

        if(DiscordBot.INSTANCE.getPremiumManager().hasPremium(guild)) {
            MusicCommandManager commandManager = DiscordBot.INSTANCE.getPremiumManager().getCommandManager(guild);
            Message message = commandManager.getMessage();
            if(message != null) {
                commandManager.destroy();
                commandManager.setMessage(null);
                message.delete().queue();
                commandManager.sendMusicMessage(DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(guild));
            }
        }







    }

    public List<AudioTrack> getPlaylist() {
        return this.manager.getPlaylist().getAllTracks();
    }


    public AudioTrack getCurrentTrack() {
        return currentTrack;
    }

    public boolean isBassBoosted() {
        return isBassBoosted;
    }
}

