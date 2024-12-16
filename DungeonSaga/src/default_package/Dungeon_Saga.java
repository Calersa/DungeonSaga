package default_package;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.Timer;

public class Dungeon_Saga {
	public static void main(String[] args) {
		System.out.println("Starting up...");
		Dungeon_Saga game = new Dungeon_Saga();

		game.GUI();
		game.Content();

	}

	// Shared Values
	static Font mainFont = new Font("DejaVu Serif", Font.CENTER_BASELINE, 16);
	Font titleFont = new Font("DejaVu Serif", Font.PLAIN, 80);
	Font normalFont = new Font("DejaVu Serif", Font.PLAIN, 30);

	static JFrame window;
	static JPanel buttonPanel;
	JPanel titlePanel;
	JPanel startPanel;
	static JLabel imageLabel;
	JLabel titleName;
	JPanel textPanel;
	BackgroundPanel backgroundPanel;

	static String commands[];
	static String roomImagePath;

	static HashMap<JButton, JPopupMenu> buttonMenus = new HashMap<>();
	static HashMap<String, String[]> menuItems = new HashMap<>();

	static JTextArea outputField = new JTextArea(3, 20);
	static JTextField inputField = new JTextField();
	static String userInput;
	static Boolean actionPerformed = false;

/////////////////////////////////////////////		GUI START		////////////////////////////////////////////////////////////////////

	// Window Settings
	public void GUI() {
		System.out.println("GUI Running");
		Image icon = Toolkit.getDefaultToolkit().getImage("Icon.png");
		window = new JFrame();
		window.setTitle("Dungeon Saga");
		window.setSize(700, 700);
		window.getContentPane().setBackground(Color.BLACK);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setIconImage(icon);
		window.setVisible(true);
		window.setResizable(false);

		TitleScreen();
	}

