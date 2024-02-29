package cn.bedrockexecute;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;

import org.geysermc.floodgate.api.FloodgateApi;

import java.util.*;
import java.io.File;

public final class Bedrockexecute extends JavaPlugin implements Listener {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        //注册命令
        Commands reloadCommand = new Commands(this);
        Objects.requireNonNull(getCommand("bedrockexecute")).setExecutor(reloadCommand);
        Objects.requireNonNull(getCommand("bedrockexecute")).setTabCompleter(reloadCommand.getTabCompleter());
        //注册监听器
        getServer().getPluginManager().registerEvents(this, this);
        //注册权限
        Permission permission = new Permission("bedrockexecute.isbedrock", PermissionDefault.FALSE);
        getServer().getPluginManager().addPermission(permission);
        //释放配置文件
        if (!new File(getDataFolder(),"config.yml").exists()) {
            saveDefaultConfig();
//            getLogger().warning("释放配置文件");
        }
        //读取配置文件
        configManager = new ConfigManager(this);
        configManager.getConfigMap();
        //检测papi提示
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("\u001B[32m已挂钩到PlaceholderAPI.\u001B[0m");
        } else {
            getLogger().warning("\u001B[33m没有找到PlaceholderAPI! 禁用解析功能.\u001B[0m");
        }
        getLogger().info("Bedrockexecute已启用");
    }

    @Override
    public void onDisable() {
        getLogger().info("Bedrockexecute已禁用");
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent player){
        UUID player_uuid = player.getPlayer().getUniqueId();
        boolean player_is_be = FloodgateApi.getInstance().isFloodgatePlayer(player_uuid);
        boolean has_is_be_perm = player.getPlayer().hasPermission("bedrockexecute.isbedrock");
//        getLogger().warning(player.getPlayer().getName()+"玩家加入游戏");
        if (player_is_be && !has_is_be_perm) {
            //获取配置文件，并且执行命令
            Map<String, Object> configMap = configManager.getConfigMap();
            @SuppressWarnings("unchecked")
            List<String> configList = (List<String>) configMap.get("commands");

            final Iterator<String> iterator = configList.iterator();
            final JavaPlugin pluginInstance = this;
            Bukkit.getScheduler().runTaskLater(pluginInstance, new Runnable() {
                @Override
                public void run() {
                    if (iterator.hasNext()) {
                        String parse_s = iterator.next();
                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                            parse_s = PlaceholderAPI.setPlaceholders(player.getPlayer(), parse_s);
                        }
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), parse_s);
                        // 递归调用以继续执行下一个命令
                        Bukkit.getScheduler().runTaskLater(pluginInstance, this, 1); // 可以根据需要调整延迟时间
                    }
                }
            }, 1);


//            getLogger().info("基岩版");
//            getLogger().info("无权限");
        }
//        else if (player_is_be) {
//            //不操作
//            getLogger().info("基岩版");
//            getLogger().info("有权限");
//        } else {
//            //不操作
//            getLogger().info("Java版");
//        }
    }
    public void configreload(CommandSender sender) {
        configManager.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "插件已重新加载！");
    }
}
