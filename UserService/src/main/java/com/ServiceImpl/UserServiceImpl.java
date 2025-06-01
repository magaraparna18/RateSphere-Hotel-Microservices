package com.ServiceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.entities.Hotel;
import com.entities.Rating;
import com.entities.User;
import com.exception.ResourceNotFoundException;
import com.external.services.HotelService;
import com.repository.UserRepository;
import com.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private HotelService hotelService;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUser() {
        List<User> users = userRepo.findAll();

        for (User user : users) {
            try {
                ResponseEntity<List<Rating>> response = restTemplate.exchange(
                    "http://localhost:8083/ratings/user/" + user.getUserId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Rating>>() {}
                );

                List<Rating> ratingsOfUser = response.getBody();

                List<Rating> enrichedRatings = ratingsOfUser.stream().map(rating -> {
                    try {
                        ResponseEntity<Hotel> hotelResponse = restTemplate.getForEntity(
                            "http://localhost:8082/hotels/" + rating.getHotelId(),
                            Hotel.class
                        );
                        Hotel hotel = hotelResponse.getBody();
                        rating.setHotel(hotel);
                        logger.info("Fetched hotel for hotelId {}: {}", rating.getHotelId(), hotel);
                    } catch (org.springframework.web.client.HttpClientErrorException.NotFound ex) {
                        logger.warn("Hotel not found for hotelId {}: {}", rating.getHotelId(), ex.getMessage());
                        rating.setHotel(null);
                    } catch (Exception ex) {
                        logger.error("Error fetching hotel for hotelId {}: {}", rating.getHotelId(), ex.getMessage());
                        rating.setHotel(null);
                    }
                    return rating;
                }).collect(Collectors.toList());

                user.setRatings(enrichedRatings);
                logger.info("Fetched ratings for user {} : {}", user.getUserId(), enrichedRatings);
            } catch (Exception e) {
                logger.error("Failed to fetch ratings for user {} : {}", user.getUserId(), e.getMessage());
                user.setRatings(List.of());
            }
        }

        return users;
    }

    @Override
    public User getUser(String userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server: " + userId));

        ResponseEntity<List<Rating>> response = restTemplate.exchange(
            "http://RATING-SERVICE/ratings/user/" + user.getUserId(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Rating>>() {}
        );

        List<Rating> ratingsOfUser = response.getBody();
        logger.info("Ratings received for user {} : {}", userId, ratingsOfUser);

        List<Rating> ratingList = ratingsOfUser.stream().map(rating -> {
            try {
//                ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(
//                    "http://HOTEL-SERVICE/hotels/" + rating.getHotelId(),
//                    Hotel.class
//                );
                Hotel hotel = hotelService.getHotel(rating.getHotelId());                        rating.setHotel(hotel);
                rating.setHotel(hotel);
                logger.info("Fetched hotel for hotelId {}: {}", rating.getHotelId(), hotel);
            } catch (org.springframework.web.client.HttpClientErrorException.NotFound ex) {
                logger.warn("Hotel not found for hotelId {}: {}", rating.getHotelId(), ex.getMessage());
                rating.setHotel(null);
            } catch (Exception ex) {
                logger.error("Error fetching hotel for hotelId {}: {}", rating.getHotelId(), ex.getMessage());
                rating.setHotel(null);
            }
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);
        return user;
    }

    @Override
    public User updateUser(String userId, User user) {
        User existingUser = userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cannot update. User not found with id: " + userId));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAbout(user.getAbout());

        return userRepo.save(existingUser);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cannot delete. User not found with id: " + userId));

        userRepo.delete(user);
    }
}
