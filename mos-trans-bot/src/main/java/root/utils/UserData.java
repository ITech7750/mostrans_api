package root.utils;


public record UserData(
        String transport,       // Тип транспорта (МЦК, МЦД, Метро)
        String line,            // Линия (для метро и МЦД), для МЦК - ""
        String station,         // Станция
        String buildingType,    // Тип застройки (Жилая/Коммерческая)
        String floorArea,       // Площадь
        String dateTime,         // Дата и время
        String roadPart
) {
    // Метод для обновления поля transport
    public UserData withTransport(String transport) {
        return new UserData(transport, line, station, buildingType, floorArea, dateTime, roadPart);
    }

    // Метод для обновления поля line
    public UserData withLine(String line) {
        return new UserData(transport, line, station, buildingType, floorArea, dateTime, roadPart);
    }

    // Метод для обновления поля station
    public UserData withStation(String station) {
        return new UserData(transport, line, station, buildingType, floorArea, dateTime, roadPart);
    }

    // Метод для обновления поля buildingType
    public UserData withBuildingType(String buildingType) {
        return new UserData(transport, line, station, buildingType, floorArea, dateTime, roadPart);
    }

    // Метод для обновления поля floorArea
    public UserData withFloorArea(String floorArea) {
        return new UserData(transport, line, station, buildingType, floorArea, dateTime, roadPart);
    }

    // Метод для обновления поля dateTime
    public UserData withDateTime(String dateTime) {
        return new UserData(transport, line, station, buildingType, floorArea, dateTime, roadPart);
    }

    // Метод для обновления поля roadPart
    public UserData withRoadPart(String roadPart) {
        return new UserData(transport, line, station, buildingType, floorArea, dateTime, roadPart);
    }
}


