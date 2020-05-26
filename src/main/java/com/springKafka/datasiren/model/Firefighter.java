package com.springKafka.datasiren.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Firefighter implements Serializable{

    private long id;
    
    private int CO;
    
    private int temp;    
    
    private int hum;    
    
    private int bat;
    
    private double lat;
    
    private double longi;    
    
    private double alt;    
    
    private double hr;

	public void setLat(double d) {
		this.lat = d;
		
	}

	public void setLongi(double d) {
		this.longi = d;
		
	}

	public void setAlt(double d) {
		this.alt = d;
		
	}

	public void setCO(int value) {
		this.CO = value;
		
	}

	public void setHr(double value) {
		this.hr = value;
	}

	public void setBat(int value) {
		this.bat = value;
	}

	public void setTemp(int value) {
		this.temp = value;
	}

	public void setHum(int value) {
		this.hum = value;
	}

	public double getLat() {
		
		return this.lat;
	}

	public double getLongi() {
		
		return this.longi;
	}

	public double getAlt() {

		return this.alt;
	}

	public int getCO() {

		return this.CO;
	}

	public double getHr() {

		return this.hr;
	}

	public int getBat() {

		return this.bat;
	}

	public int getTemp() {

		return this.temp;
	}

	public int getHum() {

		return this.hum;
	}

}
