package com.krllrn.viewed.repositories;

import com.krllrn.viewed.models.UserFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFilmsRepository extends JpaRepository<UserFilm, Long> {
    //Find by chatID and filmID
    @Query(value = "select * from user_films where chat_id = ?1 and film_id = ?2", nativeQuery = true)
    UserFilm findByChatIdAndFilmId(Long chatId, Long filmId);

    //Find all by chatID
    @Query(value = "select * from user_films where chat_id = ?1", nativeQuery = true)
    List<UserFilm> findAllByChatId(Long chatId);

    @Query(value = "select * from user_films order by add_date desc limit 1", nativeQuery = true)
    UserFilm findFirstByOrderByAddDateDesc();
}
