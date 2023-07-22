package words;

import java.io.Serializable;

public record Word(String word, String kana, String hiragana, String translation, String example,
                   Boolean written_using_kana_only) implements Serializable { }
