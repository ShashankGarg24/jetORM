package org.dummy.entities;

import org.jetORM.annotations.Column;
import org.jetORM.annotations.Entity;
import org.jetORM.annotations.Id;

@Entity
public class Userr {

    @Id
    private Long userId;
    private String userName;
    private String userEmail;

    public Userr(){}

    public Userr(Long userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "Userr{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
