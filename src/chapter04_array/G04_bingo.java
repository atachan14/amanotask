package chapter04_array;

public class G04_bingo {
	public static void main(String[] args) {

		int cardSize = 5;
		int[] cardTemp = new int[cardSize * cardSize];
		int numMax = 99 + 1;
		int[][] card = new int[cardSize][cardSize];
		int numTotal = 60;
		int[] cardDoubleCheck = new int[numMax];
		int[] numDoubleCheck = new int[numMax];

		int hole = numMax + 1;
		int reach = 0;
		int bingo = 0;
		int resBingo = 0;

		//被らないよう数字をカードサイズ分決める
		for (int i = 0; i < cardTemp.length; i++) {
			cardTemp[i] = new java.util.Random().nextInt(numMax);
			if (cardDoubleCheck[cardTemp[i]] == 1) {
				i--;
			} else {
				cardDoubleCheck[cardTemp[i]]++;
			}
		}
		//並び変える
		for (int i = 0; i < cardTemp.length - 1; i++) {
			for (int j = 0; j < cardTemp.length - 1; j++) {
				if (cardTemp[j] > cardTemp[j + 1]) {
					int temp = cardTemp[j];
					cardTemp[j] = cardTemp[j + 1];
					cardTemp[j + 1] = temp;
				}
			}
		}
		//カードに入れる
		int cardTempCount = 0;
		for (int i = 0; i < cardSize; i++) {
			for (int j = 0; j < cardSize; j++) {
				card[j][i] = cardTemp[cardTempCount];
				cardTempCount++;
			}
		}

		card[2][2] = hole;

		boolean untilHit = false;
		//カード表示
		for (int i = 0; i < cardSize; i++) {
			System.out.println();
			for (int j = 0; j < cardSize; j++) {
				if (card[i][j] == hole) {
					System.out.print("【●】");
					continue;
				}
				if (card[i][j] / 10 == 0) {
					System.out.print("【0" + card[i][j] + "】");
				} else {
					System.out.print("【" + card[i][j] + "】");
				}

			}

		}

		System.out.print("　0.next 9.until hit＞");
		int input = new java.util.Scanner(System.in).nextInt();
		if (input == 9)
			untilHit = true;

		System.out.println();

		int num = 0;
		int numStack = 0;

		while (numStack < numTotal) {

			//数決定
			num = new java.util.Random().nextInt(numMax);
			if (numDoubleCheck[num] == 1) {
				continue;
			} else {
				numDoubleCheck[num]++;
				numStack++;
				System.out.print(numStack + "個目　");
				if (num / 10 == 0) {
					System.out.print("[0" + num + "]　");
				} else {
					System.out.print("[" + num + "]　");
				}
			}

			//アタリかハズレの判定
			boolean hit = false;
			for (int i = 0; i < cardSize; i++) {
				for (int j = 0; j < cardSize; j++) {
					if (num == card[i][j]) {
						card[i][j] = hole;
						hit = true;
						break;
					}
				}
			}

			//当たってたらカードとリーチビンゴ表示
			if (hit == true) {
				System.out.println("あたり！");
				System.out.println();
				for (int i = 0; i < cardSize; i++) {
					if (i >= 1)
						System.out.println();
					for (int j = 0; j < cardSize; j++) {
						if (card[i][j] == hole) {
							System.out.print("【●】");
							continue;
						}
						if (card[i][j] / 10 == 0) {
							System.out.print("【0" + card[i][j] + "】");
						} else {
							System.out.print("【" + card[i][j] + "】");
						}
					}
				}
				int count = 0;
				int count2 = 0;
				//リーチとビンゴの判定 横 縦
				for (int i = 0; i < cardSize; i++) {
					count = 0;
					count2 = 0;
					for (int j = 0; j < cardSize; j++) {
						if (card[i][j] == hole) {
							count++;
							if (count == 4)
								reach++;
							if (count == 5) {
								reach--;
								bingo++;
							}

						}
						if (card[j][i] == hole) {
							count2++;
							if (count2 == 4)
								reach++;
							if (count2 == 5) {
								bingo++;
								reach--;
							}
						}
					}
				}

				count = 0;
				count2 = 0;
				//リーチとビンゴの判定　斜め
				for (int i = 0; i < cardSize; i++) {
					if (card[i][i] == hole) {
						count++;
						if (count == 4)
							reach++;
						if (count == 5) {
							bingo++;
							reach--;
						}
					}
					if (card[i][(cardSize - 1) - i] == hole) {
						count2++;
						if (count2 == 4)
							reach++;
						if (count2 == 5) {
							bingo++;
							reach--;
						}
					}
				}

				if (reach >= 1 || bingo >= 1)
					System.out.println();
				if (reach >= 1)
					System.out.print(reach + "リーチ　");
				if (bingo >= 1)
					System.out.print(bingo + "ビンゴ　");
				reach = 0;
				resBingo = bingo;
				bingo = 0;

				System.out.print("　0.next 9.until hit＞");
				input = new java.util.Scanner(System.in).nextInt();
				if (input == 9) {
					untilHit = true;
				} else {
					untilHit = false;
				}
				System.out.println();

				//はずれてたら
			} else {
				if (untilHit == true) {
					System.out.print("はずれ ＞until hit");
					System.out.println();
				} else {
					System.out.print("はずれ　0.next 9.until hit＞");
					input = new java.util.Scanner(System.in).nextInt();
					if (input == 9) 
						untilHit = true;
				}
			}
		}
		System.out.println("終了～～");
		System.out.println("結果は" + resBingo + "ビンゴでした！");
		System.out.println();
	}
}