package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LevelUpEventListener {

    @SubscribeEvent
    public void onPokemonLevelUp(LevelUpEvent.Pre event) {
        ServerPlayerEntity player = event.getPlayer();
        int pokemonLevel = event.getAfterLevel();
        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

        if (pokemonLevel > maxLevel) {
            event.setCanceled(true);
            player.sendMessage(new StringTextComponent("§cDieses Level ist zu hoch für dein Pokémon!"), player.getUUID());
        }
    }

    @SubscribeEvent
    public void onExperienceGain(ExperienceGainEvent event) {
        ServerPlayerEntity player = findPlayerForPokemon(event.pokemon.getPokemon());
        int currentLevel = event.pokemon.getPokemonLevel();
        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

        if (player != null && currentLevel >= maxLevel) {
            event.setExperience(0);
            player.sendMessage(new StringTextComponent("§cDein §ePokémon §chat bereits das maximale Level erreicht"), player.getUUID());
        }
    }

    private ServerPlayerEntity findPlayerForPokemon(Pokemon pokemon) {
        // Implementiere diese Methode nach Bedarf.
        return null;
    }
}
