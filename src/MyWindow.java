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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.*;


public class MyWindow {


	public Display display;
	public Shell shell;
	public Compounds compounds;
	
	private Spinner[] setXBinary = new Spinner[5];
	private Spinner[] setEgBinary = new Spinner[5];
	private Spinner[] setLatticeBinary = new Spinner[5];
	private Spinner[] setVBOBinary = new Spinner[5];
	private Spinner[] setAcBinary = new Spinner[5];
	private Spinner[] setAvBinary = new Spinner[5];
	private Spinner[] setC11Binary = new Spinner[5];
	private Spinner[] setC12Binary = new Spinner[5];
	private Spinner[] setBBinary = new Spinner[5];
	private Spinner[] setEMassBinary = new Spinner[5];
	private Spinner[] setHHMassBinary = new Spinner[5];
	private Spinner[] setLHMassBinary = new Spinner[5];	
	private Spinner[] setXTernary = new Spinner[2];
	private Spinner[] setWidth = new Spinner[2];
	private Spinner[] setTernaryBowingVBO = new Spinner[2];
	private Spinner[] setTernaryBowingE = new Spinner[2];
	double x;
	
	private Combo[] chooseBinary = new Combo[5];
	private Combo choosePsiE, choosePsiHH, choosePsiLH;
	private String [] binaryOptions;
	private Label [] compTab = new Label[5];
	private Button eigenvaluesButton;
	public Text output;
	private Label labelWidth, labelSearchMinimumStrainA, labelSearchMinimumStrainEg;
	private Label[] whichPsi = new Label[3];
	private Composite compositeBasic, compositeMaterial;
	private Label ternaryLabels[] = new Label[2];
	private int xwidth;
	
	
	private MyPlot plotMain;
	
	private int sc;
	
