import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Help extends JDialog {

	/**
	 * Create the dialog.
	 */
	public Help() {
		Principal_Window.setLookAndFeel();
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				setVisible(false);
			}
		});
		
		setBounds(100,100,500,200);
		JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(new Color(255, 255, 255));
		menuBar.setBackground(new Color(255, 255, 255));
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Languaje");
		mnNewMenu.setForeground(new Color(0, 0, 0));
		mnNewMenu.setBackground(new Color(0, 0, 0));
		menuBar.add(mnNewMenu);
		
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource()instanceof JMenuItem){
					int val=0;
					JMenuItem btn = (JMenuItem)arg0.getSource();
					switch(btn.getText()){
					case "Java":  val=0; break;
					case "Ansi-C": val=1; break;
					case "Python": val=2; break;
					case "C++": val=3; break;
					case "JavaScript": val=4; break;
					}
					Principal_Window.l=val;
				}
			}
		};
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Java");
		mntmNewMenuItem.addActionListener(al);
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmAnsic = new JMenuItem("Ansi-C");
		mnNewMenu.add(mntmAnsic);
		mntmAnsic.addActionListener(al);
		
		JMenuItem mntmPython = new JMenuItem("Python");
		mnNewMenu.add(mntmPython);
		mntmPython.addActionListener(al);
		
		JMenuItem mntmJavascript = new JMenuItem("JavaScript");
		mnNewMenu.add(mntmJavascript);
		mntmJavascript.addActionListener(al);
		
		JMenuItem mntmC = new JMenuItem("C++");
		mnNewMenu.add(mntmC);
		mntmC.addActionListener(al);
		/*More lenguajes will be included here*/
		
		JMenu mnNewMenu_1 = new JMenu("Options");
		mnNewMenu_1.setForeground(new Color(0, 0, 0));
		menuBar.add(mnNewMenu_1);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		getContentPane().add(panel, BorderLayout.CENTER);
		
	}
}