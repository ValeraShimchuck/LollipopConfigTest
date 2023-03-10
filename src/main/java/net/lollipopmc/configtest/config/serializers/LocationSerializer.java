package net.lollipopmc.configtest.config.serializers;

import net.lollipopmc.lib.configurate.ConfigurationNode;
import net.lollipopmc.lib.configurate.serialize.SerializationException;
import net.lollipopmc.lib.configurate.serialize.TypeSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;

public class LocationSerializer implements TypeSerializer<Location> {

    public Location deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.isNull()) {
            return null;
        } else {
            ConfigurationNode world = node.node("world");
            ConfigurationNode x = node.node("x");
            ConfigurationNode y = node.node("y");
            ConfigurationNode z = node.node("z");
            ConfigurationNode pitch = node.node("pitch");
            ConfigurationNode yaw = node.node("yaw");
            return new Location(
                    Bukkit.getWorld(world.getString()),
                    x.getDouble(0.0),
                    y.getDouble(0.0),
                    z.getDouble(0.0),
                    pitch.getFloat(0.0F),
                    yaw.getFloat(0.0F)
            );
        }
    }

    public void serialize(Type type, @Nullable Location location, ConfigurationNode node) throws SerializationException {
        if (location == null) {
            node.raw(null);
        } else {
            node.node("world").set(location.getWorld().getName());
            node.node("x").set(location.getX());
            node.node("y").set(location.getY());
            node.node("z").set(location.getZ());
            node.node("pitch").set(location.getPitch());
            node.node("yaw").set(location.getYaw());
        }
    }
}
