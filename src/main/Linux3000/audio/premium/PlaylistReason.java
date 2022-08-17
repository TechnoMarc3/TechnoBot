package main.Linux3000.audio.premium;

public enum PlaylistReason {

    ALREADY_CONTAINED,
    FULL,
    FINE;

    private PremiumPlaylist playlist;


    PlaylistReason() {
    }

    public void setPlaylist(PremiumPlaylist playlist) {
        this.playlist = playlist;
    }

    public PremiumPlaylist getPlaylist() {
        return playlist;
    }
}
