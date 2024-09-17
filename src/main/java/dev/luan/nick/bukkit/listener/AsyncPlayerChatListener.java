package dev.luan.nick.bukkit.listener;

import dev.luan.core.bukkit.BukkitBootstrap;
import dev.luan.core.core.getter.LevelAPI;
import dev.luan.core.core.getter.RankAPI;
import dev.luan.core.core.manager.rank.RankManager;
import dev.luan.nick.nick.Nick;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void asyncPlayerChat(final AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        if(!BukkitBootstrap.getPlugin().isDefaultChatMessage()) return;
        final Player player = event.getPlayer();
        Bukkit.getOnlinePlayers().forEach(players -> {
            players.sendMessage(this.getPlayerName(player, players) + " §8» §f" + event.getMessage());
        });
    }

    public String getPlayerName(final Player player, final Player target) {
        final RankManager.Rank playerRank = RankAPI.getInstance().getPlayerRank(player.getUniqueId());
        String player_name = "§e" + LevelAPI.getInstance().getLevel(player.getUniqueId()) + " §8| " + (playerRank.getPrefix().isEmpty() ? playerRank.getColor(player.getName()) : "§8[" + playerRank.getPrefix() + "§8] §7" + player.getName());

        final Nick.PlayerNickProfile playerNickProfile = Nick.getInstance().getPlayerNickProfile(player.getUniqueId());
        if(playerNickProfile == null || RankAPI.getInstance().getPlayerRank(target.getUniqueId()).getPermissionsLevel() >= playerRank.getPermissionsLevel()) return player_name;


        player_name = "§e" + playerNickProfile.getLevel() + " §8| " + (playerNickProfile.getRank().getPrefix().isEmpty() ? playerNickProfile.getRank().getColor(playerNickProfile.getNickname()) : "§8[" + playerNickProfile.getRank().getPrefix() + "§8] §7" + playerNickProfile.getNickname());
        return player_name;
    }
}
