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
}
