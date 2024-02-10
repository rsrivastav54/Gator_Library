JCC = javac

CLASSES = \
gatorLibrary.java\
Node.java\
MinHeap.java\
RedBlackTree.java\
Reservation.java\
Book.java\

default: classes

classes: $(CLASSES:.java=.class)

gatorLibrary.class: gatorLibrary.java
	$(JCC) gatorLibrary.java

Node.class: Node.java
	$(JCC) Node.java

MinHeap.class: MinHeap.java
	$(JCC) MinHeap.java

RedBlackTree.class: RedBlackTree.java
	$(JCC) RedBlackTree.java

Reservation.class: Reservation.java
	$(JCC) Reservation.java

Book.class: Book.java
	$(JCC) Book.java

clean:
	rm -f *.class
	rm -f *_output_file.txt

default: gatorLibrary.class
	 java gatorLibrary input.txt	

run:	gatorLibrary.class
	java gatorLibrary input1.txt
	java gatorLibrary input2.txt
	java gatorLibrary input3.txt
	java gatorLibrary inputTest1.txt
	java gatorLibrary inputTest2.txt
	java gatorLibrary inputTest3.txt
	java gatorLibrary inputTest4.txt