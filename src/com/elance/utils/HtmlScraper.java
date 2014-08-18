package com.elance.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Date: 16.08.14
 * Time: 21:50
 */
public class HtmlScraper {
    public String saveHtml(String address) throws IOException {
        URL url = new URL(address);
        String fileName = extractPageName(address);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            try {
                writer.write(inputLine);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        in.close();
        writer.close();
        return fileName;
    }

    private String extractPageName(String url) {
        if (url.length()>50){
            url = url.substring(0, 46)+"...".replaceAll("//","");
        }
        return "a.html";
    }

    public static void main(String[] args) throws IOException {
        HtmlScraper scraper = new HtmlScraper();
        scraper.saveHtml("http://vk.com/");
    }
}
