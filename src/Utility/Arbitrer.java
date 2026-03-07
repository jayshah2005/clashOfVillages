package src.Utility;

import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;

import java.util.Random;

public class Arbitrer {

    Player attacker;
    Player defender;

    public Arbitrer(Player attacker, Player defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public double simulateAttack(float successRate){
        Random rand = new Random();

        double x = rand.nextDouble(); // Uniform in [0, 1]
        double outcome = Math.pow(x, 1/successRate); // Skews toward success rate

        attacker.resetArmy();
        defender.resetGuardTime();

        return outcome;
    }
}
