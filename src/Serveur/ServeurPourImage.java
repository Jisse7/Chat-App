

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServeurPourImage {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            
            System.out.println("Serveur en marche...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Nouvelle connexion : " + socket);

                // Créer un thread pour gérer la connexion avec le client
                Thread clientThread = new Thread(new ClientHandler(socket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classe pour gérer les interactions avec chaque client
static class ClientHandler implements Runnable {
    private Socket clientSocket;
   

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
       
    }


public void run() {
    try {
        // Lire le nom unique de l'image
        InputStream inputStream = clientSocket.getInputStream();
        ByteArrayOutputStream nameBuffer = new ByteArrayOutputStream();
        int currentByte;
        while ((currentByte = inputStream.read()) != -1) {
            if (currentByte == ';') {
                break;
            }
            nameBuffer.write(currentByte);
        }
        String uniqueName = new String(nameBuffer.toByteArray());
       

        // Lire les données de l'image
        ByteArrayOutputStream imageDataBuffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            imageDataBuffer.write(data, 0, nRead);
        }

        // Enregistrer les données de l'image avec le nom unique
        byte[] imageData = imageDataBuffer.toByteArray();
        String imagePath = "C:\\Users\\User\\Documents\\NetBeansProjects\\MessagerieV2\\src\\ressourcesServer\\" + uniqueName + ".jpg";
        FileOutputStream outputStream = new FileOutputStream(imagePath);
        outputStream.write(imageData);
//        outputStream.close();


 if(!uniqueName.equals("")){
            envoyerImageAuClient(uniqueName);
        }
        System.out.println("Nom de l'image reçu : " + uniqueName);
        System.out.println("LIGNE65");
        System.out.println("Image enregistrée avec succès : " + imagePath);

        // Fermer les flux et la connexion
//        inputStream.close();
//        clientSocket.close();

    } catch (IOException e) {
        e.printStackTrace();
    }

    // Lire les messages des utilisateurs dans un thread séparé
    Thread messageReaderThread = new Thread(() -> {
        try {
            // Utiliser un BufferedReader pour lire les messages du client B
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
              String nomImage=inputLine;
               
                    // Récupérer le nom de l'image demandée par le client B
                 
                    System.out.println("Demande du client B pour l'image : " + nomImage);

                    // Envoyer l'image demandée au client B
                    envoyerImageAuClient(nomImage);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    messageReaderThread.start();
}

  

private void envoyerImageAuClient(String nomImage) {
    try {
        System.out.println("demarre envoyerImageCLient : " + nomImage);
        // Charger l'image depuis le chemin spécifié
        byte[] imageData = Files.readAllBytes(Paths.get("C:\\Users\\User\\Documents\\NetBeansProjects\\MessagerieV2\\src\\ressourcesServer\\" + nomImage + ".jpg"));

        // Envoyer la taille de l'image au client B
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        writer.println(imageData.length);
        writer.flush();

        // Envoyer l'image au client B
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(imageData);
        outputStream.flush();

        System.out.println("Image envoyée au client B avec succès.");
    } catch (IOException e) {
        e.printStackTrace();
    }
}


        private void envoyerImageAuClient(String nomImage, PrintWriter writer, Socket clientSocket) {
            try {
                // Charger l'image depuis le chemin spécifié
                byte[] imageData = Files.readAllBytes(Paths.get("C:\\Users\\User\\Documents\\NetBeansProjects\\MessagerieV2\\src\\ressourcesServer\\" + nomImage + ".jpg"));

                // Envoyer la taille de l'image au client
                writer.println(imageData.length);
                writer.flush();

                // Envoyer l'image au client
                clientSocket.getOutputStream().write(imageData);
                clientSocket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
   private static void envoyerImageAuClient2(String nomImage, PrintWriter writer, Socket clientSocket) throws IOException {
       File imageFile = new File("php.jpg");
        byte[] imageData = Files.readAllBytes(imageFile.toPath());
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(imageData);
//        outputStream.flush();
//        outputStream.close();
//
//        clientSocket.close();
      
}
}
