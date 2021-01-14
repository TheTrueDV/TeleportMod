package io.github.mjdeveloping.teleportmod.modules.warp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mjdeveloping.teleportmod.HelperFunctions;
import io.github.mjdeveloping.teleportmod.POCO.Position;
import io.github.mjdeveloping.teleportmod.POCO.Warp;
import io.github.mjdeveloping.teleportmod.TeleportMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.dimension.DimensionType;

public class WarpCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("warp").requires(serverCommandSource -> {
			return serverCommandSource.hasPermissionLevel(0);
		}).then(CommandManager.argument("warpName", StringArgumentType.greedyString()).executes((commandContext -> {
			return executeWarp(commandContext, StringArgumentType.getString(commandContext, "warpName"));
		}))));
	}

	private static int executeWarp(CommandContext<ServerCommandSource> commandContext, String string) throws CommandSyntaxException {
		Warp moveTo = TeleportMod.getWarps().get(string);
		if (moveTo != null) {
			MinecraftServer server = commandContext.getSource().getMinecraftServer();
			ServerPlayerEntity player = null;
			DimensionType currentDim = commandContext.getSource().getPlayer().getServerWorld().getDimension();
			player = commandContext.getSource().getPlayer();
			try {
				player = commandContext.getSource().getPlayer();
			} catch (CommandSyntaxException e) {

				e.printStackTrace();
			}
			Position currentPos = new Position(player.getPos(), currentDim.getSkyProperties().toString());
			TeleportMod.getPlayerBackPosition().put(player.getUuid(), currentPos);

			server.getCommandManager().execute(server.getCommandSource(), "/execute in " + moveTo.getPosition().getWorld() + " run tp " + player.getEntityName() + " " + moveTo.getPosition().getCoords().getX() + " " + moveTo.getPosition().getCoords().getY() + " " + moveTo.getPosition().getCoords().getZ());
			HelperFunctions.sendMessage(commandContext, "Warped to: " + string);
		} else {
			HelperFunctions.sendMessage(commandContext, "Sorry, that warp does not exist.");
		}
		return 1;
	}

	private static Text sendMessage(CommandContext<ServerCommandSource> commandContext, String string) {
		return new TranslatableText("greeting.message.style", string);
	}
}