	public MyWindow(){
		
		display = new Display();
		shell = new Shell(display);
		
		shell.setText("qw");
		shell.setSize(1000,750); this.shell.setLocation(20, 20); //setting window size and location
		shell.open();
		
		binaryOptions = new String [] {"GaAs", "InAs", "GaSb", "InSb", "InP", "Custom"};		
		
		final TabFolder tabFolder = new TabFolder (shell, SWT.BORDER);
		tabFolder.setLocation(15,350);
		TabItem item = new TabItem (tabFolder, SWT.NONE);
		item.setText ("Basic");
		TabItem item1 = new TabItem (tabFolder, SWT.NONE);
		item1.setText ("Material");
		compositeBasic = new Composite (tabFolder, SWT.NONE);
		compositeBasic.setSize(400, 450);
		compositeMaterial = new Composite(tabFolder, SWT.NONE);
		item.setControl(compositeBasic);
		item1.setControl(compositeMaterial);

		addLeftLabels();

		
		Label substrateLayer = new Label(compositeBasic, SWT.HORIZONTAL);
		substrateLayer.setBounds(360, 33, 70, 20);
		substrateLayer.setText("(substrate)");
		
		//setting spinners to control ternary composition and bowing parameters
		int xpos = 80;
		for (int i =0; i<2; i++) {
			setXTernary[i] = new Spinner(compositeBasic, SWT.NONE);
			XSpinner(setXTernary[i]);
			setXTernary[i].setBounds(xpos, 175, 70, 20);
			/*
			setTernaryBowingVBO[i] = new Spinner(shell, SWT.NONE);
			BowingSpinner(setTernaryBowingVBO[i]);
			setTernaryBowingVBO[i].setBounds(xpos, 470, 70, 20);			
			*/
			xpos += 160;
		}
		
		//setting spinners to control qw width [0] - barrier (one side), [1] - qw
		labelWidth = new Label(compositeBasic, SWT.HORIZONTAL);
		labelWidth.setBounds(15, 210, 160, 20);
		labelWidth.setText("barrier [nm] | well [nm]");
		xpos = 15;
		for (int i = 0; i<2; i++) {
			setWidth[i] = new Spinner(compositeBasic, SWT.NONE);
			WidthSpinner(setWidth[i]);
			setWidth[i].setBounds(xpos, 230, 70, 20);
			if (i == 1)
				setWidth[i].setSelection(65);
			else
				setWidth[i].setSelection(500);
			xpos += 90;
			
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
		eigenvaluesButton.setBounds(600, 360, 80, 40);
		
		eigenvaluesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				plotMain.plotEigenvalues();
			}
		});
		
		
		
		labelSearchMinimumStrainA = new Label(shell, SWT.HORIZONTAL);
		labelSearchMinimumStrainA.setBounds(813, 405, 65, 20);
		labelSearchMinimumStrainA.setText("|a(l)-a(s)|");
		
		labelSearchMinimumStrainEg = new Label(shell, SWT.HORIZONTAL);
		labelSearchMinimumStrainEg.setBounds(890, 405, 40, 20);
		labelSearchMinimumStrainEg.setText("Eg <");
		
		final Spinner SearchMinimumStrainA = new Spinner(shell, SWT.NONE);
		StrainLatticeSpinner(SearchMinimumStrainA);
		SearchMinimumStrainA.setBounds(810,430,70,20);
		
		final Spinner SearchMinimumStrainEg = new Spinner(shell, SWT.NONE);
		EgSpinner(SearchMinimumStrainEg);
		SearchMinimumStrainEg.setBounds(880,430,70,20);
		SearchMinimumStrainEg.setSelection(8000);
		
		//"Calculate minimum strain" button
		eigenvaluesButton = new Button (shell, SWT.PUSH);
		eigenvaluesButton.setText("Search minimum strain");
		eigenvaluesButton.setBounds(600, 425, 200, 30);
		
		eigenvaluesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				compounds.searchMinimumStrain((double) SearchMinimumStrainA.getSelection()/Math.pow(10, SearchMinimumStrainA.getDigits()), (double) SearchMinimumStrainEg.getSelection()/Math.pow(10, SearchMinimumStrainEg.getDigits()));
			}
		});
		
		xpos = 720;
		for (int i=0; i<3; i++) {
			whichPsi[i] = new Label(shell, SWT.HORIZONTAL);
			whichPsi[i].setBounds(xpos, 350, 70, 20);
			xpos += 70;
		}
		whichPsi[0].setText("EE");
		whichPsi[1].setText("HH");
		whichPsi[2].setText("LH");
		
		
		//Choose which eigenvector to draw
		choosePsiE = new Combo(shell, SWT.READ_ONLY);
		choosePsiE.setItems(new String[]{});
		choosePsiE.setBounds(690, 370, 70, 20);
		
		choosePsiE.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				plotMain.plotVectorE(1, choosePsiE.getSelectionIndex());			
			}
		});
		
		choosePsiHH = new Combo(shell, SWT.READ_ONLY);
		choosePsiHH.setItems(new String[]{});
		choosePsiHH.setBounds(760, 370, 70, 20);
		
		choosePsiHH.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				plotMain.plotVectorE(2, choosePsiHH.getSelectionIndex());			
			}
		});
		
		choosePsiLH = new Combo(shell, SWT.READ_ONLY);
		choosePsiLH.setItems(new String[]{});
		choosePsiLH.setBounds(830, 370, 70, 20);
		
		choosePsiLH.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				plotMain.plotVectorE(3, choosePsiLH.getSelectionIndex());			
			}
		});
		
		// a loop for all binary spinners
		xpos = 40;
		xwidth = 70;
		for (sc=0; sc<5; sc++) {
			//creating choice lists for binary compounds
			chooseBinary[sc] = new Combo(compositeBasic, SWT.READ_ONLY);
			chooseBinary[sc].setItems(binaryOptions);
			chooseBinary[sc].setBounds(xpos, 0, xwidth, 20);
			
			//creating spinners for bandgap
			setEgBinary[sc] = new Spinner(compositeBasic, SWT.NONE);
			EgSpinner(setEgBinary[sc]);
			setEgBinary[sc].setBounds(xpos, 60, xwidth, 20);

			//creating spinners for lattice constant
			setLatticeBinary[sc] = new Spinner(compositeBasic, SWT.NONE);
			LatticeSpinner(setLatticeBinary[sc]);
			setLatticeBinary[sc].setBounds(xpos, 90, xwidth, 20);
			
			//creating spinners for VBO
			setVBOBinary[sc] = new Spinner(compositeBasic, SWT.NONE);
			VBOSpinner(setVBOBinary[sc]);
			setVBOBinary[sc].setBounds(xpos, 120, xwidth, 20);
			
			//creating spinners for ac
			setAcBinary[sc] = new Spinner(compositeMaterial, SWT.NONE);
			ASpinner(setAcBinary[sc]);
			setAcBinary[sc].setBounds(xpos, 30, xwidth, 20);
			
			//creating spinners for av
			setAvBinary[sc] = new Spinner(compositeMaterial, SWT.NONE);
			ASpinner(setAvBinary[sc]);
			setAvBinary[sc].setBounds(xpos, 60, xwidth, 20);
			
			//creating spinners for c11
			setC11Binary[sc] = new Spinner(compositeMaterial, SWT.NONE);
			CSpinner(setC11Binary[sc]);
			setC11Binary[sc].setBounds(xpos, 90, xwidth, 20);
			
			//creating spinners for c12
			setC12Binary[sc] = new Spinner(compositeMaterial, SWT.NONE);
			CSpinner(setC12Binary[sc]);
			setC12Binary[sc].setBounds(xpos, 120, xwidth, 20);
			
			//creating spinners for deformation potential
			setBBinary[sc] = new Spinner(compositeMaterial, SWT.NONE);
			BSpinner(setBBinary[sc]);
			setBBinary[sc].setBounds(xpos, 150, xwidth, 20);
			
			//creating spinners for electron effective mass
			setEMassBinary[sc] = new Spinner(compositeMaterial, SWT.NONE);
			MassSpinner(setEMassBinary[sc]);
			setEMassBinary[sc].setBounds(xpos, 180, xwidth, 20);
			
			//creating spinners for heavy hole effective mass
			setHHMassBinary[sc] = new Spinner(compositeMaterial, SWT.NONE);
			MassSpinner(setHHMassBinary[sc]);
			setHHMassBinary[sc].setBounds(xpos, 210, xwidth, 20);
			
			//creating spinners for light hole effective mass
			setLHMassBinary[sc] = new Spinner(compositeMaterial, SWT.NONE);
			MassSpinner(setLHMassBinary[sc]);
			setLHMassBinary[sc].setBounds(xpos, 240, xwidth, 20);
			
			xpos += 80;
		}
		
		compounds = new Compounds(this);
		addTernaryLabels();
		xpos = 40;
		for (int sc=0; sc<4; sc++) {
			
			final int localSc = sc;
			setXBinary[localSc] = new Spinner(compositeBasic, SWT.NONE);
			XSpinner(setXBinary[localSc]);
			setXBinary[localSc].setBounds(xpos, 30, 70, 20);
			
			
			//loading config from a class (todo: config from a file)
			chooseBinary[localSc].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int selection = chooseBinary[localSc].getSelectionIndex();
					loadConf(0, localSc);
					compounds.setBinary(localSc, selection);
					ternaryLabels[0].setText(compounds.ternaries[0].getLabel());
					ternaryLabels[1].setText(compounds.ternaries[1].getLabel());
					plotMain.replot();
					plotMain.resetComp();
				}
			});

			chooseBinary[localSc].select(localSc);
			loadConf(0, sc);
			
			xpos += 80;
		}
		
		
		chooseBinary[4].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = chooseBinary[4].getSelectionIndex();
				compounds.setSubstrate(selection);
				loadConf(1, 4);
				plotMain.replot();
			}
		});
		chooseBinary[4].select(4);
		loadConf(1, 4);
		
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
		output.setBounds(600, 517, 370, 200);
		output.insert("...");
		
		tabFolder.pack();
		
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
	
	private void MassSpinner(Spinner spinner) {
		spinner.setDigits(3); // allow 1 decimal place
		spinner.setMinimum(0); // set the minimum value to 1.0
		spinner.setMaximum(5000); // set the maximum value to 10000
		spinner.setIncrement(1); // set the increment value to 0.0001
	}
	
	//a method to add labels (leftmost column: Eg, a, VBO etc.)
	private void addLeftLabels() {

		String[] labelString = new String [] {"x", "Eg", "a", "VBO", "ac", "av", "c11", "c12", "b", "m(e)", "m(hh)", "m(lh)"};
		Label labelsTab[] = new Label[12];
		int ypos = 30;
		xwidth = 40;
		for (int i = 0; i<12; i++) {
			if (i<4) {
				labelsTab[i] = new Label(compositeBasic, SWT.HORIZONTAL);
				labelsTab[i].setBounds(0, ypos, xwidth, 20);
			}
			else {
				labelsTab[i] = new Label(compositeMaterial, SWT.HORIZONTAL);
				labelsTab[i].setBounds(0, ypos-120, xwidth, 20);
			}
			labelsTab[i].setText(labelString[i]);
			ypos += 30;
		}
	}
	
	//create labels in the center (ternary1 and ternary2)
	private void addTernaryLabels() {

		String[] labelString = new String [] {compounds.ternaries[0].getLabel(), compounds.ternaries[1].getLabel()};
		int xpos = 90;
		for (int i = 0; i<2; i++) {
			ternaryLabels[i] = new Label(compositeBasic, SWT.HORIZONTAL);
			ternaryLabels[i].setBounds(xpos, 150, 70, 20);
			ternaryLabels[i].setText(labelString[i]);
			xpos += 160;
		}
	}
	
	private void addOneLabels() {
		int xpos = 270;

		for (int i = 0; i<4; i++) {
			compTab[i] = new Label(compositeBasic, SWT.HORIZONTAL);
			compTab[i].setBounds(xpos, 230, 39, 20);
			xpos += 39;
		}
		refreshOneLabels(compTab);
		
	}
	
	private void refreshOneLabels(Label compTab[]) {
		String[] labelString = new String [4];
		labelString[0] = Double.toString(round(((double) setXBinary[0].getSelection()/100* (double) setXTernary[0].getSelection()/100)+(double) setXBinary[2].getSelection()/100*(double) setXTernary[1].getSelection()/100,2));
		labelString[1] = "| " + Double.toString(round(((double) setXBinary[1].getSelection()/100* (double) setXTernary[0].getSelection()/100)+(double) setXBinary[3].getSelection()/100*(double) setXTernary[1].getSelection()/100,2));
		labelString[2] = "| " + Double.toString(round((double) setXTernary[0].getSelection()/100,2));
		labelString[3] = "| " + Double.toString(round((double) setXTernary[1].getSelection()/100,2));
		
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
	
	private void loadConf(int binOrSubs, int localSc) {
		CompoundBinary temp;
		if (binOrSubs == 0)
			temp = compounds.binaries[localSc];
		else
			temp = compounds.substrate;
		
		int egInt = (int) (temp.getBandgap() * Math.pow(10, setEgBinary[localSc].getDigits()));
		int latticeInt = (int) (temp.getLatticeConstant() * Math.pow(10, setLatticeBinary[localSc].getDigits()));
		int vboInt = (int) (temp.getVBO() * Math.pow(10, setVBOBinary[localSc].getDigits()));
		int acInt = (int) (temp.getAc() * Math.pow(10, setAcBinary[localSc].getDigits()));
		int avInt = (int) (temp.getAv() * Math.pow(10, setAvBinary[localSc].getDigits()));
		int c11Int = (int) (temp.getC11() * Math.pow(10, setC11Binary[localSc].getDigits()));
		int c12Int = (int) (temp.getC12() * Math.pow(10, setC12Binary[localSc].getDigits()));
		int bInt = (int) (temp.getDeformationPotential() * Math.pow(10, setBBinary[localSc].getDigits()));
		int eMassInt = (int) (temp.getEmass() * Math.pow(10, setEMassBinary[localSc].getDigits()));
		int hhMassInt = (int) (temp.getHHmass() * Math.pow(10, setHHMassBinary[localSc].getDigits()));
		int lhMassInt = (int) (temp.getLHmass() * Math.pow(10, setLHMassBinary[localSc].getDigits()));
		setEgBinary[localSc].setSelection(egInt);
		setLatticeBinary[localSc].setSelection(latticeInt);
		setVBOBinary[localSc].setSelection(vboInt);
		setAcBinary[localSc].setSelection(acInt);
		setAvBinary[localSc].setSelection(avInt);
		setC11Binary[localSc].setSelection(c11Int);
		setC12Binary[localSc].setSelection(c12Int);
		setBBinary[localSc].setSelection(bInt);
		setEMassBinary[localSc].setSelection(eMassInt);
		setHHMassBinary[localSc].setSelection(hhMassInt);
		setLHMassBinary[localSc].setSelection(lhMassInt);
	}
	
	double round(double d, int i) {
		return (double)((int) (d * Math.pow(10,i)))/Math.pow(10, i);
	}
	
	public int getChosenBinary(int which) {
		return chooseBinary[which].getSelectionIndex();
	}
	
	public int getChosenSubstrate() {
		return chooseBinary[4].getSelectionIndex();
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
		final Spinner[][] container = new Spinner[][] {setEgBinary, setLatticeBinary, setVBOBinary, setAcBinary, setAvBinary, setC11Binary, setC12Binary, setBBinary, setEMassBinary, setHHMassBinary, setLHMassBinary}; 
		for (int j=0; j<11; j++) {
			for (int sc=0; sc<5; sc++) {
				final int localJ = j;
				final int localSc = sc;
				final CompoundBinary temp;
				if (sc == 4)
					temp = compounds.substrate;
				else
					temp = compounds.binaries[localSc];
				container[j][localSc].addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						int selection = container[localJ][localSc].getSelection();
						int digits = container[localJ][localSc].getDigits();
						double value = (double) selection / Math.pow(10, digits);
						switch (localJ) {
						case 0: temp.setBandgap(value); break;
						case 1: temp.setLatticeConstant(value); break;
						case 2: temp.setVBO(value); break;
						case 3: temp.setAc(value); break;
						case 4: temp.setAv(value); break;
						case 5: temp.setC11(value); break;
						case 6: temp.setC12(value); break;
						case 7: temp.setDeformationPotential(value); break;
						case 8: temp.setEmass(value); break;
						case 9: temp.setHhmass(value); break;
						case 10: temp.setLhmass(value); break;
						}
						plotMain.resetComp();
					}
				});
			}
		}
	}
	
}