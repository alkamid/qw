
public class CompoundBinary {

	public String label;
	public double bandgap, latticeConstant, VBO, ac, av, c11, c12, deformationPotential;
	
	public CompoundBinary(String label1, double bandgap1, double latticeConstant1, double VBO1, double ac1, double av1, double c111, double c121, double deformationPotential1) {
	
	label = label1;
	bandgap = bandgap1;
	latticeConstant = latticeConstant1;
	VBO = VBO1;
	ac = ac1;
	av = av1;
	c11 = c111;
	c12 = c121;
	deformationPotential = deformationPotential1;
	
	}
	
	public CompoundBinary(int compound) {
		switch(compound) {
			case 0: {
				label = "gaas";
				bandgap = 1.519;
				latticeConstant = 5.64163;
				VBO = -0.8;
				ac = -7.17;
				av = -1.16;
				c11 = 1221;
				c12 = 566;
				deformationPotential = -2.0;
			}
			break;
			case 1: {
				label = "inas";
				bandgap = 0.417;
				latticeConstant = 6.0583;
				VBO = -0.59;
				ac = -5.08;
				av = -1.00;
				c11 = 832.9;
				c12 = 452.6;
				deformationPotential = -1.8;
			}
			break;
			case 2: {
				label = "gasb";
				bandgap = 0.812;
				latticeConstant = 6.08174;
				VBO = -0.03;
				ac = -7.5;
				av = -0.8;
				c11 = 884.2;
				c12 = 402.6;
				deformationPotential = -2.0;
			}
			break;
			case 3: {
				label = "insb";
				bandgap = 0.235;
				latticeConstant = 6.4794;
				VBO = 0;
				ac = -6.94;
				av = -0.36;
				c11 = 684.7;
				c12 = 373.5;
				deformationPotential = -2.0;
			}
			break;
			case 4: {
				label = "inp";
				bandgap = 1.4236;
				latticeConstant = 5.8697;
				VBO = -0.94;
				ac = -6.0;
				av = -0.6;
				c11 = 1011;
				c12 = 561;
				deformationPotential = -2.0;
			}
		
		}
	}
}
