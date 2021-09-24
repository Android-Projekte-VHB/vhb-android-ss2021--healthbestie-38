package de.ur.mi.android.demos.healthbestie;

public class User {

    public String username, fullName, email, phone, password, feedback, rateValue;

    public User () {

    }


    public User (String username, String fullName,String email, String phone) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }


    public User (String email, String rateValue, String feedback) {
        this.email = email;
        this.rateValue = rateValue;
        this.feedback = feedback;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}
