package dk.dtu.compute.se.pisd.roborally.fileaccess.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.compute.se.pisd.roborally.fileaccess.Adapter;
import dk.dtu.compute.se.pisd.roborally.model.*;

import java.net.http.HttpResponse;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;


public class Repository {

    private static Repository instance;

    private Repository(){

    }

    /***
     *
     * @return the singleton instance of Repository.
     */
    public static Repository getInstance() {
        if (instance == null) {
            // Create a new instance if it doesn't exist
            instance = new Repository();
        }
        return instance;
    }
    HTTPClient client = HTTPClient.getInstance();

    /**
     * save board to server with spec. name
     * @param board the board to be saved
     * @param name the name for the board
     */
    public void saveBoard(Board board, String name) {
        BoardTemplate template = new BoardTemplate(board, board.getSpaces(), board.getPlayers(), board.getCurrentPlayer());
        String path = "saveGame/" + name;
        client.makePostRequest(path, templateString(template));


    }

    private String templateString(BoardTemplate template){
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(SpaceAction.class, new Adapter<SpaceAction>())
                .disableHtmlEscaping()
                .setPrettyPrinting()
                ;
        Gson gson = simpleBuilder.create();

        return gson.toJson(template, template.getClass()/*, writer*/);
    }

    /**
     * gets a list of options from server for specified path
     * @param path for which list is gotten
     * @return an array of options
     * @throws Exception of an error occurs during request
     */
    public String[] getList(String path) throws Exception {
        HttpResponse<String> response = client.makeGetRequest("sendList/" + path);
        System.out.println(response.body());
        String[] arrayOfOptions = response.body().toString().split(",");
        for (String s : arrayOfOptions) {
            System.out.println(s);
        }

        return arrayOfOptions;
    }


    /**
     * Loads a board from the server with the specified board name.
     *
     * @param boardName  to be loaded.
     * @return The loaded  board.
     * @throws Exception If an error happens during the request.
     */
    public Board loadGameState(String boardName) throws Exception {
            Board board = reestablishBoard(boardName);
            return board;

    }
        private Board reestablishBoard(String boardName) throws Exception {
            System.out.println("existingBoard/" + boardName);
            HttpResponse<String> boardString = client.makeGetRequest("existingBoard/" + boardName);
            BoardTemplate template = returnBoardTemplate(boardString);
            Board board = new Board(template.width, template.height, template.spaceTemplates);
            setExistingPlayers(template, board);
            setExistingBoardState(board, template);
            return board;

        }

        public void setExistingPlayers(BoardTemplate template, Board board){
            for(PlayerTemplate p : template.playerTemplates){
                Player newPlayer = new Player(p.color, p.name);
                Space newPlayerSpace = board.getSpace(p.spaceTemplate.x, p.spaceTemplate.y);
                newPlayer.setSpace(newPlayerSpace, board);
                newPlayer.setTokenCount(p.tokenCount);
                newPlayer.setHeading(p.heading);
                newPlayer.setCards(p.getCommandCards());
                board.getPlayers().add(newPlayer);
                if(p.name.equals(template.currentTemplate.name))
                    board.setCurrentPlayer(newPlayer);
            }

        }

        private void setExistingBoardState(Board board, BoardTemplate template){
            board.setPhase(template.phase);
            board.setStep(template.step);
            board.setWinnerStatus(template.winnerIsFound);
            board.setStepMode(template.stepMode);
        }


    private BoardTemplate returnBoardTemplate(HttpResponse<String> response){
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(SpaceAction.class, new Adapter<SpaceAction>());
        Gson gson = simpleBuilder.create();
        BoardTemplate boardTemplate = gson.fromJson(response.body().toString(), BoardTemplate.class);
        return boardTemplate;

    }

    /**
     * Creates a new game with the specified number of players and board name.
     *
     * @param playerCount no. of players in the game.
     * @param boardName   name of board
     * @return The ID of the new game.
     * @throws Exception If an error occurs while the request.
     */
    public int newGameId(int playerCount, String boardName, boolean loadExisting) throws Exception {
        HttpResponse<String> response = client.makeGetRequest("createGame/" + playerCount + "/" + boardName + "/" + loadExisting);
        System.out.println("New get request:" + response.body());
        System.out.println("new/" + playerCount + "/" + boardName);
        return valueOf(response.body());
    }

    /**
     * Joins a game with the specific game ID.
     *
     * @param gameID .
     * @return A response showing status of the join request.
     * @throws Exception If an error happens during the request.
     */
    public String joinGameWithID(Integer gameID) throws Exception {
        HttpResponse<String> response = client.makeGetRequest("join/" + gameID);
        return response.body();

    }
    /**
     * gets a list of available games from the server.
     *
     * @return An array of available games.
     * @throws Exception If an error occurs during the request.
     */
    public String[] availableGamesList() throws Exception {
        HttpResponse<String> response = client.makeGetRequest("availableGames");
        System.out.println(response.body());
        String[] arrayOfOptions = response.body().toString().split(",");
        for(String s : arrayOfOptions){
            System.out.println(s);
        }

        return arrayOfOptions;
    }

