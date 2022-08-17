package main.Linux3000.premium.manager;

import main.Linux3000.audio.premium.PremiumPlaylistManager;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PremiumManager {

    private PremiumManager INSTANCE;

    private List<Guild> guilds;
    private HashMap<Guild, List<PremiumFeature>> features;




    public PremiumManager() {
        INSTANCE = this;

        guilds = new ArrayList<>();
        features = new HashMap<>();
    }



    public void registerPremium(Guild guild) {
        if(guild == null) {
            System.out.println("null");
            return; }
        guilds.add(guild);
        List<PremiumFeature> featureList = new ArrayList<>();
        featureList.add(new MusicCommandManager(guild));
        featureList.add(new PremiumPlaylistManager(guild));
        features.put(guild, featureList);
    }

    public List<Guild> getPremiumGuilds() {
        return guilds;
    }

    public MusicCommandManager getCommandManager(Guild guild) {
        if(!hasPremium(guild)) return null;
        for(PremiumFeature feature : features.get(guild)) {
            if(feature instanceof MusicCommandManager) {
                return (MusicCommandManager) feature;
            }
        }
        return null;
    }

    public PremiumPlaylistManager getPlaylistManager(Guild guild) {
        if(!hasPremium(guild)) return null;
        for(PremiumFeature feature : features.get(guild)) {
            if(feature instanceof PremiumPlaylistManager) {
                return (PremiumPlaylistManager) feature;
            }
        }
        return null;
    }

    public boolean hasPremium(Guild guild) {
        return guilds.contains(guild);
    }

    public PremiumManager getINSTANCE() {
        return INSTANCE;
    }
}
