package BudjetBrain;

import BudjetBrain.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

import static java.util.Arrays.stream;

@Service
public class TransactionService {


    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryPredictor categoryPredictor;


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    //находит транзакцию по ее айди
    public Transaction getTransactionId(Long id) {
        for (Transaction transaction : transactionRepository.findAll()) {
            if (transaction.getId().equals(id)) {
                return transaction;
            }
        }
        return null;
    }

    public List<Transaction> getTransactionsByIdUser(Long userId) {
        return transactionRepository.findByUserId(userId);

    }

    //принимает транзакции дто
    public Transaction addTransaction(TransactionDTO dto) {
        System.out.println("CategoryPredictor is null? " + (categoryPredictor == null));
        //найти юзер по дто гетюзер  в списке юзерс
        List<User> users = userRepository.findAll();

        System.out.println("Всего пользователей:" + users.size());
        System.out.println("Айди пользователя, которого ищем:" + dto.getUserId());
        for (User user : users) {
            System.out.println("Пользователь айди:" + user.getId() + ", email =" + user.getEmail());
        }

        Optional<User> userOptional = userRepository.findById(dto.getUserId());
        if (userOptional.isEmpty()) {
            return null;
        }
        User foundUser = userOptional.get();


        Category category = null;
        String categoryName = null;
        //дальше блок по получению категории
        //дальше ищем в бд эту категорию, что она уже была сохранена
        if (dto.getCategoryId() != null) {
            Optional<Category> categoryOptional = categoryRepository.findById(dto.getCategoryId());
            if (categoryOptional.isPresent()) {
                category = categoryOptional.get();
            } else if (categoryOptional.isEmpty()) {
                String description = dto.getDescription();
                //дальше по этому описанию ищем в категори предиктор категорию
                categoryName = categoryPredictor.predictCategory(description);
//ищет в бд категорию с названием еда у этого пользователя
                category = categoryRepository.findByNameAndUser(categoryName, foundUser);
            }
            if (category == null) {
                String categoryType = dto.getTypeTransaction();
                category = new Category(categoryType, categoryName, foundUser);
            }
        } else if (dto.getCategoryId() == null) {
            //мл-категоризация
//получаем описание из трназакции
            String description = dto.getDescription();
            //дальше по этому описанию ищем в категори предиктор категорию
            categoryName = categoryPredictor.predictCategory(description);
//ищет в бд категорию с названием еда у этого пользователя
            category = categoryRepository.findByNameAndUser(categoryName, foundUser);
        }
        if (category == null) {
            String categoryType = dto.getTypeTransaction();
            category = new Category(categoryType, categoryName, foundUser);
            categoryRepository.save(category);
        }

        Transaction transaction = new Transaction(dto.getAmount(), //все что приходит в дто транзактион - мы пишем в сервис
                dto.getTypeTransaction(),
                category,          //категори ищется в бд
                foundUser
        );
        transactionRepository.save(transaction);
        return transaction;
    }

    // метод - выводящий сумму доходов, расходов и баланс сейчас
    public BalanceDTO calculateBalance(Long userId) {
        //список всех транзакций берем по айди юзера и фильтруем по доходу и расходу
        List<Transaction> transactionOfUserForBalance = transactionRepository.findByUserId(userId);
        Double income = 0.0;
        Double expense = 0.0;
        Double balance = 0.0;
        for (Transaction t : transactionOfUserForBalance) {
            if ("INCOME".equals(t.getTypeTransaction())) {
                income += t.getAmount();
            } else if ("EXPENSE".equals(t.getTypeTransaction())) {
                expense += t.getAmount();
            }
        }
        balance = income - expense;
        BalanceDTO result = new BalanceDTO(balance, income, expense);

        return result;
    }

    public Map<String, Double> calculateCategory(Long userId) {
        //метод - разделение доходов и расходов по категориям
        Map<String, Double> calculateCategory = new HashMap<>();
        //берем все транзакции пользователя
        List<Transaction> transactionsForCalculateCategory = transactionRepository.findByUserId(userId);
//для каждой транзакции выделяем тип, наименование, сумму
        //каждая категория хранит уже итоговую сумму - а не все транзакции
        for (Transaction tc : transactionsForCalculateCategory) {
            //беру все данные из тк - это готовый объект транзактион
            Category category = tc.getCategory();
            String categoryName = category.getName();
            Double amount = tc.getAmount();
            String categoryType = tc.getTypeTransaction();

            //берем текущую сумму из каждой категории
            Double currentSum = calculateCategory.getOrDefault(categoryName, 0.0);
            if ("INCOME".equals(categoryType)) {
                currentSum += amount;
            } else if ("EXPENSE".equals(categoryType)) {
                currentSum -= amount;
            }
            //обновляем мапу - пут
            calculateCategory.put(categoryName, currentSum);
        }
        return calculateCategory;
    }

    //вывод одной категории
    //название категории пользователь указал в запросе
    public Double getSumOneCategory(Long userId, String categoryName) {
        Map<String, Double> allCategories = calculateCategory(userId);
//наша задача просто получить сумму
        Double getSumOneCategory = allCategories.get(categoryName);
        return getSumOneCategory;
    }

    //получение транзакций по месяцам
    public Double getMonthSumOfUser(Long userId, int month) {
        List<Transaction> getSumOfUser = transactionRepository.findByUserIdAndMonth(userId, month);
        Double sumMonth = getSumOfUser.stream().mapToDouble(Transaction::getAmount).sum();

return sumMonth;

}
public Map<String, Double> getSumOfEveryCategory(Long userId, int month) {
        List<Object[]> getSumOfEveryCategory = transactionRepository.findByUserIdAndMonthGroupedByCategory(userId,month);
Map<String,Double> result = new HashMap<>();

for(Object[] o:getSumOfEveryCategory) {
    String categoryName = (String) o[0];
    Double categorySum = (Double) o[1];
    result.put(categoryName, categorySum);
}
    return result;


}
        }






