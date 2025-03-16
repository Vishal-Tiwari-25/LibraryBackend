package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.User;
import com.example.LibraryManagementSystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> addUsers(List<User> users){
        return userRepository.saveAll(users);
    }
    public User addUser(User user){
        return userRepository.save(user);
    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }
    public Optional<User> getUserByName(String name){
        return userRepository.findByName(name);
    }
    public List<User> getUsersByGender(String gender){
        return userRepository.findAllByGender(gender);
    }
    @Transactional
    public void updateUser(User user,Long userId){
//        String name=user.getName();
//        String gender= user.getGender();
//        LocalDate dob=user.getDob();
//        String email=user.getEmail();
//        String phone=user.getPhone();
//        Long user_id=user.getUserId();
//       userRepository.updateUser(name,email,dob,gender,phone,user_id);
        int rowsUpdated = userRepository.updateUser(
                user.getName(),
                user.getEmail(),
                user.getDob(),
                user.getGender(),
                user.getPhone(),
                userId
        );

        System.out.println("Rows Updated: " + rowsUpdated);  // ✅ Debugging

        if (rowsUpdated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found or no changes made!");
        }
    }
}
