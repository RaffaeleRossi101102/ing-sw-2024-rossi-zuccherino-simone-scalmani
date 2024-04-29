package SoftEng_2024.Model;


import SoftEng_2024.Model.Enums.Angles;

import java.util.List;

public class StepGoalCard extends GoalCard{
    private final Angles baseResource;
    private final Angles sideResource;
    private final boolean baseTop;
    private final boolean sideLeft;

    public StepGoalCard(Angles baseResource, Angles sideResource, boolean baseTop, boolean sideLeft, int points, String goalType) {
        super(points, goalType);
        this.baseResource = baseResource;
        this.sideResource = sideResource;
        this.baseTop = baseTop;
        this.sideLeft = sideLeft;
    }

    @Override
    public int calcScore(Board playerBoard){
        int res = 0;
        Cell starterCell;
        Cell[][] localBoard = playerBoard.getCardBoard();
        List<Cell> cellArrayList = playerBoard.getCardList();

        if (!baseTop){
            if (sideLeft){
                //vado nella lista e scorro finché non becco una carta del colore della base
                for (Cell cell : cellArrayList){
                    int counterSide = 0;
                    int counterPattern = 0;
                    //se non è visitata, è del colore giusto
                    if (cell.getCard().getResources()[4].equals(baseResource)
                            && !cell.getVisited()
                            && cell.getRow() > 2
                            && cell.getColumn() > 0

                    ){
                        //metto la base dentro starterCell
                        starterCell = cell;
                        starterCell.setVisited(true);
                        //devo scorrere in basso e contare il numero di carte viola
                        while(  //finché ho carte blu
                                starterCell.getRow()<= localBoard.length-3
                                        //se sotto alla base c'è una carta
                                        && localBoard[starterCell.getRow()+2][starterCell.getColumn()].getCard()!=null
                                        //che è del colore giusto (base)
                                        && localBoard[starterCell.getRow()+2][starterCell.getColumn()].getCard().getResources()[4].equals(baseResource)
                                        //e non é visitata
                                        && !localBoard[starterCell.getRow()+2][starterCell.getColumn()].getVisited()
                                        //e in basso a sinistra c'è una carta
                                        && localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].getCard()!=null
                                        //del colore giusto (side)
                                        && localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].getCard().getResources()[4].equals(sideResource)
                                        //e la prima carta side non è visitata
                                        && !localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].getVisited()
                        )
                        {
                            //setto il lato come visitato
                            localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].setVisited(true);
                            //aumento il counter delle carte del lato
                            counterSide ++;
                            //scorro in basso alla carta della base successiva
                            starterCell = localBoard[starterCell.getRow() + 2][starterCell.getColumn()];
                            //setto la nuova base a visitata
                            starterCell.setVisited(true);
                        }
                        //riparto dalla carta della lista
                        starterCell = cell;
                        while(
                            //finché ho carte in alto
                                starterCell.getRow() - 2 >= 0
                                        //se sopra alla base c'è una carta
                                        && localBoard[starterCell.getRow()-2][starterCell.getColumn()].getCard()!=null
                                        //che è del colore giusto (base)
                                        && localBoard[starterCell.getRow()-2][starterCell.getColumn()].getCard().getResources()[4].equals(baseResource)
                                        //e non é visitata
                                        && !localBoard[starterCell.getRow()-2][starterCell.getColumn()].getVisited()
                                        //e in alto a sinistra c'è una carta
                                        && localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].getCard()!=null
                                        //del colore giusto (side)
                                        && localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].getCard().getResources()[4].equals(sideResource)
                                        //e la prima carta side non è visitata
                                        && !localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].getVisited()
                        )
                        //finché salgo
                        {
                            //setto il lato come visitato
                            localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].setVisited(true);
                            //aumento il counter delle carte del lato
                            counterSide ++;
                            //scorro alla carta della base successiva
                            starterCell = localBoard[starterCell.getRow() - 2][starterCell.getColumn()];
                            //setto la nuova base a visitata
                            starterCell.setVisited(true);
                        }
                        //sono arrivato alla carta base più in alto che c'é e verifico se é presente il pattern
                        //altrimenti aggiungo comunque la carta side al conteggio
                        if (verifySide(localBoard,localBoard[starterCell.getRow()-1][starterCell.getColumn()-1],false) == 1)
                        {
                            counterPattern++;
                        }
                        else if (verifySide(localBoard,localBoard[starterCell.getRow()-1][starterCell.getColumn()-1],false) == 0
                                && localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].getCard()!=null
                                && localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].getCard().getResources()[4].equals(sideResource))
                        {
                            counterSide++;
                        }
                    }else if (cell.getCard().getResources()[4].equals(baseResource)
                            && !cell.getVisited()
                            && cell.getRow() <= 2
                            && cell.getColumn() <= 0){
                        cell.setVisited(true);
                    }
                    res += (int)(counterSide/2)+counterPattern;

                }
            }else if (!sideLeft){
                //vado nella lista e scorro finché non becco una carta del colore della base
                for (Cell cell : cellArrayList){
                    int counterSide = 0;
                    int counterPattern = 0;
                    //se non è visitata, è del colore giusto
                    if (cell.getCard().getResources()[4].equals(baseResource)
                            && !cell.getVisited()
                            && cell.getRow() > 2
                            && cell.getColumn() < localBoard[0].length

                    ){
                        //metto la base dentro starterCell
                        starterCell = cell;
                        starterCell.setVisited(true);
                        //devo scorrere in basso e contare il numero di carte viola
                        while(  //finché ho carte blu
                                starterCell.getRow()<= localBoard.length-3
                                        //se sotto alla base c'è una carta
                                        && localBoard[starterCell.getRow()+2][starterCell.getColumn()].getCard()!=null
                                        //che è del colore giusto (base)
                                        && localBoard[starterCell.getRow()+2][starterCell.getColumn()].getCard().getResources()[4].equals(baseResource)
                                        //e non é visitata
                                        && !localBoard[starterCell.getRow()+2][starterCell.getColumn()].getVisited()
                                        //e in basso a destra c'è una carta
                                        && localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].getCard()!=null
                                        //del colore giusto (side)
                                        && localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].getCard().getResources()[4].equals(sideResource)
                                        //e la prima carta side non è visitata
                                        && !localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].getVisited()
                        )
                        {
                            //setto il lato come visitato
                            localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].setVisited(true);
                            //aumento il counter delle carte del lato
                            counterSide ++;
                            //scorro alla carta della base successiva
                            starterCell = localBoard[starterCell.getRow() + 2][starterCell.getColumn()];
                            //setto la nuova base a visitata
                            starterCell.setVisited(true);
                        }
                        //riparto dalla carta della lista
                        starterCell = cell;
                        while(
                            //finché ho carte in alto
                                starterCell.getRow() - 2 >= 0
                                        //se sopra alla base c'è una carta
                                        && localBoard[starterCell.getRow()-2][starterCell.getColumn()].getCard()!=null
                                        //che è del colore giusto (base)
                                        && localBoard[starterCell.getRow()-2][starterCell.getColumn()].getCard().getResources()[4].equals(baseResource)
                                        //e non é visitata
                                        && !localBoard[starterCell.getRow()-2][starterCell.getColumn()].getVisited()
                                        //e in alto a sinistra c'è una carta
                                        && localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].getCard()!=null
                                        //del colore giusto (side)
                                        && localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].getCard().getResources()[4].equals(sideResource)
                                        //e la prima carta side non è visitata
                                        && !localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].getVisited()
                        )
                        //finché salgo
                        {
                            //setto il lato come visitato
                            localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].setVisited(true);
                            //aumento il counter delle carte del lato
                            counterSide ++;
                            //scorro in alto alla carta della base successiva
                            starterCell = localBoard[starterCell.getRow() - 2][starterCell.getColumn()];
                            //setto la nuova base a visitata
                            starterCell.setVisited(true);
                        }
                        //sono arrivato alla carta base più in alto che c'é e verifico se é presente il pattern
                        //altrimenti aggiungo comunque la carta side al conteggio
                        if (verifySide(localBoard,localBoard[starterCell.getRow()-1][starterCell.getColumn()+1],false) == 1)
                        {
                            counterPattern++;
                        }
                        else if (verifySide(localBoard,localBoard[starterCell.getRow()-1][starterCell.getColumn()+1],false) == 0
                                && localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].getCard()!=null
                                && localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].getCard().getResources()[4].equals(sideResource))
                        {
                            counterSide++;
                        }
                    }else if (cell.getCard().getResources()[4].equals(baseResource)
                            && !cell.getVisited()
                            && cell.getRow() <= 2
                            && cell.getColumn() <= 0){
                        cell.setVisited(true);
                    }
                    res += (int)(counterSide/2)+counterPattern;

                }
            }
        }else if (baseTop){
            if (sideLeft){
                //vado nella lista e scorro finché non becco una carta del colore della base
                for (Cell cell : cellArrayList){
                    int counterSide = 0;
                    int counterPattern = 0;
                    //se non è visitata, è del colore giusto
                    if (cell.getCard().getResources()[4].equals(baseResource)
                            && !cell.getVisited()
                            && cell.getRow() < localBoard.length-3
                            && cell.getColumn() > 0
                    ){
                        //metto la base dentro starterCell
                        starterCell = cell;
                        starterCell.setVisited(true);
                        //System.out.println("trovato possibile candidato");
                        //devo scorrere in basso e contare il numero di carte side
                        while(  //finché ho carte base
                                starterCell.getRow() <= localBoard.length-6
                                        //se sotto alla base c'è una carta
                                        && localBoard[starterCell.getRow()+2][starterCell.getColumn()].getCard()!=null
                                        //che è del colore giusto (base)
                                        && localBoard[starterCell.getRow()+2][starterCell.getColumn()].getCard().getResources()[4].equals(baseResource)
                                        //e non é visitata
                                        && !localBoard[starterCell.getRow()+2][starterCell.getColumn()].getVisited()
                                        //e in basso a sinistra c'è una carta che é la prima del side
                                        && localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].getCard()!=null
                                        //del colore giusto (side)
                                        && localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].getCard().getResources()[4].equals(sideResource)
                                        //e non è visitata
                                        && !localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].getVisited()
                        )
                        {
                            //setto il lato come visitato
                            localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].setVisited(true);
                            //aumento il counter delle carte del lato
                            counterSide ++;
                            // System.out.println("Il numero di carte laterali è: "+ counterSide);
                            //scorro alla carta della base successiva
                            starterCell = localBoard[starterCell.getRow() + 2][starterCell.getColumn()];
                            //setto la nuova base a visitata
                            starterCell.setVisited(true);
                        }
                        //sono arrivato alla carta base più in basso che c'é e verifico se é presente il pattern
                        //altrimenti aggiungo comunque la carta side al conteggio
                        if (verifySide(localBoard,localBoard[starterCell.getRow()+1][starterCell.getColumn()-1],true) == 1)
                        {
                            counterPattern++;
                        }
                        else if (verifySide(localBoard,localBoard[starterCell.getRow()+1][starterCell.getColumn()-1],true) == 0
                                && localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].getCard()!=null
                                && localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].getCard().getResources()[4].equals(sideResource))
                        {
                            counterSide++;
                        }
                        //riparto dalla carta della lista
                        starterCell = cell;
                        while(
                            //finché ho carte in alto
                                starterCell.getRow() - 2 >= 0
                                        //se sopra alla base c'è una carta
                                        && localBoard[starterCell.getRow()-2][starterCell.getColumn()].getCard()!=null
                                        //che è del colore giusto (base)
                                        && localBoard[starterCell.getRow()-2][starterCell.getColumn()].getCard().getResources()[4].equals(baseResource)
                                        //e non é visitata
                                        && !localBoard[starterCell.getRow()-2][starterCell.getColumn()].getVisited()
                                        //e in alto a sinistra c'è una carta che é la prima del side
                                        && localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].getCard()!=null
                                        //del colore giusto (side)
                                        && localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].getCard().getResources()[4].equals(sideResource)
                                        //e non è visitata
                                        && !localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].getVisited()
                        )
                        //finché salgo
                        {
                            //setto il lato come visitato
                            localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].setVisited(true);
                            //aumento il counter delle carte del lato
                            counterSide ++;
                            //scorro alla carta della base successiva
                            starterCell = localBoard[starterCell.getRow() - 2][starterCell.getColumn()];
                            //setto la nuova base a visitata
                            starterCell.setVisited(true);
                        }
                    }else if (cell.getCard().getResources()[4].equals(baseResource)
                            && !cell.getVisited()
                            && cell.getRow() >= localBoard.length-3
                            && cell.getColumn() == 0){
                        cell.setVisited(true);
                    }
                    res += (int)(counterSide/2)+counterPattern;
                }
            }else if (!sideLeft){
                //vado nella lista e scorro finché non becco una carta del colore della base
                for (Cell cell : cellArrayList){
                    int counterSide = 0;
                    int counterPattern = 0;
                    //se non è visitata, è del colore giusto
                    if (cell.getCard().getResources()[4].equals(baseResource)
                            && !cell.getVisited()
                            && cell.getRow() < localBoard.length-3
                            && cell.getColumn() < localBoard[0].length

                    ){
                        //metto la base dentro starterCell
                        starterCell = cell;
                        starterCell.setVisited(true);
                        //devo scorrere in basso e contare il numero di carte dei side
                        while(  //finché ho carte della base
                                starterCell.getRow()<= localBoard.length-6
                                        //se sotto alla base c'è una carta
                                        && localBoard[starterCell.getRow()+2][starterCell.getColumn()].getCard()!=null
                                        //che è del colore giusto (base)
                                        && localBoard[starterCell.getRow()+2][starterCell.getColumn()].getCard().getResources()[4].equals(baseResource)
                                        //e non é visitata
                                        && !localBoard[starterCell.getRow()+2][starterCell.getColumn()].getVisited()
                                        //e in basso a destra c'è una carta che é la prima del side
                                        && localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].getCard()!=null
                                        //del colore giusto (side)
                                        && localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].getCard().getResources()[4].equals(sideResource)
                                        //e non è visitata
                                        && !localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].getVisited()
                        )
                        {

                            //setto il lato come visitato
                            localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].setVisited(true);
                            //aumento il counter delle carte del lato
                            counterSide ++;
                            //scorro alla carta della base successiva
                            starterCell = localBoard[starterCell.getRow() + 2][starterCell.getColumn()];
                            //setto la nuova base a visitata
                            starterCell.setVisited(true);
                        }
                        //sono arrivato alla carta base più in basso che c'é e verifico se é presente il pattern
                        //altrimenti aggiungo comunque la carta side al conteggio
                        if (verifySide(localBoard,localBoard[starterCell.getRow()+1][starterCell.getColumn()+1],true) == 1)
                        {
                            counterPattern++;
                        }
                        else if (verifySide(localBoard,localBoard[starterCell.getRow()+1][starterCell.getColumn()+1],true) == 0
                                && localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].getCard()!=null
                                && localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].getCard().getResources()[4].equals(sideResource))
                        {
                            counterSide++;
                        }
                        //riparto dalla carta della lista
                        starterCell = cell;
                        while(
                            //finché ho carte in alto
                                starterCell.getRow() - 2 >= 0
                                        //se sopra alla base c'è una carta
                                        && localBoard[starterCell.getRow()-2][starterCell.getColumn()].getCard()!=null
                                        //che è del colore giusto (base)
                                        && localBoard[starterCell.getRow()-2][starterCell.getColumn()].getCard().getResources()[4].equals(baseResource)
                                        //e non é visitata
                                        && !localBoard[starterCell.getRow()-2][starterCell.getColumn()].getVisited()
                                        //e in alto a sinistra c'è una carta che é la prima del side
                                        && localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].getCard()!=null
                                        //del colore giusto (side)
                                        && localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].getCard().getResources()[4].equals(sideResource)
                                        //e non è visitata
                                        && !localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].getVisited()
                        )
                        //finché salgo
                        {
                            //setto il lato come visitato
                            localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].setVisited(true);
                            //aumento il counter delle carte del lato
                            counterSide ++;
                            //scorro in alto alla carta della base successiva
                            starterCell = localBoard[starterCell.getRow() - 2][starterCell.getColumn()];
                            //setto la nuova base a visitata
                            starterCell.setVisited(true);
                        }
                    }else if (cell.getCard().getResources()[4].equals(baseResource)
                            && !cell.getVisited()
                            && cell.getRow() >= localBoard.length-3
                            && cell.getColumn() == localBoard[0].length)
                    {
                        cell.setVisited(true);
                    }
                    res += (int)(counterSide/2)+counterPattern;
                }
            }
        }
        //Scorro la lista e setto nuovamente a visited=false tutte le celle della matrice
        for(Cell cella : cellArrayList) cella.setVisited(false);
        return res*this.getPoints();
    }
    private int verifySide(Cell[][] localBoard,Cell starterCell,boolean down){
        //c'è una carta in quella sotto e non è visitata ed è quella giusta
        if(!starterCell.getVisited()
                && starterCell.getCard()!=null
                && starterCell.getCard().getResources()[4].equals(sideResource)) {
            //se devo scendere
            if (down) {
                //se sotto c'è spazio
                if (starterCell.getRow() + 2 < localBoard.length
                        //e sotto c'è una carta
                        && localBoard[starterCell.getRow() + 2][starterCell.getColumn()].getCard() != null
                        //che non è stata visitata
                        && !localBoard[starterCell.getRow() + 2][starterCell.getColumn()].getVisited()
                        //ed è corretta
                        && localBoard[starterCell.getRow() + 2][starterCell.getColumn()].getCard().getResources()[4].equals(sideResource)) {
                    //settala come visitata insieme a quella sotto
                    starterCell.setVisited(true);
                    localBoard[starterCell.getRow() + 2][starterCell.getColumn()].setVisited(true);
                    return 1;
                }
            } else {
                //se devo scorrere verso l'alto
                // sono dentro la matrice
                if (starterCell.getRow() - 2 >= 0
                        //e sotto c'è una carta
                        && localBoard[starterCell.getRow() - 2][starterCell.getColumn()].getCard() != null
                        //che non è visitata
                        && !localBoard[starterCell.getRow() - 2][starterCell.getColumn()].getVisited()
                        //ed è quella giusta
                        && localBoard[starterCell.getRow() - 2][starterCell.getColumn()].getCard().getResources()[4].equals(sideResource) )
                {
                    starterCell.setVisited(true);
                    localBoard[starterCell.getRow() - 2][starterCell.getColumn()].setVisited(true);
                    return 1;
                }
            }
        }
        return 0;
    }
}
