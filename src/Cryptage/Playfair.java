/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cryptage;

/**
 *
 * @author User
 */
public class Playfair extends Cryptage {

	
	private char [][] matriceDeCryptage;
	//besoin de créer un nouvel attibut
	
	public Playfair() {
		this("default");
	}
	public Playfair(String clef) {
		super(clef);
		// clef et alphabetDeCryptage sont instanciés par super 
		
		matriceDeCryptage = new char[6][6];
		// on instancie matriceDeCryptage
		int k=0;
		for(int i=0; i < this.matriceDeCryptage.length; i++) {
			for(int j=0; j<this.matriceDeCryptage[i].length;j++,k++) {
				matriceDeCryptage[i][j]=this.alphabetDeCryptage.charAt(k);
			}			
		}
	}
	
	public String toString() {
	    StringBuilder result = new StringBuilder(super.toString() +
	            "\nTransformation de cryptage : " + ALPHABET +
	            "\nAlphabet de cryptage       : " + alphabetDeCryptage +
	            "\nMatrice de cryptage        : \n");

	    for (int i = 0; i < this.matriceDeCryptage.length; i++) {
	        for (int j = 0; j < this.matriceDeCryptage[i].length; j++) {
	            result.append(matriceDeCryptage[i][j]).append(" ");
	        }
	        result.append("\n");
	    }

	    return result.toString();
	}


