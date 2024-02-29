package cn.bedrockexecute;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigManager {
    private final Bedrockexecute plugin;
    private FileConfiguration config;
    private File configFile;
    private Map<String, Object> configMap;

    public ConfigManager(Bedrockexecute plugin) {
        this.plugin = plugin;
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        configMap = new LinkedHashMap<>(config.getValues(false)); // 使用 LinkedHashMap 保持顺序

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource("config.yml");
        if (defConfigStream != null) {
            try {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
                config.setDefaults(defConfig);
            } finally {
                try {
                    defConfigStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<String, Object> getConfigMap() {
        if (configMap == null) {
            reloadConfig();
        }
        return configMap;
    }
}
