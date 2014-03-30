package org.zaval.data.event;

import org.zaval.data.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class describes event that is performed by <code>TextModel</code> interface implementation.
 * Using the event class the text implementation notifies listeners about the text content changes.
 */
public class TextEvent
extends EventObject
{
  /**
   * The text inserted event type.
   */
   public static final int INSERTED = 1;

  /**
   * The text removed event type.
   */
   public static final int REMOVED  = 2;

  /**
   * The text updated event type.
   */
   public static final int UPDATED  = 3;

   private int offset, size, firstUpdatedLine, updatedLines, pl, id;

  /**
   * Constructs a new text event class with the given source, event id, text offset,
   * text size and line number that the text has had before its changing. The offset
   * defines where removing or inserting operation has been performed
   * and size defines size of removed or inserted part.
   * @param target the source of the event.
   * @param id the type of the event.
   * @param offset the text offset.
   * @param size the text size.
   * @param pl the number of lines that the text has had before changing. The
   * argument can help to understand how much lines has been added or removed from the text.
   */
   public TextEvent(Object target, int id, int offset, int size, int pl)
   {
     super(target);
     if (id != INSERTED && id != REMOVED && id != UPDATED) throw new IllegalArgumentException();
     this.id     = id;
     this.offset = offset;
     this.size   = size;
     this.pl     = pl;
   }


  /**
   * Sets the set of lines that has been updated.
   * @param first the specified start updated line number.
   * @param lines the specified number of updated lines.
   */
   public void setUpdatedLines (int first, int lines) {
     firstUpdatedLine = first;
     updatedLines     = lines;
   }

  /**
   * Gets the start updated line number from that the text has been modified.
   * @return a line number.
   */
   public int getFirstUpdatedLine() {
     return firstUpdatedLine;
   }

  /**
   * Gets the number of updated lines that has been modified for the text.
   * @return a number of lines.
   */
   public int getUpdatedLines() {
     return updatedLines;
   }

  /**
   * Returns the offset that defines where the removing or inserting operation has been
   * performed.
   * @return a text offset.
   */
   public int getOffset() {
     return offset;
   }

  /**
   * Returns the size that defines size of removed or inserted text part.
   * @return a text size.
   */
   public int getSize() {
     return size;
   }

  /**
   * Returns the previous line numbers. The information can help to define how many lines have been
   * inserted or removed depending on the event type.
   * @return a previous line numbers.
   */
   public int getPrevLines () {
     return pl;
   }

  /**
   * Gets the text event id
   * @return a text event id.
   */
   public int getID () {
    return id;
   }
}

