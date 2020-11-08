package sample;

//import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import static java.lang.Math.*;

public class Controller {




    public static class DrawStuff extends JComponent {

        private int current[] = {0,0};
        int x = 0;
        int y = 0;
        int numberOfCells=10;
        int cellSize=600/numberOfCells;
        boolean finished=false;
        Cell[][] cell = new Cell[numberOfCells][numberOfCells];

        String startMessage= """
                    
                    
                    
                    
                    Now start the game!
                    """;

        ArrayList<int[]> unusedOperators = new ArrayList<>();
        ArrayList <int[]> answer = new ArrayList<>();

        int[] unusedOpPlace={0,0,0,0};
        ArrayList<int[]> array = new ArrayList<>();

        void cellCreate(){
            for (int j=0; j < numberOfCells; j++) {
                for (int i = 0; i < numberOfCells; i++) {
                    cell[i][j] = new Cell();
                }
            }   //cellák létrehozása

        }
        void useBackTrackOperator(){
            if (cell[x][y].hasParent==true) {
                System.out.println("vissza");
                int xhelp = cell[x][y].parent[0];
                int yhelp = cell[x][y].parent[1];

                current[0]=xhelp;
                current[1]=yhelp;

                x=xhelp;
                y=yhelp;
                System.out.println(" ");


            }
        }
        void drawOut(Graphics2D graph2D){
            for (int j=0; j < numberOfCells; j++) {
                for (int i = 0; i < numberOfCells; i++) {
                    if (cell[i][j].walls[0] == 1) {
                        Shape drawLine0 = new Line2D.Float(10 + (i * cellSize), 10 + (j * cellSize), (10+cellSize) + (i * cellSize), 10 + (j * cellSize));
                        graph2D.draw(drawLine0);
                    }
                    else{
                        graph2D.setColor(Color.lightGray);
                        Shape drawLine0 = new Line2D.Float(10 + (i * cellSize), 10 + (j * cellSize), (10+cellSize) + (i * cellSize), 10 + (j * cellSize));
                        graph2D.draw(drawLine0);
                        graph2D.setColor(Color.black);

                    }
                    if (cell[i][j].walls[1] == 1) {
                        Shape drawLine0 = new Line2D.Float((10+cellSize) + (i * cellSize), 10 + (j * cellSize), (10+cellSize) + (i * cellSize), (10+cellSize) + (j * cellSize));
                        graph2D.draw(drawLine0);

                    }
                    else{
                        graph2D.setColor(Color.lightGray);
                        Shape drawLine0 = new Line2D.Float((10+cellSize) + (i * cellSize), 10 + (j * cellSize), (10+cellSize) + (i * cellSize), (10+cellSize) + (j * cellSize));
                        graph2D.draw(drawLine0);
                        graph2D.setColor(Color.black);

                    }
                    if (cell[i][j].walls[2] == 1) {
                        Shape drawLine0 = new Line2D.Float(10 + (i * cellSize), (10+cellSize) + (j * cellSize), (10+cellSize) + (i * cellSize), (10+cellSize) + (j * cellSize));
                        graph2D.draw(drawLine0);

                    }
                    if (cell[i][j].walls[3] == 1) {
                        Shape drawLine0 = new Line2D.Float(10 + (i * cellSize), 10 + (j * cellSize), 10 + (i * cellSize), cellSize + (j * cellSize));
                        graph2D.draw(drawLine0);

                    }

                }
            }
        }
        void mazeGen(){
            int useThisOperator;
            for (int q=0; q < numberOfCells; q++) {
                for (int w = 0; w < numberOfCells; w++) {
                    while (!cell[q][w].visited) {
                        cell[x][y].usableOperators = usableOperatorsForWalls(cell);
                        useThisOperator = operatorChoiseForWalls(cell);

                        if (useThisOperator == -1) useBackTrackOperator();

                        else UseOperator(cell, useThisOperator);
                    }
                }
            }
        }

        public void paint(Graphics g) {
            Graphics2D graph2D = (Graphics2D) g;
            graph2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape drawRect = new Rectangle2D.Float(10, 10, 600, 600);
            graph2D.draw(drawRect);
            cellCreate();
            mazeGen();
            randomWallBreak(numberOfCells,cell);
            drawOut(graph2D);
            solve(4);

        }


        public void solve(int solvingMethod) {
            switch (solvingMethod){
                case 1:
                    RandomOperatorTry(cell);
                    break;
                case 2:
                    breadthSearch(cell);
                    break;
                case 3:
                    heuristicBackTrack(cell);
                case 4:
                    HeuristicPathFinder(cell);
//                default:
//                    Logger logger= Logger.getLogger(Controller.class.getName());
//                    logger.info("Wrong number for solving methood");

            }


        }

