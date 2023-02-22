package com.krllrn.viewed.mappers;

import com.krllrn.viewed.models.Film;
import com.krllrn.viewed.models.FilmDTO;
import com.krllrn.viewed.models.FilmSearchResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.krllrn.viewed.ViewedApplication.KP_URL;

@Component
public class FilmMapper {

    @Autowired
    private final ModelMapper mapper;

    public FilmMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public FilmDTO filmSearchToDto(FilmSearchResponse filmSearchResponse, Long filmId) {
        FilmDTO filmDTO = mapper.map(filmSearchResponse, FilmDTO.class);
        filmDTO.setFilmUrl(KP_URL + filmId.toString());
        return filmDTO;
    }

    public Film filmDtoToEntity(FilmDTO filmDTO) {
        return mapper.map(filmDTO, Film.class);
    }
}
