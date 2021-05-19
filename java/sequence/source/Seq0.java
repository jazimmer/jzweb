/**
 * A empty sequence subclass of Seq. 
 *
 * See the documentation of Seq.
 */
public final class Seq0<T> extends Seq<T> {
    
    
    public boolean isEmpty() { return true; }
    
    public int length() { return 0; }
    
    public void setFirst(T t) { 
        throw new IllegalStateException(
            "cannot set value of in empty list"
        );
    }
    
    public T getFirst() { return null; }
    
    public boolean equals( Object obj ) {
        return obj != null && obj instanceof Seq0;
    }      // all empty sequences are equal
    
    public Seq<T> copy() {  return this; }  
           // empty sequences are as good as immutable
    
    protected Seq<T> getSubseq( ) { return this; }
    
    protected void setSubseq(Seq<T> t) { }
           // nothing changes when empty sequence glued to front of t
    
    protected char [] toStringRecurs(int where) {
        return new char [where+1];
    } 

    protected Seq<T> reverseRecurs(Seq<T> buildMe) { return buildMe; } 
    
}

