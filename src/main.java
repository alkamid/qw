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

public class main {
	static Matrix matrix;

	public static void main(String[] args) {
		
		//początek testów
		
		int n = 4000;
		
		matrix = new Matrix(0,1,5,200,n);
		//double [][] test = rs.TridiagonalHarm(5,n);
		//double [][] test = rs.Tridiagonal(1,2,20,100,n);
		//System.out.println(Arrays.toString(matrix.diagonal));	
		
		double[] rozw;
		rozw = matrix.Bisect(0, 200, 40);
		for (int i=0; i<40; i++) {
			
			rozw[i] = matrix.normalizeEnergy(rozw[i]);
			if (i>0)
				System.out.println(rozw[i]-rozw[i-1]);
		}
		System.out.println(Arrays.toString(rozw));
		
		//koniec testów
		
		MyWindow window = new MyWindow();

	}

}
