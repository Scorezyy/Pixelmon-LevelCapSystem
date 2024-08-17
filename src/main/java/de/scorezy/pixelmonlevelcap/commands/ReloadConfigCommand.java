package de.scorezy.pixelmonlevelcap.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class ReloadConfigCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("levelcapreload")
                        .requires(source -> source.hasPermission(2))
                        .executes(ReloadConfigCommand::execute)
        );
    }

    private static int execute(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        ConfigLoader.loadConfig();
        source.sendSuccess(new TranslationTextComponent(ConfigLoader.getDefaultConfigLoaded()), true);
        return Command.SINGLE_SUCCESS;
    }
}
