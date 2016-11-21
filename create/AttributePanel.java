package create;

import java.awt.FlowLayout;
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

public class AttributePanel 
extends JPanel implements ActionListener{
	private final int elementHeight = 20;
	
	private int elementWidth=150;
	
	private final int margin = 30;
	
	
	private JTextField tableNameField;
	
	private JTextField [] fields;
	
	private final int fieldNumber = 0;
	
	
	
	private JLabel tableNameLabel;
	
	private JLabel [] labels;
	
	private final int labelNumber = 0;
	
	
	
	private JButton attributButton;
	
	private JButton buttons[];
	
	private final int buttonNumber = 0;
	
	
	private AttributePanel attributePanel;
	
	private JPanel [] panels;
	
	private final int panelNumber = 0;
	
	
	private JComboBox attributeTypeComboBox;
	
	private JComboBox [] comboBox;
	
	private final int comboBoxNumber = 1;
	
	private JCheckBox notNullCheck;
	
	private JCheckBox uniqueCheck;
	
	private JCheckBox pkCheck;
	
	private JCheckBox fkCheck;
	
	private JCheckBox [] checkBoxs;
	
	private final int checkBoxNumber = 0;
	
	
	private Object[] types = new Object[]{"VARCHAR", "INT", "DATE", "CHAR"};
	
	
	public AttributePanel()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.handlePanels();
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
		this.addButtons();
	}
	

	
	// ==========================FIELDS========================
	private void createFields()
	{	
		this.fields = new JTextField [this.fieldNumber];
		
			
	}
	
	
	/**
	 * Positionne et dimensionne les boîtes de saisie.
	 */
	private void bindFields()
	{
	
		
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
	
		
	}
	
	
	private void bindLabels()
	{
	
		
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
	private void createPanels()
	{
		this.panels = new JPanel [this.panelNumber];
		
	}
	
	
	private void bindPanels()
	{
		
	}
	
	
	private void addPanels()
	{
		for (JPanel jl : this.panels) {
			this.add(jl);
		}
	}
	/**
	 * Instancie, positionne, dimensionne et associe
	 * les étiquettes.
	 */
	private void handlePanels()
	{
		this.createPanels();
		this.bindPanels();
		this.addPanels();
	}
	
	// ==========================COMBOBOX========================
	private void createComboBox()
	{
		this.comboBox = new JComboBox [this.comboBoxNumber];
		this.comboBox[0] = this.attributeTypeComboBox = new JComboBox(types);
		
	}
	
	
	private void bindComboBox()
	{
		//this.attributeTypeComboBox.setBounds(0, 20, this.elementWidth, this.elementHeight);
		
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
		this.checkBoxs = new JCheckBox [this.checkBoxNumber];
		
		
	}
	
	
	private void bindCheckBox()
	{
		
		
	}
	
	
	private void addCheckBox()
	{
		for (JCheckBox jl : this.checkBoxs) {
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
