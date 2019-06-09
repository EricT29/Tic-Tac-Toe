package com.example.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // setup the game variables
    private boolean playerTurn = true;
    private boolean compTurn = false;
    private boolean gameOver;
    private boolean newGameStarted;
    private Button newGameButton;
    private Button playAsX;
    private Button playAsO;
    private Button gameGrid[][] = new Button[3][3];
    Random random = new Random();

    String compSymbol;
    String playerSymbol;

    // this class represents moves taken by each player

    class Move {
        private int score;
        private int[] coordinates;

        public Move() {
            this.score = 0;
            this.coordinates = new int[]{0, 0};
        }

        public Move(int[] coordinates) {
            this.score = 0;
            this.coordinates = coordinates;
        }

        public int getScore() {
            return this.score;
        }

        public void setScore(int newScore) {
            this.score = newScore;
        }

        public int[] getCoordinates() {
            return this.coordinates;
        }

        public void setCoordinates(int[] newCoordinates) {
            this.coordinates = newCoordinates;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // randomly choose which player gets which symbol
        if (Math.random() > 0.5) {
            playerSymbol = "X";
            compSymbol = "O";
        } else {
            compSymbol = "X";
            playerSymbol = "O";
        }

        playAsX = (Button) findViewById(R.id.play_as_X);
        playAsO = (Button) findViewById(R.id.play_as_O);

        playAsX.setOnClickListener(this);
        playAsO.setOnClickListener(this);

        newGameButton = (Button) findViewById(R.id.NewGame);

        newGameButton.setOnClickListener(this);

        // get references to widgets
        gameGrid[0][0] = (Button) findViewById(R.id.button1);
        gameGrid[0][1] = (Button) findViewById(R.id.button2);
        gameGrid[0][2] = (Button) findViewById(R.id.button3);
        gameGrid[1][0] = (Button) findViewById(R.id.button4);
        gameGrid[1][1] = (Button) findViewById(R.id.button5);
        gameGrid[1][2] = (Button) findViewById(R.id.button6);
        gameGrid[2][0] = (Button) findViewById(R.id.button7);
        gameGrid[2][1] = (Button) findViewById(R.id.button8);
        gameGrid[2][2] = (Button) findViewById(R.id.button9);

        // set each button to listen for clicks
        for (int x = 0; x < gameGrid.length; x++) {
            for (int y = 0; y < gameGrid[x].length; y++) {
                gameGrid[x][y].setOnClickListener(this);
            }
        }

        // set each button to listen for text changed
        for (int x = 0; x < gameGrid.length; x++) {
            for (int y = 0; y < gameGrid[x].length; y++) {
                final Button button = gameGrid[x][y];
                button.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    // if text has been changed and the board is not being setup:
                    // check to see if a there has been a win or a tie
                    // if there has not been then change whose turn it is
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if (!newGameStarted) {
                            checkBoard();
                            if (!gameOver) {
                                if (playerTurn) {
                                    compTurn = true;
                                    playerTurn = false;
                                    computerTurn();
                                } else {
                                    playerTurn = true;
                                    compTurn = false;
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    // declare which player has won
    public void declareWinner() {
        gameOver = true;
        String winningPlayer;
        if (playerTurn) {
            winningPlayer = "The Player";
        } else {
            winningPlayer = "The Computer";
        }
        Toast.makeText(getBaseContext(), winningPlayer + " has won the game!",
                Toast.LENGTH_LONG).show();
    }

    // declare a tie between the two players
    public void declareTie() {
        gameOver = true;
        Toast.makeText(getBaseContext(), "It's a Tie! ",
                Toast.LENGTH_LONG).show();
    }

    private void checkBoard() {
        // concatenate the board into 8 strings to each be checked for a win
        String row1 = gameGrid[0][0].getText().toString() + gameGrid[0][1].getText().toString() + gameGrid[0][2].getText().toString();
        String row2 = gameGrid[1][0].getText().toString() + gameGrid[1][1].getText().toString() + gameGrid[1][2].getText().toString();
        String row3 = gameGrid[2][0].getText().toString() + gameGrid[2][1].getText().toString() + gameGrid[2][2].getText().toString();
        String col1 = gameGrid[0][0].getText().toString() + gameGrid[1][0].getText().toString() + gameGrid[2][0].getText().toString();
        String col2 = gameGrid[0][1].getText().toString() + gameGrid[1][1].getText().toString() + gameGrid[2][1].getText().toString();
        String col3 = gameGrid[0][2].getText().toString() + gameGrid[1][2].getText().toString() + gameGrid[2][2].getText().toString();
        String diag1 = gameGrid[0][0].getText().toString() + gameGrid[1][1].getText().toString() + gameGrid[2][2].getText().toString();
        String diag2 = gameGrid[0][2].getText().toString() + gameGrid[1][1].getText().toString() + gameGrid[2][0].getText().toString();

        // check for a winning row column and diagonal for both players. If either has won declare a winner
        if (threeInARow(row1, compSymbol) | threeInARow(row2, compSymbol) | threeInARow(row3, compSymbol) | threeInARow(col1, compSymbol) | threeInARow(col2, compSymbol) | threeInARow(col3, compSymbol) | threeInARow(diag1, compSymbol) | threeInARow(diag2, compSymbol) | threeInARow(row1, playerSymbol) | threeInARow(row2, playerSymbol) | threeInARow(row3, playerSymbol) | threeInARow(col1, playerSymbol) | threeInARow(col2, playerSymbol) | threeInARow(col3, playerSymbol) | threeInARow(diag1, playerSymbol) | threeInARow(diag2, playerSymbol)) {
            declareWinner();
        }

        // check for remaining legal moves
        int moveCount = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (!gameGrid[x][y].getText().toString().isEmpty()) {
                    moveCount++;
                }
            }
        }

        // if there are no legal moves left declare a tie
        if (moveCount == 9) {
            declareTie();
        }
    }

    // check for three of the same symbols in a row
    public boolean threeInARow(String line, String symbol) {
        int matches = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == symbol.charAt(0)) {
                matches++;
            }
        }
        return matches == 3;
    }

    // process the computer's turn
    public void computerTurn() {
        String[][] initBoard = new String[3][3];

        // get a copy of the current board
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                initBoard[x][y] = gameGrid[x][y].getText().toString();
            }
        }

        // get the best move for the current board
        Move move = getBestMove(compSymbol, initBoard);
        // apply that move to the board
        gameGrid[move.getCoordinates()[0]][move.getCoordinates()[1]].setText(compSymbol);
    }

    // use minimax to get the best move
    public Move getBestMove(String symbol, String[][] board) {
        String otherSymbol = null;
        String[][] brainBoard = new String[3][3];
        ArrayList<int[]> availableMoves = new ArrayList<>();
        ArrayList<Move> bestMoves = new ArrayList<>();
        ArrayList<Move> neutralMoves = new ArrayList<>();
        ArrayList<Move> losingMoves = new ArrayList<>();
        Move move;

        // check available moves
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                brainBoard[x][y] = board[x][y];
                if (brainBoard[x][y].isEmpty()) {
                    availableMoves.add(new int[]{x, y});
                }
            }
        }

        // check one full move tree
        Iterator it = availableMoves.iterator();
        while (it.hasNext()) {
            move = new Move((int[]) it.next());

            // copy the last board to a new board
            String[][] newBoard = new String[3][3];
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    newBoard[x][y] = brainBoard[x][y];
                }
            }

            // apply the move to the board
            newBoard[move.coordinates[0]][move.coordinates[1]] = symbol;

            // concatenate the board into 8 strings to each be checked for a win
            String row1 = newBoard[0][0] + newBoard[0][1] + newBoard[0][2];
            String row2 = newBoard[1][0] + newBoard[1][1] + newBoard[1][2];
            String row3 = newBoard[2][0] + newBoard[2][1] + newBoard[2][2];
            String col1 = newBoard[0][0] + newBoard[1][0] + newBoard[2][0];
            String col2 = newBoard[0][1] + newBoard[1][1] + newBoard[2][1];
            String col3 = newBoard[0][2] + newBoard[1][2] + newBoard[2][2];
            String diag1 = newBoard[0][0] + newBoard[1][1] + newBoard[2][2];
            String diag2 = newBoard[0][2] + newBoard[1][1] + newBoard[2][0];

            // check for a winning row column and diagonal
            if (threeInARow(row1, symbol) | threeInARow(row2, symbol) | threeInARow(row3, symbol) | threeInARow(col1, symbol) | threeInARow(col2, symbol) | threeInARow(col3, symbol) | threeInARow(diag1, symbol) | threeInARow(diag2, symbol)) {
                move.setScore(1);
                return move;
            }

            // if the current symbol is X then the opposing player's symbol is O, otherwise it's X
            if (symbol.equals("X")) {
                otherSymbol = "O";
            } else {
                otherSymbol = "X";
            }

            // if the other player will lose next turn: return 1 and add current move to the best moves list
            // if the other player will neither lose nor win next turn: return 0 and add current move to the neutral moves list
            // if the other player will win next turn: return -1 and add current mvoe to the losing moves list Eric M. Thompson
            int otherScore = getBestMove(otherSymbol, newBoard).getScore();
            if (otherScore == -1) {
                move.setScore(1);
                bestMoves.add(move);
            } else if (otherScore == 0) {
                move.setScore(0);
                neutralMoves.add(move);
            } else if (otherScore == 1) {
                move.setScore(-1);
                losingMoves.add(move);
            }

        }

        // randomly pick best moves or neutral moves or losing moves
        // if there's no legal moves return a placeholder move
        if (!bestMoves.isEmpty()) {
            return bestMoves.get(random.nextInt(bestMoves.size()));
        } else if (!neutralMoves.isEmpty()) {
            return neutralMoves.get(random.nextInt(neutralMoves.size()));
        } else if (!losingMoves.isEmpty()) {
            return losingMoves.get(random.nextInt(losingMoves.size()));
        } else {
            return new Move();
        }
    }

    // clear the board and setup a new game
    public void newGame() {
        newGameStarted = true;
        playerTurn = true;
        compTurn = false;

        for (int x = 0; x < gameGrid.length; x++) {
            for (int y = 0; y < gameGrid[x].length; y++) {
                gameGrid[x][y].setText("");
            }
        }

        newGameStarted = false;
        gameOver = false;
    }

    // play as x
    public void chooseX() {
        if (!compTurn) {
            playerSymbol = "X";
            compSymbol = "O";
        }
    }

    // play as o
    public void chooseO() {
        if (!compTurn) {
            playerSymbol = "O";
            compSymbol = "X";
        }
    }

    // Set Text of button to player's symbol when clicked
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.NewGame:
                newGame();
                break;
            case R.id.play_as_X:
                chooseX();
                break;
            case R.id.play_as_O:
                chooseO();
                break;
            default:
                Button b = (Button) v;
                // if it's not the computer's turn and the game isn't over and the chosen button is empty:
                // put down the player's symbol
                if (!compTurn && !gameOver && b.getText().toString().isEmpty()) {
                    b.setText(playerSymbol);
                }
        }
    }
}
