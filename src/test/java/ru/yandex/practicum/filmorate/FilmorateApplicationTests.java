package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)

class FilmorateApplicationTests {
    private final JdbcTemplate jdbcTemplate;

    private User createTestUser() {
        User newUser = new User();
        newUser.setId(1);
        newUser.setName("Ivan");
        newUser.setLogin("oldLogin");
        newUser.setEmail("qwerty@mail.ru");
        newUser.setBirthday(LocalDate.of(1990, 1, 1));
        return newUser;
    }

    private Film createTestFilm() {
        Film newFilm = new Film();
        newFilm.setId(1);
        newFilm.setName("Rick and Morty");
        newFilm.setDescription("Science makes sense, family doesnt");
        newFilm.setDuration(1000);
        newFilm.setReleaseDate(LocalDate.of(2013, 1, 1));
        MPARating mpa = new MPARating();
        mpa.setId(1);
        newFilm.setMpa(mpa);
        return newFilm;
    }

    @Test
    public void testCreateAndFindUserById() {
        User newUser = createTestUser();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.createUser(newUser);

        User savedUser = userStorage.findUserById(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testUpdateUser() {
        User newUser = createTestUser();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.createUser(newUser);
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName("Ivan");
        updatedUser.setLogin("newLogin");
        updatedUser.setEmail("qwerty@mail.ru");
        updatedUser.setBirthday(LocalDate.of(1990, 1, 1));

        User savedUser = userStorage.updateUser(updatedUser);

        assertThat(savedUser.getLogin())
                .isEqualTo(updatedUser.getLogin());
    }

    @Test
    public void testGetUsers() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User user = createTestUser();
        userStorage.createUser(user);

        Collection<User> listOfUsers = userStorage.getUsers();

        assertThat(listOfUsers.size())
                .isEqualTo(1);
    }

    @Test
    public void testGetFriendsOfUser() {
        User newUser = createTestUser();
        Set<Integer> friends = new HashSet<>(0);
        newUser.setFriends(friends);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.createUser(newUser);

        User friend = new User();
        friend.setId(2);
        friend.setName("Maxim");
        friend.setLogin("newLogin");
        friend.setEmail("qwerty@mail.ru");
        friend.setBirthday(LocalDate.of(1990, 1, 1));
        Set<Integer> fri = new HashSet<>(0);
        friend.setFriends(fri);
        userStorage.createUser(friend);

        userStorage.addFriend(1, 2);

        assertThat(1)
                .isEqualTo(userStorage.getListOfFriends(1).size());
    }

    @Test
    public void testDeleteFriendsOfUser() {
        User newUser = createTestUser();
        Set<Integer> friends = new HashSet<>(0);
        newUser.setFriends(friends);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.createUser(newUser);

        User friend = new User();
        friend.setId(2);
        friend.setName("Maxim");
        friend.setLogin("newLogin");
        friend.setEmail("qwerty@mail.ru");
        friend.setBirthday(LocalDate.of(1990, 1, 1));
        Set<Integer> fri = new HashSet<>(0);
        friend.setFriends(fri);
        userStorage.createUser(friend);

        userStorage.addFriend(1, 2);

        assertThat(1)
                .isEqualTo(userStorage.getListOfFriends(1).size());

        userStorage.deleteFriend(1,2);

        assertThat(0)
                .isEqualTo(userStorage.getListOfFriends(0).size());
    }

    @Test
    public void testCreateAndFindFilmById() {
        Film newFilm = createTestFilm();
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.addFilm(newFilm);

        Film savedFilm = filmStorage.getFilmById(1);

        assertThat(savedFilm.getName())
                .isNotNull()
                .isEqualTo(newFilm.getName());
    }

    @Test
    public void testUpdateFilm() {
        Film newFilm = createTestFilm();
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.addFilm(newFilm);
        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("Rick and Morty");
        updatedFilm.setDescription("Science makes sense");
        updatedFilm.setDuration(1000);
        updatedFilm.setReleaseDate(LocalDate.of(2013, 1, 1));
        MPARating mpa = new MPARating();
        mpa.setId(1);
        updatedFilm.setMpa(mpa);


        Film savedFilm = filmStorage.updateFilm(updatedFilm);

        assertThat(savedFilm.getDescription())
                .isEqualTo(updatedFilm.getDescription());
    }

    @Test
    public void testGetFilms() {
        Film newFilm = createTestFilm();
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.addFilm(newFilm);

        Collection<Film> listOfFilms = filmStorage.getFilms();

        assertThat(listOfFilms.size())
                .isEqualTo(1);
    }

    @Test
    public void testAddAndDeleteLike() {
        User newUser = createTestUser();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.createUser(newUser);
        Film newFilm = createTestFilm();
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.addFilm(newFilm);

        filmStorage.addLike(1,1);

        assertThat(1)
                .isEqualTo(filmStorage.getFilmById(1).getLikes().size());

        filmStorage.deleteLike(1,1);
        assertThat(0)
                .isEqualTo(filmStorage.getFilmById(1).getLikes().size());
    }
}
