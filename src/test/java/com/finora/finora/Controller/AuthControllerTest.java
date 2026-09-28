// package com.finora.finora.Controller;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.web.servlet.MockMvc;

// import com.finora.finora.Service.AuthService;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

// @WebMvcTest(AuthController.class)
// class AuthControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private AuthService authService;

//     @Test
//     void loginPage_shouldBeAvailableAtAuthLoginUrl() throws Exception {
//         mockMvc.perform(get("/auth/login"))
//                 .andExpect(status().isOk())
//                 .andExpect(view().name("auth/login"));
//     }

//     @Test
//     void registerPage_shouldBeAvailableAtAuthRegisterUrl() throws Exception {
//         mockMvc.perform(get("/auth/register"))
//                 .andExpect(status().isOk())
//                 .andExpect(view().name("auth/register"));
//     }
// }
