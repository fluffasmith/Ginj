/*
 * This code is based on BoundedRangeMondel
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package info.ginj.ui.component;

import javax.swing.event.ChangeListener;


/**
 * Defines the data model used by the <code>TimelineSlider</code>.
 * Defines six interrelated integer properties: minimum, maximum, lower, higher, extent
 * and value.  These six integers define nested ranges like this:
 * <pre>
 * minimum &lt;= lower &lt;= value &lt;= value+extent &lt;= higher &lt;= maximum
 * </pre>
 * The outer range is <code>minimum,maximum</code>, the intermediate range is <code>lower,higher</code> and the inner
 * range is <code>value,value+extent</code>.  Each range
 * must lie within the enclosing one, i.e. <code>value</code> must be
 * less than or equal to <code>maximum</code> and <code>value+extent</code>
 * must greater than or equal to <code>minimum</code>, and <code>maximum</code>
 * must be greater than or equal to <code>minimum</code>.
 * There are a few features of this model that one might find a little
 * surprising.  These quirks exist for the convenience of the
 * Swing BoundedTimelineRangeModel clients, such as <code>Slider</code> and
 * <code>ScrollBar</code>.
 * <ul>
 * <li>
 *   The minimum and maximum set methods "correct" the other
 *   five properties to accommodate their new value argument.  For
 *   example setting the model's minimum may change its maximum, lower, higher, value,
 *   and extent properties (in that order), to maintain the constraints
 *   specified above.
 *
 * <li>
 *   The lower and higher set methods "correct" their argument to
 *   fit within the limits defined by the min and max properties, but they
 *   "correct" the other three properties to accommodate their new value argument.
 *   For example setting the model's lower may change its higher, value,
 *   and extent properties (in that order), to maintain the constraints
 *   specified above.
 *
 * <li>
 *   The value and extent set methods "correct" their argument to
 *   fit within the limits defined by the other five properties.
 *   For example if <code>value == maximum</code>, <code>setExtent(10)</code>
 *   would change the extent (back) to zero.
 *
 * <li>
 *   The six BoundedTimelineRangeModel values are defined as Java Beans properties
 *   however Swing ChangeEvents are used to notify clients of changes rather
 *   than PropertyChangeEvents. This was done to keep the overhead of monitoring
 *   a BoundedTimelineRangeModel low. Changes are often reported at MouseDragged rates.
 * </ul>
 *
 * <p>
 *
 * @author Hans Muller
 * @author Vicne
 * @see DefaultBoundedTimelineRangeModel
 * @since 1.2
 */
public interface BoundedTimelineRangeModel
{
    // constant indices for the thumbs
    int THUMB_NONE = -1;
    int THUMB_CURRENT = 0;
    int THUMB_LOWER = 1;
    int THUMB_HIGHER = 2;

    /**
     * Returns the minimum acceptable value.
     *
     * @return the value of the minimum property
     * @see #setMinimum
     */
    int getMinimum();


    /**
     * Sets the model's minimum to <I>newMinimum</I>.   The
     * other three properties may be changed as well, to ensure
     * that:
     * <pre>
     * min &lt;= lower &lt;= value &lt;= value+extent &lt;= higher &lt;= max
     * </pre>
     * <p>
     * Notifies any listeners if the model changes.
     *
     * @param newMinimum the model's new minimum
     * @see #getMinimum
     * @see #addChangeListener
     */
    void setMinimum(int newMinimum);


    /**
     * Returns the model's maximum.  Note that the upper
     * limit on the model's value is (maximum - extent).
     *
     * @return the value of the maximum property.
     * @see #setMaximum
     * @see #setExtent
     */
    int getMaximum();


    /**
     * Sets the model's maximum to <I>newMaximum</I>. The other
     * three properties may be changed as well, to ensure that
     * <pre>
     * min &lt;= lower &lt;= value &lt;= value+extent &lt;= higher &lt;= max
     * </pre>
     * <p>
     * Notifies any listeners if the model changes.
     *
     * @param newMaximum the model's new maximum
     * @see #getMaximum
     * @see #addChangeListener
     */
    void setMaximum(int newMaximum);

    /**
     * Returns the model's lower.
     * @return the model's lower
     * @see #setLower
     * @see BoundedTimelineRangeModel#getLower
     */
    int getLower();

    /**
     * Sets the lower to <I>n</I> after ensuring that <I>n</I>
     * that the other properties obey the model's constraints:
     * <pre>
     * min &lt;= lower &lt;= value &lt;= value+extent &lt;= higher &lt;= max
     * </pre>
     * @see BoundedTimelineRangeModel#setMaximum
     */
    void setLower(int newLower);

    /**
     * Returns the model's higher.
     * @return the model's higher
     * @see #setHigher
     * @see BoundedTimelineRangeModel#getHigher
     */
    int getHigher();

