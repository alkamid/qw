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
import org.swtchart.IAxisTick;
import org.swtchart.ILineSeries;
import org.swtchart.LineStyle;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class MyPlot extends Chart {
	
	private Display display;
	private Shell shell;
	private MyWindow window;
	private Compounds compounds;
	private Matrix matrixTemp, matrixE, matrixHH, matrixLH;
	public ILineSeries dataCBO, dataVBO, dataMargin, dataVec, dataEHH, dataELH;
	public ILineSeries[] dataEigenvaluesE, dataEigenvaluesHH, dataEigenvaluesLH;
	
	public MyPlot(Display newdisplay,Shell newshell, MyWindow newmywindow, Compounds newcompounds) {

		super(newshell,SWT.NONE);
		display = newdisplay;
		shell = newshell;
		window = newmywindow;
		compounds = newcompounds;
		
		dataCBO = (ILineSeries) getSeriesSet().createSeries(SeriesType.LINE, "CBO");
		dataVBO =(ILineSeries) getSeriesSet().createSeries(SeriesType.LINE, "VBO");
		dataEHH =(ILineSeries) getSeriesSet().createSeries(SeriesType.LINE, "EHH");
		dataELH =(ILineSeries) getSeriesSet().createSeries(SeriesType.LINE, "ELH");
		dataCBO.setLineWidth(2);
		dataVBO.setLineWidth(2);
		dataEHH.setLineWidth(2);
		dataELH.setLineWidth(2);
		dataCBO.setLineColor(display.getSystemColor(SWT.COLOR_RED));
		dataEHH.setLineColor(display.getSystemColor(SWT.COLOR_GREEN));
		dataELH.setLineColor(display.getSystemColor(SWT.COLOR_GREEN));
		dataCBO.setSymbolType(PlotSymbolType.NONE);
		dataVBO.setSymbolType(PlotSymbolType.NONE);
		dataEHH.setSymbolType(PlotSymbolType.NONE);
		dataELH.setSymbolType(PlotSymbolType.NONE);
		dataVec = (ILineSeries) getSeriesSet().createSeries(SeriesType.LINE, "Vector");
		dataVec.setLineWidth(2);
		dataVec.setSymbolType(PlotSymbolType.NONE);
		
		setSize(shell.getSize().x-40,350);

		
		getLegend().setVisible(false);
		getTitle().setText("");
		getAxisSet().getXAxis(0).getTitle().setText("x");
		getAxisSet().getYAxis(0).getTitle().setText("energy [eV]");
		IAxisTick xTick = getAxisSet().getXAxis(0).getTick();
		xTick.setVisible(false);
		resetComp();
		resetWidth();
		
	}
	
	void resetComp() {
		compounds.layer.resetParameters();
		compounds.layer.strain();
		replot();	
	}
	
	void replot() {
		double[] tableVBO = new double[]{compounds.substrate.VBO, compounds.substrate.VBO, compounds.layer.EHH, compounds.layer.EHH, compounds.substrate.VBO, compounds.substrate.VBO};
		double[] tableCBO = new double[]{compounds.substrate.VBO+compounds.substrate.bandgap, compounds.substrate.VBO+compounds.substrate.bandgap, compounds.layer.EE, compounds.layer.EE, compounds.substrate.VBO+compounds.substrate.bandgap, compounds.substrate.VBO+compounds.substrate.bandgap};
		double[] tableELH = new double[]{compounds.layer.ELH, compounds.layer.ELH};
		dataVBO.setYSeries(tableVBO);
		dataCBO.setYSeries(tableCBO);
		dataELH.setYSeries(tableELH);
		eigenvaluesErase();
		margin(tableVBO, tableCBO);
	}
	
	void resetWidth() {
		double widthBarrier = window.getWidth(0);
		double widthQw = window.getWidth(1);
		double sum = 2*widthBarrier + widthQw;
		dataVBO.setXSeries(new double[]{0.0, widthBarrier/sum, widthBarrier/sum, (widthBarrier+widthQw)/sum, (widthBarrier+widthQw)/sum, 1.0});
		dataCBO.setXSeries(new double[]{0.0, widthBarrier/sum, widthBarrier/sum, (widthBarrier+widthQw)/sum, (widthBarrier+widthQw)/sum, 1.0});
		dataEHH.setXSeries(new double[]{widthBarrier/sum, (widthBarrier+widthQw)/sum});
		dataELH.setXSeries(new double[]{widthBarrier/sum, (widthBarrier+widthQw)/sum});
		eigenvaluesErase();
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
	
	void plotEigenvalues() {
		
		window.output.insert("\n\n\n");
		compounds.layer.resetParameters(); // refreshing all parameters just in case
		// electron states first
		double eBarrier = compounds.substrate.bandgap+compounds.substrate.VBO;
		double eQW = compounds.layer.EE;
		double eMassBarrier = compounds.substrate.emass;
		double eMassQW = compounds.layer.emass;
		// heavy holes
		double hBarrier = compounds.substrate.VBO;
		double hhQW = compounds.layer.EHH;
		double hhMassBarrier = compounds.substrate.hhmass;
		double hhMassQW = compounds.layer.hhmass;
		double hhHeight = Math.abs(hBarrier - hhQW);
		// light holes
		double lhQW = compounds.layer.ELH;
		double lhMassBarrier = compounds.substrate.lhmass;
		double lhMassQW = compounds.layer.lhmass;
		double lhHeight = Math.abs(hBarrier - lhQW);
		// width - common for each type of carriers
		double widthBarrier = window.getWidth(0);
		double widthQW = window.getWidth(1);
		double width = widthBarrier*2+widthQW;
		
		if (eBarrier <= eQW) {
			window.output.insert("Electron barrier is lower than the well. This program calculates only Type I QW states.");
			return;
		}
		else if (hBarrier >= hhQW || hBarrier >= lhQW) {
			window.output.insert("Hole barrier is higher than the well. This program calculates only Type I QW states.");
			return;
		}
		else {
			window.output.insert("Calculating...");
			
			// creating matrices and finding eigenvalues + eigenvectors (maybe separate values from vectors in the future)
			matrixE = new Matrix(eQW, eBarrier, widthQW, widthBarrier, eMassQW, eMassBarrier, 5000);
			matrixHH = new Matrix(0, hhHeight, widthQW, widthBarrier, hhMassQW, hhMassBarrier, 5000);
			matrixLH = new Matrix(0, lhHeight, widthQW, widthBarrier, lhMassQW, lhMassBarrier, 5000);
			
			matrixE.valsvecs();
			matrixHH.valsvecs();
			matrixLH.valsvecs();
			matrixE.normalizeEnergy();
			matrixHH.normalizeEnergy();
			matrixLH.normalizeEnergy();
			matrixE.normalizeVectors();
			matrixHH.normalizeVectors(hhQW);
			matrixLH.normalizeVectors(lhQW);
			
			// scale holes energy
			matrixHH.normalizeEnergyHoles(hhQW);
			matrixLH.normalizeEnergyHoles(lhQW);
	
			dataEigenvaluesE = new ILineSeries[matrixE.eigenvalues.length];
			dataEigenvaluesHH = new ILineSeries[matrixHH.eigenvalues.length];
			dataEigenvaluesLH = new ILineSeries[matrixLH.eigenvalues.length];
			
			window.setNumEigenvectorsE(matrixE.eigenvalues.length);
			window.setNumEigenvectorsHH(matrixHH.eigenvalues.length);
			window.setNumEigenvectorsLH(matrixLH.eigenvalues.length);
			
			window.output.insert("\nElectron energy states:");
			
			for (int i=0; i<dataEigenvaluesE.length; i++) {
				dataEigenvaluesE[i] = (ILineSeries) getSeriesSet().createSeries(SeriesType.LINE, "E" + Integer.toString(i));
				dataEigenvaluesE[i].setLineWidth(1);
				dataEigenvaluesE[i].setXSeries(new double[]{widthBarrier/width, (widthBarrier+widthQW)/width});
				dataEigenvaluesE[i].setYSeries(new double[]{matrixE.eigenvalues[i], matrixE.eigenvalues[i]});
				dataEigenvaluesE[i].setSymbolType(PlotSymbolType.NONE);
				window.output.insert("\n" + Double.toString(round(matrixE.eigenvalues[i],4)) + " [eV]");
			}
			
			window.output.insert("\nHeavy hole energy states:");
			
			for (int i=0; i<dataEigenvaluesHH.length; i++) {
				dataEigenvaluesHH[i] = (ILineSeries) getSeriesSet().createSeries(SeriesType.LINE, "HH" + Integer.toString(i));
				dataEigenvaluesHH[i].setLineWidth(1);
				dataEigenvaluesHH[i].setXSeries(new double[]{widthBarrier/width, (widthBarrier+widthQW)/width});
				dataEigenvaluesHH[i].setYSeries(new double[]{matrixHH.eigenvalues[i], matrixHH.eigenvalues[i]});
				dataEigenvaluesHH[i].setSymbolType(PlotSymbolType.NONE);
				window.output.insert("\n" + Double.toString(round(matrixHH.eigenvalues[i],4)) + " [eV]");
			}
			
			window.output.insert("\nLight hole energy states:");
			
			for (int i=0; i<dataEigenvaluesLH.length; i++) {
				dataEigenvaluesLH[i] = (ILineSeries) getSeriesSet().createSeries(SeriesType.LINE, "LH" + Integer.toString(i));
				dataEigenvaluesLH[i].setLineWidth(1);
				dataEigenvaluesLH[i].setXSeries(new double[]{widthBarrier/width, (widthBarrier+widthQW)/width});
				dataEigenvaluesLH[i].setYSeries(new double[]{matrixLH.eigenvalues[i], matrixLH.eigenvalues[i]});
				dataEigenvaluesLH[i].setSymbolType(PlotSymbolType.NONE);
				dataEigenvaluesLH[i].setLineColor(display.getSystemColor(SWT.COLOR_GREEN));
				window.output.insert("\n" + Double.toString(round(matrixLH.eigenvalues[i],4)) + " [eV]");
			}
			
			window.output.insert("\nLowest energy gap: " + Double.toString(round(matrixE.eigenvalues[0]-Math.max(matrixHH.eigenvalues[0],matrixLH.eigenvalues[0]),4)));
			
			window.output.insert("\nStrain: " + Double.toString(Math.abs(round((compounds.layer.latticeConstant-compounds.substrate.latticeConstant)/compounds.layer.latticeConstant*100,4))) + "%");
			
			redraw();
		}
		
	}
	
	void eigenvaluesErase() {
		if (dataEigenvaluesE != null)
			for (int i=0; i<dataEigenvaluesE.length; i++) {
				dataEigenvaluesE[i].setLineStyle(LineStyle.NONE);
			}
		if (dataEigenvaluesHH != null)
			for (int i=0; i<dataEigenvaluesHH.length; i++) {
				dataEigenvaluesHH[i].setLineStyle(LineStyle.NONE);
			}
		if (dataEigenvaluesLH != null)
			for (int i=0; i<dataEigenvaluesLH.length; i++) {
				dataEigenvaluesLH[i].setLineStyle(LineStyle.NONE);
			}
		if (dataVec != null)
			dataVec.setLineStyle(LineStyle.NONE);
		}
	
	
	void plotVectorE(int carrier, int which) {
		switch (carrier) {
		case 1: matrixTemp = matrixE; break;
		case 2: matrixTemp = matrixHH; break;
		case 3: matrixTemp = matrixLH; break;
		}
		double[] vec = matrixTemp.eigenvectors[which];
		int n = vec.length;
		double step = 1.0/n;
		double[] tab = new double[n];
		double[] y = new double[n];
		for (int i=0; i<n; i++) {
			tab[i] = i*step;
			y[i] = vec[i];
		}
		dataVec.setXSeries(tab);
		dataVec.setYSeries(y);
		dataVec.setLineStyle(LineStyle.SOLID);
		getAxisSet().getYAxis(0).adjustRange();
		redraw();
	}
	
	void plotVectorE(double[] vec) {
		int n = vec.length;
		double step = 1.0/n;
		double[] tab = new double[n];
		double[] y = new double[n];
		for (int i=0; i<n; i++) {
			tab[i] = i*step;
			y[i] = vec[i];
		}
		dataVec.setXSeries(tab);
		dataVec.setYSeries(y);
		dataVec.setLineStyle(LineStyle.SOLID);
		getAxisSet().getYAxis(0).adjustRange();
		redraw();
	}
	
	double round(double d, int i) {
		return (double)((int) (d * Math.pow(10,i)))/Math.pow(10, i);
	}
	
	
}
