package com.example.myapplication;

public class DatabaseHelper
{
    String name, phone, nid, occupation, first_shot, second_shot;

    //sign_up
    public DatabaseHelper(String name, String phone, String nid, String occupation, String first_shot, String second_shot) {
        this.name = name;
        this.phone = phone;
        this.nid = nid;
        this.occupation = occupation;
        this.first_shot = first_shot;
        this.second_shot = second_shot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getFirst_shot() {
        return first_shot;
    }

    public void setFirst_shot(String first_shot) {
        this.first_shot = first_shot;
    }

    public String getSecond_shot() {
        return second_shot;
    }

    public void setSecond_shot(String second_shot) {
        this.second_shot = second_shot;
    }
}

class DatabaseHelperApplyVaccine
{
    String vaccination_center, name_of_dose;

    public DatabaseHelperApplyVaccine(String vaccination_center, String name_of_dose) {
        this.vaccination_center = vaccination_center;
        this.name_of_dose = name_of_dose;
    }

    public String getVaccination_center() {
        return vaccination_center;
    }

    public void setVaccination_center(String vaccination_center) {
        this.vaccination_center = vaccination_center;
    }

    public String getName_of_dose() {
        return name_of_dose;
    }

    public void setName_of_dose(String name_of_dose) {
        this.name_of_dose = name_of_dose;
    }
}