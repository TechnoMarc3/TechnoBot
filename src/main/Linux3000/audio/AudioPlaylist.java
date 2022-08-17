package main.Linux3000.audio;


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;
import java.util.List;

public class AudioPlaylist {

    private String name;

    private List<AudioTrack> audioTracks;

    public AudioPlaylist() {
        audioTracks = new ArrayList<>();
    }

    public AudioPlaylist(String name, List<AudioTrack> audioTracks) {
        this.name = name;
        this.audioTracks = audioTracks;

        System.out.println(audioTracks.size());
    }


    public String getName() {
        return name;
    }



    public void addTrack(AudioTrack track) {
    audioTracks.add(track);
    }

    public void removeTrack(AudioTrack track) {
        audioTracks.remove(track);
    }

    public void setAudioTracks(List<AudioTrack> audioTracks) {
        this.audioTracks = audioTracks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AudioTrack> getAllTracks() {
        return this.audioTracks;
    }
}
