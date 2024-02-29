package cn.bedrockexecute;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class Commands extends CommandsTabCompleter implements CommandExecutor {
    private Bedrockexecute plugin;

    public Commands(Bedrockexecute plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("bedrockexecute")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                // 重新加载配置文件
                plugin.configreload(sender);
                return true;
            }
        }
        return false;
    }

    public TabCompleter getTabCompleter() {
        return new CommandsTabCompleter();
    }
}
