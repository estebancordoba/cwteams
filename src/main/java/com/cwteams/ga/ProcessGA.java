package com.cwteams.ga;

import java.util.ArrayList;
import java.util.Random;

public class ProcessGA {
	public static ArrayList<GroupsFormed> GeneticAlgorithmProcess(ArrayList<MemberData> members, int numMembersxGroup, int numIndividuals,
								   int numCaracteristicas, int numGeneraciones, double mediaAptitud, double porcentajeSeleccion,
								   double probabilidadMutarInd, double probabilidadMutarGen){
		
		//Variables
		boolean media=false;
		int indiceIndividuo=-1;
		
		//Normalizar		
		members=normalizar(members);		
		//Agregar miembros dummys		
		members=completarMiembros(members, numMembersxGroup);
		//Poblacion Inicial		
		ArrayList<Individuals> individuals = generarIndividuosInciales(members, numMembersxGroup, numIndividuals);
		
		for (int i = 0; i < numGeneraciones; i++) {			
			//Evaluacion			
			ArrayList<Double> tm = promedioxCaracteristica(members,numCaracteristicas);			
			ArrayList<double[][]> im = promedioCaracteristicaxGrupoI(tm,individuals,numCaracteristicas);			
			ArrayList<Double> smc = sumatoriaCaracteristicasI(tm,im);
									
			for (int j = 0; j < smc.size(); j++) {				
				if(smc.get(j)==mediaAptitud){
					media=true;
					indiceIndividuo=j;
					break;
				}
			}
			if(media) break;
			
			//Seleccion
			double tf = adaptacionTotal(smc);			
			ArrayList<Double> psi=probabilidadesSeleccion(smc,tf);			
			ArrayList<Double> pacum = probabilidadesAcumuladas(psi);			
			/**/// 40%??????????? Para cuadrar que los restantes sean pares sumar o restar 1? - Solucionado ///**/			
			ArrayList<Double> numAS = numerosAleatoriosSeleccion(numIndividuals, porcentajeSeleccion,pacum);
			/**/// Y si se repite un rango entre los aleatorios??????????? - Solucionado ///**/			
			ArrayList<Individuals> indS = seleccionIndividuos(individuals,pacum,numAS);
			
			//Cruce
			ArrayList<Individuals> indC = individuosParaCruce(individuals,indS);
			ArrayList<Individuals> cruI = cruzarIndividuos(indC);
			
			//Mutacion
			ArrayList<Individuals> indM = new ArrayList<Individuals>();
			indM.addAll(indS);
			indM.addAll(cruI);		
			/**/// Al mutar los genes, se debe hacer de a dos??? sino con cual se muta? - Solucionado///**/
			ArrayList<Individuals> indMut = mutarIndividuos(indM,probabilidadMutarInd,probabilidadMutarGen);
			
			//Nueva Poblacion
			individuals = indMut;
		}
				
		if(!media){
			ArrayList<Double> tm = promedioxCaracteristica(members,numCaracteristicas);
			ArrayList<double[][]> im = promedioCaracteristicaxGrupoI(tm,individuals,numCaracteristicas);
			ArrayList<Double> smc = sumatoriaCaracteristicasI(tm,im);
			
			double menF=Integer.MAX_VALUE;
			for (int j = 0; j < smc.size(); j++) {				
				if(smc.get(j)<menF){
					menF=smc.get(j);
					indiceIndividuo=j;					
				}
			}
	//		println(smc.get(indiceIndividuo));
		}		
		
		
		return individuals.get(indiceIndividuo).getGroups();
	}
	
