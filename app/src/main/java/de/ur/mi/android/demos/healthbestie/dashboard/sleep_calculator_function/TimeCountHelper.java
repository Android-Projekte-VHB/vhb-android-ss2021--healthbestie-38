package de.ur.mi.android.demos.healthbestie.dashboard.sleep_calculator_function;

import android.os.CountDownTimer;

public class TimeCountHelper {
    private final static int DEFAULT_SLEEP_HOUR = 23;
    private final static int DEFAULT_SLEEP_MIN = 30;
    private final static int DEFAULT_WAKE_UP_HOUR = 8;
    private final static int DEFAULT_WAKE_UP_MIN = 15;

    private int sleepHour = DEFAULT_SLEEP_HOUR;
    private int sleepMinute = DEFAULT_SLEEP_MIN;
    private int wakeUpHour = DEFAULT_WAKE_UP_HOUR;
    private int wakeUpMinute = DEFAULT_WAKE_UP_MIN;

    public void setSleepHour(int startHour){this.sleepHour = startHour;}
    public void setSleepMin(int startMin){this.sleepMinute = startMin;}
    public void setWakeUpHour(int finishHour){this.wakeUpHour = finishHour;}
    public void setWakeUpMin(int finishMinute){this.wakeUpMinute = finishMinute;}

    private int hr9HrsAfterGoToBed, hr7HrsAfterGoToBed, hr6HrsAfterGoToBed, hr4HrsAfterGoToBed;
    private int hr9HrsBeforeWakeUp, hr7HrsBeforeWakeUp, hr6HrsBeforeWakeUp, hr4HrsBeforeWakeUp;
    private int min9HrsAfterGoToBed, min7HrsAfterGoToBed, min6HrsAfterGoToBed, min4HrsAfterGoToBed;
    private int min9HrsBeforeWakeUp, min7HrsBeforeWakeUp, min6HrsBeforeWakeUp, min4HrsBeforeWakeUp;

    //get hour
    public int getHr9hrsBeforeWakeUp(){
        if(this.wakeUpHour < 9) {
            hr9HrsBeforeWakeUp = 24 + (this.wakeUpHour - 9);
        }
        else {
            hr9HrsBeforeWakeUp = this.wakeUpHour - 9;
        }
        return hr9HrsBeforeWakeUp;
    }

    public int getHr7HrsBeforeWakeUp(){
        if(this.wakeUpHour < 7) {
            hr7HrsBeforeWakeUp = 24 + (this.wakeUpHour - 7);
        }else{
            hr7HrsBeforeWakeUp = this.wakeUpHour - 7;}
        return hr7HrsBeforeWakeUp;
    }

    public int getHr6HrsBeforeWakeUp(){
        if(this.wakeUpHour < 6) {
            hr6HrsBeforeWakeUp = 24 + (this.wakeUpHour - 6);
        } else {
            hr6HrsBeforeWakeUp = this.wakeUpHour - 6;}
        return hr6HrsBeforeWakeUp;
    }

    public int getHr4HrsBeforeWakeUp(){
        if(this.wakeUpHour < 4) {
            hr7HrsBeforeWakeUp = 24 + (this.wakeUpHour - 4);
        } else {
            hr4HrsBeforeWakeUp = this.wakeUpHour - 4;}
        return hr4HrsBeforeWakeUp;
    }

    public int getHr9hrsAfterGoToBed(){
        if(this.sleepHour + 9 > 24) {
            hr9HrsAfterGoToBed = 9 - (24 - this.sleepHour);
        } else {
            hr9HrsAfterGoToBed = this.sleepHour + 9;}
        return hr9HrsAfterGoToBed;
    }

    public int getHr7hrsAfterGoToBed(){
        if(this.sleepHour + 7 > 24) {
            hr7HrsAfterGoToBed = 7 - (24 - this.sleepHour);
        } else {
            hr7HrsAfterGoToBed = this.sleepHour + 7;}
        return hr7HrsAfterGoToBed;
    }

