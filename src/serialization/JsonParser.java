package serialization;
import com.fasterxml.jackson.core.type.TypeReference;

public interface JsonParser {
  <T> T parse(String json, Class<T> clazz);  // 解析简单对象
  <T> T parse(String json, TypeReference<T> type); // 解析复杂泛型（如List<Entity>）
}
