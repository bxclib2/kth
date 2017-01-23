all:  compile index

index: compile
	@echo Generating index with tokenizer.c:
	./tokenizer < korpus | sort > index
	@echo Generating new indexfiles:
	java Generator
	@echo Removing the old index:
	#rm index

compile:
	gcc -o tokenizer tokenizer.c
	javac -encoding utf8 Konkordans.java 
	javac -encoding utf8 GeneratePrefixFile.java Prefix.java Generator.java IndexBinary.java
clean:
	rm *.class
