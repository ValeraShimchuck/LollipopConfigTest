package net.lollipopmc.configtest.config;

import net.lollipopmc.lib.configurate.objectmapping.ConfigSerializable;
import net.lollipopmc.lib.configurate.objectmapping.meta.NodeKey;
import net.lollipopmc.lib.configurate.objectmapping.meta.Setting;
import net.lollipopmc.lib.configuration.holder.ConfigHolder;

import java.io.File;
import java.util.*;

@ConfigSerializable
public class Config extends ConfigHolder<Config> {


    public String publicString = "test public";
    private String privateString = "test private";

    // final can be serialized/deserialized, but can`t be reloaded by IConfigContainer.reload()
    //public final String finalString = "test final";
    public transient String thisNotSerialize = "not serialize";
    public Section section1 = new Section();
    public Section section2 = new Section();
    public OtherSection otherSection = new OtherSection();
    public ConstructorSection constructorSection = new ConstructorSection("some value");
    public int primitive = 5;
    public Integer wrapped = 4;
    public TestEnum testEnum = TestEnum.VALUE1;
    public Set<Integer> set = Set.of(1,2,3,4,5);
    public Map<TestEnum, String> mapWithEnum = new HashMap<>();
    {
        // default values
        Arrays.stream(TestEnum.values())
                .forEach(testEnum -> mapWithEnum.put(testEnum, testEnum.name().toLowerCase()));
    }
    public List<String> mutableList = new ArrayList<>();
    {
        // default value
        mutableList.add("1");
        mutableList.add("some data");
    }

    public List<String> immutableList = List.of("data1", "data2");
    public Map<String, Map<String, Integer>> mapInMap = new HashMap<>();
    {
        Map<String, Integer> testMap = new HashMap<>();
        testMap.put("some key", 1);
        testMap.put("another key", 2);
        mapInMap.put("map", testMap);
    }
    public NodeParentSection nodeParent = new NodeParentSection();

    public Config(File baseFilePath) {
        super(baseFilePath);
    }

    private Config() { // must have empty constructor
        this(null);
    }

    public String getPrivateString() {
        return privateString;
    }

    @ConfigSerializable
    public static class Section { // class must be static
        public String sectionValue = "sectionValue";
    }

    @ConfigSerializable
    public static class OtherSection {

        public InnerSection innerSection = new InnerSection();

        @ConfigSerializable
        public static class InnerSection {
            public String value = "data";
        }

    }

    @ConfigSerializable
    public static class ConstructorSection {

        public String value;

        public ConstructorSection(String value) {
            this.value = value;
        }

        private ConstructorSection() { } // must have empty constructor

    }

    @ConfigSerializable
    public static class NodeParentSection {

        @Setting(nodeFromParent = true)
        public String value = "node parent";
    }

    @Override
    public void onPostLoad() {
        System.out.println("hey, i run after config load");
    }

    public enum TestEnum {
        VALUE1, VALUE2;
    }

}
