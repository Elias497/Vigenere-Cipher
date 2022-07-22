import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Vigenere {
Scanner scan;
String[][] array;

//purpose: To create a File object if possible, and initializes a Scanner object
//assumptions: There is a file
//inputs: fileName - the location of the file
//post-conditions: Creates File and a Scanner object or print out "File is not found"
public Vigenere(String fileName)
{
	try
	{
		File file = new File(fileName); 
		scan= new Scanner(file); 
	}
	catch(FileNotFoundException e)
	{
		System.out.println("File is not found");  
	}
}

//purpose: To try to create a 2D array from the file using the Scanner object
//assumptions: There is a file organized to have tokens with equal lengths and width
//inputs: None
//post-conditions: A 2D array is created or print out "The text file is not compatible"
private void createArray()
{
	try
	{
		array = new String[26][26];
		for(int i=0; i < array.length; i++)
		{
			for(int j=0; j < array[i].length; j++)
			{
				array[i][j] = scan.next();
			}
		}
		scan.close();
	}
	catch(NullPointerException e)
    {
        System.out.print("The text file is not compatible");
    }
}
	
//purpose: To encrypt a String with with a given key using the 2D array
//assumptions: There exists a String and a key value
//inputs: m - The given String value
//		  k - The given key value
//post-conditions: To return the encrypted cipher
private String encrypt(String m, String k)
{
	String string = modifyString(m);
	String key = modifyKeyLength(k, string.length());
	String cipher = "";
	for(int i=0; i < string.length(); i++)
	{
		cipher = cipher + array[string.charAt(i)-65][key.charAt(i)-65];
	}
	return cipher;
}

//purpose: To change the given String into all capital letters and remove all spaces
//assumptions: The string must not contain anything other than letters and/or spaces
//inputs: m - A String that needs or does not need to be modified
//post-conditions: returns the string with all capital letters and with no spaces
private String modifyString(String m)
{
	String upper = m.toUpperCase();
	String Final = "";
	for (int i=0; i < upper.length(); i++)
	{
		if(upper.charAt(i) != ' ')
		{
			Final = Final + upper.charAt(i);
		}
	}
	return Final;
}

//purpose: To make sure that the key value is the same lengths as the given String
//assumptions: The key is given as all capital letters
//inputs: k - The key that will be modified
//		  m - The length of the String
//post-conditions: The key is returned with the same length as the String
private String modifyKeyLength(String k, int m)
{
	String key = "";
	while(key.length() < m)
	{
		key = key + k;
	}
	key = key.substring(0, m);
	return key;
}

//purpose: To calculate the frequency of each character in the String
//assumptions: There is a String with at least one letter
//inputs: c - The String that is being analyzed
//post-conditions: Displays each character with its frequency
private void printFrequencies(String c)
{
	int count = 0;
	for(int i = 0; i < c.length(); i++)
	{
		if(checkPrevious(c, i))
		{
			for(int j = 0; j < c.length(); j++)
			{
				if(c.charAt(i) == c.charAt(j))
				{
					count++;
				}
			}
			System.out.println(c.charAt(i) + " " + count);
			count = 0;
		}
	}
}

//purpose: To check if a specific character is in a lower index position than the one given
//assumptions: None
//inputs: c - The String that is being used to check
//		  i - The index position (specific character) that is being checked for the String
//post-conditions: false is returned if there exists at least 1 more before or true if none
private boolean checkPrevious(String c, int i)
{
	if(i != 0)
	{
		for(int j = 0; j < i; j++)
		{
			if(c.charAt(i) == c.charAt(j))
				return false;
		}
	}
	return true;
}

//purpose: To decrypt a cipher using a key
//assumptions: There exists a cipher and a key
//inputs: c - The cipher that will be decrypted
//		  k - The key value that will be used to decrypt
//post-conditions: The plain (decrypted) text is returned
private String decrypt(String c, String k)
{
	String cipher = modifyString(c);
	String key = modifyKeyLength(k, c.length());
	String plain = "";
	String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	char character = 'A';
	for(int i=0; i < cipher.length(); i++)
	{
		character = alphabet.charAt(findRow(cipher, key, i));
		plain = plain + character;
		
	}
	return plain;
}

//purpose: To find the row position(letter) that is needed for the plain text
//assumptions: There exists a cipher character
//inputs: cipher - The String that is being analyzed for the plain text version
//		  i - The character position of the String being analyzed
//post-conditions: Return the row position(letter)
private int findRow(String cipher, String key, int i)
{
	String aString = "";
	for(int x = 0; x < 26; x++)
	{
		aString = aString + cipher.charAt(i);
		if(array[x][key.charAt(i)-65].equals(aString))
		{
			return x;
		}
		aString = "";
	}
	return -1;
}


//TEST CODE
public static void main(String [] args)									//EXPECTED
{	
	//Necessary 2 lines beginning code placed here
	Vigenere run = new Vigenere("vigenere.txt");
	run.createArray();
	
	//Testing Constructor											
	Vigenere fail = new Vigenere("");							   		// File is not found
	
	//Testing createArray
	fail.createArray();													//The text file is not compatible
	
	//This is an extra space for test display organization
	System.out.println("");	System.out.println("");
	
	//Testing modifyKeyLength(String k, int m)
	System.out.println(run.modifyString("Hello"));							// HELLO
	System.out.println(run.modifyString("Hi my name is Elias   "));			// HIMYNAMEISELIAS
	
	//Testing modifyKeyLength(String k, int m)
	for(int i = 0; i < 15; i++)
	System.out.println(run.modifyKeyLength("LEMON", i));					//LEMON is displayed and repeated depending on i(length of string)
	
	//This is an extra space for test display organization
	System.out.println("");
	
	//Testing encrypt(String m, String k) (Also uses the 2 previous methods)
	System.out.println(run.encrypt("AtTaCkA t DaWN", "LEMON"));  			//LXFOPVEFRNHR
	System.out.println(run.encrypt("Elias", "ANYTHING"));					//EYGTZ
	System.out.println(run.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ")); //Values down/right the diagonal of the picture
	
	System.out.println("");
	
	//Testing checkPrevious(String c, int i)
	System.out.println(run.checkPrevious("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 25)); //false (Z is never shown before)
	System.out.println(run.checkPrevious("ATTACKATDAWN", 0));				//false
	System.out.println(run.checkPrevious("ATTACKATDAWN", 1));				//false
	System.out.println(run.checkPrevious("ATTACKATDAWN", 2));				//true (T is shown once before)
	System.out.println(run.checkPrevious("ATTACKATDAWN", 3));				//true (A is shown before)
	
	System.out.println("");
	
	//Testing printFrequencies(String c) (Also uses the previous method)
	run.printFrequencies("HELLO");	 										//H 1 
																			//E 1 
																			//L 2 
																			//O 1	
	run.printFrequencies("AAAAAAAAAAAAAACAAAAAAAAAAAAAAAA"); 				//A 30
																			//B 1
	System.out.println("");
	
	//Testing findRow(String cipher, String key, int i)
	System.out.println(run.findRow("LXFOPVEFRNHR", "LEMON", 0));			//0 (0th row is A)
	System.out.println(run.findRow("LXFOPVEFRNHR", "LEMON", 1));			//19 (19th row is T)
	System.out.println(run.findRow("LXFOPVEFRNHR", "LEMON", 2));			//19 (19th row is T)
	System.out.println(run.findRow("LXFOPVEFRNHR", "LEMON", 3));			//0 (0th row is A)
	System.out.println(run.findRow("LXFOPVEFRNHR", "LEMON", 4));			//2 (2nd row is C)	//Spells ATTAC
	
	System.out.println("");
	
	//Testing decrypt(String c, String k)
	System.out.println(run.decrypt("LXFOPVEFRNHR", "LEMON"));				//ATTACKATDAWN
	System.out.println(run.decrypt("LXFOPVEFRNHR", "LEMONLEMONLEMONLEMONLEMON")); //ATTACKATDAWN
}	
	
	
	
	
	
	
}
