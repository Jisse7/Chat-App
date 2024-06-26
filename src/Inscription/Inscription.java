/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Inscription;

import BDD.Bdd;
import Cryptage.Playfair;
import Messagerie.Main;
//import Serveur.Server;
import static java.awt.Color.GREEN;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author User
 */
public class Inscription extends javax.swing.JFrame {

    /**
     * Creates new form Inscription
     */
    public Inscription() {
        initComponents();
        initEvenements();
    }

    public void initEvenements() {
        formMdpCo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    boutonSeConnecter.doClick();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        connexion = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        formEmailCo = new javax.swing.JTextField();
        boutonSeConnecter = new javax.swing.JButton();
        boutonPasDeCompte = new javax.swing.JButton();
        formMdpCo = new javax.swing.JPasswordField();
        inscription = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        creeruncompte = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        formPrenom = new javax.swing.JTextField();
        formNom = new javax.swing.JTextField();
        formEmail = new javax.swing.JTextField();
        boutonSenregistrer = new javax.swing.JButton();
        boutonJaiUnCompte = new javax.swing.JButton();
        formMdp = new javax.swing.JPasswordField();
        logErreurCo = new javax.swing.JLabel();
        logErreurCreation = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Formulaire");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        connexion.setBackground(new java.awt.Color(221, 253, 221));
        connexion.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("SE CONNECTER");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Email");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Mot de passe");

