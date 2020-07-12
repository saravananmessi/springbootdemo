package com.howtodoinjava.rest.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SendMessage {
    public static void main(String args[]) {
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=979818293&text=saa";

        //Add Telegram token (given Token is fake)
        String apiToken = "1217258476:AAH8IAYb50LC2JHsnix-7v7FNjMECK1xd0k";
      
        //Add chatId (given chatId is fake)
        String chatId = "979818293";
        String text = "Hello world!";

        urlString = String.format(urlString, apiToken, chatId, text);

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            
          ///////////////////////////////////////////////////////////////////////////////
          ///////////////////////////////////////////////////////////////////////////////
          //Additoinal Code is HERE
          ///////////////////////////////////////////////////////////////////////////////
            //getting text, we can set it to any TextView
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine = "";
                StringBuilder sb = new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                //You can set this String to any TextView
                String response = sb.toString();
                System.out.println("Hiiiiii");
          ///////////////////////////////////////////////////////////////////////////////
          ///////////////////////////////////////////////////////////////////////////////
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
