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
import java.util.Arrays;
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
	private Spinner[] setWidth = new Spinner[4];
	private Spinner[] setTernaryBowingVBO = new Spinner[4];
	private Spinner[] setTernaryBowingE = new Spinner[4];
	double x;
	
	private Combo[] chooseBinary = new Combo[4];
	private Combo chooseSubstrate;
	private String [] binaryOptions, substrateOptions;
	private Label [] compTab = new Label[4];
	
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
		addTernaryLabels();
		
		chooseSubstrate = new Combo(shell, SWT.READ_ONLY);
		chooseSubstrate.setItems(substrateOptions);
		chooseSubstrate.select(4);
		chooseSubstrate.setBounds(640, 400, 70, 20);
		
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
			
			setTernaryBowingVBO[i] = new Spinner(shell, SWT.NONE);
			BowingSpinner(setTernaryBowingVBO[i]);
			setTernaryBowingVBO[i].setBounds(xpos, 470, 70, 20);			
			
			xpos += 70;
		}
		
		//setting spinners to control qw width [0] - left barrier, [1] - qw, [2] - right barrier
		xpos = 600;
		for (int i = 0; i<3; i++) {
			setWidth[i] = new Spinner(shell, SWT.NONE);
			WidthSpinner(setWidth[i]);
			setWidth[i].setBounds(xpos, 470, 70, 20);
			if (i == 1)
				setWidth[i].setSelection(150);
			else
				setWidth[i].setSelection(2000);
			xpos += 70;
			
			//listener - when spinners are changed, recalculate the width
			setWidth[i].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					plotMain.resetWidth();
				}
			});
		}
		
		
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
					case 0:	setXBinary[1].setSelection(100-selection);	break;
					case 1: setXBinary[0].setSelection(100-selection);	break;
					case 2: setXBinary[3].setSelection(100-selection);	break;
					case 3: setXBinary[2].setSelection(100-selection);	break;
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
					if (localI == 0)
						setXTernary[1].setSelection(100-selection);
					else
						setXTernary[0].setSelection(100-selection);
					plotMain.resetComp();
					refreshOneLabels(compTab);				
				}
			});
		}		
		
		
		plotMain = new MyPlot(display, shell, this, compounds);
	
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
		int xpos = 450;
		for (int i = 0; i<2; i++) {
		labelsTab[i] = new Label(shell, SWT.HORIZONTAL);
		labelsTab[i].setBounds(xpos, 400, 70, 20);
		labelsTab[i].setText(labelString[i]);
		xpos += 70;
		}
	}
	
	private void addOneLabels() {
		int ypos = 550;

		for (int i = 0; i<4; i++) {
			compTab[i] = new Label(shell, SWT.HORIZONTAL);
			compTab[i].setBounds(450, ypos, 70, 20);
			ypos += 50;
		}
		refreshOneLabels(compTab);
		
	}
	
	private void refreshOneLabels(Label compTab[]) {
		String[] labelString = new String [4];
		labelString[0] = Double.toString(roundTwoDecimals(((double) setXBinary[0].getSelection()/100* (double) setXTernary[0].getSelection()/100)+(double) setXBinary[2].getSelection()/100*(double) setXTernary[1].getSelection()/100));
		labelString[1] = Double.toString(roundTwoDecimals(((double) setXBinary[1].getSelection()/100* (double) setXTernary[0].getSelection()/100)+(double) setXBinary[3].getSelection()/100*(double) setXTernary[1].getSelection()/100));
		labelString[2] = Double.toString(roundTwoDecimals((double) setXTernary[0].getSelection()/100));
		labelString[3] = Double.toString(roundTwoDecimals((double) setXTernary[1].getSelection()/100));
		
		for (int i = 0; i<4; i++) {
			compTab[i].setText(labelString[i]);
		}
	}
	
	public double getComp(int which) {
		if (which >0 && which < 5)
			return setXBinary[which].getSelection() / Math.pow(10, setXBinary[which].getDigits());
		else if (which == 5 || which == 6)
			return setXTernary[which-5].getSelection() / Math.pow(10, setXBinary[which-5].getDigits());
		else
			return 0;
	}
	
	public double getWidth(int which) {
		if (which >=0 && which < 3) {
			if (which == 1)
				return setWidth[which].getSelection() / Math.pow(10, setXBinary[which].getDigits()-1); //layer's width is multiplied by 10 to make it visible
			else
				return setWidth[which].getSelection() / Math.pow(10, setXBinary[which].getDigits());
		}
		else
			return 0;
	}
	
	private void loadConf(int localSc) {
		int egInt = (int) (compounds.binaries[localSc].getBandgap() * Math.pow(10, setXBinary[localSc].getDigits()));
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
	
}