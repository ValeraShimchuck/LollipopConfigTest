package net.lollipopmc.configtest;

import net.kyori.adventure.text.Component;
import net.lollipopmc.configtest.config.Config;
import net.lollipopmc.configtest.config.ConfigWithSerializers;
import net.lollipopmc.configtest.config.ConfigWithUnSerializableDependency;
import net.lollipopmc.configtest.config.container.ConfigContainer;
import net.lollipopmc.lib.command.CommandManager;
import net.lollipopmc.lib.command.exceptions.InvalidSyntaxException;
import net.lollipopmc.lib.command.execution.CommandExecutionCoordinator;
import net.lollipopmc.lib.command.minecraft.extras.MinecraftExceptionHandler;
import net.lollipopmc.lib.command.paper.PaperCommandManager;
import net.lollipopmc.lib.configuration.container.interfaces.IConfigContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.function.Function;

public final class ConfigTestPlugin extends JavaPlugin {

    private IConfigContainer container;
    private CommandManager<CommandSender> commandManager;

    @Override
    public void onEnable() {
        container = new ConfigContainer(getDataFolder(), new ConfigWithUnSerializableDependency.Data("data"));
        commandManager = setupCommandManager();
        commandManager.command(commandManager.commandBuilder("configtest")
                .literal("reload")
                .handler(ctx -> {
                    container.reload();
                    printConfigsData();
                }));
        printConfigsData();
        // Plugin startup logic
    }

    private void printConfigsData() {
        ConfigWithSerializers configWithSerializers = container.get(ConfigWithSerializers.class);
        ConfigWithUnSerializableDependency configWithUnSerializableDependency =
                container.get(ConfigWithUnSerializableDependency.class);
        printConfigValues();
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendConfigWithSerializersComponents(player);
            player.teleport(configWithSerializers.location);
        });
        sendConfigWithSerializersComponents(Bukkit.getConsoleSender());
        System.out.println("location: " + configWithSerializers.location);
        print(
                "otherData: " + configWithUnSerializableDependency.otherData,
                "Data data: " + configWithUnSerializableDependency.getData().getImportantData()
        );
    }

    private void printConfigValues() {
        Config config = container.get(Config.class);
        print(
                "publicString: " + config.publicString,
                "privateString: " + config.getPrivateString(),
                //"finalString: " + config.finalString,
                "NOT SERIALIZABLE: " + config.thisNotSerialize,
                "section1: " + config.section1.sectionValue,
                "section2: " + config.section2.sectionValue,
                "otherSection: " + config.otherSection.innerSection.value,
                "constructorSection: " + config.constructorSection.value,
                "primitive: " + config.primitive,
                "wrapped: " + config.wrapped,
                "testEnum: " + config.testEnum,
                "set: " + config.set,
                "mapWithEnum: " + config.mapWithEnum,
                "mutableList: " + config.mutableList,
                "immutableList: " + config.immutableList,
                "mapInMap: " + config.mapInMap,
                "nodeParent: " + config.nodeParent.value
        );
    }

    private void sendConfigWithSerializersComponents(CommandSender sender) {
        ConfigWithSerializers configWithSerializers = container.get(ConfigWithSerializers.class);
        sender.sendMessage(prepend("colored: ", configWithSerializers.coloredComponent));
        sender.sendMessage(prepend("multiLine: ", configWithSerializers.multiLineComponent));
        sender.sendMessage(prepend("some: ", configWithSerializers.someComponent));
    }

    private Component prepend(String prepender, Component message) {
        return Component.text(prepender).append(message);
    }

    private void print(String... strings) {
        Arrays.stream(strings).forEach(System.out::println);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private CommandManager<CommandSender> setupCommandManager() {
        CommandManager<CommandSender> manager;
        try {
            manager = new PaperCommandManager<>(
                    this, CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        new MinecraftExceptionHandler<CommandSender>()
                .withDefaultHandlers()
                .apply(manager, s -> s);
        return manager;
    }

}
