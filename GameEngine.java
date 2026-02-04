import PlayerAccount.Player;
import PlayerAccount.Resources;
import PlayerAccount.Village;
import PlayerAccount.VillageObject;
import PlayerAccount.Units.Fighter;

import java.util.ResourceBundle;

public abstract class GameEngine {

  private Player players;

  public Village findRandomVillage() {
    return null;
  }

  public int getSuccessRate(Player attacker, Player defender) {
    return 0;
  }

  public Resources getLoot(Player player) {
    return null;
  }

  public Fighter[] generateArmy(Player player) {
    return null;
  }

  public boolean canUpgrade(Player player, VillageObject obj) {
    return false;
  }

  public boolean canAttack(Player player) {
    return false;
  }

  public boolean canProduce(Player player, VillageObject obj) {
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