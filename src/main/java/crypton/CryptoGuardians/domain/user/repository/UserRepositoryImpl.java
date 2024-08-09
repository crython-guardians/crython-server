//package crypton.CryptoGuardians.domain.user.repository;
//
//import crypton.CryptoGuardians.domain.user.entity.User;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public class UserRepositoryImpl implements UserRepository{
//    // For testing purpose, use memory
//    private static List<User> users = new ArrayList<>();
//    private static Long id = 1L;
//
//    @Override
//    public User save(User user) {
//        user.setId(id++);
//        users.add(user);
//        return user;
//    }
//
//    @Override
//    public Optional<User> findByUsername(String username) {
//        return users.stream()
//                .filter(user -> user.getUsername().equals(username))
//                .findAny();
//    }
//
//    @Override
//    public Optional<User> findById(Long id) {
//        return users.stream()
//                .filter(user -> user.getId().equals(id))
//                .findAny();
//    }
//}
