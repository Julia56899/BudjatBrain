package BudjetBrain.Category;


import BudjetBrain.User.User;
import BudjetBrain.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List <Category> allCategoryOfUser(Long userId) {
    //все категории пользователя
        return categoryRepository.findByUserId(userId);
}
public List <Category> typeCategoryOfUser (User user, String type) {
    //категории пользователя по типу - доход/расход
    return categoryRepository.findByUserAndType(user, type);
}
    //проверка - есть ли уже такая категория у пользователя - чтобы не повторялось
public boolean repeatCategoryOfUser(String name, User user) {
    return categoryRepository.findByNameAndUser(name, user)!=null;
    }

}
