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
            String emoji = getWeatherEmoji(description);

            return "Погода в Києві: " + emoji + " " + description + ", температура: " + temp + "°C";
        } catch (Exception e) {
            e.printStackTrace();
            return "Не вдалося отримати дані";
        }
    }
    private String getWeatherEmoji(String description) {
        String lowerDesc = description.toLowerCase();

        if (lowerDesc.contains("ясно") || lowerDesc.contains("clear")) {
            return "☀️";
        } else if (lowerDesc.contains("хмар") || lowerDesc.contains("cloud")) {
            return "☁️";
        } else if (lowerDesc.contains("дощ") || lowerDesc.contains("rain")) {
            return "🌧️";
        } else if (lowerDesc.contains("сніг") || lowerDesc.contains("snow")) {
            return "❄️";
        } else if (lowerDesc.contains("гроз") || lowerDesc.contains("thunder")) {
            return "⛈️";
        } else if (lowerDesc.contains("туман") || lowerDesc.contains("mist")) {
            return "🌫️";
        } else {
            return "🌡️"; // Стандартний емодзі, якщо стан не розпізнано
        }
    }
}