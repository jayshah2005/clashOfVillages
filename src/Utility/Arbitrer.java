package src.Utility;

public class Arbitrer {

    public AttackOutcome judgeAttack(float AttackScore, float DefenceScore){
        boolean success = AttackScore > DefenceScore;
        return new AttackOutcome(success);
    }

}
