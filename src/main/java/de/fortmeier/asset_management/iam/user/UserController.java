package de.fortmeier.asset_management.iam.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller with all user requests.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Gets all user.
     *
     * @return the Response Entity with the status code and all found user in its body.
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<User> users = userService.findAll();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : users) {
            userDtoList.add(
                    new UserDto(
                            user.getFirstName(),
                            user.getLastName(),
                            user.getUsername(),
                            user.isEnabled()
                    )
            );
        }

        return ResponseEntity.ok(userDtoList);
    }

    /**
     * Enables a registered user by the userName.
     *
     * @param userName Name of the user, which will be enabled.
     * @return the Response Entity with the status code.
     */
    @PostMapping("/enable")
    public ResponseEntity<String> enableUser(@RequestParam("userName") String userName) {

        try {
            userService.enableUser(userName);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();

    }

    /**
     * Deletes a user by the userName.
     * @param userName Name of the user, which will be deleted.
     * @return a Response Entity with the status code.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam("userName") String userName) {

        try {
            if (userName.equals("Admin"))
                return ResponseEntity.badRequest().build();
            userService.deleteUser(userName);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();

    }

}
