package br.com.carlosjunior.registrationlogin.web;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import br.com.carlosjunior.registrationlogin.services.UserService;
import br.com.carlosjunior.registrationlogin.web.dto.UserRegistrationDto;

class UserRegistrationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserRegistrationController controller;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowRegistrationForm() {
        String result = controller.showRegistrationForm();
        assert(result.equals("registration"));
    }

    @Test
    void testRegisterUserAccount() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setFirstName("testuser");
        registrationDto.setLastName("testuserlast");
        registrationDto.setEmail("testuser@test.com");
        registrationDto.setPassword("password");
        registrationDto.setStatus("DD");

        String result = controller.registerUserAccount(registrationDto);

        verify(userService, times(1)).save(registrationDto);

        assert(result.equals("redirect:/registration?success"));
    }

}
