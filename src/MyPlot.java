

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
	public CompoundBinary substrate;
	public CompoundBinary gasb;
	public CompoundTernary layer;
	public ILineSeries dataCBO, dataVBO, dataMargin;
	double x;
	
	public MyPlot(Display display,Shell shell) {

		super(shell,SWT.NONE);
		this.display = display;
		this.shell = shell;
		
		this.substrate = new CompoundBinary("GaAs", 1.519, 0, -0.8, -7.17, -1.16, 1221, 566, -2.0);
		this.gasb = new CompoundBinary("GaSb", 0.812, 1, -0.03, -7.5, -0.8, 884.2, 402.6, -2.0);
		this.layer = new CompoundTernary(substrate, gasb, -1.06, 1.25);
		
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
