package com.example.notesbackend.account.repository;

import com.example.notesbackend.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class for account management.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     *
     * @param email
     * @return
     */
    @Query("SELECT a FROM Account a WHERE a.email = :email")
    Optional<Account> findByEmail(@Param("email") String email);

    /**
     *
     * @param username
     * @return
     */
    @Query("SELECT a FROM Account a WHERE a.username = :username")
    Optional<Account> findByUsername(@Param("username") String username);

    /**
     *
     * @param id
     * @return
     */
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findByAccountId(@Param("id") Long id);

    /**
     *
     * @param password
     * @param id
     * @return
     */
    @Modifying
    @Query("UPDATE Account a SET a.password = :password WHERE a.id = :id")
    int updatePassword(@Param("password") String password, @Param("id") int id);

    /**
     *
     * @param email
     * @param id
     * @return
     */
    @Modifying
    @Query("UPDATE Account a SET a.email = :email WHERE a.id = :id")
    int updateEmail(String email, int id);

    /**
     *
     * @param firstName
     * @param lastName
     * @param id
     * @return
     */
    @Modifying
    @Query("UPDATE Account a SET a.firstName = :firstName, a.lastName = :lastName WHERE a.id = :id")
    int updateName(String firstName, String lastName, int id);

}