	@Override
	public String cryptage(String s) {
		try {
		    for (int i = 0; i < s.length(); i++) {
    char currentChar = s.charAt(i);
    
    // Ignorer les espaces
    if (currentChar == ' ') {
        continue; // Passe à l'itération suivante sans exécuter le reste du code dans la boucle
    }

    // La méthode indexOf renvoie -1 lorsque l'élément recherché n'est pas trouvé dans la chaîne
    if (ALPHABET.indexOf(currentChar) == -1) {
        throw new IllegalArgumentException("Message incorrect, tous les caractères doivent être dans l'alphabet.");
    }
}


		StringBuilder messageSansEspaces = new StringBuilder(s);

	    // Supprimer les espaces en parcourant la chaîne en sens inverse
	    for (int i = messageSansEspaces.length() - 1; i >= 0; i--) {
	        if (messageSansEspaces.charAt(i) == ' ' ) {
	            messageSansEspaces.deleteCharAt(i);
	        }
	    }

	    
	    /////////////////////////////////////// Code pas beau mais fonctionnel, ici on cherche a juste supprimer les espaces inutiles avant et après le s 
	    
	    StringBuilder messageSansEspacesAvantEtApres = new StringBuilder(s);
	    
	    for (int i = messageSansEspacesAvantEtApres.length() - 1; i >= 0; i--) {
	    	if(i==0) {
	    		break;
	    	}
	        if (messageSansEspacesAvantEtApres.charAt(i) == ' ' && messageSansEspacesAvantEtApres.charAt(i-1)==' ') {
	        	messageSansEspacesAvantEtApres.deleteCharAt(i);
	        }
	    } 
//	    messageSansEspacesAvantEtApres.deleteCharAt(0);
	    
	    /////////////////////////////////////// on aurait pu travailler avec String   OU PAS PARCE QUE STRIGN EST IMMUABLE ET DONC CA DEVIENT BAVEUT. VIVE STRINGBUFFER
	    
	    s = messageSansEspaces.toString();
	  

//	    System.out.println(s);
	    
	   // System.out.println(messageSansEspacesAvantEtApres);

	    char tab2[] = new char[s.length()];
	    for (int i = 0; i < s.length(); i++) {
	        tab2[i] = s.charAt(i);
	    }

	    int a = 0;
	    int b = 1;
	    char ec1;
	    char ec2;
	    StringBuilder traduit = new StringBuilder();

	    // Parcourir la chaîne par paires de caractères
	    for (; b < s.length(); a += 2, b += 2) {
	        while (a < s.length() && tab2[a] == ' ') {
	            a += 2;
	        }
	        while (b < s.length() && tab2[b] == ' ') {
	            b += 2;
	        }

	        if (a < s.length() && b < s.length()) {
	            ec1 = tab2[a];
	            ec2 = tab2[b];

	           // System.out.print(ec1 + "" + ec2 + " ");

                      //if (ec1 == ec2 || ec2 == ' ') {
	               // traduit.append(ec1);
	               // traduit.append(ec2);
                        
	            //}
	            
	            

	            int x = 0;
	            int y = 0;
	            int u = 0;
	            int v = 0;

	            // Trouver les indices des caractères dans le tableau
	            for (int i = 0; i < this.matriceDeCryptage.length; ++i) {
	                for (int j = 0; j < matriceDeCryptage[i].length; ++j) {
	                    if (matriceDeCryptage[i][j] == ec1) {
	                        x = j;
	                        y = i;
	                    }
	                    if (matriceDeCryptage[i][j] == ec2) {
	                        u = j;
	                        v = i;
	                    }
	                }
	            }
	            
	           // System.out.println(
	            //		ec1 + ":" + "x : " + x +" "+ "y : " + y 
	            //		+
	            //		"\n" 
	            //		+
	            //		ec2 +":" + "u : " + u + " v : " +  v);
if( y == v && x == u){
    traduit.append(matriceDeCryptage[y][(x + 1) % 6]);
    traduit.append(matriceDeCryptage[v][(u + 1) % 6]);
}
	            
	        if (y == v && x!=u) {
                    // on met cette condition pour éviter le cas de la double lettre "mm" qui entrainera l'execution de plusieurs if
    traduit.append(matriceDeCryptage[y][(x + 1) % 6]);
    traduit.append(matriceDeCryptage[v][(u + 1) % 6]);
}

if (x != u && y != v) {
    traduit.append(matriceDeCryptage[y][u]);
    traduit.append(matriceDeCryptage[v][x]);
}

if (x == u && y!=v) {
    traduit.append(matriceDeCryptage[(y+1)%6][x]);
    traduit.append(matriceDeCryptage[(v+1)%6][u]);
}


	        }
	    }
	    		if(s.length()%2!=0) {
	    			traduit.append(s.charAt(s.length()-1));
	    		}
	   
//	    System.out.println("\nmessage à crypter :");
//	    System.out.println(messageSansEspacesAvantEtApres);
	    
	    // on va maintenant remettre les espaces dans traduit en comptant ceux dans  messageSansEspacesAvantEtApres
	    
	    for(int i=0; i<messageSansEspacesAvantEtApres.length();i++) {
	    	if(messageSansEspacesAvantEtApres.charAt(i)==' ') {
	    		traduit.insert(i,' ');
	    	}
	    }
	    //////////////////////////////////////////////////////////////////////////////////////////////
	    
	 
	
//	    System.out.println("Traduit : \n" + traduit.toString());
	    
	    ///////////////////////////////////////////////////
	    //toString marche pas sur traduit car elle sert à autre chose, donc on brut force.
	    String cryptageFin="";
	    for(int i = 0; i <traduit.length();i++) {
	    	cryptageFin+=traduit.charAt(i);
	    }
	    
	    ///////////////////////////////////////////////////
	    
	    return cryptageFin;
	    } catch (IllegalArgumentException e) {
	        // Affichez le message d'erreur
	        System.out.println(e.getMessage());
	        return "";  // Ou une valeur par défaut en cas d'erreur
	    }
	
	    
	}

