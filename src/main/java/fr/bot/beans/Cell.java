/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Zouhair
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Cell {

    private int index;

    private int mouvement;
    private int layerObject2Num;
    private int layerObject2Interactive;
    private boolean walkable;
    private String description;

    private int x;
    private int y;

    public int getOriantationIfChangeur() {
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

}
