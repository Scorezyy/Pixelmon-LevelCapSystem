package de.scorezy.pixelmonlevelcap.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.scorezy.pixelmonlevelcap.Main;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class ReloadCmd {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("levelcapreload")
                        .requires(source -> source.hasPermission(2))
                        .executes(ReloadCmd::execute)
        );
    }

    private static int execute(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        Main.getInstance().loadConfigs();
        source.sendSuccess(new TranslationTextComponent("pixelmonlevelcap.config_msg.reload"),
                true);
        return Command.SINGLE_SUCCESS;
    }
}