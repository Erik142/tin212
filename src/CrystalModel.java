import java.util.ArrayList;
import java.util.Random;

public class CrystalModel {
	private ArrayList<Ion> crystals;
	private Ion ion;
	private int radius;
	private int startR;
	
	public CrystalModel(int width) {
		this.radius = (int)width/2;
		this.startR = (int)(0.9*this.radius);
		this.reset();
	}
	
	public int getSize() {
		return 2*radius;
	}
	
	public ArrayList<Ion> getCrystals() {
		return this.crystals;
	}
	
	public boolean crystallizeOneIon() {
		/*Släpp en ny jon -> dropNewIon()
		Flytta denna ett steg åt gången tills den
		kristalliseras.
		Om jonen kommer utanför flyktcirkeln så
		släpps en ny jon.
		Metoden returnerar false när kristallen är klar
		(dvs när sista jonen kristalliseras på startcirkeln,
		och true om vi kan kristallisera fler joner.*/
		
		dropNewIon();
		
		
		//Flytta jonen
		Random r = new Random();
		while(!outsideCircle(this.radius, ion.getX(), 
				ion.getY())) {
		int move = r.nextInt(4);
		
		switch(move) {
			case 0: 
				ion.setY(ion.getY() + 1);
				break;
			case 1: 
				ion.setY(ion.getY() - 1);
				break;
			case 2: 
				ion.setX(ion.getX() + 1);
				break;
			case 3: 
				ion.setX(ion.getX() - 1);
				break;
		}
		
		//System.out.println("xPos = " + ion.getX() + ", yPos = " + ion.getY());
		
		//!outsideCircle ->
		//anyNeighbours ->
		if (!getModelValue(ion.getX(),ion.getY()) && anyNeighbours(ion.getX(),ion.getY())) {
			crystals.add(ion);
			break;
		}
		}
		if (outsideCircle(this.radius, ion.getX(), ion.getY())) {
			crystallizeOneIon();
		}
		return !(Math.pow(this.startR, 2) == 
				Math.pow(ion.getX(), 2) + 
				Math.pow(ion.getY(), 2));
		
		//crystals.add(ion)
		
		//Pythagoras -> true/false
	}
	
	public boolean getModelValue(int x, int y) {
		
		return crystals.contains(new Ion(x,y));
	}
	
	public boolean outsideCircle(int r, int x, int y) {
		return (Math.pow(r, 2) < (Math.pow(x, 2) + 
				Math.pow(y, 2)));
	}
	
	public boolean anyNeighbours(int x, int y) {
		
		Ion i1 = new Ion(x-1,y);
		Ion i2 = new Ion(x+1,y);
		Ion i3 = new Ion(x,(int)(y-1));
		Ion i4 = new Ion(x,y+1);
		
		return (crystals.contains(i1) || crystals.contains(i2)
				|| crystals.contains(i3) || crystals.contains(i4));
		
	}
	
	public void dropNewIon() {
		//this.ion = new Ion();
		int x = generatePosition(startR);
		int bound = (int)(Math.sqrt(Math.pow(startR, 2) -
				Math.pow(x, 2)));
		int y = generatePosition(bound);
		//System.out.println("bound = " + bound);
		if (!getModelValue(x,y)) {
			this.ion = new Ion(x,y);
		}
		else {
			dropNewIon();
		}
	}
	
	private int generatePosition(int bound) {
		if (bound != 0) {
		Random rand = new Random();
		return rand.nextInt(2*bound) - bound;
		}
		return 0;
	}
	
	public void reset() {
		crystals = new ArrayList<Ion>();
		crystals.add(new Ion(0,0));
	}
	
	public int xBathToModelRep(int x) {
		return x + this.radius;
	}
	
	public String printAll() {
		String builder = "";
		for(int i = 0; i < crystals.size(); i ++) {
			builder = builder + "xPos = " + crystals.get(i).getX() + ", yPos = " + crystals.get(i).getY() + "\n";
		}
		return builder;
	}
	
	public String toString() {

        int x = ion.getX(); // the ions position in the bath
        int y = ion.getY();
        int size = this.radius;
        StringBuffer s = new StringBuffer(1000);
        for(int i=-size-1; i<size+1; i++) {
            s.append("-");
        }
        s.append("\n");
        for(int i=-size; i<size; i++) {
            s.append("|");
            for(int j=-size; j<size; j++) {
                if (getModelValue(i, j)) {
                    if (i==x && j==y) {
                        s.append("#");
                    } else {
                        s.append("*");
                    }
                } else {
                    s.append(" ");
                }
            }
            s.append("|");
            s.append("\n");
        }
        for(int i = -size-1; i < size+1; i++) {
            s.append("-");
        }
        s.append("\n");
        return s.toString();
	}
}
