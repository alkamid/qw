
public class CompoundTernary {

	CompoundBinary binary1, binary2;
	double bowingBandgap, bowingVBO;
	
	public CompoundTernary(CompoundBinary binary1, CompoundBinary binary2, double bowingBandgap, double bowingVBO) {
		this.binary1 = binary1;
		this.binary2 = binary2;
		this.bowingBandgap = bowingBandgap;
		this.bowingVBO = bowingVBO;
		
	}
	
	public double valueVBO(double x) {
		return (1-x)*this.binary1.VBO+x*this.binary2.VBO-this.bowingVBO*x*(1-x);
	}
	public double valueBandgap(double x) {
		return (1-x)*this.binary1.bandgap+x*this.binary2.bandgap-this.bowingBandgap*x*(1-x);
	}
	
}
