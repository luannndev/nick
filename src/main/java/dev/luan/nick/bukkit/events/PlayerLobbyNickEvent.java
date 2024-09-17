package dev.luan.nick.bukkit.events;

import dev.luan.core.core.manager.rank.RankManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerLobbyNickEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private final Player player;
    private final RankManager.Rank rank;
    private final String nickname;
    private final boolean setNick;

    public PlayerLobbyNickEvent(final Player player, final RankManager.Rank rank, final String nickname, final boolean setNick) {
        this.player = player;
        this.rank = rank;
        this.nickname = nickname;
        this.setNick = setNick;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
