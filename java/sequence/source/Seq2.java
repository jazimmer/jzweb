/**
 * A nonempty sequence subclass of Seq.
 *
 * See the documentation of Seq.
 */
public final class Seq2<T> extends  Seq<T> {

    public Seq2 (T f, Seq<T> r) {
        if( r==null ) throw new IllegalArgumentException(
               "sequences cannot be null)"
        );
        car = f;
        cdr = r;
    }  // like Lisp's cons

    public Seq2 (T f) {
        car = f;
        cdr = new Seq0<T>();
    }  // makes singleton sequence

    public Seq<T> copy () {
       return new Seq2<T>(car,cdr.copy());
    }

    public boolean isEmpty() { return false; }

    public boolean equals(Object obj) {
        return obj!=null &&
               obj instanceof Seq2 &&
               ((Seq2)obj).getFirst().equals(getFirst()) &&
               getSubseq().equals( ((Seq2)obj).getSubseq() );
    }

    public int length() { return cdr.length()+1; }

    public void setFirst(T t) { car = t; }

    public T getFirst() { return car; }

    protected void setSubseq(Seq<T> t) {
        cdr = t;
    }

    protected Seq<T> getSubseq() { return cdr; }

    protected char [] toStringRecurs(int where) {
          String thisNode = getFirst().toString();
          char [] retval =
             cdr.toStringRecurs(where+thisNode.length()+1);
          retval[where+thisNode.length()] = ',';
          for(int i=0; i<thisNode.length(); i+=1) {
               retval[i+where] = thisNode.charAt(i);
          }
          return retval;
    }

    protected Seq<T> reverseRecurs(Seq<T> buildMe) {
       return cdr.reverseRecurs(new Seq2<T>(car,buildMe));
    }

    // Instance Variables

    protected T car;

    protected Seq<T> cdr;

}

