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

public class CompoundQuaternary {

	CompoundTernary ternary1, ternary2;
	CompoundBinary substrate;
	double bowingBandgap, bowingVBO, xternary1, xternary2, EE, EHH, ELH;
	double VBO, bandgap, emass, lhmass, hhmass, av, ac, c11, c12, latticeConstant, deformationPotential;
	String label;
	
	public CompoundQuaternary(CompoundTernary ter1, CompoundTernary ter2, CompoundBinary subs, double bowingBandgapTemp, double bowingVBOTemp) {
		ternary1 = ter1;
		ternary2 = ter2;
		substrate = subs;
		xternary1 = 0.5;
		xternary2 = 0.5;
		bowingBandgap = bowingBandgapTemp;
		bowingVBO = bowingVBOTemp;
		label = "";
		resetParameters();
		strain();
			
	}
	
	public CompoundQuaternary(CompoundTernary ter1, CompoundTernary ter2, CompoundBinary subs) {
		ternary1 = ter1;
		ternary2 = ter2;
		substrate = subs;
		xternary1 = 0.5;
		xternary2 = 0.5;
		if (ternary1.label == "gainas" && ternary2.label == "gainsb") {
			label = "GaInAsSb";
			bowingBandgap = 0.0;
			bowingVBO = 0.0;
		}
		resetParameters();
		strain();
		
		
	}
	
	public void setVBO() {
		VBO = xternary1*ternary1.VBO+xternary2*ternary2.VBO-bowingVBO*xternary1*xternary2;
	}
	public void setBandgap() {
		bandgap = xternary1*ternary1.bandgap+xternary2*ternary2.bandgap-bowingBandgap*xternary1*xternary2;
	}
	
	public void setEmass() {
		emass = xternary1*ternary1.emass+xternary2*ternary2.emass;
	}
	public void setHHmass() {
		hhmass = xternary1*ternary1.hhmass+xternary2*ternary2.hhmass;
	}
	public void setLHmass() {
		lhmass = xternary1*ternary1.lhmass+xternary2*ternary2.lhmass;
	}
	public void setAv() {
		av = xternary1*ternary1.av+xternary2*ternary2.av;
	}
	public void setAc() {
		ac = xternary1*ternary1.ac+xternary2*ternary2.ac;
	}
	public void setC12() {
		c12 = xternary1*ternary1.c12+xternary2*ternary2.c12;
	}
	public void setC11() {
		c11 = xternary1*ternary1.c11+xternary2*ternary2.c11;
	}
	public void setDeformationPotential() {
		deformationPotential = xternary1*ternary1.deformationPotential+xternary2*ternary2.deformationPotential;
	}
	public void setLatticeConstant() {
		latticeConstant = xternary1*ternary1.latticeConstant+xternary2*ternary2.latticeConstant;
	}
	
	public void resetParameters() {
		ternary1.resetParameters();
		ternary2.resetParameters();
		setVBO();
		setBandgap();
		setEmass();
		setHHmass();
		setLHmass();
		setAv();
		setAc();
		setC12();
		setC11();
		setDeformationPotential();
		setLatticeConstant();
	}
	
	//getters and setters
	public void setTernary1(CompoundTernary newTernary1) {
		ternary1 = newTernary1;
	}
	public void setTernary2(CompoundTernary newTernary2) {
		ternary2 = newTernary2;
	}
	public void setXternary1 (double newXternary1) {
		xternary1 = newXternary1;
		xternary2 = 1-newXternary1;
	}
	public void setXternary2 (double newXternary2) {
		xternary2 = newXternary2;
		xternary1 = 1-newXternary2;
	}
	
	public void setBowingBandgap(double newBowingBandgap) {
		bowingBandgap = newBowingBandgap;
	}
	
	public void setBowingVBO(double newBowingVBO) {
		bowingVBO = newBowingVBO;
	}
	
	public void setEE(double newEE) {
		EE = newEE;
	}
	public void setEHH(double newEHH) {
		EHH = newEHH;
	}
	public void setELH(double newELH) {
		ELH = newELH;
	}
	
	public double getBowingVBO() {
		return bowingVBO;
	}
	
	public double getBowingBandgap() {
		return bowingBandgap;
	}
	
	public double getXternary1() {
		return xternary1;
	}
	public double getXternary2() {
		return xternary2;
	}
	public CompoundTernary getTernary1() {
		return ternary1;
	}
	public CompoundTernary getTernary2() {
		return ternary2;
	}
	
	public double getEE() {
		return EE;
	}
	public double getEHH() {
		return EHH;
	}
	public double getELH() {
		return ELH;
	}
	
	public void strain() {
		double e = (substrate.latticeConstant-latticeConstant)/latticeConstant;
		double dev = 2*av*(1-c12/c11)*e;
		double dec = 2*ac*(1-c12/c11)*e;
		double des = deformationPotential*(1-2*c12/c11)*e;
		setEE(VBO+bandgap+dec);
		setEHH(VBO+dev+des);
		setELH(VBO+dev-des);
	}
	
}
