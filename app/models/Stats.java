package models;

/**
 * @author Yashvi Pithadia
 *
 */
public class Stats {

	private String word;
	private Long count;
	
	/**
	 * Fetch Word
	 * @return Word
	 */
	public String getWord() {
		return word;
	}
	/**
	 * Store Word
	 * @param word: Word
	 */
	public void setWord(String word) {
		this.word = word;
	}
	
	/**
	 * Fetch Word count
	 * @return Word Count
	 */
	public Long getCount() {
		return count;
	}
	/**
	 * Store Word Count
	 * @param count: Word Count
	 */
	public void setCount(Long count) {
		this.count = count;
	}
	
}
