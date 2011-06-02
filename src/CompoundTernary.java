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
	public String label;
	
	public CompoundTernary(CompoundBinary bin1, CompoundBinary bin2, double bowingBandgapTemp, double bowingVBOTemp) {
		binary1 = bin1;
		binary2 = bin2;
		xbinary1 = 0.5;
		xbinary2 = 0.5;
		bowingBandgap = bowingBandgapTemp;
		bowingVBO = bowingVBOTemp;
		label = "";
		
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
	}
	
	public double VBO() {
		return xbinary1*binary1.getVBO()+xbinary2*binary2.getVBO()-bowingVBO*xbinary1*xbinary2;
	}
	public double bandgap() {
		return xbinary1*binary1.getBandgap()+xbinary2*binary2.getBandgap()-bowingBandgap*xbinary1*xbinary2;
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
	
	public String getLayer() {
		return getLayer();
	}
	public double getXbinary1 () {
		return xbinary1;
	}
	public double getXbinary2() {
		return xbinary2;
	}
	
}
