import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.Player;
import jsonreader.PlayerDeserializer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerDeserializerTest {

  @Test
  public void testDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new SimpleModule().addDeserializer(Player.class, new PlayerDeserializer()));

    Player player = mapper.readValue(new File("test/Test.json"), Player.class);

    assertEquals("Test Player", player.getName());
    assertEquals(100, player.getHealth());
    assertEquals(13, player.getMaxWeight());
    assertEquals(0, player.getScore());
    // Add more assertions as needed
  }
}