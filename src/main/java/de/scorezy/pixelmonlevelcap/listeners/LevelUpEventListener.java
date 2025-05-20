package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

public class LevelUpEventListener {

    @SubscribeEvent
    public void onPokemonLevelUp(LevelUpEvent.Pre event) {
        if (event.getPlayer() != null) {
            ServerPlayer player = event.getPlayer();
            int pokemonLevel = event.getAfterLevel();
            int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

            if (pokemonLevel > maxLevel) {
                event.setCanceled(true);
                String message = ConfigLoader.getLevelBlockedMessage();
                player.sendSystemMessage(Component.literal(message));
            }
        }
    }

    @SubscribeEvent
    public void onExperienceGain(ExperienceGainEvent event) {
        ServerPlayer player = findPlayerForPokemon(event.pokemon.toPokemon());
        int currentLevel = event.pokemon.getPokemonLevel();
        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

        if (player != null && currentLevel >= maxLevel) {
            event.setExperience(0);
            String message = ConfigLoader.getMaxLevelReachedMessage();
            player.sendSystemMessage(Component.literal(message));
        }
    }

    private ServerPlayer findPlayerForPokemon(Pokemon pokemon) {
        //Platzhalter ;)
        return null;
    }
}
