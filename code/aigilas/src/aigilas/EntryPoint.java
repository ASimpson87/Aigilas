package aigilas;import spx.io.Client;import spx.io.Server;import xna.wrapper.Console;import xna.wrapper.Environment;import xna.wrapper.Game;public class EntryPoint {	public static void main(String[] args) {		Thread server = new Thread(new ServerThread());		server.run();		try {			Thread.sleep(100);		} catch (InterruptedException e) {			e.printStackTrace();		}		Game game = new Game();		game.Initialize();		while (game.IsRunning()) {			game.Update();		}		Server.Get().Close();		Client.Get().Close();		Console.WriteLine("Finished being a client");		Environment.Exit();	}}