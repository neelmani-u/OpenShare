package com.openshare.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("users")
public class User {
    @Id
    private String id;
    private String username;
    private String passwordHash;

    // getters/setters
    public String getId(){return id;}
    public void setId(String id){this.id = id;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public String getPasswordHash(){return passwordHash;}
    public void setPasswordHash(String passwordHash){this.passwordHash = passwordHash;}
}
