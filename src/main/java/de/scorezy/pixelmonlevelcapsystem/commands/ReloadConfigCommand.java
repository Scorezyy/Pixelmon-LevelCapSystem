package de.scorezy.pixelmonlevelcapsystem.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

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
        String reloadMsg = ConfigLoader.getMessagesConfig().getConfigReloaded();
        source.sendSuccess(new StringTextComponent(reloadMsg), true);
        return Command.SINGLE_SUCCESS;
    }
}
