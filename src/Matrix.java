import java.util.Arrays;
import org.netlib.lapack.*;
import org.netlib.util.intW;



public class Matrix {
	
	public double[] diagonal;
	public double[] outDiagonal;
	public double s;
	public int size;
	public double eBarrier, eQW;
	private static double freeElectronMass = 9.10938215*1.0e-31;
	private static double hbar = 1.054571628*1.0e-34;
	private static double nanoMeter = 1.0e-9;
	private static double electronCharge = 1.602176487*1.0e-19;
	private double alpha = (2*freeElectronMass*electronCharge*nanoMeter*nanoMeter)/(hbar*hbar);
	public double[] eigenvalues;
	public double[][] eigenvectors;
	
	public Matrix(double[] values) {
		size = values.length;
		s = 1;
		diagonal = new double[size];
		outDiagonal = new double[size-1];
		for (int i=0; i<size-1; i++) {
			diagonal[i] = values[i];
			outDiagonal[i] = -1;
		}
		diagonal[size-1] = values[size-1];
	}
	
	public Matrix(double EWell, double EBarrier, double wellWidth, double barrierWidth, int n) {
		double fullWidth = 2*barrierWidth+wellWidth;
		double startWell = barrierWidth;
		double endWell = barrierWidth+wellWidth;
		s = fullWidth/(n+1);
		size = n;
		double [] x = new double[n+2];

		for (int i =0; i<=n+1; i++) {
			x[i] = s*i;
		}

		double [] V = new double[n+2];

		for (int i=0; i<=n+1; i++) {
			if (x[i]<=startWell || x[i]>= endWell)
				V[i] = EBarrier;
			else
				V[i] = EWell;
		}

		diagonal = new double[n];
		outDiagonal = new double[n-1];

		for (int i=0; i<n-1; i++) {
			diagonal[i] = 2+s*s*alpha*V[i+1];
			outDiagonal[i] = -1;
		}
		diagonal[n-1] = 2+s*s*alpha*V[n];
	}
	
	public Matrix(double EWell, double EBarrier, double wellWidth, double barrierWidth, double emassQW, double emassBarrier, int n) {
		double fullWidth = 2*barrierWidth+wellWidth;
		double startWell = barrierWidth;
		double endWell = barrierWidth+wellWidth;
		eBarrier = EBarrier;
		eQW = EWell;
		s = fullWidth/(n+1);
		size = n;
		double [] x = new double[n+2];
		double [] xm = new double [n+2];
		
		for (int i =0; i<=n+1; i++) {
			x[i] = s*i;
			xm[i] = s*(i+0.5);
		}
		
		double [] V = new double[n+2];
		double [] w = new double[n+2];
		
		for (int i=0; i<=n+1; i++) {
			if (x[i]<=startWell || x[i]>= endWell)
				V[i] = EBarrier;
			else
				V[i] = EWell;
			
			if (xm[i]<=startWell || xm[i]>= endWell)
				w[i] = 1/emassBarrier;
			else
				w[i] = 1/emassQW;
		}
		
		diagonal = new double[n];
		outDiagonal = new double[n-1];
		
		for (int i=0; i<n-1; i++) {
			diagonal[i] = w[i]+w[i+1]+s*s*alpha*V[i+1];
			outDiagonal[i] = -w[i+1];
		}
		diagonal[n-1] = w[n-1]+w[n]+s*s*alpha*V[n];
	}
	
	public Matrix(double width, int n) {
		s = 2*width/(n+1);
		size = n;
		double [] x = new double[n+2];
		
		x[0] = -1*width;
		for (int i = 1; i<=n+1; i++) {
			x[i] = x[0]+s*i;
		}
		
		double [] V = new double[n+2];
		
		for (int i=0; i<=n+1; i++) {
			V[i] = Math.pow(x[i], 2);
		}
				
		diagonal = new double[n];
		outDiagonal = new double[n-1];

		for (int i=0; i<n-1; i++) {
			diagonal[i] = 2+s*s*V[i+1];
			outDiagonal[i] = -1;
		}
		diagonal[n-1] = 2+s*s*V[n];
	}
	
