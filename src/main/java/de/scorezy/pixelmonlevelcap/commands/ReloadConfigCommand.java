package de.scorezy.pixelmonlevelcap.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ReloadConfigCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("levelcapreload")
                        .requires(source -> source.hasPermission(2))
                        .executes(ReloadConfigCommand::execute)
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ConfigLoader.loadConfig();
        source.sendSuccess(() -> Component.translatable(ConfigLoader.getDefaultConfigLoaded()), true);
        return Command.SINGLE_SUCCESS;
    }
}