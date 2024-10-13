package chapter05;

public class G03_poker {
	public static void main(String[] args) {
		int playerMax = 2;
		int dealerMax = 2;
		int totalPersons = playerMax + dealerMax;
		int handsMax = 5;
		int rerollMax = 3;

		String[] playerName = new String[] { "player1", "player2", "dealer", "" };
		int[] deck = new int[13 * 4];
		int[][] hands = new int[totalPersons][handsMax];
		int[] rerollStock = new int[totalPersons];
		int ngRerollDigit = 1;
		boolean dealerFlag = false;
		int[][] score = new int[totalPersons][2];// 0に役、1に数

		for (int i = 0; i < totalPersons; i++) {
			rerollStock[i] = rerollMax;
		}
		for (int i = 0; i < handsMax; i++) {
			ngRerollDigit *= 10;
		}

		// コピペ用
		// int nowPlayer,String[] playerName,int[][] hands,int[] rerollStock,int
		// ngRerollDigit,int[] deck,
		// nowPlayer,playerName,hands,rerollStock,ngRerollDigit,deck,

		// プレイヤーネームの確認とディーラー作成
		playerConf(playerName, playerMax, totalPersons);

		// それぞれに５枚のカード配る
		openingDraw(deck, hands);

		// 表示→input→リロールをプレイヤー分
		for (int nowPlayer = 0; nowPlayer < totalPersons; nowPlayer++) {
			if (nowPlayer < playerMax) {
				nowPlayerHandsDisplay(nowPlayer, dealerFlag, playerName, hands, rerollStock, ngRerollDigit, deck);
				nowPlayerRerollDisplay(nowPlayer, dealerFlag, playerName, hands, rerollStock, ngRerollDigit, deck);
			} else {
				dealerFlag = true;
				nowPlayerHandsDisplay(nowPlayer, dealerFlag, playerName, hands, rerollStock, ngRerollDigit, deck);
//					dealerRerollJudge();
			}
		}

		for (int nowDealer = 0; nowDealer < dealerMax; nowDealer++) {
			System.out.println("dealerのたーｎ");
		}
		// 判定
	}

	public static void playerConf(String[] playerName, int playerMax, int totalPersons) {
		for (int i = 0; i < playerMax; i++) {
			if (playerName == null) {
				System.out.println("player" + (i + 1) + "の名前を入れてね");
				playerName[i] = new java.util.Scanner(System.in).nextLine();
			}
		}
		for (int i = playerMax; i < totalPersons; i++) {
			playerName[i] = "dealer" + "i-playerMax-1";
		}
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

	public static void openingDraw(int[] deck, int[][] hands) {
		for (int i = 0; i < hands.length; i++) {
			for (int j = 0; j < hands[i].length; j++) {
				hands[i][j] = tempDraw(deck);
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

	public static void nowPlayerHandsDisplay(int nowPlayer, boolean dealerFlag, String[] playerName, int[][] hands,
			int[] rerollStock, int ngRerollDigit, int[] deck) {
		System.out.print(playerName[nowPlayer]);
		for (int i = 0; i < hands[0].length; i++) {
			System.out.print("【" + convertCard(hands[nowPlayer][i]) + "】");

		}
	}

	public static void nowPlayerRerollDisplay(int nowPlayer, boolean dealerFlag, String[] playerName, int[][] hands,
			int[] rerollStock, int ngRerollDigit, int[] deck) {
		System.out.print("残りリロール" + rerollStock[nowPlayer] + "回 0.hold");
		if (rerollStock[nowPlayer] != 0) {
			System.out.print(" 1~" + hands[nowPlayer].length + ".reroll");
		}
		System.out.print(" ＞");
		rerollInput(nowPlayer, dealerFlag, playerName, hands, rerollStock, ngRerollDigit, deck);
	}

	public static void rerollInput(int nowPlayer, boolean dealerFlag, String[] playerName, int[][] hands,
			int[] rerollStock, int ngRerollDigit, int[] deck) {

		if (rerollStock[nowPlayer] == 0) {
			inputHold(nowPlayer, playerName, hands, rerollStock, ngRerollDigit, deck);
		}
		int rerollInput = new java.util.Scanner(System.in).nextInt();
		if (rerollInput == 0) {
			inputHold(nowPlayer, playerName, hands, rerollStock, ngRerollDigit, deck);
		} else {
			rerollInputJudge(nowPlayer, dealerFlag, playerName, hands, rerollStock, ngRerollDigit, deck, rerollInput);
		}
	}

	public static void rerollInputJudge(int nowPlayer, boolean dealerFlag, String[] playerName, int[][] hands,
			int[] rerollStock, int ngRerollDigit, int[] deck, int rerollInput) {

		int[] rerollIndex = new int[hands[nowPlayer + 1].length];
		for (int i = 0; i < hands[nowPlayer].length; i++) {
			rerollIndex[i] = (rerollInput % 10 - 1);
			rerollInput /= 10;
			// System.out.println(rerollIndex[i]); //debug
		}

		for (int i = 0; i < rerollIndex.length; i++) {
			if (rerollIndex[i] >= rerollIndex.length) {
				System.out.print(rerollIndex[i] + 1 + "は無効です。");
				nowPlayerRerollDisplay(nowPlayer, dealerFlag, playerName, hands, rerollStock, ngRerollDigit, deck);
			}
		}
		rerollStock[nowPlayer]--;

		rerollDraw(nowPlayer, dealerFlag, playerName, hands, rerollStock, ngRerollDigit, deck, rerollIndex);
	}

	public static void dealerRerollJudge(int nowPlayer, String[] playerName, int[][] hands, int[] rerollStock,
			int ngRerollDigit, int[] deck) {
//			if (score[nowPlayer] < dealerJudge) {

//			}
	}

	public static void rerollDraw(int nowPlayer, boolean dealerFlag, String[] playerName, int[][] hands,
			int[] rerollStock, int ngRerollDigit, int[] deck, int[] rerollIndex) {
		for (int i = 0; i < hands[nowPlayer].length; i++) {
			if (rerollIndex[i] == -1) {
				break;
			}
			// System.out.print(rerollIndex[i]+"★");//debug
			hands[nowPlayer][(rerollIndex[i])] = tempDraw(deck);
		}
		nowPlayerHandsDisplay(nowPlayer, dealerFlag, playerName, hands, rerollStock, ngRerollDigit, deck);
		nowPlayerRerollDisplay(nowPlayer, dealerFlag, playerName, hands, rerollStock, ngRerollDigit, deck);
	}

	public static void inputHold(int nowPlayer, String[] playerName, int[][] hands, int[] rerollStock,
			int ngRerollDigit, int[] deck) {
		System.out.println();
		System.out.println();
		System.out.println("next player's opening reroll");
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
			int[] rerollStock, int ngRerollDigit, int[] deck, int[] rerollIndex) {
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
