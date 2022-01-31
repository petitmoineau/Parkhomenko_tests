package kma.topic2.junit.service;

import kma.topic2.junit.exceptions.ConstraintViolationException;
import kma.topic2.junit.exceptions.LoginExistsException;
import kma.topic2.junit.exceptions.UserNotFoundException;
import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.model.User;
import kma.topic2.junit.repository.UserRepository;
import kma.topic2.junit.validation.UserValidator;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
public class UserServiceTest {

    @SpyBean
    private UserValidator userValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void userIsNew() {
        NewUser newUser = NewUser.builder()
                .login("kugif")
                .password("qwerty")
                .fullName("Blahblah")
                .build();
        userService.createNewUser(newUser);

        Mockito.verify(userValidator).validateNewUser(newUser);

        org.assertj.core.api.Assertions.assertThat(userRepository.isLoginExists("Nastja")).isTrue();
    }

    @Test
    void userNotNew() {
        NewUser newUser = NewUser.builder()
                .login("Nastja")
                .password("qwerty")
                .fullName("Blahblah")
                .build();

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> userService.createNewUser(newUser))
                .isInstanceOf(LoginExistsException.class)
                .hasMessage("Login Nastja already taken");

    }

    @Test
    void userWrongPassword() {
        NewUser newUser = NewUser.builder()
                .login("dmvnl")
                .password("@#$%^@#$%")
                .fullName("Blahblah")
                .build();

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> userService.createNewUser(newUser))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkUserByLogin()
    {
        NewUser newUser = NewUser.builder()
                .login("Nastja")
                .password("qwerty")
                .fullName("Blahblah")
                .build();

        User user = User.builder()
                .login("Nastja")
                .password("qwerty")
                .fullName("Blahblah")
                .build();
        userService.createNewUser(newUser);

        assertEquals(user, userService.getUserByLogin(user.getLogin()));
    }

    @Test
    void checkUserByLoginFail()
    {
        User user = User.builder()
                .login("Nastjaaa")
                .password("qwerty")
                .fullName("Blahblah")
                .build();

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> userService.getUserByLogin(user.getLogin()))
                .isInstanceOf(UserNotFoundException.class);
    }

}