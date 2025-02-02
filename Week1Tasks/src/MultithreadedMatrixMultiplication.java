public class MultithreadedMatrixMultiplication {

	// Worker thread to compute one row of the result matrix
	static class MatrixMultiplierThread extends Thread {
		private int[][] matrixA;
		private int[][] matrixB;
		private int[][] result;
		private int row;

		public MatrixMultiplierThread(int[][] matrixA, int[][] matrixB, int[][] result, int row) {
			this.matrixA = matrixA;
			this.matrixB = matrixB;
			this.result = result;
			this.row = row;
		}

		@Override
		public void run() {
			for (int j = 0; j < matrixB[0].length; j++) {
				result[row][j] = 0;
				for (int k = 0; k < matrixA[0].length; k++) {
					result[row][j] += matrixA[row][k] * matrixB[k][j];
				}
			}
		}
	}

	// Multiplies two matrices using multithreading
	public static int[][] multiplyMatrices(int[][] matrixA, int[][] matrixB) {
		int rowsA = matrixA.length;
		int colsA = matrixA[0].length;
		int colsB = matrixB[0].length;

		if (colsA != matrixB.length) {
			throw new IllegalArgumentException("Matrix A's columns must match Matrix B's rows.");
		}

		int[][] result = new int[rowsA][colsB];
		Thread[] threads = new Thread[rowsA];

		// Create and start threads for each row
		for (int i = 0; i < rowsA; i++) {
			threads[i] = new MatrixMultiplierThread(matrixA, matrixB, result, i);
			threads[i].start();
		}

		// Wait for all threads to complete
		for (int i = 0; i < rowsA; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static void main(String[] args) {
		int[][] matrixA = { { 1, 2 }, { 3, 4 } };
		int[][] matrixB = { { 2, 0 }, { 1, 2 } };

		int[][] result = multiplyMatrices(matrixA, matrixB);

		System.out.println("Result of the multiplication:");
		for (int[] row : result) {
			for (int value : row) {
				System.out.print(value + " ");
			}
			System.out.println();
		}
	}
}
