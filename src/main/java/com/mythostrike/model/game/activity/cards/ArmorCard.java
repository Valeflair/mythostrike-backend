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
        Optional<Card> armorFirst = player.getEquipment().getCards().stream()
                .filter(card -> card.getType().equals(CardType.ARMOR)).findFirst();
        //remove old armor
        if (armorFirst.isPresent()) {
            Card armor = armorFirst.get();
            CardMoveHandle throwArmor = new CardMoveHandle(gameManager, "throw armor"
                    , player, null, player.getEquipment(),
                    gameManager.getGame().getTablePile(), List.of(armor));
            gameManager.getCardManager().moveCard(throwArmor);
            gameManager.getPlayerManager().removeSkillFromPlayer(player, armor.getPassiveSkill());
            gameManager.getPlayerManager().removeSkillFromPlayer(player, armor.getActiveSkill());
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

