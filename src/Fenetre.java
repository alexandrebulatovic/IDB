import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Fenetre extends JFrame implements ActionListener {

	JButton valider;
	JTextField input1, input2, input3;

	public Fenetre() {
		this.setLayout(null); // On met le layout a NULL pour tout placement manuellement avec x,y

		/* CREATION DES LABELS ET DES TEXTFIELDS */
		JLabel label1 = new JLabel("URL du serveur :");
		label1.setBounds(30, 20, 200, 20);
		this.add(label1);

		input1 = new JTextField();
		input1.setBounds(30, 40, 300, 20);
		this.add(input1);
		input1.setText("jdbc:oracle:thin:@162.38.222.149:1521:IUT");

		JLabel label2 = new JLabel("Utilisateur :");
		label2.setBounds(30,100,200,20);
		this.add(label2);

		input2 = new JTextField();
		input2.setBounds(30, 120, 200, 20);
		this.add(input2);

		JLabel label3 = new JLabel("Mot de passe :");
		label3.setBounds(30,180,200,20);
		this.add(label3);

		input3 = new JTextField();
		input3.setBounds(30, 200, 200, 20);
		this.add(input3);

		/* CREATION DU BOUTON DE VALIDATION */
		valider = new JButton("Se connecter");
		valider.setBounds(30, 250, 150, 30);
		valider.setActionCommand("OK");
		valider.addActionListener(this); // on met un listener sur le bouton pour permettre l'interaction
		this.add(valider);

		this.setTitle(" Connexion "); //Définit un titre pour notre fenêtre
		this.setSize(350, 350); //Définit sa taille : 350 pixels de large et 350 pixels de haut
		this.setLocationRelativeTo(null); //Nous demandons maintenant à notre objet de se positionner au centre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //Termine le processus lorsqu'on clique sur la croix rouge      
		this.setVisible(true); // la rendre visible     
		this.setResizable(false); // Empêcher le redimensionnement de la fenêtre
	}

	public void actionPerformed(ActionEvent e) {

		String url, user, passwd;

		if(e.getSource() == valider ){ // en cas d'appui sur le bouton de connexion

			/* on recupere les variables */
			url = input1.getText();
			user = input2.getText();
			passwd = input3.getText();


			try { // on essaie de se connecter à la base oracle

				/* 
					necessite le fichier de driver ojdbc14.jar telechargeable sur oracle.com et
					à mettre dans C:\Program Files\Java\jre_X.XX\lib\ext\
				 */

				Class.forName("oracle.jdbc.OracleDriver"); 
				System.out.println("Driver OK"); // si le driver est OK

				Connection conn = DriverManager.getConnection(url, user, passwd);
				System.out.println("Connexion effective !"); // si la connexion est OK

				Statement stat = conn.createStatement();

				/* QUELQUES REQUETES POUR TESTER QUE TOUT FONCTIONNE */
				stat.executeUpdate("CREATE TABLE people (id int, prenom varchar(255))");
				stat.executeUpdate("INSERT INTO people(id,prenom) VALUES (1,'a')");
				stat.executeUpdate("INSERT INTO people(id,prenom) VALUES (2,'b')");
				stat.executeUpdate("INSERT INTO people(id,prenom) VALUES (3,'c')");

				/* ON VERIFIE QUE LES DONNEES AIENT ETE INSEREES EN LES AFFICHANT DANS LA CONSOLE */
				ResultSet rs = stat.executeQuery("SELECT * FROM people");  

				while(rs.next()) {
					System.out.println(rs.getInt(1)+"  "+rs.getString(2));  	
				}

				stat.executeUpdate("DROP TABLE people"); // on nettoie la bd

				/* ON FERME LA CONNEXION ET ON LIBERE LES RESSOURCES UTILISEES*/
				conn.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			} 
		}

	}

	public static void main(String[] args) {
		Fenetre fen = new Fenetre();
	}
}
