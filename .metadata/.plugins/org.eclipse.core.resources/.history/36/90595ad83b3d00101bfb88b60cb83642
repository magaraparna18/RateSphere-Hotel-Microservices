package com.hotelservice.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelservice.entities.Hotel;
import com.hotelservice.exception.ResourceNotFoundException;
import com.hotelservice.repository.HotelRepository;
import com.hotelservice.service.HotelService;



@Service
public class HotelServiceImpl implements HotelService {

	@Autowired 
	HotelRepository hotelRepo;
	
	@Override
	public Hotel create(Hotel hotel) {
		String hotelId=UUID.randomUUID().toString();
		hotel.setId(hotelId);
		return hotelRepo.save(hotel);
	}

	@Override
	public List<Hotel> getAll() {
		return hotelRepo.findAll();
	}

	@Override
	public Hotel getHotel(String id) {
		return hotelRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("hotel with given id not found!!"));
	}

}
