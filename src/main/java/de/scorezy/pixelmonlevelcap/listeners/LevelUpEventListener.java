package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;

public class LevelUpEventListener {

    @SubscribeEvent
    public void onPokemonLevelUp(LevelUpEvent.Pre event) {
        ServerPlayerEntity player = event.getPlayer();
        if (player != null) {
            int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);
            if (event.getAfterLevel() > maxLevel) {
                event.setCanceled(true);
                player.sendMessage(new StringTextComponent("Dieses Level ist zu hoch für dein Pokémon!"), player.getUUID());
            }
        }
    }

    @SubscribeEvent
    public void onExperienceGain(ExperienceGainEvent event) {
        Pokemon pokemon = event.pokemon.getPokemon();
        int currentLevel = event.pokemon.getPokemonLevel();

        ServerPlayerEntity player = findPlayerForPokemon(pokemon);
        if (player != null) {
            int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);
            if (currentLevel >= maxLevel) {
                event.setExperience(0);
                player.sendMessage(new StringTextComponent("Dein Pokémon hat bereits das maximale Level erreicht und kann keine Erfahrung mehr erhalten!"), player.getUUID());
            }
        }
    }

    private ServerPlayerEntity findPlayerForPokemon(Pokemon pokemon) {
        return null; // Platzhalter
    }
}