#!/bin/sh
java -cp classes:pdfbox -Xmx1g ir.SearchGUI -d ./davisWiki -l ir17.gif -p patterns.txt
#java -cp classes:pdfbox -Xmx1g ir.SearchGUI -d ./davisWiki/_Mexican_Collection.f -d ./davisWiki/Kittens.f -l ir17.gif -p patterns.txt
#java -cp classes:pdfbox -Xmx1g ir.SearchGUI -d ./davisWiki/Zombie_Walk.f -l ir17.gif -p patterns.txt

#java -cp classes:pdfbox -Xmx1g ir.SearchGUI -d ./guardian -l ir17.gif -p patterns.txt
