package jsonio.serializer;

import enginedriver.Item;
import enginedriver.problems.IProblem;

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

}
