package com.cwteams.beans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import com.cwteams.util.BackupFileUtil;
import com.cwteams.util.CryptoUtil;
import com.cwteams.util.MsgUtil;

@ManagedBean(name="backupBean")
@SessionScoped
public class BackupBean implements Serializable {	
	
	private static final long serialVersionUID = 1L;
	
	private final List<BackupFileUtil> backupFiles;
	private BackupFileUtil backupFile;
	private final String backupPath = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + File.separatorChar
			+ "backups" + File.separatorChar;
  	
	private String user="";
	private String password="";
	private String database="";
	private String port="";
	private String ip="";
	private String type="";
	private String driverDB="";
	private String path=MySqlPath();
	private String strKey="cwteams";
	
	public BackupBean() {		
		
		this.backupFiles = new ArrayList<BackupFileUtil>();		
		
		File file = new File(this.backupPath);
		File[] listFiles = file.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".sql");
			}
		});
		
		
		for (File f1 : listFiles) {
			this.backupFiles.add(new BackupFileUtil(f1));
		}
	}
  
	public List<BackupFileUtil> getBackupFiles() {
		return this.backupFiles;
	}
  
	public BackupFileUtil getBackupFile() {
		return this.backupFile;
	}
  
	public void setBackupFile(BackupFileUtil backupFile) {
		this.backupFile = backupFile;
	}
	
	public void backup() {
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		String strFilename = "cwteams_"+dateFormat.format(now);
		String[] executeCmd = { this.path + "mysqldump", this.database, "-u" + this.user, "-p" + this.password, "-r" + this.backupPath + strFilename + ".sql" };
		try{
			Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int processComplete = runtimeProcess.waitFor();
			if (processComplete == 0) {
				File f = new File(this.backupPath + strFilename + ".sql");
			
				
				
			    FileWriter fichero = new FileWriter(f,true);
			    BufferedWriter bfwriter = new BufferedWriter(fichero);
			    
			    bfwriter.write("\n-- "+now);			    
			    bfwriter.write("\n-- "+CryptoUtil.encriptar(now.toString(), strKey));
			    bfwriter.close();
			    
				this.backupFiles.add(new BackupFileUtil(f));
				MsgUtil.msgInfo("Exito!", "La copia de seguridad ha sido realizada exitosamente!");
			} 
			else {
				MsgUtil.msgError("Error!","La copia de seguridad no ha podido ser realizada!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void restore() {
		
		String[] executeCmd = { this.path + "mysql", this.database, "--user=" + this.user, "--password=" + this.password, "-e", "source " + this.backupPath + this.backupFile.getName() };
		try{
			Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int processComplete = runtimeProcess.waitFor();
			if (processComplete == 0) {
				MsgUtil.msgInfo("Exito!", "La copia de seguridad ha sido restaurada exitosamente!");
			} 
			else {
				MsgUtil.msgError("Error!", "La copia de seguridad no ha podido ser restaurada!");
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
  
	public StreamedContent download(String source) {		
		InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/backups/" + source);		
		String contentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType("/backups/" + source);
		StreamedContent file = new org.primefaces.model.DefaultStreamedContent(stream, contentType, source);
		return file;
	}
  
	public void delete() {		
		
		File file = new File(this.backupPath + this.backupFile.getName());
		if (file.delete()) {
			this.backupFiles.remove(this.backupFile);
			this.backupFile = null;
			MsgUtil.msgInfo("Exito!", "El archivo ha sido eliminado exitosamente!");
		} else {
			MsgUtil.msgError("Error!", "El archivo no puede ser borrado!");
		}
	}
	
	public void submitBK(FileUploadEvent event) {
		boolean valido=false;
		
		try {
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String pathBK = servletContext.getRealPath("") + File.separatorChar
					+ "resources" + File.separatorChar + "images" + File.separatorChar
					+ "tmp" + File.separatorChar + "bk.sql";
			
			File f=new File(pathBK);
			
			InputStream in=new ByteArrayInputStream(event.getFile().getContents());
			FileOutputStream out = new FileOutputStream(f.getAbsolutePath());
			
			int c=0;
			while((c=in.read())>=0) out.write(c);			
			
			out.flush();
			out.close();
			
			FileReader fr = new FileReader (f);
			BufferedReader br = new BufferedReader(fr);
	        String linea;
	       
	        ArrayList<String> lineas = new ArrayList<String>();	        
	        while((linea=br.readLine())!=null)
	        	lineas.add(linea);
	        
	        String textN=lineas.get(lineas.size()-2).substring(3);
	        String textE=lineas.get(lineas.size()-1).substring(3);
	        
	        if(textN.equals(CryptoUtil.desencriptar(textE, strKey))) valido=true;
	        
	        br.close();
		        
        	if(valido){
				String[] executeCmd = { this.path + "mysql", this.database, "--user=" + this.user, "--password=" + this.password, "-e", "source " + pathBK };
				try
				{
					Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
					int processComplete = runtimeProcess.waitFor();
					if (processComplete == 0) {
						MsgUtil.msgInfo("Exito!", "La copia de seguridad ha sido restaurada exitosamente!");
					} else {
						MsgUtil.msgError("Error!", "La copia de seguridad no ha podido ser restaurada!");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
	        }
	        else{
	        	MsgUtil.msgError("Error!", "Archivo de copia de seguridad invalido.");
	        }
            
            f.delete();
            
		} catch (Exception e) {
			MsgUtil.msgError("Error!", "Ocurrio un error al intentar subir el archivo.");
			e.printStackTrace();
		}		
	}
	
	private String MySqlPath(){
		generarParametros();
		
		Statement st = null;
		String p = "";
		try {
			Class.forName(driverDB);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Connection con = java.sql.DriverManager.getConnection("jdbc:"+type+"://"+ip+":"+port+"/" + database, user, password);
			st = con.createStatement(1004, 1008);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ResultSet res = st.executeQuery("select @@basedir");			
			while (res.next()) {
				p = res.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		p = p + "/bin/";
				
		char[] rp = p.toCharArray();		
		for (int i = 0; i < rp.length; i++) 
			if(rp[i]==47 || rp[i]==92) 
				rp[i]= File.separatorChar;
		p=String.valueOf(rp);
		
		return p;
	}
	
	private void generarParametros(){		
		try {
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();		
			String path = servletContext.getRealPath("") + File.separatorChar
					+ "settings" + File.separatorChar + "Settings.properties";
			
			Properties propiedades = new Properties();
			propiedades.load(new FileInputStream(path));
			
			this.user=propiedades.getProperty("userDB");
			this.password=propiedades.getProperty("passwordDB");
			this.database=propiedades.getProperty("nameDB");
			this.port=propiedades.getProperty("portDB");
			this.ip=propiedades.getProperty("ipDB");
			this.type=propiedades.getProperty("typeDB");
			this.driverDB=propiedades.getProperty("driverDB");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
}