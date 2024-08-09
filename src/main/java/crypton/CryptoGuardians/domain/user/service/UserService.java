package crypton.CryptoGuardians.domain.user.service;

import crypton.CryptoGuardians.domain.user.entity.User;


public interface UserService {
    public User register(String username, String password);
    public User login(String username, String password);
    public Boolean checkDuplicate(String username);
}
