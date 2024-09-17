package dev.luan.nick.bungeecord.listener;

import dev.luan.nick.nick.Nick;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void playerDisconnect(final PlayerDisconnectEvent event) {
        final ProxiedPlayer proxiedPlayer = event.getPlayer();
        Nick.getInstance().removePlayerNickProfile(proxiedPlayer.getUniqueId());
    }
}
