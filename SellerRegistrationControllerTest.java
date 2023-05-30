package br.com.carlosjunior.registrationlogin.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import br.com.carlosjunior.registrationlogin.services.UserService;
import br.com.carlosjunior.registrationlogin.web.SellerRegistrationController;
import br.com.carlosjunior.registrationlogin.web.dto.UserRegistrationDto;

import static org.mockito.Mockito.*;

class SellerRegistrationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    private SellerRegistrationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new SellerRegistrationController(userService);
    }

    @Test
    void testShowRegistrationForm() {
        String result = controller.showRegistrationForm();
        assert(result.equals("seller-registration"));
    }

    @Test
    void testRegisterUserAccount() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setFirstName("seller123");
        registrationDto.setLastName("seller123");
        registrationDto.setEmail("seller123@example.com");
        registrationDto.setPassword("password");
        registrationDto.setStatus("DD");

        String result = controller.registerUserAccount(registrationDto);

        verify(userService, times(1)).saveSeller(registrationDto);
    }
}
