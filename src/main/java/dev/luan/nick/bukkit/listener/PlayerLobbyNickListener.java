package dev.luan.nick.bukkit.listener;

import dev.luan.core.core.message.ServerMessage;
import dev.luan.core.core.message.enums.CustomColor;
import dev.luan.nick.bukkit.events.PlayerLobbyNickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLobbyNickListener implements Listener {

    @EventHandler
    public void playerLobbyNick(final PlayerLobbyNickEvent event) {
        final Player player = event.getPlayer();
        if(event.isSetNick()) {
            String nick_display = event.getRank().getIconName() + " " + event.getRank().getColor(event.getNickname());
            if(event.getNickname().isEmpty()) {
                nick_display = event.getRank().getIconName();
            }

            player.sendTitle(
                    ServerMessage.getColor("Auto Nick", CustomColor.GRADIENT_PURPLE) + " §8» " + ServerMessage.getColor("✔", CustomColor.LIGHT_GREEN),
                    ServerMessage.getMessage(player.getUniqueId(), "commands.nick.autonick-on").replaceAll("%nick_display%", nick_display),
                    5, 40, 30);
            //player.sendMessage(IMessage.getMessage(player.getUniqueId(), "commands.nick.autonick-on").replaceAll("%nick_display%", nick_display));
            return;
        }
        player.sendTitle(
                ServerMessage.getColor("Auto Nick", CustomColor.GRADIENT_PURPLE) + " §8» " + ServerMessage.getColor("✕", CustomColor.RED),
                ServerMessage.getMessage(player.getUniqueId(), "commands.nick.autonick-off"),
                5, 40, 30);
        //player.sendMessage(IMessage.getMessage(player.getUniqueId(), "commands.nick.autonick-off"));
    }
}
