
/* 	未対応バグとか
 * ?
 * 
 * 

 * 　未改善
 * betエラー時の再入力ルート・レイアウト
 * 所持coinとrewardの表記・レイアウト (結果の確定までcoinは変動しないほうがわかりやすそう)
 * 
 * 

 * 
 * 未実装
 * 必要最小手数の表示
 * リトライ数・平均クリア手数
 * リタイア
 * maxNum2桁
 
 
 
 * 	？
 * 
 * 
 * 
 * 
 */

package chapter05_method;

public class G01_hit_and_blow {
	public static int Hit_and_blow(int coin) {

		int digit = 3;
		int maxNum = 10;

		double bet = 0;
		int turn = 0;
		int ansLoop = 0;
		boolean retry = true;

		int[] option = { digit, maxNum };
		double rateRatio = 1.0;
		double beginRate = 5.0;
		double turnRate = 1.0;
		double digitRate = 3.0;
		double maxNumRate = 10.0;
		double[] rates = { beginRate, turnRate, digitRate, maxNumRate, rateRatio };

		opening(coin);
		while (retry) {
			do {
				bet = betting(coin);
				if (bet == 0)
					return coin;
			} while (bettingCheck(bet, coin, option, rates));

			int[] num = new int[option[0]];
			int[] ans = new int[option[0]];
			coin -= bet;
			setup(coin, bet, option, rates);
			generateNum(num, option);
			// debug(num);

			ansLoop = 0;
			turn = 0;
			while (ansLoop == 0) {
				turn++;
				ansConvert(ans, ansInput(bet, turn, option, rates));
				countDisplay(num, ans);
				ansLoop = endCheck(hitCount(num, ans), coin, reward(bet, turn + 1, option, rates), option);
			}
			if (ansLoop == 1) {
				allLostEnd();
				coin = 0;
				return coin;
			}
			result(coin, reward(bet, turn, option, rates));
			coin += (reward(bet, turn, option, rates));
			retry = retryInput();
		}
		return coin;
	}

	public static void ruleMenu(int[] option, double[] rates) {
		while (true) {
			System.out.println("-------------------------------");
			System.out.println("1.hit_and_blow について");
			System.out.println("2.bet_rateの詳細");
			System.out.print("9.戻る　＞");

			String input = new java.util.Scanner(System.in).nextLine();
			System.out.println("-------------------------------");

			switch (input) {
			case "1":
				hitAndBlowRule();
				break;
			case "2":
				betRateRule(option, rates);
				break;
			case "9":
				return;
			}
		}
	}

	public static void hitAndBlowRule() {
		System.out.println("hit_and_blow について：");
		System.out.println("　開始すると複数の数字がランダムに生成されます。");
		System.out.println("　複数の中で同じ数字が生成されることはありません。");
		System.out.println("　プレイヤーは生成された全ての数と順序を当ててください。");
		System.out.println("　数が含まれていた場合にはblow、順序も正しい場合にはhitが表示されます。");
		System.out.println("　生成される個数はdigit、数の最大値はmaxNumで設定できます。");

		System.out.println("＜　enter　＞");
		new java.util.Scanner(System.in).nextLine();
	}

	public static void displayRule() {

	}

	public static void betRateRule(int[] option, double[] rates) {
		System.out.println("[ get reward ] = ( bet ) + bet * beginRate" + rates[0] + " * option bonus (現在:"
				+ getOptionBonus(option, rates) + ")");
		System.out.println("					- bet / turnRate" + rates[1] + " * turn");
		System.out.println("[option bonus] = digit / " + rates[2] + " * (maxNum + 1) / " + rates[3]);
		System.out.println("＜　enter　＞");
		new java.util.Scanner(System.in).nextLine();
	}

	public static void option(int[] option, double[] rates) {
		while (true) {
			System.out.println("--------------------------");
			System.out.println("option bonus:" + Math.round(getOptionBonus(option, rates) * 100.0) / 100.0 + "倍");
			System.out.println("0.digit:" + option[0]);
			System.out.println("1.maxNum:" + option[1]);
			System.out.println("9.戻る");
			System.out.print("＞");
			boolean loop = true;
			int input = 9;
			while (loop) {
				input = new java.util.Scanner(System.in).nextInt();

				int[] not = { 2, 3, 4, 5, 6, 7, 8 };
				if (tools.ErrorChecker.digitErrorCheck(1, input) || tools.ErrorChecker.numsErrorCheck(not, input)) {
					System.out.println("無効な入力です");
					System.out.print("＞");
				} else {
					loop = false;
				}
			}
			if (input == 9)
				return;
			optionChange(option, input);
		}
	}

	public static void optionChange(int[] option, int optionIndex) {
		boolean loop = true;
		int input = 0;
		while (loop) {
			System.out.print("1~9で入力してください　＞");
			input = new java.util.Scanner(System.in).nextInt();
			if (tools.ErrorChecker.digitErrorCheck(1, input)) {
				System.out.println("桁数が妙に多いです。");
			} else {
				loop = false;
			}
		}
		if (optionIndex == 1 && option[0] > input) {
			System.out.println("桁数よりも少ない数で遊ぶことはできません");
			return;
		}
		option[optionIndex] = input;
	}

	public static double getOptionBonus(int[] option, double[] rates) {
		return Double.valueOf(option[0]) / rates[2] * (Double.valueOf(option[1])) / rates[3];
	}

