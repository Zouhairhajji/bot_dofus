/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.utils;

/**
 *
 * @author zouhairhajji
 */


import fr.bot.beans.Cell;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static java.lang.Math.toIntExact;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Zouhair
 */
public class MapUtils {

    public final static String[] HEX_CHARS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
    public final static String ZKARRAY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
    
    public final static String[] hash = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-", "_"};
    public final static String[] hash2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public static List<String> cases = new ArrayList<String>();

    public final static void initializeCells() {
        System.out.println("Initialisation");
        cases.clear();
        for (int i = 0; i < hash2.length; i++) {
            for (int j = 0; j < hash.length; j++) {
                cases.add(hash2[i] + hash[j]);
            }
        }
    }

    public static int checkSum(String keyMap) {
        return keyMap.chars().map(s -> s % 16).sum() % 16;
    }

    public static String decypterData(String data, String decryptKey) throws UnsupportedEncodingException {
        String result = "";
        System.out.println(data);
        System.out.println(decryptKey);

        String decryptedKey = prepareKey(decryptKey);
        long checkSum = checkSum(decryptedKey) * 2;

        for (int i = 0, k = 0; i < data.length(); i += 2) {

            String hex_code = data.substring(i, i + 2);
            int hex_int = Integer.parseUnsignedInt(hex_code, 16);
            char power = decryptedKey.charAt((k++ + toIntExact(checkSum)) % decryptedKey.length());
            int op_result = hex_int ^ power;

            result += (char) op_result;

        }

        return URLDecoder.decode(result, "UTF-8");
    }

    private static String prepareKey(String decryptKey) throws UnsupportedEncodingException {
        String keyResult = "";
        for (int i = 0; i < decryptKey.length(); i += 2) {
            String hex_code = decryptKey.substring(i, i + 2);
            int hex_int = Integer.parseUnsignedInt(hex_code, 16);
            keyResult += (char) hex_int;
        }
        return URLDecoder.decode(keyResult, "UTF-8");
    }

    public static int getChangeurOrientation(int caseIndex) {
        int x = getCellX(caseIndex);
        int y = getCellY(caseIndex);

        if ((x - 1) == y) {
            return 4;
        } else if ((x - 27) == y) {
            return 2;
        } else if ((x + y) == 31) {
            return 3;
        } else if (y < 0) {
            if (x - Math.abs(y) == 1) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public static int getCellX(int laCase) {

        int _loc5 = Math.floorDiv(laCase, (15 * 2 - 1));
        int _loc6 = laCase - _loc5 * (15 * 2 - 1);
        int _loc7 = _loc6 % 15;
        int _loc8 = (laCase - (15 - 1) * (_loc5 - _loc7)) / 15;
        return _loc8;

    }

    public static int getCellY(int laCase) {
        int _loc5 = Math.floorDiv(laCase, (15 * 2 - 1));
        int _loc6 = laCase - _loc5 * (15 * 2 - 1);
        int _loc7 = _loc6 % 15;
        int _loc8 = _loc5 - _loc7;
        return _loc8;
    }
    
    public static String getMapID(final String datagram) {
        return datagram.split("\\|")[1];
    }

    public static String getMapIndice(final String datagram) {
        return datagram.split("\\|")[2];
    }

    public static String getMapKey(final String datagram) {
        return datagram.split("\\|")[3];
    }

    public static String getMapData(final String datagram) throws IOException {
        String mapID = getMapID(datagram);
        String mapIndice = getMapIndice(datagram);

        String mapFileName = "maps/" + mapID + "_" + mapIndice + "X.txt";
        return Files.lines(Paths.get(mapFileName))
                .collect(Collectors.joining(""))
                .split("\\|")[1];
    }

    public static Cell uncompressCell(String cData) {
        int curseur = cData.length() - 1;
        int[] data = new int[5000];
        while (curseur >= 0) {
            data[curseur] = ZKARRAY.indexOf(cData.charAt(curseur));
            curseur--;
        }

        int mouvement = (data[2] & 56) >> 3;
        int layerObject2Num = ((data[0] & 2) << 12) + ((data[7] & 1) << 12) + (data[8] << 6) + data[9];
        int layerObject2Interactive = ((data[7] & 2) >> 1);
       // attribuer ces données à l'objet Cell

        return Cell.builder()
                .mouvement(mouvement)
                .layerObject2Num(layerObject2Num)
                .layerObject2Interactive(layerObject2Interactive)
                .build();
    }
    
    
    public static String unPackSwf(final String datagram) throws IOException {
        String mapID = getMapID(datagram);
        String mapIndice = getMapIndice(datagram);

        String url = "http://staticns.ankama.com/dofus/gamedata/dofus/maps/" + mapID + "_" + mapIndice + "X.swf";
        return "";
    }

}

