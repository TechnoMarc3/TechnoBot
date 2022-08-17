package main.Linux3000.youtube;

import de.linux3000.api.YoutubeAPI;
import de.linux3000.api.YoutubeApiImpl;
import de.linux3000.base.YoutubeChannel;
import de.linux3000.events.event.VideoUploadEvent;
import de.linux3000.formats.URLFormatter;
import de.linux3000.listener.EventListener;
import de.linux3000.listener.TestListener;
import de.linux3000.manager.ChannelManager;
import main.Linux3000.premium.manager.PremiumFeature;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Timer;
import java.util.TimerTask;

public class YoutubeManager extends PremiumFeature {

    private String url;
    public static boolean isFirstRun = false;

    public YoutubeManager(String url) {
        this.url = url;
    }

    public void search() {
        YoutubeAPI.setInstance(new YoutubeApiImpl());
        final YoutubeChannel youtubeChannel = (new ChannelManager()).getYoutubeChannel(url);
        YoutubeAPI.getINSTANCE().getEventManager().registerListener(new YoutubeManager.MainListener(null));
        Timer timer = new Timer();
        final int[] i = {0};
        TimerTask task = new TimerTask() {
            public void run() {
                i[0]++;
                isFirstRun = i[0] == 1;

                (new URLFormatter()).getPage(youtubeChannel);
            }
        };
        timer.scheduleAtFixedRate(task, 10000L, 10000L);
    }

    public static class MainListener extends EventListener {
        private Guild guild;

        public MainListener(Guild guild) {
            this.guild = guild;
        }

        public void onUpload(VideoUploadEvent event) {
            if(!YoutubeManager.isFirstRun) {
                System.out.println(event.getChannel().getName() + " just uploaded : " + "https://www.youtube.com/watch?v="+event.getNewVideo().getId());
            }

        }
    }

}