	private static ArrayList<MemberData> completarMiembros(ArrayList<MemberData> members, int numMembersxGroup) {
		if(members.size()%numMembersxGroup!=0){			
			int numMiembrosNecesidad=numMembersxGroup*((members.size()/numMembersxGroup)+1);
			int numMiembrosFaltantes=numMiembrosNecesidad-members.size();
			
			double[] carF= new double[members.get(0).getCharacter().size()];
			for (int i = 0; i < carF.length; i++) carF[i]=0;
			
			for (MemberData memD : members) {
				for (int i = 0; i < memD.getCharacter().size(); i++) {
					carF[i]=carF[i]+memD.getCharacter().get(i).getValue();
				}
			}			
			
			ArrayList<Characteristics> characterF= new ArrayList<Characteristics>();
			
			for (int i = 0; i < carF.length; i++){
				Characteristics chaF = new Characteristics(Math.rint((carF[i]/members.size())*10000)/10000, members.get(0).getCharacter().get(i).getName(),members.get(0).getCharacter().get(i).getMin(),members.get(0).getCharacter().get(i).getMax());
				characterF.add(chaF);
			}
						
			for (int i = 0; i < numMiembrosFaltantes; i++) {				
				MemberData memberFalso = new MemberData(-1*(i+1), "* DUMMY "+(i+1)+" *", characterF);
				members.add(memberFalso);
			}
		}
		return members;
	}
	
	private static ArrayList<Individuals> mutarIndividuos(ArrayList<Individuals> indM, double probabilidadMutarInd,
			double probabilidadMutarGen) {
		
		ArrayList<Individuals> indMut = indM;		
		
		for (Individuals indiv : indMut) {
			Random rand = new Random();
			double probM = rand.nextDouble();
			
			if(probM<=probabilidadMutarInd){		
				int cM=0,gM=-1,mM=-1;				
				for (int i = 0; i < indiv.getGroups().size(); i++) {
					for (int j = 0; j < indiv.getGroups().get(i).getMembers().size(); j++) {
						
						double probG = rand.nextDouble();						
						if(probG<=probabilidadMutarGen){							
							cM++;
							if(cM==1){
								gM=i;
								mM=j;
							}							
							if(cM==2){ 
								if(gM!=i){								
									MemberData memD = indiv.getGroups().get(gM).getMembers().get(mM); 
									indiv.getGroups().get(gM).getMembers().set(mM, indiv.getGroups().get(i).getMembers().get(j));									
									indiv.getGroups().get(i).getMembers().set(j, memD);
								}
								
								cM=0;
								gM=-1;
								mM=-1;
							}
						}
					}
				}
			}
		}
		
		return indMut;
	}
	
	private static ArrayList<Individuals> cruzarIndividuos(ArrayList<Individuals> indC) {
		ArrayList<Individuals> cruI = new ArrayList<Individuals>();
		ArrayList<Individuals> indCruzados = new ArrayList<Individuals>();
		Random rand = new Random();
		
		for (int i = 0; i < indC.size()/2; i++) {			
			int iC1=rand.nextInt(indC.size());		
			int iC2=rand.nextInt(indC.size());
			while(indCruzados.contains(indC.get(iC1))){			
				iC1=rand.nextInt(indC.size());				
			}			
			indCruzados.add(indC.get(iC1));
			while(indCruzados.contains(indC.get(iC2))){			
				iC2=rand.nextInt(indC.size());
			}			
			indCruzados.add(indC.get(iC2));
			
			Individuals[] parIndCruce = new Individuals[2];
			parIndCruce[0]=indC.get(iC1);
			parIndCruce[1]=indC.get(iC2);
			
			//ESTO ESTA MAL, BORRAR			
			//parIndCruce[0]=indC.get(0);parIndCruce[1]=indC.get(1);
			//
			
			Individuals[] parIndCruzados = cruzarParIndividuos(parIndCruce);
			cruI.add(parIndCruzados[0]);
			cruI.add(parIndCruzados[1]);
			
		}
		
		return cruI;
	}

