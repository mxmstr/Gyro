import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Gesture {

	ArrayList<Object[]> data;
		
	public Gesture(String fileName){
		
		data = new ArrayList<Object[]>();
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
				
				Object[] values = new Object[7];
				
				values[0] = Long.parseLong(tokenizer.nextToken());
				
				for (int i=1; i < values.length; i++)
					values[i] = Double.parseDouble(tokenizer.nextToken());
				
				data.add(values);
				
			}
			
			reader.close();
			
		}
		catch (Exception e) {e.printStackTrace();}
		
	}
	
	private ArrayList<Object[]> splitDataByColumn(int start, int end) {
		
		ArrayList<Object[]> splitData = new ArrayList<Object[]>();
		
		for (int i = 0; i < data.size(); i++) {
			
			Object[] values = new Object[end - start + 1];
			
			for (int j = start; j <= end; j++)
				values[j - start] = data.get(i)[j];
			
			splitData.add(values);
			
		}
		
		return splitData;
		
	}
	
	public ArrayList<Object[]> getData() {
		
		return data;
		
	}
	
	public static boolean checkValuesMeetCriteria(
			Object[] values, Double thresholdLo, Double thresholdHi, int winLength) {
	
		int count = 0;
		
		for (int j = 0; j < values.length; j++) {
			
			Double value = (Double)values[j];
			
			if (value > thresholdLo && value < thresholdHi)
				count++;
			
			if (count == winLength)
				return true;
				
		}
		
		return false;
		
	}
	
	/*
	from indexBegin to indexEnd, search data for values that are higher than threshold. 
	Return the first index where data has values that meet this criteria for at least winLength samples
	*/
	public static int searchContinuityAboveValue(
			ArrayList<Object[]> data, int indexBegin, int indexEnd, 
			Double threshold, int winLength) {
		
		for (int i = indexBegin; i < indexEnd; i++)
			if (checkValuesMeetCriteria(data.get(i), threshold, Double.MAX_VALUE, winLength))
				return i;
		
		return -1;
		
	}
	
	/*
	from indexBegin to indexEnd (where indexBegin is larger than indexEnd), 
	search data for values that are higher than thresholdLo and lower than thresholdHi. 
	Return the first index where data has values that meet this criteria for at least winLength samples.
	*/
	public static int backSearchContinuityWithinRange(
			ArrayList<Object[]> data, int indexBegin, int indexEnd, 
			double thresholdLo, double thresholdHi, int winLength) {
		
		for (int i = indexBegin; i > indexEnd; i--) 
			if (checkValuesMeetCriteria(data.get(i), thresholdLo, thresholdHi, winLength))
				return i;
			
		
		return -1;
		
	}
	
	public static int frontSearchContinuityWithinRange(
			ArrayList<Object[]> data, int indexBegin, int indexEnd, 
			double thresholdLo, double thresholdHi, int winLength) {
		
		for (int i = indexBegin; i < indexEnd; i++) 
			if (checkValuesMeetCriteria(data.get(i), thresholdLo, thresholdHi, winLength))
				return i;
		
		return -1;
		
	}
	
	/*
	from indexBegin to indexEnd, search data1 for values that are higher than threshold1
	and also search data2 for values that are higher than threshold2. 
	Return the first index where both data1 and data2 have values that meet these criteria for at least winLength samples.
	*/
	public static int searchContinuityAboveValueTwoSignals(
			ArrayList<Object[]> data1, ArrayList<Object[]> data2, 
			int indexBegin, int indexEnd, 
			double threshold1, double threshold2, int winLength) {
		
		int index1 = searchContinuityAboveValue(data1, indexBegin, indexEnd, threshold1, winLength);
		int index2 = searchContinuityAboveValue(data2, indexBegin, indexEnd, threshold2, winLength);
		
		if (index1 == -1 || index2 == -1)
			return -1;
		
		if (index1 == index2)
			return index1;
		
		if (index1 < index2)
			indexBegin = index1 + 1;
		else
			indexBegin = index2 + 1;
		
		return searchContinuityAboveValueTwoSignals(
				data1, data2, 
				indexBegin, indexEnd, 
				threshold1, threshold2, winLength);
		
	}
	
	/*
	from indexBegin to indexEnd, search data for values that are higher than thresholdLo and lower than thresholdHi. 
	Return the the starting index and ending index of all continuous samples that meet this criteria for at least winLength data points.
	*/
	public static int[] searchMultiContinuityWithinRange(
			ArrayList<Object[]> data, int indexBegin, int indexEnd, 
			double thresholdLo, double thresholdHi, int winLength) {
		
		int[] indicies = new int[2];
		
		/*
		Find start index
		*/
		indexBegin = frontSearchContinuityWithinRange(
				data, indexBegin, indexEnd, 
				thresholdLo, thresholdHi, winLength);
		
		if (indexBegin == -1)
			return null;
		
		indicies[0] = indexBegin;
		
		/*
		Find end index
		*/
		indexEnd = backSearchContinuityWithinRange(
				data, indexEnd, indexBegin, 
				thresholdLo, thresholdHi, winLength);
		
		if (indexEnd == -1)
			return null;
		
		indicies[1] = indexEnd;
		
		return indicies;
		
	}
	
	public static void main(String[] args) {
		
		Gesture g1 = new Gesture("src//latestSwing.csv");
		
		ArrayList<Object[]> data = g1.getData();
		ArrayList<Object[]> timestamps = g1.splitDataByColumn(0, 0);
		ArrayList<Object[]> accels = g1.splitDataByColumn(1, 3);
		ArrayList<Object[]> gyros = g1.splitDataByColumn(4, 6);
		
		System.out.println(searchContinuityAboveValue(gyros, 0, 1275, 20.0, 2));
		System.out.println(backSearchContinuityWithinRange(gyros, 1275, 0, 5.0, 20.0, 2));
		System.out.println(searchContinuityAboveValueTwoSignals(gyros, accels, 0, 1275, 5.0, 10.0, 1));
		
		int[] indicies = searchMultiContinuityWithinRange(gyros, 0, 1275, 5.0, 20.0, 2);
		if (indicies != null)
			System.out.println(indicies[0] + " " + indicies[1]);
		
	}

}
