/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cryptage;



/**
 *
 * @author User
 */

public abstract class Cryptage {
	final static String ALPHABET ="abcdefghijklmnopqrstuvwxyz0123456789";
	protected String alphabetDeCryptage;
	protected String clef;
	
	

	public Cryptage() {
		this("default");
	}
	
	public Cryptage(String clef) {
            
			for(int i = 0; i < clef.length();i ++)
				if (ALPHABET.indexOf(clef.charAt(i)) == -1)
					throw new IllegalArgumentException("clef incorrecte, tous ses caractères doivent être dans l'alphabet.");
                                        
			this.clef=clef;
			String s = clef + ALPHABET;
			for(int i=0; s.length() > 36; i++ ) {
				char c = s.charAt(i);
				for(int j = s.indexOf(c, i+1); j != -1; j = s.indexOf(c, i+1))
					s = s.substring(0,j) + s.substring(j + 1);	
			}
			alphabetDeCryptage = s; // avec la rotation
	}
	
	public String toString() {
		
		return "Cryptage" + this.getClass().getSimpleName()
				+"\nMot clef : " + clef;
	}
	
	public abstract String cryptage(String s);

	public abstract String deCryptage(String s);
	
	public static void main(String [] args) {
		
//		String crypted="";
//		
//		String clef1 = "decalage";
//		
//		for(int i = 0; i < clef1.length();i ++)
//			if (ALPHABET.indexOf(clef1.charAt(i)) == -1)
//				throw new IllegalArgumentException("clef incorrecte, tous ses caractères doivent être dans l'alphabet.");
////		this.clef=clef;
//		String s = clef1 + ALPHABET;
//		for(int i=0; s.length() > 36; i++ ) {
//			char c = s.charAt(i);
//			for(int j = s.indexOf(c, i+1); j != -1; j = s.indexOf(c, i+1))
//				s = s.substring(0,j) + s.substring(j + 1);	
//		}
//		crypted = s;
//		
//		System.out.println(crypted);
		
		

}
}


			


