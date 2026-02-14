package BudjetBrain.User;

import BudjetBrain.Transaction.Transaction;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")

public class User {
    //айди, юзернэйм, емайл, пароль, датарегистр, баланс карты

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;
    private String email;
    private String password;
    private LocalDateTime createDate;
    private Double balance;

    @OneToMany (mappedBy = "user")
private List<Transaction> transactions;


    public User () {}

    public User( String username,String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;

        this.createDate = createDate;
        this.balance = 0.0;
    }
public Long getId() {
        return id;
}
//сеттер чисто для тестов нужен
    public void setId(Long id) {
        this.id = id;
    }
//транзактионс для теста
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getUsername() {
        return username;
    } public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    } public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    } public void setPassword(String password) {
        this.password = password;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }public Double getBalance() {
        return balance;
    }
}
