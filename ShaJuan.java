import java.util.Scanner;

//TESTER
public class ShaJuan{
	public static void main(String args[]){
		
			Scanner reader = new Scanner(System.in);  
			System.out.println("Enter a string to hash: ");
			String input = reader.nextLine();
			Sha  s = new Sha();
			s.ShaOne(input);
			System.out.println(s.getHash());

	}

}