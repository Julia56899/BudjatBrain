package BudjetBrain;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Double amount;
    private String typeTransaction;
    private Long categoryId;
    private Long userId;
    private String description;


    public TransactionDTO () {}

    public TransactionDTO (Double amount, String typeTransaction, Long categoryId, Long userId){
        this.amount = amount;
        this.typeTransaction = typeTransaction;
        this.categoryId = categoryId;
        this.userId = userId;
    }
    //геттеры и сеттеры
    public Double getAmount () {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getTypeTransaction() {
        return typeTransaction;

    } public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }
    public Long getCategoryId() {
        return categoryId;
    }

    public Long getUserId() {
        return userId;
    }
    public String getDescription() {
        return description;
    } public void setDescription(String description) {
        this.description = description;
    }
}



