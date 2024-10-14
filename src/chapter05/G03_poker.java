package chapter05;

public class G03_poker {
	public static void main(String[] args) {
		int playerMax = 2;
		int dealerMax = 2;
		int handsMax = 5;
		int rerollMax = 3;

		int totalPersons = playerMax + dealerMax;
		String[] playerName = new String[] { "1", "2", "3", "4" };
		int[] deck = new int[13 * 4];
		int[][] hands = new int[totalPersons][handsMax];
		int[] rerollIndex = new int[handsMax];
		int[] rerollStock = new int[totalPersons];
		boolean dealerFlag = false;
		int[][] score = new int[totalPersons][2];// 0に役、1に数
		int[][] input = new int[totalPersons][2];// 0にインプット、1にhold flag

		for (int i = 0; i < totalPersons; i++) {
			rerollStock[i] = rerollMax;
		}

		// コピペ用
		// int nowPlayer,String[] playerName,int[][] hands,int[] rerollStock,,int[]
		// deck,
		// nowPlayer,playerName,hands,rerollStock,deck,

		playerConf(playerName, playerMax, totalPersons);
		openingDraw(deck, hands);
		// プレイヤー数分のforループ→プレイヤーかディーラーのif→ホールド条件のwhileループ→ホールドならスキップ
		for (int nowPlayer = 0; nowPlayer < totalPersons; nowPlayer++) {// プレイヤー数文のforるーぷ
			if (nowPlayer < playerMax) {// プレイヤーorディーラー分岐
				while (input[nowPlayer][0] != 2) {// リロールやめるまでループ
					if (input[nowPlayer][0] == 0) {
						playerAnnounse(nowPlayer, playerName);
						input[nowPlayer][0] = 1;
					}
					HandsDisplay(nowPlayer, playerName, hands);
					RerollDisplay(nowPlayer, hands, input, rerollStock);
					inputAndErrorCheck(nowPlayer, hands, input);
					if (input[nowPlayer][0] != 2) {// リロールやめたら後半スキップ
						getRerollIndex(nowPlayer, hands, input, rerollIndex);
						rerollDraw(nowPlayer, hands, rerollIndex, deck);
						rerollStock[nowPlayer]--;
					}

				}
			} else {
				playerAnnounse(nowPlayer, playerName);
				HandsDisplay(nowPlayer, playerName, hands);
//					dealerRerollJudge();
			}
		}

		// 判定
	}

	public static void playerConf(String[] playerName, int playerMax, int totalPersons) {
		for (int i = 0; i < playerMax; i++) {
			if (playerName[i] == null) {
				System.out.println("player" + (i + 1) + "の名前を入れて");
				playerName[i] = new java.util.Scanner(System.in).nextLine();
			}
		}
		for (int i = playerMax; i < totalPersons; i++) {
			if (playerName[i] == null) {
				playerName[i] = "dealer" + (i - playerMax + 1);
			}
		}

		for (String value : playerName)
			System.out.print(value + " ★ ");
		System.out.println("以上、" + totalPersons + "名のプレイヤーでお送りします");
	}

	public static void openingDraw(int[] deck, int[][] hands) {
		for (int i = 0; i < hands.length; i++) {
			for (int j = 0; j < hands[i].length; j++) {
				hands[i][j] = tempDraw(deck);
			}
		}
	}

	public static void playerAnnounse(int nowPlayer, String[] playerName) {
		System.out.println();
		System.out.println();
		System.out.println(playerName[nowPlayer] + "'s turn");
	}

	public static void HandsDisplay(int nowPlayer, String[] playerName, int[][] hands) {
		System.out.print(playerName[nowPlayer] + " ");
		for (int i = 0; i < hands[0].length; i++) {
			System.out.print("【" + convertCard(hands[nowPlayer][i]) + "】 ");

		}
	}

	public static void RerollDisplay(int nowPlayer, int[][] hands, int[][] input, int[] rerollStock) {
		System.out.print("(残りリロール" + rerollStock[nowPlayer] + "回) ");
		if (rerollStock[nowPlayer] == 0) {
			input[nowPlayer][0] = 2;
		} else {
			System.out.print("1~" + hands[nowPlayer].length + ".reroll");
		}
		System.out.print("  0.hold ＞");
	}

