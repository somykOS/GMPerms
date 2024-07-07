package net.somyk.gmperms;

import de.maxhenkel.admiral.MinecraftAdmiral;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.somyk.gmperms.command.GameModeCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GMPerms implements ModInitializer {
	public static final String MOD_ID = "gmperms";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			MinecraftAdmiral.builder(dispatcher, registryAccess).addCommandClasses(
					GameModeCommand.class
			).setPermissionManager(GMPermissionManager.INSTANCE).build();
		});

	}
}