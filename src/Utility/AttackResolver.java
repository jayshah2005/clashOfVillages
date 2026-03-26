package src.Utility;

import src.PlayerAccount.Player;
import src.enums.AttackResult;

public interface AttackResolver {
    AttackResult resolveAttack(Player attacker, Player defender);
}