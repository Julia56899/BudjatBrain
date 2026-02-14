package Category;


import BudjetBrain.BudjetBrainApplication;
import BudjetBrain.Category.*;
import BudjetBrain.User.User;
import BudjetBrain.User.UserService;
import BudjetBrain.Transaction.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration(classes = BudjetBrainApplication.class)
@WebMvcTest(controllers = CategoryController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class CategoryControllerTest {

@Autowired
    private MockMvc mockMvc;

@MockBean
    private UserService userService;
@MockBean
    private CategoryService categoryService;

@MockBean
    private CategoryRepository categoryRepository;
@MockBean
    private TransactionService transactionService;
@MockBean
private CategoryDTO categoryDTO;

@Test
    void allCategoryOfUser_getListCategoryOfUser_categoriesOfUser() throws  Exception{
    User testUser = new User();
    testUser.setId(1L);

    Category categoryOne = new Category();
    categoryOne.setName("Еда");
    categoryOne.setType("EXPENSE");
    categoryOne.setUser(testUser);

    Category categoryTwo = new Category();
    categoryTwo.setName("Зарплата");
    categoryTwo.setType("INCOME");
    categoryTwo.setUser(testUser);

    List<Category> testTest = List.of(categoryOne, categoryTwo);

    //вызваю сервис
    when(userService.getUserId(1L)).thenReturn(testUser);
    when(categoryService.allCategoryOfUser(1L)).thenReturn(testTest);
    //вызываю эндпоинт
    mockMvc.perform(
         get("/categories/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Еда"))
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[1].user.id").value(1L));


verify(userService).getUserId(1L);
            verify(categoryService).allCategoryOfUser(1L);


}
@Test
    void typeCategoryOfIncome_getCategotiesTypeOfIncome_categoriesUserOfIncome() throws Exception {
    User testUser = new User();
    testUser.setId(1L);

    Category categoryOne = new Category();
    categoryOne.setName("Фриланс");
    categoryOne.setType("INCOME");
    categoryOne.setUser(testUser);

    Category categoryTwo = new Category();
    categoryOne.setName("Зарплата");
    categoryOne.setType("INCOME");
    categoryOne.setUser(testUser);

    List<Category> testTest = List.of(categoryOne, categoryTwo);

    //вызываем что нужно
    when(userService.getUserId(1L)).thenReturn(testUser);
    when(categoryService.typeCategoryOfUser(testUser, "INCOME")).thenReturn(testTest);

    //вызываем ендпоинт
    mockMvc.perform(
            get("/categories/1/income"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].type").value("INCOME"))
    .andExpect(jsonPath("$[*].user").exists());

    verify(userService).getUserId(1L);
    verify(categoryService).typeCategoryOfUser(testUser, "INCOME");

}

@Test
    void typeCategoryOfExpense_getCategoriesTypeOfExpense_categoriesUserOfExpense() throws Exception {

    User testUser = new User();
    testUser.setId(1L);

    Category categoryOne = new Category();
    categoryOne.setName("Еда");
    categoryOne.setType("EXPENSE");
    categoryOne.setUser(testUser);

    Category categoryTwo = new Category();
    categoryTwo.setName("Развлечения");
    categoryTwo.setType("EXPENSE");
    categoryTwo.setUser(testUser);

    List<Category> testTest = List.of(categoryOne, categoryTwo);

//вызываем все что нужно
    when(userService.getUserId(1L)).thenReturn(testUser);
    when(categoryService.typeCategoryOfUser(testUser, "EXPENSE")).thenReturn(testTest);

    //вызваем еднпоинт
    mockMvc.perform(
            get("/categories/1/expense"))
            .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$.[1].name").value("Развлечения"))
                    .andExpect(jsonPath("$[0].user.id").value(1L));


verify(userService).getUserId(1L);
verify(categoryService).typeCategoryOfUser(testUser, "EXPENSE");

}

@Test
    void saveCategoryOfUser_getSavingCategoryOfUser_savingCategory() throws Exception {

    User testUser = new User();
    testUser.setId(1L);

    CategoryDTO categoryTest = new CategoryDTO();
    categoryTest.setType("EXPENSE");
    categoryTest.setName("Еда");
    categoryTest.setColor("000000");


    Category categoryOne = new Category();
    categoryOne.setType("EXPENSE");
    categoryOne.setName("Еда");
    categoryOne.setUser(testUser);

    //вызываем все что нужно
    when(userService.getUserId(1L)).thenReturn(testUser);
    when(categoryService.repeatCategoryOfUser("Еда", testUser)).thenReturn(false);

    //вызываем эндпоинт
    mockMvc.perform(
            post("/category/1")
            .contentType(MediaType.APPLICATION_JSON)
            //в контент пишем то что отправляется в теле запроса  - то есть дто
            .content("{\"type\":\"EXPENSE\",\"name\":\"Еда\",\"color\":\"000000\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Еда"))
            .andExpect(jsonPath("$.type").value("EXPENSE"))
            .andExpect(jsonPath("$.user").exists());


    verify(userService).getUserId(1L);
    verify(categoryService).repeatCategoryOfUser("Еда", testUser);

    }

    @Test
    void calculateUserCategory_getSumOfEveryCategoryOfUser_statisticsSumOfCategory() throws Exception {

        User testUser = new User();
        testUser.setId(1L);

        Map<String, Double> testMap =Map.of(
            "Еда", 3000.0,
            "Зарплата", 5000.0,
            "Красота", 400.0
    );

    //вызываем сервис
        when(transactionService.calculateCategory(testUser.getId())).thenReturn(testMap);
        //вызываем еднпоинт
        mockMvc.perform(
                get("/users/1/categories"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Еда").value(3000.0))
                .andExpect(jsonPath("$.Красота").value(400.0));
        verify(transactionService).calculateCategory(testUser.getId());


    }

@Test
    void BalanceCategoryOfUser_getSumNeedCategoryOfUser_sumOfCategory() throws Exception {
    User testUser = new User();
    testUser.setId(1L);

    Category categoryOne = new Category();
    categoryOne.setType("EXPENSE");
    categoryOne.setName("Еда");
    categoryOne.setUser(testUser);

    Double total = 5000.0;

    //вызывем сервис
   when( transactionService.getSumOneCategory(testUser.getId(),"Еда")).thenReturn(total);

   //вызывем ендпоинт
    mockMvc.perform(
            get("/users/1/categories/Еда"))

            .andExpect(status().isOk())
            .andExpect(content().string("5000.0"));


    verify(transactionService).getSumOneCategory(testUser.getId(),"Еда");

}

}
