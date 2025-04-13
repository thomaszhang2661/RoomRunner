package enginedriver.jsonreader.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import enginedriver.model.entity.Item;
import enginedriver.model.problems.IProblem;
import java.io.IOException;

/**
 * SerializerHelperUtils class provides utility methods for JSON serialization and deserialization.
 * It's package private and not intended for public use.
 */
class SerializerHelperUtils {

  static String getSolutionString(IProblem<?> problem) {
    Object solution = problem.getSolution();
    if (solution instanceof Item) {
      return ((Item) solution).getName();
    }
    return solution.toString();
  }

  static void serializeItems(JsonGenerator jsonGenerator, Item item) throws IOException {
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
}
