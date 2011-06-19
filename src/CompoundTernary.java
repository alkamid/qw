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

public class CompoundTernary {

	private CompoundBinary binary1, binary2;
	private double bowingBandgap, bowingVBO, xbinary1, xbinary2;
	double VBO, bandgap, emass, lhmass, hhmass, av, ac, c11, c12, latticeConstant, deformationPotential;
	public String label;
	
	public CompoundTernary(CompoundBinary bin1, CompoundBinary bin2, double bowingBandgapTemp, double bowingVBOTemp) {
		binary1 = bin1;
		binary2 = bin2;
		xbinary1 = 0.5;
		xbinary2 = 0.5;
		bowingBandgap = bowingBandgapTemp;
		bowingVBO = bowingVBOTemp;
		label = "";
		resetParameters();
		
	}
	
	public CompoundTernary(CompoundBinary bin1, CompoundBinary bin2) {
		binary1 = bin1;
		binary2 = bin2;
		xbinary1 = 0.5;
		xbinary2 = 0.5;
		if (binary1.label == "gaas" && binary2.label == "inas") {
			label = "GaInAs";
			bowingBandgap = 0.477;
			bowingVBO = -0.38;
		}
		else if (binary1.label == "gasb" && binary2.label == "insb") {
			label = "GaInSb";
			bowingBandgap = 0.415;
			bowingVBO = 0;
		}
		else {
			label = "unknown";
			bowingBandgap = 0;
			bowingVBO = 0;
		}
		resetParameters();
	}
	
	public void setVBO() {
		VBO = xbinary1*binary1.getVBO()+xbinary2*binary2.getVBO()-bowingVBO*xbinary1*xbinary2;
	}
	public void setBandgap() {
		bandgap = xbinary1*binary1.getBandgap()+xbinary2*binary2.getBandgap()-bowingBandgap*xbinary1*xbinary2;
	}
	public void setEmass() {
		emass = xbinary1*binary1.getEmass()+xbinary2*binary2.getEmass();
	}
	public void setHHmass() {
		hhmass = xbinary1*binary1.getHHmass()+xbinary2*binary2.getHHmass();
	}
	public void setLHmass() {
		lhmass = xbinary1*binary1.getLHmass()+xbinary2*binary2.getLHmass();
	}
	public void setAv() {
		av = xbinary1*binary1.getAv()+xbinary2*binary2.getAv();
	}
	public void setAc() {
		ac = xbinary1*binary1.getAc()+xbinary2*binary2.getAc();
	}
	public void setC12() {
		c12 = xbinary1*binary1.getC12()+xbinary2*binary2.getC12();
	}
	public void setC11() {
		c11 = xbinary1*binary1.getC11()+xbinary2*binary2.getC11();
	}
	public void setDeformationPotential() {
		deformationPotential = xbinary1*binary1.getDeformationPotential()+xbinary2*binary2.getDeformationPotential();
	}
	public void setLatticeConstant() {
		latticeConstant = xbinary1*binary1.getLatticeConstant()+xbinary2*binary2.getLatticeConstant();
	}
	public void resetParameters() {
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
	public void setBinary1(CompoundBinary newBinary1) {
		binary1 = newBinary1;
	}
	
	public void setBinary2(CompoundBinary newBinary2) {
		binary2 = newBinary2;
	}
	
	public void setXbinary1 (double newXbinary1) {
		xbinary1 = newXbinary1;
		xbinary2 = 1-newXbinary1;
	}
	public void setXbinary2 (double newXbinary2) {
		xbinary2 = newXbinary2;
		xbinary1 = 1-newXbinary2;
	}
	
	public void setBowingBandgap(double newBowingBandgap) {
		bowingBandgap = newBowingBandgap;
	}
	
	public void setBowingVBO(double newBowingVBO) {
		bowingVBO = newBowingVBO;
	}
	
	public void setLabel(String newLabel) {
		label = newLabel;
	}
	
	public CompoundBinary getBinary1() {
		return binary1;
	}
	
	public CompoundBinary getBinary2() {
		return binary2;
	}
	
	public double getBowingVBO() {
		return bowingVBO;
	}
	
	public double getBowingBandgap() {
		return bowingBandgap;
	}
	
	public String getLabel() {
		return label;
	}
	public double getXbinary1 () {
		return xbinary1;
	}
	public double getXbinary2() {
		return xbinary2;
	}
	
}
