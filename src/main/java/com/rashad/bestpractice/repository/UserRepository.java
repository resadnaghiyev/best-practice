package com.rashad.bestpractice.repository;

import com.rashad.bestpractice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber (String phoneNumber);

    @Transactional
    @Modifying
    @Query(value = "UPDATE public.users SET failed_attempt = failed_attempt + 1, block_time = CASE WHEN failed_attempt + " +
            "1 >= 3 THEN CURRENT_TIMESTAMP + INTERVAL '15 minutes' END, account_blocked = CASE WHEN failed_attempt + " +
            "1 >= 3 THEN true else false end where id = :userId", nativeQuery = true)
    void updateFailedAttempts(@Param("userId") long userId);

    @Transactional
    @Modifying
    @Query(value = "update public.users set failed_attempt = 0, account_blocked = false, block_time = null" +
            " where id = :userId", nativeQuery = true)
    void resetBlockAccountDetails(@Param("userId") long userId);
}
