package kma.topic2.junit.service;

import kma.topic2.junit.exceptions.ConstraintViolationException;
import kma.topic2.junit.exceptions.LoginExistsException;
import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.repository.UserRepository;
import kma.topic2.junit.validation.UserValidator;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void validateNewUser() {

        Mockito.when(userRepository.isLoginExists(ArgumentMatchers.anyString())).thenReturn(false);
        NewUser newUser = NewUser.builder()
                .login("Nastja")
                .password("qwerty")
                .fullName("Blahblah")
                .build();
        Throwable throwable = catchThrowable(() -> userValidator.validateNewUser(newUser));

        assertThat(throwable).isNull();
    }

    @Test
    void validateNewUserWithInvalidPasswordSize() {

        Mockito.when(userRepository.isLoginExists(ArgumentMatchers.anyString())).thenReturn(false);

        NewUser newUser = NewUser.builder()
                .login("Nastja")
                .password("qwertyqwerty")
                .fullName("Blahblah")
                .build();

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> userValidator.validateNewUser(newUser))
                .isInstanceOf(ConstraintViolationException.class)
                .extracting("errors", InstanceOfAssertFactories.ITERABLE)
                .containsExactly("Password has invalid size");

    }

    @Test
    void validateNewUserWithInvalidRegex() {

        Mockito.when(userRepository.isLoginExists(ArgumentMatchers.anyString())).thenReturn(false);

        NewUser newUser = NewUser.builder()
                .login("Nastja")
                .password("$%^&")
                .fullName("Blahblah")
                .build();

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> userValidator.validateNewUser(newUser))
                .isInstanceOf(ConstraintViolationException.class)
                .extracting("errors", InstanceOfAssertFactories.ITERABLE)
                .containsExactly("Password doesn't match regex");
    }

    @Test
    void validateNewUserWithInvalidPasswordAtAll() {

        Mockito.when(userRepository.isLoginExists(ArgumentMatchers.anyString())).thenReturn(false);

        NewUser newUser = NewUser.builder()
                .login("Nastja")
                .password("$%^&#$%#$#$%")
                .fullName("Blahblah")
                .build();

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> userValidator.validateNewUser(newUser))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("You have errors in you object")
                .extracting("errors", InstanceOfAssertFactories.ITERABLE)
                .containsExactly("Password has invalid size", "Password doesn't match regex");
    }

    @Test
    void validateExistingUser()
    {
        Mockito.when(userRepository.isLoginExists(ArgumentMatchers.anyString())).thenReturn(true);

        NewUser newUser = NewUser.builder()
                .login("Nastja")
                .password("qwertyqwerty")
                .fullName("Parkhomenko Anastasiia")
                .build();


        org.assertj.core.api.Assertions.assertThatThrownBy(() -> userValidator.validateNewUser(newUser))
                .isInstanceOf(LoginExistsException.class)
                .hasMessage("Login Nastja already taken");
    }
}
