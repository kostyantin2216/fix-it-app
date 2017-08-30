package com.fixit.feedback;

import com.fixit.ui.adapters.MultiSelectRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kostyantin on 8/20/2017.
 */

public class ChoiceSelection {
    public final Object value;
    public final String display;

    private ChoiceSelection(Object value, String display) {
        this.value = value;
        this.display = display;
    }

    public static class Choices {
        private final ChoiceSelection[] choices;

        private Choices(List<ChoiceSelection> selections) {
            this.choices = selections.toArray(new ChoiceSelection[selections.size()]);
        }

        public MultiSelectRecyclerAdapter.SelectItem[] toSelectItems() {
            MultiSelectRecyclerAdapter.SelectItem[] selectItems = new MultiSelectRecyclerAdapter.SelectItem[choices.length];
            for(int i = 0; i < choices.length; i++) {
                selectItems[i] = new MultiSelectRecyclerAdapter.SelectItem(i, choices[i].display);
            }
            return selectItems;
        }

        public List<Object> extractChoiceValues(MultiSelectRecyclerAdapter.SelectItem[] selectItems) {
            List<Object> values = new ArrayList<>();
            for(MultiSelectRecyclerAdapter.SelectItem selectItem : selectItems) {
                values.add(get(selectItem.code).value);
            }
            return values;
        }

        public ChoiceSelection get(int index) {
            return choices[index];
        }
    }

    public static class Builder {
        private final List<ChoiceSelection> selections = new ArrayList<>();

        public Builder add(String display, Object value) {
            selections.add(new ChoiceSelection(value, display));
            return this;
        }

        public List<ChoiceSelection> build() {
            return selections;
        }

        public Choices toChoices() {
            return new Choices(selections);
        }
    }
}
