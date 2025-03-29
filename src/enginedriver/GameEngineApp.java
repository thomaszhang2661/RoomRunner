package enginedriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import java.util.Scanner;

import jsonreader.GameDataLoader;

public class GameEngineApp {
  private GameController gameController;
  private Readable source;
  private Appendable output;

  public GameEngineApp(String gameFileName, Readable source, Appendable output) throws IOException {
    this.source = Objects.requireNonNull(source);
    this.output = Objects.requireNonNull(output);

    // 解析游戏文件（JSON），加载游戏世界和玩家
    GameWorld gameWorld = GameDataLoader.loadGameWorld(gameFileName);
    // 创建玩家
    // 提示用户输入名字
    // TODO： 检测是否存在同名文件，有重名要提示不能用
    String playerName = getPlayerName();
    Player player = new Player(playerName, 100, 13, 0); // 提示玩家输入名字

    this.gameController = new GameController(gameWorld, player);
  }

  public void start() throws IOException {
    BufferedReader reader = new BufferedReader((Reader) source);

    String command;
    while ((command = reader.readLine()) != null) {
      if (command.equalsIgnoreCase("Q")) {
        System.out.println("Exiting game.");
        break;
      }
      gameController.processCommand(command);
    }
  }

  public static void main(String[] args) throws IOException {
    String s = "Sir Mix-A-Lot\nT NOTEBOOK\nN\nT HAIR CLIPPERS\nT KEY\nD NOTEBOOK\nQuit";
    BufferedReader stringReader = new BufferedReader(new StringReader(s));
    GameEngineApp gameEngineApp = new GameEngineApp(
            "./resources/align_quest_game_elements.json",
            stringReader,
            System.out);

    gameEngineApp.start();
  }

  private String getPlayerName() {
    // 创建 Scanner 对象以读取用户输入
    Scanner scanner = new Scanner(System.in);

    // 提示用户输入姓名
    System.out.print("Enter your name: ");

    // 读取用户输入的字符串
    String playerName = scanner.nextLine();

    // 输出用户输入的姓名
    System.out.println("Your name is: " + playerName);

    // 关闭 Scanner 对象
    scanner.close();
    return playerName;
  }
}
