package com.example.notesbackend.account.repository;

import com.example.notesbackend.account.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t FROM Token t INNER JOIN Account a ON t.account.id = a.id WHERE account.id = :accountId")
    List<Token> findAllValidTokensByAccount(@Param("accountId") Long accountId);

    Optional<Token> findByToken(String token);

}
