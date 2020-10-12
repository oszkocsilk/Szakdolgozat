package sample;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class Controller {



    public static class DrawStuff extends JComponent {

        private int current[] = {0,0};
        int x = 0, y = 0;
        int numberOfCells=10;
        int cellSize=800/numberOfCells;
        boolean finished=false;






        public void paint(Graphics g) {
            Graphics2D graph2D = (Graphics2D) g;

            graph2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape drawRect = new Rectangle2D.Float(10, 10, 800, 800);
            graph2D.draw(drawRect);



            Cell[][] cell = new Cell[numberOfCells][numberOfCells];

            for (int j=0; j < numberOfCells; j++) {
                for (int i = 0; i < numberOfCells; i++) {
                    cell[i][j] = new Cell();
                }
            }   //cellák létrehozása


            int useThisOperator = -10;
            for (int q=0; q < numberOfCells; q++) {
                for (int w = 0; w < numberOfCells; w++) {
                    while (cell[q][w].visited!=true) {

                        cell[x][y].usableOperators = usable_operators_for_walls(cell);
                        useThisOperator = operator_choise_for_walls(cell);



                        if (useThisOperator == -1) {


                            if (cell[x][y].hasParent==true) {
                                System.out.println("vissza");
                                int xhelp = cell[x][y].parent[0];
                                int yhelp = cell[x][y].parent[1];

                                current[0]=xhelp;
                                current[1]=yhelp;

                                x=xhelp;
                                y=yhelp;


                            }


                        } else {
                            UseOperator(cell, useThisOperator);
                        }



                    }
                }
            }
            randomWallBreak(numberOfCells,cell);


            //kirajzolás
            for (int j=0; j < numberOfCells; j++) {
                for (int i = 0; i < numberOfCells; i++) {
                    if (cell[i][j].walls[0] == 1) {
                        Shape drawLine0 = new Line2D.Float(10 + (i * cellSize), 10 + (j * cellSize), (10+cellSize) + (i * cellSize), 10 + (j * cellSize));
                        graph2D.draw(drawLine0);


                    }
                    if (cell[i][j].walls[1] == 1) {
                        Shape drawLine0 = new Line2D.Float((10+cellSize) + (i * cellSize), 10 + (j * cellSize), (10+cellSize) + (i * cellSize), (10+cellSize) + (j * cellSize));
                        graph2D.draw(drawLine0);

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


            //megoldás
            if(finished==false) {
                //RandomOperatorTry(cell);
                //heuristicBackTrack(cell);


               breadthSearch(cell);


            }
        }

        //falgeneráláshoz használt metódus: használható operátorok kigyűjtése
        int[] usable_operators_for_walls(Cell cell[][]) {


            int[] usable = {1, 1, 1, 1};//up,right,down,left

            for (int i=0; i<4;i++){

                if (cell[x][y].usedOperators[i]==1){       //van-e már használt operátora, ha igen akkor kivesszük
                    usable[i]=0;
                }

            }


            if (x < 1) {
                usable[3] = 0;

            }
            else if(cell[x-1][y].visited==true ){
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
        int operator_choise_for_walls(Cell cell[][]) {



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

        //használjuk az operátort, midnen paramétert átálítunk ennek megfelelően(szülő, használt operátorok,stb)
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
            System.out.println("\n\n\n\n\n\n\n\n\nNow start the game!");
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

                cell[x][y].usableOperators = usable_operators(cell);

                int useThisOperator = operator_choise(cell);
                if (useThisOperator == -1) {


                    if (cell[x][y].hasParent == true) {
                        System.out.println("visszalépés");

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
            System.out.println("\n\n\n\n\n\n\n\n\nNow start the game!");
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

                cell[x][y].usableOperators = usable_operators(cell);

                int useThisOperator = operator_choise_for_heuristic(cell);
                if (useThisOperator == -1) {


                    if (cell[x][y].hasParent == true) {
                        System.out.println("visszalépés");

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
                System.out.println("heurisztika: "+heuristica(x,y));
                if(x==numberOfCells-1 && y==numberOfCells-1)
                    finished=true;
            }


        }

        int[] usable_operators(Cell cell[][]) {


            int[] usable = {1, 1, 1, 1};//up,right,down,left

            for (int i=0; i<4;i++){

                if (cell[x][y].usedOperators[i]==1){       //van-e már használt operátora, ha igen akkor kivesszük
                    usable[i]=0;
                }
                if ((y > 0 && cell[x][y - 1].visited) == true){
                    usable[0]=0;
                }
                if ( x < numberOfCells-1 && cell[x+1][y].visited==true){
                    usable[1]=0;
                }
                if ( y < numberOfCells-1 && cell[x][y+1].visited==true){
                    usable[2]=0;
                }
                if ( x > 0 && cell[x-1][y].visited==true){
                    usable[3]=0;
                }
                if (y > 0 && cell[x][y - 1].usedOperators[2] == 1){
                    usable[0]=0;
                }
                if ( x < numberOfCells-1 && cell[x+1][y].usedOperators[3] == 1){
                    usable[1]=0;
                }
                if ( y < numberOfCells-1 && cell[x][y+1].usedOperators[0] == 1){
                    usable[2]=0;
                }
                if ( x > 0 && cell[x-1][y].usedOperators[1] == 1){
                    usable[3]=0;
                }

            }


            if (x < 1) {
                usable[3] = 0;

            }
            else if(cell[x][y].walls[3]==1 ){
                usable[3]=0;

            }
            else if(x>0 && cell[x-1][y].walls[1]==1){
                usable[3]=0;
            }


            if (x > numberOfCells-2) {
                usable[1] = 0;

            }
            else if(cell[x][y].walls[1]==1 ){
                usable[1]=0;

            }
            else if(x<numberOfCells-2 && cell[x+1][y].walls[3]==1){
                usable[1]=0;
            }






            if (y < 1) {
                usable[0] = 0;

            }
            else if(cell[x][y].walls[0]==1 ){
                usable[0]=0;

            }
            else if(y>1 && cell[x][y-1].walls[2]==1){
                usable[0]=0;
            }



            if (y > numberOfCells-2) {
                usable[2] = 0;

            }
            else if(cell[x][y].walls[2]==1 ){
                usable[2]=0;
            }
            else if(y<numberOfCells-2 && cell[x][y+1].walls[0]==1){
                usable[2]=0;
            }

            return usable;
        }

        int operator_choise_for_heuristic(Cell cell[][]) {


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
                    if(heuristica(x+1,y)<heuristica(x,y+1)){
                        return 1;
                    }
                    else if(heuristica(x+1,y)>heuristica(x,y+1)){
                        return 2;
                    }
                    else{
                    int randInt = rand.nextInt(2) + 1;
                    return randInt;
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

        void breadthSearch(Cell[][] cell){

            System.out.println("\n\n\n\n\n\n\n\n\nNow start the game!");
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
                cell[x][y].usableOperators = usable_operators(cell);
                System.out.println("Használható operátorok: ");
                for (int j = 0; j<4 ; j++)
                    System.out.println(cell[x][y].usableOperators[j]);




                if(cell[x][y].usableOperators[0]==1 && cell[x][y-1].visited==false ){  //fel

                    //System.out.println("fel");

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

                   // System.out.println("jobb");

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

                   // System.out.println("le");

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

                   // System.out.println("bal");

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

        int heuristica(int x, int y){
            return ( (numberOfCells-1) + (numberOfCells-1) ) - ( x + y);
        }
    }
}


