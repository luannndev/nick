package dev.luan.nick.bungeecord.commands;

import dev.luan.core.Core;
import dev.luan.core.core.getter.RankAPI;
import dev.luan.core.core.manager.rank.RankManager;
import dev.luan.core.core.message.ServerMessage;
import dev.luan.core.core.pluginmessage.PluginMessageManager;
import dev.luan.nick.nick.Nick;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class NickCommand extends Command {

    public NickCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) return;
        final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
        final RankManager.Rank playerRank = RankAPI.getInstance().getPlayerRank(proxiedPlayer.getUniqueId());
        if(!playerRank.isHigherEqualsLevel(Nick.getInstance().getNickableRank())) {
            sender.sendMessage(ServerMessage.getMessage(proxiedPlayer.getUniqueId(), "commands.nick.not-required-rank"));
            return;
        }

        RankManager.Rank nickRank = RankManager.Rank.DEFAULT;
        String nickname = "";
        if(args.length >= 1 && args.length <= 2) {
            if(args.length >= 1) {
                nickRank = RankManager.Rank.fromString(args[0]);
                if(nickRank == null || nickRank.isHigherLevel(playerRank)) {
                    final List<String> permissionGroupList = new ArrayList<>();
                    for(final RankManager.Rank rank : RankManager.Rank.values()) {
                        if(rank.getPermissionsLevel() > playerRank.getPermissionsLevel()) continue;
                        permissionGroupList.add(rank.getColor(rank.getName()));
                    }
                    int current = 1;
                    final StringBuilder stringBuilder = new StringBuilder();
                    for(final String permissionGroup : permissionGroupList) {
                        if(current == permissionGroupList.size()) {
                            stringBuilder.append(permissionGroup);
                        } else {
                            stringBuilder.append(permissionGroup).append("§8, ");
                        }
                        ++current;
                    }
                    sender.sendMessage(ServerMessage.getMessage(proxiedPlayer.getUniqueId(), "commands.nick.not-available-rank"));
                    sender.sendMessage(ServerMessage.getMessage(proxiedPlayer.getUniqueId(), "commands.nick.available-ranks").replaceAll("%ranks%", stringBuilder.substring(0)));
                    return;
                }
            }
            if(args.length == 2 && playerRank.isHigherEqualsLevel(RankManager.Rank.ADMIN)) {
                nickname = args[1];
                if(nickname.length() > 16) {
                    sender.sendMessage(ServerMessage.getMessage(proxiedPlayer.getUniqueId(), "commands.nick.nick-length"));
                    return;
                }
            }
        }

        final ICloudPlayer cloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(proxiedPlayer.getUniqueId());
        if(cloudPlayer.getConnectedServer().isLobby()) {
            if(Nick.getInstance().isNicked(proxiedPlayer.getUniqueId())) {
                Nick.getInstance().setBooleanValue(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), "nicked", false);
                Nick.getInstance().setStringValue(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), "nickname", "");
                Core.getInstance().getPluginMessageManager().sendData(proxiedPlayer.getServer().getInfo().getName(), PluginMessageManager.PluginMessageType.LOBBY_PLAYER_NICK,
                        new String[] {
                                proxiedPlayer.getUniqueId().toString(),
                                "UNNICK"
                        });
                Nick.getInstance().removePlayerNickProfile(proxiedPlayer.getUniqueId());
                return;
            }
            Nick.getInstance().setBooleanValue(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), "nicked", true);
            Nick.getInstance().setStringValue(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), "permission_group", nickRank.toString());
            Nick.getInstance().setStringValue(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), "nickname", nickname);
            Core.getInstance().getPluginMessageManager().sendData(proxiedPlayer.getServer().getInfo().getName(), PluginMessageManager.PluginMessageType.LOBBY_PLAYER_NICK,
                    new String[] {
                            proxiedPlayer.getUniqueId().toString(),
                            "NICK",
                            nickname,
                            nickRank.toString()
                    });
            return;
        }

        final Nick.PlayerNickProfile playerNickProfile = Nick.getInstance().getPlayerNickProfile(proxiedPlayer.getUniqueId());
        if(playerNickProfile != null) {
            Nick.getInstance().removePlayerNickProfile(proxiedPlayer.getUniqueId());
            Core.getInstance().getPluginMessageManager().sendData(proxiedPlayer.getServer().getInfo().getName(), PluginMessageManager.PluginMessageType.PLAYER_NICK,
                    new String[] {
                            proxiedPlayer.getUniqueId().toString(),
                            "UNNICK"
                    });
            return;
        }

        final String[] skinData = Nick.getInstance().getRandomSkinData();
        Nick.getInstance().setPlayerNickProfile(proxiedPlayer.getUniqueId(), playerRank.getName(), nickname, nickRank, skinData);

        Core.getInstance().getPluginMessageManager().sendData(proxiedPlayer.getServer().getInfo().getName(), PluginMessageManager.PluginMessageType.PLAYER_NICK,
                new String[] {
                        proxiedPlayer.getUniqueId().toString(),
                        "NICK",
                        nickname,
                        nickRank.toString(),
                        skinData[0] + ";" + skinData[1]

                });
    }
}
