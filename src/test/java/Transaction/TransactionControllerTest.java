package Transaction;

import BudjetBrain.Balance.BalanceDTO;
import BudjetBrain.BudjetBrainApplication;
import BudjetBrain.Transaction.Transaction;
import BudjetBrain.Transaction.TransactionController;
import BudjetBrain.Transaction.TransactionDTO;
import BudjetBrain.Transaction.TransactionRepository;
import BudjetBrain.Transaction.TransactionService;

import BudjetBrain.Category.Category;

import BudjetBrain.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@ContextConfiguration(classes = BudjetBrainApplication.class)
@WebMvcTest(controllers = TransactionController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class TransactionControllerTest {
    //здес мокаем сервисы
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionDTO transactionDTO;

@MockBean
private TransactionRepository transactionRepository;
@MockBean
private Category category;


    @Test
    void addTransaction_takeAOneTransactionOfUserAndSaveThisInBD() throws Exception {

        User testUser = new User();
        testUser.setId(1L);


        Category an = new Category();
        an.setId(1L);

                TransactionDTO testTest = new TransactionDTO();
        testTest.setAmount(900.0);
        testTest.setTypeTransaction("INCOME");
        testTest.setUser(1L);
        testTest.setCategory(1L);



        Transaction total = new Transaction();
        total.setAmount(900.0);
        total.setTypeTransaction("INCOME");
        total.setUser(testUser);
        total.setCategory(an);

        //мокаем сервис
        when(transactionService.addTransaction(any(TransactionDTO.class))).thenReturn(total);

//вызываю эндпоинт
        mockMvc.perform(
                post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":900.0,\"typeTransaction\":\"INCOME\",\"user\":1,\"category\":1}"))

                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.amount").value(900.0))
                                .andExpect(jsonPath("$.category").exists())
        .andExpect(jsonPath("$.user").value(1L));


            verify(transactionService).addTransaction(any(TransactionDTO.class));



    }

@Test
    void getAllTransactions_getListOfTransactionsinBD_allTransactions() throws Exception {

    User testUser = new User();
    testUser.setId(1L);



    Transaction testOne = new Transaction();
    testOne.setAmount(5000.0);
    testOne.setTypeTransaction("INCOME");
    testOne.setUser(testUser);


    Transaction testTwo = new Transaction();
    testTwo.setAmount(300.0);
    testTwo.setTypeTransaction("EXPENSE");
    testTwo.setUser(testUser);

    List<Transaction> testTest = List.of(testOne,testTwo);

        //мокаем сервис
    when(transactionService.getAllTransactions()).thenReturn(testTest);

    //вызываю эндпоинт
    mockMvc.perform(
            get("/transactions"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].amount").exists())
                    .andExpect(jsonPath("$[1].typeTransaction").value("EXPENSE"))
                    .andExpect(jsonPath("$.[0].user").value(1L));

            verify(transactionService).getAllTransactions();


    }

@Test
    void getTransaction_takeAIDOFTransactionAndFoundThis_foundNeedTransaction()  throws Exception{

    Transaction testOne = new Transaction();
    testOne.setAmount(5000.0);
    testOne.setTypeTransaction("INCOME");
    testOne.setId(1L);

    //мокаем сервис
    when(transactionService.getTransactionId(1L)).thenReturn(testOne);
    //вызываем ендпоинт
    mockMvc.perform(
            get("/transaction/1"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amount").value(5000.0));
            verify(transactionService).getTransactionId(1L);

    }
@Test
    void deleteTransaction_takeNeedTransactionAndDeleteThis_DeleteNeedTransaction() throws Exception {
    Transaction testOne = new Transaction();
    testOne.setAmount(5000.0);
    testOne.setTypeTransaction("INCOME");
    testOne.setId(1L);

    //мокаем репозиторий
    doNothing().when(transactionRepository).deleteById(1L);


    //вызываем ендпоинт
    mockMvc.perform(
            delete("/transaction/1"))

                    .andExpect(status().isOk());
            verify(transactionRepository).deleteById(1L);

    }

@Test
    void calculateBalance_countingOfBalanceOfUser_getSumOfExpenseAndIncomeAndTotalBalance() throws Exception {

    User testUser = new User();
    testUser.setId(1L);


    Transaction testOne = new Transaction();
    testOne.setAmount(5000.0);
    testOne.setTypeTransaction("INCOME");
    testOne.setUser(testUser);


    Transaction testTwo = new Transaction();
    testTwo.setAmount(300.0);
    testTwo.setTypeTransaction("EXPENSE");
    testTwo.setUser(testUser);

    List<Transaction> testTest = List.of(testOne,testTwo);


    BalanceDTO result = new BalanceDTO(4700.0, 5000.0, 300.0);



    //вызываем сервис
    when(transactionService.calculateBalance(1L)).thenReturn(result);
//вызываю эндпоинт
    mockMvc.perform(
            get("/transaction/1/balance"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.balance").value(4700.0))
                    .andExpect(jsonPath("$.income").value(5000.0));

            verify(transactionService).calculateBalance(1L);


}

@Test
    void  getSumOfEveryCategory_takeASumOfEveryCategoryAndGetThisStatistics_statisticsBalanceOfEveryCategoryOfUser() throws Exception {
    Map<String,Double> resultOne = Map.of(
            "Еда",1000.0,
            "комуналка",300.0,
            "Развлечения",100.0
    );
//мокаем сервис
    when(transactionService.getSumOfEveryCategory(1L,9)).thenReturn(resultOne);

    //вызываем эндпоинт
    mockMvc.perform(
            get("/transaction/1/balance/category/9"))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.комуналка").value(300.0))
            .andExpect(jsonPath("$.Развлечения").value(100.0));
    verify(transactionService).getSumOfEveryCategory(1L,9);


    }


}
