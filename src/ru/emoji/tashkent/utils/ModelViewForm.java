package ru.emoji.tashkent.utils;

import ru.emoji.tashkent.ActionEnum;
import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.Manager;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public abstract class ModelViewForm<E> extends MainForm implements DatabaseUsage {
    protected final User user = Application.getInstance().getUser();
    protected final Manager<E> manager;
    protected JTextField[] fields;
    protected JComboBox<E> searchField;
    private boolean canBeEdited = true;

    public ModelViewForm(ActionEnum action, Manager<E> manager) {
        super(action);
        this.manager = manager;
    }

    protected void initSearchFieldAndFields() {
        List<E> modelList;
        try {
            modelList = manager.getAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            showError(this);
            return;
        }
        for (int i = 0; i < modelList.size(); i++) {
            searchField.addItem(modelList.get(i));
        }
        setEditableToFields();
        updateFields();
    }

    protected void updateFields() {
        List<String> values = getValuesFromModel((E)searchField.getSelectedItem());
        for (int i = 0; i < fields.length; i++) {
            fields[i].setText(values.get(i));
        }
    }

    protected abstract List<String> getValuesFromModel(E element);

    protected void setEditableToFields() {
        for (int i = 0; i < fields.length; i++) {
            fields[i].setEditable(user.isAdmin() && canBeEdited);
        }
    }

    public void setFields(JTextField[] fields) {
        this.fields = fields;
    }

    public void setSearchField(JComboBox searchField) {
        this.searchField = searchField;
    }

    public void setCanBeEdited(boolean canBeEdited) {
        this.canBeEdited = canBeEdited;
    }
}
