/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omegat.core.matching;

/**
 *
 * @author miquel
 */
public class NearString {
    
    public enum MATCH_SOURCE {
        MEMORY, TM, FILES
    };
    
    public String source;
    public String translation;
    public Scores[] scores=new Scores[0];
    public MATCH_SOURCE comesFrom;
    
    public static class Scores {
        public final int score;
        /** similarity score for match without non-word tokens */
        public final int scoreNoStem;
        /** adjusted similarity score for match including all tokens */
        public final int adjustedScore;
        
        public Scores(int score, int scoreNoStem, int adjustedScore) {
            this.score = score;
            this.scoreNoStem = scoreNoStem;
            this.adjustedScore = adjustedScore;
        }
        
        public String toString() {
            return null;
        }
    }
}
