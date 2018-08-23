package com.cwteams.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.cwteams.model.hibernate.Users;

public class FacesUtils {
	
	public static Object getSessionAttribute(String attribute){
		ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) external.getSession(false);
		
		Object o = null;
		if(session!=null){
			o = session.getAttribute(attribute);
		}
		return o;
	}
	
	public static void setSessionAttribute(String attribute, Object value){
		ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) external.getSession(false);
		session.setAttribute(attribute, value);
	}
	
	public static void removeAttribute(String attribute){
		ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) external.getSession(false);
		session.removeAttribute(attribute);
	}
	
	public static Users getUsuarioLogueado(){
		return (Users) getSessionAttribute("usuario");
	}
	
	public static void setUsuarioLogueado(Users usuario){
		setSessionAttribute("usuario", usuario);
	}
	
	public static void removeUsuarioLogueado(){
		removeAttribute("usuario");
	}
	
	public static boolean existeUsuarioLogueado(){
		if(getUsuarioLogueado()!=null){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean estaInicio(){				
		FacesContext fc = FacesContext.getCurrentInstance();
		String paginaActual = fc.getViewRoot().getViewId();
		
		return paginaActual.lastIndexOf("index.xhtml") > -1 ? true : false;
	}
	
}	
