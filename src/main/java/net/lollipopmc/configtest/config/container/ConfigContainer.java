package net.lollipopmc.configtest.config.container;

import net.lollipopmc.configtest.config.Config;
import net.lollipopmc.configtest.config.ConfigWithSerializers;
import net.lollipopmc.configtest.config.ConfigWithUnSerializableDependency;
import net.lollipopmc.lib.configuration.container.RawConfigContainer;

import java.io.File;

public class ConfigContainer extends RawConfigContainer {

    private final ConfigWithUnSerializableDependency.Data data;

    public ConfigContainer(File path, ConfigWithUnSerializableDependency.Data data) {
        super(path);
        this.data = data;
        loadDefaults();
    }

    private void loadDefaults() {
        loadConfigs(
                new Config(toFile("config.yml")),
                new ConfigWithSerializers(toFile("configwithserializers.yml")),
                new ConfigWithUnSerializableDependency(toFile("configwithunserializeabledependency.yml"), data)
        );
    }

}
