package dev.luan.nick.bukkit.listener;

import dev.luan.core.bukkit.BukkitBootstrap;
import dev.luan.core.core.message.ServerMessage;
import dev.luan.core.core.message.enums.CustomColor;
import dev.luan.nick.nick.Nick;
import dev.luan.nick.bukkit.BukkitNick;
import dev.luan.nick.bukkit.events.PlayerNickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerNickListener implements Listener {

    @EventHandler
    public void playerNick(final PlayerNickEvent event) {
        final Player player = event.getPlayer();
        if(event.isSetNick()) {
            String nickname = event.getNickname();
            if(nickname.isEmpty()) {
                nickname = Nick.getInstance().getRandomNickname();
            }
            event.setNickname(nickname);

            Nick.getInstance().setPlayerNickProfile(player.getUniqueId(), player.getName(), nickname, event.getRank(), event.getSkinData());

            final String new_nickname = nickname;
            final int level = Nick.getInstance().getPlayerNickProfile(player.getUniqueId()).getLevel();
            Bukkit.getScheduler().runTaskLater(BukkitNick.getInstance(), () -> {
                player.sendMessage(ServerMessage.getMessage(player.getUniqueId(), "commands.nick.nick-set").replaceAll("%nickname%", ServerMessage.getColor(new_nickname, CustomColor.GRADIENT_PURPLE)));
                player.sendMessage(ServerMessage.getMessage(player.getUniqueId(), "commands.nick.nick-set-rank").replaceAll("%rank%", event.getRank().getIconName()));
                BukkitBootstrap.getPlugin().getPermissionManager().getScoreboardTeam(player, event.getRank(), level).addEntry(new_nickname);
                this.updatePlayers(player);
                player.setPlayerListName(null);
            }, 1L);
            return;
        }

        final String current_nickname = Nick.getInstance().getNickname(player.getUniqueId());
        BukkitBootstrap.getPlugin().getPermissionManager().removeScoreboardTeam(current_nickname);
        Nick.getInstance().removePlayerNickProfile(player.getUniqueId());
        player.sendMessage(ServerMessage.getMessage(player.getUniqueId(), "commands.nick.nick-remove"));
        this.updatePlayers(player);
    }

    private void updatePlayers(final Player player) {
        Bukkit.getServer().getOnlinePlayers().forEach(players -> {
            players.hidePlayer(player);
            players.showPlayer(player);
        });
    }
}