	// Title Screen Settings
	public void TitleScreen() {

		backgroundPanel = new BackgroundPanel();
		backgroundPanel.setBounds(0, 0, 800, 600);
		backgroundPanel.setLayout(null);

		/*
		 * titlePanel = new JPanel(); titlePanel.setBounds(100, 100, 600, 150);
		 * titlePanel.setBackground(Color.black); titleName = new
		 * JLabel("Dungeon Saga"); titleName.setForeground(Color.red);
		 * titleName.setFont(titleFont);
		 */

		/*
		 * startPanel = new JPanel(); startPanel.setBounds(325, 400, 100, 50);
		 * startPanel.setBackground(Color.black);
		 */

		JButton startButton;
		startButton = new JButton("START");
		// startButton.setBackground(Color.black);
		startButton.setForeground(Color.white);
		startButton.setFont(normalFont);
		startButton.setBackground(Color.black);
		// startButton.setOpaque(false);
		// startButton.setContentAreaFilled(false);
		// startButton.setBorderPainted(false);
		startButton.setBorder(new LineBorder(Color.black, 10));
		startButton.setBounds(275, 400, 150, 50);

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.remove(backgroundPanel);
				window.revalidate();
				MainScreen();
			}
		});

		// titlePanel.add(titleName);
		// startPanel.add(startButton);
		// backgroundPanel.add(titlePanel);
		backgroundPanel.add(startButton);
		window.add(backgroundPanel);

		window.setVisible(true);
	}

	class BackgroundPanel extends JPanel {
		private Image backgroundImage;

		public BackgroundPanel() {
			backgroundImage = new ImageIcon("TitleScreen.png").getImage(); // Title_Card
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
			g.setFont(titleFont);
			g.setColor(Color.red);
			g.drawString("Dungeon Saga", 50, 150);
		}
	}

	// Main Screen Settings
	public void MainScreen() {

		// Initial Launch Values
		commands = new String[] {"Nothing yet..."};
		roomImagePath = "Room01.png";

		String[] buttonImagePaths = { "Commands.png", "Quit.png" };
		String textOutput = ("You have made it to the dungeon, before you is a stone laid, dark room that seemingly hasn't been touched in ages, with a table on which lies a KEY. Ahead of you is a DOOR, which you assume leads further into the dungeon. \n(To interact with items, type in the uppercase word for the item and press enter, then type the number associated with the action.)");

		menuItems.put("Commands.png", commands);
		menuItems.put("Inventory.png", new String[] { "Button1", "Button2", "Button3", "Button4" });

		topButtonsWithDropdown(buttonImagePaths);
		roomImage(roomImagePath);
		textBox(textOutput);

		window.add(buttonPanel, BorderLayout.NORTH);
		window.add(imageLabel, BorderLayout.CENTER);
		window.add(textPanel, BorderLayout.SOUTH);

		window.revalidate();

	}

	private static JPopupMenu createCustomMenu(String[] items) {
		JPopupMenu menu = new JPopupMenu();
		menu.setBackground(Color.BLACK);
		menu.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		for (String item : items) {
			JMenuItem menuItem = new JMenuItem(item);
			menuItem.setFont(mainFont);
			menuItem.setForeground(Color.WHITE);
			menuItem.setBackground(Color.BLACK);
			menuItem.addActionListener(e -> {
				System.out.println("Selected: " + item);
				//ActionEvent simulatedEvent = new ActionEvent(inputField, ActionEvent.ACTION_PERFORMED, "Enter");
		        //inputField.getActionListeners()[0].actionPerformed(simulatedEvent);
			});

			menuItem.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					menuItem.setBackground(Color.DARK_GRAY);
				}

				public void mouseExited(MouseEvent e) {
					menuItem.setBackground(Color.BLACK);
				}
			});

			menu.add(menuItem);
		}
		return menu;
	}

	public void topButtonsWithDropdown(String[] imagePaths) {
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.BLACK);

		for (String path : imagePaths) {
			ImageIcon icon = new ImageIcon(path);
			int width = icon.getIconWidth();
			int height = icon.getIconHeight();

			JButton button = new JButton(icon);
			button.setPreferredSize(new Dimension(width, height));
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);

			ImageIcon darkIcon = new ImageIcon(darkenImage(icon.getImage()));

			// Handle different button types
			if (path.equals("Commands.png") || path.equals("Inventory.png")) {
				JPopupMenu menu = createCustomMenu(menuItems.get(path));
				buttonMenus.put(button, menu);

				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JPopupMenu menu = buttonMenus.get(button);
						if (menu.isVisible()) {
							menu.setVisible(false);
						} else {
							buttonMenus.values().forEach(m -> m.setVisible(false));
							menu.show(button, 0, button.getHeight());
						}
					}
				});
			} else if (path.equals("Quit.png")) {
				// Add custom quit functionality
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Create a custom dialog similar to the popup menu
						JDialog quitDialog = new JDialog(window, "Quit Game", true);
						quitDialog.getContentPane().setBackground(Color.BLACK);

						JPanel dialogPanel = new JPanel();
						dialogPanel.setBackground(Color.BLACK);
						dialogPanel.setLayout((LayoutManager) new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

						// Create confirmation menu item
						JMenuItem confirmItem = new JMenuItem("Are you sure you want to quit?");
						confirmItem.setFont(mainFont);
						confirmItem.setForeground(Color.WHITE);
						confirmItem.setBackground(Color.BLACK);
						confirmItem.setEnabled(false); // Make it look like a label
						dialogPanel.add(confirmItem);

						// Yes menu item
						JMenuItem yesItem = new JMenuItem("Yes");
						yesItem.setFont(mainFont);
						yesItem.setForeground(Color.WHITE);
						yesItem.setBackground(Color.BLACK);
						yesItem.addActionListener(quitAction -> {
							System.exit(0);
						});
						yesItem.addMouseListener(new MouseAdapter() {
							public void mouseEntered(MouseEvent e) {
								yesItem.setBackground(Color.DARK_GRAY);
							}

							public void mouseExited(MouseEvent e) {
								yesItem.setBackground(Color.BLACK);
							}
						});
						dialogPanel.add(yesItem);

						// No menu item
						JMenuItem noItem = new JMenuItem("No");
						noItem.setFont(mainFont);
						noItem.setForeground(Color.WHITE);
						noItem.setBackground(Color.BLACK);
						noItem.addActionListener(closeAction -> {
							quitDialog.dispose();
						});
						noItem.addMouseListener(new MouseAdapter() {
							public void mouseEntered(MouseEvent e) {
								noItem.setBackground(Color.DARK_GRAY);
							}

							public void mouseExited(MouseEvent e) {
								noItem.setBackground(Color.BLACK);
							}
						});
						dialogPanel.add(noItem);

						// Add border to match popup menu style
						dialogPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));

						quitDialog.add(dialogPanel);
						quitDialog.pack();
						quitDialog.setLocationRelativeTo(window);
						quitDialog.setVisible(true);
					}
				});
			} else {
				// Regular action listener for other buttons
				button.addActionListener(e -> System.out.println("Button clicked: " + path));
			}

			// Mouse hover effect
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					button.setIcon(darkIcon);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					button.setIcon(icon);
				}
			});

			buttonPanel.add(button);
		}

		// click listener to window to close menus when clicking outside
		window.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Component clickedComponent = SwingUtilities.getDeepestComponentAt(window.getContentPane(), e.getX(),
						e.getY());
				if (!(clickedComponent instanceof JMenuItem)) {
					buttonMenus.values().forEach(menu -> menu.setVisible(false));
				}
			}
		});
	}

	private Image darkenImage(Image img) {
		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D overlayBlack = bufferedImage.createGraphics();
		overlayBlack.drawImage(img, 0, 0, null);
		overlayBlack.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		overlayBlack.setColor(Color.BLACK);
		overlayBlack.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		overlayBlack.dispose();

		return bufferedImage;
	}

	public void roomImage(String imagePath) {
		ImageIcon picture = new ImageIcon(imagePath);
		imageLabel = new JLabel(picture);
	}

	public void textBox(String textOutput) {
		textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout());

		// Create the outputField for displaying text
		outputField.setFont(mainFont);
		outputField.setText(textOutput);
		outputField.setEditable(false);
		outputField.setLineWrap(true);
		outputField.setWrapStyleWord(true);
		outputField.setForeground(Color.WHITE);
		outputField.setBackground(Color.BLACK);
		outputField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		textPanel.add(outputField, BorderLayout.CENTER);

		// Create the inputField where user types text
		inputField.setFont(mainFont);
		inputField.setForeground(Color.WHITE);
		inputField.setBackground(Color.BLACK);
		inputField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		textPanel.add(inputField, BorderLayout.SOUTH);

		inputField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = inputField.getText();
				System.out.println("Text entered: " + input);
				input = input.toLowerCase();

				inputField.setText("");

				userInput = input;

				actionPerformed = true;
			}
		});
	}

	// Update command menu
	private static void commandUpdate() {
		JButton commandsButton = null;

		// Add current commands array
		menuItems.put("Commands.png", commands);

		// Find button
		for (Component comp : buttonPanel.getComponents()) {
			if (comp instanceof JButton) {
				JButton button = (JButton) comp;
				ImageIcon icon = (ImageIcon) button.getIcon();
				if (icon != null && icon.getDescription().equals("Commands.png")) {
					commandsButton = button; // Assign the matching button
					break; // Exit the loop once the button is found
				}
			}
		}

		// Update Button if found
		if (commandsButton != null) {
			// Create a new menu with the updated commands
			JPopupMenu newMenu = createCustomMenu(menuItems.get("Commands.png"));
			buttonMenus.put(commandsButton, newMenu); // Update the menu for the button
		} else {
			System.out.println("Commands button not found!");
		}
	}

	// Update image
	public static void updateRoomImage(String newImagePath) {
		// Change the image path
		roomImagePath = newImagePath;

		// Create a new ImageIcon with the new image path
		ImageIcon newImage = new ImageIcon(roomImagePath);

		// Update the image displayed in the imageLabel
		imageLabel.setIcon(newImage);

		// Revalidate and repaint to update the GUI
		window.revalidate(); // This updates the layout of the window
		window.repaint(); // This repaints the window to reflect the changes
	}

	// Waits for and Outputs User Input
	public static String userInputDelay() {
		// Wait until the action is performed
		while (!actionPerformed) {
			try {
				Thread.sleep(100); // Wait for 100 milliseconds
			} catch (InterruptedException ex) {
				// Handle interruption
			}
		}

		actionPerformed = false;
		return userInput;
	}

	/*////////////////////////////////////////////		GUI END		////////////////////////////////////////////////////////////////////

	NOTICE: Around line 320, inputField.addActionListener, there are EXAMPLE USES of the 
	methods that will execute when ENTER is hit in the input box.
	These methods are meant to be used in room objects. 
	Delete example if not desired.

	NOTICE: Certain thinks in the first room are declared directly in the GUI due to 
	ordering of startup. The first room WILL be structured slightly different.

	//Take User Input ( Replaces user.next(), NOT the whole line )
		            userInputDelay();
		            		           
	//Display in Main Text Box ( Replaces System.out.print() )
		            outputField.setText();
		            
	//Change Room Image ( Execute as one of the first things your room does )
					updateRoomImage("Room99.png");    
					
	//Update Command Menu ( Do NOT forget the command updater )
		            1. For your display of actions, create an ArrayList<String>          
		            2. In the forEach loop, "ArrayList.add("- " + k + " + v);
		            3. outside of the forEach loop, 
		            commands = new String[(ArrayList.size()];
		            for(int i = 0; i < ArrayList.size(); i++){
		               commands[i] = ArrayList.get(i);
		            )
		            commandUpdate();
		            
	*/////////////////////////////////////////////		ROOMS START		////////////////////////////////////////////////////////////////////
	
	private void Content() {
		String currentRoom = "room1";
		while (!currentRoom.equals("roomEnd")) {
			switch (currentRoom) {
			case "room1":
				currentRoom = Room1();
				break;
			case "room2":
				currentRoom = Room2();
				break;
			case "room3":
				currentRoom = Room3();
				break;
			case "room4":
				currentRoom = Room4();
				break;
			case "room5":
				currentRoom = Room5();
				break;
			case "room6":
				currentRoom = Room6();
			}
		}
		updateRoomImage("RoomFinal.png");
		outputField
				.setText("You enter one final room, within is a portal of swirling, inky black. What may it contain? "
						+ "You reach forward, feeling the thrill of the rest of your adeventure close to your fingertips.  "
						+ "This has been Dungeon Saga's first dungeon. \nThank you from our Dev Team for playing, "
						+ "we hope to see you in further adventures!");

	}

	public static class interactable {
		private String description; // The word that will begin interacting with an item.
		private HashMap<String, String> actions;

		public interactable(String desc) {
			this.description = desc;
			this.actions = new HashMap<String, String>();
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String desc) {
			this.description = desc;
		}

		public HashMap getActions() {
			return actions;
		}

		public void setActions(HashMap actions) {
			this.actions = actions;
		}

		public void addAction(String action, String result) {
			this.actions.put(action, result);
		}

	}

	public static class room {
		private String description;
		private String img;
		private ArrayList<interactable> interactions = new ArrayList<>();

		public room(String description, String img, ArrayList interactions) {
			this.description = description;
			this.img = img;
			this.interactions = interactions;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

		public void addInteractable(interactable interactable) {
			interactions.add(interactable);
		}

		public void setInteractions(ArrayList interactables) {
			this.interactions = interactables;
		}

		public ArrayList<interactable> getInteractions() {
			return interactions;
		}
	}

	public static String Room1() {
		// Creation of room one.
		ArrayList<interactable> IList = new ArrayList<interactable>();

		// Make Key Interactable and add to interactable list
		String tempIdescription = "The key is a mixture of yellow and orange, giving the impression that it is made out of gold or bronze, though you cannot say for sure.";
		IList.add(new interactable(tempIdescription));
		IList.get(0).getActions().put("1", "Pick up the key");
		IList.get(0).getActions().put("2", "Back");

		// Make Door interactable and add to interactable list
		tempIdescription = "This door appears to be made of solid wood, despite its age the iron bars that hold the wood seem to have avoided rusting away. There is a keyhole above the knob.";
		IList.add(new interactable(tempIdescription));
		IList.get(1).getActions().put("1", "Try the Door handle.");
		IList.get(1).getActions().put("2", "KICK THE DOOR!");
		IList.get(1).getActions().put("3", "Back");

		String Rdescription = "You have made it to the dungeon, before you is a stone laid, dark room that seemingly hasn't been touched in ages, with a table on which lies a KEY. Ahead of you is a DOOR, which you assume leads further into the dungeon. \n(To interact with items, type in the uppercase word for the item and press enter, then type the number associated with the action.)";
		String Rimg = "Room01.png";
		room x = new room(Rdescription, Rimg, IList);

		char haskey = 'n';
		outputField.setText(x.getDescription());
		String input = " ";

		while (!input.equals("key") || !input.equals("door")) {
			input = userInputDelay();
			switch (input) {
			case "key":
				outputField.setText(x.getInteractions().get(0).getDescription());
				ArrayList<String> commandsList = new ArrayList<String>();
				x.getInteractions().get(0).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});
				commands = new String[commandsList.size()];
				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();
				while (!x.getInteractions().get(0).getActions().containsKey(input)) {

					input = userInputDelay();

					switch (input) {
					case "1":
						outputField.setText("You obtained a key!");
						input = " ";
						haskey = 'y';
						x.getInteractions().get(1).getActions().put("4", "Put the key in the keyhole.");
						break;
					case "2":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;
					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}
				break;
			case "door":
				outputField.setText(x.getInteractions().get(1).getDescription());
				commandsList = new ArrayList<String>();
				x.getInteractions().get(1).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});
				commands = new String[commandsList.size()];
				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();

				while (!x.getInteractions().get(1).getActions().containsKey(input)) {

					input = userInputDelay();

					switch (input) {
					case "1":
						outputField.setText("You try the door handle, it jiggles but does not open.");
						input = " ";
						break;
					case "2":
						outputField.setText("You slam your foot into the solid wood of the door. . . Ouch.");
						input = " ";
						break;
					case "3":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;
					case "4":
						if (haskey == 'y') {
							outputField.setText("You unlock the door and head into the next room.");
							return "room2";
						} else {
							outputField.setText("You're a dirty little cheater, aren't you?");
							break;
						}
					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}
				break;
			default:
				outputField.setText("You can't do that here.");
				new javax.swing.Timer(3000, new ActionListener() {
	    	        @Override
	    	        public void actionPerformed(ActionEvent evt) {
	    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
	    	            outputField.setText(x.getDescription()); // Re-display the room description
	    	        }
	    	    }).start();
			}
		}

		// If this string is reached, the while loop terminated when it shouldn't have.
		return "You broke it.";
	}

	// Library Room - Room 2
	public static String Room2() {

		updateRoomImage("Room02.png");
		// Creation of room 2: library
		ArrayList<interactable> IList = new ArrayList<interactable>();
		// Make Bookshelf interactable and add to interactable list
		String tempIdescription = "This old bookshelf is barely standing, but a book catches your attention.";
		IList.add(new interactable(tempIdescription));
		IList.get(0).getActions().put("1", "Examine the book.");
		IList.get(0).getActions().put("2", "Back.");

		// Make Door interactable and add to interactable list
		tempIdescription = "A heavy wooden door stands firm, locked tight. The keyhole above the handle suggests the existance of a key.";
		IList.add(new interactable(tempIdescription));
		IList.get(1).getActions().put("1", "Try the door handle.");
		IList.get(1).getActions().put("2", "Kick the door.");
		IList.get(1).getActions().put("3", "Back.");

		// Room description and image
		String Rdescription = "You enter a dimly lit library with sagging bookshelves and piles of dust-covered tomes. One BOOKSHELF stands out form the others. To the right, a heavy wooden DOOR stands in your way, locked tight.";
		// Original room description
		String Odescription = Rdescription;
		// Room Description after key
		String Udescription = Rdescription
				+ " You notice something glinting on top of the BOOKSHELF where you read that book.";
		String Rimg = "Room02.png";

		room x = new room(Rdescription, Rimg, IList);

		// Running Room 2
		char hasKey = 'n'; // Found the key
		char book = 'n'; // Book read
		commands = new String[] {"Nothing yet..."};
		commandUpdate();
		outputField.setText(x.getDescription());
		String input = " ";

		while (!input.equals("bookshelf") || !input.equals("door")) {
			input = userInputDelay();

			switch (input) {
			case "bookshelf":
				outputField.setText(x.getInteractions().get(0).getDescription());
				ArrayList<String> commandsList = new ArrayList<String>();
				x.getInteractions().get(0).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});
				commands = new String[commandsList.size()];
				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();

				while (!x.getInteractions().get(0).getActions().containsKey(input)) {
					input = userInputDelay();
					switch (input) {
					case "1":
						outputField.setText(
								"You examine the book. It reads: 'The answer lies high above, where the steps lead to knowledge once sought.' The rest is illegible.");
						input = " ";
						book = 'y';
						x.getInteractions().get(0).getActions().put("3", "Inspect the glint at the top.");
						x.setDescription(Udescription);// Change room description after reading clue.
						break;
					case "2":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;
					case "3":
						if (book == 'y') {
							outputField.setText("You searched the top of the bookshelf. You have found the key.");
							input = " ";
							hasKey = 'y';
							x.getInteractions().get(1).getActions().put("4", "Put the key in the keyhole.");
							x.setDescription(Odescription); // Return to original description after finding key
							break;
						} else {
							outputField.setText("The path is out of harmony. Align your steps and try again.");
							break;
						}
					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}
				break;

			case "door":
				outputField.setText(x.getInteractions().get(1).getDescription());
				commandsList = new ArrayList<String>();
				x.getInteractions().get(1).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});
				commands = new String[commandsList.size()];
				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();

				while (!x.getInteractions().get(1).getActions().containsKey(input)) {
					input = userInputDelay();
					switch (input) {
					case "1":
						outputField.setText("You try the door handle, but it doesn't budge.");
						input = " ";
						break;
					case "2":
						outputField.setText("You kick the door. It wasn't very effective...");
						input = " ";
						break;
					case "3":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;
					case "4":
						if (hasKey == 'y') {
							outputField.setText("You unlock the door and proceed to the next room.");
							return "room3"; // CHANGE TO "room3"
						} else {
							outputField.setText("Having fun?.");
							break;
						}
					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}
				break;

			default:
				outputField.setText("You can't do that here.");
				new javax.swing.Timer(3000, new ActionListener() {
	    	        @Override
	    	        public void actionPerformed(ActionEvent evt) {
	    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
	    	            outputField.setText(x.getDescription()); // Re-display the room description
	    	        }
	    	    }).start();

			}
		}
		// String if while loop terminated when it shouldn't have.
		return "You broke it.";
	}

	// The Start of Room 3
	public static String Room3() {
		updateRoomImage("Room03.png");

		// Creation of room 3 Orb thrower.
		ArrayList<interactable> IList = new ArrayList<interactable>();

		// Make Orb Interact able and add to interact able list
		String tempIdescription = "A mysterious orb made of a durable glass-like substance that encases an ever swirling black mist. It feels powerful, but not powerful in a sense that you can take advantage of at the moment.";
		IList.add(new interactable(tempIdescription));
		IList.get(0).getActions().put("1", "Pick up the orb");
		IList.get(0).getActions().put("2", "Back");

		// Make Button interact able and add to interact able list
		tempIdescription = "Protruding from the wall behind the cage you can see an out-of-reach button on the wall. Your body can't through the iron bars, but the gap seems wide enough for those with trained accuracy.";
		IList.add(new interactable(tempIdescription));
		IList.get(1).getActions().put("1", "Reach through the bars.");
		IList.get(1).getActions().put("2", "Throw a rock at the Button");
		IList.get(1).getActions().put("3", "Back");

		// Making room description and image, then making room object
		String Rdescription = "Upon entering this room, you are made aware of the presence of an ornate pedestal in the center of the room that holds a rune covered accursed ORB atop it. "
				+ "To your right, there is a cage wall that prevents you from walking towards an important seeming BUTTON protruding from the wall.";
		// Original room description
		String Odescription = Rdescription;

		// Room Description after orb
		String Udescription = Rdescription + " Seems like all your days throwing stones might come in handy.";

		String Rimg = "Room03.png";

		room x = new room(Rdescription, Rimg, IList);

		// Running room three.
		char hasOrb = 'n';
		commands = new String[] {"Nothing yet..."};
		commandUpdate();
		outputField.setText(x.getDescription());
		Scanner user = new Scanner(System.in);
		String input = " ";

		while (!input.equals("orb") || !input.equals("button")) {

			input = userInputDelay();

			switch (input) {
			case "orb":
				outputField.setText(x.getInteractions().get(0).getDescription());
				ArrayList<String> commandsList = new ArrayList<String>();
				x.getInteractions().get(0).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});
				commands = new String[commandsList.size()];

				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();
				while (!x.getInteractions().get(0).getActions().containsKey(input)) {

					input = userInputDelay();

					switch (input) {
					case "1":
						outputField.setText("You have picked up the Orb of Caladon!");
						input = " ";
						hasOrb = 'y';
						x.getInteractions().get(1).getActions().put("4", "Throw the orb at the button.");
						break;
					case "2":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;
					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}

				break;

			case "button":
				outputField.setText(x.getInteractions().get(1).getDescription());
				commandsList = new ArrayList<String>();
				x.getInteractions().get(1).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});

				commands = new String[commandsList.size()];

				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();

				while (!x.getInteractions().get(1).getActions().containsKey(input)) {

					input = userInputDelay();

					switch (input) {
					case "1":
						outputField.setText("To no avail your arms simply aren't long enough to reach the button.");
						input = " ";
						break;

					case "2":
						outputField.setText(
								"The rock bounces off the button, but doesn't push it in far enough. You might need something more dense.");
						input = " ";
						break;

					case "3":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;

					case "4":

						if (hasOrb == 'y') {
							outputField.setText(
									"Upon throwing the orb at the button, you see the press activate a mechanism which makes an audible click in the far distance of the halls. The orb remains behind the bars of the cage. You unlock the door and head into the next room.");
							return "room4";
						}

						else {
							outputField.setText("You're a dirty little cheater, aren't you?");
							break;
						}

					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}

				break;

			default:
				outputField.setText("You can't do that here.");
				new javax.swing.Timer(3000, new ActionListener() {
	    	        @Override
	    	        public void actionPerformed(ActionEvent evt) {
	    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
	    	            outputField.setText(x.getDescription()); // Re-display the room description
	    	        }
	    	    }).start();
			}
		}

		// If this string is reached, the while loop terminated when it shouldn't have.
		return "Uh oh. Stinky. You broke it.";
	}

	// Start of Room 4 - Scales
	public static String Room4() {

		updateRoomImage("Room04.png");

		// Creation of Room 4: Scales
		ArrayList<interactable> IList = new ArrayList<>();

		// Scale interactable and add to interactable list
		String tempIdescription = "This scale is coated by a layer of dust. Engravings and patterns suggest it may have been more ceremonial than practival. The scale is imbalanced, an inscription is found on the base of the scale, it reads: 'Equilibrium opens the way.'";
		IList.add(new interactable(tempIdescription));
		IList.get(0).getActions().put("1", "Back.");

		// Door interactable and add to interactable list
		tempIdescription = "This door is made from iron, it lacks a handle or a keyhole, there must be some mechanism that opens it.";
		IList.add(new interactable(tempIdescription));
		IList.get(1).getActions().put("1", "Kick the door.");
		IList.get(1).getActions().put("2", "Back.");

		// Rocks interactable
		tempIdescription = "These rocks look out of place. They may have another use.";
		IList.add(new interactable(tempIdescription));
		IList.get(2).getActions().put("1", "Pick up rocks");
		IList.get(2).getActions().put("2", "Back.");

		// Room description and image
		String Rdescription = "You enter a shadowy chamber with a large balance SCALE at its center, the metal tarnished with age. On one side of the room, stands another DOOR, sealed with no keyhole or handle. The only other objects found in the room are some ROCKS.";
		String Rimg = "Room04.png";

		room x = new room(Rdescription, Rimg, IList);

		// Running room 4
		char rocks = 'n';

		commands = new String[] {"Nothing yet..."};
		commandUpdate();
		outputField.setText(x.getDescription());
		String input = " ";

		while (!input.equals("scale") || !input.equals("door") || !input.equals("rocks")) {
			input = userInputDelay();
			switch (input) {
			case "scale":
				outputField.setText(x.getInteractions().get(0).getDescription());
				ArrayList<String> commandsList = new ArrayList<String>();
				x.getInteractions().get(0).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});
				commands = new String[commandsList.size()];
				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();

				while (!x.getInteractions().get(0).getActions().containsKey(input)) {
					input = userInputDelay();
					switch (input) {
					case "1":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;
					case "2":
						if (rocks == 'y') {
							outputField.setText("The scale is balanced. The door opens and you advance forward.");
							return "room5";// CHANGE TO "room5"
						} else {
							outputField.setText("Keep the intended path.");
							break;
						}
					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}
				break;
			case "door":
				outputField.setText(x.getInteractions().get(1).getDescription());
				commandsList = new ArrayList<String>();
				x.getInteractions().get(1).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});
				commands = new String[commandsList.size()];
				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();

				while (!x.getInteractions().get(1).getActions().containsKey(input)) {
					input = userInputDelay();
					switch (input) {
					case "1":
						outputField.setText("You kick the door. It wasn't very effective...");
						input = " ";
						break;
					case "2":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;
					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}
				break;

			case "rocks":
				if (rocks == 'n') {
					outputField.setText(x.getInteractions().get(2).getDescription());
					commandsList = new ArrayList<String>();
					x.getInteractions().get(2).getActions().forEach((k, v) -> {
						System.out.println("- " + k + " " + v);
						commandsList.add("- " + k + " " + v);
					});
					commands = new String[commandsList.size()];
					for (int i = 0; i < commandsList.size(); i++) {
						commands[i] = commandsList.get(i);
					}
					commandUpdate();

					while (!x.getInteractions().get(2).getActions().containsKey(input)) {
						input = userInputDelay();
						switch (input) {
						case "1":
							outputField.setText("These rocks might balance the scale.");
							input = " ";
							rocks = 'y';
							x.getInteractions().get(0).getActions().put("2", "Place rocks on scale.");
							break;
						case "2":
							commands = new String[] {"Nothing yet..."};
							commandUpdate();
							outputField.setText(x.getDescription());
							break;
						default:
							outputField.setText("You can't do that here.");
							new javax.swing.Timer(3000, new ActionListener() {
				    	        @Override
				    	        public void actionPerformed(ActionEvent evt) {
				    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
				    	            outputField.setText(x.getDescription()); // Re-display the room description
				    	        }
				    	    }).start();
						}
					}
				} else {
					outputField.setText("You can't carry more rocks.");
					new javax.swing.Timer(3000, new ActionListener() {
		    	        @Override
		    	        public void actionPerformed(ActionEvent evt) {
		    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
		    	            outputField.setText(x.getDescription()); // Re-display the room description
		    	        }
		    	    }).start();
					break;
				}
				break;

			default:
				outputField.setText("You can't do that here.");
				new javax.swing.Timer(3000, new ActionListener() {
	    	        @Override
	    	        public void actionPerformed(ActionEvent evt) {
	    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
	    	            outputField.setText(x.getDescription()); // Re-display the room description
	    	        }
	    	    }).start();
			}
		}
		return "You broke it.";
	}

	// Start of Room 5
	public static String Room5() {
		updateRoomImage("Room05.png");

		// Creation of room 5 Double Door.
		ArrayList<interactable> IList = new ArrayList<interactable>();

		// Make Engravings Interact able and add to interact able list
		String tempIdescription = "On the wall lays engravings of Caladon Breaking through a castle's defenses by shooting orb shaped projectiles. The end of the art shows Caladon finishing off the king by drop kicking the nobles head.";
		IList.add(new interactable(tempIdescription));
		IList.get(0).getActions().put("1", "Learn the secret of Caladon's kicking style.");
		IList.get(0).getActions().put("2", "Back");

		// Make Door interact able and add to interact able list
		tempIdescription = "The set of double doors are closed with the design to create the image of a skull inside an ornate looking circle surrounding it.";
		IList.add(new interactable(tempIdescription));
		IList.get(1).getActions().put("1", "Try Pushing in the door.");
		IList.get(1).getActions().put("2", "KICK IN THE DOOR!");
		IList.get(1).getActions().put("3", "Back");

		// Making room description and image, then making room object
		String Rdescription = "You enter the room expecting it to e filled with a larger puzzle and many events going on, but instead you see an empty space with a DOOR on the left wall. "
				+ "Along the walls of the room are ENGRAVINGS of Caladon doing some kind of action.";
		String Rimg = "nope.png";

		room x = new room(Rdescription, Rimg, IList);

		// Running room five.
		char haskey = 'n';
		commands = new String[] {"Nothing yet..."};
		commandUpdate();
		outputField.setText(x.getDescription());
		String input = " ";

		while (!input.equals("engravings") || !input.equals("door")) {

			input = userInputDelay();

			switch (input) {
			case "engravings":
				outputField.setText(x.getInteractions().get(0).getDescription());
				ArrayList<String> commandsList = new ArrayList<String>();
				x.getInteractions().get(0).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});

				commands = new String[commandsList.size()];

				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();

				while (!x.getInteractions().get(0).getActions().containsKey(input)) {
					input = userInputDelay();

					switch (input) {
					case "1":
						outputField.setText("You obtained terrifying strength in your legs!");
						input = " ";
						haskey = 'y';
						x.getInteractions().get(1).getActions().put("4", "PUNCH THE DOOR!");
						x.getInteractions().get(1).getActions().put("5", "DROPKICK THE SKULL ON THE DOOR!");
						break;

					case "2":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;

					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}

				break;

			case "door":
				outputField.setText(x.getInteractions().get(1).getDescription());
				commandsList = new ArrayList<String>();
				x.getInteractions().get(1).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});

				commands = new String[commandsList.size()];

				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}

				commandUpdate();

				while (!x.getInteractions().get(1).getActions().containsKey(input)) {
					input = userInputDelay();

					switch (input) {
					case "1":
						outputField.setText("You try to push the door open only to face an immutable resistance.");
						input = " ";
						break;

					case "2":
						outputField.setText(
								"You kick the door with all your might and make no difference in anything except for the pain your toes now.");
						input = " ";
						break;

					case "3":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;

					case "4":
						if (haskey == 'y') {
							outputField.setText(
									"The loud crack as you put all your muscles into the punch that could shake nations fades into pain and aching knuckles that you have to blow air on to numb a bit.");
							input = " ";
							break;
						}

					case "5":
						if (haskey == 'y') {
							outputField.setText(
									"You drop kick the skull on the door with all your might and send the door flying open as the sounds of a cool theme song play in your head... You swear you weren't that strong , but things change in dungeons.");
							return "room6";
						} else {
							outputField.setText("Sorry, cant speedrun this way");
							break;
						}

					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();

					}
				}
				break;

			default:
				outputField.setText("You can't do that here.");
				new javax.swing.Timer(3000, new ActionListener() {
	    	        @Override
	    	        public void actionPerformed(ActionEvent evt) {
	    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
	    	            outputField.setText(x.getDescription()); // Re-display the room description
	    	        }
	    	    }).start();
			}
		}

		// If this string is reached, the while loop terminated when it shouldn't have.
		return "You broke it.";
	}

	// Start of Room 6
	public static String Room6() {
		updateRoomImage("Room06.png");
		// Creation of Room 6: Scales
		ArrayList<interactable> IList = new ArrayList<>();

		// Pedestal interactable and add to interactable list
		String tempIdescription = "The pedestal seems familiar, it is missing a certain object.";
		IList.add(new interactable(tempIdescription));
		IList.get(0).getActions().put("1", "Back.");

		// Door interactable and add to interactable list
		tempIdescription = "The door is a massive slab of stone, it appears unmovable, without any visible way to open it.";
		IList.add(new interactable(tempIdescription));
		IList.get(1).getActions().put("1", "Kick the door.");
		IList.get(1).getActions().put("2", "Back.");

		// Painting interactable
		tempIdescription = "The painting depicts a powerful menacing figure. Despite its quality, it appears out of place as if it was meant to hide something.";
		IList.add(new interactable(tempIdescription));
		IList.get(2).getActions().put("1", "Move painting.");
		IList.get(2).getActions().put("2", "Back.");

		// Room description and image
		String Rdescription = "You enter a room with an unshakable sense of mystery. In the center there is a PEDESTAL, it reminds you of of something you have seen before. Against the far wall is a DOOR, its edges blending into the wall, with no visible way to open it. To the side, there is a large PAINTING depicting a powerful being.";
		String Rimg = "Room06.png";

		room x = new room(Rdescription, Rimg, IList);

		// Running room 6
		char hasOrb = 'n';
		commands = new String[] {"Nothing yet..."};
		commandUpdate();
		outputField.setText(x.getDescription());
		String input = " ";

		while (!input.equals("pedestal") || !input.equals("door") || !input.equals("painting")) {
			input = userInputDelay();
			switch (input) {
			case "pedestal":
				outputField.setText(x.getInteractions().get(0).getDescription());
				ArrayList<String> commandsList = new ArrayList<String>();
				x.getInteractions().get(0).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});
				commands = new String[commandsList.size()];
				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();

				while (!x.getInteractions().get(0).getActions().containsKey(input)) {
					input = userInputDelay();
					switch (input) {
					case "1":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;
					case "2":
						if (hasOrb == 'y') {
							outputField.setText("The orb lights up and the door unlocks, clearing the path forward.");
							return "roomEnd";
						} else {
							outputField.setText("Oops! That's not how this story unfolds. Try again!");
							break;
						}
					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}
				break;

			case "door":
				outputField.setText(x.getInteractions().get(1).getDescription());
				commandsList = new ArrayList<String>();
				x.getInteractions().get(1).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});
				commands = new String[commandsList.size()];
				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();

				while (!x.getInteractions().get(1).getActions().containsKey(input)) {
					input = userInputDelay();
					switch (input) {
					case "1":
						outputField.setText("You kick the door. It wasn't very effective...");
						input = " ";
						break;
					case "2":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;
					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}
				break;

			case "painting":
				outputField.setText(x.getInteractions().get(2).getDescription());
				commandsList = new ArrayList<String>();
				x.getInteractions().get(2).getActions().forEach((k, v) -> {
					System.out.println("- " + k + " " + v);
					commandsList.add("- " + k + " " + v);
				});
				commands = new String[commandsList.size()];
				for (int i = 0; i < commandsList.size(); i++) {
					commands[i] = commandsList.get(i);
				}
				commandUpdate();

				while (!x.getInteractions().get(2).getActions().containsKey(input)) {
					input = userInputDelay();
					switch (input) {
					case "1":
						outputField.setText(
								"The space behing the painting reveals a orb shining with magic. You decide to grab it.");
						input = " ";
						hasOrb = 'y';
						x.getInteractions().get(0).getActions().put("2", "Place the orb in the pedestal.");
						break;
					case "2":
						commands = new String[] {"Nothing yet..."};
						commandUpdate();
						outputField.setText(x.getDescription());
						break;
					default:
						outputField.setText("You can't do that here.");
						new javax.swing.Timer(3000, new ActionListener() {
			    	        @Override
			    	        public void actionPerformed(ActionEvent evt) {
			    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
			    	            outputField.setText(x.getDescription()); // Re-display the room description
			    	        }
			    	    }).start();
					}
				}
				break;

			default:
				outputField.setText("You can't do that here.");
				new javax.swing.Timer(3000, new ActionListener() {
	    	        @Override
	    	        public void actionPerformed(ActionEvent evt) {
	    	            ((javax.swing.Timer) evt.getSource()).stop(); // Stop the timer
	    	            outputField.setText(x.getDescription()); // Re-display the room description
	    	        }
	    	    }).start();
			}
		}
		// Check if loop terminates when it shouldn't
		return "You broke it.";
	}

/////////////////////////////////////////////		ROOMS END		////////////////////////////////////////////////////////////////////
}