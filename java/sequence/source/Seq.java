import java.util.Iterator;

/**

Implements a generic sequence that can be split, spliced, reversed, or tested
for equality.

<P><B>Note</B></P>

<P>This class requires two subclasses.  All documentation is here.  Use 
</P>

<P><DL><DD><PRE>
new Seq0&lt;whatever&gt;()
</PRE></DL></P>

to create an empty sequence and 

<P><DL><DD><PRE>
new Seq2&lt;whatever&gt;( &lt;&lt;instance of your generic type&gt;&gt;, &lt;&lt;some Seq&lt;whatever&gt;&gt;&gt; )
</PRE></DL></P>

to add an element to the front of a sequence or 

<P><DL><DD><PRE>
new Seq2&lt;whatever&gt;( &lt;&lt;instance of your generic type&gt;&gt; )
</PRE></DL></P>

to create a singleton sequence.  See the
<HREF A="StateSeq.html">state of the Seq class</A>.

<P><B>Warning</B></P>
<P>This is a rather low level abstraction and is
not foolproof. If you create
a "circular" sequence or use two iterators to alter one sequence, you
must be prepared for the consequences.</P>

<B>Examples of Usage </B>

<P>The following code obtains integers from a scanner and then prints
them on one line.</P>

<P><DL><DD><PRE>
Seq&lt;Integer&gt; seq = new Seq0&lt;Integer&gt;();
while( scan.hasNextInt() ) 
seq = new Seq2&lt;Integer&gt;( scan.nextInt(), seq );
seq = seq.reverse();
System.out.println(seq);
</PRE></DL></P>

The following code replaces the value in the the element of the sequence
which would be indexed by 4 if it were an array. (That is to say the
fifth element of the sequence.) It is assumed that the sequence has such
an element.

<P><DL><DD><PRE>
Seq&lt;Integer&gt;.SeqIterator i = seq.iterator();
while( i.count()&lt;4 ) i.next();
i.setFirst(10);
</PRE></DL></P>

The following method will look for a value in a sequence and
remove the first one found returning the first element of
the new sequence.

<P><DL><DD><PRE>
public static &lt;T&gt; Seq&lt;T&gt;
  removeFirstOccurence(Seq<T> seq, T removeMe) 
{
    Seq<T>.SeqIterator i = seq.iterator();
    if( !i.hasNext() ) return seq;
    if( removeMe==i.next() ) {
        i.split();
        return i.peek();
    }
    while(i.hasNext() && !i.peek().getFirst().equals(removeMe)) {
       i.next();
    }
    if( i.hasNext() ) {
        Seq<T>.SeqIterator j = i.split();
        i.next();
        j.splice(i.peek());
    }
    return seq;
}
</PRE></DL></P>

<P><B>Implementation Notes:</B></P>

<P>1) The Seq, Seq0, and Seq2 classes were created to illustrate how
object orientation uses subclassing to eliminate branching. The only if
statements I permitted myself were to prevent misuse of the methods. The
fact that empty sequences must be handled differently than nonempty
sequences is taken care of using a subclass Seq0 for empty classes and a
subclass Seq2 for nonempty classes.</P>

<P> 2) A secondary goal was to illustrate uses of recursion. The
implementation of toString() is a particular example of this: the
recursion gathers information as it "reads" the sequence and fills an
array of chars as it comes back.</P>

<P> 3) Normally one would want the empty sequence to be unique.
Implementing that way requires the use of a static which conflicts with
Java's generics. Accordingly, this implementation permits many empty
sequences and overrides equals to make them all equal. An added benefit
is that you can compare two sequences for equality. The comparison will
make use of the equals method of the generic type.</P>

<P> 4) Seq2 uses the standard car,cdr way of implementing a sequence as
a list. If you want something more esoteric, you can probably get it by
creating an alternative to Seq2 and leaving both Seq and Seq0 unchanged.
</P>

*/ 
abstract public class Seq<T> {

    /**
     * Accessor testing emptyness.
     *
     * @return true iff the sequence is empty
     */
    abstract public boolean isEmpty();
    
    /**
     * Accessor determining length.
     *
     * @return an int giving the length of the sequence
     */
    abstract public int length();
    
    /**
     * Mutator that resets the first value of the sequence.
     *
     * @param t a generic value to replace the first in the sequence
     */
    abstract public void setFirst(T t);
    
    /**
     * Accessor returning the first element in sequence.
     *
     * @return the first element of the seqeunce.
     */
    abstract public T getFirst();
    
    /**
     * Create an iterator over the elements of the sequence.
     *
     * The iterator is capable of mutating the sequence.
     *
     * @return an iterator
     */
    public SeqIterator iterator() { return new SeqIterator(); }

    /**
     * Accessor that copies the sequence.
     *
     * @return a clone
     */
    abstract public Seq<T> copy();

