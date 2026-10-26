package com.ivan.controller;

import com.ivan.service.ScheduleService;
import com.ivan.service.WeatherService;
import com.ivan.util.KeyboardUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private WeatherService weatherService = new WeatherService();
    private ScheduleService scheduleService = new ScheduleService();

    public TelegramBot() {
        super("8955208299:AAGRKATkPOPaOq3B31AUedNdrUN3hpEV1f4");
    }

    @Override
    public String getBotUsername() {
        return "ShtefanScheduleBot";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String userText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            SendMessage response = new SendMessage();
            response.setParseMode("HTML");
            response.setChatId(String.valueOf(chatId));

            if (userText.equals("/start")) {
                response.setText("Привіт! Я твій бот, якщо тебе цікавить темепратура в Києві просто напиши \"Погода\", а якщо тебе цікавить розклад просто напиши \"Розклад\" ");
            } else if (userText.equalsIgnoreCase("погода")) {
                String weatherData = weatherService.getWeather();
                response.setText(weatherData);
            } else if (userText.equalsIgnoreCase("розклад на сьогодні") || userText.equalsIgnoreCase("розклад")) {
                String scheduleData = scheduleService.getTodaySchedule();
                response.setText(scheduleData);
            } else if (userText.equalsIgnoreCase("розклад на завтра")) {
                String scheduleData = scheduleService.getTomorrowSchedule();
                response.setText(scheduleData);
            } else {
                response.setText("Ти написав: " + userText);
            }

            KeyboardUtils.attachMainMenu(response);

            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}