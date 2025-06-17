package de.scorezy.pixelmonlevelcapsystem.configs;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcludeLevelCapPokemonConfig {
    private final Set<String> excludedSpecies = new HashSet<>();

    @SuppressWarnings("unchecked")
    public void loadFromMap(Map<String, Object> map) {
        Object listObj = map.get("excluded_pokemon");
        if (listObj instanceof List) {
            for (Object obj : (List<Object>) listObj) {
                if (obj instanceof String) {
                    excludedSpecies.add(((String) obj).toLowerCase());
                }
            }
        }
    }

    public boolean isExcluded(String speciesName) {
        return excludedSpecies.contains(speciesName.toLowerCase());
    }

    public Set<String> getExcludedSpecies() {
        return excludedSpecies;
    }
}