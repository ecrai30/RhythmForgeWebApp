package com.example.d308vacationsapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")

public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionId;
    private String excursionName;
    private double price;
    private int vacationId;

    public Excursion(int excursionId, String excursionName, double price, int vacationId) {
        this.excursionId = excursionId;
        this.excursionName = excursionName;
        this.price = price;
        this.vacationId = vacationId;
    }

    public int getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(int excursionId) {
        this.excursionId = excursionId;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }
}

