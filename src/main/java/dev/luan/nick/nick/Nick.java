package dev.luan.nick.nick;


import dev.luan.core.core.database.DatabaseManager;
import dev.luan.core.core.getter.RankAPI;
import dev.luan.core.core.language.LanguageAPI;
import dev.luan.core.core.manager.PlayerManager;
import dev.luan.core.core.manager.rank.RankManager;
import dev.luan.core.core.message.ServerMessage;
import dev.luan.core.core.message.enums.CustomColor;
import dev.luan.nick.nick.language.LanguageManager;
import lombok.Getter;
import lombok.Setter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public final class Nick {

    @Getter private static Nick instance;

    private final String prefix = ServerMessage.getColor("Nick", CustomColor.GRADIENT_PURPLE) + " §8| §7";
    private final RankManager.Rank nickableRank = RankManager.Rank.PRIMEPLUS;

    private final DatabaseManager databaseManager;
    private final String databaseTable;

    private final HashMap<UUID, PlayerNickProfile> playerNickProfileDatas;
    private final List<String> nicknameList;

    public Nick(final DatabaseManager databaseManager, final String databaseTable) {
        instance = this;

        new LanguageManager(LanguageAPI.getInstance());

        this.databaseManager = databaseManager;
        this.databaseTable = databaseTable;
        this.databaseManager.createTable("CREATE TABLE IF NOT EXISTS `" + databaseTable + "` (\n" +
                " `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
                " `uuid` char(255) NOT NULL,\n" +
                " `player_name` char(255) NOT NULL,\n" +
                " `permission_group` varchar(255) NOT NULL,\n" +
                " `nickname` varchar(255) NOT NULL,\n" +
                " `nicked` boolean NOT NULL,\n" +
                " `skindata` text NOT NULL" +
                ") ", databaseTable);

        this.playerNickProfileDatas = new HashMap<>();
        this.nicknameList = Arrays.asList("Rirosiio", "Masiixa", "TommyAWBR", "Slimer2", "7Gaped", "Annnaaa_", "LbGame", "mistics", "Tvmo", "zBxn", "cursedAurelia", "Phil_jona11", "PaintWithS", "Simorou", "Munoha", "aintitnice", "ServerRam", "Arvedus", "bellaxyn", "PRORPx38", "Kuno13", "IdwIx", "ImTxm", "Juliii_i", "SmallT3ddy", "Lbminecraft", "lazie", "lerui", "Marpiipii", "Nariico", "OG_kashiko", "P0xie_", "Proxxi", "qetkx", "Rosaria01", "stellare", "Timox2005", "Umska", "Xahliia", "Wawaa", "foqunar", "kkinqs", "iiive", "ph1ii1p", "Ton23", "Maii_san_", "Adlaz", "frogg0o0", "_Panxi_", "FluksusYT", "DownieRaudie", "AngrxJoshi", "LiFt14", "sus_banana", "smartieknh", "Luclud_LP", "Jonipoponi", "KaleYouKnow_", "SeemsNahLeqit", "dalek_pi", "HackyBlocky", "TM_GamerKing", "VeteranoKid", "meliodasglp", "Salty069", "taxmax11yt", "juppi02", "Leibham", "Skywars0G", "WPKID_", "mBlitz08", "memeweb", "SheLovesHim", "Strife_SSJ", "fts_error", "goforgold77", "stpanb3n", "MC_Frites", "ntkl", "Liho2k", "IndustryBabe", "PaintedClayPot", "Expurger", "cedi2711", "DxnAmigo", "UninstallGame", "SlowerGerd2", "Awsomesus", "qLauraa", "marcs1", "Itsfede19_", "vvice", "SxrahMarie", "jakkobb", "022Jonas", "NoneDone", "zIFabi", "Myusa", "Piccollina", "Heldni", "_miaQT", "1ooni", "notkeph", "Ibadlo", "yM4rio", "alphaRabbid", "Jeaaa", "IanMrnk", "eckl_steve", "Pauni2211", "yIsaa", "jyliaa", "underaated", "lme22", "OGsixfive", "insertname221", "hlfger", "3oven", "ManchasSaber", "9YouEXlovesMe", "Sureshkumar", "Lucasmic3r", "414kaji", "Lucky_Lukas", "Emmi26", "SlientLobby", "Aokeri", "BigBallsyusuf", "Emely911", "Joshy_froschi", "RamboRamon", "Lyncon540", "AuroraSpirit_", "39CrafterJoe", "Melif0x", "M0ddedAccount", "yaZWER", "Noutor", "Ant1ru", "spookyx_x", "Deeqay6", "merteweew", "Muichiro_", "Lamonos1", "firepvp", "kloompy", "misbe1337", "LeoT", "sweeah", "BubbleAla", "Endermity", "tweepBBC", "johnny8976boi", "EMIMAKESBOBA", "JKlmnop5", "LinseyKrumel", "DannySimpijn", "OvenDucky", "1zixty", "lepiplin", "JoshyTheDragon", "GodCrownClown", "EvilSkelle", "_SilverGoose", "MilitarySloth", "Yolokwk13", "MoneyMakinDon", "BackBonedWord34", "Dorm_in_santuri", "dayzhur", "cronos56", "DeathSmilie", "Pregn4ntParrot", "Arfkek1", "Sh1rq_", "Lunaaarism", "tinybabycrocs", "Asyyr");
    }

    public PlayerNickProfile getPlayerNickProfile(final UUID uuid) {
        return this.playerNickProfileDatas.get(uuid);
    }

    public void setPlayerNickProfile(final UUID uuid, final String player_name, String nickname, final RankManager.Rank rank, final String[] skinData) {
        if(!RankAPI.getInstance().getPlayerRank(uuid).isHigherEqualsLevel(Nick.instance.getNickableRank())) return;
        if(nickname.isEmpty()) {
            nickname = this.getRandomNickname();
        }

        final PlayerNickProfile playerNickProfile = new PlayerNickProfile(uuid, player_name, nickname, rank, skinData);
        this.playerNickProfileDatas.put(uuid, playerNickProfile);
    }

    public void removePlayerNickProfile(final UUID uuid) {
        if(this.playerNickProfileDatas.containsKey(uuid)) this.playerNickProfileDatas.remove(uuid);
    }

    public boolean isNicked(final UUID uuid) {
        if(!this.isInList(uuid)) return false;
        return this.getPlayerNickProfile(uuid) != null || this.getBoolean(uuid, "nicked");
    }

    public String getNickname(final UUID uuid) {
        final PlayerNickProfile playerNickProfile = this.getPlayerNickProfile(uuid);
        if(playerNickProfile != null) {
            return playerNickProfile.getNickname();
        }
        return this.getString(uuid, "nickname");
    }

    public RankManager.Rank getRank(final UUID uuid) {
        final PlayerNickProfile playerNickProfile = this.getPlayerNickProfile(uuid);
        if(playerNickProfile != null) {
            return playerNickProfile.getRank();
        }
        return RankManager.Rank.fromString(this.getString(uuid, "permission_group"));
    }

    public String getRandomNickname() {
        Collections.shuffle(this.nicknameList);
        for(final String nick_name : this.nicknameList) {
            if(this.getUsedNicknames().contains(nick_name)) continue;
            return nick_name;
        }
        return null;
    }

    public List<String> getUsedNicknames() {
        final List<String> names = new ArrayList<>();
        for (final PlayerNickProfile playerNickProfile : this.playerNickProfileDatas.values()) {
            names.add(playerNickProfile.getNickname());
        }
        return Collections.unmodifiableList(names);
    }

    public String[] getRandomSkinData() {
        final List<String[]> skinDatas = PlayerManager.getInstance().getSkinDatas().values().stream().collect(Collectors.toList());
        Collections.shuffle(skinDatas);
        final String[] value = skinDatas.get(0);
        return new String[]{value[0], value[1]};
    }

    public boolean isInList(final UUID uuid) {
        try {
            final PreparedStatement ps = this.databaseManager.getConnection().prepareStatement("SELECT EXISTS (SELECT 1 FROM " + this.databaseTable + " WHERE uuid=?)");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean inList = rs.getBoolean(1);
                rs.close();
                ps.close();
                return inList;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            this.databaseManager.disconnect();
            this.databaseManager.connect();
            this.isInList(uuid);
        }
        return false;
    }

    public void insertPlayer(final UUID uuid, final String player_name) {
        if(isInList(uuid)) return;
        try {
            if(isInList(uuid)) return;
            PreparedStatement ps = this.databaseManager.getConnection().prepareStatement("INSERT INTO " + this.databaseTable + " (uuid,player_name,permission_group,nickname,nicked,skindata) VALUE (?,?,?,?,?,?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, player_name);
            ps.setString(3, RankManager.Rank.DEFAULT.toString());
            ps.setString(4, "");
            ps.setBoolean(5, false);
            ps.setString(6, "");
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            this.databaseManager.disconnect();
            this.databaseManager.connect();
            this.insertPlayer(uuid, player_name);
        }
    }

    public String getString(final UUID uuid, final String key) {
        try {
            try {
                PreparedStatement ps = this.databaseManager.getConnection().prepareStatement("SELECT " + key + " FROM " + this.databaseTable + " WHERE uuid=?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                String returnValue = null;
                if (rs.next()) {
                    returnValue = rs.getString(1);
                }
                rs.close();
                ps.close();
                return returnValue;
            } catch (SQLException e) {
                this.databaseManager.disconnect();
                this.databaseManager.connect();
                return getString(uuid, key);
            }
        } catch (Exception e) {
            this.databaseManager.disconnect();
            this.databaseManager.connect();
            return getString(uuid, key);
        }
    }

    public boolean getBoolean(final UUID uuid, final String key) {
        try {
            try {
                PreparedStatement ps = this.databaseManager.getConnection().prepareStatement("SELECT " + key + " FROM " + this.databaseTable + " WHERE uuid=?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                boolean returnValue = false;
                if (rs.next()) {
                    returnValue = rs.getBoolean(1);
                }
                rs.close();
                ps.close();
                return returnValue;
            } catch (SQLException e) {
                this.databaseManager.disconnect();
                this.databaseManager.connect();
                return getBoolean(uuid, key);
            }
        } catch (Exception e) {
            this.databaseManager.disconnect();
            this.databaseManager.connect();
            return getBoolean(uuid, key);
        }
    }

    public void setStringValue(final UUID uuid, final String player_name, final String key, String value) {
        if (!isInList(uuid)) {
            this.insertPlayer(uuid, player_name);
            return;
        }
        try {
            PreparedStatement ps = this.databaseManager.getConnection().prepareStatement("UPDATE " + this.databaseTable + " SET " + key + "=?, player_name=? WHERE uuid=?");
            ps.setString(1, value);
            ps.setString(2, player_name);
            ps.setString(3, uuid.toString());
            ps.execute();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement ps = this.databaseManager.getConnection().prepareStatement("UPDATE " + this.databaseTable + " SET player_name=? WHERE uuid=?");
            ps.setString(1, player_name);
            ps.setString(2, uuid.toString());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBooleanValue(final UUID uuid, final String player_name, final String key, final boolean value) {
        if (!isInList(uuid)) {
            this.insertPlayer(uuid, player_name);
            return;
        }
        try {
            PreparedStatement ps = this.databaseManager.getConnection().prepareStatement("UPDATE " + this.databaseTable + " SET " + key + "=?, player_name=? WHERE uuid=?");
            ps.setBoolean(1, value);
            ps.setString(2, player_name);
            ps.setString(3, uuid.toString());
            ps.execute();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement ps = this.databaseManager.getConnection().prepareStatement("UPDATE " + this.databaseTable + " SET player_name=? WHERE uuid=?");
            ps.setString(1, player_name);
            ps.setString(2, uuid.toString());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Getter @Setter
    public static class PlayerNickProfile {

        private final UUID uniqueId;
        private String name, nickname;
        private RankManager.Rank rank;
        private String[] skinData;
        private int level = new Random().nextInt(5);

        public PlayerNickProfile(final UUID uuid, final String player_name, final String nickname, final RankManager.Rank rank, final String[] skinData) {
            this.uniqueId = uuid;
            this.name = player_name;
            this.rank = rank;
            this.nickname = nickname;
            this.skinData = skinData;
            if(this.level <= 0) {
                this.level = 1;
            }
        }
    }
}
