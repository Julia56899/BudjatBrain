package User;


import BudjetBrain.User.User;
import BudjetBrain.User.UserDTO;
import BudjetBrain.User.UserRepository;
import BudjetBrain.User.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDTO userDTO;
    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_getListOfAllUsersInBD_getUsers()  {
        //создаем два юзера и лист с ними
        User userOne = new User();
        userOne.setEmail("tt@mail.ru");
       User userTwo = new User();
       userTwo.setEmail("rr@mail.ru");

       List<User> testUsers = List.of(userOne,userTwo);
       //мокаем репозитоий
        when(userRepository.findAll()).thenReturn(testUsers);

        //дальше из сервиса вызываем найденное
        List <User> total = userService.getAllUsers();

        assertEquals(testUsers, total);
        verify(userRepository).findAll();

    }


    @Test
    void getUserId_getIdOfUserAndFindThisUser() {
        User testUser = new User();
        testUser.setId(1L);


        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        //из сервиса вызываем найденное
        User total = userService.getUserId(1L);

        assertEquals(testUser.getId(), total.getId());
        verify(userRepository).findById(1L);
    }


    @Test
    void  registerUser_getUserDTOAndRegisterThis_registerThisUser() {
        UserDTO testUser = new UserDTO();
        testUser.setUsername("Юлия");
        testUser.setEmail("test@mail.ru");
        testUser.setPassword("12345");


        User total = new User();
        total.setUsername(testUser.getUsername());
        total.setEmail(testUser.getEmail());
        total.setPassword(testUser.getPassword());


        //вызываем реопзиторий регистрация этого юзера
        when(userRepository.save(any(User.class))).thenReturn(total);

        //из сервиса вызываем то что нашли
        User totalOne = userService.registerUser(testUser);

        assertEquals(totalOne.getUsername(), total.getUsername());
        assertEquals(totalOne.getEmail(),total.getEmail());
        verify(userRepository).save(any(User.class));
    }

}
