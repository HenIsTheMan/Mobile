package sg.diploma.product.math;

import java.util.concurrent.ThreadLocalRandom;

public final class Pseudorand{ //Static class
	private Pseudorand(){
	}

	public static float PseudorandFloatMinMax(final float min, final float max){
		return min + ThreadLocalRandom.current().nextFloat() * (max - min);
	}

	public static int PseudorandIntMinMax(final int min, final int max){
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public static int PseudorandInt(){
		return ThreadLocalRandom.current().nextInt();
	}
}