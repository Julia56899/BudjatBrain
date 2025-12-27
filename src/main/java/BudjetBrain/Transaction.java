package BudjetBrain;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String typeTransaction;

    private LocalDateTime dateTransaction;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    private Category category;


    public Transaction () {}

    public Transaction(Double amount, String typeTransaction, Category category, User user){
        this.amount = amount;
        this.typeTransaction = typeTransaction;
        this.category= category;
        this.dateTransaction = LocalDateTime.now();
        this.user = user;

    }
    //геттеры и сеттеры
    public Long getId() {
        return id;
    } public Double getAmount () {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    } public String getTypeTransaction() {
        return typeTransaction;

    } public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public LocalDateTime getDateTransaction() {
        return  dateTransaction;
    }
    public User getUser() {
        return user;
    }
    }

