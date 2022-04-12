package services;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;

import com.google.inject.Singleton;

import models.*;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;

import models.Display;


/**
 * @author Yash Trivedi
 *
 */

public class FleschCalculator {

	/**
	 * Method calculating Flesch value
	 * @param Text for with Flesch value is to be calculated
	 * @return Array containing Flesch Readability Index value and Flesch Kincaid grade level  
	 */
	public static double[]  calculateScore(String textToScore) {
		
    double nsentences = 0.0;
    double nwords = 0.0;
    double nsyllables = 0.0;
    double double_array[] = new double[100];
    
    List <String> sentences = new ArrayList<String>();
    List <String> words = new ArrayList<String>();
    

    if (textToScore != null) {
    	
    	sentences = splitSentences(textToScore);
    	
    	for (int i = 0; i < sentences.size(); i++) {
        	nsentences += 1;
    	
        	words = splitWord(sentences.get(i));
        	
        	for (int j = 0; j < words.size(); j++) {
        		
        		nwords += 1;
            	nsyllables += countSyllables(words.get(j));
         	}
    	}

        if (nsentences > 0 && nwords > 0 && nsyllables > 0) {

    		double_array[1]=(0.39 * (nwords / nsentences)) + (11.8 * (nsyllables / nwords)) - 15.59;
    		double_array[0]=206.835 - (84.6*(nsyllables / nwords)) - (1.015*nwords / nsentences);
         
        }

    }
    
    
    return double_array;
}

	
	/**
	 * @param str: string of words containing description separated by "."
	 * @return String without fullstop
	 */
	public static List<String> splitSentences(String str){
	    return Stream.of(str.split("\\."))
	      .map (elem -> new String(elem))
	      .collect(Collectors.toList());
	}
	
	/**
	 * @param str string of words separated by space
	 * @return string without space
	 */
	public static List<String> splitWord(String str){
	    return Stream.of(str.split("\\s+"))
	      .map (elem -> new String(elem))
	      .collect(Collectors.toList());
	}
	
	
	
/**
 * Method counting the number of syllables
 * 
 * @param word The word tested for its syllables
 * @return the number of syllables in the word
 */
private static int countSyllables(String words) {
    int count = 0;
    words = words.toLowerCase();

    if (words.length() > 0 && words.charAt(words.length() - 1) == 'e') {
        if (silente(words)) {
            String newword = words.substring(0, words.length() - 1);
            count = count + countit(newword);
        } else {
            count++;
        }
    } else {
        count = count + countit(words);
    }
    return count;
}

/**
 * @param word: Substring
 * @return if vowel is present in the word
 */
private static boolean silente(String word) {
    word = word.substring(0, word.length() - 1);

    Pattern yup = Pattern.compile("[aeiouy]");
    Matcher m = yup.matcher(word);

    if (m.find()) {
        return true;
    } else
        return false;
}

/**
 * @param word: Substring 
 * @return counting number of syllables
 */
private static int countit(String word) {
    int count = 0;
    Pattern splitter = Pattern.compile("[^aeiouy]*[aeiouy]+");
    Matcher m = splitter.matcher(word);

    while (m.find()) {
        count++;
    }
    return count;
}

}

