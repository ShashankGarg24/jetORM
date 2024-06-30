package org.dummy;

import org.dummy.entities.Userr;
import org.jetORM.JetOrm;
import org.jetORM.exceptions.ClassMismatchException;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IllegalAccessException, ClassMismatchException, SQLException {
        JetOrm jetOrm = new JetOrm();
        jetOrm.configure("org.dummy");
        Userr userr = new Userr(1l, "Shrasti", "sg@gmail.com");
        jetOrm.save(Userr.class, userr);
    }
}