	private static Individuals[] cruzarParIndividuos(Individuals[] parIndCruce) {
		Individuals[] indCruzados = new Individuals[2];
		
		indCruzados[0]=parIndCruce[0];
		indCruzados[1]=parIndCruce[1];
		
		int numM=indCruzados[0].getGroups().get(0).getMembers().size();
		int numG=indCruzados[0].getGroups().size();
			
		Random rand = new Random();
		int[] nR = new int[numG];
		for (int i = 0; i < numG; i++) {
			nR[i]=rand.nextInt(numM)+1;
		}
		
		//ESTO ESTA MAL, BORRAR
		//nR[0]=2;nR[1]=1;nR[2]=3;nR[3]=2;
		//
		
		ArrayList<MemberData> miembrosOtroPadre1 = new ArrayList<MemberData>();
		for (GroupsFormed grpI : indCruzados[0].getGroups()) {
			for (MemberData mDGI : grpI.getMembers()) {
				miembrosOtroPadre1.add(mDGI);
			}
		}
		ArrayList<MemberData> miembrosOtroPadre2 = new ArrayList<MemberData>();
		for (GroupsFormed grpI : indCruzados[1].getGroups()) {
			for (MemberData mDGI : grpI.getMembers()) {
				miembrosOtroPadre2.add(mDGI);
			}
		}
		                  
		for (int i = 0; i < 2; i++) {
			ArrayList<MemberData> miembrosConservar = new ArrayList<MemberData>();
			for (int j = 0; j < numG ; j++) {
				GroupsFormed grupoRecorrer = indCruzados[i].getGroups().get(j);
				for (int k = 0; k < nR[j]; k++) {
					miembrosConservar.add(grupoRecorrer.getMembers().get(k));
				}
			}
			
			ArrayList<MemberData> miembrosOtroPadre = (i==0)?miembrosOtroPadre2:miembrosOtroPadre1;
			
			int cMO=0;
			for (int j = 0; j < numG ; j++) {
				GroupsFormed grupoRecorrer = indCruzados[i].getGroups().get(j);
				for (int k = nR[j] ; k < numM; k++) {
							
					while(miembrosConservar.contains(miembrosOtroPadre.get(cMO))){						
						cMO++;			
					}
					grupoRecorrer.getMembers().set(k, miembrosOtroPadre.get(cMO));
					cMO++;
				}
			}		
		}
		
		return indCruzados;
	}

	private static ArrayList<Individuals> individuosParaCruce(ArrayList<Individuals> individuals,
			ArrayList<Individuals> indS) {
		
		ArrayList<Individuals> indC = new ArrayList<Individuals>(); 
		
		for (Individuals indiv : individuals) {
			if(!indS.contains(indiv)){
				indC.add(indiv);
			}
		}
		
		return indC;
	}

	private static ArrayList<Individuals> seleccionIndividuos(ArrayList<Individuals> individuals,
     	ArrayList<Double> pacum, ArrayList<Double> numAS) {
		
		ArrayList<Individuals> indS = new ArrayList<Individuals>(); 
		
		for (int i = 0; i < pacum.size() - 1; i++) {
			for (int j = 0; j < numAS.size(); j++) {
				if(i==0 && numAS.get(j) < pacum.get(i)){//Para el primero
					indS.add(individuals.get(i));	
					break;
				}
				if(pacum.get(i)< numAS.get(j) && numAS.get(j) < pacum.get(i+1)){
					indS.add(individuals.get(i+1));	
					break;
				}
			}
		}
		
		return indS;
	}

	private static ArrayList<Double> numerosAleatoriosSeleccion(int numIndividuals, double porcentajeCruce, 
			ArrayList<Double> pacum) {
		ArrayList<Double> numAS = new ArrayList<Double>();
		ArrayList<Integer> posA = new ArrayList<Integer>();
		
		int nA=(int) (numIndividuals*porcentajeCruce);		
		
		if((numIndividuals-nA)%2!=0) nA++;		
		
		Random rand = new Random();
		for (int i = 0; i < nA; i++) {			
			double numA=rand.nextDouble();			
			boolean colocar=false;
			
			while(!colocar){				
				for (int k = 0; k < pacum.size() - 1; k++) {					
					if((k==0 && numA < pacum.get(k)) || 
					   (pacum.get(k)< numA && numA < pacum.get(k+1))){						
						if(posA.contains((Integer)k)) colocar=false;
						else{
							posA.add((Integer)k);
							colocar=true;							
						}
						
						break;
					}									
				}
				if(!colocar) numA=rand.nextDouble();		
			}
			
			numAS.add(numA);
		}
		
		return numAS;
	}
	
	private static ArrayList<Double> probabilidadesAcumuladas(ArrayList<Double> psi) {
		ArrayList<Double> pacum = new ArrayList<Double>();
		
		double pa=0;
		for (Double ps : psi) {
			pa=pa+ps;
			pacum.add(pa);
		}
		
		return pacum;
	}

