package de.scorezy.pixelmonlevelcapsystem.utils;

import de.scorezy.pixelmonlevelcapsystem.configs.BadgeLevelConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class PermissionHandler {

    private static final Map<String, PermissionNode<Boolean>> NODES = new HashMap<>();

    @SubscribeEvent
    public static void onPermissionNodeGather(PermissionGatherEvent.Nodes event) {
        BadgeLevelConfig blc = ConfigLoader.getBadgeLevelConfig();
        if (blc == null || !blc.isUsePermissions()) {
            return;
        }

        blc.getPermissionLevels().keySet().forEach(fullPermission -> {
            PermissionNode<Boolean> node = new PermissionNode<>(
                    "lcs",
                    fullPermission,
                    PermissionTypes.BOOLEAN,
                    (player, uuid, context) -> false
            );
            NODES.put(fullPermission, node);
            event.addNodes(node);
        });
    }

    public static boolean hasPermission(ServerPlayer player, String permission) {
        PermissionNode<Boolean> node = NODES.get(permission);
        if (node == null) {
            return false;
        }
        try {
            return PermissionAPI.getPermission(player, node);
        } catch (Exception e) {
            return false;
        }
    }
}