	public String deCryptage(String s) {
		StringBuilder messageSansEspaces = new StringBuilder(s);

	    // Supprimer les espaces en parcourant la chaîne en sens inverse car on reduit dynamiquement la taille de la chaine
	    for (int i = messageSansEspaces.length() - 1; i >= 0; i--) {
	        if (messageSansEspaces.charAt(i) == ' ' ) {
	            messageSansEspaces.deleteCharAt(i);
	        }
	    }
	    
	    

	    
	    /////////////////////////////////////// Code pas beau mais fonctionnel, ici on cherche a juste supprimer les espaces inutiles avant et après le s 
	    /////////////////////////////////////// afin de reformer le message initial avec les espaces 
	    StringBuilder messageSansEspacesAvantEtApres = new StringBuilder(s);
	    
	    for (int i = messageSansEspacesAvantEtApres.length() - 1; i >= 0; i--) {
	    	if(i==0) {
	    		break;
	    	}
	        if (messageSansEspacesAvantEtApres.charAt(i) == ' ' && messageSansEspacesAvantEtApres.charAt(i-1)==' ') {
	        	messageSansEspacesAvantEtApres.deleteCharAt(i);
	        }
	    } 
//	    messageSansEspacesAvantEtApres.deleteCharAt(0);
	    
	    /////////////////////////////////////// on aurait pu travailler avec String   OU PAS PARCE QUE STRIGN EST IMMUABLE ET DONC CA DEVIENT BAVEUT. VIVE STRINGBUFFER

	    s = messageSansEspaces.toString();
	    
//	    System.out.println(s);
	    
//	    System.out.println(messageSansEspacesAvantEtApres);

	    char tab2[] = new char[s.length()];
	    for (int i = 0; i < s.length(); i++) {
	        tab2[i] = s.charAt(i);
	    }

	    int a = 0;
	    int b = 1;
	    char ec1;
	    char ec2;
	    StringBuilder traduit = new StringBuilder();

	    // Parcourir la chaîne par paires de caractères
	    for (; b < s.length(); a += 2, b += 2) {
	        while (a < s.length() && tab2[a] == ' ') {
	            a += 2;
	        }
	        while (b < s.length() && tab2[b] == ' ') {
	            b += 2;
	        }

	        if (a < s.length() && b < s.length()) {
	            ec1 = tab2[a];
	            ec2 = tab2[b];

//	            System.out.print(ec1 + "" + ec2 + " ");

	            //if (ec1 == ec2 || ec2 == ' ') {
	             //   traduit.append(ec1);
	           //     traduit.append(ec2);
	          //  }
	            
	            

	            int x = 0;
	            int y = 0;
	            int u = 0;
	            int v = 0;

	            // Trouver les indices des caractères dans le tableau
	            for (int i = 0; i < this.matriceDeCryptage.length; ++i) {
	                for (int j = 0; j < matriceDeCryptage[i].length; ++j) {
	                    if (matriceDeCryptage[i][j] == ec1) {
	                        x = j;
	                        y = i;
	                    }
	                    if (matriceDeCryptage[i][j] == ec2) {
	                        u = j;
	                        v = i;
	                    }
	                }
	            }
	            
	            //System.out.println(
	            //		ec1 + ":" + "x : " + x +" "+ "y : " + y 
	            //		+
	            //		"\n" 
	            //		+
	            //		ec2 +":" + "u : " + u + " v : " +  v);

	          // ATTENTION LES INDICES SONT INVERSEES POUR LE DECRYPTAGE, X Y U V : RIEN A VOIR AVEC ceux de la méthode CRYPTAGE  
                  
                  
                  
                  
                  
                  if( y == v && x == u){
                     // System.out.println("+1");
    traduit.append(matriceDeCryptage[y][(x - 1 +6) % 6]);
    traduit.append(matriceDeCryptage[v][(u - 1 +6) % 6]);
}
	       if (x!=u && y == v  ) {
                  // System.out.println("+2");
                traduit.append(matriceDeCryptage[y][(x - 1 + 6) % 6]);
                traduit.append(matriceDeCryptage[v][(u - 1 + 6) % 6]);
}
               if (x != u && y != v) {
    //System.out.println("+4");
    traduit.append(matriceDeCryptage[y][u]);
    traduit.append(matriceDeCryptage[v][x]);
}

if (y!=v && x == u  ) {
    // System.out.println("+3");
    traduit.append(matriceDeCryptage[(y-1+6)%6][x]);
    traduit.append(matriceDeCryptage[(v-1+6)%6][u]);
}






	        }
	    }
	    
	    // si la chaine est de longueur impair, on rajoute de le dernier caractère
	    		if(s.length()%2!=0) {
	    			traduit.append(s.charAt(s.length()-1));
	    		}
	   
//	    System.out.println("\nmessage à crypter :");
//	    System.out.println(messageSansEspacesAvantEtApres);
	    
	    // on va maintenant remettre les espaces dans traduit en comptant ceux dans  messageSansEspacesAvantEtApres
	    
	    for(int i=0; i<messageSansEspacesAvantEtApres.length();i++) {
	    	if(messageSansEspacesAvantEtApres.charAt(i)==' ') {
	    		traduit.insert(i,' ');
	    	}
	    }
	    //////////////////////////////////////////////////////////////////////////////////////////////
	    
	 
	
//	    System.out.println("Traduit : \n" + traduit.toString());
	    
	    ///////////////////////////////////////////////////
	    //toString marche pas sur traduit car elle sert à autre chose, donc on brut force.
	    String deCryptageFin="";
	    for(int i = 0; i <traduit.length();i++) {
	    	deCryptageFin+=traduit.charAt(i);
	    }
	    ///////////////////////////////////////////////////
//	    System.out.println(s);
	    return deCryptageFin;
	}
	
	

