package Serveur;

import BDD.Bdd;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server {

    private static List<String> connectedClientsEmails = new ArrayList<>();
    private static List<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Serveur en marche...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion : " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {

        private UUID clientId;
        private String clientEmail;
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private InputStream inputStream;

        public ClientHandler(Socket socket) {
            this.clientId = UUID.randomUUID();
            this.clientSocket = socket;
            try {
                this.out = new PrintWriter(clientSocket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.out.println(clientId.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String inputLine;
                

                while ((inputLine = in.readLine()) != null) {
                    String[] parts = inputLine.split(";");
                    String command = parts[0];

                    if (command.equals("EMAILCLIENT")) {
                        clientEmail = parts[1];
                        connectedClientsEmails.add(clientEmail);
                        System.out.println("EMAIL CLIENT : " + clientEmail);
                        System.out.println("Tous les clients connectés:");
                        for (String email : connectedClientsEmails) {
                            System.out.println("Client email: " + email);
                        }
                    }

                    if (command.equals("EMAILCONTACT")) {
                        String emailContact = parts[1];
                        System.out.println("EMailContact : " + emailContact);
                        notifierDestinataire(emailContact);
                    }

                    if (command.equals("ENVOYER_MESSAGE")) {
                        int idConversation = Integer.parseInt(parts[1]);
                        int idUtilisateur = Integer.parseInt(parts[2]);
                        String message = parts[3];

                        Bdd bdd = Bdd.getInstance();
                        bdd.enregistrerMessage(idConversation, idUtilisateur, message);

                        out.println("Message enregistre avec succes LA!");
                        out.println("Notification");
                    }

                    if (command.equals("EMAILSDUGROUPE")) {
                        String[] section = inputLine.split(";");
                        String[] emailsDuGroupe = section[1].split("~");
                        notifierDestinatairesGroupe(emailsDuGroupe);
                    }

                    if (command.equals("ENVOYER_IMAGE")) {
                        System.out.println("etape 1");
                        int idConversation = Integer.parseInt(parts[1]);
                        int idUtilisateur = Integer.parseInt(parts[2]);
                        String message = parts[3];
                        String imageNom = parts[4];
                        String emailContactCourant= parts[5];
                        System.out.println("etape 2");

                        Bdd bdd = Bdd.getInstance();
                        bdd.enregistrerMessageAvecImage(idConversation, idUtilisateur, message, imageNom);
                        System.out.println("etape 3");
                        
                        notifierDestinataireImage(emailContactCourant,imageNom);
                        
                        out.println("Message IMAGE enregistre avec succes LA!");
                        out.println("Notification");
            
                    }

                }

                clientHandlers.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void notifierDestinataire(String destinataireEmail) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientEmail.equals(destinataireEmail)) {
                clientHandler.out.println("NOUVEAU MESSAGE");
            }
        }
    }
    
    public static void notifierDestinataireImage(String destinataireEmail, String image) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientEmail.equals(destinataireEmail)) {
                clientHandler.out.println("NOUVELLE IMAGE;"+image);
            }
        }
    }

    public static void notifierDestinatairesGroupe(String[] emailsDuGroupe) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (containsEmail(clientHandler.clientEmail, emailsDuGroupe)) {
                clientHandler.out.println("NOUVEAU MESSAGE");
            }
        }
    }

    private static boolean containsEmail(String email, String[] emails) {
        for (String e : emails) {
            if (e.equals(email)) {
                return true;
            }
        }
        return false;
    }
    
   
}