        boutonSeConnecter.setBackground(new java.awt.Color(204, 255, 204));
        boutonSeConnecter.setText("Se connecter");
        boutonSeConnecter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonSeConnecterActionPerformed(evt);
            }
        });

        boutonPasDeCompte.setBackground(new java.awt.Color(204, 204, 255));
        boutonPasDeCompte.setText("J'ai pas de compte");
        boutonPasDeCompte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonPasDeCompteActionPerformed(evt);
            }
        });

        formMdpCo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formMdpCoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout connexionLayout = new javax.swing.GroupLayout(connexion);
        connexion.setLayout(connexionLayout);
        connexionLayout.setHorizontalGroup(
            connexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, connexionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(110, 110, 110))
            .addGroup(connexionLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(connexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(formEmailCo)
                    .addGroup(connexionLayout.createSequentialGroup()
                        .addComponent(boutonSeConnecter, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(boutonPasDeCompte))
                    .addComponent(formMdpCo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        connexionLayout.setVerticalGroup(
            connexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connexionLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel3)
                .addGap(76, 76, 76)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(formEmailCo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(formMdpCo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(connexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boutonPasDeCompte)
                    .addComponent(boutonSeConnecter))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        inscription.setBackground(new java.awt.Color(235, 235, 255));
        inscription.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Prenom");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Nom");

        creeruncompte.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        creeruncompte.setText("CREER UN COMPTE");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Email");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Mot de passe");

        formPrenom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formPrenomActionPerformed(evt);
            }
        });

        formNom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formNomActionPerformed(evt);
            }
        });

        boutonSenregistrer.setBackground(new java.awt.Color(204, 204, 255));
        boutonSenregistrer.setText("S'enregistrer");
        boutonSenregistrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonSenregistrerActionPerformed(evt);
            }
        });

        boutonJaiUnCompte.setBackground(new java.awt.Color(204, 255, 204));
        boutonJaiUnCompte.setText("J'ai déjà un compte");
        boutonJaiUnCompte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonJaiUnCompteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inscriptionLayout = new javax.swing.GroupLayout(inscription);
        inscription.setLayout(inscriptionLayout);
        inscriptionLayout.setHorizontalGroup(
            inscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inscriptionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(creeruncompte)
                .addGap(85, 85, 85))
            .addGroup(inscriptionLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(inscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inscriptionLayout.createSequentialGroup()
                            .addComponent(boutonSenregistrer, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(183, 183, 183))
                        .addComponent(jLabel5)
                        .addComponent(jLabel4)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1)
                        .addGroup(inscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(formEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                            .addComponent(formNom, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(formPrenom, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(inscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(boutonJaiUnCompte)
                        .addComponent(formMdp, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        inscriptionLayout.setVerticalGroup(
            inscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inscriptionLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(creeruncompte, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(formNom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(formEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(formMdp, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(inscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boutonSenregistrer)
                    .addComponent(boutonJaiUnCompte))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        logErreurCo.setForeground(new java.awt.Color(255, 0, 0));

        logErreurCreation.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(connexion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(26, 26, 26))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(logErreurCo, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(logErreurCreation, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(inscription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(25, 25, 25))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(inscription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connexion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(logErreurCreation, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addComponent(logErreurCo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formPrenomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formPrenomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_formPrenomActionPerformed

    private void formNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formNomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_formNomActionPerformed

    private void boutonSenregistrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonSenregistrerActionPerformed

        Playfair mdp = new Playfair("playfair");

        String nom = formNom.getText().trim();
        String prenom = formPrenom.getText().trim();
        String email = formEmail.getText().trim();
        String password = formMdp.getText();

        password = mdp.cryptage(password);

        System.out.println("S'enregistrer");

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            logErreurCreation.setText("Veuillez remplir tous les champs.");

            return;
        }

        try {
            // insertion ds la bdd
            Bdd bdd = Bdd.getInstance();
            bdd.insertData(prenom, nom, email, password);
            System.out.println("Enregistrement réussi !");
            logErreurCreation.setForeground(GREEN);
            logErreurCreation.setText("Enregistrement réussi!");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'insertion des données : " + e.getMessage());
            logErreurCreation.setText("Erreur lors de l'insertion des données");

        }

    }//GEN-LAST:event_boutonSenregistrerActionPerformed

    private void boutonSeConnecterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonSeConnecterActionPerformed

        Playfair mdp = new Playfair("playfair");

        // TODO add your handling code here:
        String email = formEmailCo.getText();
        String password = formMdpCo.getText();

        password = mdp.cryptage(password);

        try {
            Bdd bdd = Bdd.getInstance();
            boolean estConnecte = bdd.connexionUtilisateur(email, password);
            if (estConnecte) {
                System.out.println("Connexion réussie !");
                // Démarrer le serveur

                Main ecran = new Main(email);
                ecran.setVisible(true); // Affichez l'interface principale
                this.dispose(); //Ferme la fenêtre de connexion actuelle
                // Affichez un message à l'utilisateur pour lui indiquer que la connexion est réussie
            } else {
                System.out.println("Échec de la connexion : Nom d'utilisateur ou mot de passe incorrect.");
                logErreurCo.setText("Échec : Nom d'utilisateur ou mot de passe incorrect.");
                // Affichez un message à l'utilisateur pour lui indiquer que la connexion a échoué
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
            logErreurCo.setText("Erreur lors de la connexion");
        }


    }//GEN-LAST:event_boutonSeConnecterActionPerformed


    private void formMdpCoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formMdpCoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_formMdpCoActionPerformed

    private void boutonPasDeCompteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonPasDeCompteActionPerformed

        formPrenom.requestFocusInWindow();
    }//GEN-LAST:event_boutonPasDeCompteActionPerformed

    private void boutonJaiUnCompteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonJaiUnCompteActionPerformed
        formEmailCo.requestFocusInWindow();
// TODO add your handling code here:
    }//GEN-LAST:event_boutonJaiUnCompteActionPerformed

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
            java.util.logging.Logger.getLogger(Inscription.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inscription.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inscription.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inscription.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inscription().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boutonJaiUnCompte;
    private javax.swing.JButton boutonPasDeCompte;
    private javax.swing.JButton boutonSeConnecter;
    private javax.swing.JButton boutonSenregistrer;
    private javax.swing.JPanel connexion;
    private javax.swing.JLabel creeruncompte;
    private javax.swing.JTextField formEmail;
    private javax.swing.JTextField formEmailCo;
    private javax.swing.JPasswordField formMdp;
    private javax.swing.JPasswordField formMdpCo;
    private javax.swing.JTextField formNom;
    private javax.swing.JTextField formPrenom;
    private javax.swing.JPanel inscription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel logErreurCo;
    private javax.swing.JLabel logErreurCreation;
    // End of variables declaration//GEN-END:variables
}
