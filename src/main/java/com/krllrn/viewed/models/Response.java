package com.krllrn.viewed.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Response {
    private String keyword;

    private Integer pagesCount;

    private Integer searchFilmsCountResult;

    private List<FilmSearchResponse> films;
}
