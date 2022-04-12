package models;

public class Flesch {
	private double Flesch_count;
	private double FKGL;
	/**
	 * Fetch Flesch_count
	 * @return Flesch_count
	 */
	public double getFlesch_count() {
		return Flesch_count;
	}
	/**
	 * Store Flesch_count
	 * @param Flesch_count: flesch_count
	 */
	public void setFlesch_count(double flesch_count) {
		
		this.Flesch_count = flesch_count;
	}
	/**
	 * Fetch FKGL
	 * @return FKGL
	 */
	public double getFKGL() {
		return FKGL;
	}
	/**
	 * Store FKGL
	 * @param fKGL: FKGL
	 */
	public void setFKGL(double fKGL) {
		this.FKGL = fKGL;
	}
	
}
