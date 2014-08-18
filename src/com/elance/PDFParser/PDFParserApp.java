package com.elance.PDFParser;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFParserApp {

   public static HttpClient client = new HttpClient();
   public static int index = 1;
   public static String cookie = "";


   public static void main(String[] args) throws Exception {
      client.getParams().setSoTimeout(10000);
      client.getParams().setConnectionManagerTimeout(10000L);
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[0].replaceAll("\"", ""))));
      out.write("PDF URL,Keyword 1,Keyword 2,Keyword 3,Keyword 4,Keyword 5,Keyword 6,Keyword 7,Keyword 8,Keyword 9,Keyword 10,Sentence 1,Sentence 2,Sentence 3,Sentence 4,Sentence 5,Sentence 6,Sentence 7,Sentence 8,Sentence 9,Sentence 10,Sentence 11,Sentence 12,Sentence 13,Sentence 14,Sentence 15,Sentence 16,Sentence 17,Sentence 18,Sentence 19,Sentence 20,Sentence 21,Sentence 22,Sentence 23,Sentence 24,Sentence 25,Sentence 26,Sentence 27,Sentence 28,Sentence 29,Sentence 30\r\n");
      String folder = args[2].replaceAll("\"", "");
      if(!folder.endsWith("\\\\")) {
         folder = folder + "\\";
      }

      String line = "";
      File file1 = new File(args[1].replaceAll("\"", ""));
      FileInputStream fis1 = null;
      BufferedInputStream bis1 = null;
      DataInputStream dis1 = null;
      fis1 = new FileInputStream(file1);
      bis1 = new BufferedInputStream(fis1);
      dis1 = new DataInputStream(bis1);
      int counter = 0;

      while((line = dis1.readLine()) != null) {
         line = line.replaceAll("\"", "");
         ++counter;
         System.out.println("Searching for pdf #: " + counter);
         String[] temp = line.split("\\|");
         String url = temp[0].trim();
         String filename = url.substring(url.lastIndexOf("/") + 1).trim();
         downloadPhoto(url, folder, filename);
         PDFParser parser = new PDFParser(new FileInputStream(new File(folder + filename)));
         parser.parse();
         COSDocument cosDoc = parser.getDocument();
         PDFTextStripper pdfStripper = new PDFTextStripper();
         PDDocument pdDoc = new PDDocument(cosDoc);
         String parsedText = pdfStripper.getText(pdDoc);
         parsedText = parsedText.replaceAll("\\r", " ");
         parsedText = parsedText.replaceAll("\\\\r", " ");
         parsedText = parsedText.replaceAll("\\n", " ");
         parsedText = parsedText.replaceAll("\\\\n", " ");
         parsedText = parsedText.replaceAll("\\s+", " ").trim();
         String[] sentences = parsedText.split("(?<=[.?!])\\s+(?=[a-zA-Z])");
         String sentence = "";

         int k;
         for(k = 0; k < sentences.length; ++k) {
            for(int keywords = 1; keywords < temp.length; ++keywords) {
               if(sentences[k].toLowerCase().indexOf(temp[keywords].toLowerCase()) >= 0) {
                  sentence = sentence + "\"" + sentences[k].replaceAll("\"", "\"\"") + "\",";
                  break;
               }
            }
         }

         if(sentence.endsWith(",")) {
            sentence = sentence.substring(0, sentence.length() - 1).trim();
         }

         k = 1;

         String var21;
         for(var21 = ""; k < temp.length; ++k) {
            var21 = var21 + "\"" + temp[k].replaceAll("\"", "\"\"") + "\",";
         }

         while(k <= 10) {
            var21 = var21 + "\"\",";
            ++k;
         }

         if(var21.endsWith(",")) {
            var21 = var21.substring(0, var21.length() - 1).trim();
         }

         out.write("\"" + url.replaceAll("\"", "\"\"") + "\"," + var21 + "," + sentence + "\r\n");
         out.flush();
      }

      out.close();
   }

   public static String downloadPhoto(String address, String path, String fileName) {
      try {
         URL html = new URL(address);
         URI method = new URI(html.getProtocol(), html.getUserInfo(), html.getHost(), html.getPort(), html.getPath(), html.getQuery(), html.getRef());
         address = method.toASCIIString();
      } catch (Exception var16) {
         ;
      }

      String var19 = "";
      if(!path.endsWith("\\")) {
         path = path + "\\";
      }

      GetMethod var20 = null;

      for(int j = 0; j < 20; ++j) {
         try {
            var19 = "";
            var20 = new GetMethod(address);
            client.executeMethod(var20);
            byte[] ex = var20.getResponseBody();
            FileOutputStream e = new FileOutputStream(path + fileName);
            e.write(ex);
            e.close();
            break;
         } catch (Exception var17) {
            var17.printStackTrace();
            System.out.println(address);
            if(var17.getClass().toString().indexOf("UnknownHostException") >= 0) {
               try {
                  --j;
                  Thread.sleep(10000L);
               } catch (InterruptedException var15) {
                  var15.printStackTrace();
               }
            } else if(var17.getClass().toString().indexOf("FileNotFoundException") >= 0) {
               j = 111;
            } else {
               try {
                  Thread.sleep(10000L);
               } catch (InterruptedException var14) {
                  var14.printStackTrace();
               }
            }
         } finally {
            if(var20 != null) {
               var20.releaseConnection();
            }

         }
      }

      return var19;
   }

   public static String getHTML(String address) {
      String html = "";
      GetMethod method = null;

      for(int j = 0; j < 20; ++j) {
         try {
            html = "";
            method = new GetMethod(address);
            method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
            client.executeMethod(method);
            byte[] ex = method.getResponseBody();
            html = new String(ex);
            break;
         } catch (Exception var13) {
            var13.printStackTrace();
            System.out.println(address);
            if(var13.getClass().toString().indexOf("UnknownHostException") < 0 && var13.getClass().toString().indexOf("Connect") < 0 && var13.getClass().toString().indexOf("Socket") < 0) {
               if(var13.getClass().toString().indexOf("FileNotFoundException") >= 0) {
                  j = 111;
               } else {
                  try {
                     Thread.sleep(10000L);
                  } catch (InterruptedException var11) {
                     var11.printStackTrace();
                  }
               }
            } else {
               try {
                  --j;
                  Thread.sleep(10000L);
               } catch (InterruptedException var12) {
                  var12.printStackTrace();
               }
            }
         } finally {
            method.releaseConnection();
         }
      }

      return html;
   }
}
