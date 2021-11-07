package com.foodCoverer.model;

import com.sun.istack.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;


@Entity
public class Brand {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @NotNull
    @NotEmpty(message = "Brand name can not be empty")
    @NotBlank(message = "Brand name can not be blank")
    @Size(min = 1, max = 100, message = "Brand name must contain at least one character and cannot b longer than 100")
    private String name;


    @ColumnDefault("false")
    private boolean isVerified;

    @ManyToOne(cascade = CascadeType.DETACH)
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }
}




