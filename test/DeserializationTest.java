import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.GameWorld;
import enginedriver.Player;
import java.io.File;
import java.io.IOException;
import jsonreader.GameWorldDeserializer;
import jsonreader.PlayerDeserializer;
import org.junit.jupiter.api.Test;

class DeserializationTest {

  @Test
  void testDeserializeGameWorld() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new SimpleModule().addDeserializer(GameWorld.class,
            new GameWorldDeserializer()));

    GameWorld gameWorld = mapper.readValue(new File("test/TestGameWorld.json"), GameWorld.class);

    assertNotNull(gameWorld);
    assertEquals("Simple Scenarios", gameWorld.getName());
    assertEquals("0.0.1", gameWorld.getVersion());
    assertEquals(5, gameWorld.getRooms().size());
    assertTrue(gameWorld.getRooms().containsKey(1));
    assertTrue(gameWorld.getRooms().containsKey(5));
  }

  @Test
  void testDeserializePlayer() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(GameWorld.class, new GameWorldDeserializer());
    mapper.registerModule(module);

    GameWorld gameWorld = mapper.readValue(new File("test/TestGameWorld.json"), GameWorld.class);
    module.addDeserializer(Player.class, new PlayerDeserializer(gameWorld));

    Player player = mapper.readValue(new File("test/TestGameWorld.json"), Player.class);

    assertNotNull(player);
    assertEquals("Avatar", player.getName());
    assertEquals(100, player.getHealth());
    assertEquals(20, player.getMaxWeight());
    assertEquals(0, player.getScore());
  }
}