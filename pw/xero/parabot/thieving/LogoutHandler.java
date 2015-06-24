package pw.xero.parabot.thieving;

import java.awt.event.KeyEvent;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;

public class LogoutHandler implements Strategy
{
	@Override
	public boolean activate()
	{
		return !Game.isLoggedIn();
	}

	@Override
	public void execute()
	{
		Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);
		Time.sleep(5000);
	}
}