package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

public abstract class WeaponCard extends Card {


    protected WeaponCard(int id, String name, String description, CardSymbol symbol, int point) {
        super(id, name, description, CardType.WEAPON,  symbol, point);
    }

    @Override
    public void activate() {
        Player player = cardUseHandle.getPlayer();
        gameManager = cardUseHandle.getGameManager();


        //remove old weapon
        Card weaponFirst = null;
        for (Card card : player.getEquipment().getCards()) {
            if (card.getType().equals(CardType.WEAPON)) {
                weaponFirst =  card;
                break;
            }
        }

        if (weaponFirst != null) {
            CardMoveHandle throwWeapon = new CardMoveHandle(gameManager, "throw weapon"
                    , player, null, player.getEquipment(),
                    gameManager.getGame().getTablePile(), List.of(weaponFirst));
            gameManager.getCardManager().moveCard(throwWeapon);
            gameManager.getPlayerManager().removeSkillFromPlayer(player, weaponFirst.getPassiveSkill());
            gameManager.getPlayerManager().removeSkillFromPlayer(player, weaponFirst.getActiveSkill());
        }

        //equip new weapon
        CardMoveHandle equipWeapon = new CardMoveHandle(gameManager, "equip weapon"
                , player, player, player.getHandCards(),
                player.getEquipment(), List.of(this));
        gameManager.getCardManager().moveCard(equipWeapon);
        gameManager.getPlayerManager().addSkillToPlayer(player, getPassiveSkill());
        gameManager.getPlayerManager().addSkillToPlayer(player, getActiveSkill());


        /*
        Optional<Card> weaponFirst = player.getEquipment().getCards().stream()
                .filter(card -> card.getType().equals(CardType.WEAPON)).findFirst();
        //remove old weapon
        if (weaponFirst.isPresent()) {
            Card weapon = weaponFirst.get();
            CardMoveHandle throwWeapon = new CardMoveHandle(gameManager, "throw weapon"
                , player, null, player.getEquipment(),
                gameManager.getGame().getTablePile(), List.of(weapon));
            gameManager.getCardManager().moveCard(throwWeapon);
            gameManager.getPlayerManager().removeSkillFromPlayer(player, weapon.getPassiveSkill());
            gameManager.getPlayerManager().removeSkillFromPlayer(player, weapon.getActiveSkill());
        }

        //equip new weapon
        CardMoveHandle equipWeapon = new CardMoveHandle(gameManager, "equip weapon"
                , player, player, player.getHandCards(),
                player.getEquipment(), List.of(this));
        gameManager.getCardManager().moveCard(equipWeapon);
        gameManager.getPlayerManager().addSkillToPlayer(player, getPassiveSkill());
        gameManager.getPlayerManager().addSkillToPlayer(player, getActiveSkill());
        */

    }
}

