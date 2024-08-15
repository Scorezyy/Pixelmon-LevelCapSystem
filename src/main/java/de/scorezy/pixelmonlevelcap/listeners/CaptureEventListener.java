package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CaptureEventListener {

    @SubscribeEvent
    public void blockViaCaptureAttempt(CaptureEvent.StartCapture event) {
        ServerPlayerEntity player = event.getPlayer();
        int pokemonLevel = event.getPokemon().getLvl().getPokemonLevel();
        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

        if (pokemonLevel > maxLevel) {
            event.setCanceled(true);
            player.sendMessage(new StringTextComponent("§cDieses §ePokémon §cist zu stark für dich um es zu Fangen!"), player.getUUID());
        }
    }
}