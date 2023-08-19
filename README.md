# Popular Japanese words

There are two projects in the repository that uses the [popular Japanese words dataset](https://www.kaggle.com/datasets/dinislamgaraev/popular-japanese-words).

## Analysis

The `main` branch contains a Jupyter notebook with an explorative analysis of the dataset. The analysis looks at how the words are distributed across different contexts and allows to ascertain some interesting facts about the Japanese language.

## Offline dictionary

The `dev` branch contains source code in Java for a dictionary app based on the words from the dataset. Key features of the dictionary are listed below.
Check the Releases page to download the latest version.

P.S. Don't let the title of the app scare you :smile:.

**Features:**
- Up to 30k commonly used Japanese words are included
- An example sentence and an English translation are provided for each word
- Integrated Romaji to Hiragana IME
- The Hiragana-Kanji prefix tree allows for fast and accurate word retrieval

**How does search with IME works?**

Let's say we want to look up the word 免許証 (katakana: めんきょしょう, romaji: menkyoshou), so we start typing romaji

![image typing_mennkyoshou.gif not found](https://psv4.userapi.com/c240331/u248809380/docs/d52/dc69c4d2c54d/ezgif_com-gif-maker.gif)
