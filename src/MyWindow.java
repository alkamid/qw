import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Label;
import org.swtchart.ILineSeries;
import org.eclipse.swt.events.*;


public class MyWindow {


	public Display display;
	public Shell shell;
	
	private Spinner setX;
	private Spinner[] setXBinary = new Spinner[4];
	private Spinner[] setEgBinary = new Spinner[4];
	private Spinner[] setLatticeBinary = new Spinner[4];
	private Spinner[] setVBOBinary = new Spinner[4];
	private Spinner[] setAcBinary = new Spinner[4];
	private Spinner[] setAvBinary = new Spinner[4];
	private Spinner[] setC11Binary = new Spinner[4];
	private Spinner[] setC12Binary = new Spinner[4];
	private Spinner[] setBBinary = new Spinner[4];
	private Spinner[] setXTernary = new Spinner[4];
	double x;
	
	private Combo[] chooseBinary = new Combo[4];
	private Combo chooseSubstrate;
	private String [] binaryOptions, substrateOptions;
	private Label labelNotChosen;
	
	private ILineSeries dataVBO, dataCBO;
	private MyPlot plotMain;
	
	private int sc;
	
	public MyWindow(){
		
		this.display = new Display();
		this.shell = new Shell(display);
		
		this.shell.setText("Plot");
		this.shell.setSize(980,800); this.shell.setLocation(20, 20); //setting window size and location
		this.shell.open();
		
		this.plotMain = new MyPlot(this.display, this.shell, this);
		
		setX = new Spinner(this.shell, SWT.NONE);
		setX.setDigits(2); // allow 2 decimal places
		setX.setMinimum(0); // set the minimum value to 0.00
		setX.setMaximum(100); // set the maximum value to 1.00
		setX.setIncrement(1); // set the increment value to 0.01
		setX.setSelection(50); // set the selection to 0.5
		setX.setBounds(500,500,50,20);
		
		binaryOptions = new String [] {"GaAs", "InAs", "GaSb", "InSb", "Custom"};
		substrateOptions = new String [] {"InP", "Custom"};
		
		addLeftLabels();
		addTernaryLabels();
		
		chooseSubstrate = new Combo(shell, SWT.READ_ONLY);
		chooseSubstrate.setItems(substrateOptions);
		chooseSubstrate.setBounds(500, 400, 70, 20);
		
		setXTernary[0] = new Spinner(shell, SWT.NONE);
		XSpinner(setXTernary[0]);
		setXTernary[0].setBounds(570, 435, 70, 20);
		
		setXTernary[1] = new Spinner(shell, SWT.NONE);
		XSpinner(setXTernary[1]);
		setXTernary[1].setBounds(640, 435, 70, 20);
		

		setXTernary[0].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = setXTernary[0].getSelection();
				int digits = setXTernary[0].getDigits();
				setXTernary[1].setSelection(100-selection);
				plotMain.resetComp();
			}
		});
		
		setXTernary[1].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = setXTernary[1].getSelection();
				int digits = setXTernary[1].getDigits();
				setXTernary[0].setSelection(100-selection);
				plotMain.resetComp();
			}
		});
		
		
		
		int xpos = 120;
		
		
		for (sc=0; sc<4; sc++) {
			//creating choice lists for binary compounds
			chooseBinary[sc] = new Combo(shell, SWT.READ_ONLY);
			chooseBinary[sc].setItems(binaryOptions);
			chooseBinary[sc].setBounds(xpos, 400, 70, 20);
			
			//creating spinners for bandgap
			setEgBinary[sc] = new Spinner(shell, SWT.NONE);
			EgSpinner(setEgBinary[sc]);
			setEgBinary[sc].setBounds(xpos, 470, 70, 20);

			//creating spinners for lattice constant
			setLatticeBinary[sc] = new Spinner(shell, SWT.NONE);
			LatticeSpinner(setLatticeBinary[sc]);
			setLatticeBinary[sc].setBounds(xpos, 505, 70, 20);
			
			//creating spinners for VBO
			setVBOBinary[sc] = new Spinner(shell, SWT.NONE);
			VBOSpinner(setVBOBinary[sc]);
			setVBOBinary[sc].setBounds(xpos, 540, 70, 20);
			
			//creating spinners for ac
			setAcBinary[sc] = new Spinner(shell, SWT.NONE);
			ASpinner(setAcBinary[sc]);
			setAcBinary[sc].setBounds(xpos, 575, 70, 20);
			
			//creating spinners for av
			setAvBinary[sc] = new Spinner(shell, SWT.NONE);
			ASpinner(setAvBinary[sc]);
			setAvBinary[sc].setBounds(xpos, 610, 70, 20);
			
			//creating spinners for c11
			setC11Binary[sc] = new Spinner(shell, SWT.NONE);
			CSpinner(setC11Binary[sc]);
			setC11Binary[sc].setBounds(xpos, 645, 70, 20);
			
			//creating spinners for c12
			setC12Binary[sc] = new Spinner(shell, SWT.NONE);
			CSpinner(setC12Binary[sc]);
			setC12Binary[sc].setBounds(xpos, 680, 70, 20);
			
			//creating spinners for deformation potential
			setBBinary[sc] = new Spinner(shell, SWT.NONE);
			BSpinner(setBBinary[sc]);
			setBBinary[sc].setBounds(xpos, 715, 70, 20);
			
			xpos += 80;
			
			final int localXpos = xpos;
			final int localSc = sc;
			//loading config from a class (todo: config from a file)
			chooseBinary[localSc].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int selection = chooseBinary[localSc].getSelectionIndex();
					CompoundBinary binary = new CompoundBinary(selection);
					int egInt = (int) (binary.bandgap*10000);
					int latticeInt = (int) (binary.latticeConstant*100000);
					int vboInt = (int) (binary.VBO*10000);
					int acInt = (int) (binary.ac*100);
					int avInt = (int) (binary.av*100);
					int c11Int = (int) (binary.c11*10);
					int c12Int = (int) (binary.c12*10);
					int bInt = (int) (binary.deformationPotential*100);
					setEgBinary[localSc].setSelection(egInt);
					setLatticeBinary[localSc].setSelection(latticeInt);
					setVBOBinary[localSc].setSelection(vboInt);
					setAcBinary[localSc].setSelection(acInt);
					setAvBinary[localSc].setSelection(avInt);
					setC11Binary[localSc].setSelection(c11Int);
					setC12Binary[localSc].setSelection(c12Int);
					setBBinary[localSc].setSelection(bInt);
					
				}
			});
			
			//creating spinners for choosing the composition of each binary compound only if all of them were selected
			if (chooseBinary[0].getSelectionIndex() != -1 && chooseBinary[1].getSelectionIndex() != -1 && chooseBinary[2].getSelectionIndex() != -1 && chooseBinary[3].getSelectionIndex() != -1) {
			setXBinary[localSc] = new Spinner(shell, SWT.NONE);
			XSpinner(setXBinary[localSc]);
			setXBinary[localSc].setBounds(localXpos, 435, 70, 20);
			
			setXBinary[localSc].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int selection = setXBinary[localSc].getSelection();
					int digits = setXBinary[localSc].getDigits();
					switch(localSc) {
					case 0:	setXBinary[1].setSelection(100-selection);	break;
					case 1: setXBinary[0].setSelection(100-selection);	break;
					case 2: setXBinary[3].setSelection(100-selection);	break;
					case 3: setXBinary[2].setSelection(100-selection);	break;
					}
					plotMain.resetComp();
				}
			});
			}
			
		}
		
		
		if (chooseBinary[0].getSelectionIndex() == -1 || chooseBinary[1].getSelectionIndex() == -1 || chooseBinary[2].getSelectionIndex() == -1 || chooseBinary[3].getSelectionIndex() == -1) {
			labelNotChosen = new Label(shell, SWT.HORIZONTAL);
			labelNotChosen.setBounds(120, 435, 350, 20);
			labelNotChosen.setText("Choose all binary compounds to set their composition");
		}
		else {
			
		}
		
		this.setX.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = MyWindow.this.setX.getSelection();
				int digits = MyWindow.this.setX.getDigits();
				System.out.println("Selection is "+(selection / Math.pow(10, digits)));
				MyWindow.this.plotMain.setComp(selection / Math.pow(10, digits));
				MyWindow.this.plotMain.replot();
			}
		});
		
		
		while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose();
		
		//this.SpinnerParemeters()
		
		}

	//setting spinner parameters for choosing the composition
	private void XSpinner(Spinner spinner) {
		spinner.setDigits(2); // allow 2 decimal places
		spinner.setMinimum(0); // set the minimum value to 0.00
		spinner.setMaximum(100); // set the maximum value to 1.00
		spinner.setIncrement(1); // set the increment value to 0.01
		spinner.setSelection(50); // set the selection to 0.5
	}
	
	private void EgSpinner(Spinner spinner) {
		spinner.setDigits(4); // allow 4 decimal places
		spinner.setMinimum(0); // set the minimum value to 0.0000
		spinner.setMaximum(200000); // set the maximum value to 20.0000
		spinner.setIncrement(1); // set the increment value to 0.0001
	}

	private void LatticeSpinner(Spinner spinner) {
		spinner.setDigits(5); // allow 4 decimal places
		spinner.setMinimum(0); // set the minimum value to -20.0000
		spinner.setMaximum(1000000); // set the maximum value to 20.0000
		spinner.setIncrement(1); // set the increment value to 0.0001
	}
	
	private void VBOSpinner(Spinner spinner) {
		spinner.setDigits(4); // allow 4 decimal places
		spinner.setMinimum(-200000); // set the minimum value to -20.0000
		spinner.setMaximum(200000); // set the maximum value to 20.0000
		spinner.setIncrement(1); // set the increment value to 0.0001
	}
	
	private void ASpinner(Spinner spinner) {
		spinner.setDigits(2); // allow 4 decimal places
		spinner.setMinimum(-10000); // set the minimum value to -20.0000
		spinner.setMaximum(10000); // set the maximum value to 20.0000
		spinner.setIncrement(1); // set the increment value to 0.0001
	}
	
	private void CSpinner(Spinner spinner) {
		spinner.setDigits(1); // allow 4 decimal places
		spinner.setMinimum(0); // set the minimum value to -20.0000
		spinner.setMaximum(100000); // set the maximum value to 20.0000
		spinner.setIncrement(1); // set the increment value to 0.0001
	}
	
	private void BSpinner(Spinner spinner) {
		spinner.setDigits(2); // allow 4 decimal places
		spinner.setMinimum(-1000); // set the minimum value to -20.0000
		spinner.setMaximum(1000); // set the maximum value to 20.0000
		spinner.setIncrement(1); // set the increment value to 0.0001
	}
	
	//a method to add labels (leftmost column: Eg, a, VBO etc.)
	private void addLeftLabels() {

		String[] labelString = new String [] {"x", "Eg", "a", "VBO", "ac", "av", "c11", "c12", "b"};
		Label labelsTab[] = new Label[9];
		int ypos = 435;
		for (int i = 0; i<9; i++) {
		labelsTab[i] = new Label(shell, SWT.HORIZONTAL);
		labelsTab[i].setBounds(50, ypos, 70, 20);
		labelsTab[i].setText(labelString[i]);
		ypos += 35;
		}
	}
	
	//create labels in the center (ternary1 and ternary2)
	private void addTernaryLabels() {

		String[] labelString = new String [] {"Ternary1", "Ternary2"};
		Label labelsTab[] = new Label[2];
		int xpos = 570;
		for (int i = 0; i<2; i++) {
		labelsTab[i] = new Label(shell, SWT.HORIZONTAL);
		labelsTab[i].setBounds(xpos, 400, 70, 20);
		labelsTab[i].setText(labelString[i]);
		xpos += 70;
		}
	}
	
	public double getComp(int which) {
		if (which >0 && which < 5) {
		return setXBinary[which].getSelection();
	}
		else if (which == 5) {
			return setXTernary[0].getSelection();
		}
		else if (which == 6) {
			return setXTernary[1].getSelection();
		}
		else {
			return 0;
		}
	}
}