package io.github.mjdeveloping.teleportmod.modules.misc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mjdeveloping.teleportmod.HelperFunctions;
import io.github.mjdeveloping.teleportmod.POCO.Position;
import io.github.mjdeveloping.teleportmod.TeleportMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class BackCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("back").requires(serverCommandSource -> {
			return serverCommandSource.hasPermissionLevel(0);
		}).executes((commandContext -> {
			return executeBack(commandContext);
		})));
	}

	private static int executeBack(CommandContext<ServerCommandSource> commandContext) {
		MinecraftServer server = commandContext.getSource().getMinecraftServer();
		try {
			UUID playerUUID = commandContext.getSource().getPlayer().getUuid();
			ServerPlayerEntity player = commandContext.getSource().getPlayer();
			if (TeleportMod.getPlayerBackPosition().containsKey(playerUUID)) {
				Position destinationPos = TeleportMod.getPlayerBackPosition().get(playerUUID);
				Position currentPos = new Position(player.getPos(), player.getServerWorld().getDimension().getSkyProperties().toString());
				TeleportMod.getPlayerBackPosition().replace(playerUUID, currentPos);

				server.getCommandManager().execute(server.getCommandSource(), "/execute in " + destinationPos.getWorld() + " run tp " + player.getEntityName() + " " + destinationPos.getCoords().getX() + " " + destinationPos.getCoords().getY() + " " + destinationPos.getCoords().getZ());
			} else {
				HelperFunctions.sendMessage(commandContext, "You haven't teleported anywhere, so you can't go back.");
			}


		} catch (CommandSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

}
