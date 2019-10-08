package org.stevenw.shops;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandRunnable implements Runnable {
	ArrayList<String> commands = new ArrayList<>();
	Player player;
	public CommandRunnable(ArrayList<String> commands, Player player) {
		this.commands = commands;
		this.player = null;
	}
	@Override
	public void run() {

	}
	private String getWithVarsReplaced(String original) {
		return original.replaceAll("\\{PLAYER}", player.getName());
	}
}
