/*
 * Copyleft (C) 2015 Piotr Siatkowski find me on Facebook;
 * Copyleft (C) 2005 Helio Perroni Filho xperroni@yahoo.com ICQ: 2490863;
 * This file is part of BotMaker. BotMaker is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. BotMaker is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with BotMaker (look at the
 * Documents directory); if not, either write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA, or visit
 * (http://www.gnu.org/licenses/gpl.txt).
 */

package com.thetruthbeyond.chatterbean.aiml;

import java.util.*;

import com.thetruthbeyond.chatterbean.text.structures.Transformations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

public class Topic extends AIMLElement implements NormalizedTag {

	private static final int CAPACITY = 30;

	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }

	private String name;
	private final List<Category> categories = new LinkedList<>();
  
	/* Constructor Section */
  
	public Topic(Attributes attributes) {
		name = attributes.getValue(0).trim();
	}
  
	public Topic(String name, Category... children) {
		this.name = name.trim();
		categories.addAll(Arrays.asList(children));
	}

	/* Method Section */

	@Override
	public void normalizeContent(Transformations transformations) {
		name = transformations.makeSentence(name).getNormalized().trim();
	}

	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}
	
	public void appendChild(Category child) {
		child.setTopic(this);
    	categories.add(child);
	}

	@Override
	public void appendMeTo(Aiml aiml) {
		aiml.appendChild(this);
	}

	public List<String> getMatchPath() {
		return Arrays.asList(name.split(" "));
	}

	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {
		
		// Indent
		String indent = new String(new char[indentLevel]).replace("\0", BASE_INDENT);
		parent.appendChild(document.createTextNode("\n" + indent));
				
		Element element = document.createElement("topic");
		element.setAttribute("name", name);
		
		for(Category category : categories)
			element.appendChild(category.getNode(document, element, indentLevel + 1));
		
		element.appendChild(document.createTextNode("\n" + indent));
		return element;
	}
	
	/* Properties */
	
	public List<Category> getCategories() {
		return Collections.unmodifiableList(categories);
	}
  
	public String getName() { return name; } 
	public void setName(String name) { this.name = name; }
  
	@Override
 	public boolean equals(Object obj) {
 		if(obj instanceof Topic) {
 			Topic compared = (Topic) obj;
 			return name.equals(compared.name) && categories.equals(compared.categories);
 		} else
			return false;
 	}
  
	@Override
	public String toString() {
		return name;
	}
}
