
public class Steg1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CrystalModel model = new CrystalModel(100);
		while (model.crystallizeOneIon()) {
			System.out.println(model.toString());
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException ex) {
				System.out.println("InterruptedException");
			}
		}
		System.out.println(model.toString());
		//System.out.println(model.printAll());
	}

}
