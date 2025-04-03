package jsonreader.serializer;

import enginedriver.problems.IProblem;
import enginedriver.problems.Monster;
import enginedriver.problems.Puzzle;

/**
 * SerializerHelperUtils class provides utility methods for JSON serialization and deserialization.
 * It's package private and not intended for public use.
 */
class SerializerHelperUtils {

  static String getProblemType(IProblem problem) {
    if (problem instanceof Monster) {
      return "monster";
    }
    if (problem instanceof Puzzle) {
      return "puzzle";
    }
    return null;
  }
}
