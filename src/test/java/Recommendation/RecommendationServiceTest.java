package Recommendation;


import BudjetBrain.Category.Category;
import BudjetBrain.Recommendation.RecommendationService;
import BudjetBrain.Transaction.Transaction;
import BudjetBrain.Transaction.TransactionRepository;
import BudjetBrain.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    @InjectMocks
    private RecommendationService recommendationService;

    @Mock
    private TransactionRepository transactionRepository;


    @Test
    void recomendationOfBigWaste_ifFoundBigExpenseSumThenReturnRecommendation_warningAboutOfBigWaste() {
        int currentMonth = LocalDate.now().getMonthValue();
        //создаем моковые транзакции


        User testUser = new User();
        testUser.setId(1L);

        Transaction testOne = new Transaction();
        testOne.setAmount(10000.0);
        testOne.setTypeTransaction("INCOME");
        testOne.setUser(testUser);


        Transaction testTwo = new Transaction();
        testTwo.setAmount(8000.0);
        testTwo.setTypeTransaction("EXPENSE");
        testTwo.setUser(testUser);

        List<Transaction> testTest = List.of(testOne, testTwo);

        //мокаем репозиторий
        when(transactionRepository.findByUserIdAndMonth(1L, currentMonth)).thenReturn(testTest);

//дальше вызваем сам сервис
        String result = recommendationService.recomendationOfBigWaste(1L);


        assertNotNull(result);
        assertTrue(result.contains("Неожиданная крупная трата,"));
        assertTrue(result.contains("8000.0"));
        assertTrue(result.contains("10000.0"));

        verify(transactionRepository).findByUserIdAndMonth(1L, currentMonth);


    }

    @Test
    void recommendationOfExpenseOfFood_foundExpenseSumOnFoodInMonthAndCompareItWithSumIncome_warningAboutOfBifWasteOnFood() {
        int currentMonth = LocalDate.now().getMonthValue();
        Category sup = new Category();
        sup.setName("Зарплата");

        Category supTwo = new Category();
        sup.setName("Еда");


        User testUser = new User();
        testUser.setId(1L);

        Transaction testOne = new Transaction();
        testOne.setAmount(10000.0);
        testOne.setTypeTransaction("INCOME");
        testOne.setUser(testUser);
        testOne.setCategory(sup);


        Transaction testTwo = new Transaction();
        testTwo.setAmount(8000.0);
        testTwo.setTypeTransaction("EXPENSE");
        testTwo.setUser(testUser);
        testTwo.setCategory(supTwo);
        List<Transaction> testTest = List.of(testOne, testTwo);

        List<Object[]> resultMap = new ArrayList<>();
             resultMap.add(new Object[]{   "Зарплата", 10000.0,});
             resultMap.add(new Object[]{ "Еда", 8000.0});

//мокаем репозиторий
        when(transactionRepository.findByUserIdAndMonth(1L, currentMonth)).thenReturn(testTest);
        when(transactionRepository.findByUserIdAndMonthGroupedByCategory(1L, currentMonth)).thenReturn(resultMap);

        //вызываем сервис
        String result = recommendationService.recommendationOfExpenseOfFood(1L);

        assertNotNull(result);
        assertTrue(result.contains("Вы тратите слишком много на еду"));
        assertTrue(result.contains("8000.0"));

        verify(transactionRepository).findByUserIdAndMonth(1L, currentMonth);
        verify(transactionRepository).findByUserIdAndMonthGroupedByCategory(1L, currentMonth);

    }

    @Test
    void recommendationOfSavings_ifInThisMonthNoSavingsReturnRecommendation_warningAboutAbsenceSavings() {
        int currentMonth = LocalDate.now().getMonthValue();

        Category sup = new Category();
        sup.setName("Зарплата");

        Category supTwo = new Category();
        sup.setName("Еда");

        User testUser = new User();
        testUser.setId(1L);


        Transaction testOne = new Transaction();
        testOne.setAmount(10000.0);
        testOne.setTypeTransaction("INCOME");
        testOne.setUser(testUser);
        testOne.setCategory(sup);


        Transaction testTwo = new Transaction();
        testTwo.setAmount(8000.0);
        testTwo.setTypeTransaction("EXPENSE");
        testTwo.setUser(testUser);
        testTwo.setCategory(supTwo);
        List<Transaction> testTest = List.of(testOne, testTwo);

//мокаем репозиторий
        when(transactionRepository.findByUserIdAndMonth(1L, currentMonth)).thenReturn(testTest);

//вызываем сервис
        String result = recommendationService.recommendationOfSavings(1L);

        assertNotNull(result);
        assertTrue(result.contains("в этом месяце нет накоплений, рекомендуем откладывать 10% от своего дохода"));


        verify(transactionRepository).findByUserIdAndMonth(1L, currentMonth);
    }

    @Test
    void recommendationOfImpulsePurchases_ifFoundManyWastesOnFunNeedReturnRecommendation_warningAboutManyWastesOfFunnyNeed() {
        LocalDate today = LocalDate.now();

        int currentMonth = LocalDate.now().getMonthValue();

        Category sup = new Category();
        sup.setName("Зарплата");

        Category supTwo = new Category();
        supTwo.setName("Развлечения");

        User testUser = new User();
        testUser.setId(1L);

        Transaction testOne = new Transaction();
        testOne.setAmount(5000.0);
        testOne.setTypeTransaction("INCOME");
        testOne.setUser(testUser);
        testOne.setCategory(sup);
        testOne.setDateTransaction(today.atStartOfDay());

        Transaction testTwo = new Transaction();
        testTwo.setAmount(1000.0);
        testTwo.setTypeTransaction("EXPENSE");
        testTwo.setUser(testUser);
        testTwo.setCategory(supTwo);
        testTwo.setDateTransaction(today.atStartOfDay());
        Transaction testThree = new Transaction();
        testThree.setAmount(1700.0);
        testThree.setTypeTransaction("EXPENSE");
        testThree.setUser(testUser);
        testThree.setCategory(supTwo);
        testThree.setDateTransaction(today.atStartOfDay());
        Transaction testFour = new Transaction();
        testFour.setAmount(400.0);
        testFour.setTypeTransaction("EXPENSE");
        testFour.setUser(testUser);
        testFour.setCategory(supTwo);
        testFour.setDateTransaction(today.atStartOfDay());


        List<Transaction> testTest = List.of(testOne, testTwo, testThree, testFour);

        //мокаем репозиторий
        when(transactionRepository.findByUserIdAndMonth(1L, currentMonth)).thenReturn(testTest);
//вызываем сервис
        String result = recommendationService.recommendationOfImpulsePurchases(1L);

        assertNotNull(result);
        assertTrue(result.contains("Было потрачено много на развлечения в этот день"));
        assertTrue(result.contains("3100.0"));


        verify(transactionRepository).findByUserIdAndMonth(1L, currentMonth);


    }

    @Test
    void recommendationOfIncomeExpenseImbalance_ifFoundInThisMonthImbalanceThenReturnRecommendation_warningAboutOfBigSumIncomes() {
        int currentMonth = LocalDate.now().getMonthValue();

        Category sup = new Category();
        sup.setName("Зарплата");

        Category supTwo = new Category();
        supTwo.setName("Развлечения");

        User testUser = new User();
        testUser.setId(1L);


        Transaction testOne = new Transaction();
        testOne.setAmount(5000.0);
        testOne.setTypeTransaction("INCOME");
        testOne.setUser(testUser);
        testOne.setCategory(sup);


        Transaction testTwo = new Transaction();
        testTwo.setAmount(4700.0);
        testTwo.setTypeTransaction("EXPENSE");
        testTwo.setUser(testUser);
        testTwo.setCategory(supTwo);


        List<Transaction> testList = List.of(testOne, testTwo);
//мокаем репозиторий
        when(transactionRepository.findByUserIdAndMonth(1L, currentMonth)).thenReturn(testList);
        //вызываем сервис
        String result = recommendationService.recommendationOfIncomeExpenseImbalance(1L);
        assertNotNull(result);
        assertTrue(result.contains("в этом месяце вы слишком много потратили"));
        assertTrue(result.contains("4700.0"));

        verify(transactionRepository).findByUserIdAndMonth(1L, currentMonth);

    }


    @Test
    void recommendationCategoriesWithoutExpenses_ifNotFoundWastesOfCategoryHomeAndHealthThenReturnRecommendation_warningAboutOfAbsenceNeedCategory() {


        int currentMonth = LocalDate.now().getMonthValue();

        Category sup = new Category();
        sup.setName("Зарплата");

        Category supTwo = new Category();
        supTwo.setName("Развлечения");

        User testUser = new User();
        testUser.setId(1L);


        Transaction testOne = new Transaction();
        testOne.setAmount(5000.0);
        testOne.setTypeTransaction("INCOME");
        testOne.setUser(testUser);
        testOne.setCategory(sup);


        Transaction testTwo = new Transaction();
        testTwo.setAmount(4700.0);
        testTwo.setTypeTransaction("EXPENSE");
        testTwo.setUser(testUser);
        testTwo.setCategory(supTwo);


        List<Transaction> testList = List.of(testOne, testTwo);


//мокаем репозиторий
        when(transactionRepository.findByUserIdAndMonth(1L, currentMonth)).thenReturn(testList);

        //вызываем сервис
        String result = recommendationService.recommendationCategoriesWithoutExpenses(1L);

        assertNotNull(result);
        assertTrue(result.contains("В этом месяце у вас не было трат на Жилье, проверьте платежи"));
        assertTrue(result.contains("В этом месяце у вас не было трат на здоровье, проверьте платежи"));


    }
}