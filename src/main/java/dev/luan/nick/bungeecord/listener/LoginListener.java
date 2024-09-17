package dev.luan.nick.bungeecord.listener;

import dev.luan.nick.nick.Nick;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class LoginListener implements Listener {

    @EventHandler
    public void login(final LoginEvent event) {
        final UUID uuid = event.getConnection().getUniqueId();
        final String player_name = event.getConnection().getName();
        Nick.getInstance().insertPlayer(uuid, player_name);
    }
}

