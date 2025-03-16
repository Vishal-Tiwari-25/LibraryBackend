package com.example.LibraryManagementSystem.Controller;
import com.example.LibraryManagementSystem.Entity.User;
import com.example.LibraryManagementSystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("get-users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }
    @GetMapping("get-users/by-id/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUser(id));
    }
    @GetMapping("get-users/by-name/{name}")
    public ResponseEntity<Optional<User>> getUserByName(@PathVariable String name){
        return ResponseEntity.ok(userService.getUserByName(name));
    }
    @GetMapping("get-users/by-gender/{gender}")
    public ResponseEntity<List<User>> getUserByGender(@PathVariable String gender){
        return ResponseEntity.ok(userService.getUsersByGender(gender));
    }
    @PostMapping("/add-users")
    public ResponseEntity<List<User>> addUsers(@RequestBody List<User> users){
          return ResponseEntity.ok(userService.addUsers(users));
   }
   @PostMapping("/add-user")
   public ResponseEntity<User> addUser(@RequestBody User user){
        return ResponseEntity.ok(userService.addUser(user));
   }
   @PutMapping("/update-user/{id}")
    public ResponseEntity<String> updateUser(@RequestBody User user,@PathVariable Long id){
        userService.updateUser(user,id);
        return  ResponseEntity.ok("Updation Successfull!!");
   }

}
