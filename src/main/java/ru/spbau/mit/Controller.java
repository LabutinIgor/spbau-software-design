package ru.spbau.mit;

import ru.spbau.mit.game_objects.*;
import ru.spbau.mit.strategies.BotRandomStrategy;
import ru.spbau.mit.strategies.GuardStrategy;
import ru.spbau.mit.strategies.HumanStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Controller {
    private static final int DEFAULT_CNT_BOTS = 100;
    private static final int DEFAULT_CNT_ARTIFACTS = 200;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 30;
    private static final String DEFAULT_MAP_FILENAME = "maps/map_00.txt";

    private UIMain uiMain;
    private int cntBots;
    private int cntArtifacts;
    private GameMap gameMap;
    private Player player;
    private List<Player> bots;
    private String mapFileName;

    public static void main(String[] args) throws IOException {
        Controller controller = new Controller(args);
        controller.setUpUI();
        controller.start();
    }

    public Controller(String[] args) {
        if (args.length == 0) {
            mapFileName = DEFAULT_MAP_FILENAME;
        } else {
            mapFileName = args[0];
        }
    }

    private void setUpUI() {
        uiMain = new UIMain();
    }

    public void start() {
        while (true) {
            initializeMap();
            runGame();
            uiMain.waitUntilRestartPressed();
        }
    }

    private void runGame() {
        while (true) {
            uiMain.draw(gameMap, player);
            player.makeMove(gameMap);
            bots = bots.stream().filter(Player::isAlive).collect(Collectors.toList());
            for (int i = 0; i < bots.size(); i++) {
                bots.get(i).makeMove(gameMap);
            }
            if (bots.size() == 0) {
                uiMain.drawWin();
                break;
            }
            if (!player.isAlive()) {
                uiMain.drawLoose();
                break;
            }
        }
    }

    private void initializeMap() {
        bots = new ArrayList<>();
        try {
            loadMap(mapFileName);
        } catch (IOException exception) {
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
                            new Characteristics(random.nextInt(20) + 1, 0, 0), ArtifactType.SWORD);
                    break;
                case 1:
                    artifact = new Artifact(
                            new Characteristics(0, random.nextInt(20) + 1, 0), ArtifactType.ARMOR);
                    break;
                default:
                    artifact = new Artifact(
                            new Characteristics(0, 0, random.nextInt(20) + 1), ArtifactType.MEDICINE);
            }
            gameMap.setObject(gameMap.getRandomFreePosition(), artifact);
        }
    }

    public Direction getDirectionForHuman() {
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
                    case '.':
                        gameMap.setObject(position, new EmptyCell());
                        break;
                    case '#':
                        gameMap.setObject(position, new Wall());
                        break;
                    case 'A':
                        gameMap.setObject(position,
                                new Artifact(new Characteristics(20, 20, 100), ArtifactType.GENERAL));
                        break;
                    case 'G':
                        Player bot = new Player(new GuardStrategy(), new Characteristics(10, 0, 100), position);
                        bots.add(bot);
                        gameMap.setObject(position, bot);
                }
            }
        }
    }
}
