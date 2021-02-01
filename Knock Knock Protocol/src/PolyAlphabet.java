import java.io.*;
import java.net.*;
import java.util.*;

public class PolyAlphabet{
	String result; //encrypted message
	
	//constructor, takes in user message
	public PolyAlphabet(String message) {
		result = "";
		int[] shiftSeq = new int[]{5, 19, 19, 5, 19};
		int shiftIndex = 0; 
		
		//shift characters of message according to a sequence in order to encrypt the message
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
		
	}
	
}