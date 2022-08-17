package main.Linux3000.audio.premium;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Linux3000.audio.AudioPlaylist;
import main.Linux3000.premium.manager.PremiumFeature;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PremiumPlaylistManager extends PremiumFeature {

    private Guild guild;



    public PremiumPlaylistManager(Guild guild) {
        this.guild = guild;
    }

    public void saveCurrentPlaylist(String name) {
        AudioPlaylist playlist = PremiumPlayerManager.getInstance().getMusicManager(guild).getPlaylist();
        new PremiumPlaylist(name, playlist.getAllTracks(), guild);

        System.out.println("registered playlist : " + new PremiumPlaylist(name, playlist.getAllTracks(), guild));
    }
    @Nullable
    public PremiumPlaylist getAudioPlaylist(String name) {
      /*  if(playlists.length == 0) { return null; }
        for(AudioPlaylist playlist : playlists) {
            if(playlist == null) continue;
            if(playlist.getName() == null) continue;
            if(playlist.getName().equalsIgnoreCase(name)) {
                return playlist;
            }
        }
        return null; */
        if(PremiumPlaylist.getPlaylists(guild) == null) {
            return null;
        }
        for(PremiumPlaylist playlist : PremiumPlaylist.getPlaylists(guild)) {
            if(playlist.getName().equalsIgnoreCase(name)) {
                return playlist;
            }
        }
        return null;
    }

    public PlaylistReason check(Guild guild, List<AudioTrack> tracks) {
        List<PremiumPlaylist> playlists = PremiumPlaylist.getPlaylists(guild);
        if(playlists.size() == 5) {
            return PlaylistReason.FULL;
        }
        for(PremiumPlaylist playlist : playlists) {
            if(playlist.getTracks().containsAll(tracks)) {
                PlaylistReason reason = PlaylistReason.ALREADY_CONTAINED;
                reason.setPlaylist(playlist);
                return reason;
            }
        }

        return PlaylistReason.FINE;
    }

    public void load() {
        //TODO

    }

}
