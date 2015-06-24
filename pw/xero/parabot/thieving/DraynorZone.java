package pw.xero.parabot.thieving;

import java.awt.event.KeyEvent;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Bank;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.methods.Skill;
import org.rev317.min.api.methods.SceneObjects.Option;

public class DraynorZone
{
	int ID_DRAYNORSTALLS[] = {7053, 14011};
	int ID_FARMER = 2234;
	int ID_BOOTH = 2213;
	
	DraynorLocation location = new DraynorLocation();
	DraynorBank bank = new DraynorBank();
	DraynorEmpty empty = new DraynorEmpty();
	DraynorStealStall stall = new DraynorStealStall();
	DraynorStealFarmer farmer = new DraynorStealFarmer();
	
	boolean isBanking = false;
	
	public DraynorZone(boolean isBanking)
	{
		this.isBanking = isBanking;
	}
	
	public class DraynorLocation implements Strategy
	{		
		@Override
		public boolean activate()
		{
			return (SceneObjects.getNearest(ID_DRAYNORSTALLS).length == 0);
		}

		@Override
		public void execute()
		{
			Teleport.SKILLING_DRAYNOR_THIEVING.Teleport();
			Time.sleep(3000, 5000);
		}

	}
	
	public class DraynorBank implements Strategy
	{

		@Override
		public boolean activate()
		{
			return Inventory.isFull();
		}

		@Override
		public void execute()
		{
			if(!Bank.isOpen())
				SceneObjects.getClosest(ID_BOOTH).interact(Option.USE);
			Time.sleep(3000, 5000);
			
			if(Bank.isOpen())
			{
				Logger.addMessage("Bank is open!", true);
				//Bank.depositAll(); doesn't work anymore?
				Menu.sendAction(646, -1, -1, 21012);
				Time.sleep(1000, 2000);
				Bank.close();
				Time.sleep(300, 750);
			}
		}
	}
	
	public class DraynorEmpty implements Strategy
	{
		@Override
		public boolean activate()
		{
			return Inventory.isFull();
		}

		@Override
		public void execute()
		{
			Keyboard.getInstance().sendKeys("::empty");
			Time.sleep(350, 500);
			Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);
			Time.sleep(1500, 2000);
		}

	}
	
	public class DraynorStealStall implements Strategy
	{
		@Override
		public boolean activate()
		{
			return (Players.getMyPlayer().getAnimation() == -1);
		}

		@Override
		public void execute()
		{
			SceneObjects.getClosest(ID_DRAYNORSTALLS).interact(Option.STEAL_FROM);
			Time.sleep(1500, 2000);
		}

	}
	
	public class DraynorStealFarmer implements Strategy
	{
		@Override
		public boolean activate()
		{
			if(Players.getMyPlayer().getAnimation() == -1)
			{
				if(isBanking)
				{
					if(Skill.HITPOINTS.getLevel() > 1) return true;
					else return false;
				}
				else return true;
			}
			return false;
		}

		@Override
		public void execute()
		{
			Npcs.getClosest(ID_FARMER).interact(org.rev317.min.api.methods.Npcs.Option.PICKPOCKET);
			Time.sleep(1500, 2000);
		}

	}
}