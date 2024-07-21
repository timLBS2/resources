package edu.uob.basicCommands;

import edu.uob.entities.Player;

/**
 * @className: BasicCMD
 * @description Abstract class of basic commands
 * @Package edu.uob.basicCommands
 * @author: Li
 * @date: 01/05/2023
 **/
public abstract class BasicCMD {
    public abstract String execute(Player player, String[] tokens);
}
