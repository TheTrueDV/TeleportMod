package io.github.mjdeveloping.teleportmod.POCO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Home {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("position")
    @Expose
    private Position position;
    @SerializedName("playerUUID")
    @Expose
    private UUID playerUUID;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Position getPosition() {
        return position;
    }


    public void setPosition(Position position) {
        this.position = position;
    }


    public UUID getPlayerUUID() {
        return playerUUID;
    }


    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

}
