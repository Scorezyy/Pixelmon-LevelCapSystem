package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class PlayerInteractListener {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof PixelmonEntity) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            int pokemonLevel = ((PixelmonEntity) event.getTarget()).getLvl().getPokemonLevel();
            int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

            if (pokemonLevel > maxLevel) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
                String message = ConfigLoader.getRightClickBlockedMessage();
                player.sendSystemMessage(Component.literal(message));
            }
        }
    }
}
