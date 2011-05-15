

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.LineStyle;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class MyPlot extends Chart {

	private int plotWidth, plotHeight; // plot dimensions
	private double xMin=0, xMax=1; // x range
	
	private Display display;
	private Shell shell;
	private MyWindow window;
	public CompoundBinary substrate;
	public CompoundBinary gasb, gaas, inas, insb;
	public CompoundTernary layer, ternary1, ternary2;
	public ILineSeries dataCBO, dataVBO, dataMargin;
	double x;
	
	public MyPlot(Display display,Shell shell, MyWindow mywindow) {

		super(shell,SWT.NONE);
		this.display = display;
		this.shell = shell;
		this.window = mywindow;
		
		this.substrate = new CompoundBinary(0);
		gasb = new CompoundBinary(2);
		gaas = new CompoundBinary(0);
		inas = new CompoundBinary(1);
		insb = new CompoundBinary(3);
		this.layer = new CompoundTernary(substrate, gasb, -1.06, 1.25);
		ternary1 = new CompoundTernary(gaas, inas);
		ternary2 = new CompoundTernary(gasb, insb);
		
		
		dataCBO = (ILineSeries) this.getSeriesSet().createSeries(SeriesType.LINE, "CBO");
		dataCBO.setXSeries(new double[]{0.1, 0.4, 0.4001, 0.8, 0.8001, 1.0});
		dataCBO.setYSeries(new double[]{substrate.VBO+substrate.bandgap, substrate.VBO+substrate.bandgap, layer.valueVBO(x)+layer.valueBandgap(x), layer.valueVBO(x)+layer.valueBandgap(x), substrate.VBO+substrate.bandgap, substrate.VBO+substrate.bandgap});
		dataCBO.setLineWidth(2);
		dataCBO.setLineColor(this.display.getSystemColor(SWT.COLOR_RED));
		dataCBO.setSymbolType(PlotSymbolType.NONE);
		
		System.out.println(this.getPlotArea().toControl(4, 4));
		x = 0.5;
		this.setSize(this.shell.getSize().x-40,400);
		this.dataVBO =(ILineSeries) this.getSeriesSet().createSeries(SeriesType.LINE, "VBO");
		dataVBO.setSymbolType(PlotSymbolType.NONE);
		dataVBO.setLineWidth(2);
		dataVBO.setXSeries(new double[]{0.1, 0.4, 0.4001, 0.8, 0.8001, 1.0});
		dataVBO.setYSeries(new double[]{substrate.VBO, substrate.VBO, layer.valueVBO(x), layer.valueVBO(x), substrate.VBO, substrate.VBO});
		this.getAxisSet().adjustRange();
		
		this.getLegend().setVisible(false);
		this.getTitle().setText("");
		this.getAxisSet().getXAxis(0).getTitle().setText("x [A]");
		this.getAxisSet().getYAxis(0).getTitle().setText("energy [eV]");
		this.replot();
	}
	
	void setComp(double x) {
		this.x = x;
	}
	
	void resetComp() {
		double Xbin1 = window.getComp(1) / Math.pow(10, 2);
		System.out.println(Xbin1);
		double Xbin3 = window.getComp(3) / Math.pow(10, 2);
		System.out.println(Xbin3);
		double Xter1 = window.getComp(5) / Math.pow(10, 2);
		System.out.println(Xter1);
		double qwVBO = (1-Xter1)*ternary1.valueVBO(Xbin1) + Xter1*ternary2.valueVBO(Xbin3);
		double qwBandgap = (1-Xter1)*ternary1.valueBandgap(Xbin1) + Xter1*ternary2.valueBandgap(Xbin3);
		double[] tableVBO = new double[]{substrate.VBO, substrate.VBO, qwVBO, qwVBO, substrate.VBO, substrate.VBO};
		double[] tableCBO = new double[]{substrate.VBO+substrate.bandgap, substrate.VBO+substrate.bandgap, qwVBO+qwBandgap, qwVBO+qwBandgap, substrate.VBO+substrate.bandgap, substrate.VBO+substrate.bandgap};
		dataVBO.setYSeries(tableVBO);
		dataCBO.setYSeries(tableCBO);
		margin(tableVBO, tableCBO);
	}
	
	void replot() {
		double[] tableVBO = new double[]{substrate.VBO, substrate.VBO, layer.valueVBO(x), layer.valueVBO(x), substrate.VBO, substrate.VBO};
		double[] tableCBO = new double[]{substrate.VBO+substrate.bandgap, substrate.VBO+substrate.bandgap, layer.valueVBO(x)+layer.valueBandgap(x), layer.valueVBO(x)+layer.valueBandgap(x), substrate.VBO+substrate.bandgap, substrate.VBO+substrate.bandgap};
		dataVBO.setYSeries(tableVBO);
		dataCBO.setYSeries(tableCBO);
		margin(tableVBO, tableCBO);
	}
	
	
	// setting the default margin as 0.1
	void margin(double[] tableVBO, double[] tableCBO) {
		margin(tableVBO, tableCBO, 0.1);
	}
	
	void margin(double[] tableVBO, double[] tableCBO, double marginValue) {
		ILineSeries dataMargin =(ILineSeries) this.getSeriesSet().createSeries(SeriesType.LINE, "margin");
		dataMargin.setXSeries(new double[]{0.5,0.5});
		dataMargin.setYSeries(new double[]{Math.min(tableVBO[0]-marginValue, tableVBO[3]-marginValue), Math.max(tableCBO[0]+marginValue, tableCBO[3]+marginValue)});
		dataMargin.setLineStyle(LineStyle.NONE);
		dataMargin.setSymbolType(PlotSymbolType.NONE);	
		this.getAxisSet().getYAxis(0).adjustRange();
		this.redraw();		
	}

}
