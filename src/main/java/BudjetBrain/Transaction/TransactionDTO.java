package BudjetBrain.Transaction;

public class TransactionDTO {

    private Double amount;
    private String typeTransaction;
    private Long category;
    private Long user;
    private String description;


    public TransactionDTO () {}

    public TransactionDTO (Double amount, String typeTransaction, Long category, Long user){
        this.amount = amount;
        this.typeTransaction = typeTransaction;
        this.category = category;
        this.user = user;
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
    public Long getCategory() {
        return category;
    }
    public void setCategory(Long category) {
        this.category = category;
    }

    public Long getUser() {
        return user;
    }
//для тестов сделаем сеттер
    public void setUser(Long user) {
        this.user = user;
    }



    public String getDescription() {
        return description;
    } public void setDescription(String description) {
        this.description = description;
    }
}



