package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;

public class PlayerInteractListener {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof PixelmonEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            PixelmonEntity pokemon = (PixelmonEntity) event.getTarget();

            int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);
            int pokemonLevel = pokemon.getLvl().getPokemonLevel();

            if (pokemonLevel > maxLevel) {
                event.setCancellationResult(ActionResultType.FAIL);
                event.setCanceled(true);
                player.sendMessage(new StringTextComponent("Das Pokémon ist zu hoch für dich!"), player.getUUID());
            }
        }
    }
}