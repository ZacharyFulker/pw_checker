import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Impementaion of DLB for pw_check
 * @author Zach
 */
public class DLB {
	protected char endChar = '%'; 
        protected nodeForDLB firstNode; 
	
	/**
         * Adds desired string to DLB
         * @param newWord word being added to DLB
         * @param stringIndex current position within string
         * @param node current node
         */
	public void DLBadd(String newWord, int stringIndex, nodeForDLB node) {
            if( stringIndex < newWord.length() ) {
                while(node.horizontal != null) {
                    if( node.value == newWord.charAt(stringIndex) ) {
                        if(node.vertical == null && stringIndex+1 < newWord.length() ) {
                            node.vertical = new nodeForDLB( newWord.charAt(stringIndex+1), null, null );
                            DLBadd(newWord,stringIndex+1,node.vertical);						
                        }
                        else if(node.vertical == null) {
                            node.vertical = new nodeForDLB(endChar, null, null);
                        }
                        else if(node.vertical != null) {
                            DLBadd(newWord,stringIndex+1,node.vertical);
                        }
                    }
                    node = node.horizontal;
                }
                if( node.horizontal == null && node.value != newWord.charAt(stringIndex) ) {
                    node.horizontal = new nodeForDLB(newWord.charAt(stringIndex), null, null);
                    DLBadd(newWord,stringIndex,node.horizontal);
                }
                if( node.horizontal == null && node.value == newWord.charAt(stringIndex) ) {
                    if(node.vertical == null && stringIndex+1 < newWord.length() ) {
                        node.vertical = new nodeForDLB( newWord.charAt(stringIndex+1), null, null );
                        DLBadd(newWord,stringIndex+1,node.vertical);						
                    }
                    else if(node.vertical == null) {
                        node.vertical = new nodeForDLB(endChar, null, null);
                    }
                    else if(node.vertical != null) {
                        DLBadd(newWord,stringIndex+1,node.vertical);
                    }
                }
            }else {
                if( node.vertical == null ) {
                    node.vertical = new nodeForDLB(endChar, null, null);
                }
                else if( node.vertical != null && node.vertical.value != endChar) {
                    node.horizontal = new nodeForDLB(endChar, null, null);
                }
            }

	}
        
	/**
         * This is called if this is the first word being added to the DLB
         * @param newWord word being added to DLB
         */
        public void trieIsCurrentlyEmpty(String newWord) {
            firstNode = new nodeForDLB(newWord.charAt(0), null, null);
            DLBadd( newWord,0,firstNode );
	}

	/**
         * add calls trieIsCurrentlyEmpty() if first add, otherwise calls DLBadd() with stringIndex 0  and node firstNode 
         * @param newWord word being added to DLB
         */
	public void add(String newWord) {
            if(firstNode == null) {
                trieIsCurrentlyEmpty(newWord);
            }
            else {
                DLBadd(newWord,0,firstNode);
            }
	}

	/**
         * search calls DLBsearch with the initial parameters
         * @param word word being searched for 
         * @return 1 if a word is found, 0 if a prefix but not word is found, -1 if the prefix does not exist
         */
	public int search(String word) {
            return( DLBsearch(word,0,firstNode) );
	}

	
	/**
         * searches the DLB for the given word
         * @param word word being searched for
         * @param stringIndex current position in the string
         * @param node current node
         * @return 1 if a word is found, 0 if a prefix but not word is found, -1 if the prefix does not exist
         */
	public int DLBsearch(String word, int stringIndex, nodeForDLB node) {
            int found;
            if( stringIndex < word.length() ) {
                while(node.horizontal != null) {
                    if( node.value == word.charAt(stringIndex) ) {
                        found = DLBsearch(word,stringIndex+1,node.vertical);
                        if( found == 0 ) {
                            return( 0 );
                        }
                        else if( found == 1 ) {
                            return( 1 );
                        }
                        else {
                            return( -1 );
                        }
                }
                node = node.horizontal;
                }
                if( node.horizontal == null && node.value == word.charAt(stringIndex) ) {
                    found = DLBsearch(word,stringIndex+1,node.vertical);
                    if( found == 0 ) {
                            return( 0 );
                        }
                        else if( found == 1 ) {
                            return( 1 );
                        }
                        else {
                            return( -1 );
                        }
                }else {
                        return( -1 );
                    }
            }else {      
                if( node.value == endChar ){
                    return( 1 );		
                }    
                return( 0 );
            }
	}

        /**
         * returns the last node of the longest prefix found for the word given
         * @param word the word for which the last node of the longest prefix is returned
         * @return last node of the longest prefix is returned
         */
        public nodeForDLB getLastNodeOfPrefix(String s) {
            nodeForDLB child = firstNode;    
            nodeForDLB node = firstNode;
                for (int i = 0; i < s.length(); i++) {
                        node = node.vertical;
                        while(!(node.value == s.charAt(i))){
                            if(node.horizontal == null){
                                return child;
                            }
                            node = node.horizontal;
                        }
                        child = node;
                }
                return node;
        }
        
