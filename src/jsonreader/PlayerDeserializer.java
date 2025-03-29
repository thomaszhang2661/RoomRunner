package jsonreader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import enginedriver.Player;

public class PlayerDeserializer extends JsonDeserializer<Player> {

  @Override
  public Player deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
    JsonNode rootNode = mapper.readTree(jsonParser);

    String name = rootNode.get("name").asText();
    int health = rootNode.get("health").asInt();
    int strength = rootNode.get("strength").asInt();
    int score = rootNode.has("score") ? rootNode.get("score").asInt() : 0;

    return new Player(name, health, strength, score);
  }
}