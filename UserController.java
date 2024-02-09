package customlogin;

import customlogin.dto.UserDto;
import customlogin.model.User;
import customlogin.service.UserService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // Retrieve logged-in user's name from session
        String name = (String) session.getAttribute("username");
        model.addAttribute("username", name);

        // Fetch all registered users from the database
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "home";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userDto") UserDto userDto, HttpSession session, RedirectAttributes redirectAttributes) {
        String username = userDto.getName();
        String password = userDto.getPassword();
        
        if (userService.validateCredentials(username, password)) {
            // Retrieve the user's name and store it in the session
            User user = userService.findByName(username);
            session.setAttribute("username", user.getName()); // Set the username in the session
            return "redirect:/home";
        } else {
            // Redirect to login page with error parameter
            redirectAttributes.addAttribute("error", true);
            return "redirect:/login";
        }
    }



    @GetMapping("/register")
    public String register(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String registerSave(@ModelAttribute("user") UserDto userDto, Model model) {
        try {
            userService.save(userDto);
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage(), e);
            model.addAttribute("error", "An error occurred during registration. Please try again.");
            return "register";
        }
    }
}
