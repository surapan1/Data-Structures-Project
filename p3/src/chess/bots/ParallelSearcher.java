package chess.bots;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;


public class ParallelSearcher<M extends Move<M>, B extends Board<M, B>> extends AbstractSearcher<M, B> {
	
	private static final ForkJoinPool POOL = new ForkJoinPool();
	protected static int sequentialCutoff = 1;
	//public static long moveCount;
	
    public M getBestMove(B board, int myTime, int opTime) {
    	BestMove<M> best = parallel(this.evaluator, board, this.ply, this.cutoff, this.sequentialCutoff);
        
        reportNewBestMove(best.move);
        //moveCount = 0;
        return best.move;
    }
    
    /*public long getCount() {
    	return moveCount;
    }*/
    
    static <M extends Move<M>, B extends Board<M, B>> BestMove<M> parallel
    (Evaluator<B> evaluator, B board, int depth, int cutoff, int sequentialCutoff) {
    	
    	return POOL.invoke(new getBestMoveTask<M, B>(evaluator, board, depth, board.generateMoves(), null, 0, 
    			board.generateMoves().size(), cutoff, sequentialCutoff));

    }
    
private static class getBestMoveTask<M extends Move<M>, B extends Board<M, B>> extends RecursiveTask<BestMove<M>> {
        
        Evaluator<B> eval;
        B board;
        int depth;
        List<M> moves;
        M move;
        int start, end;
        int cutoff, sequentialCutoff;
        
        public getBestMoveTask(Evaluator<B> eval, B board, int depth, List<M> moves, M move,
                int start, int end, int cutoff, int sequentialCutoff) {
            this.eval = eval;
            this.board = board;
            this.depth = depth;
            this.moves = moves;
            this.move = move;
            this.start = start;
            this.end = end;
            this.cutoff = cutoff;
            this.sequentialCutoff = sequentialCutoff;
            //moveCount++;
        }

        protected BestMove<M> compute() {    

            if (move != null) {
                this.board = board.copy();
                this.board.applyMove(move);
                this.moves = board.generateMoves();
                this.start = 0;
                this.end = this.moves.size();
            }
            if (this.moves.isEmpty()) {
                if (this.board.inCheck()) {
                    return new BestMove<M>(0 - eval.mate() - depth);
                } else {
                    return new BestMove<M>(0 - eval.stalemate());
                }
            }
            
            if (depth <= cutoff) {
                return SimpleSearcher.minimax(eval, board, depth);
            }
            
            if (end - start <= sequentialCutoff) {
                LinkedList<getBestMoveTask<M, B>> bMove = new LinkedList<getBestMoveTask<M, B>>();
                for (int i = start + 1; i < end; i++) {
                	getBestMoveTask<M, B> forks = new getBestMoveTask<M, B>(eval, board, depth-1,
                            null, moves.get(i), 0, 0, cutoff, sequentialCutoff);
                	forks.fork();
                    bMove.add(forks);
                }
                
                getBestMoveTask<M, B> lastFork = new getBestMoveTask<M, B>(eval, board, depth-1,
                        null, moves.get(start), 0, 0, cutoff, sequentialCutoff);
                
                LinkedList<BestMove<M>> results = new LinkedList<BestMove<M>>();
                results.add(lastFork.compute());
                for (int i = 0; i < bMove.size(); i++) {
                	getBestMoveTask<M, B> fork = bMove.get(i);
                    results.add(fork.join());
                }
                
                BestMove<M> best = new BestMove<M>(0 - eval.infty());
                for (int i = 0; i < results.size(); i++) {
                    BestMove<M> cur = results.get(i);
                    M curMove = moves.get(start + i);
                    cur.negate();
                    if (cur.value > best.value) {
                        best = cur;
                        best.move = curMove;
                    }
                }
                return best;
            }
            
            int mid = start + (end - start)/2;
            getBestMoveTask<M, B> left = new getBestMoveTask<M, B>(eval, board, depth, moves,
                    null, start, mid, cutoff, sequentialCutoff);
            getBestMoveTask<M, B> right = new getBestMoveTask<M, B>(eval, board, depth, moves,
                    null, mid, end, cutoff, sequentialCutoff);
            
            right.fork();
            
            BestMove<M> leftMove = left.compute();
            BestMove<M> rightMove = right.join();
            if (leftMove.value >= rightMove.value) {
                return leftMove;
            } else {
                return rightMove;
            }
        }
    }
}