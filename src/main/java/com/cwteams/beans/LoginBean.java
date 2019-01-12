package com.cwteams.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.cwteams.model.hibernate.Users;
import com.cwteams.service.UsersService;
import com.cwteams.util.MsgUtil;
import com.cwteams.util.FacesUtils;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{UsersBean}")
	UsersService usersService;

	private Users user;

	public LoginBean() {
		user = new Users();
	}
	
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	public String efectuarLogin() {
		RequestContext context = RequestContext.getCurrentInstance();        
        boolean loggedIn = false;
		
        String retorno = null;
        this.user = usersService.login(user);
                
        if (user == null) {
        	loggedIn = false;        	
            MsgUtil.msgWarning(LanguageBean.obtenerMensaje("error"),LanguageBean.obtenerMensaje("invalid_credentials"));
            user=new Users();
        } else {
        	FacesUtils.setUsuarioLogueado(user);        	
        	loggedIn = true;
        		
            if (user.getUserType().getIdUserType()==1) {
                FacesUtils.setUsuarioLogueado(user);
                retorno = "pretty:dashboard";                
            }
            if (user.getUserType().getIdUserType()==2) {
                FacesUtils.setUsuarioLogueado(user);
                retorno = "pretty:managerS";                
            } 
            if (user.getUserType().getIdUserType()==3) {
                FacesUtils.setUsuarioLogueado(user);
                retorno = "pretty:users";                
            } 
        }        
        context.addCallbackParam("loggedIn", loggedIn);
        return retorno;
    }

    public String efectuarLogoff() {
        FacesUtils.removeUsuarioLogueado();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.invalidate();
        return "pretty:login";
    }
        
    public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

}
