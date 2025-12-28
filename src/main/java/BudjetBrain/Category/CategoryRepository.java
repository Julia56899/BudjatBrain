package BudjetBrain.Category;

import BudjetBrain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//все категории пользователя
List<Category> findByUserId(Long userId);
    //категории пользователя по типу - доход/расход
List<Category> findByUserAndType(User user, String type); //логика в сервисе будет
    //проверка - есть ли уже такая категория у пользователя - чтобы не повторялось
Category findByNameAndUser(String name, User user);
}