package com.foodCoverer.model;

import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class User implements Serializable {
    private static final long serialVersionUID = -8241440213086388009L;

    @Id
    private String id;

    @NonNull
    @Column(length = 45)
    String name;

    @NonNull
    @Column(unique = true)
    String email;

    Role role;

    String userImageUrl;

    public User(String id, @NonNull String name, @NonNull String email, String pictureUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = Role.USER;
        this.userImageUrl = pictureUrl;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String pictureUrl) {
        this.userImageUrl = pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                name.equals(user.name) &&
                email.equals(user.email) &&
                role == user.role &&
                Objects.equals(userImageUrl, user.userImageUrl);
    }

}
