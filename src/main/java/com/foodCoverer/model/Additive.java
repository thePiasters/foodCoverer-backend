package com.foodCoverer.model;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Additive {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    private String symbol;
    private String additiveName;
    private String functionName;
    private SafetyLevel safetyLevel;
    private boolean euApproved;

    @Column(length = 1000)
    private String description;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAdditiveName() {
        return additiveName;
    }

    public void setAdditiveName(String additiveName) {
        this.additiveName = additiveName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionCategory) {
        this.functionName = functionCategory;
    }

    public SafetyLevel getSafetyLevel() {
        return safetyLevel;
    }

    public void setSafetyLevel(SafetyLevel safetyLevel) {
        this.safetyLevel = safetyLevel;
    }

    public boolean isEuApproved() {
        return euApproved;
    }

    public void setEuApproved(boolean euApproved) {
        this.euApproved = euApproved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
