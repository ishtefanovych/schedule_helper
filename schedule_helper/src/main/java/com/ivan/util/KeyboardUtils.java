package com.ivan.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardUtils {

    // Метод static дозволяє викликати його без створення об'єкта класу
    public static void attachMainMenu(SendMessage message) {
        // Створюємо головний об'єкт клавіатури
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Робить кнопки за розміром тексту, а не на пів екрана
        keyboardMarkup.setOneTimeKeyboard(false); // Клавіатура не буде ховатися після натискання

        // Створюємо список, який буде тримати всі наші рядки кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Створюємо перший рядок кнопок
        KeyboardRow row = new KeyboardRow();
        row.add("Розклад");
        row.add("Погода");

        // Додаємо рядок до клавіатури
        keyboard.add(row);

        // Зберігаємо структуру та прикріплюємо до повідомлення
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
    }
}