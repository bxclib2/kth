import java.util.*;
import java.io.*;

public class Adkcasting {

    private Kattio io;
    private Role[] roleArray, originalRoleArray;
    private ArrayList<Integer>[] sceneArray;
    // private ArrayList<Integer>[] roleArray, sceneArray, originalRoleArray;
    //private ArrayList<ArrayList<Integer>> actor;
    private HashMap<Integer, ArrayList<Integer>> actor;
    private int[] cast;
    private boolean divaOneCasted, swapDivas;
    private int roles, scenes, actors, superActors, usedActors, divasCasted;
    public static void main(String[] args) {
        new Adkcasting();
    }
    public Adkcasting(){
        io = new Kattio(System.in, System.out);
        superActors = 1;
        usedActors = 2; // divorna måste vara med
        readNumbers();
        roleArray = new Role[roles + 1];
        originalRoleArray = new Role[roles + 1];

        sceneArray = new ArrayList[scenes + 1];
        //actor = new ArrayList<ArrayList<Integer>>();
        actor = new HashMap<Integer, ArrayList<Integer>>();
        cast = new int[roles + 1];
        for(int i = 1; i <= roles; i++) {
            roleArray[i] = new Role(i);
            // roleArray[i] = new ArrayList<Integer>();
        }
        for(int i = 1; i <= scenes; i++) {
            sceneArray[i] = new ArrayList<Integer>();
        }
        actor.put(1, new ArrayList<Integer>());
        actor.put(2, new ArrayList<Integer>());
        originalRoleArray = roleArray;
        readRoles(roles);
        readScenes(scenes);
        //testScenes();

        fillWithDivas();

        for(int i = 1; i < roles; i++) {
            tryMoreDivas(i);
        }
        while(obligatoryRole());
        while(tryActors());
        fillWithSuperActors();
        printAll();
        io.flush();
    }

    private void readNumbers() {
        roles = io.getInt();
        scenes = io.getInt();
        actors = io.getInt();
    }

