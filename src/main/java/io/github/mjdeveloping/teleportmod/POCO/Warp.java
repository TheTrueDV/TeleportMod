package io.github.mjdeveloping.teleportmod.POCO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Warp {

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("position")
	@Expose
	private Position position;

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
}
