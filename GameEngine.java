import PlayerAccount.Player;
import PlayerAccount.Resources;
import PlayerAccount.Village;
import PlayerAccount.VillageObject;
import PlayerAccount.Units.Fighter;

import java.util.ResourceBundle;

// game engine holds all of the methods that control the game
public abstract class GameEngine {

  private Player players; // is dependant on the player

  public Village findRandomVillage() { // finds random village for player to attack
    return null;
  }

  public int getSuccessRate(Player attacker, Player defender) { // deteremines if the players attack was successful
    return 0;
  }

  public Resources getLoot(Player player) { // if attack is successful, determines the loot they will recieve
    return null;
  }

  public Fighter[] generateArmy(Player player) { // generates the units that will be used to attack / defend
    return null;
  }

  public boolean canUpgrade(Player player, VillageObject obj) { // checks if the player has the resources to upgrade an object
    return false;
  }

  public boolean canAttack(Player player) { // checks if the player can be attacked
    return false;
  }

  public boolean canProduce(Player player, VillageObject obj) { // checks if the building can be produced
    return false;
  }

  public float getAttackScore(Player player) {
    return 0.0f;
  }

  public float getDefenceScore(Player player) {
    return 0.0f;
  }

  public float getLootScore(Player player) {
    return 0.0f;
  }

  public float getOverallScore(Player player) {
    return 0.0f;
  }

}