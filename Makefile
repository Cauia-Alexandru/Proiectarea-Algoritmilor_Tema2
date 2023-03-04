build:
	javac Curse.java Fortificatii.java Beamdrone.java

run-p2:
	java Fortificatii

run-p3:
	java Beamdrone

run-p4:
	java Curse

clean:
	rm -rf *.class