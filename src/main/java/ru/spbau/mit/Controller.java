package ru.spbau.mit;

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

    private UIMain uiMain;
    private int cntBots;
    private int cntArtifacts;
    private GameMap gameMap;
    private Player player;
    private List<Player> bots = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Controller().start();
    }

    private void start() {
        uiMain = new UIMain();
        try {
            loadMap("maps/map_00.txt");
        } catch (IOException exception) {
            gameMap = new GameMap(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            cntBots = DEFAULT_CNT_BOTS;
            cntArtifacts = DEFAULT_CNT_ARTIFACTS;
        }

        player = new Player(new HumanStrategy(this),
                new Characteristics(20, 5, 100), gameMap.getRandomFreePosition());
        gameMap.setObject(player.getPosition(), player);

        for (int i = 0; i < cntBots; i++) {
            Player bot = new Player(new BotStrategy(),
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

        while (player.isAlive()) {
            uiMain.draw(gameMap, player);

            player.makeMove(gameMap);
            bots = bots.stream().filter(Player::isAlive).collect(Collectors.toList());
            for (int i = 0; i < bots.size(); i++) {
                bots.get(i).makeMove(gameMap);
            }
        }
    }

    public Direction getDirectionForHuman() {
        return uiMain.getDirectionFromKeyboard();
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
                switch (cellType) {
                    case '.':
                        gameMap.setObject(new Position(i, j), new EmptyCell());
                        break;
                    case '#':
                        gameMap.setObject(new Position(i, j), new Wall());
                        break;
                    case 'A':
                        gameMap.setObject(new Position(i, j),
                                new Artifact(new Characteristics(20, 20, 100), ArtifactType.GENERAL));
                }
            }
        }
    }
}
