package datasetgenerator;

import java.util.Random;

public class Point {
	private float x1;
	private float x2;
	private String category;
	
	public Point(float x1, float x2, String category) {
		
		this.x1 = x1;
		this.x2 = x2;
		this.category = category;
	}
	
	public Point(float x1rangeMin, float x1rangeMax, float x2rangeMin, float x2rangeMax) {
	
		x1 = x1rangeMin + (new Random().nextFloat() * (x1rangeMax - x1rangeMin));
		x2 = x2rangeMin + (new Random().nextFloat() * (x2rangeMax - x2rangeMin));
		category = categorizePoint(x1, x2);
	}
	
	private String categorizePoint(float x1, float x2) {
		
		// this can be done outside a point's initialization
		// but we're going the quick & dirty route
		String category = "";
		if(Math.pow(x1 - 0.5, 2) + Math.pow(x2 - 0.5, 2) < 0.16) {
			
			category = "C1";
		}
		else if(Math.pow(x1 + 0.5, 2) + Math.pow(x2 + 0.5, 2) < 0.16) {
			
			category = "C1";
		}
		else if(Math.pow(x1 - 0.5, 2) + Math.pow(x2 + 0.5, 2) < 0.16) {
			
			category = "C2";
		}
		else if(Math.pow(x1 + 0.5, 2) + Math.pow(x2 - 0.5, 2) < 0.16) {
			
			category = "C2";
		}
		else if((x1 > 0 && x2 > 0) || (x1 < 0 && x2 < 0)) {
			
			category = "C3";
		}
		else if((x1 < 0 && x2 > 0) || (x1 > 0 && x2 < 0)) {
			
			category = "C4";
		}
		
		return category;
	}
	
	public void setCategory(String newCategory) {
		
		this.category = newCategory;
	}
	
	public String getCategory() {
		
		return category;
	}
	
	public double [] createCategoryVector() {
		
		double categoryVec [] = {0,0,0,0};
		if (this.category.equals("C1")) { categoryVec[0] = 1; }
		if (this.category.equals("C2")) { categoryVec[1] = 1; }
		if (this.category.equals("C3")) { categoryVec[2] = 1; }
		if (this.category.equals("C4")) { categoryVec[3] = 1; }
		
		return categoryVec;
	}
	
	public Point createDeepCopy() {
		
		return new Point(this.x1, this.x2, this.category);
	}
	
	public String toString() {
		
		return (String.valueOf(x1) + "," + String.valueOf(x2));
	}
	
	public double [] getCoordinates() {
		
		double point[] = {x1, x2};
		return point;
	}
}
