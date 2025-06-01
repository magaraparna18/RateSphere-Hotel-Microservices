package com.hotelservice.service;

import java.util.List;

import com.hotelservice.entities.Hotel;


public interface HotelService {
	Hotel create(Hotel hotel);
	List<Hotel> getAll();
	Hotel getHotel(String id);

}
