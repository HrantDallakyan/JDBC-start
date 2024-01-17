package repository.user;

import model.User;

import java.util.List;

public interface UserRepository {

    void create(User user);

    void update(User user);

    void delete(int id);

    List<User> getAll();

    User getById(int userId);

    User getByEmail(String email);
}
