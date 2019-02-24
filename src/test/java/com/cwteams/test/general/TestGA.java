package com.cwteams.test.general;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
 
public class TestGA {
 
	@BeforeClass
	public static void setUpClass() throws Exception {
	//Inicialización general de variables, escritura del log...
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
	//Liberación de recursos, escritura en el log...
	}
	
	@Before
    public void setUp() {
    //Inicialización de variables antes de cada Test
    }
	
	@After
    public void tearDown() {
    //Tareas a realizar después de cada test
    }
	
	@Test
    public void comprobarAccion() {
    //creamos el entorno necesario para la prueba
	//Usamos alguna de las funciones arriba descritas 
	//para realizar la comprobación
    }
	
    public void funcionAuxiliar() {
    //tareas auxiliares
    }
	
}
