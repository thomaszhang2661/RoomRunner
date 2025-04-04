[JSON access] - 04/03/2025
 - Created utility classes for serialization and deserialization with shared helper functions
 - Added accessor methods to GameWorld: getItems(), getFixtures(), getMonsters(), getPuzzles()
 - Refactored serialization from string concatenation to custom Jackson serializers (similar to existing deserialization)
 - Removed unnecessary toString() methods and related tests; updated GameController's save()/restore() methods
 - Split storage into separate gameworld.json and player.json files (player.json stores item names as strings, with actual items in gameworld.json)
 - Modified GameEngineApp constructor to maintain backward compatibility
 - Renamed "jsonio" package to "jsonio" to better reflect its expanded functionality
 - Set resource as the root directory for saving json, and move all savefiles to resource.
 - Updated GameEngineApp to use the new JSON serialization and deserialization methods