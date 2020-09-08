package com.estazo.project.seeable.app;

public class UserHelperClass2 {
    String name, surname, phone, namehelper, phonehelper;

    public UserHelperClass2(String name, String surname, String phone, String namehelper, String phonehelper) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.namehelper = namehelper;
        this.phonehelper = phonehelper;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String setSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getNamehelper() {
        return namehelper;
    }
    public void setNamehelper(String namehelper) {
        this.namehelper = namehelper;
    }
    public String getPhonehelper() {
        return phonehelper;
    }
    public void setPhonehelper(String phonehelper) {
        this.phonehelper = phonehelper;
    }
}
