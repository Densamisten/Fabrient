package io.github.densamisten.mixin.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import net.minecraft.client.MinecraftClient;

public class DistanceFromGround {
    static World world = MinecraftClient.getInstance().world;
    public static int getDistance(Entity e) {
        BlockState state;
        Vec3d loc = e.getPos();
        int y = (int) Math.floor(loc.y); // Use y coordinate as an integer
        BlockPos pos = new BlockPos((int) loc.x, y, (int) loc.z); // Create BlockPos with integer coordinates
        int distance = 0;
        while(world.getBlockState(pos).isAir()) {
            pos = pos.down(); // Use down() method to get the position below
            distance++;
        }
        return distance;
    }
}