        //falgeneráláshoz használt metódus: használható operátorok kigyűjtése
        int[] usableOperatorsForWalls(Cell cell[][]) {


            int[] usable = {1, 1, 1, 1};//up,right,down,left

            for (int i=0; i<4;i++){

                if (cell[x][y].usedOperators[i]==1){       //van-e már használt operátora, ha igen akkor kivesszük
                    usable[i]=0;
                }

            }


            if (x < 1) {
                usable[3] = 0;

            }
            else if(cell[x - 1][y].visited){
                usable[3]=0;


            }


            if (x > numberOfCells-2) {
                usable[1] = 0;

            }

            else if(cell[x+1][y].visited==true  ){
                usable[1]=0;

            }
            if (y < 1) {
                usable[0] = 0;

            }
            else if(cell[x][y-1].visited==true  ){
                usable[0]=0;

            }
            if (y > numberOfCells-2) {
                usable[2] = 0;

            }
            else if(cell[x][y+1].visited==true ){
                usable[2]=0;
            }

            return usable;
        }
        //falgeneráláshoz használt metódus: kiválasztja, hogy melyik operátort használjuk.
        int operatorChoiseForWalls(Cell cell[][]) {



            int operator = 0;
            int placeCounter = -1;
            for (int i = 0; i < 4; i++) {                            //megszámoljuk a haszálható operátorokat

                if (cell[x][y].usableOperators[i] == 1) {
                    operator++;


                }
            }

            if (operator == 0) {                                    //ha nincs hasnálható operátor,akkor visszalép
                return -1;
            }

            if (operator == 1) {                                     //ha csak 1 operátor alkalmazható, azt alkalmazzuk
                for (int i = 0; i < numberOfCells-1; i++) {

                    if (cell[x][y].usableOperators[i] == 1) {
                        return placeCounter+1;
                    }
                    placeCounter++;

                }
            }

            else{Random rand = new Random();
                int randInt = rand.nextInt(operator);


                if (operator == 4) {                                     //ha mind a 4 operátor alkalmazható, nincs szükség a hely számolásra
                    for (int i = 0; i < numberOfCells-1; i++) {                          //elég randomot kiadni
                        if (operator == randInt) {
                            return placeCounter;
                        } else {
                            placeCounter++;
                        }

                    }
                } else {
                    operator = -1;
                    for (int i = 0; i < numberOfCells-1; i++) {
                        if (operator == randInt) {
                            return placeCounter;
                        } else if (cell[x][y].usableOperators[i] == 1) {
                            operator++;
                            placeCounter++;
                        } else {
                            placeCounter++;
                        }

                    }
                }}
            return placeCounter;
        }

        //használjuk az operátort, minden paramétert átálítunk ennek megfelelően(szülő, használt operátorok,stb)
        int[] UseOperator(Cell[][] cell, int useThisOperator) {




            if (useThisOperator == 0) {
                System.out.println("Fel");
                current [0] =x;
                current [1] =y-1;


                cell[x][y-1].parent[0] = x;
                cell[x][y-1].parent[1] = y;
                cell[x][y].usedOperators[0]=1;
                cell[x][y-1].usedOperators[2]=1;
                cell[x][y-1].hasParent=true;
                cell[x][y].visited=true;
                cell[x][y-1].visited=true;
                cell[x][y].walls[0]=0;
                cell[x][y-1].walls[2]=0;
                y--;


            }

            if (useThisOperator == 1) {
                System.out.println("Jobbra");
                current [0] =x+1;
                current [1] =y;

                cell[x+1][y].parent[0] = x;
                cell[x+1][y].parent[1] = y;
                cell[x][y].usedOperators[1]=1;
                cell[x+1][y].usedOperators[3]=1;
                cell[x+1][y].hasParent=true;
                cell[x][y].visited=true;
                cell[x+1][y].visited=true;
                cell[x][y].walls[1]=0;
                cell[x+1][y].walls[3]=0;
                x++;


            }

            if (useThisOperator == 2) {
                System.out.println("Le");
                current [0] =x;
                current [1] =y+1;

                cell[x][y+1].parent[0] = x;
                cell[x][y+1].parent[1] = y;
                cell[x][y].usedOperators[2]=1;
                cell[x][y+1].usedOperators[0]=1;
                cell[x][y+1].hasParent=true;
                cell[x][y].visited=true;
                cell[x][y+1].visited=true;
                cell[x][y].walls[2]=0;
                cell[x][y+1].walls[0]=0;
                y++;


            }

            if (useThisOperator == 3) {
                System.out.println("Balra");
                current [0] =x-1;
                current [1] =y;

                cell[x-1][y].parent[0] = x;
                cell[x-1][y].parent[1] = y;
                cell[x][y].usedOperators[3]=1;
                cell[x-1][y].usedOperators[1]=1;
                cell[x-1][y].hasParent=true;
                cell[x][y].visited=true;
                cell[x-1][y].visited=true;
                cell[x][y].walls[3]=0;
                cell[x-1][y].walls[1]=0;
                x--;


            }


            return current;
        }

        //megoldás menete back trackkel
        void RandomOperatorTry(Cell[][] cell){
            System.out.println(startMessage);
            x=0;
            y=0;
            int counter=1;






            for(int i=0; i<numberOfCells;i++){
                for(int j=0; j<numberOfCells;j++){
                    for (int k=0; k<4;k++) {
                        cell[i][j].usedOperators[k] = 0;
                    }
                    cell[i][j].visited=false;
                    cell[i][j].hasParent=false;
                }

            }
            cell[0][0].visited=true;
            cell[0][0].parent[0]=-1;
            cell[0][0].parent[1]=-1;


            while(finished!=true){

                cell[x][y].usableOperators = usableOperators(cell);

                int useThisOperator = operator_choise(cell);
                if (useThisOperator == -1) {


                    if (cell[x][y].hasParent == true) {

                        int xhelp = cell[x][y].parent[0];
                        int yhelp = cell[x][y].parent[1];

                        current[0] = xhelp;
                        current[1] = yhelp;

                        x = xhelp;
                        y = yhelp;


                    }


                } else {
                    UseOperator(cell, useThisOperator);
                    System.out.println("\nx:"+x+" y:"+y);
//                    for(int k=0; k<4;k++)
//                        System.out.println(" lehetőségek:"+cell[x][y].usableOperators[k]+" Falak:"+cell[x][y].walls[k]+" használt operátorok:"+cell[x][y].usedOperators[k]);


                }
                System.out.println("lépés:"+counter);
                counter++;
                if(x==numberOfCells-1 && y==numberOfCells-1)
                    finished=true;
            }


        }

