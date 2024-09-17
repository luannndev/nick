package dev.luan.nick.bukkit.events;

import dev.luan.core.core.manager.rank.RankManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerNickEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private final Player player;
    private final RankManager.Rank rank;
    @Setter
    private String nickname;
    private final boolean setNick;
    private final String[] skinData;

    public PlayerNickEvent(final Player player, final RankManager.Rank rank, final String nickname, final boolean setNick, final String[] skinData) {
        this.player = player;
        this.rank = rank;
        this.nickname = nickname;
        this.setNick = setNick;
        this.skinData = skinData;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
