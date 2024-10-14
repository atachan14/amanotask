package chapter03;

public class G02_dice_poker {
	public static void main(String[] args) {

		int playerTotal = 2; // プレイヤー数
		final int diceTotal = 5; // サイコロの個数 定数
		final int diceSide = 6; // サイコロの面数　定数
		int reroll = 2 + 1; // 最大リロール数
		int dealerR = 300; // ディーラーのリロール基準

		int[] score = new int[playerTotal]; // 各々のスコア
		String star = ""; // 勝敗履歴
		int starCount = 0;

		// プレイヤー名入力
		String[] player = new String[playerTotal];
		for (int i = 0; i < playerTotal; i++) {
			if (i == playerTotal - 1) {
				player[playerTotal - 1] = "dealer";
			} else {
				System.out.print("プレイヤー" + (i + 1) + "の名前を入力して！＞");
				player[i] = new java.util.Scanner(System.in).nextLine();
			}
		}


		// リトライのループ
		while (true) {
			int[] remit = new int[playerTotal]; // それぞれの残りリロール回数
			for (int i = 0; i < playerTotal; i++)
				remit[i] = reroll;
			System.out.println();

			// 投げる人分岐
			for (int p = 0; p < playerTotal; p++) {
				while (true) {
					
					// リロール残数-1とスコア初期化
					remit[p]--;
					score[p] = 0;

					// サイコロ投げてdiceResult[]に入れる
					int[] diceResult = new int[diceTotal];
					for (int i = 0; i < diceTotal; i++) {
						diceResult[i] = new java.util.Random().nextInt(diceSide) + 1;
					}

////		debug サイコロの中身
//					diceResult[0] = p+1;
//					diceResult[1] = 0;
//					diceResult[2] = 0;
//					diceResult[3] = 0;
//					diceResult[4] = 0;

					// さいころ結果表示
					System.out.print(player[p]);
					for (int i = 0; i < diceTotal; i++) {
						System.out.print("【" + diceResult[i] + "】");
					}
					System.out.println();

					// ゾロを数えてmatch[]に入れる
					int[] match = new int[diceTotal];
					for (int i = 0; i < diceTotal; i++) {
						for (int j = 0; j < diceTotal; j++) {
							if (diceResult[i] == diceResult[j]) {
								match[i]++;
							}
						}
					}

//// debug ゾロ表示
//				System.out.print("match");
//				for (int d = 0; d < diceTotal; d++) {
//					System.out.print("[" + match[d] + "]");
//				}
//				System.out.println();

					// スコア判定
					int straight = 0;
					for (int i = 0; i < match.length; i++) {
						switch (match[i]) {
						case 5:
							score[p] += (700 / 5) + diceResult[i];
							break;
						case 4:
							score[p] += (600 / 4) + diceResult[i];
							break;
						case 3:
							score[p] += (300 / 3) + diceResult[i];
							break;
						case 2:
							score[p] += (100 / 2) + diceResult[i];
							break;
						case 1:
							score[p] += diceResult[i];
							if (diceResult[i] != 1 && diceResult[i] != 6) {
								straight++;
								if (straight == 4) {
									score[p] += 500;
									straight = 0;
								}
							}
							break;
						}
					}

					// ディーラーのリロール
					if (player[p].equals("dealer")) {
						if (remit[p] == 0 || score[p] > dealerR) {
							System.out.print("リロールしません。（残り" + remit[p] + "回） 1.ok　＞");
							int i = new java.util.Scanner(System.in).nextInt();
							System.out.println();
							break;
						} else {
							System.out.print("リロールします。（残り" + remit[p] + "回） 1.ok　＞");
							int i = new java.util.Scanner(System.in).nextInt();
							System.out.println();
						}

						// プレイヤーのリロール
					} else {
						if (remit[p] == 0) {
							System.out.print("リロールできません。（残り" + remit[p] + "回）　1.ok　＞");
							int i = new java.util.Scanner(System.in).nextInt();
							System.out.println();
							break;
						} else {
							System.out.print("リロールする？（残り" + remit[p] + "回） 1.yes 2.no　＞");
							int rerollChoose = new java.util.Scanner(System.in).nextInt();
							System.out.println();

							if (rerollChoose%2 == 0)
								break;

						}
					}
				} // 投げるループ
			} // 投げる人の分岐

//		スコア表示
			for (int p = 0; p < playerTotal; p++) {
				switch (score[p] / 100) {
				case 7:
					System.out.print(player[p] + ":5カード！");
					break;
				case 6:
					System.out.print(player[p] + ":4カード！");
					break;
				case 5:
					System.out.print(player[p] + ":ストレート！");
					break;
				case 4:
					System.out.print(player[p] + ":フルハウス！");
					break;
				case 3:
					System.out.print(player[p] + ":3カード！");
					break;
				case 2:
					System.out.print(player[p] + ":2ペア！");
					break;
				case 1:
					System.out.print(player[p] + ":1ペア！");
					break;
				case 0:
					System.out.print(player[p] + ":なんもなし！");
					break;
				}
//			System.out.print(score[p]);
				System.out.print(" ");
			}
			System.out.println();

			// 決着
			System.out.print("＞");
			int winner = 0;
			for (int i = 0; i < playerTotal; i++) {
				if (score[winner] < score[i]) {
					winner = i;
				}
			}

			// 同役決着
			for (int i = 0; i < playerTotal; i++) {
				if (winner == i)
					continue;
				if (score[winner] / 100 == score[i] / 100) {
					System.out.print(score[winner] % 100);
					for (int j = 0; j < playerTotal; j++) {
						if (winner == j)
							continue;
						if (score[winner] / 100 == score[j] / 100) {
							System.out.print("対" + score[j] % 100);
						}
					}
					System.out.print("で");
					break;
				}
			}

			System.out.println(player[winner] + "の勝ち！");
			System.out.println();

			// リトライ判定
			System.out.print("リトライする？ 1.yes 2.no　＞");
			int retry = new java.util.Scanner(System.in).nextInt();

			if (retry == 2) {
				System.out.println("おわり。");
				break;
			}

			// 勝敗履歴
			System.out.println();
			if (starCount % 5 == 0 && starCount != 0)
				star += " ";
			if (starCount % 10 == 0 && starCount != 0)
				star += "\n";
			if (player[winner] == "dealer") {
				star += "★";
			} else {
				star += "☆";
			}
			starCount++;
			System.out.print(star);
			System.out.println();
		}
	}
}



