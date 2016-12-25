package ru.spbau.mit;

import ru.spbau.mit.game_objects.*;
import ru.spbau.mit.strategies.BotRandomStrategy;
import ru.spbau.mit.strategies.GuardStrategy;
import ru.spbau.mit.strategies.HumanStrategy;
import ru.spbau.mit.view.RoguelikeUI;
import ru.spbau.mit.view.UIMain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The Controller class is starting class of game,
 * it loads map, initializes it with additional random bots and artifacts
 * and starts game by calling start method of Game object
 */
public class Controller {
    private static Logger logger = Logger.getLogger(Controller.class.getName());

    private static final int DEFAULT_CNT_BOTS = 100;
    private static final int DEFAULT_CNT_ARTIFACTS = 200;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 30;
    private static final String DEFAULT_MAP_PREFIX = "maps/map_";

    private RoguelikeUI uiMain;
    private int cntBots;
    private int cntArtifacts;
    private GameMap gameMap;
    private Player player;
    private List<Player> bots;
    private String mapFileName;
    private boolean isMapFromArgument = false;
    private int currentMap = 0;

    /**
     * This static method starts application
     */
    public static void main(String[] args) throws IOException {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
        } catch (IOException exception) {
            System.err.println("Could not setup logger configuration: " + exception.toString());
        }
        new Controller(args).start();
    }

    /**
     * This constructor initializes map name with given argument,
     * or with default first map, then when you win mup wil change to next
     */
    public Controller(String[] args) {
        if (args.length > 0) {
            isMapFromArgument = true;
            mapFileName = args[0];
        }
    }

    /**
     * This method sets up UI class and run games in infinite cycle
     */
    private void start() {
        uiMain = new UIMain();
        while (true) {
            logger.log(Level.INFO, "new game started");
            if (!isMapFromArgument) {
                mapFileName = DEFAULT_MAP_PREFIX + currentMap + ".txt";
            }
            initializeMap();
            Game game = new Game(uiMain, gameMap, player, bots);
            game.start();
            uiMain.waitUntilRestartPressed();
            if (game.getIsHumanWin()) {
                currentMap++;
            }
        }
    }

    /**
     * This method initializes map from file
     * or with default values if file is invalid
     */
    private void initializeMap() {
        bots = new ArrayList<>();
        try {
            loadMap(mapFileName);
        } catch (IOException exception) {
            logger.log(Level.WARNING, "can't load map", exception);
            gameMap = new GameMap(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            cntBots = DEFAULT_CNT_BOTS;
            cntArtifacts = DEFAULT_CNT_ARTIFACTS;
        }

        player = new Player(new HumanStrategy(this),
                new Characteristics(10, 0, 100), gameMap.getRandomFreePosition());
        gameMap.setObject(player.getPosition(), player);
        for (int i = 0; i < cntBots; i++) {
            Player bot = new Player(new BotRandomStrategy(),
                    new Characteristics(10, 0, 100), gameMap.getRandomFreePosition());
            bots.add(bot);
            gameMap.setObject(bot.getPosition(), bot);
        }

        Random random = new Random();
        for (int i = 0; i < cntArtifacts; i++) {
            int artifactType = random.nextInt(3);
            Artifact artifact;
            switch (artifactType) {
                case 0:
                    artifact = new Artifact(
                            new Characteristics(random.nextInt(10) + 1, 0, 0), ArtifactType.SWORD);
                    break;
                case 1:
                    artifact = new Artifact(
                            new Characteristics(0, random.nextInt(10) + 1, 0), ArtifactType.ARMOR);
                    break;
                default:
                    artifact = new Artifact(
                            new Characteristics(0, 0, random.nextInt(40) + 1), ArtifactType.MEDICINE);
            }
            gameMap.setObject(gameMap.getRandomFreePosition(), artifact);
        }
    }

    /**
     * This method gets user keyboard actions from ui,
     * updates players inventory while inventory keys pressed
     * and returns direction when it pressed
     */
    public Direction updateInventoryAndGetDirection() {
        while (true) {
            char c = uiMain.getPressedKey();
            switch (c) {
                case 'a':
                    return Direction.LEFT;
                case 's':
                    return Direction.DOWN;
                case 'd':
                    return Direction.RIGHT;
                case 'w':
                    return Direction.UP;
                default:
                    player.getInventory().changeArtifactState(c - '0');
                    uiMain.draw(gameMap, player);
            }
        }
    }

    /**
     * This method loads map from file
     */
    private void loadMap(String fileName) throws IOException {
        Scanner in = new Scanner(new File(fileName));
        int width = in.nextInt();
        int height = in.nextInt();
        cntBots = in.nextInt();
        cntArtifacts = in.nextInt();
        in.nextLine();
        gameMap = new GameMap(width, height);
        for (int i = 0; i < height; i++) {
            String line = in.nextLine();
            if (line.length() != width) {
                throw new IOException("Wrong input");
            }
            for (int j = 0; j < width; j++) {
                char cellType = line.charAt(j);
                Position position = new Position(i, j);
                switch (cellType) {
                    case '#':
                        gameMap.setObject(position, new Wall());
                        break;
                    case 'A':
                        gameMap.setObject(position,
                                new Artifact(new Characteristics(20, 20, 100), ArtifactType.GENERAL));
                        break;
                    case 'S':
                        gameMap.setObject(position,
                                new Artifact(new Characteristics(10, 0, 0), ArtifactType.SWORD));
                        break;
                    case 'D':
                        gameMap.setObject(position,
                                new Artifact(new Characteristics(0, 10, 0), ArtifactType.ARMOR));
                        break;
                    case '+':
                        gameMap.setObject(position,
                                new Artifact(new Characteristics(0, 0, 40), ArtifactType.MEDICINE));
                        break;
                    case 'B':
                        Player bot = new Player(new BotRandomStrategy(), new Characteristics(15, 0, 100), position);
                        bots.add(bot);
                        gameMap.setObject(position, bot);
                        break;
                    case 'G':
                        bot = new Player(new GuardStrategy(), new Characteristics(15, 0, 100), position);
                        bots.add(bot);
                        gameMap.setObject(position, bot);
                        break;
                    default:
                        gameMap.setObject(position, new EmptyCell());
                        break;
                }
            }
        }
    }
}
