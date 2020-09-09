package writeup;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.ParallelSearcher;
import chess.bots.SimpleSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Searcher;

public class OptimzingEstimation {

    public static final String STARTING_POSITIONS[] = {"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -",
    													"r3k2r/pp5p/2n1p1p1/q1pp1p2/5B2/2PP1Q2/P1P2PPP/R4RK1 b kq -",
    													"2k2Qr1/p6p/2n5/3pp3/1pp5/2qPP3/P1P4P/R1R2K2 b - -"};
    public static final int TEST_RUNS = 5;
    public static final int DEPTH = 5;

    public static ArrayMove getBestMove(String fen, int depth, int cutoff) { 
    	//Searcher<ArrayMove, ArrayBoard> searcher = new ParallelSearcher<>();
    	Searcher<ArrayMove, ArrayBoard> searcher = new SimpleSearcher<>();
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator());

        return searcher.getBestMove(ArrayBoard.FACTORY.create().init(fen), 100, 100);
    }
    
    public static void printMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        String botName = searcher.getClass().toString().split(" ")[1].replace("chess.bots.", "");
        System.out.println(botName + " returned " + getBestMove(fen, depth, cutoff));
    }
    
    public static long timeMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        String botName = searcher.getClass().toString().split(" ")[1].replace("chess.bots.", "");
        long sTime = System.nanoTime();
        getBestMove(fen, depth, cutoff);
        sTime = System.nanoTime() - sTime;
        return sTime;
    }
    
    public static void main(String[] args) {
        Searcher<ArrayMove, ArrayBoard> dumb = new ParallelSearcher<>();

        	int cutoff = 2;
        	for (int pos = 0; pos < STARTING_POSITIONS.length; pos++){
	        	System.out.println("Position " + (pos + 1) + " @");
	        	long time = 0;
	        	for (int jj = 0; jj < TEST_RUNS; jj++){
	        		time += timeMove(STARTING_POSITIONS[pos], dumb, DEPTH, cutoff);
	        	}
	        	System.out.println("Average time: " + (time / TEST_RUNS) + " ns\n");
	        }
    }
}
