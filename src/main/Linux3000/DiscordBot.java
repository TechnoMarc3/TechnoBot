package main.Linux3000;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import main.Linux3000.audio.MusicManagerController;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.audio.premium.PremiumPlaylist;
import main.Linux3000.commands.types.CommandManager;
import main.Linux3000.events.MemberLevelUpEvent;
import main.Linux3000.listeners.ButtonClickListener;
import main.Linux3000.listeners.JoinQuitListener;
import main.Linux3000.listeners.MessageReceivedListener;
import main.Linux3000.listeners.XpListener;
import main.Linux3000.manage.FileManager;
import main.Linux3000.manage.LiteSQL;
import main.Linux3000.premium.manager.PremiumManager;
import main.Linux3000.manage.SQLManager;
import main.Linux3000.stats.xp.XP;
import main.Linux3000.stats.xp.image.ImageCreator;

import main.Linux3000.youtube.YoutubeManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;


import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class DiscordBot {

    //TODO: Premium
    public static DiscordBot INSTANCE;
    public ImageCreator creator;
    public File tempFile;
    public XP xp;
    public MemberLevelUpEvent event;
    public JDA jda;
    private PremiumManager premiumManager;
    public PlayerManager playerManager;
    private CommandManager cmdMan;
    public AudioPlayerManager audioPlayerManager;
    private PremiumPlayerManager premiumPlayerManager;

    private MusicManagerController managerController;

    //TODO: https://developers.google.com/youtube/v3/guides/push_notifications,  Callback, Netty

    public static void main(String[] args) {
        try {
            new DiscordBot();
        } catch (LoginException | IllegalArgumentException | IOException e) {

            e.printStackTrace();
        }

    }

    public DiscordBot() throws LoginException, IllegalArgumentException, IOException {


        INSTANCE = this;
        this.cmdMan = new CommandManager();

        this.tempFile = new File("temp.txt");
        if (!this.tempFile.exists()) {
            this.tempFile.createNewFile();
        }


        JDABuilder builder = JDABuilder.createDefault("Nzg2NTkzNjUyMzAxMzY1MjQ5.GF9Uz5.X44riumy3Ic6gzSF0q1vZj6HPR7uw-URSZpFrE");
        this.creator = new ImageCreator();
        builder.enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.addEventListeners(new MessageReceivedListener());
        builder.addEventListeners(new XpListener());
        builder.addEventListeners(new ButtonClickListener());
        builder.addEventListeners(new JoinQuitListener());
        FileManager.create();
        builder.setStatus(OnlineStatus.ONLINE);

        premiumManager = new PremiumManager();


        this.audioPlayerManager = new DefaultAudioPlayerManager();
        this.playerManager = new PlayerManager();
        this.premiumPlayerManager = new PremiumPlayerManager();
        this.managerController = new MusicManagerController();
        LiteSQL.createDataSource();
        SQLManager.onCreate();
        this.xp = new XP();

        this.event = new MemberLevelUpEvent();
        System.out.println("Bot online");

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);


        try {
            this.jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        new YoutubeManager("https://www.youtube.com/c/Karriereguru").search();
        shutdown();

    }

    public void shutdown() {
        new Thread(() -> {
            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        if (jda != null) {
                            jda.shutdown();
                            System.out.println("Bot offline");
                        }
                        reader.close();
                        System.exit(-1);
                    }
                }
            } catch (IOException ignored) {

            }
        }).start();
    }

    public PremiumManager getPremiumManager() {
        return premiumManager;
    }

    public CommandManager getCmdMan() {
        return cmdMan;
    }

    public JDA getJDA() {
        return jda;
    }

    public MusicManagerController getManagerController() {
        return managerController;
    }


    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}