    private void readRoles(int n) {
        for(int i = 1; i <= n; i++) {
            int rolesToRead = io.getInt();
            for(int j = 0; j < rolesToRead; j++) {
                roleArray[i].add(io.getInt());
            }
        }
    }
    private void readScenes(int s) {
        for(int i = 1; i <= s; i++) {
            int scenesToRead = io.getInt();
            for(int j = 0; j < scenesToRead; j++) {
                sceneArray[i].add(io.getInt());
            }
        }

    }
    private void fillWithDivas() {

        for(int roleNumber = 1; roleNumber < cast.length; roleNumber++) {

            // spelar ingen denna roll?
            if(cast[roleNumber] == 0) {

                // har vi inte ansatt någon diva än?
                if(divasCasted == 0) {
                    // kan diva 1 spela rollen?

                    // testa diva 2 först i stället
                    if(swapDivas) {
                        if(roleArray[roleNumber].contains(2)) {
                        // låt skådespelare 2 spela roleNumber (ingen superskådis)
                            addActor(2, roleNumber, false);
                            divasCasted = 1;
                            divaOneCasted = false;
                        } else if(roleArray[roleNumber].contains(1)) {
                            addActor(1, roleNumber, false);
                            divaOneCasted = true;
                            divasCasted = 1;
                        }
                    } else {

                        if(roleArray[roleNumber].contains(1)) {
                            // låt skådespelare 1 spela roleNumber (ingen superskådis)
                            addActor(1, roleNumber, false);
                            divaOneCasted = true;
                            divasCasted = 1;
                        } else if(roleArray[roleNumber].contains(2)) {
                            addActor(2, roleNumber, false);
                            divaOneCasted = false;
                            divasCasted = 1;
                        }
                    }
                }
                else if(divasCasted == 1){
                    int diva;
                    ArrayList<Integer> divaRoles;
                    if(divaOneCasted) {
                        // vår diva är skådespelare 2
                        diva = 2;
                        // hämta rollerna som diva 1 spelar
                        divaRoles = actor.get(1);
                    } else {
                        // vår diva är skådespelare 1
                        diva = 1;
                        // hämta rollerna som diva 2 spelar
                        divaRoles = actor.get(2);
                    }

                    // kan vår diva spela denna roll?
                    if(roleArray[roleNumber].contains(diva)) {

                        boolean divasInSameScene = false;
                        //kolla så divorna inte är i samma scen
                        for(int scene = 1; scene < sceneArray.length; scene++) {

                            // är rollen med i denna scen?
                            if(sceneArray[scene].contains(roleNumber)) {
                                for(int divaRole = 0; divaRole < divaRoles.size(); divaRole++) {
                                    // spelar diva 1 redan i scenen med vår roll?
                                    if(sceneArray[scene].contains(divaRoles.get(divaRole))) {
                                        divasInSameScene = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if(!divasInSameScene) {
                            // låt denna diva spela roleNumber (ingen superskådis)
                            addActor(diva, roleNumber, false);
                            divasCasted = 2;
                        }
                    }
                }
            }
        }
        if(divasCasted != 2) {
            Arrays.fill(cast, 0);
            divasCasted = 0;
            roleArray = originalRoleArray;
            actor.get(1).clear();
            actor.get(2).clear();
            swapDivas = true;
            fillWithDivas();
        }
    }
    private void tryMoreDivas(int roleNumber) {
        ArrayList<Integer> diva1Roles = actor.get(1);
        ArrayList<Integer> diva2Roles = actor.get(2);
        int diva = 1;

        if(cast[roleNumber] == 0) {
            if(roleArray[roleNumber].contains(1)) {
                if(!checkScenes(diva, roleNumber)) return;

                // kolla så divorna inte spelar mot varandra
                for(int scene = 1; scene < scenes; scene++) {
                    if(sceneArray[scene].contains(roleNumber)) {
                        for(int i = 0; i < diva2Roles.size(); i++) {
                            if(sceneArray[scene].contains(diva2Roles.get(i))) {
                                return;
                            }
                        }
                    }
                }
                addActor(diva, roleNumber, false);
            }
            else if(roleArray[roleNumber].contains(2)) {
                diva = 2;
                if(!checkScenes(diva, roleNumber)) return;

                // kolla så divorna inte spelar mot varandra
                for(int scene = 1; scene < scenes; scene++) {
                    if(sceneArray[scene].contains(roleNumber)) {
                        for(int i = 0; i < diva1Roles.size(); i++) {
                            if(sceneArray[scene].contains(diva1Roles.get(i))) {
                                return;
                            }
                        }
                    }
                }
                addActor(diva, roleNumber, false);
            }
        }
    }
    private boolean obligatoryRole() {
        Arrays.sort(roleArray,1,roleArray.length);
        for(int i = 1; i < roleArray.length; i++) {
            int roleNumber = roleArray[i].id;
            if(roleArray[i].size() == 1 && cast[roleNumber] == 0 && roleArray[i].get(0) > 2) {
                if(checkScenes(roleArray[i].get(0), roleNumber)) {
                    addActor(roleArray[i].get(0), roleNumber, false);
                    return true;
                }
            }
        }
        return false;
    }
    private boolean tryActors() {
        Arrays.sort(roleArray,1,roleArray.length);


        for(int i = 1; i < roleArray.length; i++) {
            int roleNumber = roleArray[i].id;
            if(cast[roleNumber] == 0) {
                for(int actorNumber = roleArray[i].size()-1; actorNumber >= 0 ; actorNumber--) {
                    int possibleActor = roleArray[i].get(actorNumber);
                    if(possibleActor > 2) {
                        if(checkScenes(possibleActor, roleNumber)) {
                            addActor(possibleActor, roleNumber, false);
                            return true;
                        }
                    }
                }
            }
        }


        // for(int roleNumber = 1; roleNumber < roleArray.length; roleNumber++) {
            // if(cast[roleNumber] == 0) {
                // for(int actorNumber = 0; actorNumber < roleArray[roleNumber].size(); actorNumber++) {
                    // int possibleActor = roleArray[roleNumber].get(actorNumber);
                    // if(possibleActor > 2) {
                        // if(checkScenes(possibleActor, roleNumber)) {
                            // addActor(possibleActor, roleNumber, false);
                            // return true;
                        // }
                    // }
                // }
            // }
        // }
        return false;
    }
    private boolean canActorPlayRole(int actorNumber, int roleNumber) {
        if(roleArray[roleNumber].contains(actorNumber)) {
            return true;
        }
        return false;
    }
    private boolean checkScenes(int actorNumber, int roleNumber) {
        // Om denna skådespelare inte har någon roll behöver vi inte kolla något
        if(actor.get(actorNumber) == null) {
            return true;
        }
        for(int scene = 1; scene < sceneArray.length; scene++) {

            ArrayList<Integer> actorRoles = actor.get(actorNumber);
            // är rollen med i denna scen?
            if(sceneArray[scene].contains(roleNumber)) {
                for(int i = 0; i < actorRoles.size(); i++) {
                    // kolla så att den nya rollen inte är med i samma scen
                    if(sceneArray[scene].contains(actorRoles.get(i))) {
                        return false;
                    }
                }
            }
        }
        // actorNumber kan spela denna roll utan att möta sig själv
        return true;
    }
    private void fillWithSuperActors() {
        for(int i = 1; i < cast.length; i++) {
            if(cast[i] == 0) {
                // superskådis (true) spelar roll i
                addActor(actors + superActors, i, true);
            }
        }
    }
    private void addActor(int actorNumber, int role, boolean isSuperActor) {
        cast[role] = actorNumber;
        if(actor.get(actorNumber) == null) {
            actor.put(actorNumber, new ArrayList<Integer>());
            usedActors++;
        }
        actor.get(actorNumber).add(role);
        if(isSuperActor) {
            superActors++;
        } else {
            for (int i = 1; i < roleArray.length; i++) {
                if (roleArray[i].id == role) {
                    roleArray[i].remove((Object)new Integer(actorNumber));
                    return;
                }
            }
            // roleArray[role].remove((Object)new Integer(actorNumber));
        }
    }
    private void printAll() {
        io.println(usedActors);
        for(int i = 1; i < (actors + superActors); i++) {
            if(actor.get(i) != null && actor.get(i).size() > 0) {
                ArrayList<Integer> tmp = actor.get(i);

                io.print(i + " " + tmp.size());
                for(int j = 0; j < tmp.size(); j++) {
                    io.print(" " + tmp.get(j));
                }
                io.println();
            }
        }
    }

    /** Simple yet moderately fast I/O routines.
     *
     * Example usage:
     *
     * Kattio io = new Kattio(System.in, System.out);
     *
     * while (io.hasMoreTokens()) {
     *    int n = io.getInt();
     *    double d = io.getDouble();
     *    double ans = d*n;
     *
     *    io.println("Answer: " + ans);
     * }
     *
     * io.close();
     *
     *
     * Some notes:
     *
     * - When done, you should always do io.close() or io.flush() on the
     *   Kattio-instance, otherwise, you may lose output.
     *
     * - The getInt(), getDouble(), and getLong() methods will throw an
     *   exception if there is no more data in the input, so it is generally
     *   a good idea to use hasMoreTokens() to check for end-of-file.
     *
     * @author: Kattis
     */

    class Kattio extends PrintWriter {
        public Kattio(InputStream i) {
        super(new BufferedOutputStream(System.out));
        r = new BufferedReader(new InputStreamReader(i));
        }
        public Kattio(InputStream i, OutputStream o) {
        super(new BufferedOutputStream(o));
        r = new BufferedReader(new InputStreamReader(i));
        }

        public boolean hasMoreTokens() {
        return peekToken() != null;
        }

        public int getInt() {
        return Integer.parseInt(nextToken());
        }

        public double getDouble() {
        return Double.parseDouble(nextToken());
        }

        public long getLong() {
        return Long.parseLong(nextToken());
        }

        public String getWord() {

        return nextToken();
        }



        private BufferedReader r;
        private String line;
        private StringTokenizer st;
        private String token;

        private String peekToken() {
        if (token == null)
            try {
            while (st == null || !st.hasMoreTokens()) {
                line = r.readLine();
                if (line == null) return null;
                st = new StringTokenizer(line);
            }
            token = st.nextToken();
            } catch (IOException e) { }
        return token;
        }

        private String nextToken() {
        String ans = peekToken();
        token = null;
        return ans;
        }
    }
}
class Role extends ArrayList<Integer> implements Comparable<Role> {
    public Integer id;
    // public ArrayList<Integer> actors;
    public Role(int id) {
        this.id = id;
    }
    public int compareTo(Role role) {
        return this.size()-role.size();
    }
}
