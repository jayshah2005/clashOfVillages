package src.Utility;

import src.PlayerAccount.Buildings.Defence;
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

        System.out.println("Attack won: " + result.getChallengeWon());
        System.out.println("Loot size: " + result.getLoot().size());

        // convert back to orginal format
        return convertResult(result);
    }

    /**
     * convert attacker player army
     */
    private ChallengeEntitySet<Double, Double> convertArmy(Player player) {
        ChallengeEntitySet<Double, Double> set = new ChallengeEntitySet<>();

        player.fighters.forEach((fighter, count) -> {
            double damage = fighter.getAttackScore();
            double health = 20 + fighter.getAttackScore();
            for (int i = 0; i < count; i++) {
                set.getEntityAttackList().add(
                        new ChallengeAttack<>(damage, health)
                );
            }
        });

        return set;
    }

    /**
     * convert defender village
     */
    private ChallengeEntitySet<Double, Double> convertVillage(Player player) {
        ChallengeEntitySet<Double, Double> set = new ChallengeEntitySet<>();

        for (var obj : player.village.getVillageObjects()) {

            if (obj instanceof Defence) {

                Defence d = (Defence) obj;

                double defense = d.getDamage();
                double hp = d.getHitpoints();

                ChallengeDefense<Double, Double> def =
                        new ChallengeDefense<>(defense, hp);

                set.getEntityDefenseList().add(def);
            }
        }

        // if no defence buildings, default defense
        if (set.getEntityDefenseList().isEmpty()) {
            set.getEntityDefenseList().add(new ChallengeDefense<>(5.0, 20.0));
        }

        // resources for loot
        set.getEntityResourceList().add(new ChallengeResource<>(100.0));
        set.getEntityResourceList().add(new ChallengeResource<>(50.0));

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