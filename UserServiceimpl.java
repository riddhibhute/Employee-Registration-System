package customlogin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import customlogin.dto.UserDto;
import customlogin.model.User;
import customlogin.repositories.UserRepository;
import java.util.List; // Add this import statement

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() { // Implement the method
        return userRepository.findAll();
    }   
    
    @Override
    public boolean validateCredentials(String name, String password) { // Corrected method signature
        // Find the user by name
        User user = userRepository.findByName(name);
        
        // Check if the user exists and if the password matches
        return user != null && user.getPassword().equals(password);
    }
    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }
    
    @Override
    public User save(UserDto userDto) {
        User user = new User(
                userDto.getName(),
                userDto.getDateOfBirth(),
                userDto.getGender(),
                userDto.getAddress(),
                userDto.getCity(),
                userDto.getState(),
                userDto.getPassword() // Assuming you handle password encoding outside of this service
        );

        return userRepository.save(user);
    }
}
