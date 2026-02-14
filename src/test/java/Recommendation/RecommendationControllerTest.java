package Recommendation;


import BudjetBrain.BudjetBrainApplication;
import BudjetBrain.Recommendation.RecommendationController;
import BudjetBrain.Recommendation.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = BudjetBrainApplication.class)
@WebMvcTest(controllers = RecommendationController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecommendationService recommendationService;

    @Test
    void recomendationOfBigWaste_callingEndpointOfRecomendationOfBigWaste_warningAboutOfBigWaste() throws Exception {

        String ans = "Неожиданная крупная трата";
        //вызываем сервис
        when(recommendationService.recomendationOfBigWaste(1L)).thenReturn(ans);
        //вызываем эндпоинт
        mockMvc.perform(
                get("/recommendation/BigWaste/1"))

                        .andExpect(status().isOk())
                        .andExpect(content().string(ans));

        verify(recommendationService).recomendationOfBigWaste(1L);

    }
@Test
    void recommendationOfExpenseOfFood_callingEndpointOfrecommendationOfExpenseOfFood_warningAboutOfBifWasteOnFood() throws  Exception {

String ans ="Вы тратите слишком много на еду";

//вызываем сервис
    when(recommendationService.recommendationOfExpenseOfFood(1L)).thenReturn(ans);
    //вызываю эндпоинт
    mockMvc.perform(
       get("/recommendation/ExpenseOfFood/1"))
               .andExpect(status().isOk())
               .andExpect(content().string(ans));

       verify(recommendationService).recommendationOfExpenseOfFood(1L);


}

@Test
    void recommendationOfSavings_callingEndpointOfRecommendationOfSavings_warningAboutOfSavings() throws Exception {
        String ans = "в этом месяце нет накоплений, рекомендуем откладывать 10% от своего дохода";


        //вызываем сервис
    when(recommendationService.recommendationOfSavings(1L)).thenReturn(ans);
    //вызываю эндпоинт
    mockMvc.perform(
        get("/recommendation/OfSavings/1"))
            .andExpect(content().string(ans));


    verify(recommendationService).recommendationOfSavings(1L);
    }


    @Test
    void recommendationOfImpulsePurchases_callingEndpointOfRecommendationOfImpulsePurchases_warningAboutOfImpulsePurchases() throws Exception {
        String ans = "Было потрачено много на развлечения в этот день";
        //вызываем сервис
        when(recommendationService.recommendationOfImpulsePurchases(1L)).thenReturn(ans);

        //вызываю эндпоинт
        mockMvc.perform(
          get("/recommendation/ImpulsePurchases/1"))

                .andExpect(status().isOk())
                .andExpect(content().string(ans));

        verify(recommendationService).recommendationOfImpulsePurchases(1L);
    }

    @Test
    void recommendationOfIncomeExpenseImbalance_callingEndpointOfrecommendationOfIncomeExpenseImbalance_warningAboutOfImbalance() throws Exception {
        String ans = "в этом месяце вы слишком много потратили";
        //вызываем сервис
        when(recommendationService.recommendationOfIncomeExpenseImbalance(1L)).thenReturn(ans);

        //вызываю эндпоинт
        mockMvc.perform(
                get("/recommendation/IncomeExpenseImbalance/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(ans));

        verify(recommendationService).recommendationOfIncomeExpenseImbalance(1L);
    }
@Test
    void recommendationCategoriesWithoutExpenses_callingEndpointOfRecommendationOfCategoriesWithoutExpenses_warningAboutOfCategoriesWithoutExpenses() throws Exception {
        String ans = "В этом месяце у вас не было трат на Жилье, проверьте платежи";
        String ansA = "В этом месяце у вас не было трат на здоровье, проверьте платежи";


        //вызываем сервис
    when(recommendationService.recommendationCategoriesWithoutExpenses(1L)).thenReturn(ans +" "+ansA);
    //вызываю эндпоинт
    mockMvc.perform(
            get("/recommendation/noExpenseOfCategory/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("В этом месяце у вас не было трат на Жилье, проверьте платежи В этом месяце у вас не было трат на здоровье, проверьте платежи"));


    verify(recommendationService).recommendationCategoriesWithoutExpenses(1L);

}


}