	private static ArrayList<Double> probabilidadesSeleccion(ArrayList<Double> smc, double tf) {
		ArrayList<Double> psi = new ArrayList<Double>();
		
		for (Double sm : smc) {
			double ps = sm/tf;
			psi.add(ps);
		}
		
		return psi;
	}

	private static double adaptacionTotal(ArrayList<Double> smc) {
		double tf=0;
		for (Double sm : smc) {
			tf=tf+sm;
		}
		return tf;
	}

	private static ArrayList<Double> sumatoriaCaracteristicasI(ArrayList<Double> tm, ArrayList<double[][]> im) {
		ArrayList<Double> smc = new ArrayList<Double>();
		
		for (int k = 0; k < im.size(); k++) {
			double cart=0;
			for (int i = 0; i < im.get(k).length; i++) {
				double car=0;
				for (int j = 0; j < im.get(k)[i].length; j++) {					
					car=car+Math.pow((tm.get(i)-im.get(k)[i][j]),2);
				}
				cart=cart+car;
			}
			smc.add(cart);
		}
		
		return smc;
	}

	private static ArrayList<double[][]> promedioCaracteristicaxGrupoI(ArrayList<Double> tm, ArrayList<Individuals> individuals, int numC) {
		ArrayList<double[][]> im = new ArrayList<double[][]>();

		int col=numC;	    
	    int fil=individuals.get(0).getGroups().size();///////////////////////////////
	    
		for (Individuals ind : individuals) {
			double[][] imgrp = new double[col][fil];
			
			int contF=0;
			for (GroupsFormed grp : ind.getGroups()) {				
				for (int i = 0; i < numC; i++) {
					double tmpP=0.0;
					for (MemberData mem : grp.getMembers()) {
						tmpP=tmpP+mem.getCharacter().get(i).getValue();
					}
					tmpP=tmpP/numC;
					imgrp[i][contF]=tmpP;					
				}				
				contF++;
			}
			im.add(imgrp);
		}		
		return im;
	}

	private static ArrayList<Double> promedioxCaracteristica(ArrayList<MemberData> members, int numC) {
		
		ArrayList<Double> tm = new ArrayList<Double>();
		for (int i = 0; i < numC; i++) tm.add(0.0);
		for (MemberData mem : members) {
			for (int i = 0; i < numC; i++) {
				Double tmpC = tm.get(i);
				tmpC=tmpC+mem.getCharacter().get(i).getValue();
				tm.set(i, tmpC);
			}
		}
		for (int i = 0; i < numC; i++) tm.set(i, tm.get(i)/members.size());
		
		return tm;
	}

	private static ArrayList<Individuals> generarIndividuosInciales(ArrayList<MemberData> members, int numMembersxGroup, int numIndividuals) {
		
		ArrayList<Individuals> individuals = new ArrayList<Individuals>();
		
		for (int i = 0; i < numIndividuals; i++) {
			ArrayList<GroupsFormed> groups = new ArrayList<GroupsFormed>();
			ArrayList<MemberData> members_admitted = new ArrayList<MemberData>();			
			for (int j = 0; j < (members.size()/numMembersxGroup); j++) {
				ArrayList<MemberData> members_group = new ArrayList<MemberData>();
				for (int k = 0; k < numMembersxGroup; k++) {
					boolean admitted=false;
					while(!admitted){
						Random rand = new Random();
						int numA = rand.nextInt(members.size());				
						MemberData md = members.get(numA);				
						if(!members_admitted.contains((MemberData)md)){				
							members_admitted.add(md);
							members_group.add(md);
							admitted=true;					
						}				
					}			
				}
				GroupsFormed grp = new GroupsFormed(members_group);
				groups.add(grp);
			}
			Individuals ind = new Individuals(groups);
			individuals.add(ind);
		}
		
		return individuals;
	}

	private static ArrayList<MemberData> normalizar(ArrayList<MemberData> members) {
		for (MemberData member : members) {			
			for (Characteristics character : member.getCharacter()) {
				character.setValue((character.getValue()-character.getMin())/(character.getMax()-character.getMin()));				
			}
		}		
		return members;
	}
}
