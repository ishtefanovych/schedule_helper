package com.ivan.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;

@Service
public class ScheduleService {

    private final String SCHEDULE_URL = "https://api.campus.kpi.ua/schedule/lessons?groupId=4351";

    public String getTodaySchedule() {
        return fetchSchedule(LocalDate.now(), "сьогодні");
    }

    public String getTomorrowSchedule() {
        return fetchSchedule(LocalDate.now().plusDays(1), "завтра");
    }

    private String fetchSchedule(LocalDate targetDate, String dayLabel) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(SCHEDULE_URL, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            DayOfWeek targetDayOfWeek = targetDate.getDayOfWeek();
            String targetDayName = getUkrainianDayName(targetDayOfWeek);

            if (List.of("Нд").contains(targetDayName)) {
                return "На " + dayLabel + " (" + targetDayName + ") пар немає";
            }

            int weekOfYear = targetDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            String weekNodeName = (weekOfYear % 2 != 0) ? "scheduleFirstWeek" : "scheduleSecondWeek";
            String weekPrintName = (weekOfYear % 2 != 0) ? "Перший тиждень" : "Другий тиждень";

            JsonNode targetWeek = root.path(weekNodeName);

            JsonNode daySchedule = null;
            for (JsonNode dayNode : targetWeek) {
                if (dayNode.path("day").asText().equals(targetDayName)) {
                    daySchedule = dayNode;
                    break;
                }
            }

            if (daySchedule == null || daySchedule.path("pairs").isEmpty()) {
                return "На " + dayLabel + " (" + targetDayName + ") пар немає! Вихідний! 🎉";
            }

            JsonNode pairs = daySchedule.path("pairs");
            StringBuilder result = new StringBuilder();
            result.append("📅 Розклад на ").append(dayLabel).append(" (").append(targetDayName).append(", ").append(weekPrintName).append("):\n\n");

            String lastTime = "";
            for (JsonNode pair : pairs) {
                String time = pair.path("time").asText();
                String name = pair.path("name").asText();
                String type = pair.path("type").asText();
                String lecturer = pair.path("lecturer").path("name").asText();
                String shortTime = time.substring(0, 5);

                if (!time.equals(lastTime)) {
                    if (!lastTime.isEmpty()) {
                        result.append("──────────────────────────\n");
                    }
                    String pairName = getPairName(shortTime);
                    result.append("🕒 <b>").append(pairName).append(" (").append(shortTime).append(")</b>\n");
                    lastTime = time;
                }

                result.append(" ├ 📚 ").append(name).append(" (").append(type).append(")\n")
                        .append(" └ 👨‍🏫 ").append(lecturer).append("\n\n");
            }

            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Помилка";
        }
    }

    private String getUkrainianDayName(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> "Пн";
            case TUESDAY -> "Вв";
            case WEDNESDAY -> "Ср";
            case THURSDAY -> "Чт";
            case FRIDAY -> "Пт";
            case SATURDAY -> "Сб";
            case SUNDAY -> "Нд";
        };
    }

    private String getPairName(String time) {
        if (time.startsWith("08:30")) return "Перша пара";
        if (time.startsWith("10:25")) return "Друга пара";
        if (time.startsWith("12:20")) return "Третя пара";
        if (time.startsWith("14:15")) return "Четверта пара";
        if (time.startsWith("16:10")) return "П'ята пара";
        if (time.startsWith("18:05")) return "Шоста пара";
        return "Пара";
    }
}