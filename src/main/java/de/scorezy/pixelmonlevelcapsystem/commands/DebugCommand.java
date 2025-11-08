package de.scorezy.pixelmonlevelcapsystem.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.pixelmonmod.pixelmon.init.registry.PixelmonDataComponents;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem.BadgeCase;
import de.scorezy.pixelmonlevelcapsystem.utils.BadgeUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class DebugCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("badgedebug")
                        .requires(source -> source.hasPermission(2))
                        .executes(DebugCommand::execute)
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        try {
            ServerPlayer player = source.getPlayerOrException();

            ItemStack badgeCaseStack = BadgeUtils.findBadgeCaseItemStack(player);

            if (badgeCaseStack.isEmpty()) {
                source.sendSuccess(() -> Component.literal("§cKein BadgeCase gefunden!"), false);
                return Command.SINGLE_SUCCESS;
            }

            // BadgeCase-Daten auslesen
            BadgeCase badgeCase = badgeCaseStack.get(PixelmonDataComponents.BADGE_CASE);

            if (badgeCase == null) {
                source.sendSuccess(() -> Component.literal("§cBadgeCase hat keine Daten!"), false);
                return Command.SINGLE_SUCCESS;
            }

            // Informationen sammeln
            boolean isOwner = badgeCase.isOwner(player);
            int badgeCount = badgeCase.badges().size();
            int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

            // Debug-Ausgabe
            source.sendSuccess(() -> Component.literal("§6=== BadgeCase Debug ==="), false);
            source.sendSuccess(() -> Component.literal("§eOwner: §f" + (isOwner ? "§aJa" : "§cNein")), false);
            source.sendSuccess(() -> Component.literal("§eAnzahl Badges: §f" + badgeCount), false);
            source.sendSuccess(() -> Component.literal("§eMax. Badge Level: §f" + maxLevel), false);
            source.sendSuccess(() -> Component.literal("§6===================="), false);

        } catch (Exception e) {
            source.sendFailure(Component.literal("§cFehler beim Ausführen des Befehls: " + e.getMessage()));
        }

        return Command.SINGLE_SUCCESS;
    }
}
