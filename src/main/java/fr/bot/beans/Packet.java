/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.beans;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author zouhairhajji
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Packet {

    private Timestamp timestamp;
    
    private boolean sent;

    private String rowdata;

    public static Packet create_packet(boolean sent, String rowData){
        return Packet
                .builder()
                .rowdata(rowData)
                .sent(sent)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
