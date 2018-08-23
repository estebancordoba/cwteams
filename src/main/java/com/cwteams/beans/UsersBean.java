package com.cwteams.beans;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.mail.MessagingException;
import javax.servlet.ServletContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.primefaces.event.FileUploadEvent;

import com.cwteams.model.hibernate.Users;
import com.cwteams.model.hibernate.UsersType;
import com.cwteams.service.UsersService;
import com.cwteams.util.EmailUtils;
import com.cwteams.util.FacesUtils;
import com.cwteams.util.MsgUtil;

@ManagedBean(name = "usersBean")
@SessionScoped
public class UsersBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{UsersBean}")
	UsersService usersService;

	private Users user;
	private Users userFirst;
	private String n_pass;
	private String n_email;
	
	private Users userRemPass;
	
	private String userOldName="";	
	
	private boolean editar = false;
	private boolean mDatos = true;
	private boolean mPass = true;
	private List<Users> filterUsers;
	private List<Users> selectedUsers;

	public UsersBean() {
		cancelar();
	}

	public boolean ismDatos() {
		return mDatos;
	}

	public boolean ismPass() {
		return mPass;
	}

	public void guardar() {
		if(!editar || mPass) user.setPass(DigestUtils.md5Hex(user.getPass()));
		
		if (!editar) {
			try {
				usersService.saveUser(user);
				this.cancelar();
				MsgUtil.msgInfo("Exito!", "Usuario guardado correctamente.");				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				usersService.updateUser(user);
				this.cancelar();
				MsgUtil.msgInfo("Exito!", "Usuario actualizado");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void guardarProfile() {		
		if(mPass) user.setPass(DigestUtils.md5Hex(user.getPass()));		
		try {
			usersService.updateUser(user);
			FacesUtils.setUsuarioLogueado(user);
			this.cancelar();
			MsgUtil.msgInfo("Exito!", "Se ha modificado tu perfil");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void userExists(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if(value == null) {
            return;
        }
		String nomU=(String)value;		
		
		Users usr = usersService.getUserXUser(nomU);		
		
		if(usr!=null){				
			if(!(editar && nomU.equals(userOldName))){
				FacesMessage msg = new FacesMessage("Nombre de usuario ya existe");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				
				throw new ValidatorException(msg);
			}
		}
				
	}
	
	public void userValidate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if(value == null) {
            return;
        }
		
		List<Users> usrs = usersService.getUsers();
		Boolean valid=false;
		userRemPass=new Users();
		
		for (Users us : usrs) {		
			if(us.getUser().equals(value.toString())){				
				valid=true;
				userRemPass=us;
				break;				
			}
		}	
		
		if(!valid){
			FacesMessage msg = new FacesMessage("Nombre de usuario no existe");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ValidatorException(msg);
		}
	}
	
	public void emailValidate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if(value == null) {
            return;
        }
		Pattern pattern;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		
		if(!pattern.matcher(value.toString()).matches()) {
			FacesMessage msg = new FacesMessage("Formato de correo invalido");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ValidatorException(msg);
        }		
	}

	public void actualizar(Integer id_u) {
		user = usersService.getUserXId(id_u);
		editar = true;
		mDatos = true;
		mPass = false;
		
		userOldName = user.getUser();
	}
	
	public void cambiarC(Integer id_u) {
		user = usersService.getUserXId(id_u);
		user.setPass("");
		
		editar = true;
		mDatos = false;
		mPass = true;		
	}

	public void eliminar(Integer id_u) {
		try {
			Users user_eliminar = usersService.getUserXId(id_u);			
			usersService.removeUser(user_eliminar);
			MsgUtil.msgInfo("Exito!", "Usuario eliminado correctamente");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelar() {
		user = new Users();
		editar = false;
		mDatos = true;
		mPass = true;
		userOldName = "";
		n_email="";
		selectedUsers=null;
	}
	
	public void firstUser() {		
		user=usersService.firstUser();		
		userOldName = user.getUser();
		editar = true;
	}

	public List<Users> getUsuarios() {
		return usersService.getUsers();
	}
	
	public Users getUserXId(Integer id) {
		return usersService.getUserXId(id);
	}	
		
	//public void sendEmail(String destination){
	public void sendEmail(String n_email){
		if(!userRemPass.getEmail().equals(n_email)){
			this.n_email="";
			MsgUtil.msgError("Error!", "El correo no pertenece al usuario");			
			return;
		}
		
		String new_pass=RandomStringUtils.randomAlphanumeric(10);
		
    	userRemPass.setPass(DigestUtils.md5Hex(new_pass));	
    	userRemPass.setFirst(true);
		try {
			usersService.updateUser(userRemPass);
			
			try
		    {		    	
		    	EmailUtils.sendEmail(userRemPass.getEmail(),new_pass);
		    } catch (MessagingException ex) {
		    	ex.printStackTrace();
		    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al enviar recordatorio de contraseña!"));
		    }			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void submitMultipleUsers(FileUploadEvent event) {		
		try {
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String path = servletContext.getRealPath("") + File.separatorChar
					+ "resources" + File.separatorChar + "images" + File.separatorChar
					+ "tmp" + File.separatorChar + "mu.csv";
			
			File f=new File(path);
			
			InputStream in=new ByteArrayInputStream(event.getFile().getContents());
			FileOutputStream out = new FileOutputStream(f.getAbsolutePath());
			
			int c=0;
			while((c=in.read())>=0) out.write(c);			
			
			out.flush();
			out.close();
			
			FileReader fr = new FileReader(f);          
	        BufferedReader br = new BufferedReader(fr);
	        
	        String auxt=br.readLine();	        
	        
	        String [] conf=auxt.split(",");
	        int linea=1;
	        
	        if(conf.length<3 || conf.length>7){
        		MsgUtil.msgError("Error!", "Numero invalido de columnas en archivo, linea "+linea+".");
        		br.close();
                f.delete();
        		return;
        	}
	        int user,pass,name,surname;
	        int email,phone,user_type;
	        ArrayList<String> config = new ArrayList<String>();
	        for (String col : conf) config.add(col);	        
	        user=config.indexOf("user");
	        pass=config.indexOf("pass");
	        name=config.indexOf("name");
	        surname=config.indexOf("surname");	        
	        email=config.indexOf("email");
	        phone=config.indexOf("phone");
	        user_type=config.indexOf("user_type");
	        
	        int c_c=0;
	        if(user!=-1) c_c++;
	        if(pass!=-1) c_c++;
	        if(name!=-1) c_c++;
	        if(surname!=-1) c_c++;        
	        if(email!=-1) c_c++;
	        if(phone!=-1) c_c++;
	        if(user_type!=-1) c_c++;

        	if(c_c!=conf.length){
        		MsgUtil.msgError("Error!", "Existen columnas de cabezera invalidas, linea "+linea+".");
        		br.close();
                f.delete();
        		return;
        	}
	        
	        ArrayList<Users> mUsers = new ArrayList<Users>();
	        
            while((auxt = br.readLine())!=null){            	
            	//Usuario, Rol, Contraseña
            	String[] linxlin=auxt.split(",");
            	linea++;

            	if(linxlin.length!=conf.length){
            		MsgUtil.msgError("Error!", "Numero invalido de columnas en archivo, linea "+linea+".");
            		br.close();
                    f.delete();
            		return;
            	}
            	
            	Users userUM = new Users();
            	
            	if(user!=-1){
            		if(linxlin[user].isEmpty() || linxlin[user].equals("")){
            			MsgUtil.msgError("Error!", "Nombre de usuario no valido, linea "+linea+".");
                		br.close();
                        f.delete();
                		return;
            		}
            		Users usr = usersService.getUserXUser(linxlin[user]);
            	
            		if(usr!=null){
            			MsgUtil.msgError("Error!", "Nombre de usuario ya existe, linea "+linea+".");
                		br.close();
                        f.delete();
                		return;
            		}
            		userUM.setUser(linxlin[user]); 
            	}            	
            	if(pass!=-1){
            		if(linxlin[pass].isEmpty() || linxlin[pass].equals("")){
            			MsgUtil.msgError("Error!", "Contraseña no valida, linea "+linea+".");
                		br.close();
                        f.delete();
                		return;
            		}
            		userUM.setPass(DigestUtils.md5Hex(linxlin[pass]));
            	}            	
            	if(name!=-1) userUM.setName(linxlin[name]);            	
            	if(surname!=-1) userUM.setSurname(linxlin[surname]);            	
            	if(email!=-1){            		
            		if(!linxlin[email].matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            			MsgUtil.msgError("Error!", "Email no valido, linea "+linea+".");
                		br.close();
                        f.delete();
                		return;
            		}
            		userUM.setEmail(linxlin[email]); 
            	}            	
            	if(phone!=-1) userUM.setPhone(linxlin[phone]);            	
            	if(user_type!=-1) {
            		if(linxlin[user_type].isEmpty() || linxlin[user_type].equals("")){
            			MsgUtil.msgError("Error!", "Tipo de usuario no valido, linea "+linea+".");
                		br.close();
                        f.delete();
                		return;
            		}
            		if(!linxlin[user_type].matches("[0-9]+")){
            			MsgUtil.msgError("Error!", "Un tipo de usuario es invalido, linea "+linea+".");
                		br.close();
                        f.delete();
                		return;
            		}            		
            		if(Integer.parseInt(linxlin[user_type])<1 || Integer.parseInt(linxlin[user_type])>2){
            			MsgUtil.msgError("Error!", "Un tipo de usuario es invalido, linea "+linea+".");
                		br.close();
                        f.delete();
                		return;
            		}
            		userUM.setUserType(usersService.getUserTypeXId(Integer.parseInt(linxlin[user_type]))); 
            	}
            	
            	mUsers.add(userUM);
            }
            
            for (Users mUser : mUsers) {
            	try {
    				usersService.saveUser(mUser);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}  
			}
            
            MsgUtil.msgInfo("Exito!", "Usuarios guardados correctamente.");
            
            br.close();
            f.delete();
            
		} catch (Exception e) {
			MsgUtil.msgError("Error!", "Ocurrio un error al intentar guardar los usuarios.");
			e.printStackTrace();
		}		
	}
	
	public void eliminarVariosUsuarios(){				
		try {	
			for (Users eliminar : selectedUsers) {	
				usersService.removeUser(eliminar);
			}
			MsgUtil.msgInfo("Exito!", "Registros eliminados correctamente.");
		} catch (Exception e) {
			MsgUtil.msgError("Error!", "Ocurrio un error al intentar eliminar los registros.");
			e.printStackTrace();
		}		
		selectedUsers=null;
	}

	public List<UsersType> getRoles() {
		return usersService.getUserTypes();
	}

	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	public String getN_pass() {
		return n_pass;
	}

	public void setN_pass(String n_pass) {
		this.n_pass = n_pass;
	}
	
	public String getN_email() {
		return n_email;
	}

	public void setN_email(String n_email) {
		this.n_email = n_email;
	}

	public List<Users> getFilterUsers() {
		return filterUsers;
	}

	public void setFilterUsers(List<Users> filterUsers) {
		this.filterUsers = filterUsers;
	}
	
	public List<Users> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<Users> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
	
	public Users obtenerUsuario(){
		FacesContext context = FacesContext.getCurrentInstance();
		return (Users) context.getExternalContext().getSessionMap().get("usuario");		
	}
	
	public boolean obtenerUsuarioFirst(Integer id_user){
		userFirst=usersService.getUserXId(id_user);		
		
		if(userFirst.getFirst()){
			userFirst.setPass("");
			n_email=userFirst.getEmail();
		}
		
		return userFirst.getFirst();
	}
	
	public void cambiarPassFirst() {
		try {			
			userFirst.setPass(DigestUtils.md5Hex(n_pass));
			userFirst.setEmail(n_email);
			userFirst.setFirst(false);
			usersService.updateUser(userFirst);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap().put("usuario", userFirst);
			
			this.n_pass=null;
			
			MsgUtil.msgInfo("Exito!", "Contraseña actualizada");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
