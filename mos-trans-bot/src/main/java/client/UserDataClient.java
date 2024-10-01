package client;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import root.utils.UserData;

@Service
public class UserDataClient {

    private final RestTemplate restTemplate;

    public UserDataClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendUserData(UserData userData) {
        // Указываем URL сервера
        String serverUrl = "http://localhost:8080/api/stations/predict";

        // Устанавливаем заголовки
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Создаем HTTP-entity с данными и заголовками
        HttpEntity<UserDataRequest> requestEntity = new HttpEntity<>(toUserDataRequest(userData), headers);

        // Отправляем POST запрос и получаем ResponseEntity
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(serverUrl, requestEntity, String.class);

        // Возвращаем тело ответа
        return responseEntity.getBody();
    }

    // Преобразуем UserData в UserDataRequest (с нужными полями)
    private UserDataRequest toUserDataRequest(UserData userData) {
        return new UserDataRequest(
                userData.line(),
                userData.station(),
                userData.floorArea() != null ? Double.parseDouble(userData.floorArea()) : null,
                userData.buildingType(),
                userData.dateTime()
        );
    }
}
