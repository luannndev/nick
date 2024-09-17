package dev.luan.nick.bukkit.listener;

import dev.luan.core.Core;
import dev.luan.core.core.getter.RankAPI;
import dev.luan.nick.nick.Nick;
import dev.luan.nick.bukkit.events.PlayerNickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if(Core.getInstance().isLobbyServer()) return;
        if(!RankAPI.getInstance().getPlayerRank(player.getUniqueId()).isHigherEqualsLevel(Nick.getInstance().getNickableRank())) return;
        final Nick.PlayerNickProfile playerNickProfile = Nick.getInstance().getPlayerNickProfile(player.getUniqueId());
        if(playerNickProfile != null) {
            final PlayerNickEvent playerNickEvent = new PlayerNickEvent(player, playerNickProfile.getRank(), playerNickProfile.getNickname(), true, playerNickProfile.getSkinData());
            Bukkit.getServer().getPluginManager().callEvent(playerNickEvent);
        }
    }
}
