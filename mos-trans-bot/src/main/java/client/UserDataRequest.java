package client;


public class UserDataRequest {

    private String line;
    private String name;
    private Double squareMeters;
    private String buildingType;
    private String datetime;

    public UserDataRequest(String line, String name, Double squareMeters, String buildingType, String datetime) {
        this.line = line;
        this.name = name;
        this.squareMeters = squareMeters;
        this.buildingType = buildingType;
        this.datetime = datetime;
    }

    // Getters и Setters
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(Double squareMeters) {
        this.squareMeters = squareMeters;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}