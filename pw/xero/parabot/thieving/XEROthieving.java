package pw.xero.parabot.thieving;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.api.utils.Timer;
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
	private final Image chat = getImage("http://parabot.xero.pw/img/thieving.jpg");
	
	private int money, steals, randoms, oldstack = 0;
	private Timer runtime = new Timer();
	int location = 0;
	
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
			strategies.add(ArdougneZone.steal);
			location = 1;
			break;
		case 2: // DRAYNOR-STALLS
			DraynorZone DraynorZone = new DraynorZone(gui.doBank.isSelected());
			strategies.add(DraynorZone.location);
			strategies.add(new Deposit());
			if(gui.doBank.isSelected()) strategies.add(DraynorZone.bank);
			else strategies.add(DraynorZone.empty);
			strategies.add(DraynorZone.stall);
			location = 2;
			break;
		case 3: // DRAYNOR-FARMER
			DraynorZone DraynorZone1 = new DraynorZone(gui.doBank.isSelected());
			strategies.add(DraynorZone1.location);
			strategies.add(new Deposit());
			if(gui.doBank.isSelected()) strategies.add(DraynorZone1.bank);
			else strategies.add(DraynorZone1.empty);
			strategies.add(DraynorZone1.farmer);
			location = 3;
			break;
		case 4: // DONATOR
			DonatorZone DonatorZone = new DonatorZone();
			strategies.add(DonatorZone.location);
			strategies.add(DonatorZone.steal);
			location = 4;
			break;
		default:
			return false;
		}
		
		provide(strategies);
		steals = 0;
		randoms = 0;
		money = 0;
		oldstack = 0;
		return true;
	};
	
	@Override
	public void onFinish()
	{
		// --- SUBMIT progress data to mysql... tbd when in SDN/BDN --- //
	}
	
	@Override
	public void messageReceived(MessageEvent me)
	{
		String message = me.getMessage().toLowerCase();
		if(me.getType() == 0)
		{
			if(message.contains("anti-bot")) { randoms++; }
			else if(message.contains(("silk"))) { money += 5120; steals++; }
			else if(message.contains(("golden"))) { money += 6000; steals++; }
			else if(message.contains(("emerald"))) { money += 12000; steals++; }
			else if(message.contains(("battlestaff"))) { money += 16000; steals++; }
			else if(message.contains(("adamant"))) { money += 20000; steals++; }
			else if(message.contains(("steal some coins")))
			{
				Item cash = Inventory.getItem(996);
				if(cash != null)
				{
					int gained = cash.getStackSize();
					money += gained - oldstack;
					if(gained > 2000000000)
					{
						Menu.sendAction(493, cash.getId() - 1, cash.getSlot(), 3214);
						Time.sleep(1000);
						oldstack = 0;
					}
					else
						oldstack = gained;
				}
				steals++;
			}
			else if(message.contains("coins."))
			{
				String values[] = message.split(" ");
				money += Integer.parseInt(values[2]);
				
				Item cash = Inventory.getItem(996);
				if(cash != null)
				{
					if(cash.getStackSize() > 2000000000)
					{
						Menu.sendAction(493, cash.getId() - 1, cash.getSlot(), 3214);
						Time.sleep(1000);
					}
				}
				steals++;
			}
			else if(message.contains("seed stall") || message.contains("wine stall") || message.contains("master farmer"))
			{
				steals++;
			}
		}
	}

	@Override
	public void paint(Graphics graphics)
	{
		Graphics2D g = (Graphics2D)graphics;
		g.drawImage(chat, 8, 345, null);
		g.setFont(new Font("Verdana", 0, 12));
		g.setColor(new Color(255, 255, 255));
		
		g.drawString("Runtime: " + runtime.toString(), 18, 366);
		g.drawString("Steals: " + steals, 18, 384);
		g.drawString("Steals/h: " + runtime.getPerHour(steals), 18, 402);
		if(location != 2 && location != 3)
		{
			g.drawString("Money: " + formatNumber(money), 18, 420);
			g.drawString("Money/h: " + formatNumber(runtime.getPerHour(money)), 18, 438);
		}
		g.drawString("Randoms: " + randoms, 18, (location != 2 && location != 3) ? 456 : 420);
	}
	
	private Image getImage(String url)
	{
		try{ return ImageIO.read(new URL(url)); }
		catch(Exception exception) { return null; }
	}
	
	private String formatNumber(double number)
	{
		DecimalFormat format = new DecimalFormat("#,###.00");

		if (number >= 1000 && number < 1000000) { return format.format(number / 1000) + "K"; }
		else if (number >= 1000000 && number < 1000000000) { return format.format(number / 1000000) + "M"; }
		else if (number >= 1000000000){ return format.format(number / 1000000000) + "B"; }
		
        return ("" + number);
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
