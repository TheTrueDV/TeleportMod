package io.github.mjdeveloping.teleportmod.modules.home.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mjdeveloping.teleportmod.HelperFunctions;
import io.github.mjdeveloping.teleportmod.POCO.Home;
import io.github.mjdeveloping.teleportmod.POCO.Position;
import io.github.mjdeveloping.teleportmod.TeleportMod;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;


public class HomesCommand {

	private static Logger logger = Logger.getLogger(HomesCommand.class.getName());

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("homes").requires(serverCommandSource -> {
			return serverCommandSource.hasPermissionLevel(0);
		}).then(CommandManager.literal("add").executes(commandContext -> {
			return executeAdd(commandContext, "");
		}).then(CommandManager.argument("homeName", StringArgumentType.greedyString()).executes((commandContext -> {
			return executeAdd(commandContext, StringArgumentType.getString(commandContext, "homeName"));
		})))).then(CommandManager.literal("remove").executes(commandContext -> {
			return executeDelete(commandContext, "");
		}).then(CommandManager.argument("homeName", StringArgumentType.greedyString()).executes((commandContext -> {
			return executeDelete(commandContext, StringArgumentType.getString(commandContext, "homeName"));
		})))).then(CommandManager.literal("list").executes(commandContext -> {
			return executeList(commandContext);
		})));
	}

	private static int executeList(CommandContext<ServerCommandSource> commandContext) {

		StringBuilder stringBuilder = new StringBuilder();

		if (TeleportMod.getHomes() != null) {
			for (Map.Entry<String, Home> entry : TeleportMod.getHomes().entrySet()) {
				try {

					UUID uuid = commandContext.getSource().getPlayer().getUuid();
					UUID entryUUID = entry.getValue().getPlayerUUID();
					if (entryUUID.equals(uuid)) {
						Position pos = entry.getValue().getPosition();

						stringBuilder.append(entry.getValue().getName() + " "
								+ "(XYZ: " + (int) pos.getCoordsRounded().getX() + " " + (int) pos.getCoordsRounded().getY() + " " + (int) pos.getCoordsRounded().getZ() + " in " + pos.getWorld().split(":")[1] + ")\n");


					}
				} catch (CommandSyntaxException e) {
					e.printStackTrace();
				}
			}

			if (!stringBuilder.toString().isEmpty()) {
				stringBuilder.insert(0, "\nHomes: \n");
				String list = stringBuilder.toString();
				HelperFunctions.sendMessage(commandContext, list);
			} else {
				HelperFunctions.sendMessage(commandContext, "You have no homes yet. \n /homes add <home name> to add one!");
			}
			return 1;
		} else {
			TeleportMod.setHomes(new HashMap<>());
			executeList(commandContext);
			return 0;
		}


	}


	private static int executeAdd(CommandContext<ServerCommandSource> commandContext, String string) {

		Entity entity = null;
		Home newHome = new Home();
		Position playerPosition = new Position();
		try {

			entity = commandContext.getSource().getEntity();
			playerPosition.setCoords(entity.getPos());
			playerPosition.setWorld(commandContext.getSource().getPlayer().getServerWorld().getDimension()
					.getSkyProperties().toString());

			newHome.setPosition(playerPosition);
			newHome.setName(string);
			newHome.setPlayerUUID(entity.getUuid());
			if (TeleportMod.getHomes().containsKey(string)) {

			}
			HelperFunctions.addToHomeList(string, newHome);

		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		Home testHome = null;
		try {
			testHome = TeleportMod.getHomes().get(string);
		} catch (Exception e) {
			e.printStackTrace();
			testHome = new Home();

		}


		HelperFunctions.sendMessage(commandContext,
				"Home Set: " + testHome.getName() + "\nAt: " + testHome.getPosition());

		return 1;
	}

	private static int executeDelete(CommandContext<ServerCommandSource> commandContext, String string) {
		HelperFunctions.removeFromHomeList(string);
		HelperFunctions.sendMessage(commandContext, "Delete Command Executed With Home Name: " + string);

		return 1;
	}

}