        //megoldás Heurisztika és visszalépés használatával
        void heuristicBackTrack(Cell[][] cell){
            System.out.println(startMessage);
            x=0;
            y=0;
            int counter=1;






            for(int i=0; i<numberOfCells;i++){
                for(int j=0; j<numberOfCells;j++){
                    for (int k=0; k<4;k++) {
                        cell[i][j].usedOperators[k] = 0;
                    }
                    cell[i][j].visited=false;
                    cell[i][j].hasParent=false;
                }

            }
            cell[0][0].visited=true;
            cell[0][0].parent[0]=-1;
            cell[0][0].parent[1]=-1;


            while(finished!=true){

                cell[x][y].usableOperators = usableOperators(cell);

                int useThisOperator = operatorChoiseForHeuristic(cell);
                if (useThisOperator == -1) {


                    if (cell[x][y].hasParent == true) {

                        int xhelp = cell[x][y].parent[0];
                        int yhelp = cell[x][y].parent[1];

                        current[0] = xhelp;
                        current[1] = yhelp;

                        x = xhelp;
                        y = yhelp;


                    }


                } else {
                    UseOperator(cell, useThisOperator);
                    System.out.println("\nx:"+x+" y:"+y);
//                    for(int k=0; k<4;k++)
//                        System.out.println(" lehetőségek:"+cell[x][y].usableOperators[k]+" Falak:"+cell[x][y].walls[k]+" használt operátorok:"+cell[x][y].usedOperators[k]);


                }
                System.out.println("lépés:"+counter);
                counter++;
                System.out.println("heurisztika: "+ manhattanDist(x,y));
                if(x==numberOfCells-1 && y==numberOfCells-1)
                    finished=true;
            }


        }









        void HeuristicPathFinder(Cell[][] cell){
            System.out.println(startMessage);
            x=0;
            y=0;
            int counter=1;




            for(int i=0; i<numberOfCells;i++){
                for(int j=0; j<numberOfCells;j++){
                    for (int k=0; k<4;k++) {
                        cell[i][j].usedOperators[k] = 0;
                    }
                    cell[i][j].visited=false;
                    cell[i][j].hasParent=false;

                }

            }
            cell[0][0].visited=true;
            cell[0][0].parent[0]=-1;
            cell[0][0].parent[1]=-1;


            while(!finished){

                cell[x][y].usableOperators = usableOperators(cell);

                int useThisOperator = operatorChoiseForNewHeuristic(cell);
                if (useThisOperator == -1) {
                    // az alternatív útvonalak helyzetei közül ez a legjobb heurisztikájú pozícióhoz vezető pozi.
                    int help=heuristicCalculationForUnusedOperators(cell);
                    int distance=distanceBetweenAgentAndCell(cell,unusedOperators.get(help)[2],unusedOperators.get(help)[3]);
                    getOperatorArray(unusedOperators.get(help)[2],unusedOperators.get(help)[3]);


                    // ebben van a helyes opráttor sorozat: answer


                    int stepCount=answer.size();

                    for (int step=stepCount-1; step>0; step--){
                        System.out.println("x: "+answer.get(step)[0]+" y:"+answer.get(step)[1]);
                        System.out.println("lépés:"+counter+"\n");
                        counter++;
                    }
                    if(stepCount>0){
                        System.out.println("x: "+answer.get(0)[0]+" y:"+answer.get(0)[1]);

                    }
                    x=unusedOperators.get(help)[2];
                    y=unusedOperators.get(help)[3];


                    //törölni kell a használt alternatív útvonal koordinátáját
                    unusedOperators.remove(help);
                    //első lépésben megkeresem azt az elemét a listámnak, amelyik ezt tartalmazza


                    //második lépésként törlöm az elemet



                } else {
                    UseOperator(cell, useThisOperator);
                    System.out.println("\nx:"+x+" y:"+y);
//                    for(int k=0; k<4;k++)
//                        System.out.println(" lehetőségek:"+cell[x][y].usableOperators[k]+" Falak:"+cell[x][y].walls[k]+" használt operátorok:"+cell[x][y].usedOperators[k]);


                }
                System.out.println("lépés:"+counter);
                counter++;
                if(x==numberOfCells-1 && y==numberOfCells-1)
                    finished=true;
            }



        }


