public class TestSeq {
    
    
    public static void main( String [] junk ) {
        // see empty sequence
        System.out.println(new Seq0<Integer>());

        // make 3 element seqence
        Seq<Integer> g1 = 
             new Seq2<Integer>( 
                 1, 
                 new Seq2<Integer>( 
                    2,
                    new Seq2<Integer>( 
                        3,
                        new Seq0<Integer>()
             )));
        System.out.println(g1);

        // make a clone and change it
        Seq<Integer> g2 = g1.copy();
        Seq<Integer>.SeqIterator i = g2.iterator();
        while( i.hasNext() ) {
            int n = i.next();
            i.replaceRecent( n + 10 );
        }
        System.out.println("altered clone:" + g2.toString());
        
        // alter g1 by splicing on the altered clone
        i = g1.iterator();
        System.out.print("walking through g1 before splicing:");
        while( i.hasNext() ) {
            System.out.print(" "); System.out.print(i.next()); 
        }
        System.out.println();
        i.splice(g2);
        System.out.println("spliced:"+g1);
        
        // split 4 places in
        System.out.print("moving four elements in and splitting:");
        i = g1.iterator();
        do {
            System.out.print(i.next()+" ");
        } while( i.count()<4 );
        System.out.println();
        Seq<Integer> suffix = i.peek();
        Seq<Integer>.SeqIterator e = i.split();
        System.out.print("g1 after the split: " + g1);
        System.out.println();
        System.out.print("the suffix: " + suffix);
        System.out.println();
        
        
        // put it back
        e.splice(suffix);
        System.out.println("after splicing it back together:" + g1);
        
        // prepare for equality check
        Seq<Integer> g1copy = g1.copy();
        System.out.println("clone of same: "+g1copy);
        
        // splice in a two element sequence at beginning
        // then splice an empty sequence inside
        Seq<Integer> two = 
            new Seq2<Integer>( 
                      -2, 
                      new Seq2<Integer>( -1, new Seq0<Integer>() ) );
        i = g1.iterator();
        i.splice(two);
        Seq<Integer> two1 = two.copy();
        i.splice(new Seq0<Integer>());
        assert i.next()==1;
        Seq<Integer> two2 = two.copy();
        System.out.println("after more splicing: " + two );
        
        // equality checks
        assert two1.equals(two1);
        assert two1.equals(two2);
        assert two2.equals(two1);
        i = two.iterator(); i.next(); i.next();
        assert i.peek().equals(g1copy);
        i = g1.iterator();
        assert i.peek().equals(g1copy);
        assert g1copy.equals(i.peek());
        while( i.hasNext() ) i.next();
        assert i.peek().equals(new Seq0());
        assert (new Seq0()).equals(i.peek());
        System.out.println(
            "equality checks were passed (if you remembered -ea)");

    }
    
}

