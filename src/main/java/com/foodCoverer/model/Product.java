package com.foodCoverer.model;



import com.sun.istack.NotNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;
import javax.validation.constraints.*;

@Entity
public class Product {

    @Id
    @Positive(message = "The value must be positive")
    private Long productId;

    boolean isVerified;

    @Size(max = 50)
    @NotNull
    @NotBlank
    @NotEmpty
    String productName;

    @OneToOne(cascade = CascadeType.REMOVE)
    Image image;

    @ManyToOne(cascade = CascadeType.DETACH)
    User user;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    NutritionData nutritionData;

    @ManyToOne(cascade = CascadeType.DETACH)
    Brand brand;

    @Nullable
    @ManyToMany(fetch = FetchType.EAGER,  cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    Set<Additive> additives;

    @ManyToOne
    Subcategory subcategory;

    @Nullable
    @ManyToMany(fetch = FetchType.EAGER,  cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    Set<Allergen> allergens;

    @Nullable
    @ManyToMany(fetch = FetchType.EAGER,  cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    Set<Tag> tags;

    char grade;

    @Column(length = 1000)
    @Size(max = 1000)
    String ingredients;

    double quantity;


    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public NutritionData getNutritionData() {
        return nutritionData;
    }

    public void setNutritionData(NutritionData nutritionData) {
        this.nutritionData = nutritionData;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Allergen> getAllergens() {
        return allergens;
    }

    public void setAllergens(Set<Allergen> allergens) {
        this.allergens = allergens;
    }

    public void setGrade(char nutriscoreGrade) {
        this.grade = nutriscoreGrade;
    }

    public char getGrade() {
        return grade;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredientsText) {
        this.ingredients = ingredientsText;
    }

    public Set<Additive> getAdditives() {
        return additives;
    }

    public void setAdditives(Set<Additive> additives) {
        this.additives = additives;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String name) {
        this.productName = name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }


    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
