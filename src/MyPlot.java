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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.LineStyle;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class MyPlot extends Chart {
	
	private Display display;
	private Shell shell;
	private MyWindow window;
	private Compounds compounds;
	/*
	public CompoundBinary substrate;
	public CompoundBinary[] binaries = new CompoundBinary[4];
	public CompoundTernary[] ternaries = new CompoundTernary[2];
	public CompoundQuaternary layer;
	*/
	public ILineSeries dataCBO, dataVBO, dataMargin;
	
	public MyPlot(Display newdisplay,Shell newshell, MyWindow newmywindow, Compounds newcompounds) {

		super(newshell,SWT.NONE);
		display = newdisplay;
		shell = newshell;
		window = newmywindow;
		compounds = newcompounds;
		
		dataCBO = (ILineSeries) getSeriesSet().createSeries(SeriesType.LINE, "CBO");
		dataVBO =(ILineSeries) getSeriesSet().createSeries(SeriesType.LINE, "VBO");
		dataCBO.setLineWidth(2);
		dataVBO.setLineWidth(2);	
		dataCBO.setLineColor(display.getSystemColor(SWT.COLOR_RED));
		dataCBO.setSymbolType(PlotSymbolType.NONE);
		dataVBO.setSymbolType(PlotSymbolType.NONE);
		
		setSize(shell.getSize().x-40,400);

		
		getLegend().setVisible(false);
		getTitle().setText("");
		getAxisSet().getXAxis(0).getTitle().setText("x [A]");
		getAxisSet().getYAxis(0).getTitle().setText("energy [eV]");
		resetComp();
		resetWidth();
	}
	
	void resetComp() {
		double Xbin1 = window.getComp(1);
		double Xbin3 = window.getComp(3);
		double Xter1 = window.getComp(5);
		compounds.ternaries[0].setXbinary1(Xbin1);
		compounds.ternaries[1].setXbinary1(Xbin3);
		compounds.layer.setXternary1(Xter1);
		replot();
	}
	
	void replot() {
		double[] tableVBO = new double[]{compounds.substrate.VBO, compounds.substrate.VBO, compounds.layer.VBO(), compounds.layer.VBO(), compounds.substrate.VBO, compounds.substrate.VBO};
		double[] tableCBO = new double[]{compounds.substrate.VBO+compounds.substrate.bandgap, compounds.substrate.VBO+compounds.substrate.bandgap, compounds.layer.VBO()+compounds.layer.bandgap(), compounds.layer.VBO()+compounds.layer.bandgap(), compounds.substrate.VBO+compounds.substrate.bandgap, compounds.substrate.VBO+compounds.substrate.bandgap};
		dataVBO.setYSeries(tableVBO);
		dataCBO.setYSeries(tableCBO);
		margin(tableVBO, tableCBO);
	}
	
	void resetWidth() {
		double widthLeft = window.getWidth(0);
		double widthQw = window.getWidth(1);
		double widthRight = window.getWidth(2);
		double sum = widthLeft + widthQw + widthRight;
		dataVBO.setXSeries(new double[]{0.0, widthLeft/sum, widthLeft/sum, (widthLeft+widthQw)/sum, (widthLeft+widthQw)/sum, 1.0});
		dataCBO.setXSeries(new double[]{0.0, widthLeft/sum, widthLeft/sum, (widthLeft+widthQw)/sum, (widthLeft+widthQw)/sum, 1.0});
		getAxisSet().adjustRange();
		redraw();
		
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
		getAxisSet().getYAxis(0).adjustRange();
		redraw();		
	}
	
	
	
	
	
}
