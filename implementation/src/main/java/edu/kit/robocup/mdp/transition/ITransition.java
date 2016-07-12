package edu.kit.robocup.mdp.transition;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.PlayerActionSet;

/**
 * Created by dani on 12.06.2016.
 */
public interface ITransition {
    State getNewStateSample(State s, PlayerActionSet a, PitchSide pitchSide);
    State getNewStateSampleWithEnemyPolicy(State s, PlayerActionSet a, PitchSide pitchSide);
    boolean hasEnemyTeam();
    int getNumberAllPlayers();
    int getStateDimension();
    int getNumberPlayersPitchside();
}
