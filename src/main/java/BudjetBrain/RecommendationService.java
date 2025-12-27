package BudjetBrain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class RecommendationService {

    @Autowired
    private TransactionRepository transactionRepository;


    //неожиданная крупная трата
    public String recomendationOfBigWaste(Long userId) {
        //определяем текущий месяц
        int currentMonth = LocalDate.now().getMonthValue();
        //получаю все транзакции пользователя за месяц
        List<Transaction> transactionsForRecomendationOne = transactionRepository.findByUserIdAndMonth(userId, currentMonth);
        //прохожусь по ним - считаю сумму доходов за месяц
        Double sumIncome = 0.0;
        for (Transaction tt : transactionsForRecomendationOne) {
            if ("INCOME".equals(tt.getTypeTransaction())) {
                sumIncome += tt.getAmount();
            }
        }
        //считаем большие расходы
        Double threshold = sumIncome / 2;

        for (Transaction tran : transactionsForRecomendationOne) {
            if ("EXPENSE".equals(tran.getTypeTransaction()) && tran.getAmount() > threshold) {
                return "Неожиданная крупная трата," + "вы потратили" + tran.getAmount() + "что на 50% больше вашего ежемесячного дохода" + sumIncome + "рублей" + "сделайте финансовую подушку";
            }

        }
        return null;
    }

    //Траты на "Еду" > 40% от всех расходов
    public String recommendationOfExpenseOfFood(Long userId) {
        //определяем текущий месяц
        int nowMonth = LocalDate.now().getMonthValue();
        List<Transaction> transactionsForRecomendationOne = transactionRepository.findByUserIdAndMonth(userId, nowMonth);
        //прохожусь по ним - считаю сумму расходов за месяц
        Double sumExpense = 0.0;
        for (Transaction tt : transactionsForRecomendationOne) {
            if ("EXPENSE".equals(tt.getTypeTransaction())) {
                sumExpense += tt.getAmount();
            }
        }
        List<Object[]> categoryAll = transactionRepository.findByUserIdAndMonthGroupedByCategory(userId, nowMonth);
        Map<String, Double> result = new HashMap<>();
        for (Object[] o : categoryAll) {
            String categoryName = (String) o[0];
            Double categorySum = (Double) o[1];
            result.put(categoryName, categorySum);
        }
        Double categoryFood = result.get("Еда");
//мы получили сумму расходов по еде, теперь сравниваем отношение - итоговые расходы и еду
        if (categoryFood != null && categoryFood > (sumExpense * 0.4)) {
            return "Вы тратите слишком много на еду" + categoryFood + "а это больше 40% ваших расходов" + sumExpense;
        }

        return null;
    }

    //нет накоплений - Нет INCOME транзакций с категорией "Накопления" за этот месяц
    public String recommendationOfSavings(Long userId) {
        //выводим все транзакции за три месяца - то есть это текущий месяц и два назад
        int currentMonth = LocalDate.now().getMonthValue();
        List<Transaction> currentTransaction = transactionRepository.findByUserIdAndMonth(userId, currentMonth);
        Boolean foundSaving = false;
//проходим по всем расходам  -если не совпадает ничего с "накопления" - то рекомендация
        for (Transaction t : currentTransaction) {
            if ("EXPENSE".equals(t.getTypeTransaction()) && "Накопления".equals(t.getCategory().getName())) {
                foundSaving = true;
            }
        }

        if (!foundSaving) {
            return "в этом месяцу нет накоплений, рекомендуем откладывать 10% от своего дохода";
        }
        return null;
    }

    //много мелких трат (< 1000) на "Развлечения" в один день
    public String recommendationOfImpulsePurchases(Long userId) {
        int currentMonth = LocalDate.now().getMonthValue();
        List<Transaction> currentTransactions = transactionRepository.findByUserIdAndMonth(userId, currentMonth);
//считаем доход
        Double sumIncome = 0.0;
        for (Transaction tran : currentTransactions) {
            if ("INCOME".equals(tran.getTypeTransaction())) {
                sumIncome += tran.getAmount();
            }
        }
        if (sumIncome == 0.0) {
            return null;
        }

        int daysInMonth = LocalDate.now().lengthOfMonth();
        Double incomeOfMonth = sumIncome / daysInMonth;

        //это у нас просто список всех дат
        Map<LocalDate, List<Transaction>> mapOfDays = new HashMap<>();

        for (Transaction t : currentTransactions) {
            LocalDate date = t.getDateTransaction().toLocalDate();
            if (!mapOfDays.containsKey(date)) {
                mapOfDays.put(date, new ArrayList<>());

            }
            mapOfDays.get(date).add(t);
        }

        for (LocalDate date : mapOfDays.keySet()) {
            List<Transaction> transactionsOfDay = mapOfDays.get(date);
            Double sumPurchases = 0.0;
            for (Transaction ttt : transactionsOfDay) {
                if ("EXPENSE".equals(ttt.getTypeTransaction()) && "Развлечения".equals(ttt.getCategory().getName())) {
                    sumPurchases += ttt.getAmount();
                }
                if (sumPurchases > incomeOfMonth * 0.3) {
                    return "Было потрачено много на разлечения в этот день" + ttt.getDateTransaction() + "а именно 30% от вашего ежедвевного дохода, потрачено было" + sumPurchases;
                }
            }

        }
        return null;
    }
    // Дисбаланс доходов/расходов
    //Условие: расходы в этом месяце сотсавляют 90% от дохода
    public String recommendationOfIncomeExpenseImbalance(Long userId) {
        //текущий месяц
        int currentMonth = LocalDate.now().getMonthValue();
        List<Transaction> currentTransactions = transactionRepository.findByUserIdAndMonth(userId, currentMonth);
        //считаем доход и расход и сравниваем
        Double incomeSum = 0.0;
        for (Transaction t : currentTransactions) {
            if ("INCOME".equals(t.getTypeTransaction())) {
                incomeSum += t.getAmount();
            }
        }
            Double expenseSum = 0.0;
            for (Transaction te : currentTransactions) {
                if ("EXPENSE".equals(te.getTypeTransaction())) {
                    expenseSum += te.getAmount();
                }
            }
                if (expenseSum > incomeSum * 0.9) {
                    return "в этом месяце вы слишком много потратили" + expenseSum + "а это 90% вашего дохода за этот месяц, будьте аккуратнее в тратах";
                }

        return null;
        }

   //Ключевые категории без трат
//Условие: Нет трат на "Жильё" или "Здоровье" 2 месяца при наличии доходов
public String recommendationCategoriesWithoutExpenses (Long userId) {

    Boolean noExpenseOne = noExpenseOfCategory(userId, "Жилье", 12);
    Boolean noExpenseTwo = noExpenseOfCategory(userId, "Здоровье", 12);

    if (noExpenseOne) {
        return "В этом месяце у вас не было трат на Жилье, проверьте платежи";
    }
    if (noExpenseTwo) {
        return "В этом месяце у вас не было трат на здоровье, проверьте платежи";
    }
    return null;
}
private Boolean noExpenseOfCategory(Long userId, String categoryName, int currentMonth) {

    List<Transaction> currentTransactions = transactionRepository.findByUserIdAndMonth(userId, currentMonth);
for(Transaction t:currentTransactions ) {
    if("EXPENSE".equals(t.getTypeTransaction()) && t.getCategory().getName().equals(categoryName) ) {
        return false;
    }
}
    return true;

    }
}













