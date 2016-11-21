package create;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TableNamePanel
extends JPanel implements ActionListener{
	
	private final int elementHeight = 20;
	
	private int elementWidth=150;
	
	private final int margin = 30;
	
	
	private JTextField tableNameField;
	
	private JTextField [] fields;
	
	private final int fieldNumber = 1;
	
	
	
	private JLabel tableNameLabel;
	
	private JLabel [] labels;
	
	private final int labelNumber = 1;
	
	
	
	private JButton attributButton;
	
	private JButton buttons[];
	
	private final int buttonNumber = 1;
	
	
	private AttributesPanel attributesPanel;
	
	private TableNamePanel tableNamePanel;
	
	private JPanel [] panels;
	
	private final int panelNumber = 0;
	
	
	private JComboBox attributeTypeComboBox;
	
	private JComboBox [] comboBox;
	
	private final int comboBoxNumber = 0;
	


	
	private JCheckBox [] checkBoxs;
	
	private final int checkBoxNumber = 0;

	
	
	public TableNamePanel()
	{
		this.setLayout(null);
		//this.handlePanels();
		this.handleComboBox();
		this.handleFields();
		this.handleLabels();
		this.handleButtons();
		this.handleCheckBox();
		
	}
	
	
	// ==========================BUTTONS========================
	private void createButtons()
	{
		this.buttons = new JButton [this.buttonNumber];
		
	}
	
	/**
	 * Positionne et dimensionne les boutons.
	 */
	private void bindButtons()
	{
	}
	
	/**
	 * Associe les boutons à this.
	 */
	private void addButtons()
	{
		for (JButton jb : this.buttons) {
			jb.addActionListener(this);
			this.add(jb);
		}
	}
	
	private void handleButtons()
	{
		this.createButtons();
		this.bindButtons();
		//this.addButtons();
	}
	

	
	// ==========================FIELDS========================
	private void createFields()
	{	
		this.fields = new JTextField [this.fieldNumber];
		this.fields[0] = this.tableNameField = new JTextField();
			
	}
	
	
	/**
	 * Positionne et dimensionne les boîtes de saisie.
	 */
	private void bindFields()
	{
		this.tableNameField.setBounds(152, 20, this.elementWidth, this.elementHeight);
		
	}
	
	
	/**
	 * Associe les boîtes de saisie à l'IHM.
	 */
	private void addFields()
	{
		for (JTextField jtf : this.fields) {
			this.add(jtf);
		}
	}
	
	
	private void handleFields()
	{
		this.createFields();
		this.bindFields();
		this.addFields();
	}
	
	// ==========================LABEL========================
	private void createLabels()
	{
		this.labels = new JLabel [this.labelNumber];
		this.labels[0] = this.tableNameLabel = new JLabel("Nom de la table :");
		
	}
	
	
	private void bindLabels()
	{
		int foot = (int)(0.25*this.fieldNumber*this.elementHeight);
		foot*=2;
		int jump = 0;//et ici le textBox
		
		for(final JLabel label : this.labels){
			label.setBounds(this.margin, jump, this.elementWidth, this.elementHeight);
			jump+=foot;				
		}
	}
	
	
	private void addLabels()
	{
		for (JLabel jl : this.labels) {
			this.add(jl);
		}
	}
	/**
	 * Instancie, positionne, dimensionne et associe
	 * les étiquettes.
	 */
	private void handleLabels()
	{
		this.createLabels();
		this.bindLabels();
		this.addLabels();
	}
	
	// ==========================PANELS========================
	/*private void createPanels()
	{
		//this.panels = new JPanel[this.panelNumber];
		
	}
	
	
	private void bindPanels()
	{
		
		
	}
	
	
	private void addPanels()
	{
		for (JLabel jl : this.labels) {
			this.add(jl);
		}
	}
	
	 * Instancie, positionne, dimensionne et associe
	 * les étiquettes.
	 
	private void handlePanels()
	{
		this.createPanels();
		this.bindPanels();
		this.addPanels();
	}*/
	
	// ==========================COMBOBOX========================
	private void createComboBox()
	{
		this.comboBox = new JComboBox [this.comboBoxNumber];
		
	}
	
	
	private void bindComboBox()
	{
		
	}
	
	
	private void addComboBox()
	{
		for (JComboBox jc : this.comboBox) {
			this.add(jc);
		}
	}
	/**
	 * Instancie, positionne, dimensionne et associe
	 * les étiquettes.
	 */
	private void handleComboBox()
	{
		this.createComboBox();
		this.bindComboBox();
		this.addComboBox();
	}
	
	// ==========================CHECKBOX========================
	
	private void createCheckBox()
	{
		this.checkBoxs = new JCheckBox [this.comboBoxNumber];
		
	}
	
	
	private void bindCheckBox()
	{
		
	}
	
	
	private void addCheckBox()
	{
		for (JComboBox jl : this.comboBox) {
			this.add(jl);
		}
	}
	/**
	 * Instancie, positionne, dimensionne et associe
	 * les étiquettes.
	 */
	private void handleCheckBox()
	{
		this.createComboBox();
		this.bindComboBox();
		this.addComboBox();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}