        int[] usableOperators(Cell cell[][]) {


            int[] usable = {1, 1, 1, 1};//up,right,down,left

            for (int i=0; i<4;i++) {

                if (cell[x][y].usedOperators[i] == 1) {       //van-e már használt operátora, ha igen akkor kivesszük
                    usable[i] = 0;
                }
            }

            if (y > 0 && cell[x][y - 1].visited)usable[0]=0;
            if ( x < numberOfCells-1 && cell[x + 1][y].visited)usable[1]=0;
            if ( y < numberOfCells-1 && cell[x][y + 1].visited)usable[2]=0;
            if ( x > 0 && cell[x - 1][y].visited)usable[3]=0;
            if (y > 0 && cell[x][y - 1].usedOperators[2] == 1) usable[0]=0;
            if ( x < numberOfCells-1 && cell[x+1][y].usedOperators[3] == 1)usable[1]=0;
            if ( y < numberOfCells-1 && cell[x][y+1].usedOperators[0] == 1)usable[2]=0;
            if ( x > 0 && cell[x-1][y].usedOperators[1] == 1)usable[3]=0;

            if (x < 1)usable[3] = 0;
            else if(cell[x][y].walls[3]==1 )usable[3]=0;
            else if(cell[x - 1][y].walls[1] == 1)usable[3]=0;
            if (x > numberOfCells-2)usable[1] = 0;
            else if(cell[x][y].walls[1]==1 )usable[1]=0;
            else if(x<numberOfCells-2 && cell[x+1][y].walls[3]==1)usable[1]=0;

            if (y < 1)usable[0] = 0;
            else if(cell[x][y].walls[0]==1 ) usable[0]=0;
            else if(y>1 && cell[x][y-1].walls[2]==1)usable[0]=0;
            if (y > numberOfCells-2) usable[2] = 0;
            else if(cell[x][y].walls[2]==1 ) usable[2]=0;
            else if(y<numberOfCells-2 && cell[x][y+1].walls[0]==1) usable[2]=0;


            return usable;
        }

