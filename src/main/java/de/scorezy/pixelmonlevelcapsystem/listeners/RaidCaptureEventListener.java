package de.scorezy.pixelmonlevelcapsystem.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import de.scorezy.pixelmonlevelcapsystem.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

public class RaidCaptureEventListener {

    @SubscribeEvent
    public void onStartRaidCapture(CaptureEvent.StartRaidCapture event) {
        if (!ConfigLoader.getSettingsConfig().isLevelCapRaidPokemons()) {
            return;
        }

        ServerPlayer player = event.getPlayer();
        String playerName = player.getName().getString();
        String speciesName = event.getRaidPokemon().getSpecies().getName();
        int originalLevel = event.getRaidPokemon().getPokemonLevel();
        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

        if (originalLevel > maxLevel) {
            Logger.debug(playerName + " tried to raid-capture " + speciesName +
                    " (lvl " + originalLevel + ") - levelcap to " + maxLevel);
            event.getRaidPokemon().setLevel(maxLevel);
        } else {
            Logger.debug(playerName + " raid-captured " + speciesName +
                    " (lvl " + originalLevel + "), allowed (cap " + maxLevel + ")");
        }
    }
}
