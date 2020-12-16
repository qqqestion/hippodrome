package ru.emoji.tashkent.utils;

import ru.emoji.tashkent.enums.ActionEnum;
import ru.emoji.tashkent.Application;
import ru.emoji.tashkent.database.entity.User;
import ru.emoji.tashkent.database.manager.Manager;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ModelViewForm<E>
        extends MainForm implements DatabaseUsage, OnKeyClickListenerListener {
    protected final User user = Application.getInstance().getUser();
    protected final Manager<E> manager;
    protected JTextField[] fields;
    protected JComboBox<E> searchField;
    private boolean canBeEdited = true;
    private OnKeyClickListenerListener onKeyClickListenerListener;

    public ModelViewForm(ActionEnum action, Manager<E> manager) {
        super(action);
        this.manager = manager;
        setOnSaveListener(this);
    }

    protected void initSearchFieldAndFields() {
        updateSearchField();
        setEditableToFields();
        updateFields();
    }

    protected void updateFields() {
        E object = (E) searchField.getSelectedItem();
        if (object == null) {
            return;
        }
        List<String> values = getValuesFromModel(object);
        for (int i = 0; i < fields.length; i++) {
            fields[i].setText(values.get(i));
        }
    }

    protected void onItemAdded(int id) {
        updateSearchField();
        try {
            E item = manager.getById(id);
            for (int i = 0; i < searchField.getItemCount(); i++) {
                System.out.println(searchField.getItemAt(i));
            }
            searchField.setSelectedItem(item);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateSearchField() {
        searchField.removeAllItems();
        List<E> modelList;
        try {
            modelList = manager.getAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            showError(this);
            return;
        }
        System.out.println("Model list: " + modelList);
        for (int i = 0; i < modelList.size(); i++) {
            searchField.addItem(modelList.get(i));
        }
        if (user.isAdmin() && canBeEdited) {
            searchField.addItem(manager.createEntity());
        }
        System.out.println("Fields updated");
    }

    protected abstract List<String> getValuesFromModel(E element);

    protected void setEditableToFields() {
        ActionOnEnterListener keyListener = new ActionOnEnterListener(onKeyClickListenerListener);
        for (int i = 0; i < fields.length; i++) {
            boolean isEnable = user.isAdmin() && canBeEdited;
            fields[i].setEditable(isEnable);
            fields[i].addKeyListener(isEnable ? keyListener : null);
        }
    }

    public void setFields(JTextField[] fields) {
        this.fields = fields;
    }

    public void setSearchField(JComboBox searchField) {
        this.searchField = searchField;
    }

    public void setOnSaveListener(OnKeyClickListenerListener listener) {
        onKeyClickListenerListener = listener;
    }

    public void setCanBeEdited(boolean canBeEdited) {
        this.canBeEdited = canBeEdited;
    }

    protected List<String> fillStringList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            list.add("");
        }
        return list;
    }

    protected void onDelete() {
        E object = (E) searchField.getSelectedItem();
        System.out.println(object + " trying to delete");
        try {
            manager.delete(object);
            updateSearchField();
            System.out.println(object + " deleted");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            showError(this);
        }
    }

    protected void initDeleteButton(JButton button) {
        if (!user.isAdmin()) {
            button.setVisible(false);
            button.setEnabled(false);
        }
        button.addActionListener(e -> {
            System.out.println("Delete button clicked!");
            int confirm = JOptionPane.showConfirmDialog(this, "Вы точно хотите удалить данный объект?");
            System.out.println(confirm);
            if (confirm == 0) {
                onDelete();
            }
        });
    }

    @Override
    public void onSave() {
    }
}