        int operatorChoiseForNewHeuristic(Cell cell[][]) {


            int operator = 0;
            int placeCounter = -1;
            for (int i = 0; i < 4; i++) {                            //megszámoljuk a haszálható operátorokat

                if (cell[x][y].usableOperators[i] == 1) {
                    operator++;


                }
            }


            String noUsableOperatorMessage= """
                    
                    
                    nincs használható operátor
                    alternatív útvonal következik
                    
                    """;

            if (operator == 0) {                                    //ha nincs használható operátor,akkor alt way következik
                System.out.print(noUsableOperatorMessage);
                return -1;
            } else if (operator == 1) {                                     //ha csak 1 operátor alkalmazható, azt alkalmazzuk
                for (int i = 0; i < numberOfCells - 1; i++) {
                    if (cell[x][y].usableOperators[i] == 1) {
                        return placeCounter + 1;
                    }
                    placeCounter++;

                }
            } else {
                //heurisztika kiértékelése!
                Random rand = new Random();

                //jobbra vagy le fog menni a jobb alsó sarokban lévő cél miatt
                if (cell[x][y].usableOperators[1] == 1 && cell[x][y].usableOperators[2] == 1) {
                    if(manhattanDist(x+1,y)< manhattanDist(x,y+1)){
                        if(cell[x][y].usableOperators[0] == 1){
                            unusedOpPlace= new int[]{x, y-1,x,y};
                            unusedOperators.add(unusedOpPlace);
                        }
                        if(cell[x][y].usableOperators[3] == 1){
                            unusedOpPlace= new int[]{x-1, y,x,y};
                            unusedOperators.add(unusedOpPlace);
                        }
                        unusedOpPlace= new int[]{x, y+1,x,y};
                        unusedOperators.add(unusedOpPlace);
                        return 1;
                    }
                    else if(manhattanDist(x+1,y)> manhattanDist(x,y+1)){
                        if(cell[x][y].usableOperators[0] == 1){
                            unusedOpPlace= new int[]{x, y-1,x,y};
                            unusedOperators.add(unusedOpPlace);
                        }
                        if(cell[x][y].usableOperators[3] == 1){
                            unusedOpPlace= new int[]{x-1, y,x,y};
                            unusedOperators.add(unusedOpPlace);
                        }
                        unusedOpPlace= new int[]{x+1, y,x,y};
                        unusedOperators.add(unusedOpPlace);
                        return 2;
                    }
                    else{
                        //itt a 2 cella ahova lépne egyenlő manhattan távolságra van a céltól
                        if(eucDist(x+1,y)< eucDist(x,y+1)){if(cell[x][y].usableOperators[0] == 1){
                            unusedOpPlace= new int[]{x, y-1,x,y};
                            unusedOperators.add(unusedOpPlace);
                        }
                            if(cell[x][y].usableOperators[3] == 1){
                                unusedOpPlace= new int[]{x-1, y,x,y};
                                unusedOperators.add(unusedOpPlace);
                            }
                            unusedOpPlace= new int[]{x, y+1,x,y};
                            unusedOperators.add(unusedOpPlace);
                            return 1;
                        }
                        else if(eucDist(x+1,y)> eucDist(x,y+1)){
                            if(cell[x][y].usableOperators[0] == 1){
                                unusedOpPlace= new int[]{x, y-1,x,y};
                                unusedOperators.add(unusedOpPlace);
                            }
                            if(cell[x][y].usableOperators[3] == 1){
                                unusedOpPlace= new int[]{x-1, y,x,y};
                                unusedOperators.add(unusedOpPlace);
                            }
                            unusedOpPlace= new int[]{x+1, y,x,y};
                            unusedOperators.add(unusedOpPlace);
                            return 2;
                        }
                        else{
                            int randInt = rand.nextInt(2) + 1;
                            if (randInt==1){
                                unusedOpPlace= new int[]{x, y+1,x,y};
                            }
                            else
                            {
                                unusedOpPlace= new int[]{x+1, y,x,y};
                            }
                            unusedOperators.add(unusedOpPlace);
                            return randInt;
                        }
                    }
                } else if (cell[x][y].usableOperators[1] == 1) {
                    if(cell[x][y].usableOperators[0] == 1){
                        unusedOpPlace= new int[]{x, y-1,x,y};
                        unusedOperators.add(unusedOpPlace);
                    }
                    if(cell[x][y].usableOperators[2] == 1){
                        unusedOpPlace= new int[]{x, y+1,x,y};
                        unusedOperators.add(unusedOpPlace);
                    }
                    if(cell[x][y].usableOperators[3] == 1){
                        unusedOpPlace= new int[]{x-1, y,x,y};
                        unusedOperators.add(unusedOpPlace);
                    }
                    return 1;
                } else if (cell[x][y].usableOperators[2] == 1) {
                    if(cell[x][y].usableOperators[0] == 1){
                        unusedOpPlace= new int[]{x, y-1,x,y};
                        unusedOperators.add(unusedOpPlace);
                    }
                    if(cell[x][y].usableOperators[1] == 1){
                        unusedOpPlace= new int[]{x+1, y,x,y};
                        unusedOperators.add(unusedOpPlace);
                    }
                    if(cell[x][y].usableOperators[3] == 1){
                        unusedOpPlace= new int[]{x-1, y,x,y};
                        unusedOperators.add(unusedOpPlace);
                    }
                    return 2;
                } else {
                    int randInt = rand.nextInt(2);
                    if (randInt == 0) {
                        unusedOpPlace= new int[]{x-1, y,x,y};
                        unusedOperators.add(unusedOpPlace);
                        return 0;
                    } else {
                        unusedOpPlace= new int[]{x, y-1,x,y};
                        unusedOperators.add(unusedOpPlace);
                        return 3;
                    }
                }


            }
            return 1;
        }
        int operatorChoiseForHeuristic(Cell cell[][]) {


            int operator = 0;
            int placeCounter = -1;
            for (int i = 0; i < 4; i++) {                            //megszámoljuk a haszálható operátorokat

                if (cell[x][y].usableOperators[i] == 1) {
                    operator++;


                }
            }
            Logger logger
                    = Logger.getLogger(
                    Controller.class.getName());


            String noUsableOperatorMessage= """
                    
                    nincs használható operátor
                    visszalépés
                    """;
//            logger.info(noUsableOperatorMessage);

            if (operator == 0) {                                    //ha nincs használható operátor,akkor visszalép
                System.out.print(noUsableOperatorMessage);
                return -1;
            } else if (operator == 1) {                                     //ha csak 1 operátor alkalmazható, azt alkalmazzuk
                for (int i = 0; i < numberOfCells - 1; i++) {
                    if (cell[x][y].usableOperators[i] == 1) {
                        return placeCounter + 1;
                    }
                    placeCounter++;

                }
            } else {
                //heurisztika kiértékelése!
                Random rand = new Random();

                //jobra vagy le fog menni a jobb alsó sarokban lévő cél miatt
                if (cell[x][y].usableOperators[1] == 1 && cell[x][y].usableOperators[2] == 1) {
                    if(manhattanDist(x+1,y)< manhattanDist(x,y+1)){
                        return 1;
                    }
                    else if(manhattanDist(x+1,y)> manhattanDist(x,y+1)){
                        return 2;
                    }
                    else{
                        //itt a 2 cella ahova lépne egyenlő manhattan távolságra van a céltól
                        if(eucDist(x+1,y)< eucDist(x,y+1)){
                            return 1;
                        }
                        else if(eucDist(x+1,y)> eucDist(x,y+1)){
                            return 2;
                        }
                        else{
                            int randInt = rand.nextInt(2) + 1;
                            return randInt;
                        }
                    }
                } else if (cell[x][y].usableOperators[1] == 1) {
                    return 1;
                } else if (cell[x][y].usableOperators[2] == 1) {
                    return 2;
                } else {
                    int randInt = rand.nextInt(2);
                    if (randInt == 0) {
                        return 0;
                    } else {
                        return 3;
                    }
                }


            }
            return 1;
        }
        public void breadthSearch(Cell[][] cell){

            System.out.println(startMessage);
            x=0;
            y=0;
            int counter=1;

            for(int i=0; i<numberOfCells;i++){
                for(int j=0; j<numberOfCells;j++){
                    for (int k=0; k<4;k++) {
                        cell[i][j].usedOperators[k] = 0;
                    }
                    cell[i][j].visited=false;
                    cell[i][j].hasParent=false;
                }

            }
            cell[0][0].visited=true;
            cell[0][0].parent[0]=-1;
            cell[0][0].parent[1]=-1;

            ArrayList<int[]> array = new ArrayList<>();

            int[] place={0,0};
            array.add(place);
            int i=0;




            while (finished!=true){
                place = array.get(i);

                if(place[0]==numberOfCells-1 && place[1]==numberOfCells-1){
                    System.out.println("Sikeresen megtaláltam a megoldást!");
                    finished=true;
                    break;

                }
                cell[x][y].visited=true;
                System.out.println("Jelenleg itt vagyunk: "+ place[0] +", "+place[1] );

                x=place[0];
                y=place[1];
                cell[x][y].usableOperators = usableOperators(cell);
                System.out.println("Használható operátorok: ");
                for (int j = 0; j<4 ; j++)
                    System.out.println(cell[x][y].usableOperators[j]);




                if(cell[x][y].usableOperators[0]==1 && cell[x][y-1].visited==false ){  //fel



                    cell[place[0]][place[1]].usedOperators[0]=1;
                    int[] temp= new int[2];

                    place[1]--;
                    temp[0]=place[0];
                    temp[1]=place[1];
                    if (!array.contains(temp)) {

                        array.add(temp);

                        cell[place[0]][place[1]].visited = true;
                        cell[place[0]][place[1]].hasParent = true;
                        cell[place[0]][place[1]].parent[0] = place[0];
                        cell[place[0]][place[1]].parent[1] = place[1] + 1;
                        cell[place[0]][place[1]].usableOperators[2] = 0;
                    }
                    place[1]++;


                }
                if(cell[x][y].usableOperators[1]==1 && cell[x+1][y].visited==false){  //jobbra



                    cell[x][y].usedOperators[1]=1;

                    int[] temp= new int[2];
                    place[0]++;
                    temp[0] =place[0];
                    temp[1]= place[1];
                    if (!array.contains(temp)) {

                        array.add(temp);

                        cell[place[0]][place[1]].hasParent = true;
                        cell[place[0]][place[1]].parent[0] = place[0] - 1;
                        cell[place[0]][place[1]].parent[1] = place[1];
                        cell[place[0]][place[1]].visited = true;
                        cell[place[0]][place[1]].usableOperators[3] = 0;
                    }
                    place[0]--;


                }
                if(cell[x][y].usableOperators[2]==1 && cell[x][y+1].visited==false){  //le



                    cell[place[0]][place[1]].usedOperators[2]=1;

                    int[] temp= new int[2];
                    place[1]++;
                    temp[0]=place[0];
                    temp[1]=place[1];
                    if (!array.contains(temp)) {

                        array.add(temp);

                        cell[place[0]][place[1]].visited = true;
                        cell[place[0]][place[1]].hasParent = true;
                        cell[place[0]][place[1]].parent[0] = place[0];
                        cell[place[0]][place[1]].parent[1] = place[1] - 1;
                        cell[place[0]][place[1]].usableOperators[0] = 0;
                    }

                    place[1]--;


                }
                if(cell[x][y].usableOperators[3]==1 && cell[x-1][y].visited==false){  //bal



                    cell[place[0]][place[1]].usedOperators[3]=1;

                    int[] temp= new int[2];
                    place[0]--;
                    temp[0]=place[0];
                    temp[1]=place[1];
                    if (!array.contains(temp)) {

                        array.add(temp);

                        cell[place[0]][place[1]].hasParent = true;
                        cell[place[0]][place[1]].parent[0] = place[0] + 1;
                        cell[place[0]][place[1]].parent[1] = place[1];
                        cell[place[0]][place[1]].visited = true;
                        cell[place[0]][place[1]].usableOperators[1] = 0;

                    }
                    place[0]++;

                }

                i++;

            }
//            for (int j =0; j<numberOfCells; j++){
//                for (int k =0; k<numberOfCells; k++){
//                    System.out.println("x:"+j+" y:"+k+" szülője: "+cell[j][k].parent[0]+", "+cell[j][k].parent[1]);
//                }
//            }





            // finished = true

            x = array.get(i)[0]; //9
            y = array.get(i)[1]; //9
            ArrayList <int[]> answer = new ArrayList<>();




            while(x!=0 || y!=0){

                answer.add(cell[x][y].parent);
                int x2=x;
                int y2=y;
                x=cell[x2][y2].parent[0];
                y=cell[x2][y2].parent[1];

            }

            for (int[] ans : answer){
                System.out.println(ans[0] +", "+ ans[1]);
            }

        }






