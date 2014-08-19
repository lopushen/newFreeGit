package com.elance.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Date: 16.08.14
 * Time: 21:50
 */
public class FileScraper {
    public static void saveFile(String year, String address, String dir) throws IOException {
        URL url = new URL(address);
        InputStream in = url.openStream();
        FileOutputStream fos = new FileOutputStream(new File(extractPageName(year, address, dir)));

        System.out.println("reading file...");
        int length = -1;
        byte[] buffer = new byte[1024];// buffer for portion of data from
        // connection
        while ((length = in.read(buffer)) > -1) {
            fos.write(buffer, 0, length);
        }
        fos.close();
        in.close();
        System.out.println("file was downloaded");
    }

    private static String extractPageName(String year, String url, String dir) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return dir + "/" + year + "_" + url.substring(url.lastIndexOf('/')+1);
    }


    public static void downloadReports(List<Row> rows, String dir) {
        for (Row row : rows) {
            try {
                saveFile(row.getYear(), row.getUrl(), dir);
            } catch (IOException e) {
                ErrorMessageShower.showError("Error downloading file from url " + row.getUrl());
            }
        }
    }
}
