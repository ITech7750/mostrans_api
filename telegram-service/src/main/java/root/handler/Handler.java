package root.handler;

import java.util.Map;

import client.UserDataClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import root.utils.LoadForecastTelegramBot;
import root.utils.UserData;
import root.ui.UIRenderer;

public class Handler {

    private static final String STATE_WAITING_FOR_NODE = "waiting_for_node";
    private static final String STATE_WAITING_FOR_MCC_STATION = "waiting_for_mcc_station";
    private static final String STATE_WAITING_FOR_METRO_LINE = "waiting_for_metro_line";
    private static final String STATE_WAITING_FOR_METRO_STATION = "waiting_for_metro_station";
    private static final String STATE_WAITING_FOR_MCD_LINE = "waiting_for_mcd_line";
    private static final String STATE_WAITING_FOR_MCD_STATION = "waiting_for_mcd_station";
    private static final String STATE_WAITING_FOR_BUILDING_TYPE = "waiting_for_building_type";
    private static final String STATE_WAITING_FOR_FLOOR_AREA = "waiting_for_floor_area";
    private static final String STATE_WAITING_FOR_DATE_TIME = "waiting_for_date_time";
    private static final String STATE_WAITING_FOR_ROAD_PART = "waiting_for_road_part";

    private final UIRenderer uiRenderer;  // Для отправки сообщений

    // Constructor that accepts UserDataClient
    public Handler(UserDataClient userDataClient) {
        this.uiRenderer = new UIRenderer(userDataClient);  // Pass UserDataClient to UIRenderer
    }

    // Обработка первого сообщения
    public void handleInitialMessage(long chatId, String messageText, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) {
        if (messageText.equals("Ручной ввод")) {
            userStates.put(chatId, STATE_WAITING_FOR_NODE);  // Устанавливаем состояние для выбора узла
            uiRenderer.sendNodeSelectionMenu(chatId, bot);  // Отправляем меню для выбора узла
        } else if (messageText.equals("Пример")) {
            // Отправляем изображение из ресурсов
            String imagePath = "NHBotExample.png";  // Указываем относительный путь
            uiRenderer.sendImage(chatId, bot, imagePath);

            // Переходим к следующему состоянию, ожидаем выбор части дорожного полотна
            userStates.put(chatId, STATE_WAITING_FOR_ROAD_PART);
            uiRenderer.sendRoadPartMenu(chatId, bot);  // Показываем меню выбора части дорожного полотна
        } else {
            uiRenderer.sendMainMenu(chatId, bot);  // Возвращаем пользователя в главное меню
        }
    }



    // Обработка состояний
    public void handleState(long chatId, String messageText, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) throws TelegramApiException {
        String currentState = userStates.get(chatId);

        switch (currentState) {
            case STATE_WAITING_FOR_NODE:
                handleNodeSelection(chatId, messageText, bot, userStates, userData);
                break;
            case STATE_WAITING_FOR_BUILDING_TYPE:
                handleBuildingTypeSelection(chatId, messageText, bot, userStates, userData);
                break;
            case STATE_WAITING_FOR_DATE_TIME:
                handleDateTimeInput(chatId, messageText, bot, userStates, userData);
                break;
            case STATE_WAITING_FOR_MCC_STATION:
                handleMCCStationSelection(chatId, messageText, bot, userStates, userData);
                break;
            case STATE_WAITING_FOR_METRO_LINE:
                handleMetroLineSelection(chatId, messageText, bot, userStates, userData);
                break;
            case STATE_WAITING_FOR_METRO_STATION:
                handleMetroStationSelection(chatId, messageText, bot, userStates, userData);
                break;
            case STATE_WAITING_FOR_MCD_LINE:
                handleMCDLineSelection(chatId, messageText, bot, userStates, userData);
                break;
            case STATE_WAITING_FOR_MCD_STATION:
                handleMCDStationSelection(chatId, messageText, bot, userStates, userData);
                break;
            case STATE_WAITING_FOR_FLOOR_AREA:
                handleFloorAreaInput(chatId, messageText, bot, userStates, userData);
                break;
            case STATE_WAITING_FOR_ROAD_PART:
                handleRoadPartSelection(chatId, messageText, bot, userStates, userData);
                break;
        }
    }



