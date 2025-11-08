package de.scorezy.pixelmonlevelcapsystem.listeners;

import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.PokemonLevel;
import de.scorezy.pixelmonlevelcapsystem.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

public class ExperienceListener {

    @SubscribeEvent
    public void onExperienceGain(ExperienceGainEvent event) {
        Pokemon pokemon = event.pokemon;

        if (pokemon == null) return;
        ServerPlayer player = pokemon.getOwnerPlayer();
        if (player == null) return;

        boolean capItems         = ConfigLoader.getSettingsConfig().isBlockInteractions();
        boolean capBattles       = ConfigLoader.getSettingsConfig().isBlockLevelUps();
        boolean allowExpOverflow = ConfigLoader.getSettingsConfig().isBlockExperienceGain();
        boolean debug            = ConfigLoader.getSettingsConfig().isDebug();

        if (!capItems && !capBattles) {
            return;
        }

        int currentLevel = pokemon.getPokemonLevel();
        int levelCap     = BadgeUtils.getMaxLevelForPlayer(player);
        if (currentLevel < levelCap) {
            return;
        }

        boolean shouldBlock = false;
        String blockReason  = "";

        if (event.isFromBattle() || event.getType() == ExperienceGainType.BATTLE) {
            if (capBattles) {
                shouldBlock = true;
                blockReason  = "BATTLE";
            }
        } else {
            if (capItems) {
                shouldBlock = true;
                blockReason  = "ITEM (" + event.getType().name() + ")";
            }
        }

        if (!shouldBlock) {
            return;
        }

        int currentXP = pokemon.getExperience();
        int gainedXP  = event.getExperience();

        if (allowExpOverflow) {
            event.setCanceled(true);
            int newTotalXP = currentXP + gainedXP;
            pokemon.setExperience(newTotalXP);

            if (debug) {
                Logger.debug("Added " + blockReason + " EXP with overflow for "
                        + pokemon.getSpecies().getName() +
                        " (lvl " + currentLevel + "), cap: " + levelCap +
                        ", EXP: " + currentXP + " + " + gainedXP + " = " + newTotalXP);
            }

            String template = ConfigLoader.getMessagesConfig().getOverLevelMessage();
            String msg = template
                    .replace("{pokemon}", pokemon.getSpecies().getName())
                    .replace("{exp}", String.valueOf(gainedXP));
            player.sendSystemMessage(Component.literal(msg));

        } else {
            event.setCanceled(true);
            event.setExperience(0);

            PokemonLevel lvl = pokemon.getPokemonLevelContainer();
            lvl.setLevel(levelCap);
            int cappedXP = lvl.getExpForLevel(levelCap + 1) - 1;
            lvl.setExp(cappedXP);
            lvl.updateExpToNextLevel();

            if (debug) {
                Logger.debug("Capped " + blockReason + " EXP for "
                        + pokemon.getSpecies().getName() +
                        " (was lvl " + currentLevel + "), cap: " + levelCap +
                        ", EXP capped at: " + cappedXP);
            }

            String msg;
            if ("BATTLE".equals(blockReason)) {
                msg = ConfigLoader.getMessagesConfig().getMaxLevelReached();
            } else {
                msg = ConfigLoader.getMessagesConfig().getInteractBlocked();
            }
            player.sendSystemMessage(Component.literal(msg));
        }
    }

    public void onLevelUp(LevelUpEvent.Pre event) {
        ServerPlayer player        = event.getPlayer();
        int afterLevel                   = event.getAfterLevel();
        int levelCap                     = BadgeUtils.getMaxLevelForPlayer(player);
        boolean allowExpOverflow         = ConfigLoader.getSettingsConfig().isBlockExperienceGain();

        if (afterLevel > levelCap) {
            event.setCanceled(true);

            Pokemon pokemon = event.getPokemon();
            if (pokemon != null) {
                PokemonLevel lvl = pokemon.getPokemonLevelContainer();
                lvl.setLevel(levelCap);
                int cappedXP = lvl.getExpForLevel(levelCap + 1) - 1;
                lvl.setExp(cappedXP);
                lvl.updateExpToNextLevel();
            }

            String msg = ConfigLoader.getMessagesConfig().getMaxLevelReached();
            player.sendSystemMessage(Component.literal(msg));
        }
    }
}