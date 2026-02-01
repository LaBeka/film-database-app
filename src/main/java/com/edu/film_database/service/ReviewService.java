package com.edu.film_database.service;

import com.edu.film_database.dto.request.CreateReviewRequestDto;
import com.edu.film_database.dto.request.UpdateReviewRequestDto;
import com.edu.film_database.dto.response.FilmReviewResponseDto;
import com.edu.film_database.dto.response.ReviewResponseDto;
import com.edu.film_database.exception.FilmNotFoundException;
import com.edu.film_database.exception.ReviewNotFoundException;
import com.edu.film_database.exception.ReviewNotUsersOwnReviewException;
import com.edu.film_database.exception.UserNotFoundException;
import com.edu.film_database.model.Film;
import com.edu.film_database.model.Review;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.FilmRepository;
import com.edu.film_database.repo.ReviewRepository;
import com.edu.film_database.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository review_repo;
    private final UserRepository user_repo;
    private final FilmRepository film_repo;

    public ReviewService(ReviewRepository reviewRepo, UserRepository userRepo, FilmRepository filmRepo) {
        review_repo = reviewRepo;
        user_repo = userRepo;
        film_repo = filmRepo;
    }

    public ReviewResponseDto getSpecificReview(int index){
        Optional<Review> review_tmp = review_repo.findById(index);
        if(review_tmp.isPresent()){
            return convertFromReview(review_tmp.get());
        }
        throw new ReviewNotFoundException("Cannot find review with id " + index);
    }

    public List<FilmReviewResponseDto> getAllReviews(){
        List<Film> film_tmp = film_repo.findAll();
        if(!film_tmp.isEmpty()){
            return film_tmp.stream()
                    .map(this::convertFromFilm).toList();
        }
        return new ArrayList<>();
    }

    public FilmReviewResponseDto getByFilm(int id){
        Optional<Film> film_tmp = film_repo.findById(id);
        if(film_tmp.isPresent()){
            return convertFromFilm(film_tmp.get());
        }
        throw new FilmNotFoundException("Cannot find the film with id " + id);
    }

    public List<FilmReviewResponseDto> getByUserName(String email){
        Optional<User> user_tmp = user_repo.findByEmail(email);

        if(user_tmp.isPresent()){
            List<Film> film_tmp = review_repo.findByUser(user_tmp.get())
                    .stream().map(Review::getFilm).distinct().toList();

            return film_tmp.stream()
                    .map(film -> convertFromUserFilms(
                            user_tmp.get(), film)).toList();
        }

        throw new UserNotFoundException("Cannot find the user with email " + email);
    }

    private FilmReviewResponseDto convertFromUserFilms(User user, Film film){
        return new FilmReviewResponseDto(
                film.getId(),
                review_repo.findByUserAndFilm(user, film)
                        .stream().map(review ->
                                getReviewsUserFilm(review.getId())).toList()
        );
    }

    private ReviewResponseDto getReviewsUserFilm(int id){
        Review review_tmp = review_repo.findById(id).get();
        return convertFromReview(review_tmp);
    }

    private FilmReviewResponseDto convertFromFilm(Film film){
        List<Review> review_tmp = review_repo.findByFilm(film);
        if(!review_tmp.isEmpty()){
            return new FilmReviewResponseDto(
                    film.getId(),
                    review_tmp.stream()
                            .map(this::convertFromReview).toList()
            );
        }
        return new FilmReviewResponseDto(
                film.getId(),
                new ArrayList<>()
        );
    }

    public FilmReviewResponseDto createReview(Principal principal, CreateReviewRequestDto dto){
        Optional<Film> film_tmp = film_repo.findById(dto.getFilmId());
        Optional<User> user_tmp = user_repo.findByEmail(principal.getName());
        if(film_tmp.isPresent()){
            if(user_tmp.isEmpty()){
                throw new UserNotFoundException("User with email " + principal.getName() +
                        " does not exist, cannot create review");
            }
            Review review_tmp = review_repo.save(
                                new Review(
                                dto.getText(),
                                LocalDate.now(),
                                dto.getScore(),
                                user_tmp.get(),
                                film_tmp.get()
            ));
            return convertFromFilmReview(review_tmp);
        }
        throw new FilmNotFoundException("Cannot find the film with id " + dto.getFilmId());
    }

    public FilmReviewResponseDto updateReview(Principal principal, UpdateReviewRequestDto dto){
        Optional<User> user_tmp = user_repo.findByEmail(principal.getName());
        Optional<Review> review_tmp = review_repo.findById(dto.getReviewIndex());

        if(review_tmp.isPresent()){
            if(user_tmp.isEmpty()) {
                throw new UserNotFoundException("User with email " + principal.getName() +
                        " does not exist cannot, update review");
            }
            if(review_tmp.get().getUser().equals(user_tmp.get())){
                updateReviewFields(review_tmp.get(), dto);
                return convertFromFilmReview(review_tmp.get());
            }
            throw new ReviewNotUsersOwnReviewException(
                    "Cannot change other users review");
        }
        throw new ReviewNotFoundException("Cannot update non existing review");
    }

    private void updateReviewFields(Review review, UpdateReviewRequestDto dto){
        if(dto.getText() != null){
            review.setText(dto.getText());
        }
        if(dto.getScore() > 0){
            review.setScore(dto.getScore());
        }
        review_repo.save(review);
    }

    public FilmReviewResponseDto deleteReviewUser(int index, Principal principal){
        User user_tmp = user_repo.findByEmail(principal.getName()).get();
        Optional<Review> review_tmp = review_repo.findById(index);

        if(review_tmp.isPresent()){
            if(review_tmp.get().getUser().equals(user_tmp)){
                review_repo.delete(review_tmp.get());
                return convertFromFilmReview(review_tmp.get());
            }
            throw new ReviewNotUsersOwnReviewException(
                    "Cannot delete other users review");
        }
        throw new ReviewNotFoundException("Cannot delete a review " +
                "that does not exist");
    }

    public FilmReviewResponseDto deleteReviewAdmin(int index){
        Optional<Review> review_tmp = review_repo.findById(index);
        if(review_tmp.isPresent()){
            review_repo.delete(review_tmp.get());
            return convertFromFilmReview(review_tmp.get());
        }
        throw new ReviewNotFoundException("Cannot delete a review " +
                "that does not exist");
    }

    private FilmReviewResponseDto convertFromFilmReview(Review review){
        return new FilmReviewResponseDto(review.getFilm().getId(),
                List.of(convertFromReview(review)));
    }

    private ReviewResponseDto convertFromReview(Review review){
        return new ReviewResponseDto(
                review.getId(),
                review.getUser().getUsername(),
                review.getUser().getEmail(),
                review.getText(),
                review.getDate(),
                review.getScore()
        );
    }
}
