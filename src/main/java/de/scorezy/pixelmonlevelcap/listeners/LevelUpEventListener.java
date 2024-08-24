package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class LevelUpEventListener {

    @SubscribeEvent
    public void onPokemonLevelUp(LevelUpEvent.Pre event) {
        Optional.ofNullable(event.getPlayer()).ifPresent(serverPlayer -> {
            int pokemonLevel = event.getAfterLevel();
            int maxLevel = BadgeUtils.getMaxLevelForPlayer(serverPlayer);

            if (pokemonLevel > maxLevel) {
                event.setCanceled(true);
                serverPlayer.sendMessage(
                        new TranslationTextComponent("pixelmonlevelcap.capped_msg.level_blocked"),
                        serverPlayer.getUUID());
            }
        });
    }

    @SubscribeEvent
    public void onExperienceGain(ExperienceGainEvent event) {
        Optional.ofNullable(findPlayerForPokemon(event.pokemon.getPokemon())).ifPresent(serverPlayer -> {
            int currentLevel = event.pokemon.getPokemonLevel();
            int maxLevel = BadgeUtils.getMaxLevelForPlayer(serverPlayer);

            if (currentLevel >= maxLevel) {
                event.setExperience(0);
                serverPlayer.sendMessage(
                        new TranslationTextComponent("pixelmonlevelcap.capped_msg.max_level_reached"),
                        serverPlayer.getUUID());
            }
        });
    }

    private ServerPlayerEntity findPlayerForPokemon(Pokemon pokemon) {
        //Immer noch ein Platzhalter ;)
        return null;
    }
}
