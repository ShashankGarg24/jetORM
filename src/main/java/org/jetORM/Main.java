package org.jetORM;

import org.jetORM.schema.TableGenerator;

public class Main {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        TableGenerator tableGenerator = new TableGenerator();
        tableGenerator.generateDatabaseTablesFromClasses();
    }
}