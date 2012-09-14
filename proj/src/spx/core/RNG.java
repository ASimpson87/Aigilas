package spx.core;

import java.util.Random;

public class RNG {
	public static Random Rand;

	public static int Next(int min, int max) {
		if (max - min > 0) {
			return RNG.Rand.nextInt(max - min) + min;
		}
		return 0;
	}

	public static double Angle() {
		return RNG.Rand.nextInt(360) * Math.PI / 180;
	}

	public static int Negative(int radius) {
		return RNG.Rand.nextInt(Math.abs(radius) * 2) - Math.abs(radius);
	}

	public static boolean CoinFlip() {
		return RNG.Rand.nextInt(2) == 1;
	}

	public static void Seed(int seed) {
		Rand = new Random(seed);
	}
}