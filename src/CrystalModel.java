import java.util.Random;

/**
 * CrystalModel är en klass som representerar ett elektrolytbad som omges med en
 * cirkulär anod av zink, och i vars mitt en kolkatod är placerad. Därefter läggs
 * en svag spänning över systemet.
 * 
 ...
 * @author 
 *
 */

public class CrystalModel {
	// variabler
	private int escapeCircleRadius; // radius of escape circle
	private int startCircleRadius; // radius of start circle
	// the bath
	private boolean[][] modelRep; // (model Representation) is the bolean matxix
	// position of walking ion in our coordinate system, the bath,
	// where 0,0 is in the middle
	private int x = 0;  // xBath
	private int y = 0;  // yBath
	
	private int bathSize;
	
	private Random genStep;
	/**
	 * Skapar en modell av kristallbadet (elektrolytbadet).
	 * @param size Kristallbadets bredd
	 */
	public CrystalModel(int size) {
		escapeCircleRadius = size/2-4; //(-4 to awoid indexOutOfBounds)
		startCircleRadius = escapeCircleRadius - (int)(0.1*escapeCircleRadius);
		this.bathSize = size;
		reset();
	}
	
	// getters
	public int getX() { 
		return x;
	}
	public int getY() {
		return y;
	}
	public int getEscapeCircleRadius() {
		return this.escapeCircleRadius; // TODO
	}
	public int getBathSize() {
		return this.bathSize;
	}
	
	/**
	 * Kontrollera om det finns en kristalliserad jon på position x,y.
	 * @param x koordinaten
	 * @param y koordinaten
	 * @return "true" om det finns en kristalliserad jon på position x,y.
	 */
	public boolean getModelValue(int x, int y) {
		//System.out.println(x + " " + y);
		return modelRep[xBathToModelRep(x)][yBathToModelRep(y)];
	}

	/**
	 * Släpper en ny jon och flyttar jonen ett steg åt gången tills den
	 * kristalliseras. Kommer den utanför flyktcirkeln så släpps en ny jon.
	 * @return "false" när kristallen är klar (dvs när sista jonen kristalliseras
	 * på startcirkeln) och "true" om vi kan kristallisera fler joner
	 */
    public boolean crystallizeOneIon() {
		// TODO
    	dropNewIon();
    	
    	genStep = new Random();
    	
    	do {
    		if (anyNeighbours(x,y) && !getModelValue(x,y)) {
    			System.out.println("ion x: " + this.x + ", ion y: " + this.y);
    			this.modelRep[xBathToModelRep(x)][yBathToModelRep(y)] = true;
    			if (onCirclePerimeter(this.startCircleRadius, x, y)) {
    				return false;
    			}
    			else {
    				return true;
    			}
    		}
    		else {
    			int step = genStep.nextInt(4);
    			
    			switch(step) {
    				case 0:
    					x += 1;
    					break;
    				case 1:
    					x -= 1;
    					break;
    				case 2: 
    					y += 1;
    					break;
    				case 3:
    					y -= 1;
    					break;
    			};
    		}
    		
    	} while (!outsideCicle(this.escapeCircleRadius, x, y));
    	return crystallizeOneIon();
	}
	
	/** ...
	*/
	public boolean runSomeSteps(int steps) {
		int i= 0;
		boolean goOn = false;
		do {
			goOn = crystallizeOneIon();
			i++;
		} while ( i<steps && goOn );
		return goOn; // we are done
	}
	
	/**
	 * Initierar modellen (dvs matrisen) och lägger en första kristalliserad jon mitt i "badet". 
	 */
	public void reset() {
		this.modelRep = new boolean[bathSize][bathSize];
		this.modelRep[xBathToModelRep(0)][yBathToModelRep(0)] = true;// TODO
	}
	
	/**
	 * Kollar om position x,y är utanför (eller på) cirkeln med radie r.
	 * Använder pytagoras sats.
	 * @param r Cirkelns radie.
	 * @param x koordinaten
	 * @param y koordinaten
	 * @return "true" om positionen är utanför cirkeln
	 */
	public static boolean outsideCicle(int r, int x, int y) {
		return ( Math.pow(r, 2) < Math.pow(x, 2) + Math.pow(y, 2) ); // TODO
	}
	
	private static boolean onCirclePerimeter(int r, int x, int y) {
		return (Math.pow(r, 2) - 1 <= Math.pow(x, 2) + Math.pow(y, 2));
	}

	/**
	* Returns the crystals state i.e. a string according to figure 3 i labPM. 
	* x and y is the position of the ion in the bath
	* @return A string that draws the crystal.
	*/
	public String toString() {
		int x = getX(); // the ions position in the bath
		int y = getY();
		int size = getEscapeCircleRadius();
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
	
	
	/**
	 * Släpper en jon på startcirkeln (dvs slumpar fram en ny punkt x,y på startcirkeln).
	 */
	private void dropNewIon() {
		// we know the radius (=startCircleRadius), 
		// we need to generate a random angle 0<=angle<360
		// and transform to a x,y cordinate
		double startAngle = Math.random() * 2 * Math.PI;
		this.x = (int)(this.startCircleRadius * Math.cos(startAngle));
		this.y = (int)(this.startCircleRadius * Math.sin(startAngle));// TODO
	}
	
	/**
	 * Omvandlar en "bad"-kordinat till ett matris värde.
	 * All access till matrisen måste transformeras i.e. 0,0 -> size/2, size/2
	 * @param x "bad"-koordinat som ska omvandlas
	 * @return motsvarande x-koordinat iför matrisen
	 */
	private int xBathToModelRep(int x) {
		return x+escapeCircleRadius+4;
	}
	private int yBathToModelRep(int y) {
		return escapeCircleRadius-y+4;
	}
	
	/**
	 * Kollar om jonen på position x,y har några grannar som kristalliserats.
	 * @param x koordinaten
	 * @param y koordinaten
	 * @return "true" om jonen har några grannar som kristalliserats
	 */
	private boolean anyNeighbours(int x, int y) {
		// if close to some ion in matrix return true
		return (this.modelRep[xBathToModelRep(x-1)][yBathToModelRep(y)] || this.modelRep[xBathToModelRep(x+1)][yBathToModelRep(y)] ||
				this.modelRep[xBathToModelRep(x)][yBathToModelRep(y-1)] || this.modelRep[xBathToModelRep(x)][yBathToModelRep(y+1)]); // TODO
	}
	
}