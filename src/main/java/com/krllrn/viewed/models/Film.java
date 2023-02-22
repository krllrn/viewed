package com.krllrn.viewed.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "films")
public class Film {
    @Id
    @Column(name = "film_id")
    private Long filmId;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "film_year")
    private Integer filmYear;

    private String description;

    private String rating;

    @Column(name = "url")
    private String filmUrl;

}
