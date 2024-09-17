package dev.luan.nick.bukkit.listener;

import dev.luan.nick.nick.Nick;
import dev.luan.nick.bukkit.BukkitNick;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void playerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(BukkitNick.getInstance(), () -> {
            Nick.getInstance().removePlayerNickProfile(player.getUniqueId());
        }, 5L);
    }
}