	/**
         * return the longest found prefix of the word given
         * @param word word being searched for longest prefix 
         * @return the string of the longest prefix found
         */
        public String getLongestPrefix(String s) {
            StringBuilder sb = new StringBuilder();
            nodeForDLB child = firstNode;    
            nodeForDLB node = firstNode;
                for (int i = 0; i < s.length(); i++) {
                        node = node.vertical;
                        while(!(node.value == s.charAt(i))){
                            if(node.horizontal == null){
                                return sb.toString();
                            }
                            node = node.horizontal;
                        }
                        sb.append(node.value);
                        child = node;
                }
                return sb.toString();
        }
	
	/**
         * reads in malformed phrases from dictionary.txt and enumerates malformed words with symbols to my_dictionary.txt and adds them to a DLB
         * @throws FileNotFoundException
         * @throws IOException 
         */
    protected void generateBadStringTrie() throws FileNotFoundException, IOException {
        BufferedReader read = new BufferedReader(new FileReader("dictionary.txt"));
        FileWriter fw = new FileWriter("my_dictionary.txt");
	BufferedWriter bw = new BufferedWriter(fw);
        String line;
        while ((line = read.readLine()) != null) {
            add(line); 
            bw.write(line);
            bw.newLine();
            String[] characters = line.split("");
            for(int i = 0; i<characters.length; i++){
                if(characters[i].equalsIgnoreCase("t")){
                    characters[i] = "7";
                    String line2 = String.join("", characters);
                    bw.write(line2);
                    bw.newLine();
                    add(line2);
                } else if(characters[i].equalsIgnoreCase("a")){
                    characters[i] = "4";
                    String line2 = String.join("", characters);
                    bw.write(line2);
                    bw.newLine();
                    add(line2);
                } else if(characters[i].equalsIgnoreCase("o")){
                    characters[i] = "0";
                    String line2 = String.join("", characters);
                    bw.write(line2);
                    bw.newLine();
                    add(line2);
                } else if (characters[i].equalsIgnoreCase("e")){
                    characters[i] = "3";
                    String line2 = String.join("", characters);
                    bw.write(line2);
                    bw.newLine();
                    add(line2);
                } else if(characters[i].equalsIgnoreCase("i")){
                    characters[i] = "1";
                    String line2 = String.join("", characters);
                    bw.write(line2);
                    bw.newLine();
                    add(line2);
                } else if(characters[i].equalsIgnoreCase("l")){
                    characters[i] = "1";
                    String line2 = String.join("", characters);
                    bw.write(line2);
                    bw.newLine();
                    add(line2);
                } else if(characters[i].equalsIgnoreCase("s")){
                    characters[i] = "$";
                    String line2 = String.join("", characters);
                    bw.write(line2);
                    bw.newLine();
                    add(line2);
                }
                    
            }
            String[] characters2 = line.split("");
            for(int i = characters2.length-1; i>=0; i--){
                if(characters2[i].equalsIgnoreCase("t")){
                    characters2[i] = "7";
                    String line2 = String.join("", characters2);
                    if(search(line2)!=1){
                        bw.write(line2);
                        bw.newLine();
                        add(line2);
                    }    
                } else if(characters2[i].equalsIgnoreCase("a")){
                    characters2[i] = "4";
                    String line2 = String.join("", characters2);
                    if(search(line2)!=1){
                        bw.write(line2);
                        bw.newLine();
                        add(line2);
                    }                   
                } else if(characters2[i].equalsIgnoreCase("o")){
                    characters2[i] = "0";
                    String line2 = String.join("", characters2);
                    if(search(line2)!=1){
                        bw.write(line2);
                        bw.newLine();
                        add(line2);
                    }
                } else if (characters2[i].equalsIgnoreCase("e")){
                    characters2[i] = "3";
                    String line2 = String.join("", characters2);
                    if(search(line2)!=1){
                        bw.write(line2);
                        bw.newLine();
                        add(line2);
                    }
                } else if(characters2[i].equalsIgnoreCase("i")){
                    characters2[i] = "1";
                    String line2 = String.join("", characters2);
                    if(search(line2)!=1){
                        bw.write(line2);
                        bw.newLine();
                        add(line2);
                    }
                } else if(characters2[i].equalsIgnoreCase("l")){
                    characters2[i] = "1";
                    String line2 = String.join("", characters2);
                    if(search(line2)!=1){
                        bw.write(line2);
                        bw.newLine();
                        add(line2);
                    }
                }else if(characters2[i].equalsIgnoreCase("s")){
                    characters2[i] = "$";
                    String line2 = String.join("", characters2);
                    if(search(line2)!=1){
                        bw.write(line2);
                        bw.newLine();
                        add(line2);
                    }
                }                    
            }            
        }
        bw.close();        
    }
}