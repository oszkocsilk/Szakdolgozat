package sample;

public class Cell {

    boolean visited;
    int walls[];              //mindenhol fal van
    int usableOperators[];    //minden operátor használható
    int parent[];
    boolean hasParent;
    int usedOperators[];      //használt operátorok: egyiket se használtam még

    public Cell(){
         visited=false;
         walls= new int[]{1, 1, 1, 1};              //mindenhol fal van
         usableOperators= new int[]{1, 1, 1, 1};    //minden operátor használható
         parent= new int[]{-1, -1};
         hasParent=false;
         usedOperators= new int[]{0, 0, 0, 0};      //használt operátorok: egyiket se használtam még
    }

}


