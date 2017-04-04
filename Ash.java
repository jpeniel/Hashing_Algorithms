import java.util.*;
import java.math.BigInteger;
public class Ash{
	
		private String h0 = "01100111010001010010001100000001";
		private String h1 = "11101111110011011010101110001001";
		private String h2 = "10011000101110101101110011111110";
		private String h3 = "00010000001100100101010001110110";
		private String h4 = "11000011110100101110000111110000";
		//constant values
		
		private BitSet A;
		private BitSet B;
		private BitSet C;
		private BitSet D;
		private BitSet E;
		
		
	//convert string to bitset
	
   public BitSet fromString(String str){
	  BitSet t = new BitSet(32);
	  int   k = str.length()-1;
		for(int i=31; i>=0; i--){
			if(k<0){
				break;
			}
	 
			if(str.charAt(k) == '1'){
				t.set(i);			
			}
			k--;
		}
		return t;
	}
	
	
	//just for the original chunk
	public BitSet wholeString(String str){
	    
		BitSet t = new BitSet(str.length());
		for(int i=0; i<str.length(); i++){
			
			if(str.charAt(i) == '1'){
				t.set(i);			
			}
		}
		return t;
	}
	
	//convert bitset to string
	private StringBuilder strWord;
	public String printString(BitSet printWord, int j){
		strWord = new StringBuilder();
		for(int i=0; i<j; i++){
			if(printWord.get(i) == false){
				strWord.append("0");
			}else{
				strWord.append(1);
			}
		}
		return strWord.toString();
	}
	
	private BitSet[] wordChunk; //array of bitsets for 80 word chunk	
		//16 word chunk
	public void breakChunk(BitSet currChunk){
		wordChunk=new BitSet[80];
		int z = 0;
		for(int i=0; i<16; i++){
			wordChunk[i]=currChunk.get(z, z+32);
			z+=32;
		}
		extendChunk(wordChunk);
		
	}
	
	//extend to 80 words
	private BitSet tempChunk;
	public  void extendChunk(BitSet[] wordChunk){
	for(int i=16; i<80; i++){
			tempChunk = new BitSet(32);
			initializeL(tempChunk, wordChunk[i-3]);
			tempChunk.xor(wordChunk[i-8]);
			tempChunk.xor(wordChunk[i-14]);
			tempChunk.xor(wordChunk[i-16]);
			wordChunk[i] = leftRotate(tempChunk, 1);
			}
			
	}
		
		//left rotate binary
	public BitSet leftRotate(BitSet curr, int lr){
		BitSet newC = curr.get(lr, 32);
		int j = 0;
		for(int i = 32-lr; i<32; i++){
			if(curr.get(j) == true){
				newC.set(i);
			}
			j++;
		}
		return newC;
	}
	
	//initialize deep copy of bitset
	public void initializeL(BitSet L, BitSet h){
		for(int i =0; i<32; i++){
			if(h.get(i)==true){
				L.set(i);
			}
		}
	}
			
	private String[] k = {"01011010100000100111100110011001","01101110110110011110101110100001", "10001111000110111011110011011100", "11001010011000101100000111010110"};
	private BitSet tempChunk2;
	private BitSet tempChunk3;
	//where hashing happens 
	public String mainLoop(){
		
		//initialize variables
		A = fromString(h0);
		B = fromString(h1);
		C = fromString(h2);
		D = fromString(h3);
		E = fromString(h4);
		
		for(int i=0; i<80; i++){
			
			//initialize temporary bitset
			tempChunk = new BitSet(32);
			tempChunk2 = new BitSet(32);
			tempChunk3 = new BitSet(32);
			tempAdd = new BitSet();
			
			initializeL(tempChunk, B);
			
			if(i>=0 && i<=19){
			initializeL(tempChunk2, B);
			
			//function 1
			tempChunk.and(C);
			tempChunk2.flip(0, 32);
			tempChunk2.and(D);
			tempChunk.or(tempChunk2);
			
			combine(tempChunk, wordChunk[i],k[0]);
			
			}else if(i>=20 && i<=39){
				
				//function 2
				tempChunk.xor(C);
				tempChunk.xor(D);
				
				combine(tempChunk, wordChunk[i],k[1]);
				
			}else if(i>=40 && i<=59){
				
				initializeL(tempChunk2, B);
				initializeL(tempChunk3, C);
				
				//function 3
				tempChunk.and(C);
				tempChunk2.and(D);
				tempChunk3.and(D);
				tempChunk.or(tempChunk2);
				tempChunk.or(tempChunk3);
				
				combine(tempChunk, wordChunk[i],k[2]);
			}else if(i>=60 && i<=79){
				
				//function 4
				tempChunk.xor(C);
				tempChunk.xor(D);
			
				combine(tempChunk, wordChunk[i],k[3]);
			}
			
		}
		return theEnd();
	}
		
