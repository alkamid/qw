import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Spinner;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.LineStyle;
import org.eclipse.swt.events.*;


public class MyWindow {


	public Display display;
	public Shell shell;
	
	private Spinner setX;
	private Spinner[] setXBinary = new Spinner[4];
	double x;
	
	private Combo chooseBinary1, chooseBinary2, chooseBinary3, chooseBinary4;
	private String [] binaryOptions;
	
	private ILineSeries dataVBO, dataCBO;
	private MyPlot plotMain;
	
	private int sc;
	
	public MyWindow(){
		
		this.display = new Display();
		this.shell = new Shell(display);
		
		this.shell.setText("Plot");
		this.shell.setSize(980,615); this.shell.setLocation(20, 20); //setting window size and location
		this.shell.open();
		
		this.plotMain = new MyPlot(this.display, this.shell);
		
		this.setX = new Spinner(this.shell, SWT.NONE);
		this.setX.setDigits(2); // allow 2 decimal places
		this.setX.setMinimum(0); // set the minimum value to 0.00
		this.setX.setMaximum(100); // set the maximum value to 1.00
		this.setX.setIncrement(1); // set the increment value to 0.01
		this.setX.setSelection(50); // set the selection to 0.5
		this.setX.setBounds(40,500,50,20);
		
		this.binaryOptions = new String [] {"GaAs", "InAs", "GaSb", "InSb", "Custom"};

		
		this.chooseBinary1 = new Combo(this.shell, SWT.READ_ONLY);
		this.chooseBinary1.setItems(this.binaryOptions);
		this.chooseBinary1.setBounds(120,400,70,30);
		
		this.chooseBinary2 = new Combo(this.shell, SWT.READ_ONLY);
		this.chooseBinary2.setItems(this.binaryOptions);
		this.chooseBinary2.setBounds(200,400,70,30);
		
		this.chooseBinary3 = new Combo(this.shell, SWT.READ_ONLY);
		this.chooseBinary3.setItems(this.binaryOptions);
		this.chooseBinary3.setBounds(280,400,70,30);
		
		this.chooseBinary4 = new Combo(this.shell, SWT.READ_ONLY);
		this.chooseBinary4.setItems(this.binaryOptions);
		this.chooseBinary4.setBounds(360,400,70,30);
		
		int xpos = 120;
		/*
		sc = this.sc;
		for (this.sc=0; this.sc<4; sc++) {
			this.setXBinary[sc] = new Spinner(this.shell, SWT.NONE);
			binarySpinner(this.setXBinary[sc]);
			this.setXBinary[sc].setBounds(xpos, 440, 70, 20);
			xpos += 80;
			
			
			this.setXBinary[sc].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int selection = MyWindow.this.setXBinary[sc].getSelection();
					int digits = MyWindow.this.setXBinary[sc].getDigits();
					switch(sc) {
					case 0:	MyWindow.this.setXBinary[1].setSelection(100-selection);	break;
					case 1: MyWindow.this.setXBinary[0].setSelection(100-selection);	break;
					case 2: MyWindow.this.setXBinary[3].setSelection(100-selection);	break;
					case 3: MyWindow.this.setXBinary[2].setSelection(100-selection);	break;
					}
				}
			});
		}*/
		
		
		this.setX.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = MyWindow.this.setX.getSelection();
				int digits = MyWindow.this.setX.getDigits();
				System.out.println("Selection is "+(selection / Math.pow(10, digits)));
				MyWindow.this.plotMain.setComp(selection / Math.pow(10, digits));
				MyWindow.this.plotMain.replot();
			}
		});
		
		this.setXBinary[0].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = MyWindow.this.setXBinary[0].getSelection();
				int digits = MyWindow.this.setXBinary[0].getDigits();
				MyWindow.this.setXBinary[1].setSelection(100-selection);
			}
		});
		
		this.setXBinary[1].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = MyWindow.this.setXBinary[1].getSelection();
				int digits = MyWindow.this.setXBinary[1].getDigits();
				MyWindow.this.setXBinary[0].setSelection(100-selection);
			}
		});
		
		this.setXBinary[2].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = MyWindow.this.setXBinary[2].getSelection();
				int digits = MyWindow.this.setXBinary[2].getDigits();
				MyWindow.this.setXBinary[3].setSelection(100-selection);
			}
		});	
		
		this.setXBinary[3].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = MyWindow.this.setXBinary[3].getSelection();
				int digits = MyWindow.this.setXBinary[3].getDigits();
				MyWindow.this.setXBinary[2].setSelection(100-selection);
			}
		});
		
		
		while (!this.shell.isDisposed ()) { //petla dla obslugi srodowiska graficznego
		if (!this.display.readAndDispatch ()) this.display.sleep ();
		}
		this.display.dispose();
		
		//this.SpinnerParemeters()
		
		}
	
	//test
	
	private void binarySpinner(Spinner spinner) {
		spinner.setDigits(2); // allow 2 decimal places
		spinner.setMinimum(0); // set the minimum value to 0.00
		spinner.setMaximum(100); // set the maximum value to 1.00
		spinner.setIncrement(1); // set the increment value to 0.01
		spinner.setSelection(50); // set the selection to 0.5
	}

}