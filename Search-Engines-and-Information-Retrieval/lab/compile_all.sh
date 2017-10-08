#!/bin/sh
if ! [ -d classes ];
then
   mkdir classes
fi
javac -cp ./pdfbox -d classes ir/Tokenizer.java ir/TokenTest.java ir/Index.java ir/Indexer.java ir/HashedIndex.java ir/Query.java ir/PostingsList.java ir/PostingsEntry.java ir/SPIMInvert.java ir/SearchGUI.java ir/RankedQuery.java ir/SimpleCache.java ir/TFIDF.java ir/BigramIndex.java ir/Searcher.java
