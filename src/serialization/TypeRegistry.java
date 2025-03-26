package serialization;

public class TypeRegistry {
  private final ObjectMapper mapper;

  public JacksonParser() {
    this.mapper = new ObjectMapper();
    registerCustomAdapters(); // 注册游戏专用适配器
  }

  private void registerCustomAdapters() {
    mapper.registerModule(new SimpleModule()
            .addAdapter(new Vector2dAdapter())  // 自定义向量解析
            .addAdapter(new ColorAdapter())      // 自定义颜色解析
    );
  }

  @Override
  public <T> T parse(String json, Class<T> clazz) {
    try {
      return mapper.readValue(json, clazz);
    } catch (IOException e) {
      throw new ResourceLoadException("JSON 解析失败: " + clazz.getSimpleName(), e);
    }
  }
}
