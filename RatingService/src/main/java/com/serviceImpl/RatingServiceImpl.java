package com.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entities.Rating;
import com.repository.RatingRepository;
import com.service.RatingService;

@Service
public class RatingServiceImpl implements RatingService {

	
	@Autowired 
	private RatingRepository  ratingRepo;
	@Override
	public Rating createRating(Rating rating) {
		String ratingId=UUID.randomUUID().toString();
		rating.setRatingId(ratingId);
		return ratingRepo.save(rating);
	}
	@Override
	public List<Rating> getRatings() {
		return ratingRepo.findAll();
	}

	@Override
	public List<Rating> getRatingByUserId(String userId) {
		return ratingRepo.findByUserId(userId);
	}

	@Override
	public List<Rating> getRatingByHotelId(String hotelId) {
		return ratingRepo.findByHotelId(hotelId);
	}

}
