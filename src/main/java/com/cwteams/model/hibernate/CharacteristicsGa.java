package com.cwteams.model.hibernate;
// Generated 11/02/2018 09:05:45 PM by Hibernate Tools 5.2.3.Final

/**
 * CharacteristicsGa generated by hbm2java
 */
public class CharacteristicsGa implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idCharacteristicsGa;
	private double value;
	private String name;
	private Double minimum;
	private Double maximum;
	private int idMembersGa;

	public CharacteristicsGa() {
	}
	
	public CharacteristicsGa(double value, String name, Double minimum, Double maximum, int idMembersGa) {
		this.value = value;
		this.name = name;
		this.minimum = minimum;
		this.maximum = maximum;
		this.idMembersGa = idMembersGa;
	}

	public Integer getIdCharacteristicsGa() {
		return this.idCharacteristicsGa;
	}

	public void setIdCharacteristicsGa(Integer idCharacteristicsGa) {
		this.idCharacteristicsGa = idCharacteristicsGa;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getMinimum() {
		return this.minimum;
	}

	public void setMinimum(Double minimum) {
		this.minimum = minimum;
	}

	public Double getMaximum() {
		return this.maximum;
	}

	public void setMaximum(Double maximum) {
		this.maximum = maximum;
	}

	public int getIdMembersGa() {
		return this.idMembersGa;
	}

	public void setIdMembersGa(int idMembersGa) {
		this.idMembersGa = idMembersGa;
	}

}