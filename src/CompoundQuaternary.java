
public class CompoundQuaternary {

	CompoundTernary ternary1, ternary2;
	double bowingBandgap, bowingVBO;
	String label;
	
	public CompoundQuaternary(CompoundTernary ternary1, CompoundTernary ternary2, double bowingBandgap, double bowingVBO) {
		this.ternary1 = ternary1;
		this.ternary2 = ternary2;
		this.bowingBandgap = bowingBandgap;
		this.bowingVBO = bowingVBO;
		this.label = "";
		
	}
	
	public double valueVBO(double x) {
		return (1-x)*this.ternary1.VBO+x*this.ternary2.VBO-this.bowingVBO*x*(1-x);
	}
	public double valueBandgap(double x) {
		return (1-x)*this.ternary1.bandgap+x*this.ternary2.bandgap-this.bowingBandgap*x*(1-x);
	}
	
	public CompoundQuaternary(CompoundTernary ternary1, CompoundTernary ternary2) {
		if (ternary1.label == "gainas" && ternary2.label == "gainsb") {
			this.label = "GaInAsSb";
			this.bowingBandgap = 0.0;
			this.bowingVBO = 0.0;
		}
	}
	
}
