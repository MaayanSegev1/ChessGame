package mvc.ViewController;


import java.util.List;
import java.util.Map;

import mvc.Model.*;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The type View controller.
 */
public class ViewController extends Application {

    private Game game;
    private GridPane gridPaneBoard;
    private TilePane informationTilePane;
    private Text playerTurnText;
    private Text gameStateText;
    private Stage mainWindow;
    /**
     * If the part is selected, the next click will determine the destination
     * box for the selected part. Otherwise, he will have to select one.
     */

    private Point selectedPoint = null;
    private int selectedPieceIndex = -1;

    @Override
    public void start(Stage primaryStage) {
        mainWindow = primaryStage;
        // Game initialization
        game = new Game();
        // Grid initialization
        gridPaneBoard = new GridPane();

        //Observer adapting to board updates
        game.getBoard().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                updateGridPane();
                if (selectedPieceIndex != -1)
                    unselectPiece();
            }
        });

        //Observer looking at game information
        game.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                changePlayerTurn();
                if (game.getState() == "check") {
                    gameStateText.setText("Chess");
                    informationTilePane.getChildren().remove(1);
                    informationTilePane.getChildren().add(1, gameStateText);
                } else if (game.getState() == "normal") {
                    gameStateText.setText("");
                    informationTilePane.getChildren().remove(1);
                    informationTilePane.getChildren().add(1, gameStateText);
                } else {
                    showEndOfGameDialog();
                }
            }
        });

        updateGridPane();

        BorderPane border = new BorderPane();
        border.setCenter(gridPaneBoard);
        playerTurnText = new Text("The turn is for the white");
        playerTurnText.setFont(Font.font("Verdana", 20));
        informationTilePane = new TilePane();
        informationTilePane.getChildren().add(0, playerTurnText);
        informationTilePane.setVgap(30);
        informationTilePane.setPrefSize(250, 80);
        informationTilePane.setPadding(new Insets(10, 10, 10, 10));
        informationTilePane.setTileAlignment(Pos.TOP_CENTER);

        gameStateText = new Text("");
        gameStateText.setFont(Font.font("Verdana", 20));
        informationTilePane.getChildren().add(1, gameStateText);

        Text surrenderText = new Text("Exit");
        surrenderText.setFont(Font.font("Verdana", 15));
        HBox backgroundSurrenderHBox = new HBox();
        backgroundSurrenderHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                game.endGame(true);
            }
        });
        backgroundSurrenderHBox.setAlignment(Pos.CENTER);
        backgroundSurrenderHBox.setBackground(Background.EMPTY);
        backgroundSurrenderHBox.setStyle("-fx-background-color: white");
        backgroundSurrenderHBox.getChildren().add(surrenderText);
        informationTilePane.getChildren().add(2, backgroundSurrenderHBox);

        //javafx.scene.control.Button surrenderButton = new javafx.scene.control.Button();
        //information'sTilePane.getChildren().add(0, turnDisplay);

        border.setRight(informationTilePane);

        Scene scene = new Scene(border, Color.LIGHTBLUE);

        mainWindow.setTitle("Master Chess Game");
        mainWindow.getIcons().add(new Image(getClass().getResource("/resources/pieces/chessIcon.png").toString()));
        mainWindow.setScene(scene);
        mainWindow.show();
    }

    /**
     * Select piece.
     *
     * @param x the x
     * @param y the y
     */
    public void selectPiece(int x, int y) {
        Piece p = game.getBoard().getPiece(new Point(x, y));
        if (p != null && p.getOwner() == game.getActivePlayer()) {
            selectedPoint = new Point(x, y);
            selectedPieceIndex = x * 8 + y;
            SubScene box = (SubScene) gridPaneBoard.getChildren().get(selectedPieceIndex);
            box.setFill(Color.LIGHTBLUE);
            //Color of the squares where the piece can go
            changeColorOfPossibleDestinations(true, selectedPoint);
        }
    }

    /**
     * Unselect piece.
     */
    public void unselectPiece() {
        SubScene box = (SubScene) gridPaneBoard.getChildren().get(selectedPieceIndex);
        Point p = new Point(selectedPieceIndex / 8, selectedPieceIndex % 8);
        uncoloredBox(box, p);

        changeColorOfPossibleDestinations(false, p);
        selectedPoint = null;
        selectedPieceIndex = -1;
    }

    /**
     * Change color of possible destinations.
     *
     * @param toggle the toggle
     * @param p      the p
     */
    public void changeColorOfPossibleDestinations(boolean toggle, Point p) {
        SubScene boxDest;
        if (toggle == true) {
            int index;
            List<Map.Entry<Move, Boolean>> moves = game.getMoves(selectedPoint);
            Point destination;
            for (Map.Entry<Move, Boolean> m : moves) {
                destination = new Point(m.getKey().getDestination().getX(),
                        m.getKey().getDestination().getY());
                index = (destination.getX() * 8) + destination.getY();
                boxDest = (SubScene) gridPaneBoard.getChildren().get(index);
                if (m.getValue() == true)
                    boxDest.setFill(Color.LIGHTGREEN);
                else
                    boxDest.setFill(Color.LIGHTSALMON);
            }
        } else {
            for (int i = 0; i < 64; i++) {
                boxDest = (SubScene) gridPaneBoard.getChildren().get(i);
                uncoloredBox(boxDest, new Point(i / 8, i % 8));
            }
        }
    }

    /**
     * Update grid pane.
     */
    public void updateGridPane() {
        //Placement of parts and creation of their controllers
        int column = 0;
        int row = 0;
        Piece p;
        SubScene box;
        BorderPane boxContent;
        ImageView pieceImg;
        String imagePath;
        if (gridPaneBoard.getChildren().size() > 0) {
            gridPaneBoard.getChildren().clear();
        }
        for (int i = 0; i < 64; i++) {
            imagePath = "";
            gridPaneBoard.add(new SubScene(new BorderPane(), 50.0, 50.0),
                    column,
                    row);
            box = (SubScene) gridPaneBoard.getChildren().get(row * 8 + column);

            if ((column + row) % 2 == 0)
                box.setFill(Color.BEIGE);
            else
                box.setFill(Color.SIENNA);
            if (game.getBoard().getPiece(new Point(row, column)) != null) {
                p = game.getBoard().getPiece(new Point(row, column));
                //Color of the part
                if (p.getOwner().isWhite())
                    imagePath += "white_";
                else
                    imagePath += "black_";
                //Type of part
                switch (p.getClass().toString()) {
                    case "class mvc.Model.Pawn":
                        imagePath += "pawn";
                        break;
                    case "class mvc.Model.King":
                        imagePath += "king";
                        break;
                    case "class mvc.Model.Queen":
                        imagePath += "queen";
                        break;
                    case "class mvc.Model.Rook":
                        imagePath += "rook";
                        break;
                    case "class mvc.Model.Bishop":
                        imagePath += "bishop";
                        break;
                    case "class mvc.Model.Knight":
                        imagePath += "knight";
                        break;
                }
                boxContent = (BorderPane) box.getRoot();
                pieceImg = new ImageView();
                imagePath = "/resources/pieces/" + imagePath + ".png";
                pieceImg.setImage(new Image(getClass().getResource(
                        imagePath).toString()));
                pieceImg.setFitHeight(50);
                pieceImg.setFitWidth(50);
                boxContent.setCenter(pieceImg);
            } else {
                boxContent = (BorderPane) box.getRoot();
                pieceImg = new ImageView();
                boxContent.setCenter(pieceImg);
            }

            final int x = row;
            final int y = column;

            column++;
            if (column > 7) {
                column = 0;
                row++;
            }
            // Creation of a controller per cell
            box.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (selectedPoint == null) {
                        selectPiece(x, y);
                    } else {
                        /*
                           A part has been previously selected
                           If the new box selected is a destination
                           valid for the selected part, we move it to this
                           place. Otherwise, we deselect the part.
                         */
                        // Check the position of the cell
                        Point startPoint = new Point(selectedPieceIndex / 8, selectedPieceIndex % 8);
                        List<Map.Entry<Move, Boolean>> moves = game.getMoves(selectedPoint);
                        Point destination;
                        int index;
                        int indexMoves = 0;
                        Map.Entry<Move, Boolean> tempMove = null;
                        boolean isAPossibleMove = false;
                        if (moves.size() != 0) {
                            do {
                                tempMove = moves.get(indexMoves);
                                destination = new Point(tempMove.getKey().getDestination().getX(),
                                        tempMove.getKey().getDestination().getY());
                                index = destination.getX() * 8 + destination.getY();
                                if (x == destination.getX() && y == destination.getY()) {
                                    isAPossibleMove = true;
                                }
                                indexMoves++;
                            } while (indexMoves < moves.size() && !isAPossibleMove);
                        }
                        if (isAPossibleMove) {
                            /*
                              The square is a possible movement,
                              but we need to check that this does not cause a failure
                             */

                            if (tempMove.getValue() == true) {
                                Point destinationPoint = new Point(x, y);
                                Move move = new Move(startPoint, destinationPoint,
                                        Move.Direction.NONE);
                                game.getBoard().movePiece(move, false);
                                game.nextPlayerTurn();
                            }
                        } else {
                            //Deselect the part
                            unselectPiece();
                        }
                    }
                }
            });
        }
    }

    /**
     * Uncolored box.
     *
     * @param box the box
     * @param p   the p
     */
    public void uncoloredBox(SubScene box, Point p) {
        if ((p.getX() + p.getY()) % 2 == 0)
            box.setFill(Color.BEIGE);
        else
            box.setFill(Color.SIENNA);
    }


    /**
     * Change player turn.
     */
    public void changePlayerTurn() {
        String currentPlayer;
        if (game.getActivePlayer().isWhite())
            currentPlayer = "white";
        else
            currentPlayer = "black  ";
        playerTurnText.setText("The turn is for the " + currentPlayer);
        informationTilePane.getChildren().remove(0);
        informationTilePane.getChildren().add(0, playerTurnText);
    }

    /**
     * Show end of game dialog.
     */
    public void showEndOfGameDialog() {
        if (game.getState() == "checkmate")
            gameStateText.setText("Checkmate");
        else
            gameStateText.setText("Leaving");
        informationTilePane.getChildren().remove(1);
        informationTilePane.getChildren().add(1, gameStateText);
        final Stage dialog = new Stage(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(mainWindow);
        TilePane dialogPane = new TilePane();
        dialogPane.setVgap(30);
        dialogPane.setPrefSize(250, 80);
        dialogPane.setPadding(new Insets(10, 10, 10, 10));
        dialogPane.setTileAlignment(Pos.TOP_CENTER);
        //Announcement of the end of the game
        String result = new String("The player ");
        if (game.getActivePlayer().isWhite())
            result += "White";
        else
            result += "Black";
        result += " a ";
        if (game.getState() == "checkmate")
            result += "lost";
        else
            result += "withdrew";
        result += ". What do you want to do ?";
        Text informationText = new Text(result);
        Text restartGameText = new Text("Replay");
        Text quitGameText = new Text("Leave the game");
        HBox backgroundRestartHBox = new HBox();
        backgroundRestartHBox.setAlignment(Pos.CENTER);
        backgroundRestartHBox.setStyle("-fx-background-color: white");
        backgroundRestartHBox.getChildren().add(restartGameText);
        backgroundRestartHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                game.restartGame();
                gameStateText.setText("");
                informationTilePane.getChildren().remove(1);
                informationTilePane.getChildren().add(1, gameStateText);
                dialog.close();
            }
        });
        HBox backgroundQuitHBox = new HBox();
        backgroundQuitHBox.setAlignment(Pos.CENTER);
        backgroundQuitHBox.setStyle("-fx-background-color: white");
        backgroundQuitHBox.getChildren().add(quitGameText);
        backgroundQuitHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dialog.close();
                mainWindow.close();
            }
        });
        dialogPane.getChildren().add(informationText);
        dialogPane.getChildren().add(backgroundRestartHBox);
        dialogPane.getChildren().add(backgroundQuitHBox);
        Scene scene = new Scene(dialogPane);
        scene.setFill(Color.LIGHTGREY);
        dialog.setScene(scene);
        dialog.setTitle("Master Chess Game");
        dialog.showAndWait();
    }

    /**
     * The entry point of application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
