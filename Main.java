package sample;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class Main extends Application {
    Stage window;
    TextField youtubeLink, extractedCodeResultLbl;
    Label youtubelabel, deckCodeLabel;
    WebView webview;
    String x = " ";

    public static void main(String[] args) {
        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        webview = new WebView();


        window = primaryStage;
        window.setTitle("Deck Code extractor");

        youtubeLink = new TextField();
        youtubeLink.setPromptText("Link");

        youtubeLink.setMinWidth(100);
        youtubeLink.setPrefWidth(250);

        youtubelabel = new Label("Youtube Link: ");
        deckCodeLabel = new Label("Deck code: ");
        extractedCodeResultLbl = new TextField();
        extractedCodeResultLbl.setPromptText("Code");
        extractedCodeResultLbl.setPrefWidth(350);
        extractedCodeResultLbl.setPrefHeight(10);

        javafx.scene.control.Button addButton = new javafx.scene.control.Button("Extract Code");
        addButton.setOnAction(e -> transferButtonClicked());

        HBox topBox = new HBox();
        topBox.setPadding(new Insets(10, 10, 10, 10));
        topBox.setSpacing(10);
        topBox.getChildren().addAll(youtubelabel, youtubeLink, addButton);
        //

        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(10, 10, 10, 20));
        bottomBox.setSpacing(10);
        bottomBox.getChildren().addAll(deckCodeLabel, extractedCodeResultLbl);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(topBox, bottomBox);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();


    }

    public void transferButtonClicked() {

        extragereTitlu(youtubeLink.getCharacters().toString());


    }

    public void extragereTitlu(String videoLink) {
        try {
            final WebEngine webengine = webview.getEngine();
            webengine.getLoadWorker().stateProperty().addListener(
                    (ov, oldState, newState) -> {
                        if (newState == Worker.State.SUCCEEDED) {
                            Document doc = webengine.getDocument();
                            try {
                                StringWriter sw = new StringWriter();
                                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                                String x = "";
                                transformer.transform(new DOMSource(doc),
                                        new StreamResult(sw));
                                findDeckCodes(sw.toString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            // findDeckCodes(doc.getElementById("description").toString());
                        }
                    });
            webengine.load(videoLink);


        } catch (Exception exception) {
            exception.getMessage();
        }

    }

    private void findDeckCodes(String longAssString) {

        String[] parts = longAssString.split("\\r?\\n");
        String aux = "";
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains("AAE") && parts[i].length() <= 130 && parts[i].length() >= 60) {
                aux = parts[i];
                aux = aux.substring(aux.indexOf("AAE"));
                System.out.println(aux);
            }
        }

    }


}
