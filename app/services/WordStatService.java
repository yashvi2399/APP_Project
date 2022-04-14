package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import models.Stats;

/**
 * @author Yashvi Pithadia
 *
 */
public class WordStatService {

	/**
	 * @param preview_description: Project description
	 * @return calculated wordstat list
	 */
	public static ArrayList<Stats> setStats(String preview_description) {
		
		ArrayList<Stats> statsList= new ArrayList<Stats>();
		
		List<String> words = Arrays.asList(preview_description.split(" "));
		
		
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
            
            statsList.add(stat);
            
			}
		}
		
		return statsList;
		
	}
}
