package de.scorezy.pixelmonlevelcap.config;

import de.scorezy.pixelmonlevelcap.Main;
import de.scorezy.pixelmonlevelcap.lib.Reference;
import info.pixelmon.repack.yaml.snakeyaml.DumperOptions;
import info.pixelmon.repack.yaml.snakeyaml.Yaml;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {

//    private static final String CONFIG_FILE_NAME = "levelcap.yml";
//
//    private static final Map<String, Object> configData = new HashMap<>();
//
//    public static void loadConfig() {
//        Path configDir = FMLPaths.CONFIGDIR.get();
//        File configFile = new File(configDir.toFile(), CONFIG_FILE_NAME);
//
//        if (!configFile.exists()) {
//            createDefaultConfig(configFile);
//        }
//
//        try (BufferedReader reader = Files.newBufferedReader(configFile.toPath(), StandardCharsets.UTF_8)) {
//            Yaml yaml = new Yaml();
//            configData.putAll(yaml.load(reader));
//        } catch (IOException e) {
//            Main.LOGGER.error(e.toString());
//        }
//    }
//
//    private static void createDefaultConfig(File configFile) {
//        Map<String, Object> yamlData = new HashMap<>();
//
//        yamlData.put("badge_levels", Reference.DEFAULT_CAPPED_LEVELS);
//
//        DumperOptions options = new DumperOptions();
//        options.setIndent(2);
//        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
//        options.setPrettyFlow(true);
//
//        Yaml yaml = new Yaml(options);
//
//        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(configFile.toPath()), StandardCharsets.UTF_8)) {
//            yaml.dump(yamlData, writer);
//        } catch (IOException e) {
//            Main.LOGGER.error(e.toString());
//        }
//    }
}