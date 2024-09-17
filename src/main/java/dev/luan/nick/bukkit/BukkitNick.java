package dev.luan.nick.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import dev.luan.core.Core;
import dev.luan.core.core.manager.rank.RankManager;
import dev.luan.core.core.pluginmessage.PluginMessageManager;
import dev.luan.nick.nick.Nick;
import dev.luan.nick.bukkit.events.PlayerLobbyNickEvent;
import dev.luan.nick.bukkit.events.PlayerNickEvent;
import dev.luan.nick.bukkit.listener.*;
import dev.luan.nick.bukkit.packets.PlayerNickPacket;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class BukkitNick extends JavaPlugin implements PluginMessageListener {

    @Getter
    private static BukkitNick instance;

    private ProtocolManager protocolManager;

    @Override
    public void onLoad() {
        instance = this;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, () -> { new Nick(Core.getInstance().getDatabaseManager(), "nick_manager"); }, 3L);

        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerChatListener(), this);
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerLobbyNickListener(), this);
        pluginManager.registerEvents(new PlayerLoginListener(), this);
        pluginManager.registerEvents(new PlayerNickListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);

        this.protocolManager.addPacketListener(new PlayerNickPacket(this, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO));

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player unusedPlayer, byte[] message) {
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        final String pluginMessageTypeString = in.readUTF();
        final PluginMessageManager.PluginMessageType pluginMessageType = PluginMessageManager.PluginMessageType.valueOf(pluginMessageTypeString);
        if(pluginMessageType == null) return;

        if(pluginMessageType.equals(PluginMessageManager.PluginMessageType.PLAYER_NICK)) {
            final String uuid = in.readUTF();

            final Player targetPlayer = Bukkit.getPlayer(UUID.fromString(uuid));
            if(targetPlayer == null || !targetPlayer.isOnline()) return;
            final String nickedString = in.readUTF();
            if(nickedString.equalsIgnoreCase("NICK")) {
                final String nickname = in.readUTF();

                final String getRank = in.readUTF();
                final RankManager.Rank rank = RankManager.Rank.fromString(getRank);

                final String skinData = in.readUTF();
                final String[] skinDatas = new String[]{skinData.split(";")[0], skinData.split(";")[1]};
                final PlayerNickEvent playerNickEvent = new PlayerNickEvent(targetPlayer, rank, nickname, true, skinDatas);
                Bukkit.getServer().getPluginManager().callEvent(playerNickEvent);
                return;
            }
            final PlayerNickEvent playerNickEvent = new PlayerNickEvent(targetPlayer, null, Nick.getInstance().getNickname(targetPlayer.getUniqueId()), false, null);
            Bukkit.getServer().getPluginManager().callEvent(playerNickEvent);
            return;
        }

        if(pluginMessageType.equals(PluginMessageManager.PluginMessageType.LOBBY_PLAYER_NICK)) {
            final String uuid = in.readUTF();
            final Player targetPlayer = Bukkit.getPlayer(UUID.fromString(uuid));
            if(targetPlayer == null || !targetPlayer.isOnline()) return;
            final String nickedString = in.readUTF();
            if(nickedString.equalsIgnoreCase("NICK")) {
                final String nickname = in.readUTF();

                final String getRank = in.readUTF();
                final RankManager.Rank rank = RankManager.Rank.fromString(getRank);

                final PlayerLobbyNickEvent playerLobbyNickEvent = new PlayerLobbyNickEvent(targetPlayer, rank, nickname, true);
                Bukkit.getServer().getPluginManager().callEvent(playerLobbyNickEvent);
                return;
            }
            final PlayerLobbyNickEvent playerLobbyNickEvent = new PlayerLobbyNickEvent(targetPlayer, null, null, false);
            Bukkit.getServer().getPluginManager().callEvent(playerLobbyNickEvent);
            return;
        }
    }
}
