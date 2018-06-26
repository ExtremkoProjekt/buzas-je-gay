/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author MATEJ
 */
public class BuildingProgress {

    private int id;
    private int level;
    private int price;
    private int value;
    private int steps;

    public static final int KASAREN = 1;
    public static final int ZLATA_BANA = 2;
    public static final int HLAVNA_BUDOVA = 3;


    public BuildingProgress(int id, int level, int price, int value, int steps) {
        this.id = id;
        this.level = level;
        this.price = price;
        this.value = value;
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public static String getTypeOfBuidling(int i){
        switch (i){
            case BuildingProgress.KASAREN:
                return "KASAREN";

            case BuildingProgress.ZLATA_BANA:
                return "ZLATA_BANA";

            case BuildingProgress.HLAVNA_BUDOVA:
                return "HLAVNA_BUDOVA";

            default:
                return "NULL";

        }
    }
}
