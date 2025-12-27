package BudjetBrain;
import BudjetBrain.User;

import BudjetBrain.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;


//здесь должен быть метод регистор - роегистрация пользоватея
@Service


public class UserService {


    @Autowired
    private UserRepository userRepository; //поменять все


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserId(Long id) {
        for(User user:userRepository.findAll()) {
            if(user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }


    public User registerUser (UserDTO userDTO) {
        System.out.println("Запрос пришел!" +userDTO.getEmail());
        //проверяем нет такого пользовталея в дто

        for(User currentUser:userRepository.findAll()) {
            if (userDTO.getEmail().equals(currentUser.getEmail())) {
                return null;
            }
        }
//создаем новый объект
        User newUser = new User(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword()
                );

        //сохраняем его
        userRepository.save(newUser);

        //возвращаем
        return newUser;

    }

}