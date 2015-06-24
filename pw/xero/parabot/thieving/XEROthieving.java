package pw.xero.parabot.thieving;

import java.awt.Graphics;
import java.util.ArrayList;

import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.wrappers.Item;

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
			strategies.add(new Deposit());
			if(gui.doBank.isSelected()) strategies.add(EdgevilleZone.bank);
			else strategies.add(EdgevilleZone.sell);
			strategies.add(EdgevilleZone.steal);
			break;
		case 1: // ARDOUGNE
			ArdougneZone ArdougneZone = new ArdougneZone();
			strategies.add(ArdougneZone.location);
			strategies.add(new Deposit());
			strategies.add(ArdougneZone.steal);
			break;
		case 2: // DRAYNOR-STALLS
			DraynorZone DraynorZone = new DraynorZone(gui.doBank.isSelected());
			strategies.add(DraynorZone.location);
			strategies.add(new Deposit());
			if(gui.doBank.isSelected()) strategies.add(DraynorZone.bank);
			else strategies.add(DraynorZone.empty);
			strategies.add(DraynorZone.stall);
			break;
		case 3: // DRAYNOR-FARMER
			DraynorZone DraynorZone1 = new DraynorZone(gui.doBank.isSelected());
			strategies.add(DraynorZone1.location);
			strategies.add(new Deposit());
			if(gui.doBank.isSelected()) strategies.add(DraynorZone1.bank);
			else strategies.add(DraynorZone1.empty);
			strategies.add(DraynorZone1.farmer);
			break;
		case 4: // DONATOR
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
	
	public class Deposit implements Strategy
	{
		@Override
		public boolean activate()
		{
			return (Game.getOpenInterfaceId() == -1 && Game.getOpenBackDialogId() == -1 && Inventory.contains(996));
		}

		@Override
		public void execute()
		{
			Item coins = Inventory.getItem(996);
			if(coins != null)
				Menu.sendAction(493, coins.getId() - 1, coins.getSlot(), 3214);
			
			Time.sleep(1000, 2000);
		}
		
	}
}
