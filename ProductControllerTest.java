package br.com.carlosjunior.registrationlogin.web;


import br.com.carlosjunior.registrationlogin.entities.Product;
import br.com.carlosjunior.registrationlogin.entities.User;
import br.com.carlosjunior.registrationlogin.repositories.ProductRepository;
import br.com.carlosjunior.registrationlogin.repositories.UserRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private User testSeller;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        // Create a test seller
        testSeller = new User();
        testSeller.setFirsName("Test Seller");
        testSeller.setLastName("Test Seller Last");
        testSeller.setEmail("seller@test.com");
        testSeller.setPassword("password");
        userRepository.save(testSeller);
    }


    @Test
    public void testAddProduct() throws Exception {
        // Create a multipart file from a test image
        ClassPathResource resource = new ClassPathResource("test_image.jpg");
        MockMultipartFile imageFile = new MockMultipartFile("file", "test_image.jpg", "image/jpeg", resource.getInputStream());

        // Send a POST request to add a new product
        mockMvc.perform(MockMvcRequestBuilders.multipart("/seller/addProduct")
                        .file(imageFile)
                        .param("name", "Test Product")
                        .param("description", "Test Description")
                        .param("price", "10.00")
                        .param("quantity", "5")
                        .with(SecurityMockMvcRequestPostProcessors.user(testSeller.getEmail())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.containsString("/products/")));

        // Check that the product was added to the database
        List<Product> products = productRepository.findAllBySeller(testSeller);
        assertEquals(1,products.size());
        Product product = products.get(0);
        assertEquals("Test Product",product.getName());
        assertEquals("Test Description",product.getDescription());
        assertEquals(10.00,product.getPrice());
        assertEquals(5,product.getQuantity());
        assertEquals(testSeller,product.getSeller());
        assertNotNull(product.getText());
    }
}