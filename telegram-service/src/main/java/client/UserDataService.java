package client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import root.utils.UserData;

@Component
public class UserDataService {

    private final UserDataClient userDataClient;

    @Autowired
    public UserDataService(UserDataClient userDataClient) {
        this.userDataClient = userDataClient;
    }

    public void processAndSendUserData(UserData userData) {
        // Здесь можно произвести дополнительную обработку userData, если необходимо
        userDataClient.sendUserData(userData);
    }
}
