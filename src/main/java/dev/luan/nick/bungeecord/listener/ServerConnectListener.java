package dev.luan.nick.bungeecord.listener;

import dev.luan.core.core.getter.RankAPI;
import dev.luan.core.core.manager.rank.RankManager;
import dev.luan.nick.nick.Nick;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectListener implements Listener {

    @EventHandler
    public void serverConnect(final ServerConnectEvent event) {
        final ProxiedPlayer proxiedPlayer = event.getPlayer();
        if(!RankAPI.getInstance().getPlayerRank(proxiedPlayer.getUniqueId()).isHigherEqualsLevel(Nick.getInstance().getNickableRank())) return;
        if(event.getTarget().getName().toLowerCase().contains("lobby")) {
            Nick.getInstance().removePlayerNickProfile(proxiedPlayer.getUniqueId());
            return;
        }
        if(Nick.getInstance().isNicked(proxiedPlayer.getUniqueId())) {
            final String[] skinData = Nick.getInstance().getRandomSkinData();
            String nickname = Nick.getInstance().getNickname(proxiedPlayer.getUniqueId());
            if(nickname.isEmpty()) {
                nickname = Nick.getInstance().getRandomNickname();
            }
            final RankManager.Rank rank = Nick.getInstance().getRank(proxiedPlayer.getUniqueId());

            Nick.getInstance().setStringValue(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), "skindata", skinData[0] + ";" + skinData[1]);
            Nick.getInstance().setPlayerNickProfile(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), nickname, rank, skinData);
        }
    }
}
