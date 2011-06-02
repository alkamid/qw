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
	double bowingBandgap, bowingVBO, xternary1, xternary2;
	String label;
	
	public CompoundQuaternary(CompoundTernary ter1, CompoundTernary ter2, double bowingBandgapTemp, double bowingVBOTemp) {
		ternary1 = ter1;
		ternary2 = ter2;
		xternary1 = 0.5;
		xternary2 = 0.5;
		bowingBandgap = bowingBandgapTemp;
		bowingVBO = bowingVBOTemp;
		label = "";
		
	}
	
	public double VBO() {
		return xternary1*ternary1.VBO()+xternary2*ternary2.VBO()-bowingVBO*xternary1*xternary2;
	}
	public double bandgap() {
		return xternary1*ternary1.bandgap()+xternary2*ternary2.bandgap()-bowingBandgap*xternary1*xternary2;
	}
	
	public CompoundQuaternary(CompoundTernary ter1, CompoundTernary ter2) {
		ternary1 = ter1;
		ternary2 = ter2;
		xternary1 = 0.5;
		xternary2 = 0.5;
		if (ternary1.label == "gainas" && ternary2.label == "gainsb") {
			label = "GaInAsSb";
			bowingBandgap = 0.0;
			bowingVBO = 0.0;
		}
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
	
}