    public int getHr6hrsAfterGoToBed(){
        if(this.sleepHour + 6 > 24) {
            hr6HrsAfterGoToBed = 6 - (24 - this.sleepHour);
        } else {
            hr6HrsAfterGoToBed = this.sleepHour + 6;}
        return hr6HrsAfterGoToBed;
    }

    public int getHr4hrsAfterGoToBed(){
        if(this.sleepHour + 4 > 24) {
            hr4HrsAfterGoToBed = 4 - (24 - this.sleepHour);
        } else {
            hr4HrsAfterGoToBed = this.sleepHour + 4;}
        return hr4HrsAfterGoToBed;
    }


    //get minute
    public int getMin9hrsBeforeWakeUp(){
        min9HrsBeforeWakeUp = this.wakeUpMinute;
        return min9HrsBeforeWakeUp;
    }

    public int getMin7hrsBeforeWakeUp(){
        if(this.wakeUpMinute < 30){
            min7HrsBeforeWakeUp = this.wakeUpMinute - 30;
        }
        if(this.wakeUpMinute >= 30) {
            min7HrsBeforeWakeUp = this.wakeUpMinute + 30;
        }
        while(min7HrsBeforeWakeUp < 0){
            min7HrsBeforeWakeUp = Math.abs(min7HrsBeforeWakeUp);
        }
        while(min7HrsBeforeWakeUp >= 60){
            min7HrsBeforeWakeUp -= 60;
        }
        return min7HrsBeforeWakeUp;
    }

    public int getMin6hrsBeforeWakeUp(){
        min6HrsBeforeWakeUp = this.wakeUpMinute;
        return min6HrsBeforeWakeUp;
    }

    public int getMin4hrsBeforeWakeUp(){
        if(this.wakeUpMinute < 30){
            min4HrsBeforeWakeUp = this.wakeUpMinute - 30;
        }
        if(this.wakeUpMinute >= 30) {
            min4HrsBeforeWakeUp = this.wakeUpMinute + 30;
        }
        while(min4HrsBeforeWakeUp < 0){
            min4HrsBeforeWakeUp = Math.abs(min4HrsBeforeWakeUp);
        }
        while(min4HrsBeforeWakeUp >= 60){
            min4HrsBeforeWakeUp -= 60;
        }
        return min4HrsBeforeWakeUp;
    }

    public int getMin9hrsAfterGoToBed(){
        min9HrsAfterGoToBed = this.sleepMinute;
        return min9HrsAfterGoToBed;
    }

    public int getMin7hrsAfterGoToBed(){
        if(this.sleepMinute > 30){
            min7HrsAfterGoToBed = this.sleepMinute + 30;
        }
        if(this.wakeUpMinute <= 30) {
            min7HrsAfterGoToBed = this.sleepMinute - 30;
        }
        while(min7HrsAfterGoToBed >= 60) {
            min7HrsAfterGoToBed -= 60;
        }
        while(min7HrsAfterGoToBed < 0){
            min7HrsAfterGoToBed = Math.abs(min7HrsAfterGoToBed);
        }
        return min7HrsAfterGoToBed;
    }

    public int getMin6hrsAfterGoToBed(){
        min6HrsAfterGoToBed = this.sleepMinute;
        return min6HrsAfterGoToBed;
    }

    public int getMin4hrsAfterGoToBed(){
        if(this.sleepMinute > 30){
            min4HrsAfterGoToBed = this.sleepMinute + 30;
        }
        if(this.wakeUpMinute <= 30) {
            min4HrsAfterGoToBed = this.sleepMinute - 30;
        }
        while(min4HrsAfterGoToBed < 0){
            min4HrsAfterGoToBed = Math.abs(min4HrsAfterGoToBed);
        }
        while(min4HrsAfterGoToBed >= 60){
            min4HrsAfterGoToBed -= 60;
        }
        return min4HrsAfterGoToBed;
    }
}

