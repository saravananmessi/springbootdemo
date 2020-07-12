//package com.howtodoinjava.rest.controller;
//
//import java.io.IOException;
//
////import org.apache.commons.httpclient.HttpClientError;
////import org.apache.http.HttpRequest;
////import org.apache.http.HttpResponse;
////import org.apache.http.client.HttpClient;
//
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.time.Duration;
//
//import javax.ws.rs.core.UriBuilder;
//
//public class TelegramNotifier {
//
//    private static final String CHAT_ID = "-445824692";
//    private static final String TOKEN = "1217258476:AAH8IAYb50LC2JHsnix-7v7FNjMECK1xd0k";
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//
//        String message = "Gud Mrng!!!!!";
//
//        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).version(HttpClient.Version.HTTP_2).build();
//
//        UriBuilder builder = UriBuilder
//                .fromUri("https://api.telegram.org")
//                .path("/{token}/sendMessage")
//                .queryParam("chat_id", CHAT_ID)
//                .queryParam("text", message);
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .GET()
//                .uri(builder.build("bot" + TOKEN))
//                .timeout(Duration.ofSeconds(5))
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        System.out.println(response.statusCode());
//        System.out.println(response.body());
//    }
//}