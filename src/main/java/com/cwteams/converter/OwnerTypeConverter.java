package com.cwteams.converter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.cwteams.model.hibernate.Users;
import com.cwteams.service.UsersService;

@ManagedBean(name = "ownerConverter")
@RequestScoped
public class OwnerTypeConverter implements Converter{
	@ManagedProperty(value = "#{UsersBean}")
	UsersService usersService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		try {

			if (value != null && value.trim().length() > 0) {
				return usersService.getUserXId(Integer.parseInt(value));
			} else {
				return null;
			}

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error!", "Usuario no valido");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);			
		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value != null) {
			return String.valueOf(((Users) value).getIdUser());
		} else {
			return null;
		}

	}

	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
}
