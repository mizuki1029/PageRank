
/*
 * This program follows the steps below:
 * 1. read lines from idols.txt and store the name
 * 2. read lines from pages.txt and create vertexes
 * 3. read lines from links.txt and add edges
 * 4. point passing
 * 5. decide Top 10
 * 6. repeat (3. & 4. ) several times
 */
import java.util.*;
import java.io.*;

public class GirlIdolGroupElection {
	
	static Vector<Page> page = new Vector<Page>(); //store pages
	static Vector<String> idol = new Vector<String>(); //store pages
	static Vector<Integer> idolIndex = new Vector<Integer>(); //store pages
	static int pageNum = 0; //number of pages
	
	/*set of values*/
	final static double initialPoint = 1000.0; //give initialPoint to all pages at first
	final static int bestX = 20; //decide Top X(10)
	final static double rate = 0.75; //give (rate * my_point / number of links) to link destinations
	final static int numOfAttempts = 20; // point passing will be held 20 times
	
	public static void main(String[] args) {
		setGraphStructure(); //create vertexes and edges
		System.out.println("Set Graph Structure!");
		/* 6. repeat (3.point passing & 4.decide Top 10) several times */
		for(int times = 1; times <= numOfAttempts; times++){
			pointPassing(); //divide point between link destinations
			System.out.println("Point Passing!");
			checkImportantPage(times); //find important pages and display them
		}

	}
	
	/*
	 * 1. read lines from idols.txt and store the name
	 * 2. read lines from pages.txt and create vertexes
	 * 3. read lines from links.txt and add edges
	 */
	public static void setGraphStructure() {
		/*1. read lines from idols.txt and store the name*/
		try {
			File file = new File("idols.txt");
			/* exception when the file not exit */
			if (!file.exists()) {
				System.out.print("File Not Exit");
				return;
			}
			/* read lines from links.txt */
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				idol.add(str);
			}
			System.out.println("finished to read idols.txt");
			fileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*2. read lines from pages.txt and create vertexes*/
		try {
			File file = new File("pages.txt");
			/* exception when the file not exit */
			if (!file.exists()) {
				System.out.print("File Not Exit");
				return;
			}
			/* read lines from pages.txt */
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				StringTokenizer cut = new StringTokenizer(str, "	");
				int index = Integer.parseInt(cut.nextToken());
				String name = cut.nextToken();
				/* create vertexes */
				page.add(Page.addPage(name, initialPoint, index));
				if(idol.contains(name)){
					idolIndex.add(index);
				}
				pageNum=index;
			}
			System.out.println("finished to read pages.txt");
			fileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*3. read lines from links.txt and add edges*/
		try {
			File file = new File("links.txt");
			/* exception when the file not exit */
			if (!file.exists()) {
				System.out.print("File Not Exit");
				return;
			}
			/* read lines from links.txt */
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				StringTokenizer cut = new StringTokenizer(str, "	");
				int index = Integer.parseInt(cut.nextToken());
				/* create edges */
				int LinkIndex = Integer.parseInt(cut.nextToken());
				page.get(index).addLink(page.get(LinkIndex));
			}
			System.out.println("finished to read links.txt");
			fileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 4. point passing
	 */
	public static void pointPassing(){
		for(int index=0; index<page.size(); index++){
			int linkNum = page.get(index).getLinkNum(); //get the number of links
			double point = page.get(index).getPoint(); //get my point
			Vector<Page> links = page.get(index).getLinks(); //get link destinations
			
			/*divide point between link destinations*/
			for(int mylink=0; mylink<linkNum; mylink++){
				int LinkIndex = links.get(mylink).getIndex();//get index 
				page.get(LinkIndex).addPointStore((rate*point)/linkNum); //store temporarily because point passing will be held at once
			}
		}
		for(int index=0; index<page.size(); index++){
			//ex) 0.2*(pageA_point + pageB_point + ...)/pageNum = 0.2*initialPoint
			double AddPoint = page.get(index).getPointStore()+((1.0-rate)*initialPoint);
			page.get(index).setPoint(AddPoint);
			page.get(index).InitializePointStore(); //pointscore = 0;
		}
	}
	
	/*
	 *5. decide Top 10 
	 */
	public static void checkImportantPage(int times){
		List <Double>goodPoint = new ArrayList<Double>(); //store Top10 point
		Map <Double, Integer>goodPages = new TreeMap<Double, Integer>(); // store Top10 point and page
		double point = 0;
		for(int index=0; index<page.size(); index++){
			if(idolIndex.contains(index)){
				point = page.get(index).getPoint();
				if(goodPages.size()<bestX){
					goodPoint.add(point);
					goodPages.put(point, index);
				}
				else if(point>goodPoint.get(bestX-1)){
					goodPages.remove(goodPoint.get(bestX-1));//remove the lowest
					goodPoint.remove(bestX-1);//remove the lowest
					goodPoint.add(point);
					goodPages.put(point, index);
					Collections.sort(goodPoint, Collections.reverseOrder());//sort
				}
			}
		}
		System.out.println("----------------"+times+"----------------");
		for(int i = 0; i<goodPages.size(); i++){
			int pageIndex = goodPages.get(goodPoint.get(i));
			String pageName = page.get(pageIndex).getName();
			System.out.println("Point = "+page.get(pageIndex).getPoint()+" "+pageName);
		}
		System.out.println("----------------"+times+"----------------");
		System.out.println("");
	}
}

class Page {
	String name;
	double point;
	double pointstore;
	int index;
	Vector<Page> links;
	
	/*constructor*/
	private Page(String name, double point, int index) {
		this.name = name;
		this.point = point;
		this.index = index;
		links = new Vector<Page>();
	}
	
	/*this method is called to instantiate in other Class*/
	public static Page addPage(String name, double point, int index) {
		return new Page(name, point, index);
	}
	
	public double getPoint(){
		return point;
	}
	
	public void setPoint(double point){
		this.point = point;
	}
	
	public double getPointStore(){
		return pointstore;
	}
	
	public void addPointStore(double addpoint){
		pointstore = add(pointstore, addpoint);
	}
	
	private double add(double num1, double num2){
		return num1 + num2;
	}
	
	public void InitializePointStore(){
		pointstore = 0;
	}
	
	public int getLinkNum(){
		return links.size();
	}
	
	public String getName() {
		return name;
	}
	
	public int getIndex() {
		return index;
	}
	
	/*add edges*/
	public void addLink(Page link) {
		links.addElement(link);
	}

	public Vector <Page> getLinks() {
		return links;
	}

}
