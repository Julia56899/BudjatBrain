package BudjetBrain.Category;

//в дто аннотации не добавляются

public class CategoryDTO {

    private String type;
    private String name;
    private String color;

    public CategoryDTO() {}

    public CategoryDTO (String type, String name,String color) {
        this.type = type;
        this.name = name;
        this.color = color;
    }
//геттеры и сеттеры
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
}


