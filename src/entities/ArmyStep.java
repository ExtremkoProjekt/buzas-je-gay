
package entities;

/**
 *
 * @author GULO
 */
public class ArmyStep {
    private int userID;
    private int townID;
    private int oponentUserID;
    private int oponentTownID;
    private int army;
    private int remainingSteps;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTownID() {
        return townID;
    }

    public void setTownID(int townID) {
        this.townID = townID;
    }

    public int getOponentUserID() {
        return oponentUserID;
    }

    public void setOponentUserID(int oponentUserID) {
        this.oponentUserID = oponentUserID;
    }

    public int getOponentTownID() {
        return oponentTownID;
    }

    public void setOponentTownID(int oponentTownID) {
        this.oponentTownID = oponentTownID;
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public int getRemainingSteps() {
        return remainingSteps;
    }

    public void setRemainingSteps(int remainingSteps) {
        this.remainingSteps = remainingSteps;
    }
}
