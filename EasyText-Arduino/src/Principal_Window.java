import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Highlighter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;


public class Principal_Window implements KeyListener{

	private JFrame frmEditorDeTexto;
	JTextPane textPanel;
	public Help help = new Help();
	StringBuffer current_text;
	public File ficheroSeleccionado;
	public boolean saved;
	public boolean fullscreen;
	Highlighter highlighter;
	DefaultStyledDocument document;
	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	/*Java reserved words*/
	public static int l=0;
	public String[] lenguajes = {
			//Java
			"(abstract|continue|for|new|switch|assert|default|goto|package|synchronized|boolean|"
			+ "do|if|private|this|break|double|implements|protected|throw|byte|else|import|public|throws|"
			+ "case|enum|instanceof|return|transient|catch|extends|int|short|try|char|final|interface|"
			+ "static|void|class|finally|long|strictfp|volatile|const|float|native|super|while)"
			,//ANSI C
			"(auto|double|int|struct|break|else|long|switch|case|enum|register|typedef|char|extern|return|union|const|"
			+ "float|short|unsigned|continue|for|signed|void|default|goto|sizeof|volatile|do|if|static|while)"
			,//Python
			"(and|assert|break|class|continue|def|del|elif|else|except|exec|finally|for|from|global|if|import"
			+ "|in|is|lambda|not|or|pass|print|raise|return|try|while)"
			,
			"C++"
			,
			"Java Script"
			,
			"HTML"
			,
			"ruby"
			,
			"Perl"
			,
			};
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal_Window window = new Principal_Window();
					window.frmEditorDeTexto.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Principal_Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		/**/

		setLookAndFeel();
		fullscreen=false;
		ficheroSeleccionado = null;
		current_text = new StringBuffer();
		frmEditorDeTexto = new JFrame();
		frmEditorDeTexto.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frmEditorDeTexto.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
		frmEditorDeTexto.setTitle("easyText {}");
		frmEditorDeTexto.setForeground(new Color(0, 0, 0));
		frmEditorDeTexto.setBounds(100, 100, 1000, 750);
		frmEditorDeTexto.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		frmEditorDeTexto.getContentPane().add(scrollPane, BorderLayout.CENTER);

		//****//
		
		final StyleContext cont = StyleContext.getDefaultStyleContext();
		final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0xFF9A00));
		final AttributeSet attrYellow = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.YELLOW);
		final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0xBDF400));
		

		document = new DefaultStyledDocument() {
			
				/*Insert a string*/
	            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
	                super.insertString(offset, str, a);

	                String text = getText(0, getLength());
	                int before = findLastNonWordChar(text, offset);
	                if (before < 0) before = 0;
	                int after = findFirstNonWordChar(text, offset + str.length());
	                int wordL = before;
	                int wordR = before;

	                while (wordR <= after) {
	                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
	                        if (text.substring(wordL, wordR).matches("(\\W)*"+lenguajes[l])){
	                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
	                        }else{
	                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
	                        }
	                        wordL = wordR;
	                    }
	                    wordR++;
	                }
	            }
	            /*remove a string*/
	            public void remove (int offs, int len) throws BadLocationException {
	                super.remove(offs, len);

	                String text = getText(0, getLength());
	                int before = findLastNonWordChar(text, offs);
	                if (before < 0) before = 0;
	                int after = findFirstNonWordChar(text, offs);

	                if (text.substring(before, after).matches("(\\W)*"+lenguajes[l])) {
	                    setCharacterAttributes(before, after - before, attr, false);
	                } else {
	                    setCharacterAttributes(before, after - before, attrBlack, false);
	                }
	            }
	        };
	    /* */
		textPanel = new JTextPane(document);
		textPanel.setCaretColor(Color.white);
		textPanel.setFont(new Font("Arial", Font.PLAIN, 15));
		textPanel.setText("//Wellcome to easyText {}");
		textPanel.addKeyListener(this);
