import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;

public class Rectangles {

	static boolean screenCreated = false;
	static String[] words, dim = new String[1000];
	static Screen screen;
	static List <Rectangle> recList = new ArrayList<Rectangle>();
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String myString;
		
		System.out.println("Welcome! This application allows you to do different actions with rectangles.\n");
		printMenu();
			
		do {
			myString = scanner.nextLine();
			myString = myString.toLowerCase();
			
			if (myString.startsWith("create screen")) {
				
				if (screenCreated) {
					System.out.println("You've already created a screen!");
					continue;
				}
					
				int w, h;
				w = h = 0;
				
				try {
					words = myString.split(" ");
					dim = words[2].split("x");
					w = extractValue(dim[0]);
					h = extractValue(dim[1]);
				} catch (IndexOutOfBoundsException e) {
					System.out.println("PLEASE USE A VALID COMMAND FOR CREATING A SCREEN!");
					continue;
				}
				
				if (w == -1 || h == - 1) {
					System.out.println("PLEASE USE A VALID COMMAND FOR CREATING A SCREEN!");
					continue;
				}
				
				screen = new Screen(w, h);
				screenCreated = true;
				
				System.out.printf("You succesfully created a screen with dimensions %dx%d!\n", screen.w, screen.h);
				
			}
			
			else if (myString.startsWith("create rec")) {
				
				if (!screenCreated) {
					System.out.println("YOU MUST FIRST CREATE A SCREEN!");
					continue;
				}
				
				int w , h, x0, y0;
				w = h = x0 = y0 = 0;
				String col;
				
				try {
					words = myString.split(" ");
					dim = words[2].split("x");
					w = extractValue(dim[0]);
					h = extractValue(dim[1]);
					words[3] = words[3].substring(1, words[3].length() - 1);
					words[4] = words[4].substring(0, words[4].length() - 1);
					x0 = extractValue(words[3]);
					y0 = extractValue(words[4]);
					col = words[5];
				} catch (IndexOutOfBoundsException e) {
					System.out.println("PLEASE USE A VALID COMMAND FOR CREATING A RECTANGLE!");
					continue;
				}
				
				if (w == -1 || h == - 1 || x0 == -1 || y0 == -1) {
					System.out.println("PLEASE USE A VALID COMMAND FOR CREATING A RECTANGLE!");
					continue;
				}
				
				Rectangle rec = new Rectangle(w, h, x0, y0, col);
				Rectangle screenRec = new Rectangle(screen.w, screen.h, 0, 0);
				
				if (isIncluded(screenRec, rec)) {
					recList.add(rec);
					System.out.println("The rectangle was succesfully added to the screen!");
				}
				else
					System.out.println("The rectangle couldn't be added because it was out of the screen's bounds!");
			}
			
			else if (myString.startsWith("delete rec")) {
				
				int pos = 0;
				
				try {
					words = myString.split(" ");
					pos = extractValue(words[2]);
				} catch (IndexOutOfBoundsException e) {
					System.out.println("PLEASE USE A VALID COMMAND FOR DELETING A RECTANGLE!");
					continue;
				}
				
				if (pos == -1) {
					System.out.println("PLEASE USE A VALID COMMAND FOR DELETING A RECTANGLE!");
					continue;
				}
				
				if (!isOnScreen(pos - 1)) {
					System.out.println("THE INDEX IS OUT OF BOUNDS!");
					continue;
				}
				
				recList.remove(pos - 1);
				System.out.printf("The ");
				theNth(pos);
				System.out.printf("rectangle has been deleted succesfully!");
			}
			
			else if (myString.startsWith("print rec")) {
			
				int pos = 0;
				
				try {
					words = myString.split(" ");
					pos = extractValue(words[2]);
				} catch (IndexOutOfBoundsException e) {
					System.out.println("PLEASE USE A VALID COMMAND FOR PRINTING INFO A RECTANGLE!");
					continue;
				}
				
				if (pos == -1) {
					System.out.println("PLEASE USE A VALID COMMAND FOR PRINTING INFO A RECTANGLE!");
					continue;
				}
				
				if (!isOnScreen(pos - 1)) {
					System.out.println("THE INDEX IS OUT OF BOUNDS!");
					continue;
				}
				
				rectangleInfo(pos - 1);
			}
			
			else if (myString.startsWith("dump screen")) {
				dumpScreen();
			}
			
			else if (myString.startsWith("clear screen"))
				clearScreen();
			
			else if (myString.startsWith("included rec")) {
				
				int pos2, pos1;
				pos2 = pos1 = 0;
				
				try {
					words = myString.split(" ");
					pos2 = extractValue(words[2]);
					pos1 = extractValue(words[5]);
				} catch (IndexOutOfBoundsException e) {
					System.out.println("PLEASE USE A VALID COMMAND FOR CHECKING AN INCLUSION!");
					continue;
				}
				
				if (pos2 == -1 || pos1 == -1 || !words[3].equals("in") || !words[4].startsWith("rec")) {
					System.out.println("PLEASE USE A VALID COMMAND FOR CHECKING AN INCLUSION!");
					continue;
				}
				
				if (!isOnScreen(pos2 - 1) || !isOnScreen(pos1 - 1)) {
					System.out.println("THE INDEXES ARE OUT OF BOUNDS!");
					continue;
				}
				
				System.out.printf("The ");
				
				if (isIncluded(recList.get(pos1 - 1), recList.get(pos2 - 1))) {
					theNth(pos2);
					System.out.printf("rectangle is INCLUDED in the ");
					theNth(pos1);
					System.out.printf("rectangle!\n");
				}
				else {
					theNth(pos2);
					System.out.printf("rectangle is NOT included in the ");
					theNth(pos1);
					System.out.printf("rectangle!\n");
				}
				
			}
					
			else if (myString.startsWith("intersected rec")) {
				
				int pos1, pos2;
				pos1 = pos2 = 0;
				
				try {
					words = myString.split(" ");
					pos1 = extractValue(words[2]);
					pos2 = extractValue(words[5]);
				} catch (IndexOutOfBoundsException e) {
					System.out.println("PLEASE USE A VALID COMMAND FOR CHECKING AN INTERSECTION!");
					continue;
				}
				
				if (pos1 == -1 || pos2 == -1 || !words[3].equals("and") || !words[4].startsWith("rec")) {
					System.out.println("PLEASE USE A VALID COMMAND FOR CHECKING AN INTERSECTION!");
					continue;
				}
				
				if (!isOnScreen(pos1 - 1) || !isOnScreen(pos2 - 1)) {
					System.out.println("THE INDEXES ARE OUT OF BOUNDS!");
					continue;
				}
				
				System.out.printf("The ");
				
				if (inIntersection(recList.get(pos1 - 1), recList.get(pos2 - 1))) {
					theNth(pos2);
					System.out.printf("rectangle and the ");
					theNth(pos1);
					System.out.printf("rectangle are INTERSECTED!\n");
				}
				else {
					theNth(pos2);
					System.out.printf("rectangle and the ");
					theNth(pos1);
					System.out.printf("rectangle are NOT intersected!\n");
				}
			}
			
			else if (myString.startsWith("move rec")) {
				
				int pos, pace;
				pos = pace = 0;
				
				try {
					words = myString.split(" ");
					pos = extractValue(words[2]);
					pace = extractValue(words[4]);
				} catch(IndexOutOfBoundsException e) {
					System.out.println("PLEASE USE A VALID COMMAND FOR MOVING A RECTANGLE");
					continue;
				}
				
				if (pos == -1 || pace == -1 || !words[3].equals("with") || !words[5].equals("pace")) {
					System.out.println("PLEASE USE A VALID COMMAND FOR MOVING A RECTANGLE");
					continue;
				}
				
				if (!isOnScreen(pos - 1)) {
					System.out.println("THE INDEXES ARE OUT OF BOUNDS!");
					continue;
				}
				
				Rectangle rec = recList.get(pos - 1);
				System.out.printf("The ");
				theNth(pos);
				System.out.printf("rectangle moved with %d pace in the following way from the position (%d, %d):\n", pace, rec.x0, rec.y0);
				recList.set(pos - 1, moveRec(rec, pace));
			}
			
			else if (myString.equals("menu")) {
				printMenu();
			}
			
			else if (!myString.equals("done"))
				System.out.println("INVALID COMMAND!");
			
			
		} while (!myString.equals("done"));
		
