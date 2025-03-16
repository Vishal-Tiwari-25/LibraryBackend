package com.example.LibraryManagementSystem.Controller;
import com.example.LibraryManagementSystem.Entity.Admin;
import com.example.LibraryManagementSystem.Repository.AdminRepository;
import com.example.LibraryManagementSystem.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/get-admins")
    public ResponseEntity<List<Admin>> getAdmins(){
        return ResponseEntity.ok(adminService.getAdmins());
    }
//   @PostMapping("/add-admins")
//   public ResponseEntity<List<Admin>> addAdmins(@RequestBody List<Admin> admins){
//       return ResponseEntity.ok(adminService.adminSignups(admins));
//   }
   @PostMapping("/add-admin")
   public ResponseEntity<Admin> addAdmin(@RequestBody Admin admin){
        return ResponseEntity.ok(adminService.adminSignup(admin.getAdminName(), admin.getAdminPass()));
   }
   @PostMapping("/login")
   public ResponseEntity<String> loginAdmin(@RequestBody Admin admin){
        return adminService.adminLogin(admin.getAdminName(), admin.getAdminPass());
   }
   @DeleteMapping("/delete-admin/{id}")
    public void deleteAdmins(@RequestParam Long id){
        adminService.deleteAdmin(id);
   }
}
