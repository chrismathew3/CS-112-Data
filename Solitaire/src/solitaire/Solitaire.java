package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		// COMPLETE THIS METHOD
		if(deckRear.cardValue == 27){
			deckRear.cardValue = deckRear.next.cardValue;
			deckRear.next.cardValue = 27;
			printList(deckRear);
			return;
		}
		if(deckRear.next.cardValue == 27){
			deckRear.next.cardValue = deckRear.next.next.cardValue;
			deckRear.next.next.cardValue = 27;
			printList(deckRear);
			return;
		}
		for(CardNode temp = deckRear.next; temp.next != deckRear.next; temp = temp.next){
			if(temp.next.cardValue == 27){
				temp.next.cardValue = temp.next.next.cardValue;
				temp.next.next.cardValue = 27;
				printList(deckRear);
				break;
			}
		}
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	    // COMPLETE THIS METHOD
		if(deckRear.cardValue == 28){
			deckRear.cardValue = deckRear.next.cardValue;
			deckRear.next.cardValue = deckRear.next.next.cardValue;
			deckRear.next.next.cardValue = 28;
			printList(deckRear);
			return;
			
		}
		if(deckRear.next.cardValue == 28){
			CardNode holder = new CardNode();
			holder.cardValue = deckRear.next.next.cardValue;
			holder.next = deckRear.next;
			deckRear.next.cardValue = holder.cardValue;
			deckRear.next.next.cardValue = deckRear.next.next.next.cardValue;
			deckRear.next.next.next.cardValue = 28;
			printList(deckRear);
			return;
			
			
		}
		for(CardNode temp = deckRear.next; temp.next != deckRear.next; temp = temp.next){
			if(temp.next.cardValue == 28){
				temp.next.cardValue = temp.next.next.cardValue;
				temp.next.next.cardValue = 28;
				temp.next.next.cardValue = temp.next.next.next.cardValue;
				temp.next.next.next.cardValue = 28;
				printList(deckRear);
				break;
			}
		}
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// COMPLETE THIS METHOD
		int count = 0;
		//System.out.println(count);
		for(CardNode temp = deckRear; temp.next != deckRear; temp = temp.next){
			
			if(temp.next.cardValue == 27 || temp.next.cardValue == 28){
				count++;
				
				if(count == 2){
					//check if joker is at the front and the tail if so do nothing
					if(deckRear.next.cardValue == 27 || deckRear.next.cardValue == 28){
						if(deckRear.cardValue == 27 || deckRear.cardValue == 28){
							return;
						}else{
							deckRear = temp.next;
							printList(deckRear);
							System.out.println("hi");
							
						}
						
					}
					//case where a joker is at the end but there are still cards before the other joker
					if(deckRear.cardValue == 27 || deckRear.cardValue == 28){
						System.out.println("weed");
						CardNode J1 = new CardNode();
						J1 = deckRear.next;
						do{
							J1 = J1.next;
						}while(J1.next.next.cardValue < 27);
						deckRear = J1.next;
						System.out.println("well");
					}
					//case where the joker is neither at the front nor the end
					CardNode pc = new CardNode();
					pc = deckRear;
					while(pc.next.next.cardValue < 27){
						pc = pc.next;
					}
					CardNode newHead = new CardNode();
					newHead.next = temp.next.next;
					CardNode tail = new CardNode();
					tail.next = deckRear;
					CardNode head = new CardNode();
					head.next = deckRear.next;
					deckRear = pc.next;
					temp.next.next = head.next;
					tail.next.next = pc.next.next;
					pc.next.next = newHead.next;
					printList(deckRear);
					return;
					}
			
			}	
		}
	}
	
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		// COMPLETE THIS METHOD
		//1.Store value of last card into cap variable
		int cap;
		if(deckRear.cardValue > 26){
			printList(deckRear);
			return;
		}else{
			cap = deckRear.cardValue;
		}
		//2. Create a new node to count down from the beginning until cap = count
		//3. Do all the next swaps 
		int count = 1;
		

		CardNode head = new CardNode();
		head.next = deckRear.next;
		
		CardNode sTL = new CardNode();
		sTL.next = deckRear.next;
		
		CardNode tail = new CardNode();
		tail.next = deckRear;
		
		while(sTL.next.next != deckRear){
			sTL = sTL.next;
		}
		CardNode curr = new CardNode();
		curr.next = deckRear.next;
		while(count != cap){
			
			count++;
			curr = curr.next;
		}
	
		tail.next.next = curr.next.next;
		curr.next.next = tail.next;
		sTL.next.next = head.next;
		deckRear = tail.next;
	}
		
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		int cap;
		int count = 1;
		do{
			jokerA();
			jokerB();
			tripleCut();
			countCut();
		}while(deckRear.next.cardValue > 26);
		cap = deckRear.next.cardValue;
		CardNode curr = new CardNode();
		
		curr.next = deckRear.next;
		printList(deckRear);
		
		while(count != cap){
			count++;
			curr = curr.next;
		}
		
		return curr.next.next.cardValue;
		
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		// COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		String encryption; 
		encryption = message.toUpperCase();
		encryption = encryption.trim();
		encryption = encryption.replaceAll("[^a-zA-Z]", "");
		char[] ch = encryption.toCharArray();
		int[] holder = new int[28];
		int key;
		for(int i = 0; i < ch.length;i++){
			holder[i] = ch[i] - 'A' +1;
			key = getKey();
			System.out.println(key);
			printList(deckRear);
			holder[i] = holder[i] + key;
			System.out.println(holder[i]);
			if(holder[i] > 26){
				holder[i] = holder[i] - 26;
			}
			ch[i] = (char) (holder[i] + 'A');
			System.out.println(ch[i]);
			
		}
		String encrypted = new String(ch);
		System.out.println(encrypted);
	    return encrypted;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		// COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		String Decryption;
		Decryption = message.toUpperCase();
		Decryption = Decryption.trim();
		Decryption = Decryption.replaceAll("[^a-zA-Z]", "");
		char[] ch = Decryption.toCharArray();
		int[] holder = new int[28];
		int key;
		for(int i = 0; i < ch.length;i++){
			holder[i] = ch[i] - 'A' +1;
			key = getKey();
			if(holder[i] <= key){
				holder[i] = holder[i] + 26;
			}
			holder[i] = holder[i] - key;
			ch[i] = (char)(holder[i] + 'A'-2);
		}
		String Decrypted = new String(ch); 
		System.out.println(Decrypted);
	    return Decrypted;
	}
}