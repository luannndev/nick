package dev.luan.nick.bungeecord;

import dev.luan.core.Core;
import dev.luan.nick.nick.Nick;
import dev.luan.nick.bungeecord.commands.NickCommand;
import dev.luan.nick.bungeecord.listener.LoginListener;
import dev.luan.nick.bungeecord.listener.PlayerDisconnectListener;
import dev.luan.nick.bungeecord.listener.ServerConnectListener;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.concurrent.TimeUnit;

public class BungeeNick extends Plugin {

    @Getter
    private static BungeeNick instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getScheduler().schedule(this, () -> {
            new Nick(Core.getInstance().getDatabaseManager(), "nick_manager");
        }, 750L, TimeUnit.MILLISECONDS);

        final PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.registerListener(this, new LoginListener());
        pluginManager.registerListener(this, new PlayerDisconnectListener());
        pluginManager.registerListener(this, new ServerConnectListener());

        pluginManager.registerCommand(this, new NickCommand("nick"));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