//		textPanel.setForeground(new Color(74,96,0)); /*Color de la fuente*/
		textPanel.setBackground(new Color(0x161311)); /*Color de fondo*/
		scrollPane.setViewportView(textPanel);
		}
	@Override
	public String toString() {
		String s="";
		try {
			 s = document.getText(0, document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return s;
	}

	public String numTabs(int caret){
		StringBuffer s = new StringBuffer();
		//Count the numbers of tabs between the caret and the first \n
		while(true){
			try {
				String cad = document.getText(--caret, 1);
				if(cad.equalsIgnoreCase("\t")){
					s.append("\t");
				}else if(cad.equalsIgnoreCase("\n")){
					break;
				}
			} catch (BadLocationException e) {
				break;
			}
		}
		if(s.length()!=0){
			return s.toString();
		}else{
			return "";
		}
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		
		saved=false;
		try{
			highlighter.removeAllHighlights();
		}catch(NullPointerException e){
			//Do nothing
		}
		if(arg0.getKeyCode()== KeyEvent.VK_ENTER){
			int p = textPanel.getCaretPosition();
			String numtabs = numTabs(p);
			//*Search the first non blank character*/
			try{
				if(document.getText(p, 1).equalsIgnoreCase("}") & document.getText(p-1,1).equalsIgnoreCase("{")){
					arg0.consume();
					document.insertString(p, "\n"+numtabs+"\t\n"+numtabs, null);
					if(!numtabs.equalsIgnoreCase("")){
						textPanel.setCaretPosition(p+(numtabs.length())+2);
					}else{
						textPanel.setCaretPosition(p+2);
					}
				}
			}catch(BadLocationException e){
				System.out.println("Badlocationexception");
			}
		}
		/*Reserved characters*/
		if(arg0.getKeyCode() == 129){/* { */
			try {
				arg0.consume();
				document.insertString(textPanel.getCaretPosition(), "{}", null);
				textPanel.setCaretPosition(textPanel.getCaretPosition()-1);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		if(arg0.getKeyCode() == 222){/* ' */
			try {
				arg0.consume();
				document.insertString(textPanel.getCaretPosition(), "''", null);
				textPanel.setCaretPosition(document.getLength()-1);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		if(arg0.getKeyCode() == 56){/* ( */
			try {
				arg0.consume();
				document.insertString(textPanel.getCaretPosition(), "()", null);
				textPanel.setCaretPosition(document.getLength()-1);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		if(arg0.getKeyCode() == 128){/* [ */
			try {
				arg0.consume();
				document.insertString(textPanel.getCaretPosition(), "[]", null);
				textPanel.setCaretPosition(document.getLength()-1);
			} catch (BadLocationException e) {
				
				e.printStackTrace();
			}
		}
		if(arg0.getKeyCode() == 50){/* " */
			try {
				arg0.consume();
				document.insertString(textPanel.getCaretPosition(), "\"\"", null);
				textPanel.setCaretPosition(document.getLength()-1);
			} catch (BadLocationException e) {
				
				e.printStackTrace();
			}
		}
		
		/*CTRL PRESSED*/
		if((arg0.getModifiers() & KeyEvent.CTRL_MASK)!=0){
			if(arg0.getKeyCode() == KeyEvent.VK_Z){
				undoTyping();
			}
			if((arg0.getKeyCode() == KeyEvent.VK_H)){
				helpMenu();
			}
			if((arg0.getKeyCode() == KeyEvent.VK_U)){
				upload();
			}
			if((arg0.getKeyCode() == KeyEvent.VK_B)){
				verify();
			}
			if((arg0.getKeyCode() == KeyEvent.VK_S)){
				guardar();
			}
			if((arg0.getKeyCode() == KeyEvent.VK_D)){
				guardarComo();
			}
			if((arg0.getKeyCode() == KeyEvent.VK_L)){
				cargarFichero();
			}
			if((arg0.getKeyCode() == KeyEvent.VK_N)){
				completeScreen();
			}
			if((arg0.getKeyCode() == KeyEvent.VK_F)){
				try {
					buscar();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void helpMenu() {
		help.setVisible(true);

	}

	private void undoTyping() {
		

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		arg0.consume();

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
	/*Turns fullScreen and normalScreen*/
	protected void completeScreen(){
		if(fullscreen==false){
			device.setFullScreenWindow(frmEditorDeTexto);
			fullscreen =true;
		}else{
			fullscreen=false;
			device.setFullScreenWindow(null);
		}
	}
	//*Index the firts and las words*//
	private int findLastNonWordChar (String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar (String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}
	
	protected void cargarFichero(){
		if(ficheroSeleccionado!=null && !saved){
			int selection = JOptionPane.showOptionDialog(
					null, 
					"El documento actual no se ha guardado.\n"
							+ "¿Quiere guardarlo?", 
							"Aviso", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.WARNING_MESSAGE, 
							new ImageIcon("icon.jpg"), 
							null, 
							null);
			if(selection==JOptionPane.YES_OPTION){
				guardar();
			}
		}

		JFileChooser fileChooser = new JFileChooser(".");
		int status = fileChooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION) {
			ficheroSeleccionado = fileChooser.getSelectedFile();
			/*Leemos fichero selecionado*/
			StringBuffer str_readed = new StringBuffer();
			try {
				String str;
				BufferedReader s = new BufferedReader(new FileReader (ficheroSeleccionado));
				while((str = s.readLine())!=null){
					str_readed.append(str+"\n");
				}
				textPanel.setText(str_readed.toString());
				s.close();

			} catch (Exception e1) {
				/*Error de solapacion o de formato*/
				JOptionPane.showMessageDialog(null, "Error en la lectura");
			}
		}

	}
	protected void guardar() {
		if(ficheroSeleccionado==null){
			guardarComo();
		}else{
			escribirFichero();
		}
		saved=true;
	}

	private void escribirFichero() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(ficheroSeleccionado));
			pw.print(textPanel.getText());
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void verify() {
		File f = new File("temp/temp.ino"); 
		try{
		f.createNewFile();
		
		ficheroSeleccionado=f;
		guardar();
		Process p = Runtime.getRuntime().exec(("arduino temp/temp.ino --verify --port /dev/ttyACM0 "));
		}
		 catch ( IOException e){
			System.out.println("Error verifying the program to the arduino");
		 }
	}

	protected void upload() {
		verify();	
		try{
		Process p = Runtime.getRuntime().exec(("arduino ./temp/temp.ino --upload --port /dev/ttyACM0 "));
		} catch  ( IOException e){
			System.out.println("Error uploadling the program to the arduino");
		}
	}
	
	protected static void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (InstantiationException e) {
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		}
	}
	protected void guardarComo() {
		JFileChooser jfile = new JFileChooser(".");
		jfile.setApproveButtonText("Guardar");
		int status = jfile.showSaveDialog(null);	// Save dialog instead of open dialog
		String ruta = ""; 
		try{ 
			if(status == JFileChooser.APPROVE_OPTION){ 
				ruta = jfile.getSelectedFile().getAbsolutePath(); 
				/*Escribir sobre el fichero selecionado*/
				File f = new File(ruta); 
				if(f.exists()){
					int selection = JOptionPane.showOptionDialog(
							null, 
							"Ya existe un fichero con ese nombre,\n¿Desea remplazarlo?", 
							"Aviso", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.WARNING_MESSAGE, 
							null, 
							null, 
							null);
					if(selection==JOptionPane.YES_OPTION){
						ficheroSeleccionado=f;
						escribirFichero();
					}
				}else{
					ficheroSeleccionado=f;
					escribirFichero();
				}
			}
		}catch (Exception ex){ 
			ex.printStackTrace(); 
		} 
		saved=true;
	}
	/**
	 * Must correct this method.
	 */
	protected void buscar() throws BadLocationException{
		String word = JOptionPane.showInputDialog(null);
		Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.gray);
		highlighter = textPanel.getHighlighter();

		int firstOffset = -1;
		if (word == null || word.equals("")) {
			return;
		}

		word = word.toLowerCase();
		int lastIndex = 0;
		int wordSize = word.length();

		while ((lastIndex = textPanel.getText().indexOf(word, lastIndex)) != -1) {
			int endIndex = lastIndex + wordSize;
			try {
				highlighter.addHighlight(lastIndex, endIndex, painter);
			} catch (BadLocationException e) {
				// Nothing to do
			}
			if (firstOffset == -1) {
				firstOffset = lastIndex;
			}
			lastIndex = endIndex;
		}
	}

}
