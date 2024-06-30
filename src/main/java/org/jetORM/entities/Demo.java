package org.jetORM.entities;

import org.jetORM.annotations.Entity;
import org.jetORM.annotations.Id;

import java.math.BigDecimal;

@Entity
public class Demo {

    @Id
    private int id;
}
