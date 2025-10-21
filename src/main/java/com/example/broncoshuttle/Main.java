package com.example.broncoshuttle;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Bronco Shuttle Redesigned Application - Matching Figma Design
 * CS 2450 - Programming Graphical User Interfaces
 */
public class Main extends Application {
    private Label lastUpdatedLabel;
    private List<CountdownTimer> timers = new ArrayList<>();
    private ComboBox<String> stopDropdown;

    // Color Scheme - Updated to match Figma
    private static final String NAVY_BLUE = "#2C3E50";
    private static final String LIGHT_GRAY_BG = "#F8F9FA";
    private static final String WHITE = "#ffffff";
    private static final String M1_COLOR = "#000000";
    private static final String ML_COLOR = "#6C757D";
    private static final String GREEN_LIVE = "#28a745";
    private static final String BORDER_COLOR = "#DEE2E6";
    private static final String TEXT_DARK = "#212529";
    private static final String TEXT_MUTED = "#6C757D";
    private static final String BLUE_ACCENT = "#007BFF";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bronco Shuttle");
        primaryStage.setWidth(560);
        primaryStage.setHeight(900);

        VBox mainContainer = new VBox();
        mainContainer.setStyle("-fx-background-color: " + LIGHT_GRAY_BG + ";");
        mainContainer.setSpacing(0);

        mainContainer.getChildren().add(createHeader());
        mainContainer.getChildren().add(createStopSelector());

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(createMainContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + LIGHT_GRAY_BG + ";");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        mainContainer.getChildren().add(scrollPane);

        Scene scene = new Scene(mainContainer);
        primaryStage.setScene(scene);
        primaryStage.show();

        startAllCountdownTimers();
    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.setStyle("-fx-background-color: " + NAVY_BLUE + "; -fx-padding: 20;");
        header.setSpacing(5);

        Label title = new Label("BRONCO SHUTTLE");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        title.setTextFill(Color.WHITE);

        HBox updateBox = new HBox(8);
        updateBox.setAlignment(Pos.CENTER_LEFT);

        Circle clockIcon = new Circle(8);
        clockIcon.setFill(Color.TRANSPARENT);
        clockIcon.setStroke(Color.web("#B0BEC5"));
        clockIcon.setStrokeWidth(1.5);

        lastUpdatedLabel = new Label("Updated: Just now");
        lastUpdatedLabel.setFont(Font.font("System", 12));
        lastUpdatedLabel.setTextFill(Color.web("#B0BEC5"));

        updateBox.getChildren().addAll(clockIcon, lastUpdatedLabel);
        header.getChildren().addAll(title, updateBox);

