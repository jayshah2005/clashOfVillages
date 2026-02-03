import PlayerAccount.Player;
import PlayerAccount.Village;
import PlayerAccount.VillageObject;

import java.util.ResourceBundle;

public abstract class GameEngine {

  private Player players;

  public Village findRandomVillage() {
  }

  public int getSuccessRate(Player attacker, Player defender) {
  }

  public Resources getLoot(player: Player) {
  }

  public  generateArmy(player: Player) {
  }

  public canUpgrade( player: Player, obj VillageObject) {
  }

  public canAttack(Player player) {
  }

  public canProduce(Player player,  obj:VillageObject) {
  }

  public getAttackScore(Player player) {
  }

  public getDefenceScore(Player player) {
  }

  public getLootScore() {
  }

  public getOverallScore() {
  }

}