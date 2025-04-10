package jsonio.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import enginedriver.Player;
import java.io.IOException;

/**
 * A custom serializer for the Player class, which converts a Player object into a JSON
 * representation.
 */
public class PlayerSerializer extends JsonSerializer<Player> {
  @Override
  public void serialize(Player player, JsonGenerator jsonGenerator,
                        SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeStringField("name", player.getName());
    jsonGenerator.writeStringField("health", String.valueOf(player.getHealth()));
    jsonGenerator.writeStringField("inventory", String.join(", ", player.getEntities().keySet()));
    jsonGenerator.writeStringField("max_weight", String.valueOf(player.getMaxWeight()));
    jsonGenerator.writeStringField("current_weight", String.valueOf(player.getCurrentWeight()));
    jsonGenerator.writeStringField("room_number", String.valueOf(player.getRoomNumber()));
    jsonGenerator.writeStringField("score", String.valueOf(player.getScore()));

    jsonGenerator.writeEndObject();
  }
}
