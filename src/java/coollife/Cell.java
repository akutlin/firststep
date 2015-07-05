package coollife;

public class Cell {
	private static final int PLANT_FOOD_MAX = 500;
	private static final double ANIMAL_SIZE = 0.005;
	
	private int plantFood = 500;
	private double plants = 1;
	private int animals = 0;
	
	public Cell(int plantFood, int plants, int animals) {
		this.plantFood = plantFood;
		this.plants = plants;
		this.animals = animals;
	}
	
	public Cell() {
		this (300, 50, 0);
	}

	public int getPlantFood() {
		return plantFood;
	}
	
	public double getPlants() {
		return plants;
	}
	
	public int getAnimals() {
		return animals;
	}
	
	public int getPlantFoodMax() {
		return PLANT_FOOD_MAX;
	}
	
	public void setPlantFood(int plantFood) {
		this.plantFood = plantFood;
	}
	
	public void setPlants(double plants) {
		this.plants = plants;
	}
	
	public void setAnimals(int animals) {
		this.animals = animals;
	}
	
	public double getAnimalSize() {
		return ANIMAL_SIZE;
	}
}
