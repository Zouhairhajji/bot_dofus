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
public class AuthUtils {

    public static String crypt(String pwd, String key) {
        int tmp_pwd_char = 0;
        int tmp_key_char = 0;

        int tmp_pwd_div_16 = 0;
        int tmp_pwd_mod_16 = 0;

        int pos1 = 0;
        int pos2 = 0;

        String res = "#1";
        String hash[] = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-", "_"};

        for (int i = 0; i < pwd.length(); i++) {
            tmp_pwd_char = pwd.charAt(i);
            tmp_key_char = key.charAt(i);

            tmp_pwd_div_16 = tmp_pwd_char / 16;
            tmp_pwd_mod_16 = tmp_pwd_char % 16;

            pos1 = (((tmp_key_char + tmp_pwd_div_16) % (hash.length)) % (hash.length));
            pos2 = (((tmp_key_char + tmp_pwd_mod_16) % (hash.length)) % (hash.length));
            res += hash[pos1] + hash[pos2];
        }

        return res;
    }

}
