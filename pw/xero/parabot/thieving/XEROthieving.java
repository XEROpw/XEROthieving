package pw.xero.parabot.thieving;

import java.awt.Graphics;
import java.util.ArrayList;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;

@ScriptManifest
(
	name = "XERO Thieving",
	description = "The ultimate Ikov AIO thieving experience!",
	author = "VectorX",
	servers = "Ikov",
	category = Category.THIEVING,
	version = 1.0
)
public class XEROthieving extends Script implements Paintable, MessageListener
{
	ArrayList<Strategy> strategies = new ArrayList<Strategy>();
	
	@Override
	public boolean onExecute()
	{
		strategies.add(new LogoutHandler()); //for those starting in the login screen
		
		GUI gui = new GUI();
		gui.setVisible(true);
		
		while(gui.isVisible())
		{
			Time.sleep(3000);
		}
		
		switch(gui.boxChoice.getSelectedIndex())
		{
		case 0: // EDGEVILLE:
			EdgevilleZone EdgevilleZone = new EdgevilleZone();
			strategies.add(EdgevilleZone.location);
			strategies.add(EdgevilleZone.deposit);
			strategies.add(EdgevilleZone.sell);
			strategies.add(EdgevilleZone.steal);
			break;
		case 1: // ARDOUGNE
			ArdougneZone ArdougneZone = new ArdougneZone();
			strategies.add(ArdougneZone.location);
			strategies.add(ArdougneZone.steal);
			break;
		case 2: // DRAYNOR
			Logger.addMessage("Draynor not done yet :/", true);
			return false;
		case 3: // DONATOR
			DonatorZone DonatorZone = new DonatorZone();
			strategies.add(DonatorZone.location);
			strategies.add(DonatorZone.steal);
			break;
		default:
			return false;
		}
		
		provide(strategies);
		return true;
	};
	
	@Override
	public void onFinish()
	{
		// --- SUBMIT progress data to mysql... tbd when in SDN/BDN --- //
	}
	
	@Override
	public void messageReceived(MessageEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paint(Graphics arg0)
	{
		// TODO Auto-generated method stub
		
	}

}
