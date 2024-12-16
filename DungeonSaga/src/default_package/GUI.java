package default_package;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GUI {

	public static void main(String[] args) {
		new GUI();
		System.out.println("Hello world!");

	}

	Font mainFont = new Font("DejaVu Serif", Font.CENTER_BASELINE, 16);
	JFrame window;
	JPanel buttonPanel;
	JLabel imageLabel;
	JPanel textPanel;
	// JPanel testPanel;

	public GUI() {

		String[] buttonImagePaths = { "Commands.png", "Inventory.png", "Options.png", "Quit.png" };
		String roomImagePath = "Room.png";
		String textOutput = ("No! Layers! Onions have layers. Ogres have layers! Onions have layers. You get it? We both have layers. DONKEY (trailing after Shrek) Oh, you both have layers. Oh. {Sniffs} You know, not everybody likes onions. Cake! Everybody loves cakes! Cakes have layers. SHREK I don't care... what everyone likes. Ogres are not like cakes. DONKEY You know what else everybody likes? Parfaits. Have you ever met a person, you say, \"Let's get some parfait,\" they say, \"Hell no, I don't like no parfait\"? Parfaits are delicious.");
		// String textInput;

		window(); // Initiate display window.
		topButtons(buttonImagePaths); // Add upper screen buttons.
		roomImage(roomImagePath); // Add room image
		textBox(textOutput); // Add output text
		// testBoxes();

		// Window layout settings NOTE: Look into gridBagLayout methods
		window.add(buttonPanel, BorderLayout.NORTH);
		window.add(imageLabel, BorderLayout.CENTER);
		window.add(textPanel, BorderLayout.SOUTH);
		// window.add(testPanel, BorderLayout.SOUTH);

		window.setVisible(true); // WARNING: Leave at end of class for proper layering order

	}

	// Window initial settings
	public void window() {
		window = new JFrame();
		window.setTitle("Dungeon Saga"); // Title
		window.setSize(700, 700); // Resolution
		window.getContentPane().setBackground(Color.BLACK); // Background color Color.BLACK or new Color(90, 250, 250)
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null); // Open window centered on screen
	}

	// Button setup
	public void topButtons(String[] imagePaths) {
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.BLACK);

		// Loop for button layout
		for (String path : imagePaths) {
			ImageIcon icon = new ImageIcon(path);
			int width, height;
			width = icon.getIconWidth();
			height = icon.getIconHeight();

			// Button image sizing
			JButton button = new JButton(icon);
			button.setPreferredSize(new Dimension(width, height)); // Button size matches image size
			button.setBorderPainted(false);
			button.setContentAreaFilled(false); // Button background transparency

			// !!! Create darkened icon for hover effect
			ImageIcon darkIcon = new ImageIcon(darkenImage(icon.getImage()));

			// Add mouse listener for hover effect
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					button.setIcon(darkIcon); // Set dark icon on hover
				}

				@Override
				public void mouseExited(MouseEvent e) {
					button.setIcon(icon); // Reset to original icon
				}
			});

			// Action for button click
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Button clicked: " + path);
				}
			});

			buttonPanel.add(button); // Add to panel
		}

	}

	// Darken button image
	private Image darkenImage(Image img) {
		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D overlayBlack = bufferedImage.createGraphics();
		overlayBlack.drawImage(img, 0, 0, null);
		overlayBlack.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // Set transparency
		overlayBlack.setColor(Color.BLACK);
		overlayBlack.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		overlayBlack.dispose();

		return bufferedImage;
	}

	// Image setup
	public void roomImage(String imagePath) {
		ImageIcon icon = new ImageIcon(imagePath);
		imageLabel = new JLabel(icon);
	}

	// Test panel
	public void textBox(String textOutput) {

		// Create the panel and set the layout
		textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout()); // Use BorderLayout for better control of positioning
		textPanel.setBackground(Color.GREEN);

		// Create a JTextArea and set some text
		JTextArea outputField = new JTextArea(3, 20);
		outputField.setFont(mainFont);
		outputField.setText(textOutput);
		outputField.setEditable(false); // Make the JTextArea non-editable (optional)
		outputField.setLineWrap(true); // Wrap text to next line at box borders
		outputField.setWrapStyleWord(true); // Wrap at words instead of letters
		outputField.setForeground(Color.WHITE);
		outputField.setBackground(Color.BLACK);
		outputField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		textPanel.add(outputField, BorderLayout.CENTER);

		// Create a JTextField and specify its column width
		JTextField inputField = new JTextField();
		inputField.setFont(mainFont);
		inputField.setForeground(Color.WHITE);
		inputField.setBackground(Color.BLACK);
		inputField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		textPanel.add(inputField, BorderLayout.SOUTH);
		
		inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText();
                System.out.println("Text entered: " + text);
                inputField.setText("");
            }
        });

	}

	// Text boxes
	public void testBoxes() {

		// textPanel.setLayout(new BorderLayout()); // !!! BROKEN

		// Create input field
		JTextField inputField = new JTextField(20);
		textPanel.add(inputField, BorderLayout.NORTH);

		// Create output area
		JTextArea outputArea = new JTextArea(10, 20);
		outputArea.setEditable(false); // Make it read-only
		JScrollPane scrollPane = new JScrollPane(outputArea);
		textPanel.add(scrollPane, BorderLayout.CENTER);

		/*
		 * JTextField thong = new JTextField(20);
		 * thong.setFont(thong.getFont().deriveFont(50f)); textPanel.add(thong,
		 * BorderLayout.SOUTH);
		 */
		/*
		 * JTextField inputField = new JTextField(20); textPanel.add(inputField);
		 */

	}

}

//JScrollPane scrollPane = new JScrollPane(outputArea);
