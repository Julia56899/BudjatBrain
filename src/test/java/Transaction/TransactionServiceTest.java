package Transaction;


import BudjetBrain.Balance.BalanceDTO;
import BudjetBrain.Category.Category;

import BudjetBrain.Category.CategoryPredictor;
import BudjetBrain.Category.CategoryRepository;
import BudjetBrain.Transaction.Transaction;
import BudjetBrain.Transaction.TransactionDTO;
import BudjetBrain.Transaction.TransactionRepository;
import BudjetBrain.Transaction.TransactionService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import BudjetBrain.User.User;
import BudjetBrain.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private TransactionRepository  transactionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryPredictor categoryPredictor;

    @Test
    void getAllTransactions_getListTransactions_listOfTransactions() {
//моковые транзакции

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
        //мокаем репозиторий
        when(transactionRepository.findAll()).thenReturn(testTest);

        //дальше из сервиса вызываем то что нашли
        List<Transaction> total = transactionService.getAllTransactions();

        //дальше сравниваем
        assertEquals(total, testTest);
        verify(transactionRepository).findAll();

    }

    @Test

    void getTransactionId_foundTransactionById_getNeedTransaction() {
        //мок транзакция с айди
        Transaction testOne = new Transaction();
        testOne.setAmount(5000.0);
        testOne.setTypeTransaction("INCOME");
        testOne.setId(1L);

        //мокаем репозиторий
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(testOne));

        Transaction total = transactionService.getTransactionId(1L);

        assertEquals(testOne, total);
        verify(transactionRepository).findById(1L);

    }


    @Test
    void getTransactionsByIdUser_foundAllTransactionsByUserId_ListTransactionsOfUser() {

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

        //мокаем репозиторий
        when(transactionRepository.findByUserId(1L)).thenReturn(testTest);

        //вытаскиваем из сервиса то что сохранили
        List<Transaction> total = transactionService.getTransactionsByIdUser(1L);

        //дальше сравниваем
        assertEquals(testTest, total);
        verify(transactionRepository).findByUserId(1L);


    }

    @Test
    void addTransaction_takeATransactionDTOAndAddThisUnBD_saveANewTransaction() {
        //создаем транзактион дто и транзакцию
        Category sup = new Category();
        sup.setId(1L);
        sup.setName("Еда");

        User testUser = new User();
        testUser.setId(1L);


        TransactionDTO testTest = new TransactionDTO();
        testTest.setAmount(900.0);
        testTest.setTypeTransaction("EXPENSE");
        testTest.setCategory(1L);
        testTest.setUser(1L);
        testTest.setDescription("столовая");

        Transaction total = new Transaction();
        total.setAmount(900.0);
        total.setTypeTransaction("EXPENSE");
        total.setCategory(sup);
        total.setUser(testUser);



        //мокаем все что есть в сервисе
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        when(categoryPredictor.predictCategory("столовая")).thenReturn("Еда");
        when(categoryRepository.findByNameAndUser("Еда",testUser)).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(sup);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(total);



    Transaction answer = transactionService.addTransaction(testTest);

    //дальше сравниваем
    assertEquals(total.getAmount(), answer.getAmount());
    assertEquals(total.getTypeTransaction(), answer.getTypeTransaction());
    assertEquals(total.getCategory().getName(), answer.getCategory().getName());



    verify(transactionRepository).save(any(Transaction.class));

    verify(categoryRepository).findByNameAndUser("Еда",testUser);
    verify(categoryPredictor).predictCategory("столовая");
    verify(categoryRepository).findById(1L);
    verify(userRepository).findById(1L);

}

@Test
void calculateBalance_getTransactionsAndAccountOfBalance_getBalanceOfUser() {

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

//мокаем репозиторий
    when(transactionRepository.findByUserId(1L)).thenReturn(testTest);

    //дальше из сервиса вызываем то что нашли
    BalanceDTO ans = transactionService.calculateBalance(1L);
    //сравнивем
    assertEquals(4700.0, ans.getBalance());
    assertEquals(300.0, ans.getExpense());
    assertEquals(5000.0, ans.getIncome());
    verify(transactionRepository).findByUserId(1L);

}

