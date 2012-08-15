package com.aigilas;

import com.spx.io.Server;
import com.xna.wrapper.Console;

public class ServerThread implements Runnable
{
	@Override
	public void run() {
        Server.Get();
        try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while (Server.Get().IsOnlyInstance())
        {
            Server.Get().Update();
        }
        Console.WriteLine("Finished being a server");
	}
}