	private BigInteger bA, bE, bF, bK, sum; //combine
	private BitSet tempAdd;
	private BigInteger b0, b1, b2, b3, b4; //add
	private BigInteger hash0, hash1, hash2, hash3, hash4; //final binary hash
	private StringBuilder finalHash; //hexa string
	
	public void combine(BitSet F, BitSet currWord, String k){
			
			//put them all together <3
			 bA = new BigInteger(printString(leftRotate(A, 5), 32), 2);
			 bF = bA.add(new BigInteger(printString(F, 32), 2));
			 bE = bF.add(new BigInteger(printString(E, 32), 2));
			 bK = bE.add(new BigInteger(k,2));
			sum = bK.add(new BigInteger(printString(currWord, 32), 2));
		 
			//convert to bitset
			tempAdd = fromString(sum.toString(2));
		
			 //reassign variables
			 E = D.get(0, 32);
			 D = C.get(0, 32);
			 C = leftRotate(B, 30);
			 B = A.get(0,32);
			 A = tempAdd.get(0,32);
			
		
			
	}
		
	public String theEnd(){
		finalHash = new StringBuilder();
			//add 
		b0 = (new BigInteger(printString(A, 32),2)).add(new BigInteger(h0,2));
		b1 = (new BigInteger(printString(B, 32),2)).add(new BigInteger(h1,2));
		b2 = (new BigInteger(printString(C, 32),2)).add(new BigInteger(h2,2));
		b3 = (new BigInteger(printString(D, 32),2)).add(new BigInteger(h3,2));
		b4 = (new BigInteger(printString(E, 32),2)).add(new BigInteger(h4,2));
		
		
		//convert to binary
		hash0 = new BigInteger(truncate(b0.toString(2)), 2);
		hash1 = new BigInteger(truncate(b1.toString(2)), 2);
		hash2 = new BigInteger(truncate(b2.toString(2)), 2);
		hash3 = new BigInteger(truncate(b3.toString(2)), 2);
		hash4 = new BigInteger(truncate(b4.toString(2)), 2);
		
		h0 = hash0.toString(2);
		h1 = hash1.toString(2);
		h2 = hash2.toString(2);
		h3 = hash3.toString(2);
		h4 = hash4.toString(2);
		
	

		//convert five hashes to hexa then join together
		finalHash.append(hash0.toString(16));
		finalHash.append(hash1.toString(16));
		finalHash.append(hash2.toString(16));
		finalHash.append(hash3.toString(16));
		finalHash.append(hash4.toString(16));
		
		return finalHash.toString();
		
	}
		
	
	
	//truncate
	public String truncate(String printWord){
		strWord = new StringBuilder();
		int k = printWord.length()-1;
		for(int i=31; i>=0; i--){
				if(k<0){
					break;
				}
			if(printWord.charAt(k) =='0'){
				strWord.append("0");
			}else{
				strWord.append(1);
			}
				k--;
		}
		strWord.reverse();
		return strWord.toString();
	}
	 

}