package com.example.d308vacationsapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationId;
    private String vacationName;
    private double price;
    private String hotel;
    private String startDate;
    private String endDate;


    public Vacation(int vacationId, String vacationName, double price,String hotel, String startDate, String endDate) {
        this.vacationId = vacationId;
        this.vacationName = vacationName;
        this.price = price;
        this.hotel=hotel;
        this.startDate=startDate;
        this.endDate=endDate;

    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString(){
        return vacationName;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }
    // Method to return start date as String Change if doesnt work!!!!!
    public String getFormattedStartDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        return sdf.format(startDate);
    }

    // Method to return end date as String
    public String getFormattedEndDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        return sdf.format(endDate);
    }

}
