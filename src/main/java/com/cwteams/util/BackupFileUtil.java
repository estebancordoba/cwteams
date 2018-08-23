package com.cwteams.util;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BackupFileUtil implements Serializable {  
	private static final long serialVersionUID = 1L;
	private String name;
	private String date_hour;
	private String size;
	  
	public BackupFileUtil() {}
	  
	public BackupFileUtil(File f){
		Date lastModified = new Date(f.lastModified());
	    Float s = Float.valueOf((float)f.length() / 1000.0F);
	    SimpleDateFormat formatter = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy '-' h:mm:ss a", new Locale("es"));
	    String formattedDateString = formatter.format(lastModified);
	    String formattedSize = String.format("%.1f", new Object[] { s }) + " KB";
	    this.name = f.getName();
	    this.date_hour = formattedDateString;
	    this.size = formattedSize;
	}
	  
	public String getName() {
		return this.name;
	}
	  
	public void setName(String name) {
		this.name = name;
	}
	  
	public String getDate_hour() {
		return this.date_hour;
	}
	  
	public void setDate_hour(String date_hour) {
		this.date_hour = date_hour;
	}
	  
	public String getSize() {
		return this.size;
	}
	  
	public void setSize(String size) {
		this.size = size;
	}
}