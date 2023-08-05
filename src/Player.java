public class Player {

    String user;
    boolean isAlive;
    String rollP;
    int playerVotes;

    public Player(String user , String roll ){
        this.user=user;
        this.isAlive=true;
        this.rollP=roll;
        playerVotes=0;
    }

    /**
     * @param alive the boolean show player is alive or not
     */
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
