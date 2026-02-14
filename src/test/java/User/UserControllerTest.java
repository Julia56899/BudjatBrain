package User;


import BudjetBrain.BudjetBrainApplication;
import BudjetBrain.Transaction.TransactionDTO;
import BudjetBrain.Transaction.TransactionService;
import BudjetBrain.User.UserDTO;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import BudjetBrain.User.User;
import BudjetBrain.User.UserController;
import BudjetBrain.User.UserService;
import BudjetBrain.Transaction.Transaction;

@ContextConfiguration(classes = BudjetBrainApplication.class)
@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})

public class UserControllerTest {
@MockBean
    private UserService userService;
@MockBean
private UserDTO userDTO;
@MockBean
private TransactionDTO transactionDTO;
@MockBean
private TransactionService transactionService;


@Autowired
    private MockMvc mockMvc;

@Test
    void registerUser_takeAUserDTOAndRegisterThis_registerInDBOFUserDTO() throws Exception {
    //тестовый юзер
    User testUser = new User();
    testUser.setUsername("Юлия");
    testUser.setEmail("tt@mail.ru");
    testUser.setPassword("12345");

    //мокаем юзерсервис
    when(userService.registerUser(any(UserDTO.class))).thenReturn(testUser);

    //вызываем эндпоинт
mockMvc.perform(
        post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Юлия\",\"email\":\"tt@mail.ru\",\"password\":\"12345\"}"))
              //дальше проверки
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Юлия"))
                        .andExpect(jsonPath("$.email").value("tt@mail.ru"));

        //провера вызова сервиса
        verify(userService).registerUser(any(UserDTO.class));

}

@Test
    void getAllUsers_getListOfAllUsersOfThisFinanceTrecker_ListOfUsers() throws  Exception {
    User userOne = new User();
    userOne.setUsername("Юлия");
    userOne.setEmail("tt@mail.ru");
    userOne.setPassword("12345");

    User userTwo = new User();
    userTwo.setUsername("Лиза");
    userTwo.setEmail("ll@mail.ru");
    userTwo.setPassword("55555");


    List<User> testUsers = List.of(userOne,userTwo);

    //мокаю юзерсерсервис
    when(userService.getAllUsers()).thenReturn(testUsers);

    //вызываю эндпоинт
    mockMvc.perform(
            get("/api/users"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].email").value("tt@mail.ru"))
                    .andExpect(jsonPath("$[1].email").value("ll@mail.ru"));

            verify(userService).getAllUsers();

}
@Test
    void userId_foundOfUserWithId_getUserById() throws Exception {
    User testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("Лиза");
    testUser.setEmail("ll@mail.ru");
    testUser.setPassword("55555");


    //мокаем юзерсервис
    when(userService.getUserId(1L)).thenReturn(testUser);

    //вызываю эндпоинт
    mockMvc.perform(
            get("/api/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));

            verify(userService).getUserId(1L);


    }
@Test
    void getTransactionsOfUser_getListTransactionsOfUserById_transactionsOfUser() throws Exception {

    User testUser = new User();
    testUser.setId(1L);



    Transaction testTransactionOne = new Transaction();
    testTransactionOne.setAmount(5000.0);
    testTransactionOne.setTypeTransaction("INCOME");
    testTransactionOne.setUser(testUser);


    Transaction testTransactionTwo = new Transaction();
    testTransactionTwo.setAmount(300.0);
    testTransactionTwo.setTypeTransaction("EXPENSE");
    testTransactionTwo.setUser(testUser);

    List<Transaction> testTest = List.of(testTransactionOne,testTransactionTwo);

    //мокаем юзерсервис
    when(transactionService.getTransactionsByIdUser(1L)).thenReturn(testTest);

    //вызываем эндпоинт
    mockMvc.perform(
            get("/api/users/1/transactions"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[*].typeTransaction").exists())
                    .andExpect(jsonPath("$[1].amount").value(300.0));

            verify(transactionService).getTransactionsByIdUser(1L);


}


}
