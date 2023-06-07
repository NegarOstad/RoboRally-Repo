/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
//import dk.dtu.compute.se.pisd.roborally.model.BoardElement;
//import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
//import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.*;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {
    private static final HttpClient httpClient =
            HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(20)).build();

    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = "json";

    public static Board loadBoard(String boardname) throws IOException, InterruptedException {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        HttpRequest httpRequest =
                HttpRequest.newBuilder().GET().uri(URI.create("http://10.209.204.5:8080/loadGame/"))
                        .build();
        //httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenAccept(System.out::println).join();
        HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());


        // In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(SpaceAction.class, new Adapter<SpaceAction>());
        Gson gson = simpleBuilder.create();

		Board newBoard;
		 //FileReader fileReader = null;
        JsonReader reader = null;
		try {
            //String filename = "src/main/java/dk/dtu/compute/se/pisd/roborally/fileaccess/savefiles/"+boardname+".json";
            //fileReader = new FileReader(filename);
            //reader = gson.newJsonReader(fileReader);
			//reader = gson.newJsonReader(new InputStreamReader(inputStream));
            BoardTemplate boardTemplate = gson.fromJson(reader, BoardTemplate.class);
			// Genopstil board
            newBoard = new Board(boardTemplate.width, boardTemplate.height, boardname, boardTemplate.spaceTemplates);

            // Genopstil spillerne
            for(PlayerTemplate p : boardTemplate.playerTemplates){
                Player newPlayer = new Player(p.color, p.name);
                Space newPlayerSpace = newBoard.getSpace(p.spaceTemplate.x, p.spaceTemplate.y);
                newPlayer.setSpace(newPlayerSpace, newBoard);
                newPlayer.setTokenCount(p.tokenCount);
                newPlayer.setHeading(p.heading);
                newPlayer.setCards(p.getCommandCards());
                newBoard.getPlayers().add(newPlayer);
                if(p.name.equals(boardTemplate.currentTemplate.name))
                    newBoard.setCurrentPlayer(newPlayer);

            }
            newBoard.setPhase(boardTemplate.phase);
            newBoard.setStep(boardTemplate.step);
            newBoard.setWinnerStatus(boardTemplate.winnerIsFound);
            newBoard.setStepMode(boardTemplate.stepMode);

			reader.close();
			return newBoard;
		} catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    //inputStream = null;
                } catch (IOException e2) {}
            }
           /* if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e2) {}
			}*/
		}
		return null;
    }

    public static void saveBoard(Board board, String name) {
        BoardTemplate template = new BoardTemplate(board, board.getSpaces(), board.getPlayers(), board.getCurrentPlayer());

        //ClassLoader classLoader = LoadBoard.class.getClassLoader();
        // TODO: this is not very defensive, and will result in a NullPointerException
        //       when the folder "resources" does not exist! But, it does not need
        //       the file "simpleCards.json" to exist!
        //String filename =classLoader.getResource("boards").getPath() + "/" + name + "." + JSON_EXT;

//     System.out.println("this is my file name: " + filename);
       //String filename = "src/defaultname.json";

        String filename = "src/main/resources/boards/"+name+".json";

        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(SpaceAction.class, new Adapter<SpaceAction>())
                .disableHtmlEscaping()
                //.setPrettyPrinting()
        ;
        Gson gson = simpleBuilder.create();

        //FileWriter fileWriter = null;
        //JsonWriter writer = null;
        //fileWriter = new FileWriter(filename);
        //writer = gson.newJsonWriter(fileWriter);
       String jsonString = gson.toJson(template, template.getClass()/*, writer*/);
        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                            .POST(HttpRequest.BodyPublishers.ofString(jsonString, StandardCharsets.UTF_8))
                            .uri(URI.create("http://10.209.204.5:8080/saveGame/"+name))
                            .build();

        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenAccept(System.out::println).join();

        //writer.close();


    }

}
