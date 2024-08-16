package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RaidCaptureEventListener {

    @SubscribeEvent
    public void onStartRaidCapture(CaptureEvent.StartRaidCapture event) {
        ServerPlayerEntity player = event.getPlayer();
        int pokemonLevel = event.getRaidPokemon().getPokemonLevel();
        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

        if (pokemonLevel > maxLevel) {
            event.setCanceled(true);
            String message = ConfigLoader.getRaidCaptureBlockedMessage();
            player.sendMessage(new StringTextComponent(message), player.getUUID());
        }
    }
}