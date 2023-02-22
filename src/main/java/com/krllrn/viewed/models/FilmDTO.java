package com.krllrn.viewed.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class FilmDTO {
    private Long filmId;

    private String nameRu;

    private String nameEn;

    private Integer filmYear;

    private String description;

    private String rating;

    private String filmUrl;
}
