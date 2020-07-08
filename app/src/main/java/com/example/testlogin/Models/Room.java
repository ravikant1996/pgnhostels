package com.example.testlogin.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room {

    @SerializedName("response")
    public String response;

    @SerializedName("addRoom")
    public String addRoom;

    @SerializedName("total")
    @Expose
    public int total;

    @SerializedName("images")
    @Expose
    private String images;


    @SerializedName("no_of_bed")
    @Expose
    public int no_of_bed;

    @SerializedName("room_no")
    public String room_no;


    @SerializedName("aid")
    @Expose
    public int aid;

    @SerializedName("bid")
    @Expose
    public int bid;

    @SerializedName("bed_no")
    @Expose
    public int bed_no;

    @SerializedName("vacent")
    @Expose
    public int vacent;

    @SerializedName("alloted")
    @Expose
    public int alloted;

    @SerializedName("rid")
    @Expose
    private int rid;
    @SerializedName("stafftype")
    private String stafftype;

    public String getPicture() {
       return images;
    }
    public Room(String images) {
        this.images = images;
    }

    public int getBed_no() {
        return bed_no;
    }

    public void setBed_no(int bed_no) {
        this.bed_no = bed_no;
    }

    @SerializedName("Status")
    public String Status;

    public void setResponse(String response) {
        this.response = response;
    }

    public String getAddRoom() {
        return addRoom;
    }

    public void setAddRoom(String addRoom) {
        this.addRoom = addRoom;
    }

    public int getNo_of_bed() {
        return no_of_bed;
    }

    public void setNo_of_bed(int no_of_bed) {
        this.no_of_bed = no_of_bed;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getResponse() {
        return response;
    }

    public String getRoomNo() {
        return room_no;
    }

    public int getRoomId() {
        return rid;
    }

    public int getBid() {
        return bid;
    }

    public int getNo_of_room() {
        return total;
    }

    public int getAllotedBed() {
        return alloted;
    }

    public int getVacentBed() {
        return vacent;
    }

    public String getStafftype() {
        return stafftype;
    }
}
