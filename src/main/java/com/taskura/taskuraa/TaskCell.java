package com.taskura.taskuraa;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TaskCell extends ListCell<Task> {
    private HBox root = new HBox(12);
    private VBox texts = new VBox(4);
    private Label title = new Label();
    private Label small = new Label();
    private Circle tagCircle = new Circle(8);

    public TaskCell() {
        super();
        title.setStyle("-fx-font-weight:600;");
        small.setStyle("-fx-font-size:11px; -fx-text-fill: #666666;");
        texts.getChildren().addAll(title, small);
        root.getChildren().addAll(tagCircle, texts);
        root.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
        } else {
            title.setText(item.getTitle());
            String desc = item.getDescription() != null ? item.getDescription() : "";
            if (desc.length() > 45) desc = desc.substring(0, 42) + "...";
            small.setText(desc);
            switch (item.getTagColor()) {
                case "red": tagCircle.setFill(Color.web("#ff6b6b")); break;
                case "orange": tagCircle.setFill(Color.web("#ffa94d")); break;
                default: tagCircle.setFill(Color.web("#6bd26b")); break;
            }
            setGraphic(root);
        }
    }
}