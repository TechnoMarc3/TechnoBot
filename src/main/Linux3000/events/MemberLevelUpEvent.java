package main.Linux3000.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nullable;


public class MemberLevelUpEvent{

    private Member member;
    private Guild guild;


    public MemberLevelUpEvent() {
    }

    public Member getMember() {
        return member;
    }

    public Guild getGuild() {
        return guild;
    }

    public  TextChannel getLevelUpChannel(Guild guild) {
        TextChannel channel = guild.getDefaultChannel();

            return channel;
    }

    public void setLevelUpChannel(Guild guild, TextChannel channel) {


    }

    public void setMember( @Nullable Member member) {
        this.member = member;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }
}
