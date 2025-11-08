package com.legalsahayak.app.model;

public class LawyerProfile {
    private final int id; 
    private final String name;
    private final String specialization;
    private final String experience;
    private final String phone;
    private final String email;

    public LawyerProfile(int id, String name, String spec, 
                         String exp, String ph, String mail) {
        this.id = id;
        this.name = name;
        this.specialization = spec;
        this.experience = exp;
        this.phone = ph;
        this.email = mail;
    }
    
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "  ID: " + id + "\n" +
               "  Name: " + name + "\n" +
               "  Specialization: " + specialization + "\n" +
               "  Experience Note: " + experience + "\n" +
               "  Phone Number: " + phone + "\n" +
               "  Email: " + email;
    }
}