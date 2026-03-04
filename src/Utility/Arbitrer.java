package src.Utility;

import src.GUI.GUI;

public class Arbitrer {

    float attackScore;
    float defenseScore;

    Arbitrer() {
        attackScore = 0;
        defenseScore = 0;
    }

    Arbitrer(float attackScore, float defenseScore) {
        this.attackScore = attackScore;
        this.defenseScore = defenseScore;
    }

    public AttackOutcome judgeAttack(float AttackScore, float DefenceScore){
        boolean success = AttackScore > DefenceScore;
        return new AttackOutcome(success);
    }

}
