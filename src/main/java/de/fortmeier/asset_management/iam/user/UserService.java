package de.fortmeier.asset_management.iam.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;


    public Optional<User> findByUserName(String username) {
        return userRepository.findByUserName(username);
    }


    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(IllegalAccessError::new);
        userRepository.delete(user);
    }

    @Transactional
    public void enableUser(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(IllegalAccessError::new);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
