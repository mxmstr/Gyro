import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class Gesture {

	Map<Long, Double[]> map;
		
	public Gesture(String fileName){
		
		map = new HashMap<Long, Double[]>();
		readGestureData(fileName);
		
	}
	
	/*
	Read from specified file. 
	*/
	public void readGestureData(String fileName) {
		
		try {
			
			FileReader inputFile = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(inputFile);
			
			String line;
			while ((line = reader.readLine()) != null) {
				
				StringTokenizer tokenizer = new StringTokenizer(line, ",");
				
				Long timestamp = Long.parseLong(tokenizer.nextToken());
				
				Double[] values = new Double[6];
				for (int i=0; i < values.length; i++)
					values[i] = Double.parseDouble(tokenizer.nextToken());
				
				map.put(timestamp, values);
				
			}
			
			reader.close();
			
		}
		catch (Exception e) {e.printStackTrace();}
		
	}
	
	/*
	from indexBegin to indexEnd, search data for values that are higher than threshold. 
	Return the first index where data has values that meet this criteria for at least winLength samples
	*/
	public static void searchContinuityAboveValue(
			Map<Long, Double[]> data, int indexBegin, int indexEnd, 
			Double threshold, int winLength) {
		
		
		
	}
	
	/*
	from indexBegin to indexEnd (where indexBegin is larger than indexEnd), 
	search data for values that are higher than thresholdLo and lower than thresholdHi. 
	Return the first index where data has values that meet this criteria for at least winLength samples.
	*/
	public static void backSearchContinuityWithinRange(
			Map<Long, Double[]> data, int indexBegin, int indexEnd, 
			double thresholdLo, double thresholdHi, int winLength) {
		
		
		
	}
	
	/*
	from indexBegin to indexEnd, search data1 for values that are higher than threshold1
	and also search data2 for values that are higher than threshold2. 
	Return the first index where both data1 and data2 have values that meet these criteria for at least winLength samples.
	*/
	public static void searchContinuityAboveValueTwoSignals(
			Map<Long, Double[]> data1, Map<Long, Double[]> data2, 
			int indexBegin, int indexEnd, 
			double threshold1, double threshold2, int winLength) {
		
		
		
	}
	
	/*
	from indexBegin to indexEnd, search data for values that are higher than thresholdLo and lower than thresholdHi. 
	Return the the starting index and ending index of all continuous samples that meet this criteria for at least winLength data points.
	*/
	public static void searchMultiContinuityWithinRange(
			Map<Long, Double[]> data, int indexBegin, int indexEnd, 
			double thresholdLo, double thresholdHi, int winLength) {
		
		
		
	}
	
	public static void main(String[] args) {
		
		Gesture g = new Gesture("src//latestSwing.csv");
		System.out.println(g.map.get(8741L)[0]);
		
	}

}
