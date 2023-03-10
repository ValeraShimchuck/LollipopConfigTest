package net.lollipopmc.configtest.config;

import net.lollipopmc.lib.configurate.objectmapping.ConfigSerializable;
import net.lollipopmc.lib.configuration.holder.ConfigHolder;

import java.io.File;

@ConfigSerializable
public class ConfigWithUnSerializableDependency
        extends ConfigHolder<ConfigWithUnSerializableDependency> {

    private transient Data data;
    public String otherData = "otherData";

    public ConfigWithUnSerializableDependency(File baseFilePath, Data data) {
        super(baseFilePath);
        this.data = data;
    }

    private ConfigWithUnSerializableDependency() {
        this(null, null);
    }

    public static class Data {

        private final String importantData;

        public Data(String importantData) {
            this.importantData = importantData;
        }

        public String getImportantData() {
            return importantData;
        }
    }

    @Override
    protected void setFieldsOnLoad(ConfigWithUnSerializableDependency newConfig) {
        newConfig.data = data;
    }

    public Data getData() {
        return data;
    }

}
