package jsonio.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import enginedriver.Fixture;
import enginedriver.GameWorld;
import enginedriver.Item;
import enginedriver.Room;
import enginedriver.problems.Monster;
import enginedriver.problems.Puzzle;
import java.io.IOException;
import java.util.Map;

/**
 * The GameWorldSerializer class is responsible for serializing the GameWorld object to JSON.
 * It uses Jackson to convert the GameWorld object into a JSON representation.
 */
public class GameWorldSerializer extends JsonSerializer<GameWorld> {
  @Override
  public void serialize(GameWorld gameWorld, JsonGenerator jsonGenerator,
                        SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeStringField("name", gameWorld.getName());
    jsonGenerator.writeStringField("version", gameWorld.getVersion());

    // Serialize rooms
    jsonGenerator.writeArrayFieldStart("rooms");

    for (Room room : gameWorld.getRooms().values()) {
      jsonGenerator.writeStartObject();

      jsonGenerator.writeStringField("room_name", room.getName());
      jsonGenerator.writeStringField("room_number", String.valueOf(room.getId()));
      jsonGenerator.writeStringField("description", room.getDescription()
              .replace("\"", "\\\"")
              .replace("\n", "\\n"));

      // Serialize exits
      Map<String, Integer> exits = room.getExits();
      for (Map.Entry<String, Integer> entry : exits.entrySet()) {
        jsonGenerator.writeNumberField(entry.getKey(), entry.getValue());
      }

      // Serialize problem
      if (room.getProblem() instanceof Puzzle) {
        jsonGenerator.writeStringField("puzzle", room.getProblem().getName());
        jsonGenerator.writeNullField("monster");
      } else if (room.getProblem() instanceof Monster) {
        jsonGenerator.writeNullField("puzzle");
        jsonGenerator.writeStringField("monster", room.getProblem().getName());
      } else {
        jsonGenerator.writeNullField("puzzle");
        jsonGenerator.writeNullField("monster");
      }

      // Serialize room items
      String items = room.getElementNames(Item.class);
      jsonGenerator.writeStringField("items", items.isEmpty() ? null : items);

      // Serialize room fixtures
      String fixtures = room.getElementNames(Fixture.class);
      jsonGenerator.writeStringField("fixtures", fixtures.isEmpty() ? null : fixtures);

      // Serialize picture
      jsonGenerator.writeStringField("picture", room.getPictureName());

      jsonGenerator.writeEndObject();
    }

    jsonGenerator.writeEndArray();

    // Serialize items
    if (!gameWorld.getRooms().isEmpty()) {
      jsonGenerator.writeArrayFieldStart("items");

      for (Item item : gameWorld.getItems()) {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("name", item.getName());
        jsonGenerator.writeStringField("weight", String.valueOf(item.getWeight()));
        jsonGenerator.writeStringField("max_uses", String.valueOf(item.getUseMax()));
        jsonGenerator.writeStringField("uses_remaining", String.valueOf(item.getRemainingUses()));
        jsonGenerator.writeStringField("value", String.valueOf(item.getValue()));
        jsonGenerator.writeStringField("when_used", item.getWhenUsed()
                .replace("\"", "\\\"")
                .replace("\n", "\\n"));
        jsonGenerator.writeStringField("description", item.getDescription()
                .replace("\"", "\\\"")
                .replace("\n", "\\n"));
        jsonGenerator.writeStringField("picture", item.getPictureName());

        jsonGenerator.writeEndObject();
      }

      jsonGenerator.writeEndArray();
    }

    // Serialize fixtures
    if (!gameWorld.getRooms().isEmpty()) {
      jsonGenerator.writeArrayFieldStart("fixtures");

      for (Fixture fixture : gameWorld.getFixtures()) {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("name", fixture.getName());
        jsonGenerator.writeStringField("weight", String.valueOf(fixture.getWeight()));
        jsonGenerator.writeStringField("puzzle", String.valueOf(fixture.getPuzzle()));
        jsonGenerator.writeStringField("states", String.valueOf(fixture.getStates()));
        jsonGenerator.writeStringField("description", fixture.getDescription()
                .replace("\"", "\\\"")
                .replace("\n", "\\n"));
        jsonGenerator.writeStringField("picture", fixture.getPictureName());

        jsonGenerator.writeEndObject();
      }

      jsonGenerator.writeEndArray();
    }

    // Serialize monsters
    if (!gameWorld.getRooms().isEmpty()) {
      jsonGenerator.writeArrayFieldStart("monsters");

      for (Monster<?> monster : gameWorld.getMonsters()) {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("name", monster.getName());
        jsonGenerator.writeStringField("active", String.valueOf(monster.getActive()));
        jsonGenerator.writeStringField("affects_target", String.valueOf(monster.getAffectsTarget()));
        jsonGenerator.writeStringField("affects_player", String.valueOf(monster.getAffectsPlayer()));
        jsonGenerator.writeStringField("solution", SerializerHelperUtils.getSolutionString(monster)
                .replace("\"", "\\\"")
                .replace("\n", "\\n"));
        jsonGenerator.writeStringField("value", String.valueOf(monster.getValue()));
        jsonGenerator.writeStringField("description", monster.getDescription()
                .replace("\"", "\\\"")
                .replace("\n", "\\n"));
        jsonGenerator.writeStringField("effects", monster.getEffects()
                .replace("\"", "\\\"")
                .replace("\n", "\\n"));
        jsonGenerator.writeStringField("damage", String.valueOf(monster.getDamage()));
        jsonGenerator.writeStringField("target", String.valueOf(monster.getTarget()));
        jsonGenerator.writeStringField("can_attack", String.valueOf(monster.getCanAttack()));
        jsonGenerator.writeStringField("attack", String.valueOf(monster.getAttack()));
        jsonGenerator.writeStringField("picture", monster.getPictureName());

        jsonGenerator.writeEndObject();
      }

      jsonGenerator.writeEndArray();
    }

    // Serialize puzzles
    if (!gameWorld.getRooms().isEmpty()) {
      jsonGenerator.writeArrayFieldStart("puzzles");

      for (Puzzle<?> puzzle : gameWorld.getPuzzles()) {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("name", puzzle.getName());
        jsonGenerator.writeStringField("active", String.valueOf(puzzle.getActive()));
        jsonGenerator.writeStringField("affects_target", String.valueOf(puzzle.getAffectsTarget()));
        jsonGenerator.writeStringField("affects_player", String.valueOf(puzzle.getAffectsPlayer()));
        jsonGenerator.writeStringField("solution", SerializerHelperUtils.getSolutionString(puzzle)
                .replace("\"", "\\\"")
                .replace("\n", "\\n"));
        jsonGenerator.writeStringField("value", String.valueOf(puzzle.getValue()));
        jsonGenerator.writeStringField("description", puzzle.getDescription()
                .replace("\"", "\\\"")
                .replace("\n", "\\n"));
        jsonGenerator.writeStringField("effects", puzzle.getEffects()
                .replace("\"", "\\\"")
                .replace("\n", "\\n"));
        jsonGenerator.writeStringField("target", String.valueOf(puzzle.getTarget()));
        jsonGenerator.writeStringField("picture", puzzle.getPictureName());

        jsonGenerator.writeEndObject();
      }

      jsonGenerator.writeEndArray();
    }

    jsonGenerator.writeEndObject();
  }
}
