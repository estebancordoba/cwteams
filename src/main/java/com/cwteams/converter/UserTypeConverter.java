package com.cwteams.converter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.cwteams.model.hibernate.UsersType;
import com.cwteams.service.UsersService;

@ManagedBean(name = "rolConverter")
@RequestScoped
public class UserTypeConverter implements Converter {
	@ManagedProperty(value = "#{UsersBean}")
	UsersService usersService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		try {

			if (value != null && value.trim().length() > 0) {
				return usersService.getUserTypeXId(Integer.parseInt(value));
			} else {
				return null;
			}

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error!", "Rol no valido");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value != null) {
			return String.valueOf(((UsersType) value).getIdUserType());
		} else {
			return null;
		}

	}

	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

}
