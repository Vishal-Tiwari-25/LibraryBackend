package com.example.LibraryManagementSystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
//@Getter
//@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    @Column(name = "admin_name" ,nullable=false, unique = true)
    private String adminName;

    @Column(name = "admin_password", nullable = false)
    private String adminPass;

}