    /**
     * Creates a new board for the specified game ID.
     *
     * @param gameId The ID for the created game.
     * @return The new board.
     * @throws Exception If an error occurs during the request.
     */
    public Board getNewBoard(String gameId) throws Exception {
        HttpResponse<String> boardString = client.makeGetRequest("newBoard/" + gameId);
        BoardTemplate template = returnBoardTemplate(boardString);
        Board board = new Board(template.width, template.height, template.spaceTemplates);
        return board;

    }

    /*public Board getExistingBoard(String gameId) throws Exception {
        HttpResponse<String> boardString = client.makeGetRequest("existingBoard/" + gameId);
        BoardTemplate template = returnBoardTemplate(boardString);
        Board board = new Board(template.width, template.height, template.spaceTemplates);
        setExistingBoardState(board, template);
        setExistingPlayers(template, board);
        return board;
    }*/
    /**
     * Checks if the game with given id is ready
     * @param gameId To check.
     * @return True if the game is ready and false if not.
     * @throws Exception If an error occurs during the request.
     */
    public boolean gameIsReady(int gameId) throws Exception {
        boolean gameIsReady;
        HttpResponse<String> response = client.makeGetRequest("gameFull/" + gameId);
        System.out.println("gameIsReady() in repository:" + response.body());
        if(response.body().equals("true"))
            gameIsReady = true;
        else
            gameIsReady = false;

        return gameIsReady;
    }
    /**
     * Gets the number of players within gameID.
     *
     * @param gameId of game where we want player count.
     * @return The number of players in the game.
     * @throws Exception If an error occurs during the request.
     */
    public int getPlayerCount(int gameId) throws Exception {
        HttpResponse<String> response = client.makeGetRequest("playerCount/" + gameId);
        System.out.println(Integer.valueOf(response.body()));
        return Integer.valueOf(response.body());
    }
    /**
     * Checks if the programming phase is done
     *
     * @param gameId gameid to check.
     * @return True if the programming phase is done and if nt then false.
     * @throws Exception If an error occurs during the request.
     */
    public boolean programmingPhaseIsDone(int gameId) throws Exception {
        boolean programmingPhaseIsDone;
        HttpResponse<String> response = client.makeGetRequest("programmingPhase/" + gameId);
        System.out.println("programmingPhaseIsDone() in repository:" + response.body());
        if (response.body().equals("true"))
            programmingPhaseIsDone = true;
        else
            programmingPhaseIsDone = false;

        return programmingPhaseIsDone;
    }
    /**
     * Checks if all players in the game are ready.
     *
     * @param gameId  game to check.
     * @return True if all players are ready and false if not.
     */
    public boolean areAllReady(int gameId) {
        boolean areAllReady;
        HttpResponse<String> response = null;
        try {
            response = client.makeGetRequest("allReady/" + gameId);
        } catch (Exception e) {
            return false;
        }
        System.out.println("areAllReady() in repository:" + response.body());
        if (response.body().equals("true"))
            areAllReady = true;
        else
            areAllReady = false;

        return areAllReady;
    }
    /**
     * Sets the game as ready to start.
     *
     * @param gameId game to set as ready.
     * @throws Exception If an error occurs during the request.
     */
    public void setReady(int gameId) throws Exception {
        boolean setReady;
        client.makePostRequest("programmingPhaseComplete/" + gameId, "");
    }

    /**
     * Gets the current turn.
     *
     * @param gameId of game to get the turn.
     * @return The current turn of the player in game.
     */
    public int getTurn(int gameId) {
        HttpResponse<String> response = null;
        try {
            response = client.makeGetRequest("getTurn/" + gameId);
        } catch (Exception e) {
            return 0;
        }
        return valueOf(response.body());
    }
    /**
     * Sets the game as executed.
     *
     * @param gameId The ID of the game to be set.
     */
    public void setExecuted(int gameId) {
        boolean setReady;
        client.makePostRequest("executed/" + gameId, "");
    }
    /**
     * Delete game with specific ID
     *
     * @param gameId of the game to delete.
     * @throws Exception If an error occurs during the request.
     */
    public void deleteGame(int gameId) throws Exception {
        client.makeDeleteRequest("deleteGame/" + String.valueOf(gameId));

    }

    public boolean getLoadExisting(String chosenGameId) throws Exception {
        HttpResponse<String> loadExisting = client.makeGetRequest("getLoadExisting/" + chosenGameId);
        if(loadExisting.body().equals("true"))
            return true;
        else
            return false;
    }
}