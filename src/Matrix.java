import java.util.Arrays;


public class Matrix {
	
	public double[] diagonal;
	public double[] outDiagonal;
	public double s;
	public int size;
	private static double alpha1 = (1.054571628*1.054571628)/(2*1.60217646);
	private static double alpha = 1;
	
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
	
	public Matrix(double width, int n) {
		s = 2*width/(n+1);
		size = n;
		double [] x = new double[n+2];
		
		x[0] = -1*width;
		for (int i = 1; i<=n+1; i++) {
			x[i] = x[0]+s*i;
		}
		
		System.out.println(s);
		
		double [] V = new double[n+2];
		
		for (int i=0; i<=n+1; i++) {
			V[i] = 0.5*Math.pow(x[i], 2);
		}
				
		diagonal = new double[n];
		outDiagonal = new double[n-1];

		for (int i=0; i<n-1; i++) {
			diagonal[i] = 2+s*s*alpha*V[i+1];
			outDiagonal[i] = -1;
		}
		diagonal[n-1] = 2+s*s*alpha*V[n];
	}
	
	
	public int Dean(double WellHeight, int HowMany) {

		double diagonalTemp[] = diagonal;
		double outDiagonalTemp[] = new double[size-1];
		for (int i=0; i<size-1; i++)
			outDiagonalTemp[i] = Math.pow(outDiagonal[i], 2);
		int counter = 0;
		double u;
		
		u=diagonalTemp[0]-WellHeight;
		if (u<0)
			counter++;
		
		for (int i=1; i<size; i++) {
			u=diagonalTemp[i]-WellHeight-outDiagonalTemp[i-1]/u;
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
	
	public double normalizeEnergy(double energy) {
		//return (energy/(s*s*alpha))/(0.5*1.054571628);
		return energy/(s*s);
	}
	
	public double[] Bisect(double wmin, double wmax, int HowMany) {
		int m1, m2;
		m1 = Dean(wmin, HowMany);
		m2 = Dean(wmax, HowMany);
		double tolerance = Math.pow(10, -12);
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
		return w;

		
	}

}
