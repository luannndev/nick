package dev.luan.nick.bukkit.listener;

import dev.luan.core.Core;
import dev.luan.core.core.manager.rank.RankManager;
import dev.luan.nick.nick.Nick;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void playerLogin(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if(Core.getInstance().isLobbyServer()) return;
        try {
            if(Nick.getInstance().isNicked(player.getUniqueId())) {
                String nickname = Nick.getInstance().getNickname(player.getUniqueId());
                if(nickname.isEmpty()) {
                    nickname = Nick.getInstance().getRandomNickname();
                }
                final RankManager.Rank rank = Nick.getInstance().getRank(player.getUniqueId());
                final String[] skinData = Nick.getInstance().getString(player.getUniqueId(), "skindata").split(";");
                Nick.getInstance().setPlayerNickProfile(player.getUniqueId(), player.getName(), nickname, rank, skinData);
            }
        } catch (Exception e) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }
}
