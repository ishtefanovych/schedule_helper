package com.ivan.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final String API_KEY = "84ea33bfb51407a29f0182cc19398b2b";
    private final String CITY = "Kyiv";

    public String getWeather() {
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&appid=" + API_KEY + "&units=metric&lang=ua";

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            double temp = root.path("main").path("temp").asDouble();
            String description = root.path("weather").get(0).path("description").asText();

            return "Погода в Києві: " + temp + " градусів, " + description;

        } catch (Exception e) {
            e.printStackTrace();
            return "Не вдалося отримати дані";
        }
    }
}