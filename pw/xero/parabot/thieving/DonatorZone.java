package pw.xero.parabot.thieving;

import java.awt.event.KeyEvent;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.methods.SceneObjects.Option;

public class DonatorZone
{
	int ID_DONATORSTALLS[] = {4705, 4275};
	DonatorLocation location = new DonatorLocation();
	DonatorSteal steal = new DonatorSteal();
	
	public class DonatorLocation implements Strategy
	{		
		@Override
		public boolean activate()
		{
			return (SceneObjects.getNearest(ID_DONATORSTALLS).length == 0);
		}

		@Override
		public void execute()
		{
			Keyboard.getInstance().sendKeys("::dzone");
			Time.sleep(1000, 3000);
			Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);
			Time.sleep(3000, 5000);
		}

	}
	
	public class DonatorSteal implements Strategy
	{
		@Override
		public boolean activate()
		{
			return (Players.getMyPlayer().getAnimation() == -1);
		}

		@Override
		public void execute()
		{
			SceneObjects.getClosest(ID_DONATORSTALLS).interact(Option.STEAL_FROM);
			Time.sleep(1500, 2000);
		}

	}
}