@Test
void calculateCategory_takeAllExpenseAndIncomesAndAccountOnCategory_getSumOfEveryCategory() {
    //делаем лист транзакций, из них мапу и ее уже сравниваем

    //создаем сущности категори
    Category categoryOne = new Category();
    categoryOne.setName("Еда");

    Category categoryTwo = new Category();
    categoryOne.setName("Зарплата");

    User testUser = new User();
    testUser.setId(1L);



    Transaction testOne = new Transaction();
    testOne.setAmount(3000.0);
    testOne.setTypeTransaction("EXPENSE");
    testOne.setUser(testUser);
    testOne.setCategory(categoryOne);

    Transaction testTwo = new Transaction();
    testTwo.setAmount(4000.0);
    testTwo.setTypeTransaction("EXPENSE");
    testTwo.setUser(testUser);
    testTwo.setCategory(categoryTwo);
    List<Transaction> testTestList = List.of(testOne,testTwo);

    Map<String, Double> testTest = Map.of(
            "Еда",3000.0,
            "Зарплата",4000.0
    );

    //мокаем репозиторий
    when(transactionRepository.findByUserId(1L)).thenReturn(testTestList);
    //вызываем в сервисе то что получили
    Map<String,Double> answer = transactionService.calculateCategory(1L);

//сравниваем
    assertEquals(testTest.get("Еда"),3000.0);
    assertEquals(testTest.get("Зарплата"),4000.0);

    verify(transactionRepository).findByUserId(1L);

}

@Test
void getSumOneCategory_takeFromRequestANameOfCategoryAndOutputTheSum_sumOfCategory() {

    Category food = new Category();
    food.setName("Еда");

    Category salary = new Category();
    salary.setName("Зарплата");


    User testUser = new User();
    testUser.setId(1L);

    Transaction t1 = new Transaction();
    t1.setAmount(3000.0);
    t1.setTypeTransaction("EXPENSE");
    t1.setUser(testUser);
    t1.setCategory(food);

    Transaction t2 = new Transaction();
    t2.setAmount(4000.0);
    t2.setTypeTransaction("INCOME");
    t2.setUser(testUser);
    t2.setCategory(salary);

    List<Transaction> transactions = List.of(t1, t2);
    Double total = 4000.0; //это мы потом сравним

    //мокаем репозиторий
    when(transactionRepository.findByUserId(1L)).thenReturn(transactions);

    Double oneTest = transactionService.getSumOneCategory(1L, "Зарплата");
    //дальше сравниваем
    assertEquals(oneTest, total);
    verify(transactionRepository).findByUserId(1L);


}

@Test
void getMonthSumOfUser_takeASomeMonthAndGetSumOfTransactionsOfUser_SumOnMonth() {

    User testUser = new User();
    testUser.setId(1L);


    Transaction testOne = new Transaction();
    testOne.setAmount(5000.0);
    testOne.setTypeTransaction("INCOME");
    testOne.setUser(testUser);
    testOne.setDateTransaction(LocalDateTime.of(2025,9,10,15,0));

    Transaction testTwo = new Transaction();
    testTwo.setAmount(300.0);
    testTwo.setTypeTransaction("EXPENSE");
    testTwo.setUser(testUser);
    testTwo.setDateTransaction(LocalDateTime.of(2025,9,11,11,0));

    Double anss = 5300.0;

    List<Transaction> testTest = List.of(testOne,testTwo);

    //мокаем репозиторий
    when(transactionRepository.findByUserIdAndMonth(1L,9)).thenReturn(testTest);

    //достаем из сервиса то что сохранили
    Double an = transactionService.getMonthSumOfUser(1L,9);

    //сравниваем
    assertEquals(an, anss);
    verify(transactionRepository).findByUserIdAndMonth(1L,9);


}

@Test
void getSumOfEveryCategory_getAllCategoryOfUserAndGetSumEveryOfThis_statisticsOfSumCategory() {
//возвращаем мапу, принимаем лист обджект
    List<Object[]> total = new ArrayList<>();
    Object[] food = new Object[2];
    food[0]="Еда";
    food[1]=1000.0;

    Object[] house= new Object[2];
    house[0]="комуналка";
    house[1]=300.0;

    Object[] fun = new Object[2];
    fun[0] = "Развлечения";
    fun[1] = 100.0;

    total.add(food);
    total.add(house);
    total.add(fun);

    Map<String,Double> resultOne = Map.of(
            "Еда",1000.0,
            "комуналка",300.0,
            "Развлечения",100.0
    );



    when(transactionRepository.findByUserIdAndMonthGroupedByCategory(1L,9)).thenReturn(total);

    Map<String,Double> result = transactionService.getSumOfEveryCategory(1L,9);

    assertEquals(resultOne, result);
    verify(transactionRepository).findByUserIdAndMonthGroupedByCategory(1L,9);

}


}
