import java.awt.event.*;
import javax.swing.*;

public class Fenetre extends JFrame implements ActionListener {

	public Fenetre() {
		this.setLayout(null);
		
		JLabel label1 = new JLabel("URL du serveur :");
		label1.setBounds(30, 20, 200, 20);
		this.add(label1);
		
		JTextField input1 = new JTextField();
		input1.setBounds(30, 40, 300, 20);
		this.add(input1);
		
		JLabel label2 = new JLabel("Utilisateur :");
		label2.setBounds(30,100,200,20);
		this.add(label2);
		
		JTextField input2 = new JTextField();
		input2.setBounds(30, 120, 200, 20);
		this.add(input2);
	
		JLabel label3 = new JLabel("Mot de passe :");
		label3.setBounds(30,180,200,20);
		this.add(label3);
		
		JTextField input3 = new JTextField();
		input3.setBounds(30, 200, 200, 20);
		this.add(input3);
		
		JButton valider = new JButton("Se connecter");
		valider.setBounds(30, 250, 150, 30);
		this.add(valider);
		
		this.setTitle(" Connexion "); //Définit un titre pour notre fenêtre
		this.setSize(350, 350); //Définit sa taille : 400 pixels de large et 100 pixels de haut
		this.setLocationRelativeTo(null); //Nous demandons maintenant à notre objet de se positionner au centre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //Termine le processus lorsqu'on clique sur la croix rouge      
		this.setVisible(true); // la rendre visible     
		this.setResizable(false); // Empêcher le redimensionnement de la fenêtre
	}

	public static void main(String[] args) {

		Fenetre fen = new Fenetre();
	}

	public void actionPerformed(ActionEvent e) {
		
	}

}

