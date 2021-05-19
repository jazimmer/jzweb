import java.util.*;

public class DemoSeq {
    
    public static Seq<Integer> example1(Scanner scan) {
        Seq<Integer> seq = new Seq0<Integer>();
        while( scan.hasNextInt() ) 
            seq = new Seq2<Integer>( scan.nextInt(), seq );
        seq = seq.reverse();
        System.out.println(seq);
        return seq;
    }
    
    public static void example2(Seq<Integer> seq) {
        Seq<Integer>.SeqIterator i = seq.iterator();
        while( i.count()<4 ) i.next();
        i.replaceRecent(10);
    }
    
    public static <T> Seq<T>
      removeFirstOccurence(Seq<T> seq, T removeMe) 
    {
        Seq<T>.SeqIterator i = seq.iterator();
        if( !i.hasNext() ) return seq;
        if( removeMe==i.next() ) {
            i.split();
            System.out.println("peeking:"+i.peek());
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
    
    public static void main( String [] junk ) {
        Scanner scan = new Scanner("0 1 2 3 4 5 6 7 8");
        Seq<Integer> seq = example1(scan);
        scan.close();
        
        example2(seq);
        System.out.println(seq);
        
        seq = removeFirstOccurence( seq, 0 );
        seq = removeFirstOccurence( seq, 1000 );
        seq = removeFirstOccurence( seq, 8 );
        scan = new Scanner("1 2 10 4 5 6 7");
        Seq<Integer> seq2 = example1(scan);
        scan.close();
        assert seq.equals(seq2);
    }
    
}

