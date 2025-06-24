package de.scorezy.pixelmonlevelcapsystem.listeners.spawn;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import de.scorezy.pixelmonlevelcapsystem.configs.ExcludeLevelCapPokemonConfig;
import de.scorezy.pixelmonlevelcapsystem.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "pixelmonlevelcapsystem", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnLevelCapListener {
    private static final String NBT_CLAMPED = "PixelmonLevelCap.Cap";
    private static final double RADIUS = 6 * 16;

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!ConfigLoader.getSettingsConfig().isLevelCapWildPokemons()) return;
        if (event.loadedFromDisk()) return;

        Entity ent = event.getEntity();
        if (!(ent instanceof PixelmonEntity pkm)) return;
        if (!(event.getLevel() instanceof ServerLevel world)) return;

        Pokemon poke = pkm.getPokemon();

        String baseKey = poke.getSpecies().getStrippedName().toLowerCase();
        Stats formStats = poke.getForm();
        String formJsonName = formStats.getName().toLowerCase();
        String fullKey = (formJsonName.equals("normal") || formJsonName.equals(baseKey) || formJsonName.isEmpty())
                ? baseKey
                : formJsonName + "-" + baseKey;

        if (poke.getSpecies().isLegendary()
                && !ConfigLoader.getSettingsConfig().isLevelCapLegendaryPokemons()) {
            if (ConfigLoader.getSettingsConfig().isDebug()) {
                Logger.debug("Skipping legendary " + fullKey);
            }
            return;
        }

        if (pkm.getOwner() != null
                || poke.getOwnerPlayerUUID() != null) {
            return;
        }

        CompoundTag data = pkm.getPersistentData();
        if (data.getBoolean(NBT_CLAMPED)) return;

        ExcludeLevelCapPokemonConfig excl = ConfigLoader.getExcludeLevelCapPokemonConfig();
        if (excl.isExcluded(baseKey, fullKey)) {
            if (ConfigLoader.getSettingsConfig().isDebug()) {
                Logger.debug("Skipping excluded Pokémon " + fullKey);
            }
            return;
        }

        List<ServerPlayer> nearby = world.getPlayers(pl -> pl.distanceTo(pkm) <= RADIUS);
        if (nearby.isEmpty()) return;

        int minCap = nearby.stream()
                .mapToInt(BadgeUtils::getMaxLevelForPlayer)
                .min()
                .orElse(1);

        int origLevel = poke.getPokemonLevel();
        int clamped = Math.min(Math.max(1, origLevel), minCap);

        if (origLevel > clamped && ConfigLoader.getSettingsConfig().isDebug()) {
            Logger.debug(String.format(
                    "%s origLevel=%d -> setLevel=%d (BadgeLevelCap=%d, playersInRange=%d)",
                    fullKey, origLevel, clamped, minCap, nearby.size()
            ));
        }

        poke.setLevel(clamped);
        data.putBoolean(NBT_CLAMPED, true);
    }
}