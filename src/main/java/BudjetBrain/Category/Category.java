package BudjetBrain.Category;


import BudjetBrain.User.User;
import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {
    //id, тип, наименование, цвет,юзер, лимит по каждой категории
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String name;
    private String color;
    private Double limitExpense;

@ManyToOne
@JoinColumn(name = "user_id")
    private User user;

public Category() {}
//в конструктор только то без чего объект не сможет существовать !! (лимит - юзер может и не устан)
    public Category (String type, String name,User user) {
    this.type = type;
    this.name = name;
    this.user = user;
    }
//геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
    return type;
    } public void setType(String type) {
    this.type = type;
    } public String getName() {
    return  name;
    } public void setName(String name) {
    this.name = name;
    }
    public String getColor() {
    return color;}
    public void setColor (String color) {
        this.color = color;
        }
        public Double getLimitExpense() {
    return  limitExpense;
    } public void setLimitExpense(Double limitExpense) {
    this.limitExpense = limitExpense;
    } public User getUser() {
    return user;
    } public void setUser(User user) {
    this.user = user;
    }
}
