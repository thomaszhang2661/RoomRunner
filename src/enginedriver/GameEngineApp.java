package enginedriver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import java.util.Scanner;

import jsonreader.GameControllerDeserializer;
import jsonreader.GameDataLoader;
import jsonreader.GameWorldDeserializer;
import jsonreader.PlayerDeserializer;

public class GameEngineApp {
  private GameController gameController;
  private Readable source;
  private Appendable output;

  public GameEngineApp(String gameFileName, Readable source, Appendable output) throws IOException {
    this.source = Objects.requireNonNull(source);
    this.output = Objects.requireNonNull(output);


    // 创建 ObjectMapper 并注册自定义反序列化器
    ObjectMapper mapper1 = new ObjectMapper();
    mapper1.registerModule(new SimpleModule()
            .addDeserializer(GameWorld.class, new GameWorldDeserializer());

    // 解析游戏文件（JSON），加载游戏世界
    GameWorld gameWorld = mapper1.readValue(new File(gameFileName), GameWorld.class);

//    如果存在player
    // 创建 ObjectMapper 并注册自定义反序列化器
    ObjectMapper mapper2 = new ObjectMapper();
    mapper2.registerModule(new SimpleModule()
            .addDeserializer(Player.class, new PlayerDeserializer()));

    // 解析游戏文件（JSON），加载玩家角色
    Player player = mapper2.readValue(new File(gameFileName), Player.class);

//    如果不存在player
    // 创建玩家
    // 提示用户输入名字
    // TODO： 检测是否存在同名文件，有重名要提示不能用
    if (player == null) {
      String playerName = getPlayerName();
      player = new Player(playerName, 100, 13, 0); // 提示玩家输入名字
    }

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
