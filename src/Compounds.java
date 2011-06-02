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
		layer = new CompoundQuaternary(ternaries[0], ternaries[1]);
		
		
	}
	
	void setBinary(int which, int selection) {
		binaries[which] = new CompoundBinary(selection);
		if (which == 0 || which == 1)
			ternaries[0] = new CompoundTernary(binaries[0], binaries[1]);
		else
			ternaries[1] = new CompoundTernary(binaries[2], binaries[3]);
		layer = new CompoundQuaternary(ternaries[0], ternaries[1]);
		//replot();
	}
	
	void setSubstrate(int selection) {
		substrate = new CompoundBinary(selection);
		//replot();
	}

}
