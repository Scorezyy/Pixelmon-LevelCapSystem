package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RaidCaptureEventListener {

    @SubscribeEvent
    public void onStartRaidCapture(CaptureEvent.StartRaidCapture event) {
        ServerPlayerEntity player = event.getPlayer();
        int pokemonLevel = event.getRaidPokemon().getPokemonLevel();
        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

        if (pokemonLevel > maxLevel) {
            event.getRaidPokemon().setLevel(maxLevel);
        }
    }
}