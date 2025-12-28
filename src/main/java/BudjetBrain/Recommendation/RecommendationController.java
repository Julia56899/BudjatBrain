package BudjetBrain.Recommendation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendationController {


    @Autowired
    private RecommendationService recommendationService;


    @GetMapping("/recommendation/BigWaste/{userId}")
    public String recomendationOfBigWaste(@PathVariable Long userId) {
        String recomendationOfBigWaste = recommendationService.recomendationOfBigWaste(userId);
        return recomendationOfBigWaste;
    }

    @GetMapping("/recommendation/ExpenseOfFood/{userId}")
    public String recommendationOfExpenseOfFood(@PathVariable Long userId) {
        String recommendationOfExpenseOfFood = recommendationService.recommendationOfExpenseOfFood(userId);
        return recommendationOfExpenseOfFood;
    }

    @GetMapping("/recommendation/OfSavings/{userId}")
    public String recommendationOfSavings(@PathVariable Long userId) {
        String recommendationOfSavings = recommendationService.recommendationOfSavings(userId);
        return recommendationOfSavings;
    }

    @GetMapping("/recommendation/ImpulsePurchases/{userId}")
    public String recommendationOfImpulsePurchases(@PathVariable Long userId) {
        String recommendationOfImpulsePurchases = recommendationService.recommendationOfImpulsePurchases(userId);
        return recommendationOfImpulsePurchases;
    }

    @GetMapping("/recommendation/IncomeExpenseImbalance/{userId}")
    public String recommendationOfIncomeExpenseImbalance(@PathVariable Long userId) {
        String recommendationOfIncomeExpenseImbalance = recommendationService.recommendationOfIncomeExpenseImbalance(userId);
        return recommendationOfIncomeExpenseImbalance;
    }

    @GetMapping("/recommendation/noExpenseOfCategory/{userId}")
    public String recommendationCategoriesWithoutExpenses (@PathVariable Long userId) {
        String noExpense = recommendationService.recommendationCategoriesWithoutExpenses(userId);
        return noExpense;
    }

    }
