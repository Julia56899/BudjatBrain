package BudjetBrain.Balance;




public class BalanceDTO {

    private Double balance;
    private Double income;
    private Double expense;

    public  BalanceDTO() {}

    public BalanceDTO(Double balance,Double income, Double expense) {
        this.balance = balance;
        this.income = income;
        this.expense = expense;
    }

    //геттеры и сеттеры
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;

    }
    public Double getExpense() {
        return expense;
    }
    public Double getIncome() {
        return income;
    }
}
