package main.Linux3000.commands;

import main.Linux3000.DiscordBot;

import main.Linux3000.commands.types.CommandManager;
import main.Linux3000.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;



public class HelpCommand implements ServerCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {
        StringBuilder builder = new StringBuilder();
        CommandManager manager =  DiscordBot.INSTANCE.getCmdMan();
        builder.append("List of commands:\n");

        manager.getCommands().stream().map(ServerCommand::name).forEach(
                (it) -> builder.append('`')
                        .append("!")
                        .append(it)
                        .append(" : ")
                        .append(manager.getHelp(it))
                        .append("`\n")
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
