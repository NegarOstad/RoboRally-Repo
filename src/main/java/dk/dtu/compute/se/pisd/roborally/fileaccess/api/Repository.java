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

    public static Repository getInstance() {
        if (instance == null) {
            // Create a new instance if it doesn't exist
            instance = new Repository();
        }
        return instance;
    }
    HTTPClient client = HTTPClient.getInstance();
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
    public String[] getList(String path) throws Exception {
      HttpResponse<String> response = client.makeGetRequest("sendList/"+path);
        System.out.println(response.body());
        String[] arrayOfOptions = response.body().toString().split(",");
        for(String s : arrayOfOptions){
            System.out.println(s);
        }

        return arrayOfOptions;
    }


    public Board loadGame(String boardName) throws Exception {
            Board board = reestablishBoard(boardName);
            return board;

    }
        private Board reestablishBoard(String boardName) throws Exception {
        HttpResponse<String> boardString = client.makeGetRequest("existingBoard/" + boardName);
        BoardTemplate template = returnBoardTemplate(boardString);
        Board board = new Board(template.width, template.height, template.spaceTemplates);
        setExistingPlayers(template, board);
        setExistingBoardState(board, template);
        return board;

        }

        private void setExistingPlayers(BoardTemplate template, Board board){
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

    public int newGameId(int playerCount, String boardName) throws Exception {
        HttpResponse<String> response = client.makeGetRequest("createGame/" + playerCount + "/" + boardName);
        System.out.println("New get request:" + response.body());
        System.out.println("new/" + playerCount + "/" + boardName);
        return valueOf(response.body());
    }


    public String joinGameWithID(Integer gameID) throws Exception {
    HttpResponse<String> response = client.makeGetRequest("join/" + gameID);
        return response.body();

    }
    public String[] availableGamesList() throws Exception {
        HttpResponse<String> response = client.makeGetRequest("availableGames");
        System.out.println(response.body());
        String[] arrayOfOptions = response.body().toString().split(",");
        for(String s : arrayOfOptions){
            System.out.println(s);
        }

        return arrayOfOptions;
    }


    public Board getNewBoard(String gameId) throws Exception {
        HttpResponse<String> boardString = client.makeGetRequest("newBoard/" + gameId);
        BoardTemplate template = returnBoardTemplate(boardString);
        Board board = new Board(template.width, template.height, template.spaceTemplates);
        return board;

    }

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

    public int getPlayerCount(int gameId) throws Exception {
        HttpResponse<String> response = client.makeGetRequest("playerCount/" + gameId);
        System.out.println(Integer.valueOf(response.body()));
        return Integer.valueOf(response.body());
    }
}
