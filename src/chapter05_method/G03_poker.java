package chapter05_method;

import java.util.Iterator;

/* 予定。
 * ディーラーのリロール判定メソッド
 * result関係
 * リトライ関係
 * オプション（リロール数等）によるコイン倍率の変化
 * リロールでのコイン
 * score[1]関連	
 * pHandsとdHandsに分けてオプションとして追加
 * 
 */

public class G03_poker {
	public static void main(String[] args) {
		int playerMax = 2;
		int dealerMax = 2;
		int handsMax = 5;
		int pRerollMax = 3;
		int dRerollMax = 3;
		int dRerollLevel = 450;

		int totalPersons = playerMax + dealerMax;
		String[] playerName = new String[] { "player1", "p2", null, null };
		int[] deck = new int[13 * 4];
		int[][] hands = new int[totalPersons][handsMax];
		int[] rerollIndex = new int[handsMax];
		int[] rerollStock = new int[totalPersons];
		int[] topPlayer = new int[totalPersons];
		int[][] score = new int[totalPersons][2];
		int[] usedIndex = new int[totalPersons]; // digitで保存
		int[] winner = new int[totalPersons];
		int[][] input = new int[totalPersons][2];// 0にインプット、1にflag(0:初期値,1:openingAnnounce済,2:hold)

		for (int i = 0; i < playerMax; i++) {
			rerollStock[i] = pRerollMax;
		}
		for (int i = playerMax; i < dealerMax; i++) {
			rerollStock[i] = dRerollMax;
		}
		// debug
//		int[] num = {4,5,7,3,1,8,12,46};
//		sort(num);
////		
//		for(int value:num)
//		System.out.print(value+" ");
//		int y = new java.util.Scanner(System.in).nextInt();

////	debug
//	int[][] debugHands = { { 6, 11, 8, 7, 10, 9, 12 } };
//	int debugPlayer = 0;
//	int[] x = straightJudge(debugPlayer, debugHands);
//	System.out.println(x[0] + "---" + x[1] + "debugおわり");
//	int y = new java.util.Scanner(System.in).nextInt();

		playerConf(playerName, playerMax, totalPersons);
		openingDraw(deck, hands);
		for (int nowPlayer = 0; nowPlayer < totalPersons; nowPlayer++) {// プレイヤー数文のforるーぷ
			if (nowPlayer < playerMax) {// プレイヤーorディーラー分岐
				while (input[nowPlayer][0] != 2) {// 以下、リロールやめるまでループ
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
//				dealerRerollJudge();
			}
		}

		handsSort(hands);
		for (int player = 0; player < totalPersons; player++)
			scoreClearing(player, hands, score);
		winnerJudge(score, winner);
//		if (winner.length > 1) {
//			drawResult();
//		} else {
//			result();
//		}
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
		System.out.println("以上、" + totalPersons + "名のプレイヤーでお送りします。");
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
		System.out.println("	" + playerName[nowPlayer] + "'s turn");
	}

	public static void HandsDisplay(int nowPlayer, String[] playerName, int[][] hands) {
//		System.out.print(playerName[nowPlayer] + " ");
		System.out.print(" →　");
		for (int i = 0; i < hands[0].length; i++) {
			System.out.print("【" + convertCard(hands[nowPlayer][i]) + "】 ");
		}
		handsSort(hands);
		System.out.println();
//		for(int i = 0 ; i < playerName[nowPlayer].length()-1;i++) {
//			System.out.print(" ");
//		}
		System.out.print("　 ");
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

// リロールの枚数とインデックスの最大値チェック
// エラーが出たらインプットからループ
	public static void inputAndErrorCheck(int nowPlayer, int hands[][], int[][] input) {
		boolean length = true;
		boolean size = true;
		while (true) {
			input[nowPlayer][1] = new java.util.Scanner(System.in).nextInt();
			if (input[nowPlayer][1] == 0) {
				input[nowPlayer][0] = 2;
				return;
			}
			int temp = input[nowPlayer][1];
			int digit = 0;
			
			length = true;
			size = true;
			
			while (temp != 0) {
				if (temp % 10 > hands[nowPlayer].length) {
					System.out.print(temp%10 + "は無効です。　＞");
					length = false;
				}
				digit++;
				temp /= 10;
				if (digit > hands[nowPlayer].length) {
					System.out.print(digit - hands[nowPlayer].length + "桁多いです。 ＞");
					size = false;
				}
				if (length&& size )
					return;
			}
		}
	}

	public static void getRerollIndex(int nowPlayer, int[][] hands, int[][] input, int[] rerollIndex) {
		for (int i = 0; i < hands[nowPlayer].length; i++) {
			rerollIndex[i] = (input[nowPlayer][1] % 10 - 1);
			input[nowPlayer][1] /= 10;
//		System.out.println(rerollIndex[i]); // debug
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

//	public static void dealerRerollJudge(int nowPlayer, int[][] hands, int[] rerollStock,int dRerollLevel,int[][] score) {
//		score[nowPlayer]
//		if (score[nowPlayer][0] < dRerollLevel) {
//			
//		}
//
////		}
//	}

// カードを引く前にデッキを確認するメソッド。
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

// カードindexをdisplay用に変換するメソッド。
	public static String convertCard(int card) {
		String[] suit = new String[] { "♠", "♡", "♦", "♧" };
		String cardMark = "";
		int cardNum = card % 13 + 1;

		switch (cardNum) {
		case 1:
			cardMark = "A";
			break;
		case 11:
			cardMark = "J";
			break;
		case 12:
			cardMark = "Q";
			break;
		case 13:
			cardMark = "K";
			break;
		default:
			break;
		}
		String convertedCard = "";
		if (cardNum > 10 || cardNum == 1) {
			convertedCard = suit[card / 13] + (cardMark);
		} else {
			convertedCard = suit[card / 13] + (cardNum);
		}
		return convertedCard;
	}

// インデックスの数だけ100倍するメソッド
	public static int convertDigit(int num, int count) {
		int convertedDigit = num;
		for (int i = 0; i < count; i++) {
			convertedDigit *= 100;
		}
		return convertedDigit;
	}

	// ストレート系判定用
	public static void handsSort(int[][] hands) {
		for (int p = 0; p < hands.length; p++) {
			for (int i = 0; i < hands[p].length - 1; i++) {
				for (int j = i + 1; j < hands[p].length; j++) {
					if (hands[p][i] % 13 < hands[p][j] % 13) {
						int temp = hands[p][i];
						hands[p][i] = hands[p][j];
						hands[p][j] = temp;
					}
				}
			}
		}
	}

	public static void arraySort(int[] num) {
		for (int i = 0; i < num.length - 1; i++) {
			for (int j = i + 1; j < num.length; j++) {
				if (num[i] < num[j]) {
					int temp = num[i];
					num[i] = num[j];
					num[j] = temp;
				}
			}
		}
	}

	// 1位を探すメソッド
	public static void winnerJudge(int[][] score, int[] winner) {
		int maxScore = 0;
		int tied = 0;
		for (int i = 0; i < score.length; i++) {
			if (maxScore < score[i][0]) {
				maxScore = score[i][0];
				winner[0] = i;
				for (int j = 1; j < winner.length; j++)
					winner[j] = -1;
				tied = 0;
			} else if (maxScore == score[i][0]) {
				tied++;
				winner[tied] = i;
			}
			
		}

		if (tied > 0) {
			int maxNum = 0;
			int[] tbWinner = new int[tied];
			int tbTied = 0;
			for (int i = 0; i < tied + 1; i++) {
				if (maxNum < score[winner[i]][1]) {
					maxNum = score[winner[i]][1];
					tbWinner[0] = winner[i];
					for (int j = 1; j < tbWinner.length; j++)
						tbWinner[j] = 0;
					tbTied = 0;
				} else if (maxNum == score[winner[i]][1]) {
					tbTied++;
					tbWinner[tbTied] = i;
				}
			}
			for (int i = 0; i < tbTied + 1; i++) {
				winner[i] = tbWinner[i];
			}
		}
	}

	public static void scoreClearing(int player, int[][] hands, int[][] score) {
		if (royalJudge(player, hands) == 3 && flashJudge(player, hands)[0] == 4) {
			score[player][0] = 900;
		} else if (straightJudge(player, hands)[0] == 4 && flashJudge(player, hands)[0] == 4) {
			score[player][0] = 800;
		} else if (matchComboJudge(player, matchCount(player, hands))[0] == 700) {
			score[player][0] = 700;
		} else if (matchComboJudge(player, matchCount(player, hands))[0] == 600) {
			score[player][0] = 600;
		} else if (flashJudge(player, hands)[0] == 4) {
			score[player][0] = 550;
		} else if (straightJudge(player, hands)[0] == 4) {
			score[player][0] = 500;
		} else {
			score[player][0] = matchComboJudge(player, matchCount(player, hands))[0];// 450,300,150
		}
	}

	public static int royalJudge(int player, int[][] hands) {
		int royal = 0;
		if (hands[player][0] != 13 || hands[player][hands[player].length - 1] != 1) {
			return royal;
		} else {
			for (int i = 1; i < hands[player].length - 1; i++) {
				if (hands[player][i] == 12) {
					if (hands[player][i - 1] > hands[player][i] + 1)
						break;
					if (hands[player][i - 1] == hands[player][i])
						continue;
					if (hands[player][i - 1] == hands[player][i] + 1) {
						royal++;
						if (royal == 4) {
							return royal;
						}
					}
				}
			}
		}
		return royal;
	}

	public static int[] straightJudge(int player, int[][] hands) {
		int[] straight = new int[3]; // 0：成立判定 1:参照値 2:使用したインデックス
		for (int i = 0; i < hands[player].length - 4; i++) {
			straight[0] = 0;
			straight[2] = 0;
			for (int j = i + 1; j < hands[player].length - 1; j++) {
				if (hands[player][j - 1] > hands[player][j] + 1)
					break;
				if (hands[player][j - 1] == hands[player][j])
					continue;
				if (hands[player][j - 1] == hands[player][j] + 1) {
					straight[0]++;
					straight[2] += convertDigit(j, straight[0]);
					if (straight[0] == 4) {
						straight[1] = i;
						return straight;
					}
				}
			}
		}
		straight[0] = 0;
		return straight;
	}

	public static int[] flashJudge(int player, int[][] hands) {

		// フラッシュ判定
		int[] flash = new int[2];
		for (int i = 1; i < hands[player].length; i++) {
			if (hands[player][0] / 13 == hands[player][i] / 13) {
				flash[0]++;
			} else {
				break;
			}
		}
		return flash;
	}

	public static int[] matchCount(int player, int[][] hands) {
		int[] match = new int[hands[player].length];
		for (int i = 0; i < hands[player].length; i++) {
			for (int j = 0; j < hands[player].length; j++) {
				if (hands[player][i] == hands[player][j]) {
					match[i]++;
				}
			}
		}
		return match;
	}

	public static int[] matchComboJudge(int player, int[] match) {
		int[] matchCombo = new int[2];
		for (int i = 0; i < match.length; i++) {
			switch (match[i]) {
			case 4:
				matchCombo[0] += 175;
				break;
			case 3:
				matchCombo[0] += 150;
				break;
			case 2:
				matchCombo[0] += 75;
				break;
			case 1:
				matchCombo[0] += 0;
				break;
			}
		}
		return matchCombo;
	}
}