package sportbets.web.dto.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginRequestDto {


    private static final Logger log = LoggerFactory.getLogger(LoginRequestDto.class);
    private String userName;
    private String password;

    public LoginRequestDto() {
    }

    public LoginRequestDto(String password, String userName) {
        this.password = password;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
