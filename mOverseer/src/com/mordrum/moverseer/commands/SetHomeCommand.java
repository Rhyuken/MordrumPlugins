package com.mordrum.moverseer.commands;

import com.mordrum.moverseer.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created with IntelliJ IDEA.
 * User: Jesse
 * Date: 5/31/13
 * Time: 12:04 AM
 */
public class SetHomeCommand extends Command {

	private Main plugin;

	public SetHomeCommand(Main instance) {
		super(
				instance.getConfig().commands_sethome_name,
				instance.getConfig().commands_sethome_perm
		);
		plugin = instance;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

	}
}
