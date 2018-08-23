package com.cwteams.serenity.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.cwteams.util.FacesUtils;

@ManagedBean(name = "guestPreferences")
@SessionScoped
public class GuestPreferences implements Serializable {
   
	private static final long serialVersionUID = 1L;

	private String layout = "moody";
        
    //private String theme = "bluegrey";
	private String theme = "grey";
    
    private boolean darkMenu;
                            
	public String getTheme() {
		definirPreferences();
		return theme;
	}
    
	public void setTheme(String theme) {		
		this.theme = theme;
	}
    
    public String getLayout() {
    	definirPreferences();
        return layout;
    }
    
    public void setLayout(String layout) {
        this.layout = layout;
    }
    
    public boolean isDarkMenu() {		
        return darkMenu;
    }
    
    public void setDarkMenu(boolean darkMenu) {
        this.darkMenu = darkMenu;
    }
    
    private void definirPreferences(){    	
    	if(FacesUtils.existeUsuarioLogueado() && !FacesUtils.estaInicio()){//Existe un usuario logueado y no esta en el inicio 		
    		int tipoUsuario=FacesUtils.getUsuarioLogueado().getUserType().getIdUserType();
    		if(tipoUsuario==1){//Admin
    			this.layout = "cityscape";        
        		this.theme = "deeppurple";
			}
			if(tipoUsuario==2){//Manager
				this.layout = "cloudy";        
	    		this.theme = "blue";
			}		 
    	}
    	else{//Guest    		
    		this.layout = "moody";        
    		this.theme = "grey";
    	}
    }
}
