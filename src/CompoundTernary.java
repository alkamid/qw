
public class CompoundTernary {

	private CompoundBinary binary1, binary2;
	private double bowingBandgap, bowingVBO;
	private String label;
	
	public CompoundTernary(CompoundBinary bin1, CompoundBinary bin2, double bowingBandgapTemp, double bowingVBOTemp) {
		binary1 = bin1;
		binary2 = bin2;
		bowingBandgap = bowingBandgapTemp;
		bowingVBO = bowingVBOTemp;
		label = "";
		
	}
	
	public CompoundTernary(CompoundBinary bin1, CompoundBinary bin2) {
		binary1 = bin1;
		binary2 = bin2;
		if (binary1.label == "gaas" && binary2.label == "inas") {
			label = "GaInAs";
			bowingBandgap = 0.477;
			bowingVBO = -0.38;
		}
		if (binary1.label == "gasb" && binary2.label == "insb") {
			label = "GaInSb";
			bowingBandgap = 0.415;
			bowingVBO = 0;
		}
	}
	
	public double valueVBO(double x) {
		return (1-x)*binary1.VBO+x*binary2.VBO-bowingVBO*x*(1-x);
	}
	public double valueBandgap(double x) {
		return (1-x)*binary1.bandgap+x*binary2.bandgap-bowingBandgap*x*(1-x);
	}
	
}
