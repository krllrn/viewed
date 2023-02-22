package com.krllrn.viewed.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FilmSearchResponse {
    private Long filmId;

    private String nameRu;

    private String nameEn;

    private String type;

    private String year;

    private String description;

    private String filmLength;

    private List<Country> countries;

    private List<Genre> genres;

    private String rating;

    private Integer ratingVoteCount;

    private String posterUrl;

    private String posterUrlPreview;

}
