package main.Linux3000.commands.types;

public interface AudioCommand extends ServerCommand{

    @Override
    default String prefix() {
        return "audio";
    }
}
