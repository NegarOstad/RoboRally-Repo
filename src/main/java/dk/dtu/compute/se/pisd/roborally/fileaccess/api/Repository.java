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
                //.setPrettyPrinting()
                ;
        Gson gson = simpleBuilder.create();

        return gson.toJson(template, template.getClass()/*, writer*/);
    }
    public String[] getList(String path) throws Exception {
      HttpResponse<String> response = client.makeGetRequest("sendList/"+path);
        /*HttpRequest httpRequestList =
                HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/sendList"))
                        .build();
        HttpResponse responseList = httpClient.send(httpRequestList, HttpResponse.BodyHandlers.ofString());*/
        System.out.println(response.body());
        String[] arrayOfOptions = response.body().toString().split(",");
        for(String s : arrayOfOptions){
            System.out.println(s);
        }

        return arrayOfOptions;
    }


    public Board loadBoard(String boardname) throws Exception {

            HttpResponse<String> response = client.makeGetRequest("sendBoard/templates/"+boardname);
            BoardTemplate template = returnBoardTemplate(response);
            Board board = new Board(template.width, template.height, template.spaceTemplates);
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
            board.setPhase(template.phase);
            board.setStep(template.step);
            board.setWinnerStatus(template.winnerIsFound);
            board.setStepMode(template.stepMode);
            return board;

    }


    private BoardTemplate returnBoardTemplate(HttpResponse<String> response){
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(SpaceAction.class, new Adapter<SpaceAction>());
        Gson gson = simpleBuilder.create();
        BoardTemplate boardTemplate = gson.fromJson(response.body().toString(), BoardTemplate.class);
        return boardTemplate;

    }

    public int newGameId(int playerCount, String boardNum) throws Exception {
        HttpResponse<String> response = client.makeGetRequest("new/" + playerCount + "/" + boardNum);
        System.out.println("New get request:" + response.body());
        System.out.println("new/" + playerCount + "/" + boardNum);
        return valueOf(response.body());
    }

    public int getGameID() throws Exception {
        HttpResponse<String> response = client.makeGetRequest("gameID");
        return parseInt(response.body());
    }


    public Board getBoard(String folder, String filename) throws Exception {
        String path = folder+"/"+filename;
        HttpResponse<String> boardString = client.makeGetRequest("sendBoard/" + path);
        BoardTemplate template = returnBoardTemplate(boardString);
        Board board = new Board(template.width, template.height, template.spaceTemplates);
        return board;

    }
}
