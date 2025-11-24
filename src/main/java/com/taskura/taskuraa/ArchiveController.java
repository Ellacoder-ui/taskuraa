package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.collections.ListChangeListener;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ArchiveController {

    @FXML private Label welcomeLabel;
    @FXML private Label leftNameLabel;
    @FXML private Label leftRoleLabel;

    @FXML private ListView<Task> archiveListView;
    // Changed to Labels so archives are view-only (no editing allowed)
    @FXML private Label aTitleLabel;
    @FXML private Label aDescLabel;
    @FXML private Label aDeadlineLabel;

    @FXML private Button backBtn; // the small arrow-only back button placed under the purple header
    @FXML private VBox detailsPane; // the right-side details pane — hide when archives empty

    private User user;
    private Task selected = null;

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            welcomeLabel.setText("Hi, " + user.getUsername());
            leftNameLabel.setText(user.getUsername());
            leftRoleLabel.setText(user.getRole());
        }
    }

    @FXML
    public void initialize() {
        // populate list
        archiveListView.setItems(InMemoryData.ARCHIVES);

        // cell text
        archiveListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(item.getTitle() + " — " + (item.getDeadline() != null ? item.getDeadline().format(DateTimeFormatter.ofPattern("d MMM yyyy")) : ""));
                }
            }
        });

        // selection listener
        archiveListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            selected = newV;
            loadSelected();
        });

        // update details pane visibility depending on whether archives list is empty
        updateDetailsVisibility();

        // listen for dynamic changes to ARCHIVES to keep UI in sync
        InMemoryData.ARCHIVES.addListener((ListChangeListener<Task>) change -> updateDetailsVisibility());
    }

    private void updateDetailsVisibility() {
        boolean hasAny = !InMemoryData.ARCHIVES.isEmpty();
        detailsPane.setVisible(hasAny);
        detailsPane.setManaged(hasAny);

        // if there are no archived items, also clear selection and fields
        if (!hasAny) {
            archiveListView.getSelectionModel().clearSelection();
            aTitleLabel.setText("");
            aDescLabel.setText("");
            aDeadlineLabel.setText("");
        } else {
            // if there is at least one and nothing is selected, select the first to show details
            if (archiveListView.getSelectionModel().getSelectedItem() == null && !InMemoryData.ARCHIVES.isEmpty()) {
                archiveListView.getSelectionModel().select(0);
            }
        }
    }

    private void loadSelected() {
        if (selected == null) {
            aTitleLabel.setText("");
            aDescLabel.setText("");
            aDeadlineLabel.setText("");
            return;
        }
        // populate view-only labels
        aTitleLabel.setText(selected.getTitle());
        aDescLabel.setText(selected.getDescription() == null ? "" : selected.getDescription());
        aDeadlineLabel.setText(selected.getDeadline() != null ? selected.getDeadline().format(DateTimeFormatter.ofPattern("d MMM yyyy")) : "");
    }

    @FXML
    private void onRestore(ActionEvent e) {
        if (selected == null) return;
        InMemoryData.ARCHIVES.remove(selected);
        InMemoryData.TASKS.add(0, selected);
        archiveListView.getSelectionModel().clearSelection();
        loadSelected();
    }

    @FXML
    private void onDeletePermanent(ActionEvent e) {
        if (selected == null) return;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Delete Permanently");
        a.setHeaderText("Delete \"" + selected.getTitle() + "\" permanently?");
        Optional<ButtonType> res = a.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            InMemoryData.ARCHIVES.remove(selected);
            archiveListView.getSelectionModel().clearSelection();
            loadSelected();
        }
    }

    /**
     * Back button action: navigate back to the Task page (taskpage.fxml) and pass the user.
     * The back button is a small arrow-only button placed beneath the purple header.
     */
    @FXML
    private void onBack(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/taskura/taskuraa/taskpage.fxml"));
            Parent root = loader.load();
            TaskPageController ctrl = loader.getController();
            ctrl.setUser(this.user);
            Stage stage = (Stage) archiveListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Taskura - Tasks");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}