        return header;
    }

    private VBox createStopSelector() {
        VBox stopBox = new VBox();
        stopBox.setStyle("-fx-background-color: " + WHITE + "; -fx-padding: 20;");
        stopBox.setSpacing(10);

        HBox labelBox = new HBox(8);
        labelBox.setAlignment(Pos.CENTER_LEFT);

        Circle pinIcon = new Circle(6);
        pinIcon.setFill(Color.web(TEXT_MUTED));

        Label stopLabel = new Label("Select Your Stop:");
        stopLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        stopLabel.setTextFill(Color.web(TEXT_DARK));

        labelBox.getChildren().addAll(pinIcon, stopLabel);

        stopDropdown = new ComboBox<>();
        stopDropdown.setPromptText("Choose a stop to see arrivals...");
        stopDropdown.getItems().addAll(
                "Metrolink 2",
                "Collins College/Kellogg West (Northbound)",
                "     Agriscapes/Farm Store",
                "Parking Structure 1",
                "PS2 Northwest",
                "College of Environmental Design",
                "Building 94",
                "Residents Hall",
                "     Student Services Building",
                "Parking Lot B1",
                "The Village",
                "Metrolink 1",
                "Interim Design Center Building",
                "SSB",
                "     Lyle Center",
                "Village",
                "Design Center",
                "SSB2",
                "Parking Structure 1 (South)",
                "Red Gum / University",
                "Residents Hall (South)",
                "Building 1",
                "Student Health Center",
                "Collins College",
                "     PS2 Across",
                "The Current",
                "Student Services Building (Main)",
                "Pomona North Metrolink"
        );
        stopDropdown.setStyle(
                "-fx-font-size: 13; " +
                        "-fx-padding: 10; " +
                        "-fx-background-color: " + LIGHT_GRAY_BG + "; " +
                        "-fx-border-color: " + BORDER_COLOR + "; " +
                        "-fx-border-radius: 4;"
        );
        stopDropdown.setPrefWidth(520);

        stopBox.getChildren().addAll(labelBox, stopDropdown);
        return stopBox;
    }

    private VBox createMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Active Routes Section
        Label activeLabel = new Label("Active Routes");
        activeLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        activeLabel.setTextFill(Color.web(TEXT_DARK));

        content.getChildren().add(activeLabel);
        content.getChildren().add(createRouteCard("M1", "Main Campus Loop 1", true, 180, "Metrolink 2", "Moderate"));
        content.getChildren().add(createRouteCard("M2", "Main Campus Loop 2", true, 420, "Student Services Building", "Low"));

        // Inactive Routes Section
        Label inactiveLabel = new Label("Currently Not Running");
        inactiveLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        inactiveLabel.setTextFill(Color.web(TEXT_DARK));
        inactiveLabel.setPadding(new Insets(10, 0, 0, 0));

        content.getChildren().add(inactiveLabel);
        content.getChildren().add(createInactiveRouteCard("ML", "Metrolink & Metro A Line Shuttle", "7:00 AM"));
        content.getChildren().add(createInactiveRouteCard("CU", "Current Apartments Shuttle", "7:30 AM"));

        return content;
    }

    private VBox createRouteCard(String code, String name, boolean isActive, int seconds, String nextStop, String capacity) {
        VBox card = new VBox(15);
        card.setStyle(
                "-fx-background-color: " + WHITE + "; " +
                        "-fx-border-color: " + BORDER_COLOR + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 20;"
        );

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);

        Label codeLabel = new Label(code);
        codeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        codeLabel.setTextFill(Color.WHITE);
        codeLabel.setStyle(
                "-fx-background-color: " + M1_COLOR + "; " +
                        "-fx-padding: 8 12; " +
                        "-fx-background-radius: 4;"
        );

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 16));
        nameLabel.setTextFill(Color.web(TEXT_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox statusBox = new HBox(6);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.setStyle(
                "-fx-background-color: #D4EDDA; " +
                        "-fx-padding: 6 12; " +
                        "-fx-background-radius: 12;"
        );

        Circle liveCircle = new Circle(4);
        liveCircle.setFill(Color.web(GREEN_LIVE));

        Label liveLabel = new Label("LIVE");
        liveLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        liveLabel.setTextFill(Color.web(GREEN_LIVE));

        statusBox.getChildren().addAll(liveCircle, liveLabel);
        header.getChildren().addAll(codeLabel, nameLabel, spacer, statusBox);

        // Info Section
        HBox infoBox = new HBox();
        infoBox.setSpacing(40);

        VBox nextStopBox = new VBox(5);
        Label nextStopTitle = new Label("Next Stop:");
        nextStopTitle.setFont(Font.font("System", 12));
        nextStopTitle.setTextFill(Color.web(TEXT_MUTED));

        Label nextStopValue = new Label(nextStop);
        nextStopValue.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        nextStopValue.setTextFill(Color.web(TEXT_DARK));

        nextStopBox.getChildren().addAll(nextStopTitle, nextStopValue);

        VBox arrivalBox = new VBox(5);
        arrivalBox.setAlignment(Pos.TOP_RIGHT);
        Label arrivalTitle = new Label("Arrives in:");
        arrivalTitle.setFont(Font.font("System", 12));
        arrivalTitle.setTextFill(Color.web(TEXT_MUTED));

        Label arrivalValue = new Label(formatSecondsShort(seconds));
        arrivalValue.setFont(Font.font("System", FontWeight.BOLD, 20));
        arrivalValue.setTextFill(Color.web(TEXT_DARK));

        arrivalBox.getChildren().addAll(arrivalTitle, arrivalValue);
        timers.add(new CountdownTimer(arrivalValue, seconds));

        Region infoSpacer = new Region();
        HBox.setHgrow(infoSpacer, Priority.ALWAYS);

        infoBox.getChildren().addAll(nextStopBox, infoSpacer, arrivalBox);

        // Capacity
        Label capacityLabel = new Label("Capacity: " + capacity);
        capacityLabel.setFont(Font.font("System", 13));
        capacityLabel.setTextFill(Color.web(TEXT_MUTED));

        // Buttons
        HBox buttons = new HBox(10);

        Button mapBtn = new Button("View Route Map");
        mapBtn.setFont(Font.font("System", FontWeight.SEMI_BOLD, 13));
        mapBtn.setStyle(
                "-fx-background-color: " + WHITE + "; " +
                        "-fx-text-fill: " + TEXT_DARK + "; " +
                        "-fx-border-color: " + BORDER_COLOR + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-padding: 10 20; " +
                        "-fx-cursor: hand;"
        );
        mapBtn.setOnAction(e -> showRouteMap(code, name, nextStop, seconds));

        Button alertBtn = new Button("Set Alert");
        alertBtn.setFont(Font.font("System", FontWeight.SEMI_BOLD, 13));
        alertBtn.setStyle(
                "-fx-background-color: " + WHITE + "; " +
                        "-fx-text-fill: " + TEXT_DARK + "; " +
                        "-fx-border-color: " + BORDER_COLOR + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-padding: 10 20; " +
                        "-fx-cursor: hand;"
        );
        alertBtn.setOnAction(e -> showAlert("Alert Set", "You'll be notified when " + code + " arrives."));

        HBox.setHgrow(mapBtn, Priority.ALWAYS);
        HBox.setHgrow(alertBtn, Priority.ALWAYS);
        buttons.getChildren().addAll(mapBtn, alertBtn);

        card.getChildren().addAll(header, infoBox, capacityLabel, buttons);
        return card;
    }

    private VBox createInactiveRouteCard(String code, String name, String resumeTime) {
        VBox card = new VBox(12);
        card.setStyle(
                "-fx-background-color: " + WHITE + "; " +
                        "-fx-border-color: " + BORDER_COLOR + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 20;"
        );

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);

        Label codeLabel = new Label(code);
        codeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        codeLabel.setTextFill(Color.WHITE);
        codeLabel.setStyle(
                "-fx-background-color: " + ML_COLOR + "; " +
                        "-fx-padding: 8 12; " +
                        "-fx-background-radius: 4;"
        );

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 16));
        nameLabel.setTextFill(Color.web(TEXT_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label statusLabel = new Label("NOT RUNNING");
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        statusLabel.setTextFill(Color.web(TEXT_MUTED));
        statusLabel.setStyle(
                "-fx-background-color: " + LIGHT_GRAY_BG + "; " +
                        "-fx-padding: 6 12; " +
                        "-fx-background-radius: 12;"
        );

        header.getChildren().addAll(codeLabel, nameLabel, spacer, statusLabel);

        Label resumeLabel = new Label("Resumes at " + resumeTime);
        resumeLabel.setFont(Font.font("System", 13));
        resumeLabel.setTextFill(Color.web(TEXT_MUTED));

        card.getChildren().addAll(header, resumeLabel);
        return card;
    }

    private String formatSecondsShort(int seconds) {
        int mins = seconds / 60;
        return mins + " min";
    }

    private void startAllCountdownTimers() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    for (CountdownTimer timer : timers) {
                        timer.decrementAndUpdate();
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void showRouteMap(String code, String name, String approaching, int eta) {
        Stage mapStage = new Stage();
        mapStage.setTitle("Route Map");
        mapStage.setWidth(500);
        mapStage.setHeight(850);

        VBox container = new VBox();
        container.setStyle("-fx-background-color: " + LIGHT_GRAY_BG + ";");

        // Header
        HBox header = new HBox();
        header.setStyle("-fx-background-color: " + NAVY_BLUE + "; -fx-padding: 15;");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(15);

        Button backBtn = new Button("←");
        backBtn.setFont(Font.font("System", FontWeight.BOLD, 18));
        backBtn.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: white; " +
                        "-fx-cursor: hand;"
        );
        backBtn.setOnAction(e -> mapStage.close());

        Label headerLabel = new Label("Route Map");
        headerLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        headerLabel.setTextFill(Color.WHITE);

        header.getChildren().addAll(backBtn, headerLabel);

        // Content
        VBox content = new VBox(15);
        content.setStyle("-fx-background-color: white; -fx-padding: 20;");

        HBox routeHeader = new HBox();
        routeHeader.setAlignment(Pos.CENTER_LEFT);
        routeHeader.setSpacing(10);

        Label routeCode = new Label(code);
        routeCode.setFont(Font.font("System", FontWeight.BOLD, 14));
        routeCode.setTextFill(Color.WHITE);
        routeCode.setStyle(
                "-fx-background-color: " + M1_COLOR + "; " +
                        "-fx-padding: 6 10; " +
                        "-fx-background-radius: 4;"
        );

        Label routeName = new Label(name);
        routeName.setFont(Font.font("System", FontWeight.BOLD, 16));

        Region rSpacer = new Region();
        HBox.setHgrow(rSpacer, Priority.ALWAYS);

        HBox liveBox = new HBox(6);
        liveBox.setAlignment(Pos.CENTER);
        liveBox.setStyle(
                "-fx-background-color: #D4EDDA; " +
                        "-fx-padding: 4 10; " +
                        "-fx-background-radius: 12;"
        );

        Circle lCircle = new Circle(4);
        lCircle.setFill(Color.web(GREEN_LIVE));

        Label lLabel = new Label("LIVE");
        lLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        lLabel.setTextFill(Color.web(GREEN_LIVE));

        liveBox.getChildren().addAll(lCircle, lLabel);
        routeHeader.getChildren().addAll(routeCode, routeName, rSpacer, liveBox);

        // Location info
        HBox locationBox = new HBox(8);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        Circle navIcon = new Circle(6);
        navIcon.setFill(Color.web(TEXT_MUTED));

        Label approachingLabel = new Label("Approaching " + approaching);
        approachingLabel.setFont(Font.font("System", 14));
        approachingLabel.setTextFill(Color.web(TEXT_DARK));

        locationBox.getChildren().addAll(navIcon, approachingLabel);

        HBox updateBox = new HBox(8);
        updateBox.setAlignment(Pos.CENTER_LEFT);

        Circle clockIcon = new Circle(6);
        clockIcon.setFill(Color.TRANSPARENT);
        clockIcon.setStroke(Color.web(TEXT_MUTED));

        Label updateLabel = new Label("Updated: Just now");
        updateLabel.setFont(Font.font("System", 12));
        updateLabel.setTextFill(Color.web(TEXT_MUTED));

        updateBox.getChildren().addAll(clockIcon, updateLabel);

        // Map area
        VBox mapArea = new VBox(20);
        mapArea.setAlignment(Pos.CENTER);
        mapArea.setStyle(
                "-fx-border-color: " + BORDER_COLOR + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-style: dashed; " +
                        "-fx-border-radius: 8; " +
                        "-fx-padding: 40;"
        );
        mapArea.setPrefHeight(250);

        Circle mapIcon = new Circle(30);
        mapIcon.setFill(Color.web("#CED4DA"));

        Label mapText1 = new Label("Interactive Map Area");
        mapText1.setFont(Font.font("System", FontWeight.BOLD, 16));
        mapText1.setTextFill(Color.web(TEXT_MUTED));

        Label mapText2 = new Label("Shows shuttle location in real-time");
        mapText2.setFont(Font.font("System", 13));
        mapText2.setTextFill(Color.web(TEXT_MUTED));

        Label mapText3 = new Label("with route path and stops");
        mapText3.setFont(Font.font("System", 13));
        mapText3.setTextFill(Color.web(TEXT_MUTED));

        Circle blueCircle = new Circle(25);
        blueCircle.setFill(Color.web(BLUE_ACCENT));

        Label positionLabel = new Label("Live Shuttle Position");
        positionLabel.setFont(Font.font("System", 12));
        positionLabel.setTextFill(Color.web(TEXT_MUTED));

        mapArea.getChildren().addAll(mapIcon, mapText1, mapText2, mapText3, blueCircle, positionLabel);

        // Stops list
        Label stopsHeader = new Label("Upcoming Stops");
        stopsHeader.setFont(Font.font("System", FontWeight.BOLD, 16));
        stopsHeader.setTextFill(Color.web(TEXT_DARK));

        VBox stopsList = new VBox(10);
        stopsList.getChildren().add(createStopItem("1", "Parking Structure 1", 2, false));
        stopsList.getChildren().add(createStopItem("2", approaching, eta/60, true));
        stopsList.getChildren().add(createStopItem("3", "Metrolink 1", 5, false));
        stopsList.getChildren().add(createStopItem("4", "Interim Design Center Building", 8, false));
        stopsList.getChildren().add(createStopItem("5", "SSB", 11, false));
        stopsList.getChildren().add(createStopItem("6", "Village", 14, false));

        Button alertBtn = new Button("Set Arrival Alert");
        alertBtn.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        alertBtn.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: " + TEXT_DARK + "; " +
                        "-fx-border-color: " + BORDER_COLOR + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 12; " +
                        "-fx-cursor: hand;"
        );
        alertBtn.setPrefWidth(460);

        content.getChildren().addAll(routeHeader, locationBox, updateBox, mapArea, stopsHeader, stopsList, alertBtn);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + LIGHT_GRAY_BG + ";");

        container.getChildren().addAll(header, scroll);

        Scene scene = new Scene(container);
        mapStage.setScene(scene);
        mapStage.show();
    }

    private HBox createStopItem(String num, String name, int mins, boolean isNext) {
        HBox item = new HBox();
        item.setAlignment(Pos.CENTER_LEFT);
        item.setSpacing(15);
        item.setStyle(
                "-fx-background-color: " + (isNext ? "#E7F3FF" : WHITE) + "; " +
                        "-fx-border-color: " + (isNext ? BLUE_ACCENT : BORDER_COLOR) + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 15;"
        );

        Circle numCircle = new Circle(18);
        numCircle.setFill(Color.web(isNext ? BLUE_ACCENT : "#CED4DA"));

        Label numLabel = new Label(num);
        numLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        numLabel.setTextFill(Color.WHITE);

        StackPane numStack = new StackPane(numCircle, numLabel);

        VBox nameBox = new VBox(3);
        Label stopName = new Label(name);
        stopName.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        stopName.setTextFill(Color.web(TEXT_DARK));

        nameBox.getChildren().add(stopName);

        if (isNext) {
            Label nextLabel = new Label("Next Stop");
            nextLabel.setFont(Font.font("System", 11));
            nextLabel.setTextFill(Color.web(BLUE_ACCENT));
            nameBox.getChildren().add(nextLabel);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label timeLabel = new Label(mins + " min");
        timeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        timeLabel.setTextFill(Color.web(isNext ? BLUE_ACCENT : TEXT_DARK));

        item.getChildren().addAll(numStack, nameBox, spacer, timeLabel);
        return item;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class CountdownTimer {
        private Label label;
        private int secondsRemaining;

        public CountdownTimer(Label label, int initialSeconds) {
            this.label = label;
            this.secondsRemaining = initialSeconds;
        }

        public void decrementAndUpdate() {
            if (secondsRemaining > 0) {
                secondsRemaining--;
                int mins = secondsRemaining / 60;
                label.setText(mins + " min");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}