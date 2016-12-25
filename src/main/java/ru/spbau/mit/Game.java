package ru.spbau.mit;

import ru.spbau.mit.game_objects.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Game class is class for running one game
 */
public class Game {
    private UIMain uiMain;
    private GameMap gameMap;
    private Player player;
    private List<Player> bots;
    private boolean isHumanWin = false;

    /**
     * This constructor initializes all fields needed for running game
     */
    public Game(UIMain uiMain, GameMap gameMap, Player player, List<Player> bots) {
        this.uiMain = uiMain;
        this.gameMap = gameMap;
        this.player = player;
        this.bots = bots;
    }

    /**
     * This method runs game,
     * game ends when player is killed or when all bots are killed
     */
    public void start() {
        while (true) {
            uiMain.draw(gameMap, player);
            player.makeMove(gameMap);
            bots = bots.stream().filter(Player::isAlive).collect(Collectors.toList());
            for (Player bot : bots) {
                bot.makeMove(gameMap);
            }
            if (bots.size() == 0) {
                isHumanWin = true;
                uiMain.drawWin();
                break;
            }
            if (!player.isAlive()) {
                uiMain.drawLoose();
                break;
            }
        }
    }

    public boolean getIsHumanWin() {
        return isHumanWin;
    }
}
