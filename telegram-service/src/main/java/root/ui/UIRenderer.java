package root.ui;

import client.UserDataClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import root.utils.LoadForecastTelegramBot;
import root.utils.UserData;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UIRenderer {

    private final UserDataClient userDataClient;


    public UIRenderer(UserDataClient userDataClient) {
        this.userDataClient = userDataClient;
    }
    // Отправка главного меню
    public void sendMainMenu(long chatId, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите действие:\n1. Ручной ввод\n2. Пример");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Ручной ввод");
        row1.add("Пример");

        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    // Отправка меню для выбора узла
    public void sendNodeSelectionMenu(long chatId, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите вид транспорта:\n1. МЦК\n2. МЦД\n3. Метро");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("МЦК");
        row1.add("МЦД");
        row1.add("Метро");

        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMCCStationSelectionMenu(long chatId, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите станцию МЦК:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Список станций МЦК
        String[] stations = {
                "Окружная", "Владыкино", "Ботанический Сад", "Ростокино", "Белокаменная",
                "Бульвар Рокоссовского", "Локомотив", "Измайлово", "Соколиная Гора",
                "Шоссе Энтузиастов", "Андроновка", "Нижегородская", "Новохохловская",
                "Угрешская", "Дубровка", "Автозаводская", "ЗИЛ", "Верхние Котлы",
                "Крымская", "Площадь Гагарина", "Лужники", "Кутузовская", "Москва-Сити",
                "Шелепиха", "Хорошёво", "Зорге", "Панфиловская", "Стрешнево", "Балтийская",
                "Коптево", "Лихоборы"
        };

        // Добавляем станции на клавиатуру по несколько станций в ряд
        for (int i = 0; i < stations.length; i += 2) {
            KeyboardRow row = new KeyboardRow();
            row.add(stations[i]);
            if (i + 1 < stations.length) {
                row.add(stations[i + 1]);
            }
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMetroLineSelectionMenu(long chatId, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите линию метро:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Линии метро
        String[] lines = {
                "Сокольническая линия (красная)", "Замоскворецкая линия (зелёная)", "Арбатско-Покровская линия (синяя)",
                "Филёвская линия (голубая)", "Кольцевая линия (коричневая)", "Калужско-Рижская линия (оранжевая)",
                "Таганско-Краснопресненская линия (фиолетовая)", "Калининская линия (жёлтая)",
                "Серпуховско-Тимирязевская линия (серая)", "Люблинско-Дмитровская линия (салатовая)",
                "Большая кольцевая линия (бирюзовая)"
        };

        // Добавляем линии метро по несколько в строке
        for (int i = 0; i < lines.length; i += 2) {
            KeyboardRow row = new KeyboardRow();
            row.add(lines[i]);
            if (i + 1 < lines.length) {
                row.add(lines[i + 1]);
            }
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendResultMessage(long chatId, String result, LoadForecastTelegramBot bot) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(result);
        bot.execute(message);
    }

    public void sendMetroStationSelectionMenu(long chatId, String line, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(line + ", выберите станцию:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Получаем станции для выбранной линии
        Map<String, String[]> metroLines = bot.getMetroLines();
        String[] stations = metroLines.get(line);

        // Добавляем станции по несколько в строке
        for (int i = 0; i < stations.length; i += 2) {
            KeyboardRow row = new KeyboardRow();
            row.add(stations[i]);
            if (i + 1 < stations.length) {
                row.add(stations[i + 1]);
            }
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMCDLineSelectionMenu(long chatId, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите линию МЦД:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Линии МЦД
        String[] lines = {
                "МЦД-1 «Белорусско-Савеловский диаметр»", "МЦД-2 «Курско-Рижский диаметр»",
                "МЦД-3 «Ленинградско-Казанский диаметр»", "МЦД-4 «Калужско-Нижегородский диаметр»"
        };

        // Добавляем линии МЦД по несколько в строке
        for (int i = 0; i < lines.length; i += 2) {
            KeyboardRow row = new KeyboardRow();
            row.add(lines[i]);
            if (i + 1 < lines.length) {
                row.add(lines[i + 1]);
            }
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMCDStationSelectionMenu(long chatId, String line, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите станцию на " + line + ":");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Получаем станции для выбранной линии
        Map<String, String[]> mcdLines = bot.getMCDLines();
        String[] stations = mcdLines.get(line);

        // Добавляем станции по несколько в строке
        for (int i = 0; i < stations.length; i += 2) {
            KeyboardRow row = new KeyboardRow();
            row.add(stations[i]);
            if (i + 1 < stations.length) {
                row.add(stations[i + 1]);
            }
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Отправка меню для выбора типа застройки
    public void sendBuildingTypeMenu(long chatId, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите тип застройки:\n1. Жилая\n2. Коммерческая");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Жилая");
        row1.add("Коммерческая");

        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void askForFloorArea(long chatId, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Введите поэтажную площадь в квадратных метрах:");

        // Удаление клавиатуры
        ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
        keyboardRemove.setRemoveKeyboard(true);  // Устанавливаем удаление клавиатуры
        message.setReplyMarkup(keyboardRemove);  // Применяем удаление клавиатуры

        try {
            bot.execute(message);  // Отправляем запрос на ввод площади
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    // Запрос на ввод даты и времени
    public void askForDateTime(long chatId, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Введите дату и время в формате: ДД.ММ.ГГГГ ЧЧ:ММ");

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendImage(long chatId, LoadForecastTelegramBot bot, String relativePath) {
        // Используем getResourceAsStream для получения файла из ресурсов
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(relativePath);

        if (inputStream != null) {
            try {
                // Конвертируем InputStream в File для передачи в InputFile
                SendPhoto sendPhotoRequest = new SendPhoto();
                sendPhotoRequest.setChatId(String.valueOf(chatId));
                InputFile inputFile = new InputFile(inputStream, relativePath);
                sendPhotoRequest.setPhoto(inputFile);

                // Отправляем фотографию
                bot.execute(sendPhotoRequest);
            } catch (TelegramApiException e) {
                e.printStackTrace();  // Обрабатываем исключение
            }
        } else {
            System.out.println("Файл не найден в ресурсах: " + relativePath);
        }
    }

    public void sendRoadPartMenu(long chatId, LoadForecastTelegramBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите часть дорожного полотна:");

        // Установка клавиатуры с опциями
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("1. метро Римская");
        row1.add("2. метро Площадь Ильича");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("3. МЦД-4. Серп и молот");
        row2.add("4. ул. Сергея Радонежского");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("5. ул. Золоторожский вал");
        row3.add("6. Гжельский переулок");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    // Метод для отображения конечного результата
    public void showFinalResult(long chatId, LoadForecastTelegramBot bot, UserData data) throws TelegramApiException {

        StringBuilder result = new StringBuilder("Результат вашего выбора:\n");

        if (data.transport() != null) {
            result.append("Вид транспорта: ").append(data.transport()).append("\n");
        }

        if (data.line() != null) {
            result.append("Линия: ").append(data.line()).append("\n");
        }

        if (data.station() != null) {
            result.append("Станция: ").append(data.station()).append("\n");
        }

        if (data.roadPart() != null) {
            result.append("Часть дорожного полотна: ").append(data.roadPart()).append("\n");
        }

        if (data.buildingType() != null) {
            result.append("Тип застройки: ").append(data.buildingType()).append("\n");
        }

        if (data.floorArea() != null) {
            result.append("Поэтажная площадь: ").append(data.floorArea()).append(" м²\n");
        }

        if (data.dateTime() != null) {
            result.append("Дата и время: ").append(data.dateTime()).append("\n");
        }

        // Отправляем данные на сервер и получаем результат
        try {
            // Отправляем запрос на сервер через userDataClient
            String response = userDataClient.sendUserData(data);

            // Парсим JSON-ответ с сервера
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> stationData = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});

            // Ищем пассажиропоток для выбранной станции
            for (Map<String, Object> station : stationData) {
                String stationName = (String) station.get("name");
                if (stationName.equals(data.station())) {
                    int passengerFlow = (int) station.get("passengerFlow");
                    result.append("\nПассажиропоток для станции ").append(stationName)
                            .append(": ").append(passengerFlow).append(" человек.\n");
                    break;
                }
            }

        } catch (Exception e) {
            result.append("\n").append("Ошибка при отправке данных на сервер: ").append(e.getMessage()).append("\n");
        }

        // Отправляем результат пользователю
        sendResultMessage(chatId, result.toString(), bot);

        // Показываем основное меню заново
        sendMainMenu(chatId, bot);
    }
}

