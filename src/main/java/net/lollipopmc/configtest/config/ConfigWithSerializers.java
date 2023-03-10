package net.lollipopmc.configtest.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.lollipopmc.configtest.config.serializers.LocationSerializer;
import net.lollipopmc.lib.configurate.objectmapping.ConfigSerializable;
import net.lollipopmc.lib.configurate.serialize.TypeSerializerCollection;
import net.lollipopmc.lib.configuration.holder.ConfigHolder;
import net.lollipopmc.lib.configuration.serializers.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;

@ConfigSerializable
public class ConfigWithSerializers extends ConfigHolder<ConfigWithSerializers> {

    public Component someComponent = Component.text("component text");
    public Component multiLineComponent = Component.text("first line").append(Component.newline())
            .append(Component.text("second line"));
    public Component coloredComponent = Component.text("red", Style.style()
            .color(TextColor.color(255,0,0))
            .build());
    public Location location = new Location(Bukkit.getWorlds().get(0), 0,0,0);

    public ConfigWithSerializers(File baseFilePath) {
        super(baseFilePath, TypeSerializerCollection.builder()
                .register(Location.class, new LocationSerializer())
                .register(Component.class, new ComponentSerializer())
                .build());
    }

    private ConfigWithSerializers() {
        this(null);
    }

}
