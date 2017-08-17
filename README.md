# GameLevelPlugin

Lobby plugin that allows for a level inventory with 'consumable levels'

reward descriptions are set in a configuration and rewards can be consumed by a single other plugin.

## Configuration

```java
//Setup LevelsConfig with an example reward
LevelsConfig config = new LevelsConfig("CakeWars", "mg.cw");
config.addRewardHandler(new LevelHandler() {
  @Override
  public List<String> getRewards() {
    return Arrays.asList(new String[]{"Reward 1", "Reward 2"})
  }
       
  @Override
  public void handleReward(Player player) {
       //grant rewards
  }
       
  @Override
  public int getItemType() {
    return 0;//0 is default, 1 is special level item (fe. level 10), it will be displayed prettier
  }
});

//Register library
GameLevelLib lib = new GameLevelLib(gameLevelServiceAPI, plugin, levelsConfig, levelSlot);
Bukkit.getPluginManager().registerEvents(lib, plugin)

//Force open inventory
lib.openInventory(player);
```