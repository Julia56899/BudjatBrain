package BudjetBrain.User;

import BudjetBrain.Transaction.Transaction;
import BudjetBrain.Transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/register")
    public User registerUser(@RequestBody UserDTO userDTO) {
        User createUser = userService.registerUser(userDTO);
        return createUser;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();

    }

    @GetMapping("/users/{id}")
    public User userId(@PathVariable Long id) {
        return userService.getUserId(id);
    }


    //транзакции полльзователя
    @GetMapping("/users/{id}/transactions")
    public List<Transaction> getTransactionsOfUser(@PathVariable Long id) {
        return transactionService.getTransactionsByIdUser(id);
    }
}

//список всех пользователей
//конкретный пользователь по айди
