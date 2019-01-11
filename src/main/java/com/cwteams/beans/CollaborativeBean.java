package com.cwteams.beans;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.cwteams.ga.Characteristics;
import com.cwteams.ga.GroupsFormed;
import com.cwteams.ga.MemberData;
import com.cwteams.ga.ProcessGA;
import com.cwteams.model.hibernate.CharacteristicsGa;
import com.cwteams.model.hibernate.CollaborativeGa;
import com.cwteams.model.hibernate.GroupsGa;
import com.cwteams.model.hibernate.MembersGa;
import com.cwteams.model.hibernate.Users;
import com.cwteams.service.CollaborativeService;
import com.cwteams.util.ExcelUtil;
import com.cwteams.util.JavaScriptUtil;
import com.cwteams.util.MsgUtil;

@ManagedBean(name = "collaborativeBean")
@SessionScoped
public class CollaborativeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{CollaborativeServiceBean}")
	CollaborativeService collaborativeService;
		
	private List<CollaborativeGa> selectCollaborative;
	private List<MemberData> membersSub;
	private List<MemberData> selectedMemberData;
	private CollaborativeGa collaborativeActual; 
	private StreamedContent fileDownload;
	private boolean archivoSubido=false;
	private ArrayList<GroupsFormed> gruposTemp;
	
	public List<CollaborativeGa> getSelectCollaborative() {
		return selectCollaborative;
	}

	public void setSelectCollaborative(List<CollaborativeGa> selectCollaborative) {
		this.selectCollaborative = selectCollaborative;
	}

	public List<MemberData> getMembersSub() {
		return membersSub;
	}

	public void setMembersSub(List<MemberData> membersSub) {
		this.membersSub = membersSub;
	}
	
	public List<MemberData> getSelectedMemberData() {
		return selectedMemberData;
	}

	public void setSelectedMemberData(List<MemberData> selectedMemberData) {
		this.selectedMemberData = selectedMemberData;
	}
	
	public CollaborativeGa getCollaborativeActual() {
		return collaborativeActual;
	}

	public void setCollaborativeActual(CollaborativeGa collaborativeActual) {
		this.collaborativeActual = collaborativeActual;
	}

	public StreamedContent getFileDownload() {
		return fileDownload;
	}

	public ArrayList<GroupsFormed> getGruposTemp() {
		return gruposTemp;
	}

	public void setGruposTemp(ArrayList<GroupsFormed> gruposTemp) {
		this.gruposTemp = gruposTemp;
	}


	
	//Services	
	public void setCollaborativeService(CollaborativeService collaborativeService) {
		this.collaborativeService = collaborativeService;
	}

	public String onFlowProcess(FlowEvent event) {
		
		if(archivoSubido==false && event.getOldStep().equals("submitdata") && !event.getNewStep().equals("submitdata")){
			FacesMessage msg = new FacesMessage(LanguageBean.obtenerMensaje("should_upload_file_data"));
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			return "submitdata";
		}
		
		if(selectedMemberData!=null && (selectedMemberData.isEmpty() || selectedMemberData.size()<4) && event.getOldStep().equals("members")){
			FacesMessage msg = new FacesMessage(LanguageBean.obtenerMensaje("should_select_consistent_amount"));
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			return "members";
		}
		if(selectedMemberData!=null && event.getOldStep().equals("members")){
						
			for (int i = 2; i <= selectedMemberData.size(); i++) {				
				if(selectedMemberData.size()%i==0){					
					collaborativeActual.setNumMembersxGroup(i);
					break;
				}
			}
		}
		
		if(event.getOldStep().equals("parameters") && !event.getNewStep().equals("members") && selectedMemberData.size()<collaborativeActual.getNumMembersxGroup()){
			FacesMessage msg = new FacesMessage(LanguageBean.obtenerMensaje("number_member_not_consistent"));
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			return "parameters";
		}	
		
		return event.getNewStep();
    }
	
	public void prepararDatos(){
		collaborativeActual=new CollaborativeGa();
		membersSub=null;
		archivoSubido=false;
    }
	
	public void prepararDatos(Users user_m){
		collaborativeActual=new CollaborativeGa();
		collaborativeActual.setUserOwner(user_m);
		membersSub=null;
		archivoSubido=false;
    }
	
	
	public void colocarCollaborative(CollaborativeGa collaborative_view){		
		collaborativeActual=collaborative_view;		
		
	}
	
	public CollaborativeGa obtenerCollaborativexId(Integer id_collaborative){
		return collaborativeService.searchCollaborativeGaxId(id_collaborative);
	}
	
	public List<GroupsGa> obtenerGrupos(){
		return collaborativeService.getGroupsGaxIdCollaborativeGa(collaborativeActual.getIdCollaborativeGa());
	}
	
	public List<CollaborativeGa> obtenerCollaboratives(boolean libre){
		return collaborativeService.getCollaborativeGa(libre);
	}
	
	public List<CollaborativeGa> getCollaborativeGaxOwner(Integer id_user){
		return collaborativeService.getCollaborativeGaxOwner(id_user);
	}
	
	public List<MembersGa> obtenerMembersxIdGroupGa(Integer id_group_ga){
		return collaborativeService.getMembersGaxIdGroupGa(id_group_ga);
	}
	
	public void firstCollaborative(){
		collaborativeActual=collaborativeService.firstCollaborative();
	}
		
	public void renewConformacion(boolean guardar){
		
		
		ArrayList<MemberData> members = new ArrayList<MemberData>();
		
		if(guardar){
			ArrayList<GroupsGa> grupos_ga_old = (ArrayList<GroupsGa>) collaborativeService.getGroupsGaxIdCollaborativeGa(collaborativeActual.getIdCollaborativeGa());
		
			for (GroupsGa grupoOld : grupos_ga_old) {
				ArrayList<MembersGa> members_ga_old = (ArrayList<MembersGa>) collaborativeService.getMembersGaxIdGroupGa(grupoOld.getIdGroupGa());
			
				for (MembersGa memberOld : members_ga_old) {
					ArrayList<Characteristics> caractert= new ArrayList<Characteristics>();
					ArrayList<CharacteristicsGa> caract_ga_old = (ArrayList<CharacteristicsGa>) collaborativeService.getCharacteristicsGaxIdMemberGa(memberOld.getIdMembersGa());
			
					for (CharacteristicsGa characGa : caract_ga_old) {
						Characteristics carac = new Characteristics(characGa.getValue(), characGa.getName(),characGa.getMinimum(),characGa.getMaximum());
						caractert.add(carac);
					}
					MemberData mem = new MemberData(memberOld.getIdUser(), memberOld.getName(), caractert);
					members.add(mem);
				}
			}	
		}
		else 
			for (GroupsFormed gr : gruposTemp) members.addAll(gr.getMembers());			
			
		procesarGA(members,guardar);
		
		
	}
	public void procesarGA(ArrayList<MemberData> members, boolean guardar){
		int numMembersxGroup = collaborativeActual.getNumMembersxGroup();
		int numIndividuals = collaborativeActual.getNumIndividuals();
		int numCaracteristicas = collaborativeActual.getNumCaracteristicas();
		int numGeneraciones = collaborativeActual.getNumGeneraciones();
		double mediaAptitud = collaborativeActual.getMediaAptitud();
		double porcentajeSeleccion = collaborativeActual.getPorcentajeSeleccion();
		double probabilidadMutarInd = collaborativeActual.getProbabilidadMutarInd();
		double probabilidadMutarGen = collaborativeActual.getProbabilidadMutarGen();
		String group_name = collaborativeActual.getName();
		String group_description = collaborativeActual.getDescription();
		boolean free = collaborativeActual.isFree();
		Users user_owner = collaborativeActual.getUserOwner();
		
		long startTime = System.nanoTime();		
		ArrayList<GroupsFormed> gruposGA = ProcessGA.GeneticAlgorithmProcess(members, numMembersxGroup, numIndividuals, numCaracteristicas, numGeneraciones, mediaAptitud, porcentajeSeleccion/100, probabilidadMutarInd/100, probabilidadMutarGen/100);
		long endTime = System.nanoTime();		
	    
		CollaborativeGa collaborative_ga = new CollaborativeGa(group_name,group_description,free,numMembersxGroup, numIndividuals, numCaracteristicas, numGeneraciones, mediaAptitud, porcentajeSeleccion, probabilidadMutarInd, probabilidadMutarGen, ((endTime-startTime)/1e6/1000),user_owner);		
		
		if(guardar){
			CollaborativeGa collaborative_eliminar=null;
			if(collaborativeActual!=null) collaborative_eliminar= collaborativeService.searchCollaborativeGaxId(collaborativeActual.getIdCollaborativeGa());		
			if(collaborative_eliminar!=null) collaborativeService.removeCollaborativeGa(collaborative_eliminar);
			
			collaborativeService.saveCollaborativeGa(collaborative_ga);					
		
			for (int i = 0; i < gruposGA.size(); i++) {
				GroupsGa grupo_ga = new GroupsGa(""+(i+1),collaborative_ga.getIdCollaborativeGa());
				collaborativeService.saveGroupGa(grupo_ga);			
				
				for (MemberData memD : gruposGA.get(i).getMembers()) {				
					MembersGa miembro_ga = new MembersGa(memD.getId(), memD.getName(), grupo_ga.getIdGroupGa());
					
					collaborativeService.saveMemberGa(miembro_ga);				
					
					for (Characteristics carMemD : memD.getCharacter()) {
						CharacteristicsGa caracteristica_ga = new CharacteristicsGa(carMemD.getValue(), carMemD.getName(), carMemD.getMin(), carMemD.getMax(), miembro_ga.getIdMembersGa());
						collaborativeService.saveCharacteristicGa(caracteristica_ga);					
					}
				}
			}
		}
		
		collaborativeActual=collaborative_ga;
		if(!guardar){
			gruposTemp=gruposGA;
			for (int i = 1; i <= gruposTemp.size(); i++) gruposTemp.get(i-1).setName(""+i);
		}
	}
	
	public void chartMember(Integer id_member){		
		List<CharacteristicsGa> lstRes = collaborativeService.getCharacteristicsGaxIdMemberGa(id_member);			
		
		String[] dataChartD = new String[lstRes.size()];
		String[] labelsChartD = new String[lstRes.size()];
		
		int cD=0;
		for (CharacteristicsGa resM : lstRes) {			
			labelsChartD[cD]=resM.getName();
			dataChartD[cD]=""+resM.getValue();
			cD++;			
		}
		RequestContext context = RequestContext.getCurrentInstance();
		JavaScriptUtil jsu = new JavaScriptUtil();
		context.execute(jsu.radarChart("chart", labelsChartD, dataChartD));		
	}
	
	public void chartGroup(Integer id_group){		
		List<MembersGa> lstMem = collaborativeService.getMembersGaxIdGroupGa(id_group);
		
		String[] titleD = new String[lstMem.size()];
		String[][] dataChartD = new String[lstMem.size()][];
		String[] labelsChartD = null;
		
		int cM=0;
		for (MembersGa mem : lstMem) {
			List<CharacteristicsGa> lstRes = collaborativeService.getCharacteristicsGaxIdMemberGa(mem.getIdMembersGa());			
			
			titleD[cM] = mem.getName();
			dataChartD[cM] = new String[lstRes.size()];	
			labelsChartD = new String[lstRes.size()];
			
			int cD=0;
			for (CharacteristicsGa resM : lstRes) {			
				labelsChartD[cD]=resM.getName();
				dataChartD[cM][cD]=""+resM.getValue();
				cD++;			
			}
			cM++;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		JavaScriptUtil jsu = new JavaScriptUtil();
		context.execute(jsu.radarChartMultiple("chart", titleD, labelsChartD, dataChartD));		
	}
	
	public void chartGroupLoad(Integer id_group, String name){	
		List<MembersGa> lstMem = collaborativeService.getMembersGaxIdGroupGa(id_group);
		
		String[] titleD = new String[lstMem.size()];
		String[][] dataChartD = new String[lstMem.size()][];
		String[] labelsChartD = null;
		
		int cM=0;
		for (MembersGa mem : lstMem) {
			List<CharacteristicsGa> lstRes = collaborativeService.getCharacteristicsGaxIdMemberGa(mem.getIdMembersGa());			
			
			titleD[cM] = mem.getName();
			dataChartD[cM] = new String[lstRes.size()];	
			labelsChartD = new String[lstRes.size()];
			
			int cD=0;
			for (CharacteristicsGa resM : lstRes) {			
				labelsChartD[cD]=resM.getName();
				dataChartD[cM][cD]=""+resM.getValue();
				cD++;			
			}
			cM++;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		JavaScriptUtil jsu = new JavaScriptUtil();
		context.execute(jsu.radarChartMultiple("chart"+name, titleD, labelsChartD, dataChartD));		
	}
	
	public void chartGroupLoadFree(String name){	
		ArrayList<MemberData> lstMem = new ArrayList<MemberData>();
		for (GroupsFormed gr : gruposTemp) {
			if(gr.getName().equals(name)){
				lstMem.addAll(gr.getMembers());
			}
		}		
		
		String[] titleD = new String[lstMem.size()];
		String[][] dataChartD = new String[lstMem.size()][];
		String[] labelsChartD = null;
		
		int cM=0;
		for (MemberData mem : lstMem) {
			List<Characteristics> lstRes = mem.getCharacter();			
			
			titleD[cM] = mem.getName();
			dataChartD[cM] = new String[lstRes.size()];	
			labelsChartD = new String[lstRes.size()];
			
			int cD=0;
			for (Characteristics resM : lstRes) {			
				labelsChartD[cD]=resM.getName();
				dataChartD[cM][cD]=""+resM.getValue();
				cD++;			
			}
			cM++;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		JavaScriptUtil jsu = new JavaScriptUtil();
		context.execute(jsu.radarChartMultiple("chart"+name, titleD, labelsChartD, dataChartD));		
	}
	
	public void chartMedia(){		
		List<GroupsGa> grupos_ga = collaborativeService.getGroupsGaxIdCollaborativeGa(collaborativeActual.getIdCollaborativeGa());		
		
		String[] dataChartD = new String[collaborativeActual.getNumCaracteristicas()];
		String[] labelsChartD = new String[collaborativeActual.getNumCaracteristicas()];
		
		double[] dataD=new double[collaborativeActual.getNumCaracteristicas()];;
		int tM=0;
		
		for (GroupsGa groupsGa : grupos_ga) {
			List<MembersGa> miembros_ga = collaborativeService.getMembersGaxIdGroupGa(groupsGa.getIdGroupGa());
			tM+=miembros_ga.size();
			for (MembersGa membersGa : miembros_ga) {
				List<CharacteristicsGa> caracteristicas_ga = collaborativeService.getCharacteristicsGaxIdMemberGa(membersGa.getIdMembersGa());
				int cD=0;
				for (CharacteristicsGa characteristicsGa : caracteristicas_ga) {
					labelsChartD[cD]=characteristicsGa.getName();
					dataD[cD]=dataD[cD]+characteristicsGa.getValue();
					cD++;
				}
			}
		}	
		
		for (int i = 0; i < dataChartD.length; i++) {
			dataD[i]=Math.rint((dataD[i]/tM)*10000)/10000;
			dataChartD[i]=""+dataD[i];
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		JavaScriptUtil jsu = new JavaScriptUtil();
		context.execute(jsu.radarChart("chart", labelsChartD, dataChartD));		
	}
	
	public void chartMediaFree(){		
		
		String[] dataChartD = new String[collaborativeActual.getNumCaracteristicas()];
		String[] labelsChartD = new String[collaborativeActual.getNumCaracteristicas()];
		
		double[] dataD=new double[collaborativeActual.getNumCaracteristicas()];;
		int tM=0;
		
		for (GroupsFormed groupsGa : gruposTemp) {
			List<MemberData> miembros_ga = groupsGa.getMembers();
			tM+=miembros_ga.size();
			for (MemberData membersGa : miembros_ga) {
				List<Characteristics> caracteristicas_ga = membersGa.getCharacter();
				int cD=0;
				for (Characteristics characteristicsGa : caracteristicas_ga) {
					labelsChartD[cD]=characteristicsGa.getName();
					dataD[cD]=dataD[cD]+characteristicsGa.getValue();
					cD++;
				}
			}
		}	
		
		for (int i = 0; i < dataChartD.length; i++) {
			dataD[i]=Math.rint((dataD[i]/tM)*10000)/10000;
			dataChartD[i]=""+dataD[i];
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		JavaScriptUtil jsu = new JavaScriptUtil();
		context.execute(jsu.radarChart("chart", labelsChartD, dataChartD));		
	}
	
	public void generarArchivoDownload(List<GroupsGa> grupos){	
	    try {

	    	String[][] datos=new String[grupos.size()][];
	    		    	
	    	for (int i = 0; i < grupos.size(); i++) {
	    		List<MembersGa> memG=collaborativeService.getMembersGaxIdGroupGa(grupos.get(i).getIdGroupGa());
	    		datos[i]=new String[memG.size()];
	    		
	    		for (int j = 0; j < memG.size(); j++) {
	    			datos[i][j]=memG.get(j).getName();
				}
			}
	    	
	        Workbook wb = ExcelUtil.generearExcel(datos, collaborativeActual.getName());

	        String excelFileName = "Groups"+collaborativeActual.getName()+".xls";

	        FileOutputStream fos = new FileOutputStream(excelFileName);
	        wb.write(fos);
	        fos.flush();
	        fos.close();

	        InputStream stream = new BufferedInputStream(new FileInputStream(excelFileName));
	        fileDownload = new DefaultStreamedContent(stream, "application/xls", excelFileName);


	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	}
	
	public void generarArchivoDownloadFree(){
		List<GroupsFormed> grupos=gruposTemp;
	    try {

	    	String[][] datos=new String[grupos.size()][];
	    		    	
	    	for (int i = 0; i < grupos.size(); i++) {
	    		List<MemberData> memG=grupos.get(i).getMembers();
	    		datos[i]=new String[memG.size()];
	    		
	    		for (int j = 0; j < memG.size(); j++) {
	    			datos[i][j]=memG.get(j).getName();
				}
			}
	    	
	        Workbook wb = ExcelUtil.generearExcel(datos, collaborativeActual.getName());

	        String excelFileName = "Groups"+collaborativeActual.getName()+".xls";

	        FileOutputStream fos = new FileOutputStream(excelFileName);
	        wb.write(fos);
	        fos.flush();
	        fos.close();

	        InputStream stream = new BufferedInputStream(new FileInputStream(excelFileName));
	        fileDownload = new DefaultStreamedContent(stream, "application/xls", excelFileName);


	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	}
	
	public void actualizar() {		
		try {	
			collaborativeService.updateCollaborativeGa(collaborativeActual);			
			MsgUtil.msgInfo(LanguageBean.obtenerMensaje("success"), LanguageBean.obtenerMensaje("record_updated"));
		} catch (Exception e) {
			MsgUtil.msgError(LanguageBean.obtenerMensaje("error"), LanguageBean.obtenerMensaje("record_updated_error"));
			e.printStackTrace();
		}
	}
	
	public void eliminar(Integer id_collaborative) {		
		try {	
			CollaborativeGa eliminar=collaborativeService.searchCollaborativeGaxId(id_collaborative); 	
			collaborativeService.removeCollaborativeGa(eliminar);			
			MsgUtil.msgInfo(LanguageBean.obtenerMensaje("success"), LanguageBean.obtenerMensaje("record_removed"));
		} catch (Exception e) {
			MsgUtil.msgError(LanguageBean.obtenerMensaje("error"), LanguageBean.obtenerMensaje("record_removed_error"));
			e.printStackTrace();
		}		
		selectCollaborative=null;
	}
	
	public void eliminarCollaboratives() {		
		try {	
			for (CollaborativeGa eliminar : selectCollaborative) {	
				collaborativeService.removeCollaborativeGa(eliminar);
			}
			MsgUtil.msgInfo(LanguageBean.obtenerMensaje("success"), LanguageBean.obtenerMensaje("record_removed"));
		} catch (Exception e) {
			MsgUtil.msgError(LanguageBean.obtenerMensaje("error"), LanguageBean.obtenerMensaje("record_removed_error"));
			e.printStackTrace();
		}		
		selectCollaborative=null;
	}
	
	public void changeActivatedCollaborative(Integer id_collaborative) {
		try {
			CollaborativeGa cga = collaborativeService.searchCollaborativeGaxId(id_collaborative);
			Boolean activado=cga.isFree();
						

			cga.setFree(!activado);			
			collaborativeService.updateCollaborativeGa(cga);	
			
			if(activado) MsgUtil.msgInfo(LanguageBean.obtenerMensaje("success"), LanguageBean.obtenerMensaje("group_released"));
			else MsgUtil.msgInfo(LanguageBean.obtenerMensaje("success"), LanguageBean.obtenerMensaje("group_hidden"));			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void submitData(FileUploadEvent event) {		
		try {
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String path = servletContext.getRealPath("") + File.separatorChar
					+ "resources" + File.separatorChar + "images" + File.separatorChar
					+ "tmp" + File.separatorChar + "cw.csv";
			
			File f=new File(path);
			
			InputStream in=new ByteArrayInputStream(event.getFile().getContents());
			FileOutputStream out = new FileOutputStream(f.getAbsolutePath());
			
			int c=0;
			while((c=in.read())>=0) out.write(c);			
			
			out.flush();
			out.close();
			
			FileReader fr = new FileReader(f);          
	        BufferedReader br = new BufferedReader(fr);
	        
	        String auxt;
	        String[] linxlin;
	        int linea=1;	        
	        
	        String groupN;
	        auxt=br.readLine();
	        linxlin=auxt.split("=");
	        
	        if(linxlin.length!=2){
	        	errorSubmitData(LanguageBean.obtenerMensaje("invalid_group_name"),linea,br,f);
        		return;
	        }
	        else{	        	
	        	if(!linxlin[0].trim().equals("group_name")){
	        		errorSubmitData(LanguageBean.obtenerMensaje("invalid_group_name"),linea,br,f);
	        		return;
	        	}
	        	groupN=linxlin[1].trim();
	        }
	        
	        linea++;
	        
	        String groupD;
	        auxt=br.readLine();
	        linxlin=auxt.split("=");
	        
	        if(linxlin.length!=2){
	        	errorSubmitData(LanguageBean.obtenerMensaje("invalid_group_description"),linea,br,f);
        		return;
	        }
	        else{	        	
	        	if(!linxlin[0].trim().equals("group_description")){
	        		errorSubmitData(LanguageBean.obtenerMensaje("invalid_group_description"),linea,br,f);
	        		return;
	        	}
	        	groupD=linxlin[1].trim();
	        }
	        
	        linea++;
	        
	        int numCharac;
	        auxt=br.readLine();
	        linxlin=auxt.split("=");
	        
	        if(linxlin.length!=2){
	        	errorSubmitData(LanguageBean.obtenerMensaje("invalid_feature_number"),linea,br,f);
        		return;
	        }
	        else{	        	
	        	if(!linxlin[0].trim().equals("characteristics_number")){
	        		errorSubmitData(LanguageBean.obtenerMensaje("invalid_feature_number"),linea,br,f);
	        		return;
	        	}
	        	else{
	        		try {
	    	        	numCharac=Integer.parseInt(linxlin[1]);
	    			} catch (Exception e) {				
	                    errorSubmitData(LanguageBean.obtenerMensaje("invalid_feature_number"),linea,br,f);
	            		return;
	    			}
	        	}
	        }
	        
	        linea++;
	        	        
	        String[] characC = new String[numCharac];
	        double[] minC=new double[numCharac];
	        double[] maxC=new double[numCharac];
	        
	        
	        
	        for (int i = 0; i < numCharac; i++) {
	        	auxt = br.readLine();	        	
	        	linxlin=auxt.split(";");
	        	
	        	if(linxlin.length!=3){
	        		errorSubmitData(LanguageBean.obtenerMensaje("invalid_feature_data"),linea,br,f);	        		
	        		return;
	        	}
	        	characC[i]=linxlin[0];
	        	try {	        		
	        		minC[i]=Double.parseDouble(linxlin[1]);
	        		maxC[i]=Double.parseDouble(linxlin[2]);
	        		if(minC[i]>=maxC[i]){
	        			errorSubmitData(LanguageBean.obtenerMensaje("min_max_invalid_features"),linea,br,f);	        		
		        		return;
	        		}
				} catch (Exception e) {					
					errorSubmitData(LanguageBean.obtenerMensaje("invalid_feature_data"),linea,br,f);	        		
	        		return;
				}
	        	linea++;
			}
	        
	        int numMem;
	        auxt=br.readLine();
	        linxlin=auxt.split("=");
	        
	        if(linxlin.length!=2){
	        	errorSubmitData(LanguageBean.obtenerMensaje("invalid_members_number_parameter"),linea,br,f);
        		return;
	        }
	        else{	        	
	        	if(!linxlin[0].trim().equals("members_number")){
	        		errorSubmitData(LanguageBean.obtenerMensaje("invalid_members_number_parameter"),linea,br,f);
	        		return;
	        	}
	        	else{
	        		try {
	    	        	numMem=Integer.parseInt(linxlin[1]);
	    			} catch (Exception e) {				
	                    errorSubmitData(LanguageBean.obtenerMensaje("invalid_members_number_parameter"),linea,br,f);
	            		return;
	    			}
	        	}
	        }
	        
	        linea++;
	        
	        boolean nameE;
	        auxt=br.readLine();
	        linxlin=auxt.split("=");
	        	        
	        if(linxlin.length!=2){
	        	errorSubmitData(LanguageBean.obtenerMensaje("invalid_name_parameter"),linea,br,f);
        		return;
	        }
	        else{	        	
	        	if(!linxlin[0].trim().equals("available_name") || (!linxlin[1].trim().equals("true") && !linxlin[1].trim().equals("false")) ){
	        		errorSubmitData(LanguageBean.obtenerMensaje("invalid_name_parameter"),linea,br,f);
	        		return;
	        	}
	        	else{
	        		nameE=Boolean.parseBoolean(linxlin[1].trim());
	        	}
	        }
	        
	        linea++;
	        ArrayList<MemberData> memDS=new ArrayList<MemberData>();
	        ArrayList<Integer> idsS = new ArrayList<Integer>();
	        for (int j = 0; j < numMem; j++) {
				ArrayList<Characteristics> charMS = new ArrayList<Characteristics>();				
				
				int numCO=numCharac+((nameE)?2:1);
				int carEm=(nameE)?2:1;
				
				auxt=br.readLine();
		        linxlin=auxt.split(";");
		        if(linxlin.length!=numCO){
		        	errorSubmitData(LanguageBean.obtenerMensaje("invalid_members_parameter"),linea,br,f);
	        		return;
		        }
		        
		        try {
					Integer idxM=Integer.parseInt(linxlin[0]);			
					
					if(idsS.contains((Integer)idxM)){
						errorSubmitData(LanguageBean.obtenerMensaje("repeated_user_id"),linea,br,f);
		        		return;
					}
					else{
						idsS.add(idxM);
					}
					
					for (int i = 0; i < numCharac; i++) {
						Characteristics charXM = new Characteristics(Double.parseDouble(linxlin[carEm+i]), characC[i], minC[i], maxC[i]);
						charMS.add(charXM);
					}
					
					MemberData memxM;
					if(nameE){
						memxM=new MemberData(idxM, linxlin[1], charMS);
					}else{
						memxM=new MemberData(idxM, charMS);
					}
					memDS.add(memxM);
					
				} catch (Exception e) {
					errorSubmitData(LanguageBean.obtenerMensaje("invalid_members_parameter"),linea,br,f);
	        		return;
				}		        
		        
		        linea++;
			}
	        
	        membersSub=memDS;
	        collaborativeActual.setNumCaracteristicas(numCharac);	        
	        collaborativeActual.setName(groupN);	        
	        collaborativeActual.setDescription(groupD);
	        collaborativeActual.setFree(false);	        
	        
	        selectedMemberData = new ArrayList<MemberData>();
			selectedMemberData.addAll(membersSub);
            
            br.close();
            f.delete();
            
            archivoSubido=true;
            
            MsgUtil.msgInfo(LanguageBean.obtenerMensaje("success"), LanguageBean.obtenerMensaje("file_upload"));
            
		} catch (Exception e) {
			MsgUtil.msgError(LanguageBean.obtenerMensaje("error"), LanguageBean.obtenerMensaje("file_upload_error"));			
            archivoSubido=false;
			e.printStackTrace();
		}		
	}

	private void errorSubmitData(String mensaje, int linea, BufferedReader br, File f) throws IOException {
		MsgUtil.msgError(LanguageBean.obtenerMensaje("error"), mensaje+", "+LanguageBean.obtenerMensaje("line")+" "+linea+".");
		br.close();
        f.delete();
        archivoSubido=false;
	}

}
