package BudjetBrain.Category;


import BudjetBrain.Transaction.TransactionService;
import BudjetBrain.User.User;
import BudjetBrain.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/categories/{userId}")
    public List<Category> allCategoryOfUser(@PathVariable Long userId) {
        User user = userService.getUserId(userId);
        return categoryService.allCategoryOfUser(userId);
    }

    @GetMapping("/categories/{userId}/income") // доходы
    public List<Category> typeCategoryOfIncome(@PathVariable Long userId) {
        User user = userService.getUserId(userId);
        return categoryService.typeCategoryOfUser(user, "INCOME");
    }

    @GetMapping("/categories/{userId}/expense") //расходы
    public List<Category> typeCategoryOfExpense(@PathVariable Long userId) {
        User user = userService.getUserId(userId);
        return categoryService.typeCategoryOfUser(user, "EXPENSE");
    }

    //проверка - есть ли уже такая категория у пользователя - чтобы не повторялось - да и в целом сохранение категории!!
    @PostMapping("/category/{userId}")
    public Category saveCategoryOfUser(@PathVariable Long userId,
                                       @RequestBody CategoryDTO dto) {
        User user = userService.getUserId(userId);
//проверка дубликата - метод булеан
        if ((categoryService.repeatCategoryOfUser(dto.getName(), user)) == true) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Категория " + dto.getName() + " уже существует");
        }
//создаем и сохраняем
        Category category = new Category(dto.getType(), dto.getName(), user);
        //1.создать новую категорию из данных дто категори и юзер
        categoryRepository.save(category);
        return category;

    }

    @GetMapping("/users/{userId}/categories")
    public Map<String, Double> calculateUserCategory (@PathVariable Long userId) {
        return transactionService.calculateCategory(userId);
}
@GetMapping("/users/{userId}/categories/{categoryName}")
public  Double BalanceCategoryOfUser(@PathVariable Long userId,
                                     @PathVariable String categoryName) {
        return transactionService.getSumOneCategory(userId,categoryName);
}

}




