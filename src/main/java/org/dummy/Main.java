package org.dummy;

import org.dummy.entities.Userr;
import org.jetORM.JetOrm;
import org.jetORM.JetOrmImpl;
import org.jetORM.exceptions.ClassMismatchException;
import org.jetORM.exceptions.PrimaryKeyNotPresentException;

public class Main {

    public static void main(String[] args) throws ClassMismatchException, PrimaryKeyNotPresentException {
        JetOrm jetOrm = new JetOrmImpl();
        jetOrm.configure("org.dummy");
        Userr userr = new Userr(1l, "Shashank", "sg@gmail.com");
        jetOrm.save(Userr.class, userr);
        System.out.printf(jetOrm.getById(Userr.class, 1l).toString());
        jetOrm.deleteById(Userr.class, 1l);
    }
}
