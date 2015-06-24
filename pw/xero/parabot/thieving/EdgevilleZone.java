package pw.xero.parabot.thieving;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.methods.Skill;
import org.rev317.min.api.methods.SceneObjects.Option;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.Npc;
import org.rev317.min.api.wrappers.SceneObject;

public class EdgevilleZone
{
	EdgevilleLocation location = new EdgevilleLocation();
	EdgevilleDeposit deposit = new EdgevilleDeposit();
	EdgevilleSell sell = new EdgevilleSell();
	EdgevilleSteal steal = new EdgevilleSteal();
	
	static final int ID_COINS = 996;
	static final int ID_BANDIT = 1878;
	
	public enum EdgevilleStall
	{
		STALL_FOOD(4875, 951, 1),
		STALL_CRAFTING(4874, 1636, 30),
		STALL_GENERAL(4876, 1640, 60),
		STALL_MAGIC(4877, 1392, 65),
		STALL_SCIMITAR(4878, 1332, 80);
		
		private int objectID, itemID, level;
		
		EdgevilleStall(int objectID, int itemID, int level)
		{
			this.objectID = objectID;
			this.itemID = itemID;
			this.level = level;
		}
		
		public static int getStall(int level)
		{
			EdgevilleStall beststall = STALL_FOOD;
			
			for(EdgevilleStall stall : values())
			{
				if(stall.level <= level) beststall = stall;
				else break;
			}
			
			return beststall.objectID;
		}
		
		public static int[] getItems()
		{
			EdgevilleStall stalls[] = values();
			int items[] = new int[stalls.length];
			
			for(int i = 0; i < values().length; i++)
			{
				items[i] = stalls[i].itemID;
			}
			
			return items;
		}
	}
	
	public class EdgevilleLocation implements Strategy
	{
		@Override
		public boolean activate()
		{
			return (SceneObjects.getNearest(EdgevilleStall.getStall(Skill.THIEVING.getRealLevel())).length == 0);
		}

		@Override
		public void execute()
		{
			Teleport.HOME_HOME.Teleport();
			Time.sleep(3000, 5000);
		}
	}
	
	public class EdgevilleDeposit implements Strategy
	{
		@Override
		public boolean activate()
		{
			return (Game.getOpenInterfaceId() == -1 && Game.getOpenBackDialogId() == -1 && Inventory.contains(ID_COINS));
		}

		@Override
		public void execute()
		{
			Item coins = Inventory.getItem(ID_COINS);
			if(coins != null)
				Menu.sendAction(493, coins.getId() - 1, coins.getSlot(), 3214);
			
			Time.sleep(1000, 2000);
		}
		
	}
	
	public class EdgevilleSell implements Strategy
	{
		@Override
		public boolean activate()
		{
			return (Inventory.getCount() >= 27);
		}

		@Override
		public void execute()
		{
			if(Inventory.isFull())
			{
				Inventory.getItems(EdgevilleStall.getItems())[0].drop();
				Time.sleep(1000, 1500);
			}
			
			if(!Inventory.isFull())
			{
				if(Game.getOpenInterfaceId() != 3824)
				{
					Npc bandit = Npcs.getClosest(ID_BANDIT);
					if(bandit != null)
					{
						bandit.interact(Npcs.Option.TALK_TO);
						Time.sleep(3000, 5000);
					}
				}
				
				if(Game.getOpenInterfaceId() == 3824)
				{
					while(Inventory.contains(EdgevilleStall.getItems()))
					{
						Item item = Inventory.getItems(EdgevilleStall.getItems())[0];
						Menu.sendAction(431, item.getId() - 1, item.getSlot(), 3823);
		                Time.sleep(200, 500);
					}
				}
			}
		}
	}
	
	public class EdgevilleSteal implements Strategy
	{
		@Override
		public boolean activate()
		{
			return (SceneObjects.getNearest(EdgevilleStall.getStall(Skill.THIEVING.getRealLevel())).length > 0 && Players.getMyPlayer().getAnimation() == -1);
		}

		@Override
		public void execute()
		{
			SceneObject stall = SceneObjects.getClosest(EdgevilleStall.getStall(Skill.THIEVING.getRealLevel()));
			if(stall != null)
			{
				stall.interact(Option.STEAL_FROM);
				Time.sleep(750, 1000);
			}
		}
		
	}
}