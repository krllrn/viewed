package com.krllrn.viewed.repositories;

import com.krllrn.viewed.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    //Find by chatID
    @Query(value = "select * from users where chat_id = ?1", nativeQuery = true)
    User findByChatId(Long chatId);
}
