package enginedriver.model;

import java.util.Map;

import enginedriver.model.entitycontainer.Room;

public interface IGameWorld {
  /**
   * Get the name of the game world.
   * @return the name of the game world
   */
  String getName();

  /**
   * Retrieve the version of the GameWorld.
   * @return the version of the world
   */
  String getVersion();

  /**
   * Get the rooms in the game world.
   * @return the rooms in the game world
   */
  Map<Integer, Room> getRooms();

  /**
   * Get a room by its ID.
   * @param id The ID of the room.
   * @return The room with the specified ID.
   */
  Room<?> getRoom(int id);

  /**
   * Set the name of the gameWorld object.
   * @param name the name of the game world
   */
  void setName(String name);

  /**
   * Set the version of the game world.
   * @param version the version of the game world
   */
  void setVersion(String version);

  /**
   * Set the rooms in the game world.
   * @param rooms the rooms in the game world
   */
  void setRooms(Map<Integer, Room> rooms);

  /**
   * Get a string representation of the game world.
   * @return a string representation of the game world
   */
  @Override
  String toString();
}
