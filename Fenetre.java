import java.awt.event.*;
import javax.swing.*;

public class Fenetre extends JFrame implements ActionListener {

	JButton valider;
	JTextField input1, input2, input3;

	public Fenetre() {
		this.setLayout(null);

		JLabel label1 = new JLabel("URL du serveur :");
		label1.setBounds(30, 20, 200, 20);
		this.add(label1);

		input1 = new JTextField();
		input1.setBounds(30, 40, 300, 20);
		this.add(input1);

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

		valider = new JButton("Se connecter");
		valider.setBounds(30, 250, 150, 30);
		valider.setActionCommand("OK");
		valider.addActionListener(this);
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

		if(e.getSource() == valider ){
			url = input1.getText();
			user = input2.getText();
			passwd = input3.getText();
			
			System.out.println(url);
			System.out.println(user);
			System.out.println(passwd);
		}

	}

	public static void main(String[] args) {
		Fenetre fen = new Fenetre();
	}
}

