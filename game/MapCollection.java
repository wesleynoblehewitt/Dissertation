package game;

import intelligence.NavigationGraph;
import intelligence.WayPoint;

import java.util.ArrayList;

import objects.Goal;
import objects.Pursuer;
import objects.Turret;
import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class MapCollection {

	public static String backgroundL1 = "Images/sky.png";
	public static String backgroundL2 = "Images/mountainBG.png";
	public static String backgroundL3 = "Images/icecave.png";
	
	public static WayPoint beta0 = new WayPoint(new Vectors(300, 375), 200, 20);
	public static WayPoint beta1 = new WayPoint(new Vectors(290, 375), 40, 30);
	public static WayPoint beta2 = new WayPoint(new Vectors(290,365), 40, 30);
	public static WayPoint beta3 = new WayPoint(new Vectors(380, 375), 40, 30);
	public static WayPoint beta4 = new WayPoint(new Vectors(80, 385), 70, 20);
	public static WayPoint beta5 = new WayPoint(new Vectors(300, 125), 200, 20);
	
	/**
	 * Called to return the list of permanent world objects in the beta map.
	 * Not to be changed.
	 * @return
	 */
	final public static ArrayList<MyObject> betaMap(){
		ArrayList<MyObject> map = new ArrayList<MyObject>();
		for(int i = 340; i < 581; i+=40){
			map.add(new MyObject(new Vectors(10, i), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/smallrocks.png"));
			map.add(new MyObject(new Vectors(990, i), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/smallrocks.png"));
		}
		for(int i = 40; i< 985; i+= 40){
			//if(i > 760){
			//	map.add(new MyObject(new Vectors(i, 590), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/dirt.png"));
			//} else {
			map.add(new MyObject(new Vectors(i, 590), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
			//}
		}
		map.add(new MyObject(new Vectors(40, 570), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(207, 570), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(275, 548), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(315, 548), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(387, 526), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(407, 506), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(319, 483), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(279, 483), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(239, 483), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(116, 482), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(40, 462), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(497, 483), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(605, 533), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(645, 513), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(645, 533), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(544, 553), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(566, 470), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(647, 446), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(30, 442), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(25, 422), Material.Solid, Constants.baseWidth/2, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(648, 426), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(661, 406), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(678, 436), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(718, 436), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(691, 396), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(718, 396), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(960, 561), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/grass.png"));
		map.add(new MyObject(new Vectors(960, 521), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/grass.png"));
		map.add(new MyObject(new Vectors(960, 482), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(920, 560), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/grass.png"));
		map.add(new MyObject(new Vectors(880, 560), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/grass.png"));
		map.add(new MyObject(new Vectors(840, 560), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/grass.png"));
		map.add(new MyObject(new Vectors(800, 560), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/grass.png"));
		map.add(new MyObject(new Vectors(920, 520), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/grass.png"));
		map.add(new MyObject(new Vectors(880, 520), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/grass.png"));
		map.add(new MyObject(new Vectors(757, 396), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(757, 436), Material.Solid, Constants.baseWidthW, Constants.baseHeightL, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(130, 423), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(223, 405), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(289, 382), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(318, 363), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(399, 361), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(419, 361), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(439, 361), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(439, 341), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(770, 570), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(817, 531), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(431, 426), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(444, 321), Material.Solid, Constants.baseWidth/2, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(368, 426), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(459, 341), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(459, 361), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(479, 361), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(573, 573), Material.Solid, Constants.baseWidth, Constants.baseHeight/1.5f, "Images/grass.png"));
		map.add(new MyObject(new Vectors(554, 571), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(534, 570), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/grass.png"));
		map.add(new MyObject(new Vectors(742, 366), Material.Earth, Constants.baseWidth, Constants.baseHeight));
		
		map.add(new Pursuer(new Vectors(275, 464), betaGraph()));
		map.add(new Pursuer(new Vectors(371, 570), betaGraph()));
		map.add(new Pursuer(new Vectors(676, 570), betaGraph()));
		
		map.add(new Goal(new Vectors(956, 455)));
		return map;
	}
	
	/**
	 * Generates the navigation graph that corresponds to the beta map
	 * @return
	 */
	final public static NavigationGraph betaGraph(){
		NavigationGraph graph = new NavigationGraph();
		WayPoint beta0 = new WayPoint(new Vectors(42, 553), 20, 20);
		WayPoint beta1 = new WayPoint(new Vectors(123, 573), 60, 20);
		WayPoint beta2 = new WayPoint(new Vectors(214, 549), 20, 20);
		WayPoint beta3 = new WayPoint(new Vectors (392, 571), 147, 20);
		graph.add(beta0);
		graph.add(beta1);
		graph.add(beta2);
		graph.add(beta3);
		graph.addEdge(beta0, beta1);
		graph.addEdge(beta1, beta2);
		graph.addEdge(beta2, beta3);
		
		WayPoint beta4 = new WayPoint(new Vectors(295, 532), 10, 10);
		graph.add(beta4);
		
		WayPoint beta5 = new WayPoint(new Vectors(396, 488), 30, 20);
		graph.add(beta5);
		
		WayPoint beta6 = new WayPoint(new Vectors(279, 462), 60, 10);
		graph.add(beta6);
		
		WayPoint beta7 = new WayPoint(new Vectors(115, 464), 20, 10);
		graph.add(beta7);
		
		WayPoint beta8 = new WayPoint(new Vectors(137, 463), 20, 20);
		graph.add(beta8);
		
		WayPoint beta9 = new WayPoint(new Vectors(131, 404), 20, 10);
		graph.add(beta9);
		
		WayPoint beta10 = new WayPoint(new Vectors(225, 387), 20, 10);
		graph.add(beta10);
		
		WayPoint beta11 = new WayPoint(new Vectors(300, 362), 25, 20);
		graph.add(beta11);
		
		WayPoint beta12 = new WayPoint(new Vectors(432, 320), 50, 25);
		graph.add(beta12);
		
		WayPoint beta13 = new WayPoint(new Vectors(550, 537), 30, 20);
		WayPoint beta14 = new WayPoint(new Vectors(671, 571), 85, 10);
		WayPoint beta15 = new WayPoint(new Vectors(818, 514), 50, 20);
		graph.add(beta13);
		graph.add(beta14);
		graph.add(beta15);
		graph.addEdge(beta13, beta14);
		graph.addEdge(beta14, beta15);
		
		WayPoint beta16 = new WayPoint(new Vectors(625, 492), 30, 20);
		graph.add(beta16);
		graph.addEdge(beta13, beta16);
		
		WayPoint beta17 = new WayPoint(new Vectors(711, 363), 30, 20);
		graph.add(beta17);
		
		WayPoint beta18 = new WayPoint(new Vectors(498, 461), 20, 10);
		graph.add(beta18);
		
		WayPoint beta19 = new WayPoint(new Vectors(565, 448), 20, 10);
		graph.add(beta19);
		
		return graph;
	}

	final public static ArrayList<MyObject> betaWorld2(){
		ArrayList<MyObject> map = new ArrayList<MyObject>();
		for(int i = 100; i < 581; i+=40){
			map.add(new MyObject(new Vectors(10, i), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/smallrocks.png"));
			map.add(new MyObject(new Vectors(990, i), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/smallrocks.png"));
		}
		for(int i = 40; i< 400; i+= 40){
			map.add(new MyObject(new Vectors(i, 590), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
		}
		
		map.add(new MyObject(new Vectors(41, 571), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/grass.png"));
		
		// first platforms (bottom right)
		map.add(new MyObject(new Vectors(433, 572), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(473, 572), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(531, 544), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(577, 517), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(617, 517), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(702, 491), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(742, 491), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(782, 491), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(822, 491), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(862, 491), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(892, 491), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(933, 466), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(954, 446), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		
		
		// turret platform 1
		map.add(new MyObject(new Vectors(896, 427), Material.Solid, Constants.baseWidth/2, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(881, 427), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(861, 427), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		
		map.add(new MyObject(new Vectors(717, 448), Material.Transparent, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(737, 448), Material.Transparent, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
	
		// Section 2 (after turret 1)
		map.add(new MyObject(new Vectors(970, 406), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(975, 386), Material.Solid, Constants.baseWidth/2, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(904, 359), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(865, 359), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(825, 359), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(785, 359), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(745, 359), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		
		map.add(new MyObject(new Vectors(627, 356), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(597, 356), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(518, 380), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(478, 400), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(438, 400), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(398, 400), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(358, 400), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/smallrocks.png"));
		
		map.add(new MyObject(new Vectors(330, 380), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(310, 360), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(290, 340), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(310, 380), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(290, 360), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(290, 380), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		
		// Section 3 
		map.add(new MyObject(new Vectors(356, 313), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(415, 291), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(484, 261), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(464, 270), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		
		map.add(new MyObject(new Vectors(575, 242), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(615, 242), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(655, 242), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(685, 242), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(715, 242), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(594, 222), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(755, 242), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(795, 242), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));	
		map.add(new MyObject(new Vectors(835, 242), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(875, 242), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(905, 262), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(925, 232), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/snow.png"));
		map.add(new MyObject(new Vectors(925, 202), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(955, 202), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(975, 202), Material.Solid, Constants.baseWidth/2, Constants.baseHeight, "Images/snow.png"));
		
		map.add(new MyObject(new Vectors(620, 336), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(560, 311), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(504, 311), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(524, 311), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/smallrocks.png"));
		
		// turret platform 2
		map.add(new MyObject(new Vectors(871, 174), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		
		map.add(new MyObject(new Vectors(508, 192), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(468, 192), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(458, 172), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(513, 146), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(558, 124), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(579, 104), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(609, 83), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(639, 83), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		
		// non solids
		
		map.add(new MyObject(new Vectors(758, 459), Material.TransparentM, Constants.baseWidth -1, (float)(Constants.baseHeight * 1.5), "Images/smallrocks.png"));
		map.add(new MyObject(new Vectors(656, 220), Material.TransparentM, Constants.baseWidth - 2, "Images/snow.png"));
		
		map.add(new MyObject(new Vectors(639, 57), Material.TransparentM, Constants.baseWidth - 2, (float)(Constants.baseHeight * 1.5), "Images/snow.png"));
		
		map.add(new MyObject(new Vectors(504, 292), Material.TransparentM, Constants.baseWidth, "Images/smallrocks.png"));
		
		map.add(new Goal(new Vectors(968, 185)));
		
		map.add(new Turret(new Vectors(861, 407), -1));
		map.add(new Turret(new Vectors(350, 380), 1));
		map.add(new Turret(new Vectors(861, 154), -1));
		
		map.add(new Pursuer(new Vectors(207, 571), betaWorld2Graph()));
		map.add(new Pursuer(new Vectors(822, 340), betaWorld2Graph()));
		
		return map;
	}
	
	final public static NavigationGraph betaWorld2Graph(){
		NavigationGraph graph = new NavigationGraph();
		WayPoint beta0 = new WayPoint(new Vectors(219, 572), 150, 10);
		WayPoint beta1 = new WayPoint(new Vectors(451, 555), 20, 10);
		WayPoint beta2 = new WayPoint(new Vectors(595, 499), 30, 15);
		WayPoint beta3 = new WayPoint(new Vectors(790, 475), 50, 10);
		graph.add(beta0);
		graph.add(beta1);
		graph.add(beta2);
		graph.add(beta3);
		graph.addEdge(beta0, beta1);
		graph.addEdge(beta1, beta2);
		graph.addEdge(beta2, beta3);
		
		WayPoint beta4 = new WayPoint(new Vectors(825, 339), 40, 10);
		graph.add(beta4);
		
		WayPoint beta5 = new WayPoint(new Vectors(430, 380), 90, 20);
		graph.add(beta5);
		
		WayPoint beta6 = new WayPoint(new Vectors(743, 223), 90, 10);
		WayPoint beta7 = new WayPoint(new Vectors(493, 178), 30, 15);
		graph.add(beta6);
		graph.add(beta7);
		graph.addEdge(beta6, beta7);
		return graph;
	}

	final public static ArrayList<MyObject> betaWorld3(){
		ArrayList<MyObject> map = new ArrayList<MyObject>();
		for(int i = 20; i < 581; i+=40){
			map.add(new MyObject(new Vectors(10, i), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/snow.png"));
			map.add(new MyObject(new Vectors(990, i), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/snow.png"));
		}
		for(int i = 40; i < 985; i+= 40){
			map.add(new MyObject(new Vectors(i, 590), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
			map.add(new MyObject(new Vectors(i, 10), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
			
		}
		
		map.add(new MyObject(new Vectors(40, 570), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		
		map.add(new MyObject(new Vectors(118, 60), Material.Solid, Constants.baseWidth, Constants.baseHeightL * 2, "Images/snow.png"));
		map.add(new MyObject(new Vectors(512, 30), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(391, 60), Material.Solid, Constants.baseWidth, Constants.baseHeightL * 2, "Images/snow.png"));
		map.add(new MyObject(new Vectors(594, 50), Material.Solid, Constants.baseWidth, Constants.baseHeightL * 1.5f, "Images/snow.png"));
		map.add(new MyObject(new Vectors(874, 40), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/snow.png"));
		map.add(new MyObject(new Vectors(614, 40), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/snow.png"));
		map.add(new MyObject(new Vectors(372, 30), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(411, 30), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(431, 30), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(391, 120), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/snow.png"));
		map.add(new MyObject(new Vectors(66, 30), Material.Solid, Constants.baseWidthW * 2.3f, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(30, 50), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(970, 50), Material.Solid, Constants.baseWidth, Constants.baseHeightL * 1.5f, "Images/snow.png"));
		map.add(new MyObject(new Vectors(950, 30), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(752, 30), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
	
		
		map.add(new MyObject(new Vectors(187, 570), Material.Solid, Constants.baseWidthW * 2, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(197, 550), Material.Solid, Constants.baseWidthW, Constants.baseHeight, "Images/snow.png"));
		map.add(new MyObject(new Vectors(207, 530), Material.Solid, Constants.baseWidth, Constants.baseHeight, "Images/snow.png"));
		
		// icicles
		map.add(new MyObject(new Vectors(391, 120), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/snow.png"));
		map.add(new MyObject(new Vectors(512, 60), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/snow.png"));
		map.add(new MyObject(new Vectors(594, 100), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/snow.png"));
		map.add(new MyObject(new Vectors(874, 80), Material.Solid, Constants.baseWidth, Constants.baseHeightL, "Images/snow.png"));
		return map;
	}
	
	final public static NavigationGraph alphaWorld(){
		NavigationGraph graph = new NavigationGraph();
		graph.add(beta0);
		graph.add(beta1);
		graph.add(beta2);
		graph.add(beta3);
		graph.add(beta4);
		graph.add(beta5);
		graph.addEdge(beta0, beta4);
		graph.addEdge(beta0, beta1);
		graph.addEdge(beta1, beta2);
		graph.addEdge(beta2, beta3);
		return graph;
	}
	
}
