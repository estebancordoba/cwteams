package com.cwteams.test.unit;

import org.junit.Before;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.cwteams.ga.Characteristics;
import com.cwteams.ga.GroupsFormed;
import com.cwteams.ga.MemberData;
import com.cwteams.ga.ProcessGA;
 
public class TestGA {
 	
	@Before
    public void setUp() {
    }
	
	@After
    public void tearDown() {    
    }
	
	@Test
    public void testGA() {
        ArrayList<MemberData> members = new ArrayList<MemberData>();

        // MEMBER 1
        ArrayList<Characteristics> characteristics1 =  new ArrayList<Characteristics>();        
        characteristics1.add(new Characteristics(0.5833, "CARACTERISTICA A", 0.0, 1.0));        
        characteristics1.add(new Characteristics(0.4218, "CARACTERISTICA B", 0.0, 1.0));        
        characteristics1.add(new Characteristics(0.8621, "CARACTERISTICA C", 0.0, 1.0));

        MemberData member1 = new MemberData(1, "MIEMBRO 1", characteristics1);
        members.add(member1);

        // MEMBER 2
        ArrayList<Characteristics> characteristics2 =  new ArrayList<Characteristics>();        
        characteristics2.add(new Characteristics(0.8966, "CARACTERISTICA A", 0.0, 1.0));        
        characteristics2.add(new Characteristics(0.1233, "CARACTERISTICA B", 0.0, 1.0));        
        characteristics2.add(new Characteristics(0.7642, "CARACTERISTICA C", 0.0, 1.0));

        MemberData member2 = new MemberData(2, "MIEMBRO 2", characteristics2);
        members.add(member2);

        // MEMBER 3
        ArrayList<Characteristics> characteristics3 =  new ArrayList<Characteristics>();        
        characteristics3.add(new Characteristics(0.2832, "CARACTERISTICA A", 0.0, 1.0));        
        characteristics3.add(new Characteristics(0.8830, "CARACTERISTICA B", 0.0, 1.0));        
        characteristics3.add(new Characteristics(0.1829, "CARACTERISTICA C", 0.0, 1.0));

        MemberData member3 = new MemberData(3, "MIEMBRO 3", characteristics3);
        members.add(member3);

        // MEMBER 4
        ArrayList<Characteristics> characteristics4 =  new ArrayList<Characteristics>();        
        characteristics4.add(new Characteristics(0.7238, "CARACTERISTICA A", 0.0, 1.0));        
        characteristics4.add(new Characteristics(0.8262, "CARACTERISTICA B", 0.0, 1.0));        
        characteristics4.add(new Characteristics(0.1272, "CARACTERISTICA C", 0.0, 1.0));

        MemberData member4 = new MemberData(4, "MIEMBRO 4", characteristics4);
        members.add(member4);
        
        ArrayList<GroupsFormed> gruposGA = ProcessGA.GeneticAlgorithmProcess(members, 2, 80, 3, 1000, 0.01, 0.4, 0.2, 0.15);
        
        Assert.assertTrue(gruposGA.size() == 2);
        
        for (int i = 0; i < gruposGA.size(); i++) {			
			Assert.assertTrue(gruposGA.get(i).getMembers().size() == 2);
			for (MemberData memD : gruposGA.get(i).getMembers()) {
				Assert.assertTrue(memD.getCharacter().size() == 3);
			}
		}
    }	
}