	public static void main(String[] args) {
		
		Playfair d = new Playfair("playfair");
		System.out.println(d);
                System.out.println("message de base   : le langage java :");
                System.out.println(" ");
                System.out.println("on crypte         : " + d.cryptage("le langage java"));
                System.out.println("on decrypte       : " +d.deCryptage("fb ayoeicb nfta"));
                System.out.println(" ");
                System.out.println("message de base   : programmation :");
                System.out.println(" ");
		System.out.println("on crypte         : " +d.cryptage( "programmation"));
		System.out.println("on decrypte       : " +d.deCryptage("rhwocpnnc0gwn"));
                System.out.println(" ");
                System.out.println("message de base   : je taime :");
                System.out.println(" ");
		System.out.println("on crypte         : " +d.cryptage( "je taime rohini"));
		System.out.println("on decrypte       : " +d.deCryptage("nb 0cyog bhjfoi"));
//		System.out.println(d.deCryptage("hq")); // pour tester même colonne
		
		
//	    Playfair d = new Playfair("playfair");
//	    char[][] tab = new char[][] {
//	            {'p', 'l', 'a', 'y', 'f', 'i'},
//	            {'r', 'b', 'c', 'd', 'e', 'g'},
//	            {'h', 'j', 'k', 'm', 'n', 'o'},
//	            {'q', 's', 't', 'u', 'v', 'w'},
//	            {'x', 'z', '0', '1', '2', '3'},
//	            {'4', '5', '6', '7', '8', '9'}
//	    };
//
//	    String s = "              le langage java            ";
//	    
//	    
//	    StringBuilder messageSansEspaces = new StringBuilder(s);
//
//	    // Supprimer les espaces en parcourant la chaîne en sens inverse
//	    for (int i = messageSansEspaces.length() - 1; i >= 0; i--) {
//	        if (messageSansEspaces.charAt(i) == ' ' ) {
//	            messageSansEspaces.deleteCharAt(i);
//	        }
//	    }
//
//	    
//	    /////////////////////////////////////// Code pas beau mais fonctionnel, ici on cherche a juste supprimer les espaces inutiles avant et après le s 
//	    
//	    StringBuilder messageSansEspacesAvantEtApres = new StringBuilder(s);
//	    
//	    for (int i = messageSansEspacesAvantEtApres.length() - 1; i >= 0; i--) {
//	    	if(i==0) {
//	    		break;
//	    	}
//	        if (messageSansEspacesAvantEtApres.charAt(i) == ' ' && messageSansEspacesAvantEtApres.charAt(i-1)==' ') {
//	        	messageSansEspacesAvantEtApres.deleteCharAt(i);
//	        }
//	    } 
//	    messageSansEspacesAvantEtApres.deleteCharAt(0);
//	    
//	    /////////////////////////////////////// on aurait pu travailler avec String   OU PAS PARCE QUE STRIGN EST IMMUABLE ET DONC CA DEVIENT BAVEUT. VIVE STRINGBUFFER
//	    
//	    s = messageSansEspaces.toString();
//	  
//
//	    System.out.println(s);
//	    
////	    System.out.println(messageSansEspacesAvantEtApres);
//	    
//	    
//
//	    char tab2[] = new char[s.length()];
//	    for (int i = 0; i < s.length(); i++) {
//	        tab2[i] = s.charAt(i);
//	    }
//
//	    int a = 0;
//	    int b = 1;
//	    char ec1;
//	    char ec2;
//	    StringBuilder traduit = new StringBuilder();
//
//	    // Parcourir la chaîne par paires de caractères
//	    for (; b < s.length(); a += 2, b += 2) {
//	        while (a < s.length() && tab2[a] == ' ') {
//	            a += 2;
//	        }
//	        while (b < s.length() && tab2[b] == ' ') {
//	            b += 2;
//	        }
//
//	        if (a < s.length() && b < s.length()) {
//	            ec1 = tab2[a];
//	            ec2 = tab2[b];
//
////	            System.out.print(ec1 + "" + ec2 + " ");
//
//	            if (ec1 == ec2 || ec2 == ' ') {
//	                traduit.append(ec1);
//	                traduit.append(ec2);
//	            }
//	            
//	            
//
//	            int x = 0;
//	            int y = 0;
//	            int u = 0;
//	            int v = 0;
//
//	            // Trouver les indices des caractères dans le tableau
//	            for (int i = 0; i < tab.length; ++i) {
//	                for (int j = 0; j < tab[i].length; ++j) {
//	                    if (tab[i][j] == ec1) {
//	                        x = j;
//	                        y = i;
//	                    }
//	                    if (tab[i][j] == ec2) {
//	                        u = j;
//	                        v = i;
//	                    }
//	                }
//	            }
//	            
//	            System.out.println(
//	            		ec1 + ":" + "x : " + x +" "+ "y : " + y 
//	            		+
//	            		"\n" 
//	            		+
//	            		ec2 +":" + "u : " + u + " v : " +  v);
//
//
//	            // Ajouter les caractères traduits
//	            
//	         // Ajouter les caractères traduits
//	            if (y == v) {
//	                System.out.println("y==v");
//	                traduit.append(tab[y][(x + 1) % 6]);  // Remplacer x + 1 par (x + 2) % 6 pour prendre les deux caractères suivants
//	                traduit.append(tab[v][(u + 1) % 6]);  // Remplacer u + 1 par (u + 2) % 6 pour prendre les deux caractères suivants
//	            }
//
//	            if (x != u && y != v) {
//	                System.out.println("x!=u && y!=v");
//	                traduit.append(tab[y][u]);
//	                traduit.append(tab[v][x]);
//	            }
//
//	         // Ajouter les caractères traduits
//	            if (x == u) {
//	                traduit.append(tab[x][(y + 1) % 6]);  // Remplacer y + 1 par (y + 2) % 6 pour prendre les deux caractères suivants
//	                traduit.append(tab[u][(v + 1) % 6]);  // Remplacer v + 1 par (v + 2) % 6 pour prendre les deux caractères suivants
//	            }
//	            
//	            
//
//
//	        }
//	    }
//	    		if(s.length()%2!=0) {
//	    			traduit.append(s.charAt(s.length()-1));
//	    		}
//	   
//	    System.out.println("\nmessage à crypter :");
//	    System.out.println(messageSansEspacesAvantEtApres);
//	    
//	    // on va maintenant remettre les espaces dans traduit en comptant ceux dans  messageSansEspacesAvantEtApres
//	    
//	    for(int i=0; i<messageSansEspacesAvantEtApres.length();i++) {
//	    	if(messageSansEspacesAvantEtApres.charAt(i)==' ') {
//	    		traduit.insert(i,' ');
//	    	}
//	    }
//	    //////////////////////////////////////////////////////////////////////////////////////////////
//	    
//	 
//	
//	    System.out.println("Traduit : \n" + traduit.toString());
//	    
//	    traduit.toString();
    
//	}
//
//	}

////		for(int a=0, b=1;b<tab2.length;a++,b++) {
////			
////			ec1=tab2[a];
////			ec2=tab2[b];
////			
////
////			if(ec1==' ') {
////				a++;
////				b++;
////			}
////			
////			System.out.println(ec1 +" "+ec2);
////		}
//			
////		for(int i=0; i<tab.length;i++) {
////			for(int j=0; j<tab[i].length;i++) {
////		
////			}
////			
////		}

	}
}
	