        int operator_choise(Cell cell[][]) {



            int operator = 0;
            int placeCounter = -1;
            for (int i = 0; i < 4; i++) {                            //megszámoljuk a haszálható operátorokat

                if (cell[x][y].usableOperators[i] == 1) {
                    operator++;


                }
            }

            if (operator == 0) {                                    //ha nincs használható operátor,akkor visszalép
                System.out.println("nincs használható operátor");
                return -1;
            }

            else if (operator == 1) {                                     //ha csak 1 operátor alkalmazható, azt alkalmazzuk
                for (int i = 0; i < numberOfCells-1; i++) {
                    if (cell[x][y].usableOperators[i] == 1) {
                        return placeCounter+1;
                    }
                    placeCounter++;

                }
            }



            else{
                Random rand = new Random();
                int randInt = rand.nextInt(operator);



                if (operator == 4) {                                     //ha mind a 4 operátor alkalmazható, nincs szükség a hely számolásra
                    return randInt;



                }
                else {
                    operator = -1;
                    for (int i = 0; i < numberOfCells-1; i++) {
                        if (operator == randInt) {
                            return placeCounter;
                        } else if (cell[x][y].usableOperators[i] == 1) {
                            operator++;
                            placeCounter++;
                        } else {
                            placeCounter++;
                        }

                    }
                }
            }
            return placeCounter;
        }
        //itt választom ki, hogy melyik felakat döntöm le az új útvonalak miatt
        void randomWallBreak(int num, Cell[][] cell){
            num=num-2;
            int wallX;
            int wallY;
            for(int i=0; i<num/2; i++){
                wallX=(int)(Math.random()*num)+1;
                wallY=(int)(Math.random()*num)+1;

                cell[wallX][wallY].walls[0] = 0;
                cell[wallX][wallY].usableOperators[0] = 1;
                cell[wallX][wallY-1].walls[2]=0;
                cell[wallX][wallY-1].usableOperators[2] = 1;


                cell[wallX][wallY].walls[1] = 0;
                cell[wallX][wallY].usableOperators[1] = 1;
                cell[wallX+1][wallY].walls[3]=0;
                cell[wallX+1][wallY].usableOperators[3] = 1;


                cell[wallX][wallY].walls[2] = 0;
                cell[wallX][wallY].usableOperators[2] = 1;
                cell[wallX][wallY+1].walls[0]=0;
                cell[wallX][wallY+1].usableOperators[0] = 1;


                cell[wallX][wallY].walls[3] = 0;
                cell[wallX][wallY].usableOperators[3] = 1;
                cell[wallX-1][wallY].walls[1]=0;
                cell[wallX-1][wallY].usableOperators[1] = 1;



                System.out.println("X értéke: "+wallX+"Y értéke: "+wallY);

            }
        }

