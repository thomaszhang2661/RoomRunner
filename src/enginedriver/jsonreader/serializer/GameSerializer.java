package enginedriver.jsonreader.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import enginedriver.GameController;
import enginedriver.model.GameWorld;
import enginedriver.model.entity.Fixture;
import enginedriver.model.entity.Item;
import enginedriver.model.entitycontainer.Player;
import enginedriver.model.entitycontainer.Room;
import enginedriver.model.problems.Monster;
import enginedriver.model.problems.Puzzle;
import java.io.IOException;
import java.util.Map;

/**
 * A custom serializer for the GameController class, which converts a GameController object
 * into a JSON representation containing both world and player data.
 */
public class GameSerializer extends JsonSerializer<GameController> {
  @Override
  public void serialize(GameController game, JsonGenerator jsonGenerator,
                        SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();

    // serialize game world
    jsonGenerator.writeObjectFieldStart("world");
    serializeWorld(game.getGameWorld(), game.getPlayer(), jsonGenerator);
    jsonGenerator.writeEndObject();

    // serialize player
    jsonGenerator.writeObjectFieldStart("player");
    serializePlayer(game.getPlayer(), jsonGenerator);
    jsonGenerator.writeEndObject();

    jsonGenerator.writeEndObject();
  }

  /**
   * Serialize a GameWorld object into JSON.
   */
  private void serializeWorld(GameWorld gameWorld, Player player, JsonGenerator jsonGenerator) throws IOException {
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
    jsonGenerator.writeArrayFieldStart("items");
    if (!gameWorld.getRooms().isEmpty()) {
      // room items
      for (Item item : gameWorld.getItems()) {
        SerializerHelperUtils.serializeItems(jsonGenerator, item);
      }

    }
    if (!player.getEntitiesByType(Item.class).isEmpty()) {
      // player items
      for (Item item : player.getEntitiesByType(Item.class)) {
        SerializerHelperUtils.serializeItems(jsonGenerator, item);
      }
    }
    jsonGenerator.writeEndArray();


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
  }

  /**
   * Serialize a Player object into JSON.
   */
  private void serializePlayer(Player player, JsonGenerator jsonGenerator) throws IOException {
    jsonGenerator.writeStringField("name", player.getName());
    jsonGenerator.writeStringField("health", String.valueOf(player.getHealth()));
    jsonGenerator.writeStringField("inventory", String.join(", ", player.getEntities().keySet()));
    jsonGenerator.writeStringField("max_weight", String.valueOf(player.getMaxWeight()));
    jsonGenerator.writeStringField("current_weight", String.valueOf(player.getCurrentWeight()));
    jsonGenerator.writeStringField("room_number", String.valueOf(player.getRoomNumber()));
    jsonGenerator.writeStringField("score", String.valueOf(player.getScore()));

    // Serialize items
    jsonGenerator.writeArrayFieldStart("items");
    for (Item item : player.getEntitiesByType(Item.class)) {
      SerializerHelperUtils.serializeItems(jsonGenerator, item);
    }
    jsonGenerator.writeEndArray();
  }
}