package com.elance.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesReader {
    private Properties prop;
    private static PropertiesReader propertiesReader;

    public static PropertiesReader getInstance() {
        if (propertiesReader == null) {
            propertiesReader = new PropertiesReader();
        }
        return propertiesReader;
    }

    public String getProperty(String name) {
        return prop.getProperty(name);
    }

    public void updateProperty(String name, String value) {
        try (FileOutputStream out = new FileOutputStream(ServiceConstants.PROPERTIES_PATH)) {

            prop.setProperty(name, value);
            prop.store(out, null);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        readProperties();
    }

    private void readProperties() {
        prop = new Properties();
        try (InputStream input = new FileInputStream(ServiceConstants.PROPERTIES_PATH);) {

            if (input == null) {
                ErrorMessageShower.showError("No properties specified");
                return;
            }
            prop.load(input);
        } catch (IOException ex) {
            ErrorMessageShower.showError("No properties file found");
        }
    }
}
