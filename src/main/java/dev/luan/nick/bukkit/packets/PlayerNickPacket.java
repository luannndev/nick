package dev.luan.nick.bukkit.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.luan.core.Core;
import dev.luan.core.core.getter.RankAPI;
import dev.luan.core.core.manager.rank.RankManager;
import dev.luan.nick.nick.Nick;
import dev.luan.nick.bukkit.BukkitNick;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerNickPacket extends PacketAdapter {

    private final BukkitNick plugin;

    public PlayerNickPacket(final BukkitNick plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);
        this.plugin = plugin;
    }

    public boolean isNewTablist() {
        return ProtocolLibrary.getProtocolManager().getMinecraftVersion().isAtLeast(MinecraftVersion.fromServerVersion("4081-Spigot-b754dcc-e8f9149 (MC: 1.20.4)"));
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if(Core.getInstance().isLobbyServer()) return;
        final PacketContainer packetContainer = event.getPacket();
        if(this.isNewTablist()) {
            Set<EnumWrappers.PlayerInfoAction> action = packetContainer.getPlayerInfoActions().read(0);
            if(!action.contains(EnumWrappers.PlayerInfoAction.ADD_PLAYER)) {
                return;
            }
        }

        final Player packetSenderPlayer = event.getPlayer();
        final RankManager.Rank forPlayerPermissionGroup = RankAPI.getInstance().getPlayerRank(packetSenderPlayer.getUniqueId());
        final List<PlayerInfoData> playerInfoDataList = packetContainer.getPlayerInfoDataLists().read(this.isNewTablist() ? 1 : 0); // sent data;
        final List<PlayerInfoData> newPlayerInfoDataList = new ArrayList<>(); // new data

        for (PlayerInfoData playerInfoData : playerInfoDataList) {
            final Player addPlayer = Bukkit.getServer().getPlayer(playerInfoData.getProfile().getName());
            if (addPlayer == null) {
                final Player realPlayer = Bukkit.getPlayer(playerInfoData.getProfileId());
                if(realPlayer == null) {
                    newPlayerInfoDataList.add(playerInfoData);
                    continue;
                }
                final RankManager.Rank realPlayerPermissionGroup = RankAPI.getInstance().getPlayerRank(realPlayer.getUniqueId());
                final PlayerInfoData playerInfoData1 = new PlayerInfoData(
                        WrappedGameProfile.fromPlayer(realPlayer),
                        playerInfoData.getPing(),
                        EnumWrappers.NativeGameMode.fromBukkit(realPlayer.getGameMode()),
                        playerInfoData.getDisplayName());
                //WrappedChatComponent.fromText(realPlayerPermissionGroup.getPrefix() + realPlayerPermissionGroup.getColor() + realPlayer.getName()));
                newPlayerInfoDataList.add(playerInfoData1);
                continue;
            }

            if(packetSenderPlayer.getName().equalsIgnoreCase(addPlayer.getName())) {
                newPlayerInfoDataList.add(playerInfoData);
                continue;
            }

            final RankManager.Rank addPlayerPermissionGroup = RankAPI.getInstance().getPlayerRank(addPlayer.getUniqueId());
            final Nick.PlayerNickProfile playerNickProfile = Nick.getInstance().getPlayerNickProfile(addPlayer.getUniqueId());
            if (playerNickProfile != null) {
                if(playerNickProfile.getNickname().equalsIgnoreCase(addPlayer.getName())) {
                    newPlayerInfoDataList.add(playerInfoData);
                    continue;
                }
                if (!(forPlayerPermissionGroup.getPermissionsLevel() >= addPlayerPermissionGroup.getPermissionsLevel())) {
                    WrappedGameProfile wrappedGameProfile = playerInfoData.getProfile();
                    wrappedGameProfile = wrappedGameProfile.withName(playerNickProfile.getNickname());
                    if (wrappedGameProfile.getProperties().containsKey("textures")) {
                        wrappedGameProfile.getProperties().removeAll("textures");
                    }
                    wrappedGameProfile.getProperties().put("textures", new WrappedSignedProperty("textures", playerNickProfile.getSkinData()[0], playerNickProfile.getSkinData()[1]));
                    ((GameProfile) wrappedGameProfile.getHandle()).getProperties().removeAll("textures");
                    ((GameProfile) wrappedGameProfile.getHandle()).getProperties().put("textures", new Property("textures", playerNickProfile.getSkinData()[0], playerNickProfile.getSkinData()[1]));
                    try {
                        Field field = playerInfoData.getClass().getDeclaredField("profile");
                        field.setAccessible(true);
                        field.set(playerInfoData, wrappedGameProfile);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    PlayerInfoData newPlayerInfoData = new PlayerInfoData(
                            wrappedGameProfile,
                            playerInfoData.getPing(),
                            playerInfoData.getGameMode(),
                            playerInfoData.getDisplayName());
                    //WrappedChatComponent.fromText(playerNickProfile.getPermissionGroup().getPrefix() + playerNickProfile.getPermissionGroup().getColor() +  playerNickProfile.getNickname()));
                    newPlayerInfoDataList.add(newPlayerInfoData);
                } else {
                    newPlayerInfoDataList.add(playerInfoData);
                }
            } else {
                newPlayerInfoDataList.add(playerInfoData);
            }
        }

        packetContainer.getPlayerInfoDataLists().write(this.isNewTablist() ? 1 : 0, newPlayerInfoDataList);
        event.setPacket(event.getPacket());
    }
}
