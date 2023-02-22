package com.krllrn.viewed.bot;

import com.krllrn.viewed.mappers.FilmMapper;
import com.krllrn.viewed.models.*;
import com.krllrn.viewed.repositories.FilmsRepository;
import com.krllrn.viewed.repositories.UserFilmsRepository;
import com.krllrn.viewed.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@PropertySource("config.properties")
public class BotServiceImpl implements BotService {

    @Value("${kp.url}")
    String kpUrl;

    @Value("${kp.token}")
    String kpToken;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FilmsRepository filmsRepository;

    @Autowired
    private UserFilmsRepository userFilmsRepository;

    @Autowired
    private FilmMapper filmMapper;

    private final RestTemplate restTemplate;

    public BotServiceImpl(UsersRepository usersRepository, RestTemplate restTemplate) {
        this.usersRepository = usersRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public SendMessage startBot(long chatId, String username) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Привет, " + username + "! \nЯ бот, который поможет тебе вспомнить, какие фильмы и " +
                "сериалы у тебя просмотрены. \nОбязательно посмотри /help для того, чтобы узнать, как мной " +
                "пользоваться.");

        userRegistration(chatId, username);

        return sendMessage;
    }

    @Override
    public SendMessage addFilm(long chatId, String username, String filmName) {
        userRegistration(chatId, username);
        SendMessage sendMessage = new SendMessage();
        Film film = getFilm(filmName);
        if (film == null) {
            sendMessage.setChatId(chatId);
            sendMessage.setText("К сожалению, " + username + ", ничего не нашлось. \nПопробуй изменить запрос и " +
                    "повторить поиск.");
        } else {
            if (userFilmsRepository.findByChatIdAndFilmId(chatId, film.getFilmId()) != null) {
                sendMessage.setChatId(chatId);
                sendMessage.setText(username + "! В твоей фильмотеке уже есть: \n"
                        + "Название: " + film.getNameRu() + "\n"
                        + "Ссылка: " + film.getFilmUrl() + "\n");
            } else {
                UserFilm userFilm = new UserFilm();
                userFilm.setFilm(film);
                userFilm.setUser(usersRepository.findByChatId(chatId));
                userFilm.setAddDate(LocalDate.now());

                userFilmsRepository.save(userFilm);
                log.info("Save film: " + film.getNameRu() + " to user: " + username);
                sendMessage.setChatId(chatId);
                sendMessage.setText("Поздравляю, " + username + "! Фильмотека пополнилась: \n"
                        + "Название: " + film.getNameRu() + "\n"
                        + "Ссылка: " + film.getFilmUrl() + "\n");
            }
        }

        return sendMessage;
    }

    @Override
    public SendMessage findFilm(long chatId, String filmName) {
        SendMessage sendMessage = new SendMessage();
        Film film = filmsRepository.findByName(filmName);
        if (film != null && userFilmsRepository.findByChatIdAndFilmId(chatId, film.getFilmId()) != null) {
            sendMessage.setChatId(chatId);
            sendMessage.setText("В фильмотеке найден: \n"
                    + "Название: " + film.getNameRu() + "\n"
                    + "Ссылка: " + film.getFilmUrl() + "\n");
        } else {
            sendMessage.setChatId(chatId);
            sendMessage.setText("В фильмотеке ничего не найдено :( \nНе скоротать ли вечер за просмотром " + filmName +
                    " ?");
        }
        return sendMessage;
    }

    @Override
    public SendMessage deleteFilm(long chatId, String username, String filmName) {
        SendMessage sendMessage = new SendMessage();
        Film film = filmsRepository.findByName(filmName);
        UserFilm userFilm = userFilmsRepository.findByChatIdAndFilmId(chatId, film.getFilmId());
        if (userFilm != null) {
            userFilmsRepository.delete(userFilm);
            sendMessage.setChatId(chatId);
            sendMessage.setText("Из фильмотеки удалено: \n"
                    + "Название: " + film.getNameRu() + "\n"
                    + "Ссылка: " + film.getFilmUrl() + "\n");
        } else {
            sendMessage.setChatId(chatId);
            sendMessage.setText("В фильмотеке ничего не найдено :( \nНе скоротать ли вечер за просмотром: \n"
                            + getFilm(filmName).getFilmUrl());
        }
        return sendMessage;
    }

    @Override
    public SendMessage lastN(long chatId, String number) {
        SendMessage sendMessage = new SendMessage();
        if (number == null || number.isEmpty() || number.isBlank()) {
            wrongMessage(chatId);
        } else if (!NumberUtils.isParsable(number) || Long.parseLong(number) < 1) {
            wrongMessage(chatId);
        }
        List<UserFilm> userFilms = userFilmsRepository.findAllByChatId(chatId).stream()
                .sorted(Comparator.comparing(UserFilm::getAddDate).reversed())
                .limit(Long.parseLong(number))
                .collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        long count = 0;
        for (UserFilm u : userFilms) {
            count++;
            String resultString = count + ". " + u.getFilm().getNameRu()+ " " + u.getFilm().getFilmUrl() + "\n";
            stringBuilder.append(resultString);
        }
        sendMessage.setChatId(chatId);
        sendMessage.setText(stringBuilder.toString());

        return sendMessage;
    }

    @Override
    public SendMessage deleteAll(long chatId, String username) {
        SendMessage sendMessage = new SendMessage();

        for (UserFilm u : userFilmsRepository.findAllByChatId(chatId)) {
            userFilmsRepository.delete(u);
        }

        sendMessage.setChatId(chatId);
        sendMessage.setText("Вся фильмотека очищена! Можно начинать смотреть заново :)");
        return sendMessage;
    }

    @Override
    public SendMessage help(long chatId, String username) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Я умею следующее: \n"
                + "/start " + "- регистрация;" + "\n"
                + "/add {Название} " + "- добавление фильма/сериала в фильмотеку;" + "\n"
                + "/find {Название} " + "- поиск по своей фильмотеке среди просмотренного;" + "\n"
                + "/last {Количество} " + "- вывод последних N просмотренных фильмов/сериалов, где N - количество;" +
                "\n"
                + "/delete {Название} " + "- удаление из просмотренного;" + "\n"
                + "/clear " + "- очистить полностью просмотренное;" + "\n"
                + "/help " + "- вывод данной справки." + "\n");

        return sendMessage;
    }

    @Override
    public SendMessage wrongMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Из нас двоих кто-то что-то сделал не так. Да поможет нам /help !");

        return sendMessage;
    }

    private Film getFilm(String filmName) {
        Film filmToReturn = new Film();
        if (filmsRepository.findByName(filmName) == null) {
            String filmEnc = URLEncoder.encode(filmName, StandardCharsets.UTF_8);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(kpUrl + "/api/v2.1/films/search-by-keyword")
                    .queryParam("keyword", filmEnc);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-KEY", kpToken);
            HttpEntity<?> entity = new HttpEntity<Object>(headers);

            ResponseEntity<Response> response = restTemplate.exchange(builder.build(true).toUri(),
                    HttpMethod.GET, entity, Response.class);
            if (response.getBody() != null) {
                FilmSearchResponse filmSearchResponse = response.getBody().getFilms().get(0);
                FilmDTO filmDTO = filmMapper.filmSearchToDto(filmSearchResponse, filmSearchResponse.getFilmId());
                filmsRepository.save(filmMapper.filmDtoToEntity(filmDTO));
                filmToReturn = filmMapper.filmDtoToEntity(filmDTO);
            } else {
                filmToReturn = null;
            }
        } else {
            filmToReturn = filmsRepository.findByName(filmName);
        }
        return filmToReturn;
    }

    private void userRegistration(long chatId, String username) {
        if (usersRepository.findByChatId(chatId) == null) {
            User newUser = new User(chatId, username);
            usersRepository.save(newUser);
            log.info("Save new user: " + newUser.getUsername());
        }
    }
}
