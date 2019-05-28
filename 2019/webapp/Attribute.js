class Attribute {
  constructor(attr_id, description, list, tally, appearance, best, worst) {
    /** The name and description for this Attribute.
    *  name: Attributes can have a long description. This gives it a
    *      shorter name.
    *  description: A description is how the Attribute appears in the 
    *         question (i.e. "Runny nose").
    */
    this.attr_id = attr_id;
    this.description = description;

    /** The list and tally for this Attribute.
    *  list: A list containing the names of Attributes ranked higher.
    *  tally: The count of how many Attributes this Attribute is
    *       compared to.
    *  appearance: The number of times this Attribute appears in
    *        a question.
    *  edges: The number of edges this Attribute has in the Markov
    *       chain.
    */
    if (list == undefined) this.list = []; else this.list = list;
    if (tally == undefined) this.tally = 0; else this.tally = tally;
    if (appearance == undefined) this.appearance = 0; else this.appearance = appearance;
    if (best == undefined) this.best = false; else this.best = best;
    if (worst == undefined) this.worst = false; else this.worst = worst;
  }

  /**
   * Change the description of this Attribute.
   */
  setDescription(description) {
    if (description === "") {
      throw "Attribute description is empty.";
    } else if (description == undefined) {
      throw "Attribute description is null.";
    }
    this.description = description;
  }
  
  /**
   * Set true if it was chosen as worst in a question.
   */
  setWorst() {
    this.worst = true;
  }

  /**
   * Set true if it was chosen as best in a question.
   */
  setBest() {
    this.best = true;
  }

  /**
   * Return the number of times attr is in this Attribute's 
   * list.
   */
  countOf(attr) {
    var count = 0;
    for (i = 0; i < list.length; i++) {
      if (list[i] == attr.attr_id) count++;
    }
    return count;
  }

  /**
   * Add the name of the Attribute that is ranked higher. Increment 
   * the tallies.
   */
  addToList(attr) {
    if (attr == undefined) {
      throw "Attribute to add to list is null.";
    }
    if (!list.includes(attr[attr_id])
        && !(attr[list].includes(this[attr_id])) 
        && !(attr[attr_id] == this[attr_id])
        && !(attr[description] == this[description])) {
      attr.incrementTally();
      this.incrementTally();
    }
    list.push(attr.attr_id);
  }

  /**
   * Increment appearance whenever it is asked in a question.
   */
  incrementAppearance() {
    appearance++;
  }
  
  /**
   * Increment the tally by 1.
   */
  incrementTally() {
    tally++;
  }

  /**
   * Return true if list contains at least one of the attributes
   * in the given array. Otherwise return false.
   */
  
  containsAtLeastOne(attrs) {
    if (attrs == undefined) {
      throw "Input is null.";
    }
    //if input is string not string array.
    if (typeof attrs == "string") {
      if (list.contains(attr)) return true;
      return false;
    }
    //if input is string array.
    if (attrs.length == 0) {
      throw "String array is empty.";
    }
    for (attr in attrs) {
      if (list.contains(attr)) return true;
    }
    return false;
  }
  
  /**
   Clears the tally, edges, and list.
   */
  clear() {
    tally = 0;
    list = [];
    appearance = 0;
    best = false;
    worst = false;
    
  }

  //Need to call function directly unlike Java
  compareTo(SimpleAttribute attr) {
    total = appearance;
    atot = attr.appearance;
    if ((atot - total) == 0) {
      return this.attr_id.compareTo(attr.attr_id);
    } else {
      return atot - total;
    }
  }
  
}