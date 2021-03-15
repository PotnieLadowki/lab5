import java.awt.geom.Rectangle2D;


public class Tricorn extends FractalGenerator {
	
		public static final int MAX_ITERATIONS = 2000;

		@Override
		public void getInitialRange(Rectangle2D.Double range) {
			range.x = -2;
			range.y = -2;
			range.width = 4;
			range.height = 4;
		}
	
		@Override
		public int numIterations(double x, double y) {
			int iteration = 0;
			double a = 0;
			double b = 0;
			
			while (iteration < MAX_ITERATIONS && a * a + b * b < 4 ) {
				double newA = a * a - b * b + x;
				double newB = -2 * a * b + y;
				a = newA;
				b = newB;
				iteration += 1;
			}
			
			if (iteration == MAX_ITERATIONS) {
				return -1;
			}
			
			return iteration;
		}
		
		public String toString() {
			return "Tricorn";
		}
}