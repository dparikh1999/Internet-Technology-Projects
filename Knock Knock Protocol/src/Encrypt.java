import java.io.*;
import java.net.*;
import java.util.*;

public class Encrypt{
	public static void main(String argv[]) {
		PolyAlphabet test = new PolyAlphabet("nice to meet you!");
		System.out.println(test.result);
		
		/*Scanner in = new Scanner(System.in);
		System.out.println("Enter string to be encrypted: ");
		String msg = in.nextLine();
		System.out.println(encrypt(msg));
		
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File("prog1b.log")), true);
			pw.append(encrypt(msg));
			pw.flush();
			System.out.println("Done");
			Date date=java.util.Calendar.getInstance().getTime();  
			System.out.println(date);  
		} catch (FileNotFoundException e) {
			System.out.println("FNF");
		}*/
	}
	
	//method to encrypt entered string
	public static String encrypt(String message) {
		
		//shifts code in pattern C1C2C2C1C2 where C1 = 5 and C2 = 19
		String result = "";
		int[] shiftSeq = new int[]{5, 19, 19, 5, 19}; //changes based on position
		int shiftIndex = 0; 
		
		for (int i=0;  i<message.length(); i++) {
			char character = message.charAt(i);
			
			//check if lowercase alphabet char
			if (character >= 'a' && character <= 'z') {
				character = (char) ((character + shiftSeq[shiftIndex] - 97) % 26 + 97);
				shiftIndex = (shiftIndex+1) % 5;
			}
			
			//check if uppercase alphabet char
			else if (character >= 'A' && character <= 'Z') {
				character = (char) ((character + shiftSeq[shiftIndex] - 65) % 26 + 65);
				shiftIndex = (shiftIndex+1) % 5;
			}
			result += character;
		}
		
		return result;
	}
}