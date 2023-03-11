package com.mythostrike.unit;

import com.mythostrike.account.repository.User;
import com.mythostrike.model.game.player.Human;
import com.mythostrike.model.game.player.PlaceholderBot;
import com.mythostrike.model.game.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {
    private Player player;
    private Human human;

    @BeforeEach
    void setUp() {
        human = new Human(new User("test1", "test"), null);
        player = new PlaceholderBot("test2");
    }


    @Test
    void attributeAssertion() {
        assertEquals(human.getUsername(), "test1");
        assertEquals(player.getUsername(), "test2");
        assertEquals(human.getDrachma(), 0);
        assertEquals(player.getDrachma(), 0);
        assertEquals(human.getRankPoints(), 0);
        assertEquals(player.getRankPoints(), 0);
        assertEquals(human.getUser().getAvatarNumber(), 1);
    }

    @Test
    void healthAssertion() {
        human.setMaxHp(100);
        human.increaseCurrentHp(500);
        assertEquals(human.getCurrentHp(), 100);
        human.decreaseCurrentHp(50);
        assertEquals(human.getCurrentHp(), 50);
        human.decreaseCurrentHp(100);
        assertEquals(human.getCurrentHp(), -50);
        player.setMaxHp(100);
        player.increaseCurrentHp(500);
        assertEquals(player.getCurrentHp(), 100);
        player.setMaxHp(50);
        assertEquals(player.getCurrentHp(), 50);
    }

    @Test
    void equalityAssertion() {
        assertEquals(player, new PlaceholderBot("test2"));
        assertEquals(human, new Human(new User("test1", "test"), null));
        assertNotEquals(player, human);
        assertNotEquals(player, new PlaceholderBot("test3"));
        assertNotEquals(human, new Human(new User("test2", "test"), null));
    }

    @Test
    void restrictionAssertion() {
        player.setPermanentRestrict("test", 0);
        player.resetRestrict();
        assertTrue(player.isRestricted("test"));
        player.setPermanentRestrict("test", 1);
        player.resetRestrict();

        assertFalse(player.isRestricted("test"));
        player.decreaseUseTime("test");
        assertTrue(player.isRestricted("test"));
        player.increaseUseTime("test");
        assertFalse(player.isRestricted("test"));

        assertFalse(player.isImmune("test"));
        player.setTemporaryImmunity("test", true);
        assertTrue(player.isImmune("test"));
        player.setPermanentImmunity("2", true);
        assertFalse(player.isImmune("2"));
        player.resetImmunity();
        assertTrue(player.isImmune("2"));
    }

}
