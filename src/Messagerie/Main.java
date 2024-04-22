/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Messagerie;

import BDD.Bdd;
import GestionAmis.GestionAmis;

import Inscription.Inscription;
import Profils.Profil;
import Profils.ProfilAmis;
import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import java.awt.Font;

import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;




/**
 *
 * @author User
 */
public class Main extends javax.swing.JFrame {

    static String email;
    static int idUserCourant;
    static String conversation; // représente le destinaire, se modifie dynamiquement au clique 
    static String emailContactCourant; //l'id de la conversation, dépend du destinataire et du user courant 
    static int idConversationCourante;
    static int idContactCourant;
    static List<String> contactsUserCourant = new ArrayList<>(); //pour fenetre recherche on lui envoie la liste des contacts

    static int idGroupeCourant = 0; //pour la selection de groupe, si ==0 alors le message envoyé sera envoyé via le premier bloc sinon le else dans le bouton envoyer 
    static String emailsDuGroupe; //on envoie àa au serveur , correspond à une string contenant la liste de tous les users d'un groupe, séparé par un ~.

    static Image imageCourant; // pour l'envoi d'image
    static String nomImageCourant; // à mettre dans zoneMessage
    static byte[] imageCourantData;
    static String imageCourantPath;
    static String imageCourantNom=null;
    static int portServeurImage= 5555;

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    
   
    

    ////////////TEST///////////////
    OutputStream outputStream;

