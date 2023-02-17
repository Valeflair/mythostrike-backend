package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

public abstract class ArmorCard extends Card {


    protected ArmorCard(int id, String name, String description, CardSymbol symbol, int point) {
        super(id, name, description, CardType.ARMOR,  symbol, point);
    }

    @Override
    public void activate() {
        Player player = cardUseHandle.getPlayer();
        gameManager = cardUseHandle.getGameManager();

        //remove old armor
        Card armorFirst = null;
        for (Card card : player.getEquipment().getCards()) {
            if (card.getType().equals(CardType.ARMOR)) {
                armorFirst =  card;
                break;
            }
        }

        if (armorFirst != null) {
            CardMoveHandle throwWeapon = new CardMoveHandle(gameManager, "throw weapon"
                    , player, null, player.getEquipment(),
                    gameManager.getGame().getTablePile(), List.of(armorFirst));
            gameManager.getCardManager().moveCard(throwWeapon);
            gameManager.getPlayerManager().removeSkillFromPlayer(player, armorFirst.getPassiveSkill());
            gameManager.getPlayerManager().removeSkillFromPlayer(player, armorFirst.getActiveSkill());
        }

        //equip new armor
        CardMoveHandle equipArmor = new CardMoveHandle(gameManager, "equip armor"
                , player, player, player.getHandCards(),
                player.getEquipment(), List.of(this));
        gameManager.getCardManager().moveCard(equipArmor);
        gameManager.getPlayerManager().addSkillToPlayer(player, getPassiveSkill());
        gameManager.getPlayerManager().addSkillToPlayer(player, getActiveSkill());
    }
}

