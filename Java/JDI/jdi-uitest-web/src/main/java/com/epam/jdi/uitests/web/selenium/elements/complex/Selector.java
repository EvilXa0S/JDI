package com.epam.jdi.uitests.web.selenium.elements.complex;
/*
 * Copyright 2004-2016 EPAM Systems
 *
 * This file is part of JDI project.
 *
 * JDI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JDI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JDI. If not, see <http://www.gnu.org/licenses/>.
 */


import com.epam.jdi.uitests.core.interfaces.complex.ISelector;
import com.epam.jdi.uitests.web.selenium.elements.GetElementType;
import com.epam.jdi.uitests.web.selenium.elements.base.BaseElement;
import com.epam.jdi.uitests.web.selenium.elements.base.Element;
import com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.objects.JSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.util.List;

import static com.epam.commons.EnumUtils.getEnumValue;
import static com.epam.commons.LinqUtils.first;
import static com.epam.commons.LinqUtils.firstIndex;
import static com.epam.jdi.uitests.core.settings.JDISettings.exception;
import static com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.WebAnnotationsUtil.findByToBy;
import static com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.objects.FillFromAnnotationRules.fieldHasAnnotation;

/**
 * Created by roman.i on 03.10.2014.
 */

public class Selector<TEnum extends Enum> extends BaseSelector<TEnum> implements ISelector<TEnum> {
    public Selector() {
        super();
    }

    public Selector(By optionsNamesLocatorTemplate) {
        super(optionsNamesLocatorTemplate);
    }

    public Selector(By optionsNamesLocatorTemplate, By allOptionsNamesLocator) {
        super(optionsNamesLocatorTemplate, allOptionsNamesLocator);
    }

    public static void setUp(BaseElement el, Field field) {
        if (!fieldHasAnnotation(field, JSelector.class, ISelector.class))
            return;
        ((Selector) el).setUp(field.getAnnotation(JSelector.class));
    }

    public Selector<TEnum> setUp(JSelector jSelector) {
        By root = findByToBy(jSelector.root());
        By value = findByToBy(jSelector.value());
        By list = findByToBy(jSelector.list());
        if (root == null)
            root = findByToBy(jSelector.jRoot());
        if (value == null)
            value = findByToBy(jSelector.jValue());
        if (list == null)
            list = findByToBy(jSelector.jList());

        if (root != null) {
            Element el = new Element(root);
            el.setParent(getParent());
            setParent(el);
            setAvatar(root);
        }
        if (list != null)
            this.allLabels = new GetElementType(list, this);
        return this;
    }


    /**
     * @param name Specify name using string
     *             Select Element with name (use text) from list
     */
    public final void select(String name) {
        actions.select(name, this::selectAction);
    }

    /**
     * @param name Specify name using enum
     *             Select Element with name (use enum) from list
     */
    public final void select(TEnum name) {
        select(getEnumValue(name));
    }

    /**
     * @param num Specify digit to select
     *              Select Element with name (use index) from list
     */
    public final void select(int num) {
        actions.select(num, this::selectAction);
    }

    /**
     * @return Get name of the selected Element
     */
    public final String getSelected() {
        return actions.getSelected(this::getSelectedAction);
    }

    /**
     * @return Get index of the selected Element
     */
    public final int getSelectedIndex() {
        return actions.getSelectedIndex(this::getSelectedIndexAction);
    }

    protected final boolean isSelectedAction(String name) {
        return getSelectedAction().equals(name);
    }

    protected final boolean isSelectedAction(int num) {
        return getSelectedIndexAction() == num;
    }

    protected String getValueAction() {
        return getSelected();
    }

    protected String getSelectedAction() {
        return getSelected(getElements());
    }

    private String getSelected(List<WebElement> els) {
        WebElement element = first(els, this::isSelectedAction);
        if (element == null)
            throw exception("No elements selected. Override getSelectedAction or place locator to <select> tag");
        new Element(element).invoker.processDemoMode();
        return element.getText();
    }

    protected int getSelectedIndexAction() {
        return getSelectedIndex(getElements());
    }

    private int getSelectedIndex(List<WebElement> els) {
        int num = firstIndex(els, this::isSelectedAction) + 1;
        if (num == 0)
            throw exception("No elements selected. Override getSelectedAction or place locator to <select> tag");
        return num;
    }
}