package aigilas.desktop;import spx.core.XnaManager;import spx.net.Server;import aigilas.Aigilas;import com.badlogic.gdx.backends.lwjgl.LwjglApplication;import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;public class Main {	public static void main(String[] args) {		Thread server = new Server();		server.start();		try {			Thread.sleep(100);		}		catch (InterruptedException e) {			e.printStackTrace();		}		System.out.println("Launching the main game loop");		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();		cfg.title = "Aigilas";		cfg.width = XnaManager.RenderWidth;		cfg.height = XnaManager.RenderHeight;		cfg.useGL20 = true;		new LwjglApplication(new Aigilas(), cfg);		new LwjglApplication(new Aigilas(), cfg);	}}