package io.github.mjdeveloping.teleportmod.modules.warp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mjdeveloping.teleportmod.HelperFunctions;
import io.github.mjdeveloping.teleportmod.POCO.Position;
import io.github.mjdeveloping.teleportmod.POCO.Warp;
import io.github.mjdeveloping.teleportmod.TeleportMod;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Map;

public class WarpsCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("warps").requires(serverCommandSource -> {
			return serverCommandSource.hasPermissionLevel(0);
		}).then(CommandManager.literal("add").executes(commandContext -> {
			return executeAdd(commandContext, "");
		}).then(CommandManager.argument("warpName", StringArgumentType.greedyString()).executes((commandContext -> {
			return executeAdd(commandContext, StringArgumentType.getString(commandContext, "warpName"));
		})))).then(CommandManager.literal("remove").executes(commandContext -> {
			return executeDelete(commandContext, "");
		}).then(CommandManager.argument("warpName", StringArgumentType.greedyString()).executes((commandContext -> {
			return executeDelete(commandContext, StringArgumentType.getString(commandContext, "warpName"));
		})))).then(CommandManager.literal("list").executes(commandContext -> {
			return executeList(commandContext);
		})));
	}

	private static int executeList(CommandContext<ServerCommandSource> commandContext) {

		StringBuilder stringBuilder = new StringBuilder();

		for (Map.Entry<String, Warp> entry : TeleportMod.getWarps().entrySet()) {
			String warpName = entry.getKey();
			Warp warp = entry.getValue();
			Position pos = entry.getValue().getPosition();

			System.out.println(warp.toString() + warpName);
			stringBuilder.append(entry.getValue().getName() + " at "
					+ "(XYZ: " + pos.getCoordsRounded().getX() + " " + pos.getCoordsRounded().getY() + " " + pos.getCoordsRounded().getZ() + " in " + pos.getWorld().split(":")[1] + ")\n");


		}

		if (!stringBuilder.toString().isEmpty()) {
			stringBuilder.insert(0, "\nWarps: \n");
			String list = stringBuilder.toString();
			HelperFunctions.sendMessage(commandContext, list);
		} else {
			HelperFunctions.sendMessage(commandContext, "There are no warps yet. \n /warps add <warp name> to add one!");
		}
		return 1;
	}

	private static int executeAdd(CommandContext<ServerCommandSource> commandContext, String string) {
		MinecraftServer minecraftServer = null;
		Entity entity = null;
		Warp newWarp = new Warp();
		Position playerPosition = new Position();
		try {
			minecraftServer = commandContext.getSource().getMinecraftServer();
			entity = commandContext.getSource().getEntity();
			playerPosition.setCoords(entity.getPos());

			playerPosition.setWorld(commandContext.getSource().getPlayer().getServerWorld().getDimension()
					.getSkyProperties().toString());

			newWarp.setPosition(playerPosition);
			newWarp.setName(string);
			if (TeleportMod.getWarps().containsKey(string)) {

			}
			HelperFunctions.addToWarpList(string, newWarp);
			//TeleportMod.warps.put(string, newWarp);
			System.out.println("Debug Point");

		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		Warp testWarp = null;
		try {
			testWarp = TeleportMod.getWarps().get(string);
		} catch (Exception e) {
			e.printStackTrace();
			testWarp = new Warp();

		}

		// sendMessage(commandContext, "Add Command Executed With Warp Name: " + string
		// + " Player Position: " +playerPosition.toString()), MessageType.CHAT,
		// entity.getUuid());
		HelperFunctions.sendMessage(commandContext, "Warp Set: " + testWarp.getName() + "\nAt: " + testWarp.getPosition());

		return 1;
	}

	private static int executeDelete(CommandContext<ServerCommandSource> commandContext, String string) {
		HelperFunctions.removeFromWarpList(string);
		HelperFunctions.sendMessage(commandContext, "Delete Command Executed With warp Name: " + string);

		return 1;
	}


}
