package default_package;
import java.io.File;
import java.util.*;
import java.util.ArrayList;

import default_package.Dungeon_Saga.interactable;
import default_package.Dungeon_Saga.room;

// Programmers: Team Content: 
// Date started: 10/29/2024
// Date finished: N/A
// Rooms and contents for the Dungeon Saga Game.
 
public class Rooms{
	
	public static class interactable{
		private String description; // The word that will begin interacting with an item.
		private HashMap<String,String> actions;
		
		public interactable(String desc) {
			this.description = desc;
			this.actions = new HashMap<String,String>();
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
}