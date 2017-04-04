import java.util.*;

public class Sha {
	
	private String finalHash; 	
	
	public void ShaOne(String input){
		
		Ash k = new Ash();

		BitSet wholeChunk = new BitSet();

		//convert string input into binary
		StringBuilder temp;
		StringBuilder binary = new StringBuilder(); 
		
		for(int i= 0; i<input.length(); i++){
			temp = new StringBuilder(); 
			temp.append(Integer.toString((int)input.charAt(i), 2));
			while(temp.length()!=8){
				temp.insert(0, '0');
			}
				
			binary.append(temp);
		}
	
		
		int counter=binary.length();
		String x = Integer.toString(counter, 2); 
		binary.append(1);
		counter++;
		while(counter%512!=448){
			binary.append("0");
			counter++;
		}
		
		
		
		for(int i=0; i<(64-x.length()); i++){
			binary.append("0");
		}
		binary.append(x);
		
	
		wholeChunk = k.wholeString(binary.toString());
		
		int numOfChunks = binary.length()/512;
		BitSet[] chunks = new BitSet[numOfChunks];
		counter=0;
		for(int i=0; i<numOfChunks; i++){
			
			chunks[i] = wholeChunk.get(counter, counter+512);
			k.breakChunk(chunks[i]);
			finalHash = k.mainLoop();
			counter+=512;
		} 

			
	}

	public String getHash(){
		return finalHash;
	}
}
