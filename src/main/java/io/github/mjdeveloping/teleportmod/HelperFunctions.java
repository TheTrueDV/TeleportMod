package io.github.mjdeveloping.teleportmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mjdeveloping.teleportmod.POCO.Home;
import io.github.mjdeveloping.teleportmod.POCO.Warp;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HelperFunctions {

	static Logger logger = Logger.getLogger(HelperFunctions.class.getName());

	private HelperFunctions() {
	}

	private static File modDir;
	private static File homesDir;
	private static File warpsDir;

	public static void setPaths(String levelName) {
		modDir = new File(FabricLoader.getInstance().getGameDir().toFile(), "saves/" + levelName + "/TeleportMod");
		setHomesDir(new File(modDir, "/homes"));
		setWarpsDir(new File(modDir, "/warps"));
	}

	public static void init() {

		modDir.mkdir();
		getHomesDir().mkdirs();
		getWarpsDir().mkdirs();

	}

	public static void sendMessage(CommandContext<ServerCommandSource> commandContext, String string) {
		TranslatableText message = new TranslatableText("greeting.message.style", string);
		try {
			commandContext.getSource().getPlayer().sendMessage(message, MessageType.CHAT,
					commandContext.getSource().getEntity().getUuid());
		} catch (CommandSyntaxException e) {

			e.printStackTrace();
		}
	}

	public static void writeHashMapsFromJSON(String jsonPath, String jsonName) {
		String jsonData;
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();

		if (jsonName.toLowerCase() == "homes") {
			try {
				Scanner reader = new Scanner(Paths.get(jsonPath + "\\homes.json").toFile());
				StringBuilder text = new StringBuilder();
				while (reader.hasNextLine()) {
					text.append(reader.nextLine());
				}
				reader.close();
				jsonData = text.toString();
				if (jsonData.isEmpty()) {
					jsonData = "{ }";
				}
				HashMap<String, Home> map = gson.fromJson(jsonData, new TypeToken<HashMap<String, Home>>() {
				}.getType());
				TeleportMod.setHomes(map);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				Scanner reader = new Scanner(Paths.get(jsonPath + "\\warps.json").toFile());
				StringBuilder text = new StringBuilder();
				while (reader.hasNextLine()) {
					text.append(reader.nextLine());
				}
				reader.close();
				jsonData = text.toString();
				if (jsonData.isEmpty()) {
					jsonData = "{ }";
				}
				HashMap<String, Warp> map = gson.fromJson(jsonData, new TypeToken<HashMap<String, Warp>>() {
				}.getType());
				TeleportMod.setWarps(map);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}

	public static void addToHomeList(String homeName, Home home) {
		TeleportMod.getHomes().put(homeName, home);
		UpdateJson(getHomesDir(), "homes.json", TeleportMod.getHomes());
	}

	public static void addToWarpList(String warpName, Warp warp) {
		TeleportMod.getWarps().put(warpName, warp);
		UpdateJson(getWarpsDir(), "warps.json", TeleportMod.getWarps());

	}

	public static void removeFromHomeList(String homeName) {
		TeleportMod.getHomes().remove(homeName);
		UpdateJson(getHomesDir(), "homes.json", TeleportMod.getHomes());
	}

	public static void removeFromWarpList(String warpName) {
		TeleportMod.getWarps().remove(warpName);
		UpdateJson(getWarpsDir(), "warps.json", TeleportMod.getWarps());
	}

	public static void UpdateJson(File path, String fileName, HashMap<?, ?> map) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(map);

		try {
			File out = new File(path, fileName);
			String test = out.getAbsolutePath();
			System.out.println(test);
			FileWriter writer = new FileWriter(out);

			if (out.length() != 0) {
				writer.append(", \n ").append(json);
			} else {
				writer.append(json);
			}
			writer.close();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error Thrown: ", e);
		}
	}

	public static File getHomesDir() {
		return homesDir;
	}

	public static void setHomesDir(File homesDir) {
		HelperFunctions.homesDir = homesDir;
	}

	public static File getWarpsDir() {
		return warpsDir;
	}

	public static void setWarpsDir(File warpsDir) {
		HelperFunctions.warpsDir = warpsDir;
	}
}
