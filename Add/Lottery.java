package test;

public class Lottery {

	public int calculateResult(int whiteBalls[], int powerBall) {
		return 0;
	}

	public static void main(String[] args) {
		
		int whiteBalls[] = new int[5];
		int powerBall = 0;
		
		if (args.length == 6) {
			for(int i=1;i<6;i++) {
				whiteBalls[i] = Integer.parseInt(args[i]);
			}
			
			//Added comment
			powerBall = Integer.parseInt(args[0]);
			
			Lottery baloto = new Lottery();
			
			int probability = baloto.calculateResult(whiteBalls, powerBall);
			
<<<<<<< .mine
			System.out.println("La probabilidad es: " + probability);			
>>>>>>> .r4
		}

	}

}