	public static void debug(int[] num) {
		for (int i : num) // debug
			System.out.print(" " + i + " "); // debug
		System.out.println();
	}

	public static void opening(int coin) {
		System.out.println("------------------------------------------");
		System.out.println();
		System.out.println();
		System.out.println("		HIT AND BLOW");
		System.out.println();
		System.out.print("　					please enter＞");
		new java.util.Scanner(System.in).nextLine();

	}

	public static int betting(int coin) {
		System.out.println("------------------------------------------");
		System.out.println(" 所持:" + coin + "coin");
		System.out.println("　0.やめる　888.ルール　999.オプション");
		System.out.print("  その他の数字：betting!　＞");
		return new java.util.Scanner(System.in).nextInt();
	}

	public static boolean bettingCheck(double bet, int coin, int[] option, double[] rates) {
		if (bet == 999) {
			option(option, rates);
			return true;
		} else if (bet == 888) {
			ruleMenu(option, rates);
			return true;
		} else if (bet > coin) {
			System.out.println("手持ちのcoinをこえています。");
			return true;
		} else if (bet < 0) {
			System.out.println("0より低い数はbetできません。");
			return true;
		}
		return false;
	}

	public static void setup(int coin, double bet, int[] option, double[] rates) {
		System.out.println("-----------------------------------");
		System.out.println("bonus:" + Math.floor(getOptionBonus(option, rates) * 100) / 100 + "倍 (digit[" + option[0]
				+ "]　maxNum[" + option[1] + "])");
		System.out.println(" -" + (int) bet + "coin");
		System.out.print(" 所持:" + coin + "coin");
	}

	public static void generateNum(int[] num, int[] option) {
		boolean loop = true;
		while (loop) {
			loop = false;
			for (int i = 0; i < option[0]; i++) {
				num[i] = new java.util.Random().nextInt(option[1]) + 1;
			}
			for (int i = 0; i < num.length - 1; i++) {
				for (int j = i + 1; j < num.length; j++) {
					if (num[i] == num[j]) {
						loop = true;
					}
				}
			}
		}
	}

	public static int ansInput(double bet, int ansCount, int[] option, double[] rates) {
		System.out.println("　現在の報酬：(+" + bet + ") + " + reward(bet, ansCount, option, rates) + "coin　　");
		System.out.println();
		int input = 0;
		boolean inputLoop = true;
		while (inputLoop) {
			System.out.print(" " + ansCount + "答目　＞");

			input = new java.util.Scanner(System.in).nextInt();
			if (tools.ErrorChecker.digitErrorCheck(option[0], input)) {
				System.out.println("妙に桁が多いです。");
			} else if (tools.ErrorChecker.numsErrorCheck(tools.ErrorChecker.getBetweenNum(9, option[1]), input)) {
				System.out.println("使えない数字が含まれてます。");
			} else {
				inputLoop = false;
			}
		}
		return input;
	}

	public static void ansConvert(int[] ans, int input) {
		// 1桁ずつ抜き取ってans配列に移動
		for (int i = 0; i < ans.length; i++) {
			ans[ans.length - 1 - i] = input % 10;
			input /= 10;
		}
		// 左詰め
		int count = 1;
		while (ans[0] == 0) {
			for (int i = 0; i < ans.length - 1; i++) {
				ans[i] = ans[i + 1];
			}
			ans[ans.length - count] = 0;
			count++;
		}
	}

	public static void countDisplay(int[] num, int[] ans) {
		System.out.print(" " + hitCount(num, ans) + "hit " + blowCount(num, ans) + "blow ");
	}

	public static int endCheck(int hit, int coin, int getCoin, int[] option) {
		if (hit == option[0])
			return 2;
		if (getCoin * (-1) > coin)
			return 1;
		return 0;
	}

	public static void allLostEnd() {
		System.out.println();
		System.out.println();
		System.out.println("	じゃあな");
		System.out.println();
		System.out.println();
	}

	public static void result(int coin, int reward) {
		System.out.println();
		System.out.println();
		if (reward < 0) {
			System.out.println("you are looser...");
			System.out.println(" 所持:" + coin + "coin　" + reward);
			return;
		}
		if (reward > 0)
			System.out.println("you are winner!");
		if (reward == 0)
			System.out.println("Nothing happened, right?");
		System.out.println(" 所持:" + coin + "coin　	 + " + reward);
		return;
	}

	public static boolean retryInput() {
		System.out.print(" もう一回する？");
		while (true) {
			System.out.print("	1.yes 2.no　＞");
			int input = new java.util.Scanner(System.in).nextInt();
			switch (input) {
			case 1:
				return true;
			case 2:
				return false;
			default:
				System.out.print("入力が変かも");
			}
		}
	}

	public static int reward(double bet, int ansCount, int[] option, double[] rates) {
		double reward = bet * rates[0] * getOptionBonus(option, rates) - bet / rates[1] * Double.valueOf(ansCount);
		return (int) reward;
	}

	public static int hitCount(int[] num, int[] ans) {
		int hit = 0;
		for (int i = 0; i < num.length; i++) {
			if (num[i] == ans[i])
				hit++;
		}
		return hit;
	}

	public static int blowCount(int[] num, int[] ans) {
		int blow = 0;
		for (int i = 0; i < num.length; i++) {
			for (int j = 0; j < num.length; j++) {
				if (i == j)
					continue;
				if (num[i] == ans[j])
					blow++;
			}
		}
		return blow;
	}
}