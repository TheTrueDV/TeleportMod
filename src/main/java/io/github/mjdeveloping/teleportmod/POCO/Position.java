package io.github.mjdeveloping.teleportmod.POCO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.math.Vec3d;

public class Position {


    public Position(Vec3d coords, String world) {
        this.setCoords(coords);
        this.setWorld(world);
    }

    public Position(double x, double y, double z, String world) {
        Vec3d coords = new Vec3d(x, y, z);
        this.setCoords(coords);
        this.setWorld(world);
    }

    /*
     * Don't use unless absolutely needed.
     */
    public Position() {
        // TODO Auto-generated constructor stub
    }


    @SerializedName("world")
    @Expose
    private String world;

    @SerializedName("coords")
    @Expose
    private Vec3d coords;


    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }


    public Vec3d getCoords() {
        return coords;
    }

    public Vec3d getCoordsRounded() {
        return new Vec3d(Math.round(coords.x), Math.round(coords.y), Math.round(coords.z));
    }

    public void setCoords(Vec3d coords) {
        this.coords = coords;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Position{ x=");
        builder.append(coords.getX());
        builder.append(", y=");
        builder.append(coords.getY());
        builder.append(", z=");
        builder.append(coords.getZ());
        builder.append(", world=");
        builder.append(getWorld());
        builder.append("}");

        return builder.toString();
    }


//	@Override
//	public String toString() {
//
//		String positionToString = "x: " + x.intValue() + ", y: " + y.intValue() + ", z: " + z.intValue() + ", world: " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//				+ world;
//
//
//		return positionToString;
//
//	}


}
