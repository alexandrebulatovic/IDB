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

	public class AttributesPanel
	extends JPanel implements ActionListener{
		
		private final int elementHeight = 20;
			
		private int elementWidth=150;
		
		private final int margin = 30;
		

		
		private JTextField [] fields;
		
		private final int fieldNumber = 0;
		
		
		
		
		private JLabel [] labels;
		
		private final int labelNumber = 0;
		
			
		
		private JButton buttons[];
		
		private final int buttonNumber = 0;
		
		
		
		private AttributePanel attributePanel;
		
		private JPanel [] panels;
		
		private final int panelNumber = 1;
		
		
		
		
		private JComboBox [] comboBoxs;
		
		private final int comboBoxNumber = 0;
		
		
		private JCheckBox [] checkBoxs;
		
		private final int checkBoxNumber = 0;

		public AttributesPanel()
		{
			this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			this.handlePanels();
			this.handleFields();
			this.handleLabels();
			this.handleButtons();
			this.handleCheckBox();
			this.handleComboBox();
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
			this.panels[0] = this.attributePanel = new AttributePanel();
			
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
			this.comboBoxs = new JComboBox [this.comboBoxNumber];
			
		}
		
		
		private void bindComboBox()
		{
			
			
		}
		
		
		private void addComboBox()
		{
			for (JComboBox jl : this.comboBoxs) {
				this.add(jl);
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
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

}

