package spread;

import java.util.ArrayList;

import com.mxgraph.model.mxCell;

public class VertexValue {
	
	private String id;
	private String name;
	private String surname;
	private double initialImmunity;
	private double currentImmunity;
	
	/**
	 * POLA POTRZEBNE DO BFSa
	 */
	private ArrayList<mxCell> neighbourhood;
	private int distance;
	private mxCell infector;	// parent z BFSa
	

	/*
	 * Dwie wersje konstruktora, bezparamentrowy jeśli chcemy ustawić wszystko za pomocą setterów
	 */
	
	public VertexValue() {
		this.id = null;
		this.name = null;
		this.surname = null;
		this.initialImmunity = 0.0;
		this.currentImmunity = 0.0;
		this.neighbourhood = new ArrayList<mxCell>();
		this.distance = 0;
		this.infector = null;
	}
	
	public VertexValue(String id, String name, String surname, double immunity) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.initialImmunity = immunity;
		this.currentImmunity = immunity;
		this.neighbourhood = new ArrayList<mxCell>();
		this.distance = 0;
		this.infector = null;
	}
	
	public ArrayList<mxCell> getNeighbours(){
		return this.neighbourhood;
	}
	
	public void addNeighbour(mxCell neighbour){
		this.neighbourhood.add(neighbour);
	}
	
	@Override
	public String toString() {				// to się pojawia w GUI wewnątrz prostokąta symbolizującego dany wierzchołek
		return name + " " + surname + "\nimmunity: " + currentImmunity;
	}
	
	public boolean isInfected(){
		return this.currentImmunity <= 0.0;
	}
	
	public boolean isInfectionInProgress(){
		return this.currentImmunity < this.initialImmunity;
	}
	
	public double getHealthRatio(){
		double rate = this.currentImmunity / this.initialImmunity;
		return (rate > 0.0) ? rate : 0.0;
	}
	
	public void reduceImmunity(double howMuch){
		double newValue = currentImmunity - howMuch;
		currentImmunity = (newValue < 0.0) ? 0.0 : newValue;
	}
	
	public void resetValue(){
		this.distance = 0;
		this.infector = null;
		this.currentImmunity = initialImmunity;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public mxCell getInfector() {
		return infector;
	}

	public void setInfector(mxCell infector) {
		this.infector = infector;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public double getInitialImmunity() {
		return initialImmunity;
	}

	public double getCurrentImmunity() {
		return currentImmunity;
	}

	public void setImmunity(double immunity) {
		this.initialImmunity = immunity;
		this.currentImmunity = immunity;
	}

}
