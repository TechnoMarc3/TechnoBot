package main.Linux3000;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import main.Linux3000.audio.MusicManagerController;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.PlayerManager1;
import main.Linux3000.commands.types.CommandManager;
import main.Linux3000.events.MemberLevelUpEvent;
import main.Linux3000.listeners.JoinQuitListener;
import main.Linux3000.listeners.MessageReceivedListener;
import main.Linux3000.listeners.XpListener;
import main.Linux3000.manage.FileManager;
import main.Linux3000.manage.LiteSQL;
import main.Linux3000.manage.SQLManager;
import main.Linux3000.stats.xp.XP;
import main.Linux3000.stats.xp.image.ImageCreator;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;


import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class DiscordBot {

    //TODO: Premium
    public static DiscordBot INSTANCE;
    public ImageCreator creator;
    public File tempFile;
    public XP xp;
    public MemberLevelUpEvent event;
    public JDA jda;
    public PlayerManager playerManager;
    private CommandManager cmdMan;
    public AudioPlayerManager audioPlayerManager;
    public PlayerManager1 playerManager1;
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


        JDABuilder builder = JDABuilder.createDefault("Nzg2NTkzNjUyMzAxMzY1MjQ5.X9IqbA.M6g1YWltFV716_WCcCklQP5NKVo");
        this.creator = new ImageCreator();
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES);
        builder.addEventListeners(new MessageReceivedListener());
        builder.addEventListeners(new XpListener());
        builder.addEventListeners(new JoinQuitListener());
        FileManager.create();
        builder.setStatus(OnlineStatus.ONLINE);

        this.audioPlayerManager = new DefaultAudioPlayerManager();
        this.playerManager1 = new PlayerManager1();
        this.playerManager = new PlayerManager();
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

    public CommandManager getCmdMan() {
        return cmdMan;
    }

    public JDA getJDA() {
        return jda;
    }

    public MusicManagerController getManagerController() {
        return managerController;
    }

    public boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}