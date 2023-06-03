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

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {

    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = "json";

    public static Board loadBoard(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

       ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(BOARDSFOLDER + "/" + boardname + "." + JSON_EXT);
        if (inputStream == null) {
            // TODO these constants should be defined somewhere
            return new Board(8,8);
        }

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
			reader = gson.newJsonReader(new InputStreamReader(inputStream));
            BoardTemplate boardTemplate = gson.fromJson(reader, BoardTemplate.class);
			// Genopstil board
            newBoard = new Board(boardTemplate.width, boardTemplate.height, boardname, boardTemplate.spaceTemplates);

            // Genopstil spillerne
            for(PlayerTemplate p : boardTemplate.playerTemplates){
                Player newPlayer = new Player(p.color, p.name);
                Space newPlayerSpace = newBoard.getSpace(p.spaceTemplate.x, p.spaceTemplate.y);
                newPlayer.setSpace(newPlayerSpace, newBoard);
                newBoard.getPlayers().add(newPlayer);

            }

           /* for (int i = 0; i < newBoard.width; i++) {
                for(int j = 0 ; j < newBoard.height; j++){
                    Space space = new Space(spaceTemplates[i][j].x, spaceTemplates[i][j].y);


                    if (space != null) {
                            space.setBoardElement(spaceTemplates[i][j].boardElement);

                    }
                }

            }*/
            System.out.println();
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
        BoardTemplate template = new BoardTemplate(board.width, board.height, board.getSpaces(), board.getPlayers(), board.getCurrentPlayer());

        //ClassLoader classLoader = LoadBoard.class.getClassLoader();
        // TODO: this is not very defensive, and will result in a NullPointerException
        //       when the folder "resources" does not exist! But, it does not need
        //       the file "simpleCards.json" to exist!
        //String filename =classLoader.getResource("boards").getPath() + "/" + name + "." + JSON_EXT;

//     System.out.println("this is my file name: " + filename);
       //String filename = "src/defaultname.json";

        String filename = "src/main/resources/boards/"+name+".json";

        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(SpaceAction.class, new Adapter<SpaceAction>()).
                setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try {
            fileWriter = new FileWriter(filename);
            writer = gson.newJsonWriter(fileWriter);
            gson.toJson(template, template.getClass(), writer);
            writer.close();

        } catch (IOException e1) {
            if (writer != null) {
                try {
                    writer.close();
                    fileWriter = null;
                } catch (IOException e2) {}
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e2) {}
            }
        }


    }

}
