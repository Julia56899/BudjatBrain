package BudjetBrain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {
    @Autowired
    private  TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;


    @PostMapping("/transaction")
    public Transaction addTransaction (@RequestBody TransactionDTO dto) {
       Transaction transaction = transactionService.addTransaction(dto);
        return transaction;
    }
  @GetMapping ("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
  }
//получение транзакции по ее айди
@GetMapping("/transaction/{id}")
public Transaction getTransaction(@PathVariable Long id) {
    return transactionService.getTransactionId(id);
}

@DeleteMapping("/transaction/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
}

@GetMapping("/transaction/{id}/balance")
public BalanceDTO calculateBalance(@PathVariable Long id){
        return transactionService.calculateBalance(id);
}


@GetMapping("/transaction/{id}/balance/category/{month}")
public Map<String, Double> getSumOfEveryCategory(@PathVariable Long id,
                                                 @PathVariable  int month) {
      return transactionService.getSumOfEveryCategory(id, month);
}



}