    // Обработка выбора узла
    private void handleNodeSelection(long chatId, String node, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) {
        // Получаем текущие данные пользователя или создаем новые, если это первый ввод
        UserData data = userData.getOrDefault(chatId, new UserData(null, null, null, null, null, null, null));

        // Обновляем поле transport
        data = data.withTransport(node);
        userData.put(chatId, data);

        if (node.equals("МЦК")) {
            // Для МЦК указываем линию как ""
            data = data.withLine("");
            userData.put(chatId, data);

            userStates.put(chatId, STATE_WAITING_FOR_MCC_STATION);
            uiRenderer.sendMCCStationSelectionMenu(chatId, bot);
        } else if (node.equals("МЦД")) {
            userStates.put(chatId, STATE_WAITING_FOR_MCD_LINE);
            uiRenderer.sendMCDLineSelectionMenu(chatId, bot);
        } else if (node.equals("Метро")) {
            userStates.put(chatId, STATE_WAITING_FOR_METRO_LINE);
            uiRenderer.sendMetroLineSelectionMenu(chatId, bot);
        }
    }


    private void handleMCCStationSelection(long chatId, String station, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) {
        String[] validStations = {
                "Окружная", "Владыкино", "Ботанический Сад", "Ростокино", "Белокаменная",
                "Бульвар Рокоссовского", "Локомотив", "Измайлово", "Соколиная Гора",
                "Шоссе Энтузиастов", "Андроновка", "Нижегородская", "Новохохловская",
                "Угрешская", "Дубровка", "Автозаводская", "ЗИЛ", "Верхние Котлы",
                "Крымская", "Площадь Гагарина", "Лужники", "Кутузовская", "Москва-Сити",
                "Шелепиха", "Хорошёво", "Зорге", "Панфиловская", "Стрешнево", "Балтийская",
                "Коптево", "Лихоборы"
        };

        boolean isValidStation = false;
        for (String validStation : validStations) {
            if (validStation.equals(station)) {
                isValidStation = true;
                break;
            }
        }

        if (isValidStation) {
            // Обновляем данные пользователя, сохраняя станцию
            UserData data = userData.getOrDefault(chatId, new UserData(null, "", null, null, null, null, null));
            data = data.withStation(station);
            userData.put(chatId, data);

            // Переходим к выбору типа застройки
            userStates.put(chatId, STATE_WAITING_FOR_BUILDING_TYPE);
            uiRenderer.sendBuildingTypeMenu(chatId, bot);
        } else {
            uiRenderer.sendMCCStationSelectionMenu(chatId, bot);
        }
    }



    private void handleMetroLineSelection(long chatId, String line, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) {
        // Логика выбора линии метро

        // Получаем текущие данные пользователя или создаем новые, если это первый ввод
        UserData data = userData.getOrDefault(chatId, new UserData(null, null, null, null, null, null, null));

        // Сохраняем линию в данные пользователя
        data = data.withLine(line); // Используем метод withLine для обновления линии
        userData.put(chatId, data);  // Обновляем данные в userData

        // Переходим к следующему состоянию — выбор станции
        userStates.put(chatId, STATE_WAITING_FOR_METRO_STATION);
        uiRenderer.sendMetroStationSelectionMenu(chatId, line, bot);
    }


    private void handleMetroStationSelection(long chatId, String station, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) {
        // Получаем линии метро
        Map<String, String[]> metroLines = bot.getMetroLines();
        boolean isValidStation = false;

        // Проверяем, выбрана ли существующая станция на любой из линий метро
        for (String[] stations : metroLines.values()) {
            for (String validStation : stations) {
                if (validStation.equals(station)) {
                    isValidStation = true;
                    break;
                }
            }
            if (isValidStation) break;  // Останавливаем проверку, если станция найдена
        }

        if (isValidStation) {
            // Получаем текущие данные пользователя или создаем новые, если это первый ввод
            UserData data = userData.getOrDefault(chatId, new UserData(null, null, null, null, null, null, null));

            // Сохраняем станцию в объект UserData
            data = data.withStation(station);
            userData.put(chatId, data);  // Обновляем данные в карте userData

            // Станция выбрана корректно, переходим к выбору типа застройки
            userStates.put(chatId, STATE_WAITING_FOR_BUILDING_TYPE);
            uiRenderer.sendBuildingTypeMenu(chatId, bot);
        } else {
            // Если неверная станция, запрашиваем станцию снова
            uiRenderer.sendMetroStationSelectionMenu(chatId, "Выберите станцию на текущей линии", bot);
        }
    }


