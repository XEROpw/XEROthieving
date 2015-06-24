package pw.xero.parabot.thieving;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.methods.Skill;
import org.rev317.min.api.methods.SceneObjects.Option;

public class ArdougneZone
{
	int ID_DONATORSTALLS[] = {4705, 4275};
	ArdougneLocation location = new ArdougneLocation();
	ArdougneSteal steal = new ArdougneSteal();
	
	public enum ArdougneStall
	{
		STALL_BAKERS(2561, 1),
		STALL_SILK(2560, 40),
		STALL_FUR(2563, 60),
		STALL_SILVER(2565, 90);
		
		private int objectID, level;
		
		ArdougneStall(int objectID, int level)
		{
			this.objectID = objectID;
			this.level = level;
		}
		
		public static int getStall(int level)
		{
			ArdougneStall beststall = STALL_BAKERS;
			
			for(ArdougneStall stall : values())
			{
				if(stall.level <= level) beststall = stall;
				else break;
			}
			
			return beststall.objectID;
		}
	}
	
	public class ArdougneLocation implements Strategy
	{		
		@Override
		public boolean activate()
		{
			return (SceneObjects.getNearest(ArdougneStall.getStall(Skill.THIEVING.getRealLevel())).length == 0);
		}

		@Override
		public void execute()
		{
			Teleport.SKILLING_ARDOUGNE_THIEVING.Teleport();
			Time.sleep(3000, 5000);
		}

	}
	
	public class ArdougneSteal implements Strategy
	{
		@Override
		public boolean activate()
		{
			return (Players.getMyPlayer().getAnimation() == -1);
		}

		@Override
		public void execute()
		{
			SceneObjects.getClosest(ArdougneStall.getStall(Skill.THIEVING.getRealLevel())).interact(Option.STEAL_FROM);
			Time.sleep(1500, 2000);
		}

	}
}
