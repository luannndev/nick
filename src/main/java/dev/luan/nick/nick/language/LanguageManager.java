package dev.luan.nick.nick.language;

import dev.luan.core.core.language.LanguageAPI;
import dev.luan.core.core.manager.rank.RankManager;
import dev.luan.core.core.message.ServerMessage;
import dev.luan.core.core.message.enums.CustomColor;
import dev.luan.nick.nick.Nick;

public class LanguageManager {

    public LanguageManager(final LanguageAPI languageAPI) {
        languageAPI.addMessage(LanguageAPI.Language.EN_US, "commands.nick.not-required-rank", Nick.getInstance().getPrefix() + "§cYou need at least the §f" + RankManager.Rank.PRIMEPLUS.getIconName() + " §crank to use that command!");
        languageAPI.addMessage(LanguageAPI.Language.EN_US, "commands.nick.not-available-rank", Nick.getInstance().getPrefix() + "§cYou cannot choose this permission group!");
        languageAPI.addMessage(LanguageAPI.Language.EN_US, "commands.nick.available-ranks", " §cAvailable permission groups: %ranks%");
        languageAPI.addMessage(LanguageAPI.Language.EN_US, "commands.nick.nick-length", Nick.getInstance().getPrefix() + "§cThe nick must have a maximum of 16 characters!");
        languageAPI.addMessage(LanguageAPI.Language.EN_US, "commands.nick.autonick-on", "§7You will be nicked as §f%nick_display%");
        languageAPI.addMessage(LanguageAPI.Language.EN_US, "commands.nick.autonick-off", ServerMessage.getColor("You will no longer be nicked!", CustomColor.RED));
        languageAPI.addMessage(LanguageAPI.Language.EN_US, "commands.nick.nick-set", " \n" + Nick.getInstance().getPrefix() + "§7You have been assigned the following Nick§8: %nickname%");
        languageAPI.addMessage(LanguageAPI.Language.EN_US, "commands.nick.nick-set-rank", Nick.getInstance().getPrefix() + "§7You are displayed as §f%rank%§7.");
        languageAPI.addMessage(LanguageAPI.Language.EN_US, "commands.nick.nick-remove", Nick.getInstance().getPrefix() + "§cYour nick has been removed!");

        languageAPI.addMessage(LanguageAPI.Language.DE_DE, "commands.nick.not-required-rank", Nick.getInstance().getPrefix() + "§cDu benötigst mindestens den §f" + RankManager.Rank.PRIMEPLUS.getIconName() + " §cRang um diesen Befehl nutzen zu können!");
        languageAPI.addMessage(LanguageAPI.Language.DE_DE, "commands.nick.not-available-rank", Nick.getInstance().getPrefix() + "§cDu kannst diese Permission-Gruppe nicht auswählen!");
        languageAPI.addMessage(LanguageAPI.Language.DE_DE, "commands.nick.available-ranks", " §cVerfügbare Permission-Gruppen: %ranks%");
        languageAPI.addMessage(LanguageAPI.Language.DE_DE, "commands.nick.nick-length", Nick.getInstance().getPrefix() + "§cDein Nick darf maximal 16 Zeichen haben!");
        languageAPI.addMessage(LanguageAPI.Language.DE_DE, "commands.nick.autonick-on", "§7Du wirst nun genickt als §f%nick_display%");
        languageAPI.addMessage(LanguageAPI.Language.DE_DE, "commands.nick.autonick-off", ServerMessage.getColor("Du wirst nicht mehr genickt!", CustomColor.RED));
        languageAPI.addMessage(LanguageAPI.Language.DE_DE, "commands.nick.nick-set", " \n" + Nick.getInstance().getPrefix() + "§7Dir wurde folgender Nick zugewiesen§8: %nickname%");
        languageAPI.addMessage(LanguageAPI.Language.DE_DE, "commands.nick.nick-set-rank", Nick.getInstance().getPrefix() + "§7Du wirst als §f%rank% §7angezeigt.");
        languageAPI.addMessage(LanguageAPI.Language.DE_DE, "commands.nick.nick-remove", Nick.getInstance().getPrefix() + "§cDein Nick wurde entfernt!");
    }
}
