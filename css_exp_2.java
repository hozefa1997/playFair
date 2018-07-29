import java.util.*;

/**
 *
 * @author HozefaKothari
 */
public class PlayFair {

    // Key Matrix is always 5X5
    static char[][] key = new char[5][5];
    
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the keyword: ");
        String k = sc.nextLine();
        k = k.toUpperCase();        //Capitalize the key.
        k = removeDuplicates(k);    //Removes duplicate letters.
        createMatrix(k);            //Creates key matrix.
        System.out.print("Enter the message: ");
        String msg = sc.nextLine();
        msg = msg.toUpperCase();    //Capitalize the message.
        msg = duplicateLetter(msg); //Removes duplicate letters.
        if( msg.length()%2 != 0 )   //If pair is not formed then X is
            msg += "X";             //appended at the end of message.
        String encryptedMsg = encryptMsg(msg);//Message is encrypted.
        //Encrypted message is decrypted.
        String decryptedMsg = decryptMsg(encryptedMsg);
        System.out.println("Key matrix for the keyword: ");
        displayKeyMatrix();         //Displays key matrix.
        System.out.println("Message : ");
        //Displays original message.
        for(int i=0 ; i<msg.length()-1 ; i+=2 )
            System.out.print(msg.substring(i,i+2)+"\t");
        System.out.println();
        System.out.println("Encrypted Message: ");
        //Encrypted message is displayed.
        for(int i=0 ; i<encryptedMsg.length()-1 ; i+=2 )
            System.out.print(encryptedMsg.substring(i,i+2)+"\t");
        System.out.println();
        //Decrypted message is displayed.
        System.out.println("Decrypted Message: " + decryptedMsg);
    }
    
    public static String removeDuplicates(String k) {
        String result="";
        for(int i=0 ; i<k.length() ; i++ ) {
            if( k.charAt(i) == ' ' ) //Removes blank spaces.
                continue;
            if( result.indexOf(k.charAt(i)) == -1 ) 
                result += k.charAt(i);
        }
        return result;
    }
    
    public static void createMatrix(String k) {
        int m=0,index,n=0;
        // J is not included for key matrix.
        // variable chars is used for key matrix generation.
        String chars = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        String tmp;
        for(int i=0 ; i< 5 ; i++ ) {
            for(int j=0 ; j<5 ; j++ ) {
                if ( m < k.length() ) { //Key is stored in the matrix first.
                    key[i][j] = k.charAt(m);
                    index = chars.indexOf(key[i][j]);
                    tmp = chars.substring( index+1,chars.length() );
                    chars = chars.substring(0, index) + tmp;
                    m++;
                }
                else {      //Then remaining alphabets are stored.
                    key[i][j] = chars.charAt(n);
                    n++;
                }
            }
        }
    }
    
    public static void displayKeyMatrix() {
        for(int i=0 ; i<5 ; i++ ) {
            for(int j=0 ; j<5 ; j++ )
                System.out.print(key[i][j]+"\t");
            System.out.println();
        }
    }
    
    //This method inserts X in between 2 duplicate letters.
    public static String duplicateLetter(String msg) {
        String tmp;
        for(int i=0 ; i<msg.length()-1 ; i++ ) {
            if( msg.charAt(i) == ' ' ) {
                tmp = msg.substring( i+1,msg.length() );
                msg = msg.substring(0, i)  + tmp;
            }
            else if( msg.charAt(i) == msg.charAt(i+1) ) {
                tmp = msg.substring( i+1,msg.length() );
                msg = msg.substring(0, i+1) + "X" + tmp;
            }
        }
        return msg;
    }
    
    public static String encryptMsg(String msg) {
        int[] a = new int[2];
        int[] b = new int[2];
        String encrypt="";
        for(int i=0; i<msg.length() ; i+=2 ) {
            a = indexOfChar(msg.charAt(i));
            b = indexOfChar(msg.charAt(i+1));
            //If pair of alphabets are in same column then move each letter
            //down by 1 & wrap it around if it is at end.
            if( a[1] == b[1] ) {
                encrypt += key[(a[0]+1)%5][a[1]];
                encrypt += key[(b[0]+1)%5][b[1]];
            }
            //If pair of alphabets are in same row then move each letter
            //right by 1 & wrap it around if it is at end.
            else if( a[0] == b[0] ) {
                encrypt += key[a[0]][(a[1]+1)%5];
                encrypt += key[b[0]][(b[1]+1)%5];
            }
            //If pair of alphabets forms rectangle then swap letters with
            //one at the end of rectangle.
            else {
                encrypt += key[a[0]][b[1]];
                encrypt += key[b[0]][a[1]];
            }
        }
        return encrypt;
    }
    //Returns the index i,j of character in key matrix.
    public static int[] indexOfChar(char c) {
        int[] a = new int[2];
        for(int i=0 ; i<5 ; i++ )
            for(int j=0 ; j<5 ; j++ )
                if( key[i][j] == c ) {
                    a[0] = i ;
                    a[1] = j ;
                    return a;
                }
        return a;
    }
    
    public static String decryptMsg(String msg) {
        int[] a = new int[2];
        int[] b = new int[2];
        String decrypt="";
        for(int i=0; i<msg.length() ; i+=2 ) {
            a = indexOfChar(msg.charAt(i));
            b = indexOfChar(msg.charAt(i+1));
            //If pair of alphabets are in same column then move each letter
            //up by 1 & wrap it around if it is at end.
            if( a[1] == b[1] ) {
                decrypt += key[(a[0]+4)%5][a[1]];
                decrypt += key[(b[0]+4)%5][b[1]];
            }
            //If pair of alphabets are in same row then move each letter
            //left by 1 & wrap it around if it is at end.
            else if( a[0] == b[0] ) {
                decrypt += key[a[0]][(a[1]+4)%5];
                decrypt += key[b[0]][(b[1]+4)%5];
            }
            //If pair of alphabets forms rectangle then swap letters with
            //one at the end of rectangle.
            else {
                decrypt += key[a[0]][b[1]];
                decrypt += key[b[0]][a[1]];
            }
        }
        int index;
        String tmp;
        int len = decrypt.length();
        //During encryption, message is divided in pair if any pair is not
        //formed then X is appended.That X is removed here. 
        if ( decrypt.charAt(len-1) == 'X' )
            decrypt = decrypt.substring(0, len-1);
        for(int i=0 ; i<decrypt.length() ; i++ ) {
            index = decrypt.indexOf("X");
            if( index < 0 ) //if there is no X in encrypted message.
                break;
            //If it has X then it checks whether the letters to its right &
            //left are same or not.If they are same then X is eliminated,
            //else X is not eliminated.
            else if( decrypt.charAt(index-1) == decrypt.charAt(index+1) ) {
                tmp = decrypt.substring( index+1,decrypt.length() );
                decrypt = decrypt.substring(0, index)  + tmp;
            }
        }
        return decrypt;
    }
}
/*Output:
Enter the keyword: play fair
Enter the message: i am hungry
Key matrix for the keyword: 
P	L	A	Y	F	
I	R	B	C	D	
E	G	H	K	M	
N	O	Q	S	T	
U	V	W	X	Z	
Message : IAMHUNGRYX
IA	MH	UN	GR	YX	
Encrypted Message: 
BP	EK	PU	OG	CY	
Decrypted Message: IAMHUNGRY

Enter the keyword: keyword
Enter the message: welcome all
Key matrix for the keyword: 
K	E	Y	W	O	
R	D	A	B	C	
F	G	H	I	L	
M	N	P	Q	S	
T	U	V	X	Z	
Message : 
WE	LC	OM	EA	LX	LX	
Encrypted Message: 
OY	SL	KS	YD	IZ	IZ	
Decrypted Message: WELCOMEALL
*/