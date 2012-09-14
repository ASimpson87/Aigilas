package spx.io;

import aigilas.management.Commands;

public class CommandDefinition {
	public Commands Command;
	public Buttons Gamepad;
	public Keys Keyboard;
	public Contexts LockContext;

	public CommandDefinition(Commands command, Keys key, Buttons button, Contexts lockContext) {
		Command = command;
		Gamepad = button;
		Keyboard = key;
		LockContext = lockContext;
	}
}