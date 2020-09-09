package chess.bots;

import java.util.List;

import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

/**
 * This class should implement the minimax algorithm as described in the
 * assignment handouts.
 */
public class SimpleSearcher<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> {
	//public static int moveCount;

    public M getBestMove(B board, int myTime, int opTime) {
        BestMove<M> best = minimax(this.evaluator, board, ply);
        //moveCount = 0;
        return best.move;
    }
    
    /*public long getCount() {
    	return moveCount;
    }*/

    public static <M extends Move<M>, B extends Board<M, B>> BestMove<M> minimax(Evaluator<B> evaluator, B board, 
    		int depth) {
        
        if (depth == 0) {
            return new BestMove<M>(evaluator.eval(board));
        }
        
        List<M> moves = board.generateMoves();

        if (moves.isEmpty()) {
            if (board.inCheck()) {
                return new BestMove<M>(0 - evaluator.mate() - depth);
            } else {
                return new BestMove<M>(0 - evaluator.stalemate());
            }
        }

        BestMove<M> best = new BestMove<M>(0 - evaluator.infty());

        for (M mv : moves) {
        	//moveCount++;
            board.applyMove(mv);
            BestMove<M> move = minimax(evaluator, board, depth-1);
            move.negate();
            board.undoMove();
            if (move.value > best.value) {
                best = move;
                best.move = mv;
            }
        }
        return best;
    }
}