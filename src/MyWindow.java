/* Copyright 2011 Adam Klimont a.k.a. alkamid
This file is part of Qw.

Qw is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Qw is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Qw.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.text.DecimalFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.*;


public class MyWindow {


	public Display display;
	public Shell shell;
	public Compounds compounds;
	
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
	private Spinner[] setWidth = new Spinner[2];
	private Spinner[] setTernaryBowingVBO = new Spinner[2];
	private Spinner[] setTernaryBowingE = new Spinner[2];
	double x;
	
	private Combo[] chooseBinary = new Combo[4];
	private Combo choosePsiE, choosePsiHH, choosePsiLH;
	private Combo chooseSubstrate;
	private String [] binaryOptions, substrateOptions;
	private Label [] compTab = new Label[4];
	private Button eigenvaluesButton;
	public Text output;
	private Label labelWidth, labelSearchMinimumStrainA, labelSearchMinimumStrainEg;
	private Label[] whichPsi = new Label[3];
	
	private MyPlot plotMain;
	
	private int sc;
	
	public MyWindow(){
		
		display = new Display();
		shell = new Shell(display);
		
		shell.setText("Plot");
		shell.setSize(980,800); this.shell.setLocation(20, 20); //setting window size and location
		shell.open();
		
		binaryOptions = new String [] {"GaAs", "InAs", "GaSb", "InSb", "Custom"};
		substrateOptions = new String [] {"GaAs", "InAs", "GaSb", "InSb", "InP", "Custom"};
		
		addLeftLabels();
		
		
		Label substrateLayer = new Label(shell, SWT.HORIZONTAL);
		substrateLayer.setBounds(450, 580, 70, 20);
		substrateLayer.setText("Substrate:");
		chooseSubstrate = new Combo(shell, SWT.READ_ONLY);
		chooseSubstrate.setItems(substrateOptions);
		chooseSubstrate.select(4);
		chooseSubstrate.setBounds(450, 600, 70, 20);
		
		chooseSubstrate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				compounds.setSubstrate(chooseSubstrate.getSelectionIndex());
				plotMain.replot();
			}
		});
		
		//setting spinners to control ternary composition and bowing parameters
		int xpos = 450;
		for (int i =0; i<2; i++) {
			setXTernary[i] = new Spinner(shell, SWT.NONE);
			XSpinner(setXTernary[i]);
			setXTernary[i].setBounds(xpos, 435, 70, 20);
			/*
			setTernaryBowingVBO[i] = new Spinner(shell, SWT.NONE);
			BowingSpinner(setTernaryBowingVBO[i]);
			setTernaryBowingVBO[i].setBounds(xpos, 470, 70, 20);			
			*/
			xpos += 70;
		}
		
		//setting spinners to control qw width [0] - barrier (one side), [1] - qw
		labelWidth = new Label(shell, SWT.HORIZONTAL);
		labelWidth.setBounds(460, 470, 120, 20);
		labelWidth.setText("barrier | well [nm]");
		xpos = 450;
		for (int i = 0; i<2; i++) {
			setWidth[i] = new Spinner(shell, SWT.NONE);
			WidthSpinner(setWidth[i]);
			setWidth[i].setBounds(xpos, 500, 70, 20);
			if (i == 1)
				setWidth[i].setSelection(65);
			else
				setWidth[i].setSelection(500);
			xpos += 70;
			
			//listener - when spinners are changed, recalculate the width
			setWidth[i].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					plotMain.resetWidth();
				}
			});
		}
		
		//"Calculate eigenvalues" button
		eigenvaluesButton = new Button (shell, SWT.PUSH);
		eigenvaluesButton.setText("Eigenvals");
		eigenvaluesButton.setBounds(600, 410, 80, 40);
		
		eigenvaluesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				plotMain.plotEigenvalues();
			}
		});
		
		
		
		labelSearchMinimumStrainA = new Label(shell, SWT.HORIZONTAL);
		labelSearchMinimumStrainA.setBounds(813, 455, 65, 20);
		labelSearchMinimumStrainA.setText("|a(l)-a(s)|");
		
		labelSearchMinimumStrainEg = new Label(shell, SWT.HORIZONTAL);
		labelSearchMinimumStrainEg.setBounds(890, 455, 40, 20);
		labelSearchMinimumStrainEg.setText("Eg <");
		
		final Spinner SearchMinimumStrainA = new Spinner(shell, SWT.NONE);
		StrainLatticeSpinner(SearchMinimumStrainA);
		SearchMinimumStrainA.setBounds(810,480,70,20);
		
		final Spinner SearchMinimumStrainEg = new Spinner(shell, SWT.NONE);
		EgSpinner(SearchMinimumStrainEg);
		SearchMinimumStrainEg.setBounds(880,480,70,20);
		SearchMinimumStrainEg.setSelection(8000);
		
		//"Calculate minimum strain" button
		eigenvaluesButton = new Button (shell, SWT.PUSH);
		eigenvaluesButton.setText("Search minimum strain");
		eigenvaluesButton.setBounds(600, 475, 200, 30);
		
		eigenvaluesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				compounds.searchMinimumStrain((double) SearchMinimumStrainA.getSelection()/Math.pow(10, SearchMinimumStrainA.getDigits()), (double) SearchMinimumStrainEg.getSelection()/Math.pow(10, SearchMinimumStrainEg.getDigits()));
			}
		});
		
		xpos = 720;
		for (int i=0; i<3; i++) {
			whichPsi[i] = new Label(shell, SWT.HORIZONTAL);
			whichPsi[i].setBounds(xpos, 400, 70, 20);
			xpos += 70;
		}
		whichPsi[0].setText("EE");
		whichPsi[1].setText("HH");
		whichPsi[2].setText("LH");
		
		
		//Choose which eigenvector to draw
		choosePsiE = new Combo(shell, SWT.READ_ONLY);
		choosePsiE.setItems(new String[]{});
		choosePsiE.setBounds(690, 420, 70, 20);
		
		choosePsiE.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				plotMain.plotVectorE(1, choosePsiE.getSelectionIndex());			
			}
		});
		
		choosePsiHH = new Combo(shell, SWT.READ_ONLY);
		choosePsiHH.setItems(new String[]{});
		choosePsiHH.setBounds(760, 420, 70, 20);
		
		choosePsiHH.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				plotMain.plotVectorE(2, choosePsiHH.getSelectionIndex());			
			}
		});
		
		choosePsiLH = new Combo(shell, SWT.READ_ONLY);
		choosePsiLH.setItems(new String[]{});
		choosePsiLH.setBounds(830, 420, 70, 20);
		
		choosePsiLH.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				plotMain.plotVectorE(3, choosePsiLH.getSelectionIndex());			
			}
		});
		
		// a loop for all binary spinners
		xpos = 120;
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
		}
		
		compounds = new Compounds(this);
		addTernaryLabels();
		xpos = 120;
		for (int sc=0; sc<4; sc++) {
			
			final int localSc = sc;
			setXBinary[localSc] = new Spinner(shell, SWT.NONE);
			XSpinner(setXBinary[localSc]);
			setXBinary[localSc].setBounds(xpos, 435, 70, 20);
			
			
			//loading config from a class (todo: config from a file)
			chooseBinary[localSc].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int selection = chooseBinary[localSc].getSelectionIndex();
					loadConf(localSc);
					compounds.setBinary(localSc, selection);
					plotMain.replot();
					plotMain.resetComp();
				}
			});

			chooseBinary[localSc].select(localSc);
			loadConf(sc);
			
			xpos += 80;
		}
		
		addOneLabels();
		
		for (sc=0; sc<4; sc++) {
			final int localSc = sc;
			setXBinary[localSc].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int selection = setXBinary[localSc].getSelection();
					switch(localSc) {
					case 0:	compounds.ternaries[0].setXbinary1((double) selection/100); setXBinary[1].setSelection(100-selection);	break;
					case 1: compounds.ternaries[0].setXbinary2((double) selection/100); setXBinary[0].setSelection(100-selection);	break;
					case 2: compounds.ternaries[1].setXbinary1((double) selection/100); setXBinary[3].setSelection(100-selection);	break;
					case 3: compounds.ternaries[1].setXbinary2((double) selection/100); setXBinary[2].setSelection(100-selection);	break;
					}
					refreshOneLabels(compTab);
					plotMain.resetComp();
					
				}
			});
		}
		
		for (int i=0; i<2; i++) {
			final int localI = i;
			setXTernary[i].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int selection = setXTernary[localI].getSelection();
					if (localI == 0) {
						compounds.layer.setXternary1((double) selection/100);
						setXTernary[1].setSelection(100-selection);
					}
					else {
						compounds.layer.setXternary2((double) selection/100);
						setXTernary[0].setSelection(100-selection);
					}
					plotMain.resetComp();
					refreshOneLabels(compTab);				
				}
			});
		}
		
		parameterListeners();
		
		plotMain = new MyPlot(display, shell, this, compounds);
		
		output = new Text (shell, SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		output.setBounds(600, 567, 370, 200);
		output.insert("...");
		
		while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose();
	
		
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
	
	private void WidthSpinner(Spinner spinner) {
		spinner.setDigits(1); // allow 1 decimal place
		spinner.setMinimum(1); // set the minimum value to 1.0
		spinner.setMaximum(100000); // set the maximum value to 10000
		spinner.setIncrement(1); // set the increment value to 0.0001
	}
	
	private void BowingSpinner(Spinner spinner) {
		spinner.setDigits(3); // allow 1 decimal place
		spinner.setMinimum(-5000); // set the minimum value to 1.0
		spinner.setMaximum(5000); // set the maximum value to 10000
		spinner.setIncrement(1); // set the increment value to 0.0001
	}
	
	private void StrainLatticeSpinner(Spinner spinner) {
		spinner.setDigits(5); // allow 1 decimal place
		spinner.setMinimum(0); // set the minimum value to 1.0
		spinner.setMaximum(50000); // set the maximum value to 10000
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

		String[] labelString = new String [] {compounds.layer.ternary1.label, compounds.layer.ternary2.label};
		Label labelsTab[] = new Label[2];
		int xpos = 450;
		for (int i = 0; i<2; i++) {
		labelsTab[i] = new Label(shell, SWT.HORIZONTAL);
		labelsTab[i].setBounds(xpos, 400, 70, 20);
		labelsTab[i].setText(labelString[i]);
		xpos += 70;
		}
	}
	
	private void addOneLabels() {
		int xpos = 450;

		for (int i = 0; i<4; i++) {
			compTab[i] = new Label(shell, SWT.HORIZONTAL);
			compTab[i].setBounds(xpos, 540, 39, 20);
			xpos += 39;
		}
		refreshOneLabels(compTab);
		
	}
	
	private void refreshOneLabels(Label compTab[]) {
		String[] labelString = new String [4];
		labelString[0] = Double.toString(roundTwoDecimals(((double) setXBinary[0].getSelection()/100* (double) setXTernary[0].getSelection()/100)+(double) setXBinary[2].getSelection()/100*(double) setXTernary[1].getSelection()/100));
		labelString[1] = "| " + Double.toString(roundTwoDecimals(((double) setXBinary[1].getSelection()/100* (double) setXTernary[0].getSelection()/100)+(double) setXBinary[3].getSelection()/100*(double) setXTernary[1].getSelection()/100));
		labelString[2] = "| " + Double.toString(roundTwoDecimals((double) setXTernary[0].getSelection()/100));
		labelString[3] = "| " + Double.toString(roundTwoDecimals((double) setXTernary[1].getSelection()/100));
		
		for (int i = 0; i<4; i++) {
			compTab[i].setText(labelString[i]);
		}
	}
	
	public double getComp(int which) {
		if (which >=0 && which < 4)
			return setXBinary[which].getSelection() / Math.pow(10, setXBinary[which].getDigits());
		else if (which == 5 || which == 6)
			return setXTernary[which-5].getSelection() / Math.pow(10, setXTernary[which-5].getDigits());
		else
			return 0;
	}
	
	public double getWidth(int which) {
		if (which >=0 && which < 2)
				return setWidth[which].getSelection() / Math.pow(10, setWidth[which].getDigits());
			else
				return 0;
	}
	
	private void loadConf(int localSc) {
		int egInt = (int) (compounds.binaries[localSc].getBandgap() * Math.pow(10, setEgBinary[localSc].getDigits()));
		int latticeInt = (int) (compounds.binaries[localSc].getLatticeConstant() * Math.pow(10, setLatticeBinary[localSc].getDigits()));
		int vboInt = (int) (compounds.binaries[localSc].getVBO() * Math.pow(10, setVBOBinary[localSc].getDigits()));
		int acInt = (int) (compounds.binaries[localSc].getAc() * Math.pow(10, setAcBinary[localSc].getDigits()));
		int avInt = (int) (compounds.binaries[localSc].getAv() * Math.pow(10, setAvBinary[localSc].getDigits()));
		int c11Int = (int) (compounds.binaries[localSc].getC11() * Math.pow(10, setC11Binary[localSc].getDigits()));
		int c12Int = (int) (compounds.binaries[localSc].getC12() * Math.pow(10, setC12Binary[localSc].getDigits()));
		int bInt = (int) (compounds.binaries[localSc].getDeformationPotential() * Math.pow(10, setBBinary[localSc].getDigits()));
		setEgBinary[localSc].setSelection(egInt);
		setLatticeBinary[localSc].setSelection(latticeInt);
		setVBOBinary[localSc].setSelection(vboInt);
		setAcBinary[localSc].setSelection(acInt);
		setAvBinary[localSc].setSelection(avInt);
		setC11Binary[localSc].setSelection(c11Int);
		setC12Binary[localSc].setSelection(c12Int);
		setBBinary[localSc].setSelection(bInt);
	}
	
	double roundTwoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
	return Double.valueOf(twoDForm.format(d));
	}
	
	public int getChosenBinary(int which) {
		return chooseBinary[which].getSelectionIndex();
	}
	
	public int getChosenSubstrate() {
		return chooseSubstrate.getSelectionIndex();
	}
	
	private void resetSpinner(Spinner spinner) {
		spinner.setSelection(5000);
	}
	
	public void setNumEigenvectorsE(int n) {
		String[] options = new String [n];
		for (int i=0; i<n; i++)
			options[i] = Integer.toString(i);
		choosePsiE.setItems(options);
	}
	public void setNumEigenvectorsHH(int n) {
		String[] options = new String [n];
		for (int i=0; i<n; i++)
			options[i] = Integer.toString(i);
		choosePsiHH.setItems(options);
	}
	public void setNumEigenvectorsLH(int n) {
		String[] options = new String [n];
		for (int i=0; i<n; i++)
			options[i] = Integer.toString(i);
		choosePsiLH.setItems(options);
	}
	
	private void parameterListeners() {
		final Spinner[][] container = new Spinner[][] {setEgBinary, setLatticeBinary, setVBOBinary, setAcBinary, setAvBinary, setC11Binary, setC12Binary, setBBinary}; 
		for (int j=0; j<8; j++) {
			for (int sc=0; sc<4; sc++) {
				final int localJ = j;
				final int localSc = sc;
				container[j][localSc].addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						int selection = container[localJ][localSc].getSelection();
						int digits = container[localJ][localSc].getDigits();
						double value = (double) selection / Math.pow(10, digits);
						switch (localJ) {
						case 0: compounds.binaries[localSc].setBandgap(value); break;
						case 1: compounds.binaries[localSc].setLatticeConstant(value); break;
						case 2: compounds.binaries[localSc].setVBO(value); break;
						case 3: compounds.binaries[localSc].setAc(value); break;
						case 4: compounds.binaries[localSc].setAv(value); break;
						case 5: compounds.binaries[localSc].setC11(value); break;
						case 6: compounds.binaries[localSc].setC12(value); break;
						case 7: compounds.binaries[localSc].setDeformationPotential(value); break;
						}
						plotMain.resetComp();
					}
				});
			}
		}
	}
	
}