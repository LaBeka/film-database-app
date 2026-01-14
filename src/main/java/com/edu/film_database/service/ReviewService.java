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

    public List<FilmReviewResponseDto> getAllReviews(){
        List<Film> film_tmp = film_repo.findAll();
        if(!film_tmp.isEmpty()){
            return film_tmp.stream()
                    .map(this::convertFromFilm).toList();
        }
        return new ArrayList<>();
    }

    public FilmReviewResponseDto getByFilm(String title){
        Optional<Film> film_tmp = film_repo.findByTitle(title);
        if(film_tmp.isPresent()){
            return convertFromFilm(film_tmp.get());
        }
        throw new FilmNotFoundException("Cannot find the film named " + title);
    }

    public List<FilmReviewResponseDto> getByUserName(String userName){
        Optional<User> user_tmp = user_repo.findByUserName(userName);
        if(user_tmp.isPresent()){

        }
        throw new UserNotFoundException("Cannot find the user named " + userName);
    }

    private FilmReviewResponseDto ConvertFromUserFilms(User user, Film film){
        return null;
    }

    private FilmReviewResponseDto convertFromFilm(Film film){
        List<Review> review_tmp = review_repo.findByFilm(film);
        if(!review_tmp.isEmpty()){
            return new FilmReviewResponseDto(
                    film.getTitle(),
                    review_tmp.stream()
                            .map(this::convertFromReview).toList()
            );
        }
        return new FilmReviewResponseDto(
                film.getTitle(),
                new ArrayList<>()
        );
    }

    private ReviewResponseDto convertFromReview(Review review){
        return new ReviewResponseDto(
                review.getUser().getUsername(),
                review.getText(),
                review.getDate(),
                review.getScore()
        );
    }

    private String createReview(String email, CreateReviewRequestDto dto){
        Optional<Film> film_tmp = film_repo.findByTitle(dto.getFilmTitle());
        if(film_tmp.isPresent()){
            review_repo.save(
                    new Review(
                            dto.getText(),
                            LocalDate.now(),
                            dto.getScore(),
                            user_repo.findByEmail(email).get(),
                            film_repo.findByTitle(film_tmp.get().getTitle()).get())
            );
            return "Review for film " + dto.getFilmTitle() + " has been added";
        }
        throw new FilmNotFoundException("Cannot find the film named " + dto.getFilmTitle());
    }

    private String updateReview(String email, UpdateReviewRequestDto dto){
        User user_tmp = user_repo.findByEmail(email).get();
        Optional<Review> review_tmp = review_repo.findById(dto.getReviewIndex());

        if(review_tmp.isPresent()){
            if(review_tmp.get().getUser().equals(user_tmp)){
                updateReviewFields(review_tmp.get(), dto);
                return "Specified review on film " + dto.getFilmTitle() + " has been updated";
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

    private String deleteReviewUser(int index, String email){
        User user_tmp = user_repo.findByEmail(email).get();
        Optional<Review> review_tmp = review_repo.findById(index);

        if(review_tmp.isPresent()){
            if(review_tmp.get().getUser().equals(user_tmp)){
                review_repo.delete(review_tmp.get());
                return "Specified review for film " + review_tmp.get().getFilm().getTitle() +
                        " has been deleted";
            }
            throw new ReviewNotUsersOwnReviewException(
                    "Cannot delete other users review");
        }
        throw new ReviewNotFoundException("Cannot delete a review " +
                "that does not exist");
    }

    private String deleteReviewAdmin(int index){
        Optional<Review> review_tmp = review_repo.findById(index);
        if(review_tmp.isPresent()){
            review_repo.delete(review_tmp.get());
            return "Specified review for film " + review_tmp.get().getFilm().getTitle() +
                    " has been deleted";
        }
        throw new ReviewNotFoundException("Cannot delete a review " +
                "that does not exist");
    }
}
