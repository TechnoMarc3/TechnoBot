package main.Linux3000.commands.types;

public interface PremiumCommand extends ServerCommand{

    @Override
    default String prefix() {
        return "premium";
    }
}
