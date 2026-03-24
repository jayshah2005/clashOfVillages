package src.Utility;

import src.PlayerAccount.Player;
import src.enums.AttackResult;
import src.ChallengeDecision.*;


/**
 * Adapter class converts current implementation into a format that can call the provided challenger code for attacks
 */
public class ChallengeAdapter implements AttackResolver {

    /**
     * Resolves an attack between two players using the provided challenge decision code
     *
     * @param attacker the attacking player
     * @param defender the defending player
     * @return AttackResult shows if the attack was a success or failure
     */
    @Override
    public AttackResult resolveAttack(Player attacker, Player defender) {

        // convert from original format to challenge format
        ChallengeEntitySet<Double, Double> attackerSet = convertArmy(attacker);
        ChallengeEntitySet<Double, Double> defenderSet = convertVillage(defender);

        // call the provided challenge arbiter
        ChallengeResult result = Arbitrer.challengeDecide(attackerSet, defenderSet);

        // convert back to orginal format
        return convertResult(result);
    }

    /**
     * convert attacker player army
     */
    private ChallengeEntitySet<Double, Double> convertArmy(Player player) {
        ChallengeEntitySet<Double, Double> set = new ChallengeEntitySet<>();
        //todo: create convorsion logic
        return set;
    }

    /**
     * convert defender village
     */
    private ChallengeEntitySet<Double, Double> convertVillage(Player player) {
        ChallengeEntitySet<Double, Double> set = new ChallengeEntitySet<>();
        //todo: create convorsion logic
        return set;
    }

    /**
     * convert the result back into the orginal format
     * @param result
     * @return
     */
    private AttackResult convertResult(ChallengeResult result) {
        return result.getChallengeWon() ? AttackResult.SUCCESS : AttackResult.FAILURE;
    }

}