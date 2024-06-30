package org.dummy;

import org.jetORM.JetOrm;

public class Main {

    public static void main(String[] args) {
        JetOrm jetOrm = new JetOrm();
        jetOrm.configure("org.dummy");
    }
}
