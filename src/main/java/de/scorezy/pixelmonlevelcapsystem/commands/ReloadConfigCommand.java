package de.scorezy.pixelmonlevelcapsystem.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ReloadConfigCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("levelcapreload")
                        .requires(src -> src.hasPermission(2))
                        .executes(ReloadConfigCommand::execute)
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        ConfigLoader.loadConfig();

        String reloadMsg = ConfigLoader.getMessagesConfig().getConfigReloaded();
        source.sendSuccess(() -> Component.literal(reloadMsg), true);

        source.sendSuccess(() -> Component.literal("§cRead console for more information."), true);

        Logger.debug("§aConfiguration successfully reloaded.");
        Logger.debug("§cIf you changed permissions, please restart the server.");

        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
    }
}
