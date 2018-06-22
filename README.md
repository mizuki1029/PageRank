ソースコードをpages.txt, links.txtと同じディレクトリにダウンロードします。
以下のコマンドを実行してください。
javac PageRank.java
java PageRank

2つのテキストファイルを読み込んだ後、"Set Graph Structure!"と表示されます
少し待つと、重要度の高いページ10個とそのポイントが合計20回表示されます。

initialPoint, bestX, rate, numOfAttemptsの値を変えることによって、結果が変わります。

ちなみにinitialPoint=1000, bestX=10, rate=0.8, numOfAttempts=20で実行すると、重要度の高い順に
日本、英語、アメリカ合衆国、東京都、地理座標系、イギリス、フランス、ドイツ、Portable_Document_Format、ウィクショナリー
という結果になります。
