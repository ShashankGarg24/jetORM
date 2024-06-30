package org.jetORM.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    private final Properties properties;

    public PropertyLoader(String path) {
        properties = new Properties();
        load(path);
    }

    private void load(String path) {
        try (InputStream inputStream = new FileInputStream(path)){
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key){
        return properties.getProperty(key);
    }
}
