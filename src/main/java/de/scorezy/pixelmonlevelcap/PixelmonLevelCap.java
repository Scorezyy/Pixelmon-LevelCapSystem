package de.scorezy.pixelmonlevelcap;

import com.mojang.brigadier.CommandDispatcher;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.config.api.yaml.YamlConfigFactory;
import de.scorezy.pixelmonlevelcap.commands.ReloadCmd;
import de.scorezy.pixelmonlevelcap.config.PLCConfig;
import de.scorezy.pixelmonlevelcap.lib.Reference;
import de.scorezy.pixelmonlevelcap.listeners.*;
import net.minecraft.command.CommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(Reference.MOD_ID)
public class PixelmonLevelCap {
    private static PixelmonLevelCap INSTANCE;

    public static final Logger LOGGER = LogManager.getLogger();

    private PLCConfig config;

    public PixelmonLevelCap() {
        INSTANCE = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static PixelmonLevelCap getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PixelmonLevelCap();
        }
        return INSTANCE;
    }

    private void setup(FMLCommonSetupEvent event) {
        loadConfigs();

        Pixelmon.EVENT_BUS.register(new CaptureEventListener());
        Pixelmon.EVENT_BUS.register(new TradeEventListener());
        Pixelmon.EVENT_BUS.register(new PlayerInteractListener());
        Pixelmon.EVENT_BUS.register(new LevelUpEventListener());
        Pixelmon.EVENT_BUS.register(new RaidCaptureEventListener());
        Pixelmon.EVENT_BUS.register(new NPCTradeEventListener());
    }

    public void loadConfigs() {
        try {
            this.config = YamlConfigFactory.getInstance(PLCConfig.class);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerEvents {
        @SubscribeEvent
        public static void onServerStarting(FMLServerStartedEvent event) {
            CommandDispatcher<CommandSource> dispatcher = event.getServer().getCommands().getDispatcher();
            ReloadCmd.register(dispatcher);
        }
    }

    public PLCConfig getConfig() {
        return config;
    }
}
