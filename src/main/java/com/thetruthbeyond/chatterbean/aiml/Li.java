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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

public class Li extends TemplateElement {
  
	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }
	
	private String name = null;
	private String value = null;
  
	public Li() {}

	public Li(Attributes attributes) {
		if(attributes.getLength() == 1) {
			name = attributes.getLocalName(0);
			value = attributes.getValue(0);
		} else {
			name = attributes.getValue(ATTRIBUTE_NAME);
			value = attributes.getValue(ATTRIBUTE_VALUE);
		}
	}

	public Li(String name, String value, TemplateElement... children) {
		super(children);

		this.name = name;
		this.value = value;
	}

	/* Methods */

	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}

	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {
		// Indent
		String indent = new String(new char[indentLevel]).replace("\0", BASE_INDENT);
		parent.appendChild(document.createTextNode(NEW_LINE + indent));
				
		Element element = document.createElement("li");
		
		if(name != null && value != null)
			element.setAttribute(name, value);

		boolean isLineBreakNeeded = false;
		for(TemplateElement child : children) {
			element.appendChild(child.getNode(document, element, indentLevel + 1));
			if(child.getId(0) == Condition.Id || child.getId(0) == Random.Id || child.getId(0) == Think.Id)
				isLineBreakNeeded = true;
		}

		if(isLineBreakNeeded)
			element.appendChild(document.createTextNode(NEW_LINE + indent));

		return element;
	}
  
	@Override
	public boolean equals(Object object) {
		if(!super.equals(object))
			return false;
		Li compared = (Li) object;
		return isEquals(name, compared.name) && isEquals(value, compared.value);
	}

	@SuppressWarnings("MethodMayBeStatic")
	private boolean isEquals(Object comparing, Object compared) {
		return comparing == null ? compared == null : comparing.equals(compared);
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
  
	public String getValue() { return value; }
	public void setValue(String value) { this.value = value; }
}
