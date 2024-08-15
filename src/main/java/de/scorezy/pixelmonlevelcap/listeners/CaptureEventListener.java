package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;

public class CaptureEventListener {

    @SubscribeEvent
    public void blockViaCaptureAttempt(CaptureEvent.StartCapture event) {
        ServerPlayerEntity player = event.getPlayer();
        if (player != null && event.getPokemon() != null) {
            int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);
            int pokemonLevel = event.getPokemon().getLvl().getPokemonLevel();

            if (pokemonLevel > maxLevel) {
                event.setCanceled(true);
                player.sendMessage(new StringTextComponent("Dieses Pokémon ist zu hoch für dich zum Fangen!"), player.getUUID());
            }
        }
    }
}