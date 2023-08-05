import java.util.ArrayList;

public class Vote {
    private String vote ;
    private String user ;

    public Vote(String vote, String user) {
        this.vote = vote;
        this.user = user;
    }

    /**
     * add vote to user
     * @param vote the vote that user choose
     * @param player find player who voted
     */
    public void calculationVotes(String vote, ArrayList<Player> player){
        for (Player value : player) {
            if (vote.equals(value.user)) {
                value.playerVotes++;
            }
        }
    }
    /**
     * remove vote for change vote
     * @param vote the vote that user choose to remove
     * @param player find player who voted
     */
    public void changeVotes(String vote, ArrayList<Player> player){
        for (Player value : player) {
            if (vote.equals(value.user)) {
                value.playerVotes--;
            }
        }
    }
    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
