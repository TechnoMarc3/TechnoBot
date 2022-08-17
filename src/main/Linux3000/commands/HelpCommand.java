package main.Linux3000.commands;

import main.Linux3000.DiscordBot;

import main.Linux3000.commands.types.BaseCommand;
import main.Linux3000.commands.types.CommandManager;
import main.Linux3000.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Comparator;
import java.util.List;


public class HelpCommand implements BaseCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {
        StringBuilder builder = new StringBuilder();
        CommandManager manager =  DiscordBot.INSTANCE.getCmdMan();

        final String[] before = {""};

        builder.append("List of commands:\n");
        List<ServerCommand> cmds = manager.getCommands(channel.getGuild());
        cmds.sort(Comparator.comparing(ServerCommand::prefix));

        cmds.forEach(
                (it) -> {if(!it.prefix().equalsIgnoreCase(before[0])) {
                    builder.append("**").append(it.prefix().toUpperCase()).append("**  \n");
                }
                before[0] = it.prefix();
                        builder.append('`')
                        .append("!")
                        .append(it.name())
                        .append(" : ")
                        .append(manager.getHelp(it.name()))
                        .append("`\n");}
        );

        channel.sendMessage(builder.toString()).queue();

    }

    @Override
    public String help() {
        return "Shows all the commands";
    }

    @Override
    public String name() {
        return "help";
    }
}
