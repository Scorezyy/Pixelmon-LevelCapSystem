package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;

public class RaidCaptureEventListener {

    @SubscribeEvent
    public void onStartRaidCapture(CaptureEvent.StartRaidCapture event) {
        ServerPlayerEntity player = event.getPlayer();
        if (player != null && event.getRaidPokemon() != null) {
            int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);
            int pokemonLevel = event.getRaidPokemon().getPokemonLevel();

            if (pokemonLevel > maxLevel) {
                event.setCanceled(true);
                player.sendMessage(new StringTextComponent("Du kannst dieses Pokémon nicht fangen, da dein Badge-Level nicht ausreicht!"), player.getUUID());
                //Close RaidFenster Code muss noch hinzugefügt werden.
            }
        }
    }
}