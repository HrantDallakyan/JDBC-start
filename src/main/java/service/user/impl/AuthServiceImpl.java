package service.user.impl;

import exception.UserAlreadyExistsException;
import exception.ValidationException;
import model.User;
import repository.user.UserRepository;
import service.user.AuthService;

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void login(String email, String password) {
        validate(email, password);
        User user = userRepository.getByEmail(email);
        if (user != null && password.equals(user.getPassword()))
            System.out.println("login successfully completed");
        else
            throw new ValidationException("Email or Password not valid");
    }

    @Override
    public void update(User user) {
        validate(user.getEmail(), user.getPassword());
        userRepository.update(user);
    }


    @Override
    public void register(User user) {
        validate(user.getEmail(), user.getPassword());
        User byEmail = userRepository.getByEmail(user.getEmail());
        if (byEmail != null)
            throw new UserAlreadyExistsException();
        userRepository.create(user);

    }

    @Override
    public void changePassword(String lastPassword, String newPassword, String repeatPassword, int id) {
        if (lastPassword.equals(newPassword) || !newPassword.equals(repeatPassword)) {
            throw new ValidationException("Incorrect data");
        }
        User user = userRepository.getById(id);
        user.setPassword(newPassword);
        userRepository.update(user);
    }


    private void validate(String email, String password) {
        if (email == null || !email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
                + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"))
            throw new ValidationException("Email not valid");
        if (password == null || password.length() < 8)
            throw new ValidationException("Password must be more than 8 symbols");
    }

}
