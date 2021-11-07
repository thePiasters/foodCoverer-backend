package com.foodCoverer.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class NutritionData {


    @Id
    @Type(type = "uuid-char")
    private UUID id;

    private double energyKcal;
    private double fat;
    private double saturatedFat;
    private double carbohydrates;
    private double sugars;
    private double proteins;
    private double salt;
    private double sodium;
    private double fiber;
    private double fruitVegetablesNuts;


    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fibre) {
        this.fiber = fibre;
    }

    public double getFruitVegetablesNuts() {
        return fruitVegetablesNuts;
    }

    public void setFruitVegetablesNuts(double fruitVegetablesNuts) {
        this.fruitVegetablesNuts = fruitVegetablesNuts;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getEnergyKcal() {
        return energyKcal;
    }

    public void setEnergyKcal(double energy) {
        this.energyKcal = energy;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getSugars() {
        return sugars;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getSalt() {
        return salt;
    }

    public void setSalt(double salt) {
        this.salt = salt;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    @Override
    public String toString() {
        return "NutritionData{" +
                "id=" + id +
                ", energyKcal=" + energyKcal +
                ", fat=" + fat +
                ", saturatedFat=" + saturatedFat +
                ", carbohydrates=" + carbohydrates +
                ", sugars=" + sugars +
                ", proteins=" + proteins +
                ", salt=" + salt +
                ", sodium=" + sodium +
                ", fiber=" + fiber +
                ", fruitVegetablesNuts=" + fruitVegetablesNuts +
                '}';
    }
}
