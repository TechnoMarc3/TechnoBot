package main.Linux3000.stats.xp.image;

import main.Linux3000.DiscordBot;
import main.Linux3000.stats.Downloader;
import main.Linux3000.stats.xp.XP;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


public class ImageCreator {

    File file;
    private File output;

    private Image avatar;
    private final int WIDTH_image = 1200;
    private final int HIGH_image = 513;

    public BufferedImage bufferedImage; ;
    public Graphics2D graphics2D;



    public ImageCreator() {

        file = new File("rsc/pictures/join/minecraft_3037033b.jpg");
        output = new File("rsc/generated.png");
    }
    public BufferedImage createJoinImage(Guild guild, Member member)  {
        bufferedImage = new BufferedImage(WIDTH_image, HIGH_image, BufferedImage.TYPE_INT_RGB);
        graphics2D = bufferedImage.createGraphics();
        String s = "";

        File file = new File("rsc/pictures/avatar.png");
        try {
            Image image = ImageIO.read(new File("rsc/pictures/join/minecraft_3037033b.jpg"));
            graphics2D.drawImage(image, 0,0, null);
            if(member.getUser().getAvatarUrl() != null) {
                s = member.getUser().getAvatarUrl();
            }
            else if(member.getUser().getAvatarUrl() == null){
                s = "https://media.tenor.com/images/bf5bbc1b5d2d8ba46235647c4bcd2b1c/tenor.png";
            }
            //TODO: Color Select
            graphics2D.setColor(Color.decode("#0057d1"));
            graphics2D.setFont(graphics2D.getFont().deriveFont(120f));
            graphics2D.drawString(member.getEffectiveName(), 600, 100);
            //TODO: Join Message
            graphics2D.setColor(Color.black);
            graphics2D.setFont(graphics2D.getFont().deriveFont(30f));
            graphics2D.drawString("Herzlich willkommen bei uns", 600, 200);
            Downloader downloader = new Downloader(s, file);
            downloader.run();
            avatar = ImageIO.read(file);
            if(s.equalsIgnoreCase("https://media.tenor.com/images/bf5bbc1b5d2d8ba46235647c4bcd2b1c/tenor.png")) {
                graphics2D.scale(-1, -1);
            graphics2D.drawImage(avatar, 0,-100, null);
            }
            if (s.equalsIgnoreCase(member.getUser().getAvatarUrl() )) {
                graphics2D.scale(4, 4);
                graphics2D.drawImage(avatar, 0, 0, null);
            }
            graphics2D.dispose();
            ImageIO.write(bufferedImage, "jpg",output);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return bufferedImage;
    }
    public BufferedImage createXpImage(Guild guild, Member member)  {
        bufferedImage = new BufferedImage(WIDTH_image, HIGH_image, BufferedImage.TYPE_INT_RGB);
        graphics2D = bufferedImage.createGraphics();
        String s = "";
        XP xpClass = DiscordBot.INSTANCE.xp;
        long xp = xpClass.getXpFromMember(guild, member);
        File file = new File("rsc/pictures/avatar.png");
        try {
            Image image = ImageIO.read(new File("rsc/pictures/join/minecraft_3037033b.jpg"));
            graphics2D.drawImage(image, 0,0, null);
            if(member.getUser().getAvatarUrl() != null) {
                s = member.getUser().getAvatarUrl();
            }
            else if(member.getUser().getAvatarUrl() == null){
                s = "https://media.tenor.com/images/bf5bbc1b5d2d8ba46235647c4bcd2b1c/tenor.png";
            }

            Downloader downloader = new Downloader(s, file);
            downloader.run();
            avatar = ImageIO.read(file);
            if(s.equalsIgnoreCase("https://media.tenor.com/images/bf5bbc1b5d2d8ba46235647c4bcd2b1c/tenor.png")) {
                graphics2D.scale(-1, -1);
                graphics2D.drawImage(avatar, 0,-100, null);
            }
            if (s.equalsIgnoreCase(member.getUser().getAvatarUrl() )) {
                graphics2D.scale(4, 4);
                graphics2D.drawImage(avatar, 0, 0, null);
            }
            graphics2D.dispose();
            ImageIO.write(bufferedImage, "jpg",output);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return bufferedImage;
    }



    public File getOutput() {
        return output;
    }


}