	public void valsvecs() {		
		DSTEGR dg = new DSTEGR();
		
		double[] outTemp = new double[size];
		for (int i=0; i<size-1; i++)
			outTemp[i] = outDiagonal[i];
			                        
		outTemp[size-1] = 0;
		
		intW m = new intW(200);
		double[] w = new double[size];
		double[][] z = new double[size][m.val];
		int[] isuppz = new int[2*m.val];
		int lwork = 18*size;
		double[] work = new double[lwork];
		int liwork = 10*size;
		int[] iwork = new int[liwork];
		intW info = new intW(1);
		dg.DSTEGR("V", "V", size, diagonal, outTemp, eQW*alpha*s*s, eBarrier*alpha*s*s, 0, 0 , 1e-16, m, w, z, isuppz, work, lwork, iwork, liwork, info);
		
		
		eigenvalues = new double[m.val];
		eigenvectors = new double[m.val][size];
		for (int j=0; j<m.val; j++) {
			eigenvalues[j] = w[j];
			for (int i=0; i<size; i++)
				eigenvectors[j][i] = z[i][j]; 
		}

	}
	
	
	public int Dean(double WellHeight, int HowMany) {

		double outDiagonalTemp[] = new double[size-1];
		for (int i=0; i<size-1; i++)
			outDiagonalTemp[i] = Math.pow(outDiagonal[i], 2);
		int counter = 0;
		double u;
		
		u=diagonal[0]-WellHeight;
		if (u<0)
			counter++;
		
		for (int i=1; i<size; i++) {
			u=diagonal[i]-WellHeight-outDiagonalTemp[i-1]/u;
			if (u<0)
				counter++;
			if (counter==HowMany)
				break;
		}
		
		return counter;
	}
	
	private boolean BisectCheck(double x1, double x, double x2) {
		return (x==x1 || x==x2);
	}
	
	public void normalizeEnergy() {
		for (int i=0; i<eigenvalues.length; i++)
			eigenvalues[i] = eigenvalues[i]/(alpha*s*s);
	}
	
	public void normalizeEnergy(double[] energies) {
		for (int i=0; i<energies.length; i++)
			energies[i] = energies[i]/(alpha*s*s);
	}
	
	public void normalizeEnergyHoles(double groundEnergy) {
		for (int i=0; i<eigenvalues.length; i++)
			eigenvalues[i] = groundEnergy - eigenvalues[i];
	}
	
	
	public void normalizeVectors() {
		for (int i=0; i<eigenvectors.length; i++) {
			for (int j=0; j<eigenvectors[i].length; j++) {
				eigenvectors[i][j] = eigenvectors[i][j]*5+eigenvalues[i];
			}
		}
	}
	
	public void normalizeVectors(double groundLevel) {
		for (int i=0; i<eigenvectors.length; i++) {
			for (int j=0; j<eigenvectors[i].length; j++) {
				eigenvectors[i][j] = eigenvectors[i][j]*5-eigenvalues[i]+groundLevel;
			}
		}
	}
	
	public double[] Bisect(double wmin, double wmax, int HowMany) {
		int m1, m2;
		wmin = wmin*alpha*s*s;
		wmax = wmax*alpha*s*s;
		m1 = Dean(wmin, HowMany);
		m2 = Dean(wmax, HowMany);
		double tolerance = 1e-16;
		double[][] wk1 = new double[2][HowMany];
		int[] wk2 = new int[HowMany];
		double[] w = new double[HowMany];
		double z, z1, z2;
		int i,j, k,m2a,k1,k2,k20;


		if (m2<=m1)
			return null;

		m2a = Math.min(m2-m1, HowMany);

		for (int u=0; u<m2a; u++) {
			wk1[0][u] = wmin;
			wk1[1][u] = wmax;
			wk2[u] = m2;
		}

		k1=m1;
		i=1;

		while(i<=m2a) {

			z1=wk1[0][i-1];
			z2=wk1[1][i-1];
			k2=wk2[i-1];
			k20=k2;

			do {
				z=0.5*(z1+z2);
				if (z2-z1<=tolerance || BisectCheck(z1, z, z2))
					break;

				k = Dean(z, k2);
				if (k == k1)
					z1 = z;
				else {
					z2 = z;
					j=k-m1;
					if ((k2-k1)>1 && j <=m2a) {
						wk2[j-1] = k;
						wk1[1][j-1] = z2;
						if (j<m2a)
							wk1[0][j]=z2;
					}
					k2=k;
				}
			} while (true);

			for (j=i; j<=Math.min(k2-m1, m2a); j++)
				w[j-1] = z;

			i = k2-m1+1;

			if (i>m2a)
				break;

			k1=k2;

			for (j=i; j<=Math.min(k20-m1, m2a); j++) {
				if (wk1[0][j-1]<z2)
					wk1[0][j-1]=z2;
				else
					z2=wk1[0][j-1];
			}
			z2=wmax;
			k2=m2;
			for (j=Math.min(k20-m1, m2a); j>=i; j--) {
				if (wk1[1][j-1]>z2) {
					wk1[1][j-1]=z2;
					wk2[j-1]=k2;
				}
				else {
					z2=wk1[1][j-1];
					k2=wk2[j-1];
				}
			}
		}
		normalizeEnergy(w);
		return w;


	}
	
