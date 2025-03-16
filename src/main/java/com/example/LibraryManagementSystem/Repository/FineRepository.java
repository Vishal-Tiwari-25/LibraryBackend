package com.example.LibraryManagementSystem.Repository;

import com.example.LibraryManagementSystem.Entity.Fine;
import com.example.LibraryManagementSystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FineRepository extends JpaRepository<Fine,Long> {

//    boolean existsByUserAndReason(User user, String lateReturn);
//  Corrected method: Check if user has unpaid fines
@Query("SELECT COUNT(f) > 0 FROM Fine f WHERE f.user.userId = :userId AND f.isPaid =:isPaid")
boolean existsByUserIdAndIsPaid(@Param("userId") Long userId,@Param("isPaid") boolean isPaid);
   // boolean existsByUserIdAndIsPaid(Long userId, boolean b);

//    Fine findByUserIdAndReason(Long userId, String lateReturn);
@Query("SELECT f FROM Fine f WHERE f.user.id = :userId AND f.reason = :reason")
Fine findByUserIdAndReason(@Param("userId") Long userId, @Param("reason") String reason);

    @Query(
            value = "SELECT f from Fine f where f.isPaid=:isPaid AND f.user.userId=:userId"
    )
    List<Fine> findAllByIsPaid(@Param("isPaid") boolean isPaid,@Param("userId") Long userId);

//    // ✅ Find unpaid fines for a user
//    @Query("SELECT f FROM Fine f WHERE f.isPaid = false AND f.user.userId = :userId")
//    List<Fine> findUnpaidFinesByUserId(@Param("userId") Long userId);
}
