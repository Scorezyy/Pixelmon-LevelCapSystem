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
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.List;
import java.util.stream.Collectors;
public class SpawnLevelCapListener {

    private static final String NBT_CLAMPED = "PixelmonLevelCap.Cap";
    private static final double RADIUS = 6 * 16;

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (!ConfigLoader.getSettingsConfig().isLevelCapWildPokemons()) {
            return;
        }

        Entity ent = event.getEntity();
        if (!(ent instanceof PixelmonEntity)) {
            return;
        }

        Level world = event.getLevel();
        if (!(world instanceof ServerLevel)) {
            return;
        }

        PixelmonEntity pkm = (PixelmonEntity) ent;
        Pokemon poke = pkm.getPokemon();

        String baseKey = poke.getSpecies()
                .getStrippedName()
                .toLowerCase();

        Stats formStats = poke.getForm();
        String formJsonName = formStats.getName()
                .toLowerCase();

        String fullKey;
        if (formJsonName.equals("normal") || formJsonName.equals(baseKey) || formJsonName.isEmpty()) {
            fullKey = baseKey;
        } else {
            fullKey = formJsonName + "-" + baseKey;
        }

        if (poke.getSpecies().isLegendary()
                && !ConfigLoader.getSettingsConfig().isLevelCapLegendaryPokemons()) {
            if (ConfigLoader.getSettingsConfig().isDebug()) {
                Logger.debug("Skipping legendary " + fullKey);
            }
            return;
        }

        if (pkm.getOwner() != null
                || poke.getOwnerPlayer() != null) {
            return;
        }

        CompoundTag data = pkm.getPersistentData();
        if (data.getBoolean(NBT_CLAMPED)) {
            return;
        }

        ExcludeLevelCapPokemonConfig excl = ConfigLoader.getExcludeLevelCapPokemonConfig();
        if (excl.isExcluded(baseKey, fullKey)) {
            if (ConfigLoader.getSettingsConfig().isDebug()) {
                Logger.debug("Skipping excluded Pokémon " + fullKey);
            }
            return;
        }

        ServerLevel serverWorld = (ServerLevel) world;
        List<ServerPlayer> nearby = serverWorld.players().stream()
                .filter(pl -> pl.distanceTo(pkm) <= RADIUS)
                .collect(Collectors.toList());

        if (nearby.isEmpty()) {
            return;
        }

        int minCap = nearby.stream()
                .mapToInt(BadgeUtils::getMaxLevelForPlayer)
                .min()
                .orElse(1);

        int origLevel = pkm.getLvl().getPokemonLevel();
        int clamped = Math.min(Math.max(1, origLevel), minCap);

        if (ConfigLoader.getSettingsConfig().isDebug()) {
            Logger.debug(String.format(
                    "%s origLevel=%d -> setLevel=%d (BadgeLevelCap=%d, playersInRange=%d)",
                    fullKey, origLevel, clamped, minCap, nearby.size()));
        }

        pkm.getLvl().setLevel(clamped);
        data.putBoolean(NBT_CLAMPED, true);
    }
}