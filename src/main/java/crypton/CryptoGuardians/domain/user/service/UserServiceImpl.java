package crypton.CryptoGuardians.domain.user.service;

import crypton.CryptoGuardians.domain.user.entity.User;
import crypton.CryptoGuardians.domain.user.exception.UserAlreadyExistsException;
import crypton.CryptoGuardians.domain.user.exception.UserNotFoundException;
import crypton.CryptoGuardians.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        User user = new User(username, password);
        userRepository.save(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    @Override
    public Boolean checkDuplicate(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
