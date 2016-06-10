package edu.kit.robocup.game.state;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.interf.game.IPlayer;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IState;

import java.util.*;
import java.util.stream.Collectors;

public class State implements IState {
    private PlayMode playMode;
    private final Ball ball;
    private final Set<IPlayerState> players = new TreeSet<>((p1, p2) -> {
        if (p1.getPitchSide() != p2.getPitchSide()) {
            return p1.getPitchSide() == PitchSide.EAST ? -1 : 1;
        } else {
            return p1.getNumber() - p2.getNumber();
        }
    });
    private final double[] pos;

    public State(Ball ball, List<IPlayerState> players) {
        this.playMode = PlayMode.UNKNOWN;
        this.ball = ball;
        this.players.addAll(players);
        pos = new double[5*(players.size()) + 4];
        initArray();
    }

    public State(double[] state, PitchSide pitchSide, int numberOfPlayersOfPitchside) {
        this.playMode = PlayMode.UNKNOWN;
        int counter = 0;
        for (int i = 0; i < state.length/5; i++) {
            counter++;
            if (counter <= numberOfPlayersOfPitchside) {
                players.add(new PlayerState(pitchSide, i, state[i], state[i + 1], state[i + 2], state[i + 3], state[i + 4], 0));
            } else {
                if (pitchSide == PitchSide.EAST) {
                    players.add(new PlayerState(PitchSide.WEST, i, state[i], state[i + 1], state[i + 2], state[i + 3], state[i + 4], 0));
                } else {
                    players.add(new PlayerState(PitchSide.EAST, i, state[i], state[i + 1], state[i + 2], state[i + 3], state[i + 4], 0));
                }
            }
        }
        int cut = (state.length/5)*5;
        ball = new Ball(state[cut], state[cut+1], state[cut+2], state[cut+3]);
        pos = new double[5*(players.size()) + 4];
        initArray();
    }

    public Ball getBall() {
        return ball;
    }

    public List<IPlayerState> getPlayers(final PitchSide pitchSide) {
        return players.stream().filter(p -> p.getPitchSide() == pitchSide).collect(Collectors.toList());
    }

    @Override
    public IPlayerState getPlayerState(IPlayer player) {
        return players.stream().filter(p -> p.equals(player)).findAny().get();
    }

    public int getPlayerCount() {
        return players.size();
    }

    /**
     * @return Dimension of state. Players get x and y coordinates, x, y velocity and body angle, ball gets x, y coordinate and x, y velocity
     */
    public int getDimension() {
    	return 5*(players.size()) + 4;
    }

    @Override
    public String toString() {
        return "State{PlayMode: " + playMode + " " + ball.toString() + " " + players.toString() + "}";
    }

    @Override
    public PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(PlayMode playMode) {
        if (playMode == null)
            this.playMode = PlayMode.UNKNOWN;
        else {
            this.playMode = playMode;
        }
    }

    /**
     * @return Array containing all x-/y-coordinates of players and the last
     * entries are ballposition and ballvelocity
     */
    public double[] getArray() {
        return pos;
    }

    private void initArray() {
        int i = 0;
		for (IPlayerState player: players) {
			pos[5 * i] = player.getPositionX();
			pos[5 * i + 1] = player.getPositionY();
            pos[5 * i + 2] = player.getVelocityX();
            pos[5 * i + 3] = player.getVelocityY();
            pos[5 * i + 4] = player.getBodyAngle();
            i++;
        }
		pos[5 * players.size()] = ball.getPositionX();
		pos[5 * players.size() + 1] = ball.getPositionY();
		pos[5 * players.size() + 2] = ball.getVelocityX();
		pos[5 * players.size() + 3] = ball.getVelocityY();
    }
}
