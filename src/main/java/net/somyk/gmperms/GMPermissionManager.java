package net.somyk.gmperms;

import de.maxhenkel.admiral.permissions.PermissionManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import org.jetbrains.annotations.Nullable;
import java.util.List;

import static net.somyk.gmperms.GMPerms.LOGGER;

public class GMPermissionManager implements PermissionManager<ServerCommandSource> {

    public static final GMPermissionManager INSTANCE = new GMPermissionManager();

    private static final Permission SURVIVAL_OWN = new Permission("gmperms.survival.own", PermissionType.NOONE);
    private static final Permission SURVIVAL_OTHER = new Permission("gmperms.survival.other", PermissionType.NOONE);
    private static final Permission ADVENTURE_OWN = new Permission("gmperms.adventure.own", PermissionType.NOONE);
    private static final Permission ADVENTURE_OTHER = new Permission("gmperms.adventure.other", PermissionType.NOONE);
    private static final Permission SPECTATOR_OWN = new Permission("gmperms.spectator.own", PermissionType.NOONE);
    private static final Permission SPECTATOR_OTHER = new Permission("gmperms.spectator.other", PermissionType.NOONE);
    private static final Permission CREATIVE_OWN = new Permission("gmperms.creative.own", PermissionType.NOONE);
    private static final Permission CREATIVE_OTHER = new Permission("gmperms.creative.other", PermissionType.NOONE);

    private static final List<Permission> PERMISSIONS = List.of(
            SURVIVAL_OWN,
            SURVIVAL_OTHER,
            ADVENTURE_OWN,
            ADVENTURE_OTHER,
            SPECTATOR_OWN,
            SPECTATOR_OTHER,
            CREATIVE_OWN,
            CREATIVE_OTHER
    );

    @Override
    public boolean hasPermission(ServerCommandSource stack, String permission) {
        for (Permission p : PERMISSIONS) {
            if (!p.permission.equals(permission)) {
                continue;
            }
            if (stack.isExecutedByPlayer()) {
                return p.hasPermission(stack.getPlayer());
            }
            if (p.getType().equals(PermissionType.OPS)) {
                return stack.hasPermissionLevel(2);
            } else {
                return p.hasPermission(null);
            }
        }
        return false;
    }

    private static Boolean loaded;

    private static boolean isFabricPermissionsAPILoaded() {
        if (loaded == null) {
            loaded = FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0");
            if (loaded) {
                LOGGER.info("Using Fabric Permissions API");
            }
        }
        return loaded;
    }

    private static class Permission {
        private final String permission;
        private final PermissionType type;

        public Permission(String permission, PermissionType type) {
            this.permission = permission;
            this.type = type;
        }

        public boolean hasPermission(@Nullable ServerPlayerEntity player) {
            if (isFabricPermissionsAPILoaded()) {
                return checkFabricPermission(player);
            }
            return type.hasPermission(player);
        }

        private boolean checkFabricPermission(@Nullable ServerPlayerEntity player) {
            if (player == null) {
                return false;
            }
            TriState permissionValue = Permissions.getPermissionValue(player, permission);
            return switch (permissionValue) {
                case DEFAULT -> type.hasPermission(player);
                case TRUE -> true;
                default -> false;
            };
        }

        public PermissionType getType() {
            return type;
        }
    }

    private enum PermissionType {

        EVERYONE, NOONE, OPS;

        boolean hasPermission(@Nullable ServerPlayerEntity player) {
            return switch (this) {
                case EVERYONE -> true;
                case NOONE -> false;
                case OPS -> player != null && player.hasPermissionLevel(player.server.getOpPermissionLevel());
            };
        }

    }

}