package Category;


import BudjetBrain.Category.Category;
import BudjetBrain.Category.CategoryService;
import BudjetBrain.Category.CategoryRepository;
import BudjetBrain.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {


    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void allCategoryOfUser_getListOfAllCategoryOfUser_listOfNeedCategory() {

        User testUser = new User();
        testUser.setId(1L);

        Category categoryOne = new Category();
        categoryOne.setName("Еда");
        categoryOne.setType("EXPENSE");
        categoryOne.setUser(testUser);

        Category categoryTwo = new Category();
        categoryOne.setName("Зарплата");
        categoryOne.setType("INCOME");
        categoryOne.setUser(testUser);

        List<Category> testTest = List.of(categoryOne, categoryTwo);
        //вызываю репозиторий
        when(categoryRepository.findByUserId(1L)).thenReturn(testTest);
        //вызываю сервис
        List<Category> total = categoryService.allCategoryOfUser(1L);
        //сравниваю
        assertEquals(total,testTest);
        verify(categoryRepository).findByUserId(1L);
    }

    @Test
    void typeCategoryOfUser_findCategoryOfUserOnType_categoryOnType() {

        User testUser = new User();
        testUser.setId(1L);

        Category categoryOne = new Category();
        categoryOne.setName("Еда");
        categoryOne.setType("EXPENSE");
        categoryOne.setUser(testUser);

        Category categoryTwo = new Category();
        categoryOne.setName("Зарплата");
        categoryOne.setType("INCOME");
        categoryOne.setUser(testUser);

        List<Category> testExpense= List.of(categoryOne);
        List<Category> testIncome = List.of(categoryTwo);

        //вызываю репозитоорий
        when(categoryRepository.findByUserAndType(testUser,"INCOME")).thenReturn(testIncome);

        List<Category> income = categoryService.typeCategoryOfUser(testUser,"INCOME");

        assertEquals(income,testIncome);
        verify(categoryRepository).findByUserAndType(testUser,"INCOME");

    }
    @Test
    void repeatCategoryOfUser_chekIfCategoriesOfUserIsRepeat_chekRepeatCategory() {
User testUser = new User();
testUser.setId(1L);

Category testCategory = new Category();
testCategory.setUser(testUser);
testCategory.setName("Еда");

//мокаем репозиторий
        when(categoryRepository.findByNameAndUser("Еда", testUser)).thenReturn(testCategory);
        //вызываю сервис
        Boolean result= categoryService.repeatCategoryOfUser("Еда", testUser);
        //дальше сравниваю
        assertTrue(result);
        verify(categoryRepository).findByNameAndUser("Еда", testUser);



    }

}
