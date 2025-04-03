//package jsonreader.deserializer;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import enginedriver.Item;
//import enginedriver.problems.IProblem;
//import enginedriver.problems.Monster;
//import enginedriver.problems.Puzzle;
//import enginedriver.problems.validator.ItemSolutionValidator;
//import enginedriver.problems.validator.SolutionValidator;
//import enginedriver.problems.validator.StringSolutionValidator;
//import java.io.IOException;
//import java.util.Map;
//
///**
// * A custom deserializer for the Problem class, which converts a JSON representation of
// * a problem into a Problem object.
// */
//public class ProblemDeserializer extends JsonDeserializer<IProblem<?>> {
//  @Override
//  public IProblem<?> deserialize(JsonParser jsonParser,
//                              DeserializationContext deserializationContext) throws IOException {
//    ObjectMapper mapper = new ObjectMapper();
//    JsonNode rootNode = mapper.readTree(jsonParser);
//
//    // get the problem type
//    String problemType = rootNode.get("type").asText();
//
//    // get the items map from the deserialization context
//    Map<String, Item> items = (Map<String, Item>) deserializationContext
//            .findInjectableValue(ItemSolutionValidator.class.getName(),
//                    null, null);
//    if (items == null) {
//      throw new IOException("Missing injectable value for items");
//    }
//
//    // deserialize the problem based on its type
//    switch (problemType) {
//      case "monster":
//        return deserializeMonster(rootNode, items);
//      case "puzzle":
//        return deserializePuzzle(rootNode, items);
//      default:
//        throw new IOException("Unknown problem type: " + problemType);
//    }
//  }
//
//  private <T> Monster<T> deserializeMonster(JsonNode node, Map<String, Item> items) {
//    return deserializeSolution(node, items, ProblemType.MONSTER);
//  }
//
//  private <T> Puzzle<T> deserializePuzzle(JsonNode node, Map<String, Item> items) {
//    return deserializeSolution(node, items, ProblemType.PUZZLE);
//  }
//
//  private <T> T deserializeSolution(JsonNode node, Map<String, Item> items, ProblemType problemType) {
//    String name = DeserializerHelperUtils.getNodeText(node, "name");
//    Boolean active = node.get("active").asBoolean();
//    Boolean affectsTarget = node.get("affects_target").asBoolean();
//    Boolean affectsPlayer = node.get("affects_player").asBoolean();
//    int value = DeserializerHelperUtils.getNodeInt(node, "value");
//    String description = DeserializerHelperUtils.getNodeText(node, "description");
//    String effects = DeserializerHelperUtils.getNodeText(node, "effects");
//    String target = DeserializerHelperUtils.getNodeText(node, "target");
//    String pictureName = DeserializerHelperUtils.getNodeText(node, "picture");
//
//    T solution = (T) extractSolution(node, items);
//    SolutionValidator<T> validator = (SolutionValidator<T>) createValidator(node, items);
//
//    if (problemType == ProblemType.MONSTER) {
//      int damage = DeserializerHelperUtils.getNodeInt(node, "damage");
//      Boolean canAttack = node.get("can_attack").asBoolean();
//      String attack = DeserializerHelperUtils.getNodeText(node, "attack");
//
//      return (T) new Monster<>(name, description, active,
//              affectsTarget, canAttack, affectsPlayer, solution, value,
//              damage, effects, target, pictureName, attack, validator);
//    } else {
//      return (T) new Puzzle<>(name, description, active,
//              affectsTarget, affectsPlayer, solution, value,
//              effects, target, pictureName, validator);
//    }
//  }
//
//  private Object extractSolution(JsonNode node, Map<String, Item> items) {
//    String solutionText = DeserializerHelperUtils.getNodeText(node, "solution");
//    if (solutionText.startsWith("'") && solutionText.endsWith("'")) {
//      return solutionText.substring(1, solutionText.length() - 1);
//    } else {
//      return items.getOrDefault(solutionText, null);
//    }
//  }
//
//  private SolutionValidator<?> createValidator(JsonNode node, Map<String, Item> items) {
//    String solutionText = DeserializerHelperUtils.getNodeText(node, "solution");
//    if (solutionText.startsWith("'") && solutionText.endsWith("'")) {
//      return new StringSolutionValidator();
//    } else {
//      return new ItemSolutionValidator();
//    }
//  }
//
//  enum ProblemType {
//    MONSTER, PUZZLE
//  }
//}
