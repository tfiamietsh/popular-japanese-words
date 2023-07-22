package words;

import java.util.HashMap;

public class Hiragana {
    public Hiragana() {
        String[] keys = new String[] {"a", "ka", "sa", "ta", "i", "ki", "shi", "chi", "u", "ku", "su", "tsu", "e", "ke",
                "se", "te", "o", "ko", "so", "to", "na", "ha", "ma", "ra", "ni", "hi", "mi", "ri", "nu", "fu", "mu",
                "ru", "ne", "he", "me", "re", "no", "ho", "mo", "ro", "wa", "ga", "za", "da", "wo", "gi", "ji", "di",
                "ya", "gu", "zu", "du", "yu", "ge", "ze", "de", "yo", "go", "zo", "do", "ba", "pa", "la", "bi", "pi",
                "li", "bu", "pu", "lu", "be", "pe", "le", "bo", "po", "lo", "lya", "kya", "sha", "cha", "lyu", "kyu",
                "shu", "chu", "lyo", "kyo", "sho", "cho", "nya", "hya", "mya", "rya", "nyu", "hyu", "myu", "ryu", "nyo",
                "hyo", "myo", "ryo", "gya", "ja", "dya", "bya", "gyu", "ju", "dyu", "byu", "gyo", "jo", "dyo", "byo",
                "pya", "pyu", "pyo", "nn", ".", ",", "/", "+", "-", "(", ")", "[", "]", "\\", "=", "1", "2", "3", "4",
                "5", "6", "7", "8", "9", "0", "`", "~", "!", "@", "#", "$", "{", "}", "<", ">", "|", "%", "^", "&", "*",
                ":", ";", "?", "ltsu"};
        String[] values = new String[] {"あ", "か", "さ", "た", "い", "き", "し", "ち", "う", "く", "す", "つ", "え", "け",
                "せ", "て", "お", "こ", "そ", "と", "な", "は", "ま", "ら", "に", "ひ", "み", "り", "ぬ", "ふ", "む", "る",
                "ね", "へ", "め", "れ", "の", "ほ", "も", "ろ", "わ", "が", "ざ", "だ", "を", "ぎ", "じ", "ぢ", "や", "ぐ",
                "ず", "づ", "ゆ", "げ", "ぜ", "で", "よ", "ご", "ぞ", "ど", "ば", "ぱ", "ぁ", "び", "ぴ", "ぃ", "ぶ", "ぷ",
                "ぅ", "べ", "ぺ", "ぇ", "ぼ", "ぽ", "ぉ", "ゃ", "きゃ", "しゃ", "ちゃ", "ゅ", "きゅ", "しゅ", "ちゅ", "ょ",
                "きょ", "しょ", "ちょ", "にゃ", "ひゃ", "みゃ", "りゃ", "にゅ", "ひゅ", "みゅ", "りゅ", "にょ", "ひょ", "みょ",
                "りょ", "ぎゃ", "じゃ", "ぢゃ", "びゃ", "ぎゅ", "じゅ", "ぢゅ", "びゅ", "ぎょ", "じょ", "ぢょ", "びょ", "ぴゃ",
                "ぴゅ", "ぴょ", "ん", "。", "、", "・", "＋", "ー", "（", "）", "「", "」", "／", "＝", "１", "２", "３", "４",
                "５", "６", "７", "８", "９", "０", "｀", "〜", "！", "＠", "＃", "＄", "｛", "｝", "＜", "＞", "｜", "％",
                "＾", "＆", "＊", "：", "；", "？", "っ"};
        table = new HashMap<>();
        for (int i = 0; i < keys.length; i++)
            table.put(keys[i], values[i]);
    }

    private String get(String elem) {
        return table.getOrDefault(elem, null);
    }

    public String transform(String s) {
        StringBuilder res = new StringBuilder();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            String newElem = null;
            for (int j = 3; j > 0; j--) {
                String elem = s.substring(i, Math.min(i + j, n));
                newElem = this.get(elem);
                if (newElem != null) {
                    res.append(newElem);
                    i += j - 1;
                    break;
                }
                if (elem.length() == 2 && elem.charAt(0) == elem.charAt(1) &&
                        elem.matches("^[qrtypsdfghjklzxcvbm]+$")) {
                    res.append(table.get("ltsu"));
                    i += 1;
                    break;
                }
            }
            if (newElem == null)
                res.append(s.charAt(i));
        }
        return res.toString();
    }
    
    private final HashMap<String, String> table;
}
