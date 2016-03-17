import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Zach
 */
public class pw_check {
    int charCounter = 0;
    int numCounter = 0;
    int symbolCounter = 0;
    int t = 0;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        File good_passwords = new File("good_passwords.txt");
        pw_check zach = new pw_check();
        DLB goodPasswordDLB = new DLB();
        DLB newDLB = new DLB();
        newDLB.generateBadStringTrie();        
        if(args.length != 0){
            if (args[0].equalsIgnoreCase("-g")){
                if(good_passwords.exists()){
                    System.out.println("List of good passwords already exists");
                }else{
                    //zach.generateGoodPasswords(newDLB);
                    FileWriter fw = new FileWriter("good_passwords.txt");
                    enumPasswords("", 0, 3, 2, 2, fw,newDLB);
	            fw.close();
                }
            }
            else {
                System.out.println("Invalid Argument");
                System.exit(0);
            }
        } else{
            if(!(good_passwords.exists())){
              System.out.println("List of good passwords has not yet been generated: enter \"java pw_check -g\" to generate list");
              System.exit(0);
            }
        }
        System.out.println("Good Passwords Generated: Creating Good Password DLB");
        BufferedReader read = new BufferedReader(new FileReader("good_passwords.txt"));
        String line;
        while ((line = read.readLine()) != null) {
            goodPasswordDLB.add(line);
        } 
        zach.getPasswordToBeChecked(goodPasswordDLB, newDLB);
        
    }
    
    /**
     * enumerates good passwords to good_password.txt
     * @param pw string of possible password
     * @param depth number of character currently in the possible password string
     * @param avai_chars # of characters that can still be added to the good password
     * @param avai_digits # of digits that can still be added to the good password
     * @param avai_symbols # of symbols that can still be added to the good password
     * @param fw writes good_passwords to good_passwords.txt
     * @param newDLB containing malformed phrases
     * @throws IOException 
     */
    public static void enumPasswords(String pw, int depth, int avai_chars, int avai_digits, int avai_symbols, FileWriter fw, DLB newDLB) throws IOException {
        char[] possibleChar = "bcdefghjklmnopqrstuvwxyz".toCharArray();
        char[] possibleDigits = "02356789".toCharArray();        
        char[] m_symbols = "!@$^_*".toCharArray();
        for (int i = 0; i < pw.length(); ++i) {
            if (1 == newDLB.search(pw.substring(i))) {
                return;
            }
        }
        if (depth == 5) {
            /* at least 1 char, 1 digit, 1 symbol */
                if (avai_chars != 3 && avai_digits != 2 && avai_symbols != 2) {
                    try {
                        fw.write(pw + System.getProperty("line.separator"));
                    } catch (IOException ex) {
                        System.err.println("write good_passwords.txt error");
                        return;
                    }
                }
            return;
        }
        if (avai_chars > 0) {
            /* limit the number of chars */
            for (int charCounter = 0; charCounter<possibleChar.length; charCounter++) {
                enumPasswords(pw + possibleChar[charCounter], 1 + depth, avai_chars - 1, avai_digits, avai_symbols, fw, newDLB);
            }
        }
        if (avai_digits > 0) {
            /* limit the number of digits */
            for (int digitsCounter = 0; digitsCounter<possibleDigits.length; digitsCounter++) {
                enumPasswords(pw + possibleDigits[digitsCounter], 1 + depth, avai_chars, avai_digits - 1, avai_symbols, fw, newDLB);
            }
        }
        if (avai_symbols > 0) {
            /* limit the number of symbols */
            for (int i = 0; i < 6; ++i) {
                enumPasswords(pw + m_symbols[i], 1 + depth, avai_chars, avai_digits, avai_symbols - 1, fw, newDLB);
            }
        }
    }

    /**
     * gets input from user and calls checkPassword() if it is a valid input
     * @param goodPasswordDLB 
     */
    private void getPasswordToBeChecked(DLB goodPasswordDLB, DLB newDLB) throws IOException {
        Scanner scan = new Scanner(System.in);
        String enteredPassword;
        while (true) {
            System.out.println("Enter the password you wish to check: -enter exit to terminate"); 
            enteredPassword = scan.nextLine();
            if(enteredPassword.equalsIgnoreCase("exit")){
                System.exit(0);
            }else if (enteredPassword.length() == 0) {
                    System.out.println("Invalid Input: ");
            } else{
                enteredPassword = enteredPassword.toLowerCase();
                checkPassword(enteredPassword, goodPasswordDLB, newDLB);
            }
                
        }
    }

     /**
     * Congratulates user if entered a good password or calls getWordsWithLongestPrefix() to return 10 reconmended passwords
     * @param enteredPassword
     * @param goodPasswordDLB 
     */
    private void checkPassword(String enteredPassword, DLB goodPasswordDLB, DLB newDLB) throws IOException {
        if(goodPasswordDLB.search(enteredPassword) == 1){
            System.out.println("Congratulations: You have entered a good password");
        }else{
            System.out.println("Invalid Password: Here are some reconmended passwords");
            String longestPrefix = goodPasswordDLB.getLongestPrefix(enteredPassword); 
            counter(longestPrefix);
            generateReconmendedPasswords(longestPrefix, longestPrefix.length(), 3-charCounter, 2-numCounter, 2,newDLB);
            
        }
    }


     public void generateReconmendedPasswords(String pw, int depth, int avai_chars, int avai_digits, int avai_symbols, DLB newDLB) throws IOException {
        char[] possibleChar = "bcdefghjklmnopqrstuvwxyz".toCharArray();
        char[] possibleDigits = "02356789".toCharArray();        
        char[] m_symbols = "!@$^_*".toCharArray();
        for (int i = 0; i < pw.length(); ++i) {
            if (1 == newDLB.search(pw.substring(i))) {
                return;
            }
        }
        if (depth == 5) {
            /* at least 1 char, 1 digit, 1 symbol */
                if (avai_chars != 3 && avai_digits != 2 && avai_symbols != 2) {
                    if(t<10){        
                        System.out.println(pw);
                        t++;

                    }
                }
            return;
        }
        if (avai_chars > 0) {
            /* limit the number of chars */
            for (int charCounter = 0; charCounter<possibleChar.length; charCounter++) {
                generateReconmendedPasswords(pw + possibleChar[charCounter], 1 + depth, avai_chars - 1, avai_digits, avai_symbols, newDLB);
            }
        }
        if (avai_digits > 0) {
            /* limit the number of digits */
            for (int digitsCounter = 0; digitsCounter<possibleDigits.length; digitsCounter++) {
                generateReconmendedPasswords(pw + possibleDigits[digitsCounter], 1 + depth, avai_chars, avai_digits - 1, avai_symbols, newDLB);
            }
        }
        if (avai_symbols > 0) {
            /* limit the number of symbols */
            for (int i = 0; i < 6; ++i) {
                generateReconmendedPasswords(pw + m_symbols[i], 1 + depth, avai_chars, avai_digits, avai_symbols - 1, newDLB);
            }
        }
    }
 
    private void counter(String longestPrefix){
        String[] characters = longestPrefix.toString().split("");
        for(int x = 0; x < characters.length; x++){
            if(characters[x] == "0" || characters[x] == "2" || characters[x] == "3"||characters[x] == "5"||characters[x] == "6"||characters[x] == "7"||characters[x] == "8"||characters[x] == "9"){
                numCounter++;
            }else if(characters[x] == "!" ||characters[x] == "@" ||characters[x] == "$" ||characters[x] == "^" ||characters[x] == "_" ||characters[x] == "*" ){
                symbolCounter++;
            } else{
                charCounter++;
            }
        }
    }
     
}
