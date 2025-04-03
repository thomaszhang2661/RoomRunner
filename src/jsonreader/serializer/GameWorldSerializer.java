package jsonreader.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import enginedriver.GameWorld;

/**
 * The GameWorldSerializer class is responsible for serializing the GameWorld object to JSON.
 * It uses Jackson to convert the GameWorld object into a JSON representation.
 */
public class GameWorldSerializer extends JsonSerializer<GameWorld> {
  @Override
  public GameWorld serialize(GameWorld gameWorld, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("name", gameWorld.getName());
      jsonGenerator.writeNumberField("width", gameWorld.getWidth());
      jsonGenerator.writeNumberField("height", gameWorld.getHeight());
      jsonGenerator.writeEndObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gameWorld;
  }
}
