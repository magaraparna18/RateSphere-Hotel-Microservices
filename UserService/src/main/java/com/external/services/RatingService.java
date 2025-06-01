package com.external.services;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.entities.Rating;

@Service
@FeignClient(name="RATING-SERVICE")
public interface RatingService {

	//get
	
	
	
	//Post
//	@PostMapping("/ratings")
//	public Rating createRating(Rating values);
	@PostMapping("/ratings")
	public Rating createRating(@RequestBody Rating values);
	
	//Put
//	@PutMapping("/ratings/{ratingId}")
//	public Rating updateRating(@PathVariable("ratingId") String ratingId ,Rating rating);

	 @PutMapping("/ratings/{ratingId}")
	    Rating updateRating(@PathVariable("ratingId") String ratingId,
	                        @RequestBody Rating rating);
	//Delete
	@DeleteMapping("/ratings/{ratingId}")
	public void deleteRating(@PathVariable String ratingId);
}
