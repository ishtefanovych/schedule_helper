package com.ivan.controller;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

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
            response.setChatId(String.valueOf(chatId));
            if (userText.equals("/start")) {
                response.setText("Привіт! Я твій бот. Скоро тут буде розклад пар.");
            } else if (userText.equals("Погода")) {
                String weatherData = weatherService.getWeatherForToday();
                response.setText(weatherData);
            } else {
                response.setText("Ти написав: " + userText);
            }

            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}