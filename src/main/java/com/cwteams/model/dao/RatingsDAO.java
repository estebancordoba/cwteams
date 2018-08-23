package com.cwteams.model.dao;

import java.util.List;

import com.cwteams.model.hibernate.Ratings;

public interface RatingsDAO {

	public List<Ratings> getRatings();
	
	public void saveRating(Ratings raiting);
	
	public void updateRating(Ratings raiting);
	
	public void removeRating(Ratings raiting);
	
	public Ratings getRatingXId(Long id);
	
	public List<Ratings> getRatingNoView();

}
