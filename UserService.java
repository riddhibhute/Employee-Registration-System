package customlogin.service;

import java.util.List;
import customlogin.dto.UserDto;
import customlogin.model.User;

public interface UserService {
    User findByName(String name);
    User save(UserDto userDto);
    boolean validateCredentials(String name, String password);
    List<User> getAllUsers(); // Add this method to the interface
}