        int heuristicAns(int x1, int y1, int x2, int y2){
            int manhattan1=((numberOfCells-1) + (numberOfCells-1) ) - ( x1 + y1);
            int manhattan2=((numberOfCells-1) + (numberOfCells-1) ) - ( x2 + y2);
            if(manhattan1<manhattan2){
                return 1;
            }
            else if (manhattan2< manhattan1){
                return 2;
            }
            else {
                double euc1=sqrt( pow((numberOfCells-1-x1),2 ) + pow((numberOfCells-1-x1),2));
                double euc2=sqrt( pow((numberOfCells-1-x2),2 ) + pow((numberOfCells-1-x2),2));
                if (euc1<euc2){
                    return 1;
                }
                else if (euc2<euc1){
                    return 2;
                }
                else {
                    Random rand = new Random();
                    int randInt = rand.nextInt(2) + 1;
                    return randInt;
                }
            }



        }

        int manhattanDist(int x, int y){
            return ( (numberOfCells-1) + (numberOfCells-1) ) - ( x + y);
        }

        double eucDist(int x,int y){
            return sqrt( pow((numberOfCells-1-x),2 ) + pow((numberOfCells-1-x),2));
        }

        int heuristicCalculationForUnusedOperators(Cell[][] cell){
            //a jelenlegi nem használt operátorok listájából összehasonlítom
            int x1,y1;
            int placeInList;
            double heuristicAns;
            double dist;
            double bestHeuristic=1000;
            x1=unusedOperators.get(0)[2];
            y1=unusedOperators.get(0)[3];
            placeInList=0;
            for(int i=0; i<unusedOperators.size(); i++ ){
                dist=distanceBetweenAgentAndCell(cell,unusedOperators.get(i)[2],unusedOperators.get(i)[3]);
                heuristicAns=manhattanDist(unusedOperators.get(i)[2],unusedOperators.get(i)[3]);

                if(heuristicAns+dist<bestHeuristic){
                    bestHeuristic=heuristicAns+dist;
                    x1=unusedOperators.get(i)[2];
                    y1=unusedOperators.get(i)[3];
                    placeInList=i;
                }else if (heuristicAns+dist==bestHeuristic){
                    double heuristicAns1=eucDist(x,y);
                    double dist1=distanceBetweenAgentAndCell(cell,x,y);
                    heuristicAns=eucDist(unusedOperators.get(i)[2],unusedOperators.get(i)[3]);
                    if (heuristicAns+dist>heuristicAns1+dist1 || heuristicAns+dist==heuristicAns1+dist1){
                        bestHeuristic=heuristicAns+dist;
                        x1=unusedOperators.get(i)[2];
                        y1=unusedOperators.get(i)[3];
                        placeInList=i;

                    }
                }

            }
//            if(x1==x && y1==y){
//                x1=unusedOperators.get(unusedOperators.size()-1)[2];
//                y1=unusedOperators.get(unusedOperators.size()-1)[3];
//            }
//            int[] ans={x1,y1};
            cell[x1][y1].visited=true;
            cell[x1][y1].usableOperators= usableOperators(cell);

            return placeInList;
        }
        int distanceBetweenAgentAndCell(Cell[][] cell,int x1, int y1) {
            //x1,y1 az a koordináta, ahova szeretnénk lépni

            array.clear();
            int[] place = {x, y};
            array.add(place);
            int i = 0;

            for (int j=0; j<numberOfCells-1;j++)            {
                for (int k=0; k<numberOfCells-1;k++){

                    cell[j][k].parentForAltWay[0] = -1;
                    cell[j][k].parentForAltWay[1] = -1;

                }
            }

            for (int j = 0; j < 4; j++){
                cell[place[0]][place[1]].usedOperators[j]=0;
            }

                while(true){


                    place = array.get(i);

                    if(place[0]==x1 && place[1]==y1){
                        break;

                    }


                    cell[place[0]][place[1]].usableOperators = usable_operators_in_visited_cells(cell,place[0],place[1]);
                    if(place[0]==0 && place[1]==0)
                    {
                        for(int j=0; j<4; j++){
                        cell[place[0]][place[1]].usableOperators[j]=0;
                        }
                    }


                    if(cell[place[0]][place[1]].usableOperators[0]==1){  //fel

                        int[] temp= place.clone();
                        temp[1]--;


                        if (!isInIt(temp)) {
                            array.add(temp);
                            cell[place[0]][place[1]-1].parentForAltWay[0]=place[0];
                            cell[place[0]][place[1]-1].parentForAltWay[1]=place[1];
                        }


                        cell[place[0]][place[1]].usedOperators[0]=1;

                    }
                    if(cell[place[0]][place[1]].usableOperators[1]==1 ){  //jobbra

                        int[] temp= place.clone();
                        temp[0]++;


                        if (!isInIt(temp)) {
                            array.add(temp);
                            cell[place[0]+1][place[1]].parentForAltWay[0]=place[0];
                            cell[place[0]+1][place[1]].parentForAltWay[1]=place[1];

                        }



                        cell[place[0]][place[1]].usedOperators[1]=1;



                    }
                    if(cell[place[0]][place[1]].usableOperators[2]==1 ){  //le

                        int[] temp= place.clone();
                        temp[1]++;

                        if (!isInIt(temp)) {
                            array.add(temp);
                            cell[place[0]][place[1]+1].parentForAltWay[0]=place[0];
                            cell[place[0]][place[1]+1].parentForAltWay[1]=place[1];

                        }


                        cell[place[0]][place[1]].usedOperators[2]=1;




                    }
                    if(cell[place[0]][place[1]].usableOperators[3]==1){  //bal


                        int[] temp= place.clone();
                        temp[0]--;


                        if (!isInIt(temp)) {
                            array.add(temp);
                            cell[place[0]-1][place[1]].parentForAltWay[0]=place[0];
                            cell[place[0]-1][place[1]].parentForAltWay[1]=place[1];

                        }





                        cell[place[0]][place[1]].usedOperators[3]=1;



                    }



                    i++;

                }



            return i;


        }
        void getOperatorArray(int x1,int y1){
            answer.clear();
            //listából kiválasztott legjobb heurisztikájú pozíció
            int x2 = x1;
            int y2 = y1;




            // x,y a jelenlegi(elakadt) helyzetünk
            while(x2!=x || y2!=y){
                int[] a={x2,y2};
                answer.add(a);
                int x3=x2;
                int y3=y2;
                x2=cell[x3][y3].parentForAltWay[0];
                y2=cell[x3][y3].parentForAltWay[1];

            }


        }
        int[] usable_operators_in_visited_cells(Cell cell[][], int xHelp, int yHelp) {


            int[] usable = {1, 1, 1, 1};//up,right,down,left
            for(int i=0; i<4; i++){
                if (cell[xHelp][yHelp].usedOperators[i]==1){
                    cell[xHelp][yHelp].usableOperators[i]=0;
                }

            }
//fel
            if(yHelp>0 && cell[xHelp][yHelp-1].visited==false){
                usable[0]=0;
            }
            if (yHelp == 0) {
                usable[0] = 0;
            }
            if(cell[xHelp][yHelp].walls[0]==1 ){
                usable[0]=0;
            }
            if(yHelp>0 && cell[xHelp][yHelp-1].walls[2]==1){
                usable[0]=0;
            }
//jobb
            if(xHelp<numberOfCells-1 && cell[xHelp+1][yHelp].visited==false){
                usable[1]=0;
            }
            if (xHelp > numberOfCells-2) {
                usable[1] = 0;
            }
            if(cell[xHelp][yHelp].walls[1]==1 ){
                usable[1]=0;
            }
            if(xHelp<numberOfCells-2 && cell[xHelp+1][yHelp].walls[3]==1){
                usable[1]=0;
            }
//le
            if(yHelp<numberOfCells-1 &&cell[xHelp][yHelp+1].visited==false){
                usable[2]=0;
            }
            if (yHelp > numberOfCells-2) {
                usable[2] = 0;

            }
            if(cell[xHelp][yHelp].walls[2]==1 ){
                usable[2]=0;
            }
            if(yHelp<numberOfCells-2 && cell[xHelp][yHelp+1].walls[0]==1) {
                usable[2] = 0;
            }
//bal
            if(xHelp>0 && cell[xHelp-1][yHelp].visited==false){
                usable[3]=0;
            }
            if (xHelp == 0) {
                usable[3] = 0;
            }
            if(cell[xHelp][yHelp].walls[3]==1 ){
                usable[3]=0;
            }
            if(xHelp>0 && cell[xHelp-1][yHelp].walls[1]==1){
                usable[3]=0;
            }


            return usable;
        }
        boolean isInIt(int[] elementArray){
            for (int[] i:array){
                if(i[0]==elementArray[0] && i[1]==elementArray[1] ){
                    return true;
                }
            }
            return false;
        }
    }

}


