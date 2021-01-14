package io.github.mjdeveloping.teleportmod;

import io.github.mjdeveloping.teleportmod.POCO.Home;
import io.github.mjdeveloping.teleportmod.POCO.Position;
import io.github.mjdeveloping.teleportmod.POCO.Warp;
import io.github.mjdeveloping.teleportmod.modules.home.commands.HomeCommand;
import io.github.mjdeveloping.teleportmod.modules.home.commands.HomesCommand;
import io.github.mjdeveloping.teleportmod.modules.misc.commands.BackCommand;
import io.github.mjdeveloping.teleportmod.modules.warp.commands.WarpCommand;
import io.github.mjdeveloping.teleportmod.modules.warp.commands.WarpsCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.*;
import java.util.logging.Logger;

import static io.github.mjdeveloping.teleportmod.HelperFunctions.setPaths;

public class TeleportMod implements ModInitializer {

	Logger logger = Logger.getLogger(TeleportMod.class.getName());

	public static HashMap<String, Home> homes;
	public static HashMap<String, Warp> warps;
	private static HashMap<String, RegistryKey<World>> worldRegistryKeys;

	private static Map<UUID, Position> playerBackPosition;
	String levelName;

	@Override
	public void onInitialize() {

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			HomesCommand.register(dispatcher);
			WarpsCommand.register(dispatcher);
			HomeCommand.register(dispatcher);
			WarpCommand.register(dispatcher);
			BackCommand.register(dispatcher);
		});

		ClientLifecycleEvents.CLIENT_STARTED.register(listener -> {

		});
		ServerLifecycleEvents.SERVER_STARTED.register(listener -> {
			worldRegistryKeys = new HashMap<>();

			FindAllRegistryKeys(listener);

			levelName = listener.getSaveProperties().getLevelName().replace('.', '_');
			setHomes(new HashMap<>());
			setWarps(new HashMap<>());
			setPlayerBackPosition(new HashMap<>());

			setPaths(levelName);
			HelperFunctions.init();
			HelperFunctions.writeHashMapsFromJSON(HelperFunctions.getHomesDir().toString(), "homes");
			HelperFunctions.writeHashMapsFromJSON(HelperFunctions.getWarpsDir().toString(), "warps");
		});

	}

	public void FindAllRegistryKeys(MinecraftServer listener) {
		List<World> worlds = new ArrayList<World>();
		listener.getWorldRegistryKeys().forEach((action -> {
			worlds.add(listener.getWorld(action));
		}));
		for (int i = 0; i < worlds.size(); i++) {
			worldRegistryKeys.put(worlds.get(i).getDimension().getSkyProperties().toString(),
					worlds.get(i).getRegistryKey());
		}


	}

	public static Map<UUID, Position> getPlayerBackPosition() {
		return playerBackPosition;
	}

	public static void setPlayerBackPosition(Map<UUID, Position> playerBackPosition) {
		TeleportMod.playerBackPosition = playerBackPosition;
	}


	public static HashMap<String, Home> getHomes() {
		return homes;
	}

	public static void setHomes(HashMap<String, Home> homes) {
		TeleportMod.homes = homes;
	}

	public static HashMap<String, Warp> getWarps() {
		return warps;
	}

	public static void setWarps(HashMap<String, Warp> warps) {
		TeleportMod.warps = warps;
	}
}
