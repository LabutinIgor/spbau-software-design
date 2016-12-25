package ru.spbau.mit;


import org.junit.Test;
import ru.spbau.mit.game_objects.Player;
import ru.spbau.mit.strategies.GuardStrategy;
import ru.spbau.mit.strategies.Strategy;
import ru.spbau.mit.view.RoguelikeUI;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameTest {
    private RoguelikeUI uiMock = new RoguelikeUI() {
        @Override
        public void draw(GameMap gameMap, Player player) {

        }

        @Override
        public void drawWin() {

        }

        @Override
        public void drawLoose() {

        }

        @Override
        public char getPressedKey() {
            return 0;
        }

        @Override
        public void waitUntilRestartPressed() {

        }
    };

    private Strategy humanStrategyMock = new Strategy() {
        @Override
        public Direction getMoveDirection(Characteristics characteristics, Inventory inventory,
                                          Position position, GameMap gameMap) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return Direction.DOWN;
        }

        @Override
        public char getSymbol() {
            return '@';
        }
    };

    @Test
    public void testGameHumanWin() {
        GameMap gameMap = new GameMap(10, 10);
        Position playerPosition = new Position(1, 1);
        Player player = new Player(humanStrategyMock, new Characteristics(1, 0, 2), playerPosition);
        List<Player> bots = new ArrayList<>();
        gameMap.setObject(playerPosition, player);
        Position botPosition = new Position(2, 1);
        bots.add(new Player(new GuardStrategy(), new Characteristics(1, 0, 1), botPosition));
        gameMap.setObject(botPosition, bots.get(0));
        Game game = new Game(uiMock, gameMap, player, bots);

        assertFalse(game.getIsHumanWin());
        game.start();
        assertTrue(game.getIsHumanWin());
    }

    @Test
    public void testGameHumanLoose() {
        GameMap gameMap = new GameMap(10, 10);
        Position playerPosition = new Position(1, 1);
        Player player = new Player(humanStrategyMock, new Characteristics(1, 0, 1), playerPosition);
        List<Player> bots = new ArrayList<>();
        gameMap.setObject(playerPosition, player);
        Position botPosition = new Position(2, 1);
        bots.add(new Player(new GuardStrategy(), new Characteristics(1, 0, 2), botPosition));
        gameMap.setObject(botPosition, bots.get(0));
        Game game = new Game(uiMock, gameMap, player, bots);

        assertFalse(game.getIsHumanWin());
        game.start();
        assertFalse(game.getIsHumanWin());
    }
}
