package io.github.mjdeveloping.teleportmod.modules.home.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mjdeveloping.teleportmod.HelperFunctions;
import io.github.mjdeveloping.teleportmod.POCO.Home;
import io.github.mjdeveloping.teleportmod.POCO.Position;
import io.github.mjdeveloping.teleportmod.TeleportMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.dimension.DimensionType;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeCommand {
	static Logger logger = Logger.getLogger(HomeCommand.class.getName());

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("home").requires(serverCommandSource -> {
			return serverCommandSource.hasPermissionLevel(0);
		}).then(CommandManager.argument("homeName", StringArgumentType.greedyString()).executes((commandContext -> {
			return executeHome(commandContext, StringArgumentType.getString(commandContext, "homeName"));
		}))));
	}

	private static int executeHome(CommandContext<ServerCommandSource> commandContext, String string) throws CommandSyntaxException {
		Home moveTo = TeleportMod.getHomes().get(string);
		if (moveTo != null) {
			ServerPlayerEntity player = null;
			player = commandContext.getSource().getPlayer();
			MinecraftServer server = commandContext.getSource().getMinecraftServer();
			DimensionType currentDim = commandContext.getSource().getPlayer().getServerWorld().getDimension();

			//ServerWorld worldToTPTo = server.getWorld(TeleportMod.worldRegistryKeys.get(moveTo.getPosition().getWorld()));
			try {
				Position currentPos = new Position(player.getPos(), currentDim.getSkyProperties().toString());
				TeleportMod.getPlayerBackPosition().put(player.getUuid(), currentPos);
				server.getCommandManager().execute(server.getCommandSource(), "/execute in " + moveTo.getPosition().getWorld() + " run tp " + player.getEntityName() + " " + moveTo.getPosition().getCoords().getX() + " " + moveTo.getPosition().getCoords().getY() + " " + moveTo.getPosition().getCoords().getZ());
				//player.moveToWorld(worldToTPTo.toServerWorld());
			} catch (Exception e) {
				logger.log(Level.WARNING, "Error Found: ", e);
			}


			HelperFunctions.sendMessage(commandContext, "Teleported to home: " + string);
		} else {
			HelperFunctions.sendMessage(commandContext, "Sorry, that home does not exist.");
		}


		return 1;
	}

	private static Text sendMessage(CommandContext<ServerCommandSource> commandContext, String string) {
		return new TranslatableText("greeting.message.style", string);
	}
}
