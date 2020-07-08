package com.example.testlogin.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class    User {

    @SerializedName("response")
    private String response;

    @SerializedName("manager_id")
    private int manager_id;

    @SerializedName("manager_name")
    private String manager_name;

    @SerializedName("manager_email")
    private String manager_email;

    @SerializedName("manager_phone")
    private String manager_phone;

    @SerializedName("aid")
    @Expose
    private int  aid;

    @SerializedName("aname")
    @Expose
    private String aname;

    @SerializedName("aemail")
    private String aemail;

    @SerializedName("aowner")
    private String aowner;

    @SerializedName("alocation")
    private String alocation;

    @SerializedName("aphoneno")
    private String aphoneno;

    @SerializedName("uid")
    @Expose
    private int  uid;

    @SerializedName("uname")
    private String uname;

    @SerializedName("uemail")
    private String uemail;

    @SerializedName("uphoneno")
    private String uphoneno;

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("phoneno")
    private String phoneno;

    @SerializedName("apassword")
    private String apassword;

    @SerializedName("spassword")
    private String spassword;

    @SerializedName("Status")
    private boolean Status;

    @SerializedName("total")
    @Expose
    public int total;

    @SerializedName("pid")
    @Expose
    private int pid;

    @SerializedName("contactperson")
    private String contactperson;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("description")
    private String description;

    @SerializedName("bedno")
    private String  bedno;

    public User(String aname) {
        this.aname = aname;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getAowner() {
        return aowner;
    }

    public String getAlocation() {
        return alocation;
    }

    public String getResponse() {
        return response;
    }


    public String getuEmail() {
        return uemail;
    }

    public String getuName() {
        return uname;
    }



    public String getmanagerEmail() {
        return manager_email;
    }

    public String getmanagerName() {
        return manager_name;
    }


    // admin
    public String getaEmail() {
        return aemail;
    }

    public String getaName() {
        return aname;
    }

    public int getManager_id() {
        return manager_id;
    }

    public int getaid() {
        return aid;
    }

    public int getUid() {
        return uid;
    }

    public int getsid() {
        return id;
    }

    public String getsEmail() {
        return email;
    }

    public String getsName() {
        return name;
    }

    //


    public String getManager_phone() {
        return manager_phone;
    }

    public String getAphoneno() {
        return aphoneno;
    }

    public String getUphoneno() {
        return uphoneno;
    }

    public String getSphoneno() {
        return phoneno;
    }

    public String getaPassword() {
        return apassword;
    }

    public String getsPassword() {
        return spassword;

    }

    public int getTotal() {
        return total;
    }

    public int getpid() {
        return pid;
    }

    public String getUbedno() {
        return bedno;
    }
}
