package com.krllrn.viewed.repositories;

import com.krllrn.viewed.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmsRepository extends JpaRepository<Film, Long> {
    //Find by chatID
    @Query(value = "select * from films where name_ru = ?1 or name_en = ?1", nativeQuery = true)
    Film findByName(String name);
}
