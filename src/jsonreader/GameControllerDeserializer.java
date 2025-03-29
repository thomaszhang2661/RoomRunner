package jsonreader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import enginedriver.GameController;
import enginedriver.GameWorld;
import enginedriver.Player;

import java.io.IOException;

public class GameControllerDeserializer extends JsonDeserializer<GameController> {

  @Override
  public GameController deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
    JsonNode rootNode = mapper.readTree(jsonParser);

    GameWorld gameWorld = mapper.treeToValue(rootNode.get("gameWorld"), GameWorld.class);
    Player player = rootNode.has("player") ? mapper.treeToValue(rootNode.get("player"), Player.class) : null;

    return new GameController(gameWorld, player);
  }
}