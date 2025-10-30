package com.legalsahayak.app.model;

public class LawyerProfile {
    private final String name;
    private final String specialization;
    private final String experience;
    private final String phone;
    private final String email;

    public LawyerProfile(String name, String spec, String exp, String ph, String mail) {
        this.name = name;
        this.specialization = spec;
        this.experience = exp;
        this.phone = ph;
        this.email = mail;
    }

    @Override
    public String toString() {
        return "  Name: " + name + "\n" +
               "  Specialization: " + specialization + "\n" +
               "  Experience Note: " + experience + "\n" +
               "  Phone Number: " + phone + "\n" +
               "  Email: " + email;
    }
}