    /**
     * Accessor that returns a copy in of the sequence in reverse order.
     *
     * @return a copy with the order of the elements reversed
     */
    public Seq<T> reverse() { return reverseRecurs(new Seq0<T>()); }

   /**
    * String representation obtained applying each elements toString.
    *
    * @return a String representation of the sequence
    */
   public String toString () {
         char [] strAry = toStringRecurs(2);
         int end = strAry.length-2;
         strAry[0] = '<'; strAry[1] = strAry[end] = ' ';
         strAry[end+1] = '>';
         return new String(strAry);
    }

    /**
     * An iterator class capable of mutating the sequence.
     * See the
     * <HREF A="StateSeq.html">state of the Seq class</A>.
     */
    public class SeqIterator implements Iterator {
        
        private SeqIterator() {
            prev = new Seq0<T>();
            next = me;
            i = 0;
        }

        /**
         * Returns the next element in the iteration.
         *
         * Calling this method repeatedly until the hasNext() method 
         * returns false will return each element in the sequence 
         * exactly once. Does not change the sequence.
         *
         * @return the next element in the sequence
         */
        public T next() {
            i += 1;
            prev = next;
            next = next.getSubseq();
            return prev.getFirst();
        }

        /**
         * For use with next().
         *
         * Does not change the underlying sequence.
         *
         * @return a boolean, true iff next() will provide another 
         * element
         */
        public boolean hasNext() {
            return !next.isEmpty();
        }

        /**
         * not implemented
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Counts the elements already returned by next().
         *
         * @return a count of the elements already returned by next()
         */
        public int count() { return i; }  // refers to what next() got

        /**
         * Alters the element of the sequence that was most recently
         * returned by next()
         *
         * @param first the value to replace the count() position in the
         * sequence
         */
        public void replaceRecent( T first ) {  // alters what next got
            prev.setFirst(first);
        }

        /**
         * Shows the sequence yet to be iterated over.  The returned
         * sequence is not a copy and includes only elements as yet
         * unseen of the sequence the iterator is passing over.
         *
         * @return returns a sequence, namely that sequence yet
         * to be iterated over.
         */
         public Seq<T> peek() { return next; }

        /**
         * Splits off the part of the sequence that peek() shows.
         * <P>
         * The original sequence will end with the most recent element 
         * to be returned by next.   The current iterator becomes a
         * newly created iterator for the sequence that is split off
         * which is to say the sequence which peek() would show.
         * </P>
         * When the cursor is iterating over an empty sequence, the
         * split method has no effect.
         *
         * @return A new iterator that has passed over the shortened
         * original sequence.  (Valuable for splicing other sequences
         * to the end of that sequence.)
         */
        public SeqIterator split() {
           Seq<T> empty = new Seq0<T>();
           // prev should now end the prefix list
              prev.setSubseq(empty);
           // want to return an iterator at end of prefix list
              SeqIterator c = iterator();
              c.next = empty;
              c.prev = prev; 
              c.i  = i;
           // want this iterator to be at beginning of suffix list
              prev = empty; i = 0; 
           return c;
        }

        /**
         * Splices a sequence into the new sequence--between the
         * subsequences that would be separated with split().
         * <P>
         * The original sequence is increased in length by the length
         * of the sequence being spliced in.  Splicing occurs just
         * before the element, if any, that next() would return next.
         * That element will still be returned by next() after the
         * splicing.
         * </P><P>
         * The sequence to be spliced in is also changed because the
         * sequence you would get with peek() is added to the end of
         * it.
         * </P>
         * The splice method still works if either of the two sequences
         * involved is empty.
         *
         * @param addMe the sequence to be splice in <P><B>Gotcha:</B>
         * When you splice to the front of a sequence, you must then use
         * addMe to represent the whole sequence.
         */
         public void splice( Seq<T> addMe ) {
            // attach addMe to prev
               prev.setSubseq(addMe);
            // attach next to end of addMe suffix
               Seq<T> save_next = next;
               // drive next to end of addMe suffix
                   next = addMe;
                   while(hasNext() ) next();
               prev.setSubseq(save_next);
               next = save_next;
        }
        
        // Instance Variables
        
        private Seq<T> prev;
        private Seq<T> next;
        private int i;
        
        // Invariants:  The number of times next() has executed is i.
        //              The first of next is the value that next() is  
        //                about to return or, if the end of the sequence 
        //                has been reached, next is empty
        //              When i>0, the first of prev is the value 
        //                returned on the ith execution of next()

    }
    
    abstract protected char [] toStringRecurs(int where);

    abstract protected Seq<T> reverseRecurs(Seq<T> buildMe);

    abstract protected Seq<T> getSubseq();

    abstract protected void setSubseq(Seq<T> new_subseq);

    // Instance Variables
    
    private Seq<T> me = this;
       // this is the only way I know to refer to this in SeqIterator
       // Seq<T>.this doesn't work and Seq.this causes warnings

}

