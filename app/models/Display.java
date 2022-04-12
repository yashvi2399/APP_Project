package models;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author Yashvi Pithadia
 *
 */
public class Display {
	String owner_id;
	long time_submitted;
	String title;
	String type;
	Jobs[] jobs;
	String preview_description;	
	ArrayList<Stats> stats = new ArrayList<Stats>();

	public ArrayList<Stats> getStats() {
		return stats;
	}
	public void setStats() {
		
		List<String> words = Arrays.asList(this.preview_description.split(" "));
		
		
		Map<String,Long> collect = words.stream()
			    .collect( Collectors.groupingBy( Function.identity(), Collectors.counting() ));
		
		LinkedHashMap<String, Long> countByWordSorted = collect.entrySet()
	            .stream()
	            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
	            .collect(Collectors.toMap(
	                    Map.Entry::getKey,
	                    Map.Entry::getValue,
	                    (v1, v2) -> {
	                        throw new IllegalStateException();
	                    },
	                    LinkedHashMap::new
	    ));
		Set<String> keys = countByWordSorted.keySet();
		
		for (String key : keys) {
			
			if(!key.equals("")) {
			
			Stats stat = new Stats();
			
			stat.setWord(key);
            stat.setCount(countByWordSorted.get(key));
            
            this.stats.add(stat);
            
			}
		}
		
	}
	public String getPreview_description() {
		return preview_description;
	}
	public void setPreview_description(String preview_description) {
		this.preview_description = preview_description;
	}

	/**
	 * To fetch owner ID
	 * @return owner ID
	 */
	public String getOwner_id() {
		return owner_id;
	}
	/**Store owner ID
	 * @param owner_id: Owner ID from query 
	 */
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	
	/**
	 * Fetch Project Title
	 * @return Project Title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Store Project Title
	 * @param title: Project Title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Fetch Project Submitted Time
	 * @return Project Submitted Time
	 */
	public String getTime_submitted() {
		
		Date date = new Date(time_submitted*1000);
		SimpleDateFormat sdate;
		sdate = new SimpleDateFormat("MMM dd yyyy");
		return sdate.format(date);
	}
	/**
	 * Store Project Submitted Time
	 * @param time_submitted: Project Submitted Time
	 */
	public void setTime_submitted(long time_submitted) {
		this.time_submitted = time_submitted;
	}

	
	/**
	 * Fetch Project Type
	 * @return Project Type
	 */
	public String getType() {
		return type;
	}
	/**
	 * Store Project Type
	 * @param type: Project Type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Fetch Skill Name
	 * @return Skill Name
	 */
	public Jobs[] getJobs() {
		return jobs;
	}
	/**
	 * Store Skill Name
	 * @param skills: Skill Name
	 */
	public void setJobs(Jobs[] jobs) {
		this.jobs = jobs;
	}
	
}

