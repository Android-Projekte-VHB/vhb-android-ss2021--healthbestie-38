package de.ur.mi.android.demos.healthbestie;


public class User {

    public String username, age, gender, email, profileImg, feedback, rateValue;


    public User () {

    }

    public User (String username, String age, String email, String gender) {
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.email = email;

    }


    public User (String email, String rateValue, String feedback) {
        this.email = email;
        this.rateValue = rateValue;
        this.feedback = feedback;
    }

    public String getUsername() {
        return username;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {return gender;}

    public String getEmail() {
        return email;
    }


    public String getProfileImg() {
        return profileImg;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getRateValue() {
        return rateValue;
    }
}
