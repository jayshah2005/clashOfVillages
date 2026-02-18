package src.Utility;

public class AttackOutcome {
    private boolean success; // boolean success

    public AttackOutcome(boolean success) {
        this.success = success;
    }

    /**
     * isSucess returns the success boolean after the attack was determined as successful by the Arbitrer class
     * @return
     */
    public boolean isSuccess(){
        return success;
    }

}
