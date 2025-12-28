package BudjetBrain.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
     List<Transaction> findByUserId(Long userId);

     //наход итоговых транзакций по месяцам
     @Query("SELECT t FROM Transaction t WHERE t.user.id=:userId AND MONTH(t.dateTransaction)=:month")
          List<Transaction>findByUserIdAndMonth(@Param("userId") Long userId,
                                               @Param("month") int month);

//это выдает все суммы по категориям
    @Query("SELECT t.category.name,SUM(t.amount) FROM Transaction t WHERE t.user.id=:userId AND MONTH(t.dateTransaction)=:month GROUP BY t.category.name")
     List<Object[]> findByUserIdAndMonthGroupedByCategory (@Param("userId") Long userId,
                                                        @Param("month") int month);


    List <Transaction> findByUserIdAndDateTransaction(Long userId, LocalDate date);

     }