    /**
     * Sets the maximum to <I>n</I> after ensuring that <I>n</I>
     * that the other properties obey the model's constraints:
     * <pre>
     * min &lt;= lower &lt;= value &lt;= value+extent &lt;= higher &lt;= max
     * </pre>
     * @see BoundedTimelineRangeModel#setMaximum
     */
    void setHigher(int newHigher);

    /**
     * Returns the model's current value.  Note that the upper
     * limit on the model's value is <code>maximum - extent</code>
     * and the lower limit is <code>minimum</code>.
     *
     * @return  the model's value
     * @see     #setValue
     */
    int getValue();


    /**
     * Returns the current value of the given thumb of the slider
     *
     * @param   thumbIndex the index of the thumb we want to get the value of
     * @return  the current value of the given thumb of the model
     */
    int getThumbValue(int thumbIndex);


    /**
     * Sets the model's current value to <code>newValue</code> if <code>newValue</code>
     * satisfies the model's constraints. Those constraints are:
     * <pre>
     * min &lt;= lower &lt;= value &lt;= value+extent &lt;= higher &lt;= max
     * </pre>
     * Otherwise, if <code>newValue</code> is less than <code>minimum</code>
     * it's set to <code>minimum</code>, if its greater than
     * <code>maximum</code> then it's set to <code>maximum</code>, and
     * if it's greater than <code>value+extent</code> then it's set to
     * <code>value+extent</code>.
     * <p>
     * When a BoundedRange model is used with a scrollbar the value
     * specifies the origin of the scrollbar knob (aka the "thumb" or
     * "elevator").  The value usually represents the origin of the
     * visible part of the object being scrolled.
     * <p>
     * Notifies any listeners if the model changes.
     *
     * @param newValue the model's new value
     * @see #getValue
     */
    void setValue(int newValue);


    /**
     * This attribute indicates that any upcoming changes to the value
     * of the model should be considered a single event. This attribute
     * will be set to a value != THUMBS_NONE at the start of a series of changes to the value,
     * and will be set to THUMBS_NONE when the value has finished changing.  Normally
     * this allows a listener to only take action when the final value change in
     * committed, instead of having to do updates for all intermediate values.
     * <p>
     * Sliders and scrollbars use this property when a drag is underway.
     *
     * @param thumbIndex THUMBS_NONE if the upcoming changes to the value property are not part of a series. Index of the thumb otherwise.
     */
    void setAdjustingThumbIndex(int thumbIndex);

    /**
     * Returns the index of the thumb being adjusted if the current changes to the value property are part
     * of a series of changes, or THUMB_NONE if no thumb is adjusting
     *
     * @return the adjustingThumbIndex property.
     * @see #setAdjustingThumbIndex
     */
    int getAdjustingThumbIndex();


    /**
     * Returns the model's extent, the length of the inner range that
     * begins at the model's value.
     *
     * @return  the value of the model's extent property
     * @see     #setExtent
     * @see     #setValue
     */
    int getExtent();


    /**
     * Sets the model's extent.  The <I>newExtent</I> is forced to
     * be greater than or equal to zero and less than or equal to
     * maximum - value.
     * <p>
     * When a BoundedRange model is used with a scrollbar the extent
     * defines the length of the scrollbar knob (aka the "thumb" or
     * "elevator").  The extent usually represents how much of the
     * object being scrolled is visible. When used with a slider,
     * the extent determines how much the value can "jump", for
     * example when the user presses PgUp or PgDn.
     * <p>
     * Notifies any listeners if the model changes.
     *
     * @param  newExtent the model's new extent
     * @see #getExtent
     * @see #setValue
     */
    void setExtent(int newExtent);



    /**
     * This method sets all of the model's data with a single method call.
     * The method results in a single change event being generated. This is
     * convenient when you need to adjust all the model data simultaneously and
     * do not want individual change events to occur.
     *
     * @param value  an int giving the current value
     * @param extent an int giving the amount by which the value can "jump"
     * @param min    an int giving the minimum value
     * @param max    an int giving the maximum value
     * @param adjustingThumbIndex an int, THUMB_NONE if no change is in progress, or index of the adjusting thumb
     *                           if a series of changes are in progress
     *
     * @see #setValue
     * @see #setExtent
     * @see #setMinimum
     * @see #setMaximum
     * @see #setAdjustingThumbIndex
     */
    void setRangeProperties(int value, int extent, int min, int max, int lower, int higher, int adjustingThumbIndex);


    /**
     * Adds a ChangeListener to the model's listener list.
     *
     * @param x the ChangeListener to add
     * @see #removeChangeListener
     */
    void addChangeListener(ChangeListener x);


    /**
     * Removes a ChangeListener from the model's listener list.
     *
     * @param x the ChangeListener to remove
     * @see #addChangeListener
     */
    void removeChangeListener(ChangeListener x);

}
