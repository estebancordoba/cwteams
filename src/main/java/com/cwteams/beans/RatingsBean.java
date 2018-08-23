package com.cwteams.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.cwteams.model.hibernate.Ratings;
import com.cwteams.service.RatingsService;
import com.cwteams.util.MsgUtil;

@ManagedBean(name = "ratingsBean")
@SessionScoped
public class RatingsBean implements Serializable{
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{RatingsServiceBean}")
	RatingsService ratingsService;
	
	private Ratings raiting;
	
	public RatingsBean() {
		cancelar();
	}
	
	public Ratings getRating() {
		return raiting;
	}
	
	public void cancelar(){
		raiting = new Ratings();
	}
	
	public void setRating(Ratings raiting) {
		this.raiting = raiting;
	}
	
	public List<Ratings> getRatings() {
		List<Ratings> rtn = ratingsService.getRatingNoView();
		for (Ratings rt : rtn) {
			rt.setView(true);
			ratingsService.updateRating(rt);
		}
		return ratingsService.getRatings();		
	}
	
	public void guardar() {					
		try {
			ratingsService.saveRating(raiting);			
			MsgUtil.msgInfo("Exito!", "Sugerencia enviada exitosamente.");
			raiting=new Ratings();			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void eliminar(Long id_r) {	
		try {
			Ratings ra_eliminar = ratingsService.getRatingXId(id_r);
			ratingsService.removeRating(ra_eliminar);
			MsgUtil.msgInfo("Exito!", "Sugerencia eliminada correctamente.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public int getRatingNoView(){
		List<Ratings> rtn = ratingsService.getRatingNoView();
		if(rtn==null || rtn.isEmpty()) return 0;
		else return rtn.size();
	}
	
	public void setRatingsService(RatingsService raitingsService) {
		this.ratingsService = raitingsService;
	}
}