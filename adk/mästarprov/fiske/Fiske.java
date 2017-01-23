import java.util.ArrayList;

public class Fiske {
	public static void main(String[] args) {
		matrix();
	}
	
	public static void matrix() {
		int[][] matrix = new int[6][4];
		int[] row1 = {6,0,0,0};
		int[] row2 = {4,5,0,0};
		int[] row3 = {5,5,13,0};
		int[] row4 = {4,6,10,2};
		int[] row5 = {8,7,8,1};
		int[] row6 = {12,3,9,5};

		matrix[0] = row1;
		matrix[1] = row2;
		matrix[2] = row3;
		matrix[3] = row4;
		matrix[4] = row5;
		matrix[5] = row6;

		int[][] maxm = new int[6][4];
		maxm = matrix;
		int n = 5;
		int j = 3;
		int k = 3;
		for(j = j - 1; j >= 0; j--) {
			for(int i = n - 1; i >= j; i--) {
				//maxm[i][k] = max(matrix[i][k], maxm[i+1][k]);
				//maxm[i][j] = max(matrix[i][j] + maxm[i+1][j+1], maxm[i+1][j]);
				matrix[i][k] = max(matrix[i][k], matrix[i+1][k]);
				matrix[i][j] = max(matrix[i][j] + matrix[i+1][j+1], matrix[i+1][j]);
			}
		}

		/*
		j = 1;
		for(int i = 4; i >= 0; i--) {
			int max = 0;
			for(int ii = i + 1; ii <= n; ii++) {
				if(matrix[ii][j+1] > max) {
					max = matrix[ii][j+1];
				}
			} 
			matrix[i][1] += max;

		}
		j = 0;
		for(int i = 4; i >= 0; i--) {
			System.out.println("i: "+ i);
			int max = 0;
			for(int ii = i + 1; ii <= n; ii++) {
				if(matrix[ii][j+1] > max) {
					max = matrix[ii][j+1];
				}
			} 
			matrix[i][0] += max;

		}
		*/
		int[] path = new int[4];
		int row = 0;
		
		j = 0;
		int i = 0;
		int col = 0;
		while(j < 4 && i < 6) {
			int max = 0;
			while(i < 6) {
				if(max <= matrix[i][j]) {
					max = matrix[i][j];
					row = i;
					col = j;
				}
				i++;
			}
			path[j] = row;
			i = row + 1;
			j = col + 1;
		}
		for(i = 0; i < 4; i++) {
			System.out.print(path[i]);
			System.out.println("");
		}
		
		for(i = 0; i < 6; i++){
			System.out.print("[");
			for(j = 0; j < 4; j++) {
				if(j == 3) System.out.println(matrix[i][j] + "]");
				else System.out.print(matrix[i][j] + ", ");
			}
			
		}
	}
	public static int max(int a, int b) {
		if(a >= b) return a;
		else return b;
	}
}