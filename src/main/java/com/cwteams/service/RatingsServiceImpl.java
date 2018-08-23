package com.cwteams.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.cwteams.model.dao.RatingsDAO;
import com.cwteams.model.hibernate.Ratings;

@Transactional(readOnly = true)
public class RatingsServiceImpl implements RatingsService, Serializable{

	private static final long serialVersionUID = 1L;
	private RatingsDAO ratingsDAO;

	@Override
	public List<Ratings> getRatings() {
		return ratingsDAO.getRatings();
	}

	@Override
	public void saveRating(Ratings raiting) {
		ratingsDAO.saveRating(raiting);
	}

	@Override
	public void updateRating(Ratings raiting) {
		ratingsDAO.updateRating(raiting);
	}

	@Override
	public void removeRating(Ratings raiting) {
		ratingsDAO.removeRating(raiting);
	}

	@Override
	public Ratings getRatingXId(Long id) {		
		return ratingsDAO.getRatingXId(id);
	}
	
	@Override
	public List<Ratings> getRatingNoView() {
		return ratingsDAO.getRatingNoView();
	}
	
	public void setRatingsDAO(RatingsDAO ratingsDAO) {
		this.ratingsDAO = ratingsDAO;
	}

}