    private void handleMCDLineSelection(long chatId, String line, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) {
        // Список доступных линий МЦД
        Map<String, String[]> mcdLines = bot.getMCDLines();

        if (mcdLines.containsKey(line)) {
            // Линия выбрана, сохраняем её в данные пользователя
            UserData data = userData.getOrDefault(chatId, new UserData(null, null, null, null, null, null, null));

            // Сохраняем выбранную линию в UserData
            data = data.withLine(line);
            userData.put(chatId, data);  // Обновляем данные пользователя

            // Переходим к следующему шагу — выбор станции на выбранной линии
            userStates.put(chatId, STATE_WAITING_FOR_MCD_STATION);
            uiRenderer.sendMCDStationSelectionMenu(chatId, line, bot);
        } else {
            // Если выбранная линия некорректна, запрашиваем выбор линии снова
            uiRenderer.sendMCDLineSelectionMenu(chatId, bot);
        }
    }


    private void handleMCDStationSelection(long chatId, String station, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) {
        // Получаем линии и станции МЦД
        Map<String, String[]> mcdLines = bot.getMCDLines();
        boolean isValidStation = false;

        // Проверяем, выбрана ли существующая станция на любой линии МЦД
        for (String[] stations : mcdLines.values()) {
            for (String validStation : stations) {
                if (validStation.equals(station)) {
                    isValidStation = true;
                    break;
                }
            }
            if (isValidStation) break;  // Останавливаем поиск, если станция найдена
        }

        if (isValidStation) {
            // Станция выбрана корректно, сохраняем её в UserData
            UserData data = userData.getOrDefault(chatId, new UserData(null, null, null, null, null, null, null));

            // Сохраняем выбранную станцию в данные пользователя
            data = data.withStation(station);
            userData.put(chatId, data);  // Обновляем данные пользователя

            // Переходим к выбору типа застройки
            userStates.put(chatId, STATE_WAITING_FOR_BUILDING_TYPE);
            uiRenderer.sendBuildingTypeMenu(chatId, bot);
        } else {
            // Если станция некорректна, запрашиваем выбор станции снова
            uiRenderer.sendMCDStationSelectionMenu(chatId, "Выберите станцию на выбранной линии", bot);
        }
    }


    // Обработка выбора типа застройки
    private void handleBuildingTypeSelection(long chatId, String buildingType, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) {
        UserData data = userData.getOrDefault(chatId, new UserData(null, null, null, null, null, null, null));

        // Сохраняем тип застройки в данные пользователя
        data = data.withBuildingType(buildingType);
        userData.put(chatId, data);  // Обновляем данные пользователя

        // Переход к вводу площади для жилой и коммерческой недвижимости
        if (buildingType.equals("Жилая") || buildingType.equals("Коммерческая")) {
            userStates.put(chatId, STATE_WAITING_FOR_FLOOR_AREA);  // Устанавливаем состояние ожидания площади
            uiRenderer.askForFloorArea(chatId, bot);  // Запрашиваем ввод площади и удаляем клавиатуру
        } else {
            // Если неверный ввод типа застройки, просим выбрать снова
            uiRenderer.sendBuildingTypeMenu(chatId, bot);
        }
    }





    private void handleFloorAreaInput(long chatId, String floorArea, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) {
        try {
            // Обновляем данные пользователя, сохраняя площадь
            UserData data = userData.getOrDefault(chatId, new UserData(null, null, null, null, null, null, null));
            data = data.withFloorArea(floorArea);
            userData.put(chatId, data);

            // Переходим к запросу даты и времени
            userStates.put(chatId, STATE_WAITING_FOR_DATE_TIME);
            uiRenderer.askForDateTime(chatId, bot);
        } catch (NumberFormatException e) {
            uiRenderer.askForFloorArea(chatId, bot);
        }
    }

    // Обработка ввода даты и времени
    private void handleDateTimeInput(long chatId, String dateTime, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) throws TelegramApiException {
        // Обновляем данные пользователя, сохраняя дату и время
        UserData data = userData.getOrDefault(chatId, new UserData(null, null, null, null, null, null, null));
        data = data.withDateTime(dateTime);
        userData.put(chatId, data);

        // Показываем результат выбора
        uiRenderer.showFinalResult(chatId, bot, data);

        // Завершаем сессию
        userStates.remove(chatId);
    }

    private void handleRoadPartSelection(long chatId, String roadPart, LoadForecastTelegramBot bot, Map<Long, String> userStates, Map<Long, UserData> userData) {
        // Сохраняем выбранную часть дорожного полотна в UserData
        UserData data = userData.getOrDefault(chatId, new UserData(null, null, null, null, null, null, null));
        data = data.withRoadPart(roadPart);  // Сохраняем часть дорожного полотна
        userData.put(chatId, data);

        // Переходим к выбору типа застройки
        userStates.put(chatId, STATE_WAITING_FOR_BUILDING_TYPE);
        uiRenderer.sendBuildingTypeMenu(chatId, bot);
    }


}
