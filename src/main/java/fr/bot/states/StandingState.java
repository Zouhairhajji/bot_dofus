/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.states;

import fr.bot.abstracts.SocketParser;
import fr.bot.beans.Cell;
import fr.bot.threads.Client;
import static fr.bot.utils.MapUtils.decypterData;
import static fr.bot.utils.MapUtils.getMapData;
import static fr.bot.utils.MapUtils.getMapKey;
import static fr.bot.utils.MapUtils.uncompressCell;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author zouhairhajji
 */
public class StandingState implements SocketParser {

    @Override
    public void parse(String datagram, Client client) throws Exception {
        if (datagram.startsWith("GDM")) {
            String mapKey = getMapKey(datagram);
            String mapData = getMapData(datagram);

            String decrypted_map = decypterData(mapData, mapKey);

            Cell[] cells = new Cell[decrypted_map.length() / 10];
            int index = -1;
            for (int i = 0; i < decrypted_map.length(); i = i + 10) {
                String cell = decrypted_map.substring(i, i + 10);
                index++;
                cells[index] = uncompressCell(cell);
            }

            String ressource = new ClassPathResource("data/ressources.txt").getPath();
            List<String[]> ressources = Files.lines(Paths.get(ressource))
                    .map(l -> l.split(":"))
                    .collect(Collectors.toList());
            
            for (Cell cell : cells) {
                ressources.stream()
                        .filter( r -> Integer.parseInt(r[0]) == cell.getLayerObject2Num())
                        .findFirst();
            }
        }
    }

}
