package BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Bdd {
    private static Bdd instance;
    private Connection connection;

    public Bdd() {
        try {
            connectBdd();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Bdd getInstance() {
        if (instance == null) {
            instance = new Bdd();
        }
        return instance;
    }

    public void connectBdd() throws SQLException {
        String server = "localhost";
        String port = "3306"; // port par défaut de MySQL
        String database = "messageriev2";
        String userName = "root";
        String password = "";
        connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database, userName, password);
    }
    
    public boolean connexionUtilisateur(String email, String password) {
        try {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification des informations de connexion : " + e.getMessage());
            return false;
        }
    }

    public void insertData(String prenom, String nom, String email, String password) {
        try {
            String sql = "INSERT INTO users (prenom, nom, email, password) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, prenom);
            statement.setString(2, nom);
            statement.setString(3, email);
            statement.setString(4, password);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utilisateur ajouté avec succès !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion des données : " + e.getMessage());
        }
    }
    
    public String[] getAll(int IdUtilisateur) {
    String[] userDetails = new String[4]; // Tableau pour stocker les détails de l'utilisateur
    
    // Connexion à la base de données
    try  {
        // Préparer la requête SQL
        String query = "SELECT prenom, nom, email, bia FROM users WHERE ID_utilisateur = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, IdUtilisateur);
            
            // Exécuter la requête SQL et récupérer les résultats
            try (ResultSet resultSet = statement.executeQuery()) {
                // Vérifier si un enregistrement a été trouvé
                if (resultSet.next()) {
                    // Récupérer les valeurs des colonnes et les stocker dans le tableau
                    userDetails[0] = resultSet.getString("prenom");
                    userDetails[1] = resultSet.getString("nom");
                    userDetails[2] = resultSet.getString("email");
                    userDetails[3] = resultSet.getString("bia");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return userDetails;
}
    
 public String updateBio(int IdUtilisateur, String newBio) {
    String resultMessage = "";
    // Connexion à la base de données
    try  {
        // Préparer la requête SQL pour mettre à jour la bio
        String query = "UPDATE users SET bia = ? WHERE ID_utilisateur = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newBio);
            statement.setInt(2, IdUtilisateur);
            
            // Exécuter la requête SQL pour mettre à jour la bio
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                resultMessage = "";
            } else {
                resultMessage = "";
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        resultMessage = "Erreur lors de la mise à jour de la bio : " + e.getMessage();
    }
    return resultMessage;
}
 
 public void supprimerContact(int idUtilisateurCourant, int idContactCourant) {
        try  {
            // Préparer la requête SQL pour supprimer le contact de la table des contacts
            String query = "DELETE FROM contacts WHERE ID_utilisateur = ? AND ID_contact_utilisateur = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Remplir les paramètres de la requête
                statement.setInt(1, idUtilisateurCourant);
                statement.setInt(2, idContactCourant);
                
                // Exécuter la requête
                int rowsDeleted = statement.executeUpdate();
                
                // Vérifier si des lignes ont été supprimées avec succès
                if (rowsDeleted > 0) {
                    System.out.println("Le contact a été supprimé avec succès.");
                } else {
                    System.out.println("Impossible de trouver le contact à supprimer.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public String[] getPrenomNomUtilisateur(String email) {
        try {
            String sql = "SELECT prenom, nom FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                return new String[]{prenom, nom};
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du prénom et du nom de l'utilisateur : " + e.getMessage());
            return null;
        }
    }
    
 public List<String> getContactsByUser(String emailUtilisateur) {
    List<String> contacts = new ArrayList<>();
    try {
        String sql = "SELECT CONCAT(c.prenom, ' ', c.nom) AS contact, c.prenom, c.nom, u.email " +
                     "FROM contacts c " +
                     "INNER JOIN users u ON c.ID_contact_utilisateur = u.ID_utilisateur " +
                     "WHERE c.ID_utilisateur = (SELECT ID_utilisateur FROM users WHERE email = ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, emailUtilisateur);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String contact = resultSet.getString("contact");
            String prenom = resultSet.getString("prenom");
            String nom = resultSet.getString("nom");
            String email = resultSet.getString("email");
            // Enregistrer l'email dans une variable distincte
            // Vous pouvez faire ce que vous voulez avec cette valeur
            String contactInfo = contact + " - " + email;
            contacts.add(contactInfo);
        }
//        resultSet.close();
//        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return contacts;
}


    public void addContact(String emailUtilisateurCourant, String prenomContact, String nomContact) {
        try {
            String sqlGetUserID = "SELECT ID_utilisateur FROM users WHERE email = ?";
            PreparedStatement getUserIdStatement = connection.prepareStatement(sqlGetUserID);
            getUserIdStatement.setString(1, emailUtilisateurCourant);
            ResultSet resultSet = getUserIdStatement.executeQuery();
            int idUtilisateur = -1;
            if (resultSet.next()) {
                idUtilisateur = resultSet.getInt("ID_utilisateur");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet e-mail : " + emailUtilisateurCourant);
                return;
            }
            String sql = "INSERT INTO contacts (ID_utilisateur, prenom, nom, ID_contact_utilisateur) " +
                         "VALUES (?, ?, ?, (SELECT ID_utilisateur FROM users WHERE prenom = ? AND nom = ?))";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idUtilisateur);
            statement.setString(2, prenomContact);
            statement.setString(3, nomContact);
            statement.setString(4, prenomContact);
            statement.setString(5, nomContact);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Le contact a été ajouté avec succès !");
            }
//            resultSet.close();
//            getUserIdStatement.close();
//            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUtilisateurParEmail(String email) {
        String utilisateur = "";
        try {
            String sql = "SELECT prenom, nom FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                utilisateur = prenom + " " + nom;
            }
            //else {
              //  utilisateur = "null";
                //        System.out.println("Aucun utilisateur trouvé avec cet e-mail : " + email);
            //}
//            resultSet.close();
//            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }
    
    public boolean estDejaContact(String emailUtilisateurCourant, String emailUtilisateurAChercher) {
    try {
        // Récupérer l'ID de l'utilisateur courant
        String sqlGetUserID = "SELECT ID_utilisateur FROM users WHERE email = ?";
        PreparedStatement getUserIdStatement = connection.prepareStatement(sqlGetUserID);
        getUserIdStatement.setString(1, emailUtilisateurCourant);
        ResultSet resultSet = getUserIdStatement.executeQuery();
        int idUtilisateurCourant = -1;
        if (resultSet.next()) {
            idUtilisateurCourant = resultSet.getInt("ID_utilisateur");
        } else {
            System.out.println("Aucun utilisateur trouvé avec cet e-mail : " + emailUtilisateurCourant);
            return false;
        }
        
        // Récupérer l'ID de l'utilisateur à rechercher
        String sqlGetIDUtilisateurAChercher = "SELECT ID_utilisateur FROM users WHERE email = ?";
        PreparedStatement getIdUtilisateurAChercherStatement = connection.prepareStatement(sqlGetIDUtilisateurAChercher);
        getIdUtilisateurAChercherStatement.setString(1, emailUtilisateurAChercher);
        resultSet = getIdUtilisateurAChercherStatement.executeQuery();
        int idUtilisateurAChercher = -1;
        if (resultSet.next()) {
            idUtilisateurAChercher = resultSet.getInt("ID_utilisateur");
        } else {
            System.out.println("Aucun utilisateur trouvé avec cet e-mail : " + emailUtilisateurAChercher);
            return false;
        }
        
        // Vérifier si l'utilisateur à rechercher est déjà dans les contacts de l'utilisateur courant
        String sqlCheckContact = "SELECT COUNT(*) AS count FROM contacts WHERE ID_utilisateur = ? AND ID_contact_utilisateur = ?";
        PreparedStatement checkContactStatement = connection.prepareStatement(sqlCheckContact);
        checkContactStatement.setInt(1, idUtilisateurCourant);
        checkContactStatement.setInt(2, idUtilisateurAChercher);
        resultSet = checkContactStatement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            return count > 0;
        } else {
            return false;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    
    // A REVOIR PLSU TARD CETTE METHODE, ELLE EST SUSPECT 
 public void enregistrerMessage(int idConversation, int idUtilisateur, String contenuMessage) {
    try {
        // Connexion à la base de données
        connectBdd();

        // Requête SQL pour insérer le message dans la table Messages
        String sql = "INSERT INTO Messages (ID_conversation, ID_utilisateur, Contenu, Date_envoi) VALUES (?, ?, ?, NOW())";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idConversation);
        statement.setInt(2, idUtilisateur);
        statement.setString(3, contenuMessage);

        // Exécution de la requête
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Le message a été enregistré avec succès !");
            // Ici, vous pourriez également envoyer une notification aux autres utilisateurs de la conversation si nécessaire
        }

        // Fermeture des ressources
        
       // go check si ça ferme ou pas 
       // statement.close();
       // connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
 
public void enregistrerMessageAvecImage(int idConversation, int idUtilisateur, String contenuMessage, String image) {
    try {
        // Connexion à la base de données
        connectBdd();

        // Requête SQL pour insérer le message dans la table Messages
        String sql = "INSERT INTO Messages (ID_conversation, ID_utilisateur, Contenu, Image, Date_envoi) VALUES (?, ?, ?, ?, NOW())";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idConversation);
        statement.setInt(2, idUtilisateur);
        statement.setString(3, contenuMessage);
        statement.setString(4, image);

        // Exécution de la requête
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Le message a été enregistré avec succès ! DEPUIS BDD???");
            
        }

        // Fermeture des ressources
       // statement.close();
      //  connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

 
 /////////////////////////////////////////////////////////////////////////////////////////////////
 // Pour ajouter une nouvelle conversation lors de l'ajout d'un contact
public void creerNouvelleConversation(int idUtilisateur1, int idUtilisateur2) {
    try {
        connectBdd();
        // Insertion de la nouvelle conversation dans la table conversations
        String sql = "INSERT INTO conversations (ID_utilisateur1, ID_utilisateur2) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idUtilisateur1);
        statement.setInt(2, idUtilisateur2);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Nouvelle conversation créée avec succès !");
            // Vous pouvez effectuer d'autres opérations ici si nécessaire
        }
//        statement.close();
//        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}




public int getIdUtilisateurParEmail(String email) throws SQLException {
    String sql = "SELECT ID_utilisateur FROM users WHERE email = ?";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setString(1, email);
    ResultSet resultSet = statement.executeQuery();
    int idUtilisateur = -1;
    if (resultSet.next()) {
        idUtilisateur = resultSet.getInt("ID_utilisateur");
    }
 //   resultSet.close();
 //   statement.close();
    return idUtilisateur;
}

private int getIdConversation(int idUtilisateur1, int idUtilisateur2) throws SQLException {
    String sql = "SELECT ID_conversation FROM conversations WHERE (ID_utilisateur1 = ? AND ID_utilisateur2 = ?) OR (ID_utilisateur1 = ? AND ID_utilisateur2 = ?)";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, idUtilisateur1);
    statement.setInt(2, idUtilisateur2);
    statement.setInt(3, idUtilisateur2);
    statement.setInt(4, idUtilisateur1);
    ResultSet resultSet = statement.executeQuery();
    int idConversation = -1;
    if (resultSet.next()) {
        idConversation = resultSet.getInt("ID_conversation");
    }
    //resultSet.close();
    //statement.close();
    return idConversation;
}



 
///////////////////////////////////////////////////////////////////////////////////////////////////////


 public int getIDUtilisateurParPrenomNom(String prenom, String nom) {
    try {
        String sql = "SELECT ID_utilisateur FROM users WHERE prenom = ? AND nom = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, prenom);
        statement.setString(2, nom);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("ID_utilisateur");
        } else {
            System.err.println("Aucun utilisateur trouvé avec ce prénom et ce nom : " + prenom + " " + nom);
            return -1; // Retourne -1 si aucun utilisateur n'est trouvé
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération de l'ID de l'utilisateur : " + e.getMessage());
        return -1; // Retourne -1 en cas d'erreur
    }
}

public int getIDConversationParNomConversation(String nomConversation) {
    try {
        String sql = "SELECT ID_conversation FROM conversations WHERE nom_conversation = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, nomConversation);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("ID_conversation");
        } else {
            System.err.println("Aucune conversation trouvée avec ce nom : " + nomConversation);
            return -1; // Retourne -1 si aucune conversation n'est trouvée
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération de l'ID de la conversation : " + e.getMessage());
        return -1; // Retourne -1 en cas d'erreur
    }
}


public int getIDConversationParUtilisateurs(int idUtilisateur1, int idUtilisateur2) {
    try {
        // Connexion à la base de données
        connectBdd();

        // Requête SQL pour récupérer l'ID de la conversation
        String sql = "SELECT ID_conversation FROM conversations WHERE (ID_utilisateur1 = ? AND ID_utilisateur2 = ?) OR (ID_utilisateur1 = ? AND ID_utilisateur2 = ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idUtilisateur1);
        statement.setInt(2, idUtilisateur2);
        statement.setInt(3, idUtilisateur2);
        statement.setInt(4, idUtilisateur1);
        ResultSet resultSet = statement.executeQuery();

        // Vérifier si une conversation a été trouvée
        if (resultSet.next()) {
            int idConversation = resultSet.getInt("ID_conversation");
            // Fermer les ressources
         //   resultSet.close();
           // statement.close();
          //  connection.close();
            return idConversation;
        } else {
            System.out.println("Aucune conversation trouvée entre les utilisateurs avec les ID : " + idUtilisateur1 + " et " + idUtilisateur2);
            // Fermer les ressources
        //    resultSet.close();
          //  statement.close();
           // connection.close();
            return -1; // Retourne -1 si aucune conversation n'est trouvée
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return -1; // Retourne -1 en cas d'erreur
    }
    
    
    
    
}
public List<String> getMessagesByConversation(int idConversation, int idUtilisateur) throws SQLException {
    List<String> messages = new ArrayList<>();
    String sql = "SELECT m.Date_envoi, u.email AS expediteur, m.Contenu, m.image " +
                 "FROM messages m " +
                 "INNER JOIN users u ON m.ID_utilisateur = u.ID_utilisateur " +
                 "WHERE m.ID_conversation = ? " +
                 "AND (m.ID_utilisateur = ? OR m.ID_utilisateur = (SELECT ID_contact_utilisateur FROM contacts WHERE ID_utilisateur = ? AND ID_contact_utilisateur = m.ID_utilisateur)) " +
                 "ORDER BY m.Date_envoi"; // Assurez-vous que la table des messages contient un champ Date_envoi pour la date
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, idConversation);
        statement.setInt(2, idUtilisateur);
        statement.setInt(3, idUtilisateur);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String dateEnvoi = resultSet.getString("Date_envoi");
            String expediteur = resultSet.getString("expediteur");
            String contenuMessage = resultSet.getString("Contenu");
            String image = resultSet.getString("image"); // Récupérer le contenu de l'image
            // Concaténer la date, l'expéditeur, le contenu du message et le contenu de l'image avec un délimiteur
            String message = dateEnvoi + ";" + expediteur + ";" + contenuMessage + ";" + image;
            messages.add(message);
        }
    }
    return messages;
}


//////////////////////////////////////////////////////////
//GROUPES
public void creerGroupe(String nomGroupe) {
        try {
            connectBdd(); // Connexion à la base de données

            // Requête SQL pour insérer le nouveau groupe dans la table 'groupes'
            String sql = "INSERT INTO groupes (nom) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nomGroupe);
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Le groupe a été créé avec succès !");
            } else {
                System.err.println("Erreur lors de la création du groupe : aucun enregistrement inséré.");
            }

            // Fermeture des ressources
            statement.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du groupe : " + e.getMessage());
        }
    }

public void ajouterMembresAuGroupe(String nomGroupe, List<String> emailsMembres, String emailUtilisateurCourant) {
    try {
        connectBdd(); // Connexion à la base de données

        // Requête SQL pour récupérer l'ID du groupe à partir de son nom
        String sqlGetGroupId = "SELECT ID_groupe FROM groupes WHERE nom = ?";
        PreparedStatement getGroupIdStatement = connection.prepareStatement(sqlGetGroupId);
        getGroupIdStatement.setString(1, nomGroupe);
        ResultSet resultSet = getGroupIdStatement.executeQuery();

        int idGroupe = -1;
        if (resultSet.next()) {
            idGroupe = resultSet.getInt("ID_groupe");
        } else {
            System.err.println("Aucun groupe trouvé avec le nom : " + nomGroupe);
            return;
        }

        // Requête SQL pour insérer les membres dans la table 'membresGroupe'
        String sqlInsertMembers = "INSERT INTO membresGroupe (ID_utilisateur, ID_groupe) VALUES (?, ?)";
        PreparedStatement insertMembersStatement = connection.prepareStatement(sqlInsertMembers);

        // Récupérer l'ID utilisateur à partir de l'email de l'utilisateur courant
        int idUtilisateurCourant = getIdUtilisateurParEmail(emailUtilisateurCourant);
        if (idUtilisateurCourant != -1) {
            // Insérer l'utilisateur courant dans la table 'membresGroupe'
            insertMembersStatement.setInt(1, idUtilisateurCourant);
            insertMembersStatement.setInt(2, idGroupe);
            insertMembersStatement.executeUpdate();
        } else {
            System.err.println("Aucun utilisateur trouvé avec l'email : " + emailUtilisateurCourant);
        }

        // Insérer les autres membres dans la table 'membresGroupe'
        for (String email : emailsMembres) {
            // Récupérer l'ID utilisateur à partir de l'email
            int idUtilisateur = getIdUtilisateurParEmail(email);
            if (idUtilisateur != -1) {
                // Insérer le membre dans la table 'membresGroupe'
                insertMembersStatement.setInt(1, idUtilisateur);
                insertMembersStatement.setInt(2, idGroupe);
                insertMembersStatement.executeUpdate();
            } else {
                System.err.println("Aucun utilisateur trouvé avec l'email : " + email);
            }
        }

        System.out.println("Les membres ont été ajoutés au groupe avec succès !");

        // Fermeture des ressources
        resultSet.close();
        getGroupIdStatement.close();
        insertMembersStatement.close();
    } catch (SQLException e) {
        System.err.println("Erreur lors de l'ajout des membres au groupe : " + e.getMessage());
    }
}

  
  public void creerNouvelleConversationAvecGroupe(int idGroupe) {
    try {
        connectBdd();
        // Insertion de la nouvelle conversation dans la table conversations
        String sql = "INSERT INTO conversations (ID_groupe) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idGroupe);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Nouvelle conversation créée avec succès !");
            // Vous pouvez effectuer d'autres opérations ici si nécessaire
        }
//        statement.close();
//        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
  
  public int getIdGroupeParNom(String nomGroupe) {
    try {
        String sql = "SELECT ID_groupe FROM groupes WHERE nom = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, nomGroupe);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("ID_groupe");
        } else {
            System.err.println("Aucun groupe trouvé avec ce nom : " + nomGroupe);
            return -1; // Retourne -1 si aucun groupe n'est trouvé
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération de l'ID du groupe : " + e.getMessage());
        return -1; // Retourne -1 en cas d'erreur
    }
}
  
  public List<String> getGroupesByUser(String emailUtilisateur) {
    List<String> groupesUtilisateur = new ArrayList<>();
    try {
        // Récupérer l'ID de l'utilisateur
        int idUtilisateur = getIdUtilisateurParEmail(emailUtilisateur);
        if (idUtilisateur != -1) {
            String sql = "SELECT g.nom FROM groupes g " +
                         "INNER JOIN membresGroupe mg ON g.ID_groupe = mg.ID_groupe " +
                         "WHERE mg.ID_utilisateur = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idUtilisateur);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nomGroupe = resultSet.getString("nom");
                groupesUtilisateur.add(nomGroupe);
            }
        } else {
            System.out.println("Utilisateur non trouvé !");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return groupesUtilisateur;
}

public int getIdConversationParGroupId(int groupId) {
    int conversationId = -1; // Valeur par défaut si aucune conversation n'est trouvée
    try {
        String sql = "SELECT ID_conversation FROM conversations WHERE ID_groupe = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, groupId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            conversationId = resultSet.getInt("ID_conversation");
        }
        resultSet.close();
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return conversationId;
}

public String getUsersInGroup(int idGroupe) {
    StringBuilder users = new StringBuilder();
    try {
        // Requête SQL pour obtenir les utilisateurs d'un groupe
        String sql = "SELECT u.email FROM users u INNER JOIN membresGroupe m ON u.ID_utilisateur = m.ID_utilisateur WHERE m.ID_groupe = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idGroupe);
        ResultSet resultSet = statement.executeQuery();

        // Parcourir les résultats et construire la chaîne de caractères des utilisateurs
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            users.append(email).append("~");
        }

        // Supprimer le dernier caractère '~' s'il existe
        if (users.length() > 0) {
            users.deleteCharAt(users.length() - 1);
        }
        
        resultSet.close();
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return users.toString();
}

public String getImagePathFromDatabase(String image) {
    try {
        // Connexion à la base de données
        connectBdd();

        // Requête SQL pour récupérer le chemin de l'image à partir de son ID
        String sql = "SELECT image FROM messageriev2 WHERE ID_image = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, image);
        ResultSet resultSet = statement.executeQuery();

        // Vérifier si une image a été trouvée
        if (resultSet.next()) {
            String imagePath = resultSet.getString("image");
            return imagePath;
        } else {
            System.out.println("Aucune image trouvée avec l'ID : " + image);
            return null;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}



//////////////////////////////////////////////////////////////////////////////////


    public static void main(String[] args) {
        Bdd bdd = Bdd.getInstance();
        bdd.insertData("James", "Lotter", "jcfeydelgmail.com", "password");
    }
}
