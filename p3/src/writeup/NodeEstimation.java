package writeup;

import java.util.Arrays;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.ParallelSearcher;
import chess.bots.SimpleSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Searcher;

public class NodeEstimation {
	public static void main(String[] args) {
		String [] fen = {
				"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -",
				"rnbqkbnr/pppppppp/8/8/8/2N5/PPPPPPPP/R1BQKBNR b KQkq -",
				"rnbqkbnr/pp1ppppp/8/2p5/8/2N5/PPPPPPPP/R1BQKBNR w KQkq c6",
				"rnbqkbnr/pp1ppppp/8/2p5/8/2N2N2/PPPPPPPP/R1BQKB1R b KQkq -",
				"rnbqkbnr/pp1pp1pp/8/2p2p2/8/2N2N2/PPPPPPPP/R1BQKB1R w KQkq f6",
				"rnbqkbnr/pp1pp1pp/8/2p2p2/4P3/2N2N2/PPPP1PPP/R1BQKB1R b KQkq e3",
				"rnbqkbnr/pp1p2pp/4p3/2p2p2/4P3/2N2N2/PPPP1PPP/R1BQKB1R w KQkq -",
				"rnbqkbnr/pp1p2pp/4p3/2p2p2/2B1P3/2N2N2/PPPP1PPP/R1BQK2R b KQkq -",
				"rnbqkb1r/pp1pn1pp/4p3/2p2p2/2B1P3/2N2N2/PPPP1PPP/R1BQK2R w KQkq -",
				"rnbqkb1r/pp1pn1pp/4p3/2p1Pp2/2B5/2N2N2/PPPP1PPP/R1BQK2R b KQkq -",
				"rnbqkb1r/pp2n1pp/4p3/2ppPp2/2B5/2N2N2/PPPP1PPP/R1BQK2R w KQkq d6",
				"rnbqkb1r/pp2n1pp/4p3/1BppPp2/8/2N2N2/PPPP1PPP/R1BQK2R b KQkq -",
				"rn1qkb1r/pp1bn1pp/4p3/1BppPp2/8/2N2N2/PPPP1PPP/R1BQK2R w KQkq -",
				"rn1qkb1r/pp1Bn1pp/4p3/2ppPp2/8/2N2N2/PPPP1PPP/R1BQK2R b KQkq -",
				"r2qkb1r/pp1nn1pp/4p3/2ppPp2/8/2N2N2/PPPP1PPP/R1BQK2R w KQkq -",
				"r2qkb1r/pp1nn1pp/4p3/2ppPp2/8/2N2N2/PPPP1PPP/R1BQ1RK1 b kq -",
				"r2qkb1r/pp1n2pp/2n1p3/2ppPp2/8/2N2N2/PPPP1PPP/R1BQ1RK1 w kq -",
				"r2qkb1r/pp1n2pp/2n1p3/2ppPp2/8/2NP1N2/PPP2PPP/R1BQ1RK1 b kq -",
				"r2qkb1r/pp4pp/2n1p3/2ppnp2/8/2NP1N2/PPP2PPP/R1BQ1RK1 w kq -",
				"r2qkb1r/pp4pp/2n1p3/2ppNp2/8/2NP4/PPP2PPP/R1BQ1RK1 b kq -",
				"r2qkb1r/pp4pp/4p3/2ppnp2/8/2NP4/PPP2PPP/R1BQ1RK1 w kq -",
				"r2qkb1r/pp4pp/4p3/2ppnp2/5B2/2NP4/PPP2PPP/R2Q1RK1 b kq -",
				"r2qkb1r/pp4pp/2n1p3/2pp1p2/5B2/2NP4/PPP2PPP/R2Q1RK1 w kq -",
				"r2qkb1r/pp4pp/2n1p3/2pp1p1Q/5B2/2NP4/PPP2PPP/R4RK1 b kq -",
				"r2qkb1r/pp5p/2n1p1p1/2pp1p1Q/5B2/2NP4/PPP2PPP/R4RK1 w kq -",
				"r2qkb1r/pp5p/2n1p1p1/2pp1p2/5B2/2NP1Q2/PPP2PPP/R4RK1 b kq -",
				"r2qk2r/pp4bp/2n1p1p1/2pp1p2/5B2/2NP1Q2/PPP2PPP/R4RK1 w kq -",
				"r2qk2r/pp4bp/2n1p1p1/2pp1p2/N4B2/3P1Q2/PPP2PPP/R4RK1 b kq -",
				"r3k2r/pp4bp/2n1p1p1/q1pp1p2/N4B2/3P1Q2/PPP2PPP/R4RK1 w kq -",
				"r3k2r/pp4bp/2n1p1p1/q1pp1p2/5B2/2NP1Q2/PPP2PPP/R4RK1 b kq -",
				"r3k2r/pp5p/2n1p1p1/q1pp1p2/5B2/2bP1Q2/PPP2PPP/R4RK1 w kq -",
				"r3k2r/pp5p/2n1p1p1/q1pp1p2/5B2/2PP1Q2/P1P2PPP/R4RK1 b kq -",
				"r3k2r/pp5p/2n1p1p1/2pp1p2/5B2/2qP1Q2/P1P2PPP/R4RK1 w kq -",
				"r3k2r/pp5p/2n1p1p1/2pp1p2/5B2/2qP1Q2/P1P2PPP/R1R3K1 b kq -",
				"2kr3r/pp5p/2n1p1p1/2pp1p2/5B2/2qP1Q2/P1P2PPP/R1R3K1 w - -",
				"2kr3r/pp5p/2n1p1p1/2pp1p2/5B2/2qP1QP1/P1P2P1P/R1R3K1 b - -",
				"2kr3r/pp5p/2n3p1/2pppp2/5B2/2qP1QP1/P1P2P1P/R1R3K1 w - -",
				"2kr3r/pp5p/2n3p1/2pppp2/8/2qPBQP1/P1P2P1P/R1R3K1 b - -",
				"2kr3r/p6p/2n3p1/1ppppp2/8/2qPBQP1/P1P2P1P/R1R3K1 w - b6",
				"2kr3r/p6p/2n3p1/1ppppp2/8/2qPB1P1/P1P2PQP/R1R3K1 b - -",
				"2kr3r/p6p/2n3p1/2pppp2/1p6/2qPB1P1/P1P2PQP/R1R3K1 w - -",
				"2kr3r/p6p/2n3p1/2pppp2/1p6/2qPB1P1/P1P2P1P/R1R3KQ b - -",
				"2kr2r1/p6p/2n3p1/2pppp2/1p6/2qPB1P1/P1P2P1P/R1R3KQ w - -",
				"2kr2r1/p6p/2n3p1/2pppp2/1p6/2qPB1P1/P1P2PQP/R1R3K1 b - -",
				"2kr2r1/p6p/2n5/2ppppp1/1p6/2qPB1P1/P1P2PQP/R1R3K1 w - -",
				"2kr2r1/p6p/2n5/2ppppp1/1p6/2qPB1P1/P1P2P1P/R1R3KQ b - -",
				"2kr2r1/p6p/2n5/2ppp1p1/1p3p2/2qPB1P1/P1P2P1P/R1R3KQ w - -",
				"2kr2r1/p6p/2n5/2ppp1p1/1p3P2/2qPB3/P1P2P1P/R1R3KQ b - -",
				"2kr2r1/p6p/2n5/2ppp3/1p3p2/2qPB3/P1P2P1P/R1R3KQ w - -",
				"2kr2r1/p6p/2n5/2ppp3/1p3p2/2qPB3/P1P2P1P/R1R2K1Q b - -",
				"2kr2r1/p6p/2n5/2ppp3/1p6/2qPp3/P1P2P1P/R1R2K1Q w - -",
				"2kr2r1/p6p/2n5/2ppp3/1p6/2qPP3/P1P4P/R1R2K1Q b - -",
				"2kr2r1/p6p/2n5/3pp3/1pp5/2qPP3/P1P4P/R1R2K1Q w - -",
				"2kr2r1/p6p/2n5/3pp3/1pp5/2qPPQ2/P1P4P/R1R2K2 b - -",
				"2k2rr1/p6p/2n5/3pp3/1pp5/2qPPQ2/P1P4P/R1R2K2 w - -",
				"2k2Qr1/p6p/2n5/3pp3/1pp5/2qPP3/P1P4P/R1R2K2 b - -",
				"2k2r2/p6p/2n5/3pp3/1pp5/2qPP3/P1P4P/R1R2K2 w - -",
				"2k2r2/p6p/2n5/3pp3/1pp5/2qPP3/P1P1K2P/R1R5 b - -",
				"2k3r1/p6p/2n5/3pp3/1pp5/2qPP3/P1P1K2P/R1R5 w - -",
				"2k3r1/p6p/2n5/3pp3/1pp1P3/2qP4/P1P1K2P/R1R5 b - -",
				"2k3r1/p6p/8/3pp3/1ppnP3/2qP4/P1P1K2P/R1R5 w - -",
				"2k3r1/p6p/8/3pp3/1ppnP3/2qP4/P1P4P/R1R2K2 b - -",
				"2k3r1/p6p/8/3pp3/1ppnP3/3P4/P1Pq3P/R1R2K2 w - -",
				"2k3r1/p6p/8/3pp3/1ppnP3/3P4/P1Pq3P/R2R1K2 b - -"};
		long countminimax = 0;
		long countparallel = 0;

		Searcher<ArrayMove, ArrayBoard> simple = new SimpleSearcher<>();
		Searcher<ArrayMove, ArrayBoard> parallel = new ParallelSearcher<>();

		for(int j = 1; j < 6; j++) {
			for(int i = 0; i < fen.length; i++) {
				getBestMove(fen[i], simple, j, 5);
				//these are commented out of the Searchers to avoid impacting efficiency
				//countminimax += simple.getCount();
				getBestMove(fen[i], parallel, j, j/2);
				//countparallel += parallel.getCount();
			}
	
			System.out.println("Simple-" + j +": " + countminimax);
			System.out.println("Parallel-" + j +": " + countparallel);
		}
	}
    public static ArrayMove getBestMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) { 
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator());

        return searcher.getBestMove(ArrayBoard.FACTORY.create().init(fen), depth, cutoff);
    }
}
