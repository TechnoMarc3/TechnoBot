package main.Linux3000.commands.types;

public interface BaseCommand extends ServerCommand{

    @Override
    default String prefix() {
        return "base";
    }
}
