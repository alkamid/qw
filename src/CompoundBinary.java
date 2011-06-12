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

public class CompoundBinary {

	public String label, label1, label2;
	public double bandgap, latticeConstant, VBO, ac, av, c11, c12, deformationPotential, emass, hhmass, lhmass;
	
	public CompoundBinary(String label1, double bandgap1, double latticeConstant1, double VBO1, double ac1, double av1, double c111, double c121, double deformationPotential1, double emass1, double hhmass1, double lhmass1) {
	
	label = label1;
	bandgap = bandgap1;
	latticeConstant = latticeConstant1;
	VBO = VBO1;
	ac = ac1;
	av = av1;
	c11 = c111;
	c12 = c121;
	deformationPotential = deformationPotential1;
	emass = emass1;
	hhmass = hhmass1;
	lhmass = lhmass1;
	
	}
	
	public CompoundBinary(int compound) {
		switch(compound) {
			case 0: {
				label = "gaas";
				label1 = "Ga";
				label2 = "As";
				bandgap = 1.519;
				latticeConstant = 5.64163;
				VBO = -0.8;
				ac = -7.17;
				av = -1.16;
				c11 = 1221;
				c12 = 566;
				deformationPotential = -2.0;
				emass = 0.067;
				hhmass = 0.45;
				lhmass = 0.074;
			}
			break;
			case 1: {
				label = "inas";
				label1 = "In";
				label2 = "As";
				bandgap = 0.417;
				latticeConstant = 6.0583;
				VBO = -0.59;
				ac = -5.08;
				av = -1.00;
				c11 = 832.9;
				c12 = 452.6;
				deformationPotential = -1.8;
				emass = 0.023;
				hhmass = 0.41;
				lhmass = 0.028;
			}
			break;
			case 2: {
				label = "gasb";
				label1 = "Ga";
				label2 = "Sb";
				bandgap = 0.812;
				latticeConstant = 6.08174;
				VBO = -0.03;
				ac = -7.5;
				av = -0.8;
				c11 = 884.2;
				c12 = 402.6;
				deformationPotential = -2.0;
				emass = 0.046;
				hhmass = 0.39;
				lhmass = 0.046;
			}
			break;
			case 3: {
				label = "insb";
				label1 = "In";
				label2 = "Sb";
				bandgap = 0.235;
				latticeConstant = 6.4794;
				VBO = 0;
				ac = -6.94;
				av = -0.36;
				c11 = 684.7;
				c12 = 373.5;
				deformationPotential = -2.0;
				emass = 0.016;
				hhmass = 0.18;
				lhmass = 0.016;
			}
			break;
			case 4: {
				label = "inp";
				label1 = "In";
				label2 = "P";
				bandgap = 1.4236;
				latticeConstant = 5.8697;
				VBO = -0.94;
				ac = -6.0;
				av = -0.6;
				c11 = 1011;
				c12 = 561;
				deformationPotential = -2.0;
				emass = 0.077;
				hhmass = 0.60;
				lhmass = 0.12;
			}
		
		}
	}
	
	
	//getters and setters
	public void setLabel(String newlabel) {
		label = newlabel;
	}
	
	public void setBandgap(double newbandgap) {
		bandgap = newbandgap;
	}
	
	public void setLatticeConstant(double newLatticeConstant) {
		latticeConstant = newLatticeConstant;
	}
	
	public void setVBO(double newVBO) {
		VBO = newVBO;
	}
	
	public void setAc(double newAc) {
		ac = newAc;
	}
	
	public void setAv(double newAv) {
		av = newAv;
	}
	
	public void setC11(double newC11) {
		c11 = newC11;
	}
	
	public void setC12(double newC12) {
		c12 = newC12;
	}
	
	public void setDeformationPotential(double newDeformationPotential) {
		deformationPotential = newDeformationPotential;
	}
	
	public void setEmass(double newEmass) {
		emass = newEmass;
	}
	
	public void setHhmass(double newHhmass) {
		hhmass = newHhmass;
	}
	
	public void setLhmass(double newLhmass) {
		lhmass = newLhmass;
	}
	
	public String getlabel() {
		return label;
	}
	
	public double getBandgap() {
		return bandgap;
	}
	
	public double getLatticeConstant() {
		return latticeConstant;
	}
	
	public double getVBO() {
		return VBO;
	}
	
	public double getAc() {
		return ac;
	}
	
	public double getAv() {
		return av;
	}
	
	public double getC11() {
		return c11;
	}
	
	public double getC12() {
		return c12;
	}
	
	public double getDeformationPotential() {
		return deformationPotential;
	}
	
	public double getEmass() {
		return emass;
	}
	
	public double getHHmass() {
		return hhmass;
	}
	
	public double getLHmass() {
		return lhmass;
	}
}
