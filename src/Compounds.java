import java.text.DecimalFormat;
import java.util.Arrays;

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

public class Compounds {
	
	private MyWindow window;
	public CompoundBinary substrate;
	public CompoundBinary[] binaries = new CompoundBinary[4];
	public CompoundTernary[] ternaries = new CompoundTernary[2];
	public CompoundQuaternary layer;
	
	public Compounds(MyWindow newmywindow) {

		window = newmywindow;
		//set chosen binaries and substrate (user's choice through the interface)
		for (int i = 0; i<4; i++)
			//binaries[i] = new CompoundBinary(window.getChosenBinary(i));
			binaries[i] = new CompoundBinary(i);
		
		//set the substrate (user's choice through the interface)
		substrate = new CompoundBinary(window.getChosenSubstrate());
		
		//set ternaries automatically (based on chosen binaries)
		ternaries[0] = new CompoundTernary(binaries[0], binaries[1]);
		ternaries[1] = new CompoundTernary(binaries[2], binaries[3]);
		
		//basing on ternaries, set the quaternary
		layer = new CompoundQuaternary(ternaries[0], ternaries[1], substrate);
		
		
	}
	
	void setBinary(int which, int selection) {
		binaries[which] = new CompoundBinary(selection);
		if (which == 0 || which == 1)
			ternaries[0] = new CompoundTernary(binaries[0], binaries[1]);
		else
			ternaries[1] = new CompoundTernary(binaries[2], binaries[3]);
		layer = new CompoundQuaternary(ternaries[0], ternaries[1], substrate);
	}
	
	void setSubstrate(int selection) {
		substrate = new CompoundBinary(selection);
		layer = new CompoundQuaternary(ternaries[0], ternaries[1], substrate);
	}
	
	void searchMinimumStrain(double strain, double energy) {
		CompoundBinary bin1 = new CompoundBinary(binaries[0].label, binaries[0].bandgap, binaries[0].latticeConstant, binaries[0].VBO, binaries[0].ac, binaries[0].av, binaries[0].c11, binaries[0].c12, binaries[0].deformationPotential, binaries[0].emass, binaries[0].hhmass, binaries[0].lhmass);
		CompoundBinary bin2 = new CompoundBinary(binaries[1].label, binaries[1].bandgap, binaries[1].latticeConstant, binaries[1].VBO, binaries[1].ac, binaries[1].av, binaries[1].c11, binaries[1].c12, binaries[1].deformationPotential, binaries[1].emass, binaries[1].hhmass, binaries[1].lhmass);
		CompoundBinary bin3 = new CompoundBinary(binaries[2].label, binaries[2].bandgap, binaries[2].latticeConstant, binaries[2].VBO, binaries[2].ac, binaries[2].av, binaries[2].c11, binaries[2].c12, binaries[2].deformationPotential, binaries[2].emass, binaries[2].hhmass, binaries[2].lhmass);
		CompoundBinary bin4 = new CompoundBinary(binaries[3].label, binaries[3].bandgap, binaries[3].latticeConstant, binaries[3].VBO, binaries[3].ac, binaries[3].av, binaries[3].c11, binaries[3].c12, binaries[3].deformationPotential, binaries[3].emass, binaries[3].hhmass, binaries[3].lhmass);
		CompoundTernary ter1 = new CompoundTernary(bin1, bin2);
		CompoundTernary ter2 = new CompoundTernary(bin3, bin4);
		CompoundBinary sub = new CompoundBinary(substrate.label, substrate.bandgap, substrate.latticeConstant, substrate.VBO, substrate.ac, substrate.av, substrate.c11, substrate.c12, substrate.deformationPotential, substrate.emass, substrate.hhmass, substrate.lhmass);
		CompoundQuaternary lay = new CompoundQuaternary(ter1, ter2, sub);
		double[] params = new double[3];
		double minimum = 100;
		double test, band;
		int indicator = 0;
		window.output.insert("\n\n\n\na(l)-a(s)\t| Eg\t\t| x1\t\t| x2\t\t| x3");
		for (double i=0.1; i<=0.9; i+=0.01) {
			ter1.setXbinary1(i);
			for (double j=0.1; j<=0.9; j+=0.01) {
				ter2.setXbinary1(j);
				for (double k=0.1; k<=0.9; k+=0.01) {
					lay.setXternary1(k);
					lay.resetParameters();
					lay.strain();
					test = Math.abs(lay.latticeConstant-sub.latticeConstant);
					band = lay.EE-lay.EHH;
					if (test<strain && band <=energy && layer.EHH+layer.bandgap < sub.VBO+sub.bandgap) {
						window.output.insert("\n" + Double.toString(roundFiveDecimals(test)) + " \t| " + Double.toString(roundFourDecimals(band)) + " \t| " + Double.toString(roundTwoDecimals(i)) + " \t| " + Double.toString(roundTwoDecimals(j))+ " \t| " + Double.toString(roundTwoDecimals(k)));
						indicator = 1;
					}
				}
			}
		}
		if (indicator == 0)
			window.output.insert("\nNot found (change parameters)");
		
	}
	
	double roundTwoDecimals(double d) {
    	DecimalFormat DForm = new DecimalFormat("#.##");
	return Double.valueOf(DForm.format(d));
	}
	
	double roundFiveDecimals(double d) {
    	DecimalFormat DForm = new DecimalFormat("#.#####");
	return Double.valueOf(DForm.format(d));
	}
	
	double roundFourDecimals(double d) {
    	DecimalFormat DForm = new DecimalFormat("#.####");
	return Double.valueOf(DForm.format(d));
	}

}
