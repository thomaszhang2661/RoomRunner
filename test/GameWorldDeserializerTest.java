import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.GameWorld;
import jsonreader.GameWorldDeserializer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameWorldDeserializerTest {

  @Test
  public void testDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new SimpleModule().addDeserializer(GameWorld.class, new GameWorldDeserializer()));

    GameWorld gameWorld = mapper.readValue(new File("test/Test.json"), GameWorld.class);

    assertEquals("Simple Scenarios", gameWorld.getName());
    assertEquals("0.0.1", gameWorld.getVersion());
    // Add more assertions as needed
  }
}