	public static void inputAndErrorCheck(int nowPlayer, int hands[][], int[][] input) {
		// リロールの枚数とインデックスの最大値チェック
		// エラーが出たらループ
		while (true) {
			input[nowPlayer][1] = new java.util.Scanner(System.in).nextInt();
			if (input[nowPlayer][1] == 0) {
				input[nowPlayer][0] = 2;
				return;
			}
			int temp = input[nowPlayer][1];
			int digit = 0;
			while (temp != 0) {
				if (temp % 10 > hands[nowPlayer].length)
					System.out.print(temp + "は無効です。　＞");
				digit++;
				temp /= 10;
			}
			if (digit > hands[nowPlayer].length)
				System.out.print(digit - hands[nowPlayer].length + "桁多いです。 ＞");

			if (digit <= hands[nowPlayer].length)
				return;
		}
	}

	public static void getRerollIndex(int nowPlayer, int[][] hands, int[][] input, int[] rerollIndex) {
		for (int i = 0; i < hands[nowPlayer].length; i++) {
			rerollIndex[i] = (input[nowPlayer][1] % 10 - 1);
			input[nowPlayer][1] /= 10;
//			System.out.println(rerollIndex[i]); // debug
		}
	}

	public static void rerollDraw(int nowPlayer, int[][] hands, int[] rerollIndex, int[] deck) {
		for (int i = 0; i < hands[nowPlayer].length; i++) {
			if (rerollIndex[i] == -1)
				break;
			// System.out.print(rerollIndex[i]+"★");//debug
			hands[nowPlayer][(rerollIndex[i])] = tempDraw(deck);
		}
	}

	public static void dealerRerollJudge(int nowPlayer, String[] playerName, int[][] hands, int[] rerollStock,
			int[] deck) {
//			if (score[nowPlayer] < dealerJudge) {

//			}
	}

	public static int tempDraw(int[] deck) {
		while (true) {
			int tempDraw = new java.util.Random().nextInt(deck.length);
			if (deck[tempDraw] == 0) {
				deck[tempDraw]++;
				// System.out.print(tempDraw+" ");
				return tempDraw;
			}
		}
	}

	public static String convertCard(int card) {
		String[] suit = new String[] { "♠", "♡", "♦", "♧" };
		int cardNum = card % 13 + 1;
		String convertedCard = suit[card / 13] + (cardNum);
		if (cardNum < 10) {
			convertedCard = suit[card / 13] + "0" + (cardNum);
		}
		return convertedCard;
	}

	public static void scoreClearing(int[][] Score) {
		//
//			boolean strait =straitFlag();
//			boolean royal = royalFlag();
//			boolean flash = flashFlag();
//			boolean
//			
//			if(royal && flash)
//				score[p][0]=9;
//			if(strait && flash)
//				comboScore[p][0] = 8;
//			if(forcard();)
//				comboScore[p][0] =7;
//			if(fullhouse();)
//				comboScore[p][0] = 8;

	}

	public static void scoreMath(int nowPlayer, boolean dealerFlag, String[] playerName, int[][] hands,
			int[] rerollStock, int[] deck, int[] rerollIndex) {
		// ストレート判定
		// ストレート判定

		int straight = 0;
		int royal = 0;
		for (int i = 1; i < hands[nowPlayer].length; i++) {
			if ((hands[nowPlayer][0] % 13 + 1) == (hands[nowPlayer][i] % 13 + 1) + i) {
				straight++;
			} else if ((hands[nowPlayer][0] % 13 + 1) == 13 && (hands[nowPlayer][i] % 13 + 1) == 1) {
				straight++;
			} else {
				straight = 0;
				break;
			}
		}
		// フラッシュ判定
		int flash = 0;
		for (int i = 1; i < hands[nowPlayer].length; i++) {
			if (hands[nowPlayer][0] / 13 == hands[nowPlayer][i] / 13) {
				flash++;
			} else {
				break;
			}
		}

		// ペア系判定
	}

}
