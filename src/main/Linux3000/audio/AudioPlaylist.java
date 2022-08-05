package main.Linux3000.audio;


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;
import java.util.List;

public class AudioPlaylist {

    private List<AudioTrack> audioTracks = new ArrayList<>();

    public void addTrack(AudioTrack track) {
        audioTracks.add(track);
    }

    public void removeTrack(AudioTrack track) {
        audioTracks.remove(track);
    }

    public void setAudioTracks(List<AudioTrack> audioTracks) {
        this.audioTracks = audioTracks;
    }

    public List<AudioTrack> getAllTracks() {
        return audioTracks;
    }
}