		scanner.close();
		
	}

	public static boolean isOnScreen(int pos) {
		if (pos < 0 || pos >= recList.size()) {
			return false;
		}
		return true;
	}
	
	public static void theNth(int pos) {
		if (pos == 1)
			System.out.printf("1st ");
		else if (pos == 2)
			System.out.printf("2nd ");
		else if (pos == 3)
			System.out.printf("3rd ");
		else
			System.out.printf("%dth ", pos);
	}
	
	public static void printMenu() {
		System.out.println("COMMANDS");
		for (int i = 0; i < 8; i++)
			System.out.printf("-");
		System.out.println('\n');
		System.out.println("Creating a screen:\n'create screen widthXheight'\n");
		System.out.println("Creating a rectangle:\n'create rectangle widthXheight (x0, y0) colour'\n");
		System.out.println("Deleting a rectangle from the screen:\n'delete rec index_of_rectangle'\n");
		System.out.println("Deleting all the rectangles from the screen:\n'clear screen'\n");
		System.out.println("Printing all the rectangles from the screen:\n'dump screen'\n");
		System.out.println("Checking if two rectangles intersect:\n'intersected rectangle index_of_rectangle and rectangle index_of_rectangle'\n");
		System.out.println("Checking if the first given rectangle contains the second one:\n'included rectangle index_of_rectangle in rectangle index_of_rectangle'\n");
		System.out.println("Moving a rectangle with a given pace to the bottom right corner of the screen:\n'move rectangle index_of_rectangle with value_of_pace pace'\n");
		System.out.println("Showing the commands:\n'menu'\n");
		System.out.println("Closing the application:\n'done'\n");
	}
	
	public static void clearScreen() {

		recList.clear();
		System.out.println("The screen was succesfully cleared of rectangles!");
	}
	
	public static void rectangleInfo(int pos) {
		if (!isOnScreen(pos)) {
			System.out.println("The given position is out of bounds!");
			return;
		}
		Rectangle rec = recList.get(pos);
		System.out.printf("The ");
		theNth(pos + 1);
		System.out.printf("rectangle has ");
		System.out.printf("dimensions (WxH): %dx%d, upper-left corner (X, Y) = (%d, %d), colour: %s\n", rec.w, rec.h, rec.x0, rec.y0, rec.col);
	}

	public static void dumpScreen() {
		if (recList.isEmpty()) {
			System.out.println("The screen is empty!");
			return;
		}
		System.out.printf("There are %d rectangles on the screen:\n", recList.size());
		for (int i = 0; i < recList.size(); i++)
			rectangleInfo(i);
		}
	
	public static int extractValue(String myString) {
		
		int x = 0;
		
		for (int i = 0; i < myString.length(); i++) {
			char c = myString.charAt(i);
			if (c >= '0' && c <= '9')
				x = x * 10 + myString.charAt(i) - '0';
			else
				return -1;
		}
			
		return x;
	}
	
	public static boolean isIncluded(Rectangle rec1, Rectangle rec2) {

		if (rec2.w == 1 && rec2.h == 1) {
			if ((rec2.x0 == rec1.x0 || rec2.x0 == rec1.x0 + rec1.w) && (rec2.y0 >= rec1.y0 && rec2.y0 <= rec1.y0 + rec1.h))
				return true;
			if ((rec2.y0 == rec1.y0 || rec2.y0 == rec1.y0 + rec1.h) && (rec2.x0 >= rec1.x0 && rec2.x0 <= rec1.x0 + rec1.w))
				return true;
		}
		if (rec2.y0 < rec1.y0 || rec2.x0 > rec1.x0 + rec1.w || rec2.y0 > rec1.y0 + rec1.h || rec2.x0 < rec1.x0)
			return false;
		if (rec2.x0 + rec2.w > rec1.x0 + rec1.w || rec2.y0 + rec2.h > rec1.y0 + rec1.h)
			return false;
		return true;
		
	}
	
	public static boolean inIntersection(Rectangle rec1, Rectangle rec2) {
		if ((rec2.x0 < rec1.x0) || (rec2.x0 == rec1.x0 && rec2.y0 < rec1.y0)) {
			Rectangle aux = rec1;
			rec1 = rec2;
			rec2 = aux;
		}
		
		if ((rec2.x0 >= rec1.x0 && rec2.x0 <= rec1.x0 + rec1.w) && (rec2.y0 >= rec1.y0 && rec2.y0 <= rec1.y0 + rec1.h))
			return true;
		if ((rec2.y0 < rec1.y0) && (rec2.x0 >= rec1.x0 && rec2.x0 <= rec1.x0 + rec1.w) && (rec2.y0 + rec2.h >= rec1.y0))
			return true;
		if ((rec2.x0 < rec1.x0) && (rec2.y0 >= rec1.y0 && rec2.y0 <= rec1.y0 + rec1.h) && (rec2.x0 + rec2.w >= rec1.x0))
			return true;
		return false;
	}
	
	public static Rectangle moveRec(Rectangle rec, int d) {
		
		int cnt = 1;
		
		while (rec.x0 + rec.w < screen.w || rec.y0 + rec.h < screen.h) {
			
			if (rec.x0 + rec.w + d >= screen.w)
				rec.x0 = screen.w - rec.w;
			else
				rec.x0 += d;
			
			if (rec.y0 + rec.h + d >= screen.h)
				rec.y0 = screen.h - rec.h;
			else
				rec.y0 += d;
			
			System.out.printf("Step %d: (%d, %d)\n", cnt++, rec.x0, rec.y0);
		}
	
		if (cnt == 1)
			System.out.println("The rectangle was already in the bottom-left corner of the screen!");
		
		return rec;
	}
	
}
