package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
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
            if(finished==false) {
                RandomOperatorTry(cell);
            }
        }

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












        void RandomOperatorTry(Cell[][] cell){
            System.out.println("\n\n\n\n\n\n\n\n\nNow start the game!");
            x=0;
            y=0;
            int counter=0;






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








         int[] usable_operators(Cell cell[][]) {


            int[] usable = {1, 1, 1, 1};//up,right,down,left

             for (int i=0; i<4;i++){

                 if (cell[x][y].usedOperators[i]==1){       //van-e már használt operátora, ha igen akkor kivesszük
                     usable[i]=0;
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

                //System.out.println(randInt);
                //System.out.println(placeCounter);

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


    }
}



