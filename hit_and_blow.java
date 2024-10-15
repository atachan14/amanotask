package tempproject;

public class hit_and_blow {

	public static int hit_and_blow(int coin) {
//			public static void main(String[] args) {
//		int coin = 100;
		int digit = 3;
		int maxNum = 9;
		int[] num = new int[digit];
		int hit = 0;
		int blow = 0;
		int bet = 0;

		bet = opening();
		if (bet == 0) {
			return coin;
		}
		if(bet > coin) {
		System.out.println("");
		}
		decideNum(num, maxNum, digit);

		while (hit != 2 || bet != 0) {
			answerInput();
		}
	}

	public static int opening() {
		System.out.println("		THT HIT AND BLOW");
		System.out.println();
		System.out.print("	input bet coin (0.やめる) ＞");
		int openingInput = new java.util.Scanner(System.in).nextInt();
		return openingInput;
	}

	public static void decideNum(int[] num, int max, int digit) {
		boolean loop = true;
		while (loop) {
			loop = false;
			for (int i = 0; i < digit; i++) {
				num[i] = new java.util.Random().nextInt(max) + 1;
			}
			for (int i = 0; i < num.length - 1; i++) {
				for (int j = i + 1; j < num.length; j++) {
					if (num[i] == num[j]) {
						loop = true;
					}
				}
			}
		}
		for (int i : num) //debug
			System.out.print(" " + i + " "); //debug
	}

	public static void answerInput(int bet) {
		System.out.print("あててね。　現在の報酬："+bet+"coin　＞");
		int ans = new java.util.Scanner(System.in).nextInt();
	}

	public static boolean digitErrorCheck(int digit, int num) {
		int temp = num;
		int count=0;
		while(temp == 0) {
			temp %= 10;
			count++;
			if(count == digit)
				return false;
		}
		return true;
	}
}