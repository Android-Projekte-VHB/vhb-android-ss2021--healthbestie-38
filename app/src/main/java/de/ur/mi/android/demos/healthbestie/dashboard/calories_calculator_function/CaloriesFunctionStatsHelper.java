package de.ur.mi.android.demos.healthbestie.dashboard.calories_calculator_function;


public class CaloriesFunctionStatsHelper {

    private final static int DEFAULT_HEIGHT_IN_CM = 160;
    private final static double DEFAULT_WEIGHT_IN_KG = 50;
    private final static double DEFAULT_CALORIES_FACTOR = 1.55;
    private final static int DEFAULT_AGE = 20;
    private final static String DEFAULT_GENDER = "Male";

    private double height = DEFAULT_HEIGHT_IN_CM;
    private double weight = DEFAULT_WEIGHT_IN_KG;
    private double factor = DEFAULT_CALORIES_FACTOR;
    private int age = DEFAULT_AGE;
    private String gender = DEFAULT_GENDER;

    private double bmr;
    private double caloriesToMaintain;
    private double caloriesToLoose;
    private double caloriesToGain;


    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setFactor(double factor){
        this.factor = factor;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setGender(String gender){
        this.gender = gender;
    }


    private double bmrCalculate() {
        if(this.gender.equals("Male")){
            bmr = 66.47 + (13.75 * this.weight) + (5.003 * this.height) - (6.755 * this.age);
        }
        if(this.gender.equals("Female")){
            bmr = 655.1 + (9.563 * this.weight) + (1.85 * this.height) - (4.676 * this.age);
        }
        return bmr;
    }

    public double getCaloriesToMaintain() {
        caloriesToMaintain = bmrCalculate() * factor;
        return caloriesToMaintain;
    }

    public double getCaloriesToLoose() {
        caloriesToLoose = caloriesToMaintain - 300;
        return caloriesToLoose;
    }

    public double getCaloriesToGain() {
        caloriesToGain = caloriesToMaintain + 350;
        return caloriesToGain;
    }
}
