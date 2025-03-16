package com.example.LibraryManagementSystem.Repository;

import com.example.LibraryManagementSystem.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
Optional<User> findByName(String name);
List<User> findAllByGender(String gender);

@Modifying
@Transactional
@Query(
        value = "UPDATE User u SET u.name=:name,u.email=:email,u.dob=:dob, u.gender=:gender, u.phone=:phone where u.userId=:userId "
)
    int updateUser(@Param("name") String name, @Param("email") String email, @Param("dob")LocalDate dob,
                    @Param("gender") String gender, @Param("phone") String phone,@Param("userId") Long userId);
}
/*
dob date
email varchar(255)
gender varchar(255)
name varchar(255)
phone va
 */