	public double[] dwsz(double w) {
		int k=0;
		double t, r, rem;
		double[] y = new double[size];
		double[] e = outDiagonal;
		double[] d = diagonal;
		double[][] wk = new double[size][2];
		double l = size;
		
		double rmin=1e308;
		wk[0][0] = -1.0*e[0]/(d[0]-w);
		for (int i=1; i<size-1; i++) {
			wk[i][0] = -1.0*e[i]/(e[i-1]*wk[i-1][0]+d[i]-w);
		}
		wk[size-1][1]=-1.0*e[size-2]/(d[size-1]-w);
		for (int i=size-2; i>0; i--) {
			t=e[i]*wk[i+1][1]+d[i]-w;
			wk[i][1]=-1.0*e[i-1]/t;
			r=Math.abs(e[i-1]*wk[i-1][0]+t);
			if (r<rmin) {
				rmin=r;
				k=i;
			}
		}
		
		y[k]=1;
		t=1;
		
		for (int i=k-1; i>=0; i--) {
			if (y[i+1]!=0)
				y[i]=y[i+1]*wk[i][0];
			else
				y[i]=-1.0*e[i+1]*y[i+2]/e[i];
			
			t=t+Math.pow(y[i],2);
		}
		
		for (int i=k+1; i<size; i++) {
			if (y[i-1] != 0)
				y[i]=y[i-1]*wk[i][1];
			else
				y[i]=-1.0*e[i-2]*y[i-2]/e[i-1];
			t=t+Math.pow(y[i], 2);
		}
		
		t=Math.sqrt(size/(l*t));
		
		for (int i=0; i<size; i++) {
			rem=rmin*t;
			y[i]*=t;
		}
		
		return y;
		
	}
	
	public double[] dwsztest(double w) {
		int k=1;
		double t, r, rem;
		double[] y = new double[size+1];
		double[] e = new double[outDiagonal.length+1];
		double[] d = new double[diagonal.length+1];
		double[][] wk = new double[size+1][3];
		double l = size;
		
		for (int i = 1; i<outDiagonal.length; i++) {
			e[i]=outDiagonal[i-1];
			d[i]=diagonal[i-1];
		}
			d[diagonal.length] = diagonal[diagonal.length-1];
		
		double rmin=1e308;
		wk[1][1] = -1.0*e[1]/(d[1]-w);
		for (int i=2; i<=size-1; i++) {
			wk[i][2] = -1.0*e[i]/(e[i-1]*wk[i-1][1]+d[i]-w);
		}
		wk[size][2]=-1.0*e[size-1]/(d[size]-w);
		for (int i=size-1; i>=2; i--) {
			t=e[i]*wk[i+1][2]+d[i]-w;
			wk[i][2]=-1.0*e[i-1]/t;
			r=Math.abs(e[i-1]*wk[i-1][1]+t);
			if (r<rmin) {
				rmin=r;
				k=i;
			}
		}
		
		y[k]=1;
		t=1;
		
		for (int i=k-1; i>=1; i--) {
			if (y[i+1]!=0)
				y[i]=y[i+1]*wk[i][1];
			else
				y[i]=-1.0*e[i+1]*y[i+2]/e[i];
			
			t=t+Math.pow(y[i],2);
		}
		
		for (int i=k+1; i<=size; i++) {
			if (y[i-1] != 0)
				y[i]=y[i-1]*wk[i][2];
			else
				y[i]=-1.0*e[i-2]*y[i-2]/e[i-1];
			t=t+Math.pow(y[i], 2);
		}
		
		t=Math.sqrt(size/(l*t));
		
		for (int i=1; i<=size; i++) {
			rem=rmin*t;
		}
		return y;
		
	}

}
