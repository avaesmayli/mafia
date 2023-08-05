import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;
    private ArrayList<String> users;
    private ArrayList<String> rolls;
    private ArrayList<String> rollArrayList;
    private ArrayList<String> citizen;
    private ArrayList<String> mafia;
    private ArrayList<Player> playerArrayList;
    private ArrayList<Player> deadPlayer;
    private ArrayList<Vote> votes;
    private ArrayList<Integer> loop;
    private Player player;
    int pl=0;
    int i=0;
    int x=0;
    int time=30000;
    int timeM=10000;
    int voteTime=30000;
    int gameLoop=0;
    int usePower=0;
    Vote v;
    int loopSize=4;
    int nullVote=0;
    int numVote=0;
    int voting=0;


    public ClientHandler(Socket socket,ArrayList<ClientHandler>clients,ArrayList<String> users , ArrayList<String> roll , ArrayList<String> roll2 , ArrayList<String> city , ArrayList<String> maf , ArrayList<Player> pl , ArrayList<Vote> votes , ArrayList<Integer> loop){
        this.socket = socket;
        this.clients=clients;
        this.users=users;
        this.rolls=roll;
        rollArrayList=roll2;
        citizen=city;
        playerArrayList=pl;
        mafia=maf;
        this.votes=votes;
        this.loop=loop;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * this method check user used before or not
     * @param user the user that you want check
     * @param users the array list of all user
     * @return 1 if user used before , 0 if user accepted
     */
    public int checkUser(String user, ArrayList<String> users){
        for(String u : users){
            if(user.equals(u)){
                return 0;
            }
        }
        return 1;
    }

    /**
     * this method find a index in arraylist rolls
     * @param roll the roll that you want find index
     * @param ra the arraylist you want check with
     * @return the index of roll
     */
    public int findRollIndex(String roll , ArrayList<String> ra){
        for(int i=0 ; i<ra.size() ; i++){
            if(ra.get(i).equals(roll)){
                return i;
            }
        }
        return -1;
    }

    /**
     * send msg to all client
     * @param msg the text
     */
    public void sendToAll(String msg){
        for (ClientHandler c : clients) {
            c.out.println(msg);
        }
    }

    /**
     * find index in citizen
     * @param a index that you want check
     * @return the index in citizen
     */
    public int indexInUsers(int a){
        for(int i=0 ; i<users.size() ; i++){
            if(users.get(i).equals(citizen.get(a-1))){
                return i;
            }
        }
        return -1;
    }

    /**
     * find index in mafia
     * @param a index that you want check
     * @return the index in mafia
     */
    public int indexInMafia(int a){
        for(int i=0 ; i<users.size() ; i++){
            if(users.get(i).equals(mafia.get(a-1))){
                return i;
            }
        }
        return -1;
    }

    /**
     * for player who have a godfather roll
     * @return the user that godfather choose for kill
     * @throws IOException for read line with buffer reader
     */
    public int godFather() throws IOException {
        int index = findRollIndex("godFather mafia", rollArrayList);
        if (index != -1) {
            ClientHandler ch1 = clients.get(index);
            ch1.out.println("choose a user to kill");
            for (int h = 0; h < citizen.size(); h++) {
                ch1.out.println((h + 1) + ")" + citizen.get(h));
            }
            String chooseUserToKill = in.readLine();
            int userIndex = indexInUsers(Integer.parseInt(chooseUserToKill));
            ch1.out.println(userIndex);
            return userIndex;
        }
        return -1;
    }

    /**
     * for player who have a drCity roll
     * @return the user that drCity choose for save
     * @throws IOException for read line with buffer reader
     */
    public int drCity() throws IOException {
        int index = findRollIndex("drCity", rollArrayList);
        if (index != -1) {
            ClientHandler ch = clients.get(index);
            ch.out.println("choose a username to save");
            for (int h = 0; h < users.size(); h++) {
                ch.out.println((h + 1) + ")" + users.get(h));
            }
            String chooseUserToSave = in.readLine();
            return Integer.parseInt(chooseUserToSave)-1;
        }
        return -1;
    }

    /**
     * for player who have a dr lecter roll
     * @return the user that dr lecter choose in mafia for save
     * @throws IOException for read line with buffer reader
     */
    public int drLecter() throws IOException {
        int index = findRollIndex("drLecter mafia", rollArrayList);
        if (index != -1) {
            ClientHandler ch = clients.get(index);
            ch.out.println("choose a username to save");
            for (int h = 0; h < mafia.size() ; h++) {
                ch.out.println((h + 1) + ")" + mafia.get(h));
            }
            String chooseUserToKill = in.readLine();
            return indexInMafia(Integer.parseInt(chooseUserToKill));
        }
        return -1;
    }

    /**
     * for player who have a detective roll
     * @return say the user that detective choose is mafia or not
     * @throws IOException for read line with buffer reader
     */
    public String detective() throws IOException {
        int index = findRollIndex("detective", rollArrayList);
        if (index != -1) {
            ClientHandler ch = clients.get(index);
            ch.out.println("choose a username");
            for (int h = 0; h < users.size() ; h++) {
                ch.out.println((h + 1) + ")" + users.get(h));
            }
            String chooseUserToGuss = in.readLine();
            int indexGuss=Integer.parseInt(chooseUserToGuss)-1;
            if(rollArrayList.get(indexGuss).equals("drLecter mafia") || rollArrayList.get(indexGuss).equals("simple mafia")) {
                return "[you find a mafia]";
            }else{
                return "[your guss is wrong dude]";
            }
        }
        return null;
    }

    /**
     * for player who have a sniper roll
     * the user that sniper choose for kill and guss is mafia
     * @throws IOException for read line with buffer reader
     */
    public void sniper() throws IOException {
        int index = findRollIndex("sniper", rollArrayList);
        if(index != -1){
            ClientHandler ch = clients.get(index);
            ch.out.println("choose a who you guss");
            for (int h = 0; h < users.size() ; h++) {
                ch.out.println((h + 1) + ")" + users.get(h));
            }
            String chooseUserToGuss = in.readLine();
            int indexGuss=Integer.parseInt(chooseUserToGuss)-1;
            if(rollArrayList.get(indexGuss).equals("drLecter mafia") || rollArrayList.get(indexGuss).equals("simple mafia") || rollArrayList.get(indexGuss).equals("godFather mafia")) {
                playerArrayList.get(indexGuss).setAlive(false);
            }else{
                playerArrayList.get(index).setAlive(false);
            }
        }
    }

    /**
     * for player who have a sniper roll
     * the user that psychologist choose for silent and guss is mafia
     * @throws IOException for read line with buffer reader
     */
    public void psychologist() throws IOException {
        int index = findRollIndex("psychologist", rollArrayList);
        if(index != -1){
            ClientHandler ch = clients.get(index);
            ch.out.println("choose a who you want to be silent");
            for (int h = 0; h < users.size() ; h++) {
                ch.out.println((h + 1) + ")" + users.get(h));
            }
            String chooseUserToGuss = in.readLine();
            int indexGuss=Integer.parseInt(chooseUserToGuss)-1;
            clients.get(indexGuss).out.println("you cant chat today because psychologist guss you are mafia");
        }
    }

    /**
     * show the roll of dead player
     */
    public void hardDie() {
        for(Player p: playerArrayList) {
            if(!p.isAlive)
                sendToAll(p.user + " was " + p.rollP);

        }

    }

    /**
     * this method run after night
     * a list of happend in night
     */
    public void lastNight(){
        for(Player p:playerArrayList){
            if(!p.isAlive){
                loopSize--;
                sendToAll(p.user + " last night killed");
                if(p.rollP.equals("drLecter mafia") || p.rollP.equals("simple mafia") || p.rollP.equals("godFather mafia")){
                    mafia.remove(p.user);
                }else{
                    citizen.remove(p.user);
                }
                users.remove(p.user);
                rollArrayList.remove(p.rollP);
                //playerArrayList.remove(p);
            }
        }
    }


    /**
     * chat in day
     * @param user that want send a text
     * @throws IOException for read with buffer reader
     */
    public void atDay(String user) throws IOException {
        String msg="";
        msg = in.readLine();
        for (ClientHandler c : clients) {
            c.out.println(user + ")" + msg);
        }
    }

    /**
     * mafias chat in night
     * @param user that want send a text
     * @throws IOException for read with buffer reader
     */
    public void mafiaChat(String user) throws IOException {
        String msg="";
        msg = in.readLine();
        for(int m=0 ; m<clients.size() ; m++){
            for (String s : mafia) {
                if (users.get(m).equals(s)) {
                    clients.get(m).out.println(user+" says "+msg);
                }
            }
        }
    }

    /**
     * mafia know each other
     * dr and mayor know each other
     * other player say the rolls
     */
    public void firstNight(){
        for(int j=0 ; j<rollArrayList.size() ; j++){
            if(rollArrayList.get(j).contains("mafia")){
                for(int h=0 ; h<rollArrayList.size() ;h++){
                    if(rollArrayList.get(h).contains("mafia")){
                        clients.get(h).out.println(users.get(j) + " is "+rollArrayList.get(j));
                    }
                }
            }
        }

        for(int j=0 ; j<rollArrayList.size() ; j++){
            if(rollArrayList.get(j).equals("drCity")){
                for(int h=0 ; h<rollArrayList.size() ;h++){
                    if(rollArrayList.get(h).equals("mayor")){
                        clients.get(h).out.println(users.get(j) + " is doctor city");
                        //clients.get(j).out.println(users.get(h) + " is mayor");
                    }
                }
            }
        }

        for(int j=0 ; j<rollArrayList.size() ; j++){
            if(rollArrayList.get(j).equals("detective")){
                clients.get(j).out.println(users.get(j)+ "-> i am detective");
            }
        }

        for(int j=0 ; j<rollArrayList.size() ; j++){
            if(rollArrayList.get(j).equals("sniper")){
                clients.get(j).out.println(users.get(j)+ "-> i am sniper");
            }
        }

        for(int j=0 ; j<rollArrayList.size() ; j++){
            if(rollArrayList.get(j).equals("psychologist")){
                clients.get(j).out.println(users.get(j)+ "-> i am psychologist");
            }
        }

        for(int j=0 ; j<rollArrayList.size() ; j++){
            if(rollArrayList.get(j).equals("hardDie")){
                clients.get(j).out.println(users.get(j)+ "-> i am hardDie");
            }
        }
    }
    @Override
    public void run() {
        try {
            loop.add(1);
            String user="";
            String roll="";
            String answer="";
            out.println("enter your username");
            user = in.readLine();
            if (checkUser(user, users) == 1) {
                out.println("username " + user + " registrated for you");
                Collections.shuffle(rolls);
                out.println(user + " you are " + rolls.get(0));
                if (rolls.get(0).contains("mafia")) {
                    mafia.add(user);
                } else {
                    citizen.add(user);
                }
                roll=rolls.get(0);
                player=new Player(user , roll);
                playerArrayList.add(player);
                rollArrayList.add(rolls.get(0));
                rolls.remove(0);
                users.add(user);
                i++;
            } else {
                out.println("this username used before\n");
            }
            if(users.size()==10){
                sendToAll("شب معارفه" );
                firstNight();
                sendToAll("[GAME STARTED]");
            }
            while (true) {



                long time1=System.currentTimeMillis();
                long time2=System.currentTimeMillis();
                out.println("its day , all of you can chat");
                while (time2-time1<time) {
                    atDay(user);
                    time2=System.currentTimeMillis();
                    TimeUnit.SECONDS.sleep(1);
                }


                voting++;

                    long timeVoting=System.currentTimeMillis();
                    long timeVoting2=System.currentTimeMillis();
                    out.println("please enter your vote");
                    out.println("you have 30 seconds time");
                for (String s : users) {
                    out.println(s);
                }
                while (timeVoting2 - timeVoting < voteTime) {
                    String chooseUserToGuss = "";
                    String change = "";
                    String chooseUserToGuss2 = "";
                    if (numVote == 0) {
                        chooseUserToGuss = in.readLine();
                        if (chooseUserToGuss.equals("")) {
                            nullVote++;
                            if (nullVote == 3) {
                                player.setAlive(false);
                            }
                        } else {
                            numVote++;
                        }
                        v = new Vote(user, chooseUserToGuss);
                        votes.add(v);
                        v.calculationVotes(chooseUserToGuss, playerArrayList);

                    }
                    out.println("do you want change your vote");
                    change = in.readLine();
                    if (numVote > 0 && change.equals("yes")) {
                        chooseUserToGuss2 = in.readLine();
                        votes.remove(v);
                        v.changeVotes(chooseUserToGuss, playerArrayList);
                        v = new Vote(user, chooseUserToGuss2);
                        votes.add(v);
                        v.calculationVotes(chooseUserToGuss2, playerArrayList);
                    }
                    timeVoting2 = System.currentTimeMillis();
                    TimeUnit.SECONDS.sleep(1);
                }
                    for (Player p : playerArrayList) {
                        if (p.playerVotes > x) {
                            x = p.playerVotes;
                        }
                    }
                    for (Player p : playerArrayList) {
                        if (p.playerVotes == x && x > 0) {
                            sendToAll(p.user + " has a most votes-> " + x + " votes");
                            if (roll.equals("mayor")) {
                                out.println("do you wanna cancelling voting?\n  yes    no");
                                answer = in.readLine();
                                if (answer.equals("yes")) {
                                    sendToAll("mayor cancelled voting");
                                } else {
                                    loop.remove(1);
                                    sendToAll(p.user + " remove with voting");
                                    p.setAlive(false);
//                                    if (p.rollP.equals("drLecter mafia") || p.rollP.equals("simple mafia") || p.rollP.equals("godFather mafia")) {
//                                        mafia.remove(p.user);
//                                    } else {
//                                        citizen.remove(p.user);
//                                    }
//                                    users.remove(p.user);
//                                    rollArrayList.remove(p.rollP);
                                }
                            }
                        }
                        p.playerVotes = 0;
                    }
                    x=0;
                while(true) {
                    out.println("its night , everyone close their eyes");
                    int a;
                    gameLoop++;
                    long timeM1=System.currentTimeMillis();
                    long timeM2=System.currentTimeMillis();
                    out.println("mafias can open the eyes and chat");
                    while (timeM2-timeM1<timeM){
                        mafiaChat(user);
                        timeM2=System.currentTimeMillis();
                    }
                    if (roll.equals("godFather mafia")) {
                        a = godFather();
                        if (a != -1) {
                            playerArrayList.get(a).setAlive(false);
                            i++;
                            loop.add(1);
                            break;
                        }
                    }

                    if (roll.equals("drCity")) {
                        a = drCity();
                        if (a != -1) {
                            playerArrayList.get(a).setAlive(true);
                            i++;
                            loop.add(1);
                            break;
                        }
                    }

                    if(roll.equals("drLecter mafia")){
                        a = drLecter();
                        if (a != -1) {
                            playerArrayList.get(a).setAlive(true);
                            i++;
                            loop.add(1);
                            break;
                        }
                    }

                    if(roll.equals("detective")){
                        out.println(detective());
                        i++;
                        loop.add(1);
                        break;
                    }

                    if(roll.equals("sniper")){
                        out.println("do you wanna shoot?\n  yes    no");
                        answer=in.readLine();
                        if(answer.equals("yes")){
                            sniper();
                        }
                        i++;
                        loop.add(1);
                        break;
                    }

                    if(roll.equals("psychologist")){
                        psychologist();
                        i++;
                        loop.add(1);
                        break;
                    }
                    if((roll.equals("hardDie") && gameLoop==0) || roll.equals("simple mafia") || roll.equals("citizen")||roll.equals("mayor")){
                        i++;
                        loop.add(1);
                        break;
                    }

                    if(gameLoop>0) {
                        if (roll.equals("hardDie")) {
                            if(usePower<2) {
                                out.println("do you wanna use your power?\n  yes    no");
                                answer = in.readLine();
                                if (answer.equals("yes")) {
                                    hardDie();
                                    usePower++;
                                }
                            }
                            i++;
                            loop.add(1);
                            break;
                        }
                    }

                }
                if(loop.size()==9) {
                    lastNight();
                    for(int i=0 ; i<loop.size()-1 ;i++ ){
                        loop.remove(1);
                    }
                }
                if(mafia.size()==0){
                    sendToAll("[citizen WINS]");
                    break;
                }
                if(mafia.size()>=citizen.size()){
                    sendToAll("[mafia WINS]");
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