    //////////////////////////////
    /**
     * Creates new form Main
     */
    public Main(String mail) {
        email = mail;
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ////////////TEST///////////////
            outputStream = socket.getOutputStream();

            //////////////////////////////
            out.println("EMAILCLIENT;" + email);

            //ZONE DE NON DROIT
            //pour l'auto actualisation de liste de discussions car flemme
            Thread thread = new Thread(() -> {
                while (true) {
                    // Appeler la méthode ici
                    afficherListeDiscussions();

                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            // pour vérifier l'existence d'une image par le caractère '‡': ALT + 0135
            Thread th = new Thread(() -> {
                while (true) {
                    // Vérifier la présence du caractère '‡' dans le texte du JTextField
                    if (zoneMessage != null && zoneMessage.getText().contains("‡")) {
                        // Si le caractère est détecté, rendre le JTextField non éditable
                        zoneMessage.setEditable(false);
                        logErreurs.setText("Fichier détecté : Zone d'écrit désactivé.Appuyer sur annuler pour réactiver");
                        System.out.println("Caractère '‡' détecté. Le JTextField est rendu non éditable.");
                        break; // Sortir de la boucle une fois le caractère détecté
                    }
                    
                  
                    try {
                        Thread.sleep(100); // Attendre 100 millisecondes avant de vérifier à nouveau
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            th.start();

            new Thread(() -> {

                try {
                    while (true) {
                        String message = in.readLine();
                         String[] parts = message.split(";");
                    String command = parts[0];
                        if (message != null) {
                            
                    
                            if (message.equals("Notification")) {
                                afficherMessagesConversation(idConversationCourante);
                            }

                            System.out.println("Message reçu du serveur : " + message);
                        }
                        if (message.equals("NOUVEAU MESSAGE")) {
                            afficherMessagesConversation(idConversationCourante);
                        }
                        
                        if (command.equals("NOUVELLE IMAGE")) {
                            try {
                                // Attendre 4 secondes (4000 millisecondes)
                                Thread.sleep(4000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            String nomImage = parts[1];
                            System.out.println("ligne 177 : " + nomImage);

                            afficherMessagesConversation(idConversationCourante);

                            telechargerImage(nomImage);
                        }

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    // Fermer la connexion lorsque le thread se termine
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        initComponents();
        init();
        initContacts();
        initEvenements();
       

    }

    public void initEvenements() {
        zoneMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    boutonEnvoyer.doClick();
                }
            }
        });
    }

    private void init() {
        
        profilAmis.setEnabled(false);
        zoneDiscussion.setEditable(false);
        fichiers.setEnabled(false);

        try {
            Bdd bdd = Bdd.getInstance();

            // Recuperez le prenom et le nom de l'utilisateur en fonction de son email
            String[] prenomNom = bdd.getPrenomNomUtilisateur(email);
            if (prenomNom != null) {
                String prenom = prenomNom[0];
                String nom = prenomNom[1];

                monPrenom.setText((prenom + " " + nom).toUpperCase());

                // Utilisez le prenom et le nom de l'utilisateur comme vous le souhaitez
                System.out.println("Prenom de l'utilisateur : " + prenom);
                System.out.println("Nom de l'utilisateur : " + nom);
            } else {
                System.out.println("Aucun utilisateur trouve avec cet email.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la recuperation du prenom et du nom de l'utilisateur : " + e.getMessage());
        }
    }

    private void initContacts() {
        afficherListeDiscussions();

        // Ajoutez un écouteur d'événements de souris à JList
        listeDiscussions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String contactSelectionne = listeDiscussions.getSelectedValue();
                    profilAmis.setEnabled(true);
                    fichiers.setEnabled(true);
                    Bdd bdd = new Bdd();
                    
                    if(contactSelectionne != null && !contactSelectionne.contains("-")){
                        profilAmis.setEnabled(false);
                        fichiers.setEnabled(false);
                    }
                    if (contactSelectionne != null && contactSelectionne.contains("-")) {
                        try {
                            
                            zoneMessage.setEditable(true); //parce que image ‡
                            zoneMessage.setText(""); //idem ↑
                            zoneMessage.setForeground(BLACK); // idem ↑

                            idGroupeCourant = 0;
                           // imageCourant = null;
                            int idUtilisateur = bdd.getIdUtilisateurParEmail(email);
                            idUserCourant = idUtilisateur;
                            // sépare le nom du contact de son email
                            String[] parts = contactSelectionne.split(" - ");
                            conversation = parts[0]; // Le nom du contact
                            emailContactCourant = parts[1]; // L'email du contact
                            idContactCourant = bdd.getIdUtilisateurParEmail(emailContactCourant); // L'id du contact courant
                            int IDconversation = bdd.getIDConversationParUtilisateurs(idUtilisateur, idContactCourant);
                            idConversationCourante = IDconversation;
                            

                            // Afficher les messages de la conversation courante
                            afficherMessagesConversation(IDconversation);

                            System.out.println("Contact sélectionné : " + conversation);
                            System.out.println("Son email : " + emailContactCourant);
                            System.out.println("Son id : " + idContactCourant);
                            System.out.println("Id de la conversation courante: " + IDconversation);
                            // Faites ce que vous voulez avec la valeur sélectionnée
                        } catch (SQLException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            // Gérez les autres exceptions ici
                            ex.printStackTrace();
                        }
                    } else {

                        idGroupeCourant = bdd.getIdGroupeParNom(contactSelectionne);
                        int idUtilisateur = 0;
                        try {
                            idUtilisateur = bdd.getIdUtilisateurParEmail(email);
                            idUserCourant = idUtilisateur;
                        } catch (SQLException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        int IDconversation = bdd.getIdConversationParGroupId(idGroupeCourant);
                        idConversationCourante = IDconversation;
                        afficherMessagesConversation(IDconversation);
                        //System.out.println("Aucun contact sélectionné.");
                    }
                }
            }
        });
    }
    
   public void initImages(int idConversation) {
    try {
        Bdd bdd = Bdd.getInstance();
        int idUtilisateur = bdd.getIdUtilisateurParEmail(email); // Récupérer l'ID de l'utilisateur courant
        List<String> messages = bdd.getMessagesByConversation(idConversation, idUtilisateur); // Passer l'ID de l'utilisateur courant

        Socket clientSocket = new Socket("localhost", portServeurImage); // Établir la connexion avec le serveur 2

        for (String message : messages) {
            // Séparer le message en ses composants (date, expéditeur, contenu, image)
            String[] parts = message.split(";");

            // Récupérer les composants du message
            String image = parts[3];

            if (image != null && image.length() > 4) {
                downloadAndSaveImage(image);
            }
        }

        // Fermer la connexion avec le serveur 2
        clientSocket.close();
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
}
   
   private void downloadAndSaveImage(String image) throws IOException {
    Socket clientSocket = new Socket("localhost", portServeurImage);
    InputStream inputStream = clientSocket.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    // Recevoir la taille de l'image
    int imageSize = Integer.parseInt(reader.readLine());

    // Créer un tableau de bytes pour stocker l'image
    byte[] imageData = new byte[imageSize];

    // Lire les données de l'image depuis le flux d'entrée
    int bytesRead = inputStream.read(imageData, 0, imageSize);

    // Créer un fichier pour stocker l'image localement
    String nomImage = image;
    FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\User\\Documents\\NetBeansProjects\\MessagerieV2\\src\\ressourcesPerso\\" + nomImage);

    // Écrire les données de l'image dans le fichier
    fileOutputStream.write(imageData, 0, imageSize);

    // Fermer les flux
    fileOutputStream.close();
    reader.close();
    clientSocket.close();
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topleft = new javax.swing.JPanel();
        creerAjouter = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        boutonGroupe = new javax.swing.JButton();
        botleft = new javax.swing.JPanel();
        Profil = new javax.swing.JButton();
        monPrenom = new javax.swing.JLabel();
        leftmid = new javax.swing.JPanel();
        scrollPaneUsers = new javax.swing.JScrollPane();
        listeDiscussions = new javax.swing.JList<>();
        topright = new javax.swing.JPanel();
        profilAmis = new javax.swing.JButton();
        newsPanel = new javax.swing.JPanel();
        logErreurs = new javax.swing.JLabel();
        seDeco = new javax.swing.JButton();
        boutonGestion = new javax.swing.JButton();
        rightmid = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        zoneDiscussion = new javax.swing.JTextPane();
        botright = new javax.swing.JPanel();
        fichiers = new javax.swing.JButton();
        boutonEnvoyer = new javax.swing.JButton();
        zoneMessage = new javax.swing.JTextField();
        boutonAnnuler = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Messagerie");
        setBackground(new java.awt.Color(245, 250, 245));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        topleft.setBackground(new java.awt.Color(255, 255, 255));
        topleft.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        creerAjouter.setBackground(new java.awt.Color(204, 255, 204));
        creerAjouter.setText("Ajouter amis");
        creerAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creerAjouterActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(238, 255, 248));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Discussions");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        boutonGroupe.setBackground(new java.awt.Color(204, 255, 204));
        boutonGroupe.setText("Créer Groupe");
        boutonGroupe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonGroupeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout topleftLayout = new javax.swing.GroupLayout(topleft);
        topleft.setLayout(topleftLayout);
        topleftLayout.setHorizontalGroup(
            topleftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topleftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(topleftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(boutonGroupe, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(creerAjouter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        topleftLayout.setVerticalGroup(
            topleftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topleftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topleftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(topleftLayout.createSequentialGroup()
                        .addComponent(creerAjouter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boutonGroupe, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        botleft.setBackground(new java.awt.Color(255, 255, 255));
        botleft.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Profil.setBackground(new java.awt.Color(204, 255, 204));
        Profil.setText("Profil");
        Profil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProfilActionPerformed(evt);
            }
        });

        monPrenom.setBackground(new java.awt.Color(238, 255, 248));
        monPrenom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        monPrenom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        monPrenom.setText("Moi");
        monPrenom.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout botleftLayout = new javax.swing.GroupLayout(botleft);
        botleft.setLayout(botleftLayout);
        botleftLayout.setHorizontalGroup(
            botleftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botleftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Profil, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(monPrenom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        botleftLayout.setVerticalGroup(
            botleftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botleftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(botleftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Profil, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(monPrenom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        leftmid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        listeDiscussions.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Patrick Sebastien", " ", "Anne Hidalgo", " ", "Hacker Chinois", " ", "Jean Marc Jancovici", " ", "Groupe DCISS" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }

        });
        listeDiscussions.setAlignmentX(50.0F);
        listeDiscussions.setAlignmentY(5.0F);
        scrollPaneUsers.setViewportView(listeDiscussions);

        javax.swing.GroupLayout leftmidLayout = new javax.swing.GroupLayout(leftmid);
        leftmid.setLayout(leftmidLayout);
        leftmidLayout.setHorizontalGroup(
            leftmidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneUsers)
        );
        leftmidLayout.setVerticalGroup(
            leftmidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneUsers, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );

        topright.setBackground(new java.awt.Color(255, 255, 255));
        topright.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        profilAmis.setBackground(new java.awt.Color(204, 204, 255));
        profilAmis.setText("Profil Amis");
        profilAmis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilAmisActionPerformed(evt);
            }
        });

        newsPanel.setBackground(new java.awt.Color(255, 255, 255));

        logErreurs.setForeground(new java.awt.Color(153, 153, 255));

        javax.swing.GroupLayout newsPanelLayout = new javax.swing.GroupLayout(newsPanel);
        newsPanel.setLayout(newsPanelLayout);
        newsPanelLayout.setHorizontalGroup(
            newsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logErreurs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        newsPanelLayout.setVerticalGroup(
            newsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newsPanelLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(logErreurs, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        seDeco.setBackground(new java.awt.Color(255, 153, 153));
        seDeco.setText("Se déconnecter");
        seDeco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seDecoActionPerformed(evt);
            }
        });

        boutonGestion.setBackground(new java.awt.Color(204, 204, 255));
        boutonGestion.setText("Gestion Amis");
        boutonGestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonGestionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout toprightLayout = new javax.swing.GroupLayout(topright);
        topright.setLayout(toprightLayout);
        toprightLayout.setHorizontalGroup(
            toprightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toprightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(toprightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(profilAmis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(boutonGestion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(seDeco)
                .addContainerGap())
        );
        toprightLayout.setVerticalGroup(
            toprightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toprightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(toprightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seDeco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(toprightLayout.createSequentialGroup()
                        .addComponent(newsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(toprightLayout.createSequentialGroup()
                        .addComponent(profilAmis, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boutonGestion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        rightmid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane1.setViewportView(zoneDiscussion);

        javax.swing.GroupLayout rightmidLayout = new javax.swing.GroupLayout(rightmid);
        rightmid.setLayout(rightmidLayout);
        rightmidLayout.setHorizontalGroup(
            rightmidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        rightmidLayout.setVerticalGroup(
            rightmidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        botright.setBackground(new java.awt.Color(255, 255, 255));
        botright.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        fichiers.setBackground(new java.awt.Color(204, 204, 255));
        fichiers.setText("Fichiers");
        fichiers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fichiersActionPerformed(evt);
            }
        });

        boutonEnvoyer.setBackground(new java.awt.Color(204, 255, 204));
        boutonEnvoyer.setText("Envoyer");
        boutonEnvoyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonEnvoyerActionPerformed(evt);
            }
        });

        zoneMessage.setToolTipText("Taper un message");

        boutonAnnuler.setBackground(new java.awt.Color(255, 153, 153));
        boutonAnnuler.setText("Annuler");
        boutonAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonAnnulerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout botrightLayout = new javax.swing.GroupLayout(botright);
        botright.setLayout(botrightLayout);
        botrightLayout.setHorizontalGroup(
            botrightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, botrightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(zoneMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(botrightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fichiers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(boutonAnnuler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(83, 83, 83))
            .addGroup(botrightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, botrightLayout.createSequentialGroup()
                    .addContainerGap(540, Short.MAX_VALUE)
                    .addComponent(boutonEnvoyer)
                    .addGap(4, 4, 4)))
        );
        botrightLayout.setVerticalGroup(
            botrightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botrightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(botrightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(zoneMessage)
                    .addGroup(botrightLayout.createSequentialGroup()
                        .addComponent(fichiers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(boutonAnnuler)))
                .addContainerGap())
            .addGroup(botrightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(botrightLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boutonEnvoyer, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(topleft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(leftmid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botleft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(topright, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rightmid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botright, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(topleft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(topright, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rightmid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(leftmid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botleft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botright, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ProfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProfilActionPerformed
        // TODO add your handling code here:
        Bdd bdd = new Bdd();
        int b=0;
        try {
            b= bdd.getIdUtilisateurParEmail(email);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        Profil a= new Profil(b);
        a.setVisible(true);
        
    }//GEN-LAST:event_ProfilActionPerformed

    private void seDecoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seDecoActionPerformed
        // TODO add your handling code here:
        Inscription a = new Inscription();
        a.setVisible(true); 
        this.dispose(); // ferme la fenetre courante
    }//GEN-LAST:event_seDecoActionPerformed

    private void creerAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creerAjouterActionPerformed
        // TODO add your handling code here:
       
        Recherche frame = new Recherche(email);
        frame.setVisible(true);
      
    }//GEN-LAST:event_creerAjouterActionPerformed

    private void boutonEnvoyerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonEnvoyerActionPerformed
         String message = zoneMessage.getText().trim();

    // Vérifier si le message n'est pas vide
    if (!message.isEmpty() && idGroupeCourant == 0 && imageCourantNom == null) {
        try {
            
            System.out.println(" BLOC 1 ££££££££££££££££££££££££££££££££££imageCourantNom :"+ imageCourantNom);
            // Récupérer l'ID de l'utilisateur à partir de son email
            Bdd bdd = Bdd.getInstance();
            int idUtilisateur = bdd.getIdUtilisateurParEmail(email);
            int idUtilisateur2 = bdd.getIdUtilisateurParEmail(emailContactCourant);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            //on récup l'id de conversation :
            int IDconversation = bdd.getIDConversationParUtilisateurs(idUtilisateur, idUtilisateur2);

            out.println("ENVOYER_MESSAGE;" + IDconversation + ";" + idUtilisateur + ";" + message);
            out.println("IDCONTACT;" + idContactCourant);
            out.println("EMAILCONTACT;" + emailContactCourant); //me sera utile pour liste de client connecté 
            System.out.println("ID de la conversation : " + IDconversation + " | ID de l'utilisateur : " + idUtilisateur);

            zoneMessage.setText("");

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    } else if (idGroupeCourant != 0 && imageCourantData == null) {
        try {
            
            System.out.println("BLOC 2");
            Bdd bdd = Bdd.getInstance();

            emailsDuGroupe = bdd.getUsersInGroup(idGroupeCourant);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("ENVOYER_MESSAGE;" + idConversationCourante + ";" + idUserCourant + ";" + message);
            out.println("EMAILSDUGROUPE;" + emailsDuGroupe);
            System.out.println(emailsDuGroupe);
            //  out.println("IDCONTACT;" + idContactCourant);
            //    out.println("EMAILCONTACT;" + emailContactCourant);
            zoneMessage.setText("");

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    } else if (!message.isEmpty() && idGroupeCourant == 0 && imageCourantNom != null) {
    try {
        // Récupérer l'ID de l'utilisateur à partir de son email
        Bdd bdd = Bdd.getInstance();
        int idUtilisateur = bdd.getIdUtilisateurParEmail(email);
        int idUtilisateur2 = bdd.getIdUtilisateurParEmail(emailContactCourant);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Récupérer l'ID de la conversation
        int IDconversation = bdd.getIDConversationParUtilisateurs(idUtilisateur, idUtilisateur2);

        // Construire le message avec le nom du fichier de l'image
        
        String uniqueName = generateUniqueName(imageCourantNom,emailContactCourant );
        out.println("ENVOYER_IMAGE;" + IDconversation + ";" + idUtilisateur + ";" + message + ";" + uniqueName+ ";"+ emailContactCourant);
        out.println("IDCONTACT;" + idContactCourant);
        out.println("EMAILCONTACT;" + emailContactCourant);

       try {
    String imagePath = imageCourantPath;
    byte[] imageCourantData = Files.readAllBytes(Paths.get(imagePath));
    // Créer un tableau de bytes contenant le nom unique suivi d'un délimiteur et des données de l'image
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byteArrayOutputStream.write(uniqueName.getBytes());
    byteArrayOutputStream.write(";".getBytes());
    byteArrayOutputStream.write(imageCourantData);

    Socket socket = new Socket("localhost", 5555);
    OutputStream outputStream = socket.getOutputStream();

    // Envoyer les données de l'image avec le nom unique séparé par le délimiteur
    byteArrayOutputStream.writeTo(outputStream);
    byteArrayOutputStream.flush();
    byteArrayOutputStream.close();

    outputStream.close();
    socket.close();
} catch (IOException e) {
    e.printStackTrace();
}



        // Réinitialiser le champ de texte
        zoneMessage.setText("");
        logErreurs.setText("");

        // Afficher un message de confirmation
        System.out.println("Message de type image envoyé avec succès");

    } catch (IOException ex) {
        ex.printStackTrace();
    } catch (SQLException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
} else {
        JOptionPane.showMessageDialog(this, "Veuillez saisir un message.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_boutonEnvoyerActionPerformed

    private void boutonGroupeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonGroupeActionPerformed
        // TODO add your handling code here:
        Bdd bdd = new Bdd();
        contactsUserCourant = bdd.getContactsByUser(email);
        Groupe frame = new Groupe(contactsUserCourant, email);
        frame.setVisible(true);
        afficherMessagesConversation(idConversationCourante);

    }//GEN-LAST:event_boutonGroupeActionPerformed

    private void fichiersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fichiersActionPerformed
      
        JFileChooser fileChooser = new JFileChooser();

      //ouvre fenetre du dossier ressource
        fileChooser.setCurrentDirectory(new java.io.File("C:\\Users\\User\\Documents\\NetBeansProjects\\MessagerieV2\\src\\ressources"));

        // filtre d'extension 
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        // Afficher la boîte de dialogue de sélection de fichier
        int result = fileChooser.showOpenDialog(this); // "this" fait référence à la fenêtre principale

        // Vérifier si l'utilisateur a sélectionné un fichier
        if (result == JFileChooser.APPROVE_OPTION) {
            // Obtenir le fichier sélectionné
            java.io.File selectedFile = fileChooser.getSelectedFile();

            try {
                // Enregistrer le chemin du fichier dans votre variable statique imageCourantPath
                imageCourantPath = selectedFile.getAbsolutePath();

                // Enregistrer le nom du fichier dans votre variable statique imageCourantNom
                imageCourantNom = selectedFile.getName();
                
                zoneMessage.setForeground(BLUE);
                
                zoneMessage.setText('‡'+imageCourantNom);
                
                zoneMessage.setEditable(false);

                // Afficher le chemin du fichier sélectionné
                System.out.println("Chemin du fichier sélectionné : " + imageCourantPath);

                // Afficher le nom du fichier sélectionné
                System.out.println("Nom du fichier sélectionné : " + imageCourantNom);
            } catch (Exception ex) {
                // Gérer les exceptions liées à la lecture de l'image
                ex.printStackTrace();
            }
        } else {
            // L'utilisateur a annulé la sélection
            System.out.println("Aucun fichier sélectionné.");
        }
    }//GEN-LAST:event_fichiersActionPerformed

    private void boutonAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonAnnulerActionPerformed
        // TODO add your handling code here:
        zoneMessage.setEditable(true);
        zoneMessage.setText("");
        zoneMessage.setForeground(BLACK);
        logErreurs.setText("");
    }//GEN-LAST:event_boutonAnnulerActionPerformed

    private void profilAmisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilAmisActionPerformed
        
              Bdd bdd = new Bdd();
        int b=0;
        try {
            b= bdd.getIdUtilisateurParEmail(emailContactCourant);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
        ProfilAmis a= new ProfilAmis(b);
        a.setVisible(true);        
    }//GEN-LAST:event_profilAmisActionPerformed

    private void boutonGestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonGestionActionPerformed
        // TODO add your handling code here:
        
          Bdd bdd = new Bdd();
        contactsUserCourant = bdd.getContactsByUser(email);
        GestionAmis frame = new GestionAmis(contactsUserCourant, email);
        
        frame.setVisible(true);
        afficherMessagesConversation(idConversationCourante);
    }//GEN-LAST:event_boutonGestionActionPerformed

    //ZONE DEGUEUX ALERT : ICI SEROTN LES METHODES POUR AFFICHER LES DATAS, ON PENSERA A FAIRE PLUS TARD UN MODEL AUTRE QUE BALAIS A CHIOTTE POUR MVC
private void afficherMessagesConversation(int idConversation) {
    try {
        Bdd bdd = Bdd.getInstance();
        int idUtilisateur = bdd.getIdUtilisateurParEmail(email); // Récupérer l'ID de l'utilisateur courant
        List<String> messages = bdd.getMessagesByConversation(idConversation, idUtilisateur); // Passer l'ID de l'utilisateur courant

        StringBuilder sb = new StringBuilder();
        Socket clientSocket = new Socket("localhost", portServeurImage); // Établir la connexion avec le serveur 2

        for (String message : messages) {
            // sépare le message en ses composants (date, expéditeur, contenu, image)
            String[] parts = message.split(";");

            String date = parts[0];
            String expediteur = parts[1];
            String contenuMessage = parts[2];
            String image = parts[3]; // Initialiser l'image à null

            sb.append("[")
                .append(date)
                .append("] ");

            // check si l'expéditeur est l'utilisateur courant
            if (expediteur.equals(email)) {
                // check si l'expéditeur est l'utilisateur courant, rendre le texte bleu
                sb.append("<font color=green>");
            }

            sb.append(expediteur)
                .append(": ")
                .append(contenuMessage);

            // si le message contient une image
            //MARCCCCCCCCCCCCCCCCCHHHHHHHHHHHHHEEEEEEEEEEEEE PASSSSSSSSSSSSSSSSSSSSSSSSSSSS
            if (image != null && image.length() > 4) {
                // Créer une instance d'ImageIcon pour votre image
                ImageIcon imageIcon = new ImageIcon("php.jpg");

                // Créer une balise HTML avec l'image
                String imageHTML = "<img src='C:\\Users\\User\\Documents\\NetBeansProjects\\MessagerieV2\\src\\Messagerie\\php.jpg"  + "'/>";

                // Ajouter l'image au StringBuilder
                sb.append("<br>").append(imageHTML);
            }

            sb.append("</font><br>"); // le saut de ligne après chaque message
        }

        // Fermer la connexion avec le serveur 2
        clientSocket.close();

        // Afficher le texte dans la zone de discussion
        zoneDiscussion.setContentType("text/html"); // Permet l'utilisation du format HTML
        sb.insert(0, "<html><body style='font-size:11px;'>"); // Augmenter légèrement la taille de la police
        sb.append("</body></html>");
        zoneDiscussion.setText(sb.toString());
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
}



 


    public void afficherListeDiscussions() {
        DefaultListModel<String> modelListeContacts = new DefaultListModel<>();

        try {
            // Récupére la liste des contacts de l'utilisateur à partir de la base de données
            Bdd bdd = new Bdd();
            List<String> listeContacts = bdd.getContactsByUser(email); // Utiliser l'email de l'utilisateur
            List<String> groupesUtilisateur = bdd.getGroupesByUser(email); // Obtenir les groupes de discussion de l'utilisateur

            // Affichez les contacts de l'utilisateur
            System.out.println("Contacts de l'utilisateur courant :");
            for (String contact : listeContacts) {
                System.out.println(contact);
                modelListeContacts.addElement(contact);
            }

            // Affichez les groupes de discussion de l'utilisateur
            System.out.println("Groupes de discussion de l'utilisateur courant :");
            for (String groupe : groupesUtilisateur) {
                System.out.println(groupe);
                modelListeContacts.addElement(groupe);
            }
        } catch (Exception e) {
            // Gérez les éventuelles erreurs lors de la récupération des contacts
            e.printStackTrace();
        }

        // Définissez le modèle de liste pour votre JList
        listeDiscussions.setModel(modelListeContacts);
        listeDiscussions.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

//ZONE DE NON DROIT :
public static void telechargerImage(String nomImage) {
    Thread telechargementThread = new Thread(() -> {
        try {
            System.out.println("Thread telechargerImage demarre");
            // Envoyer la demande pour télécharger l'image au serveur
            Socket socketImage = new Socket("localhost", 5555);

            PrintWriter outImage = new PrintWriter(socketImage.getOutputStream(), true);

            outImage.println(nomImage);
            outImage.flush();

            // Recevoir la taille de l'image du serveur
            InputStream inputStream = socketImage.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            int imageSize = Integer.parseInt(reader.readLine());

            // Créer un tableau de bytes pour stocker l'image
            byte[] imageData = new byte[imageSize];

            // Lire les données de l'image depuis le flux d'entrée
            int bytesRead = inputStream.read(imageData, 0, imageSize);

            System.out.println("Ligne 1010");
            // crée un fichier pour stocker l'image localement
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\User\\Documents\\NetBeansProjects\\MessagerieV2\\src\\ressourcesPerso\\" + nomImage);

            // écrit les données de l'image dans le fichier
            fileOutputStream.write(imageData, 0, bytesRead); // 

           
            fileOutputStream.close();
            reader.close();
            inputStream.close();
            socketImage.close(); 

            System.out.println("Fichier téléchargé avec succès LIGNE 1022");
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    
    telechargementThread.start();
}


    
        private static String generateUniqueName(String documentName, String userEmail) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String separator = "Ž";
        return timeStamp+ userEmail;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main(email).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Profil;
    private javax.swing.JPanel botleft;
    private javax.swing.JPanel botright;
    private javax.swing.JButton boutonAnnuler;
    private javax.swing.JButton boutonEnvoyer;
    private javax.swing.JButton boutonGestion;
    private javax.swing.JButton boutonGroupe;
    private javax.swing.JButton creerAjouter;
    private javax.swing.JButton fichiers;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel leftmid;
    private javax.swing.JList<String> listeDiscussions;
    private javax.swing.JLabel logErreurs;
    private javax.swing.JLabel monPrenom;
    private javax.swing.JPanel newsPanel;
    private javax.swing.JButton profilAmis;
    private javax.swing.JPanel rightmid;
    private javax.swing.JScrollPane scrollPaneUsers;
    private javax.swing.JButton seDeco;
    private javax.swing.JPanel topleft;
    private javax.swing.JPanel topright;
    private javax.swing.JTextPane zoneDiscussion;
    private javax.swing.JTextField zoneMessage;
    // End of variables declaration//GEN-END:variables
}
