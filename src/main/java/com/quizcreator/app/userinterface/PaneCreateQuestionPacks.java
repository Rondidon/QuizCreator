package com.quizcreator.app.userinterface;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import com.quizcreator.app.data.Program;
import com.quizcreator.app.data.Question;
import com.quizcreator.app.data.QuestionContainer;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class PaneCreateQuestionPacks implements Initializable {
	private ResourceBundle bundle = com.quizcreator.app.i18n.Locales.getGUIBundle();
	private static QuestionContainer selectedQC = null;
	
	@FXML BorderPane root;
	
	// FXML declarations: Edit Question Containers (left side)
	@FXML Button buttonAddQuestionPack;
	@FXML ImageView imageViewAddQuestionPack;
	@FXML TableView<QuestionContainer> tableViewQuestionPack;
	@FXML TableColumn<QuestionContainer, String> columnDescription;
	@FXML TableColumn<QuestionContainer, Integer> columnQuestionAmount;
	@FXML Label labelSelected;
	@FXML Button buttonClose;
	
	// FXML declarations: Edit Questions (right side)
	@FXML BorderPane questionPane;
	@FXML TableView<Question> tableViewQuestion;
	@FXML TableColumn<Question, String> columnQuestion;
	@FXML TableColumn<Question, String> columnType;
	@FXML TableColumn<Question, Integer> columnAnswerAmount;
	@FXML Button buttonAddQuestion;
	@FXML ImageView imageViewAddQuestion;
	
	// FXML declarations: Add / Rename Question pack
	@FXML Button buttonCancel;
	@FXML Button buttonCreate;
	@FXML TextField textField;
	@FXML Label labelHeaderAddQuestionPack;
	
	/**
	 * Handles the button actions
	 * @param e
	 */
	@FXML
	private void handleButtonAction(ActionEvent e) {
		Object source = e.getSource();
		
		// Buttons below the Tables
		// Add Question Button
		if(source == buttonAddQuestion) {
			showAddQuestionStage();
		}
		// Add Question Pack Button
		if(source == buttonAddQuestionPack) {
			showAddQuestionPackStage();
		}
		
		// Buttons for the Create Question pack stage:
		// Cancel Button
		if(source == buttonCancel) {
			WindowManager.setNullEffect("editorStage");
			WindowManager.closeStage("addQuestionPackStage");
		}
		// Create Button
		if(source == buttonCreate) {
			createQuestionPack();
		}
		// Close Button
		if(source == buttonClose) {
			selectQuestionContainer(null);
			tableViewQuestionPack.getSelectionModel().clearSelection();
		}
	}
	
	/**
	 * opens and shows the question stage
	 */
	void showAddQuestionStage()
	{
		Stage s = new Stage();
		WindowManager.addStage(s, "addQuestionStage");
		
		s.setTitle(bundle.getString("title_addquestion"));
		s.setResizable(true);
		s.setAlwaysOnTop(true);
		s.initModality(Modality.WINDOW_MODAL);
		s.initOwner(WindowManager.getStage("editorStage"));
		
		WindowManager.setBoxEffect("editorStage");
		WindowManager.bindStagePositionRelativeToParent(s, WindowManager.getStage("editorStage"));
		
		s.centerOnScreen();
		
		Question q = SceneAddQuestion.showScene(s);
		if(q != null) {
			selectedQC.addToQuestionList(q);
		}
		refreshQuestionTable(selectedQC);
	}
	
	/**
	 * Shows the "add a question pack" stage
	 */
	private void showAddQuestionPackStage() {
		
		try {
			FXMLLoader loader = new FXMLLoader(
			        getClass().getResource("SceneAddQuestionPack.fxml"), bundle
			);
			loader.setController(this);
			Scene scene = new Scene(loader.load());
			
			buttonCreate.setDisable(true);
			
			textField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					buttonCreate.setDisable(true);
					textField.setText(checkRegex(newValue));
					if(textField.getText().length() > 0 && !Program.getProject().getQuiz().checkIfQuestionContainerExists(textField.getText())) {
						buttonCreate.setDisable(false);
					}
				}
			});
			
			Stage s = new Stage();
			s.setScene(scene);
			s.setTitle(bundle.getString("title_addquestionpack"));
			s.initModality(Modality.WINDOW_MODAL);
			s.setAlwaysOnTop(true);
			s.setResizable(false);
			WindowManager.addStage(s, "addQuestionPackStage");
			s.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent event) {
					WindowManager.setNullEffect("editorStage");
					WindowManager.closeStage("addQuestionPackStage");
				}
			});
			s.show();
			WindowManager.bindStagePositionRelativeToParent(s, WindowManager.getStage("editorStage"));
			WindowManager.setBoxEffect("editorStage");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * shows the "Rename question pack" stage
	 */
	private void showRenameQuestionPackStage(QuestionContainer qc) {
		
		try {
			bundle = ResourceBundle.getBundle("de.quizgamemaker.userinterface.LanguageBundle", Locale.getDefault());
			FXMLLoader loader = new FXMLLoader(
			        getClass().getResource("SceneAddQuestionPack.fxml"), bundle
			);
			loader.setController(this);
			Scene scene = new Scene(loader.load());
			
			buttonCreate.setDisable(false);
			buttonCreate.setText(bundle.getString("button_okay"));
			
			textField.setText(qc.getDescription());
			labelHeaderAddQuestionPack.setText(new String(bundle.getString("text_headerrenamequestionpack") + "\n\"" + qc.getDescription() + "\":"));
			
			textField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					buttonCreate.setDisable(true);
					textField.setText(checkRegex(newValue));
					if(textField.getText().length() > 0 && !Program.getProject().getQuiz().checkIfQuestionContainerExists(textField.getText()) 
							|| textField.getText().equals(qc.getDescription())) {
						buttonCreate.setDisable(false);
					}
				}
			});
			
			buttonCreate.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					qc.setDescription(textField.getText());
					WindowManager.setNullEffect("editorStage");
					WindowManager.closeStage("addQuestionPackStage");
					refreshQCTable();
				}
			});
			
			Stage s = new Stage();
			s.setScene(scene);
			s.setTitle(new String(bundle.getString("title_renamequestionpack") + " \"" + qc.getDescription() + "\" - " + bundle.getString("title_software")));
			s.initModality(Modality.WINDOW_MODAL);
			s.setAlwaysOnTop(true);
			s.setResizable(false);
			WindowManager.addStage(s, "addQuestionPackStage");
			s.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent event) {
					WindowManager.setNullEffect("editorStage");
					WindowManager.closeStage("addQuestionPackStage");
				}
			});
			s.show();
			WindowManager.bindStagePositionRelativeToParent(s, WindowManager.getStage("editorStage"));
			WindowManager.setBoxEffect("editorStage");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a question pack and adds it to the qc table
	 */
	private void createQuestionPack() {
		QuestionContainer qc = new QuestionContainer();
		qc.setDescription(textField.getText());
		qc.setAddedToQuiz(false);
		Program.getProject().getQuiz().addToEventList(qc);
		refreshQCTable();
		WindowManager.setNullEffect("editorStage");
		WindowManager.closeStage("addQuestionPackStage");
	}
	
	/**
	 * Removes a question from a question pack and also removes it from the table
	 * @param q the question
	 * @param qc the question container
	 */
	private void removeFromQTable(Question q, QuestionContainer qc) {
		WindowManager.setBoxEffect("editorStage");
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(new String(bundle.getString("title_remove") + " \"" + q.getText() + "\" - " + bundle.getString("title_software")));
		alert.setHeaderText(new String(bundle.getString("text_removeq")) + "\n\"" + q.getText() + "\"");
		alert.setContentText(bundle.getString("text_nonundo"));
		ButtonType buttonTypeRemove = new ButtonType(bundle.getString("button_remove"));
		ButtonType buttonTypeCancel = new ButtonType(bundle.getString("text_cancel"), ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(buttonTypeRemove, buttonTypeCancel);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeRemove) {
        	tableViewQuestion.getItems().remove(q);  
        	qc.removeFromQuestionList(q);
		}
		refreshQCTable();
		WindowManager.setNullEffect("editorStage");
	}
	
	/**
	 * Removes a Question Container and also deletes it from the table
	 * @param qc the question container
	 */
	private void removeFromQCTable(QuestionContainer qc) {
		WindowManager.setBoxEffect("editorStage");
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(new String(bundle.getString("title_remove") + " \"" + qc.getDescription() + "\" - " + bundle.getString("title_software")));
		alert.setHeaderText(new String(bundle.getString("text_removeqp")) + "\n\"" + qc.getDescription() + "\"?");
		alert.setContentText(bundle.getString("text_nonundo"));
		ButtonType buttonTypeRemove = new ButtonType(bundle.getString("button_remove"));
		ButtonType buttonTypeCancel = new ButtonType(bundle.getString("text_cancel"), ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(buttonTypeRemove, buttonTypeCancel);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeRemove) {
        	tableViewQuestionPack.getItems().remove(qc);  
        	Program.getProject().getQuiz().removeFromEventList(qc);
		}
		WindowManager.setNullEffect("editorStage");
	}
	
	/**
	 * Selects a question container in the table
	 */
	private void selectQuestionContainer(QuestionContainer qc) {
		if(qc == null) {
			selectedQC = null; // Zeiger auf den selektierten Question Container aufheben
			questionPane.setVisible(false);
		}
		else {
			selectedQC = qc; // Zeiger auf den selektierten Question Container initialisieren
			questionPane.setVisible(true);
			labelSelected.setText(new String(bundle.getString("label_headercreatequestions") + " \"" + qc.getDescription() + "\"" ));
			refreshQuestionTable(qc);
		}
	}
	
	/**
	 * Refreshes the question table dependent on the Question Container to display 
	 * @param qc the Question Container to display 
	 */
	private void refreshQuestionTable(QuestionContainer qc) {
		qc.refreshAnswerAmountForAllQuestions();
		ObservableList<Question> data = FXCollections.observableArrayList(qc.getQuestionList());
		tableViewQuestion.setItems(data);
		columnQuestion.setCellValueFactory(new PropertyValueFactory<Question, String>("text"));
		columnType.setCellValueFactory(new PropertyValueFactory<Question, String>("questionTypeAsString"));
		columnAnswerAmount.setCellValueFactory(new PropertyValueFactory<Question, Integer>("answerAmount"));
		
		  tableViewQuestion.setOnKeyPressed( new EventHandler<KeyEvent>()
          {
				@Override
				public void handle(KeyEvent event) {
					if(event.getCode().equals(KeyCode.DELETE)) {
						if(tableViewQuestion.getSelectionModel().getSelectedItem() != null) {
							removeFromQTable(tableViewQuestion.getSelectionModel().getSelectedItem(),qc);
						}
					}
				}
          } );
		
		tableViewQuestion.setRowFactory(new Callback<TableView<Question>, TableRow<Question>>() {  
            @Override  
            public TableRow<Question> call(TableView<Question> tableView) { 
            	
                final TableRow<Question> row = new TableRow<>();  
                final ContextMenu contextMenu = new ContextMenu();  
                final MenuItem removeMenuItem = new MenuItem(bundle.getString("button_remove"));  
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
                    @Override  
                    public void handle(ActionEvent event) { 
                    	removeFromQTable(row.getItem(),qc);
                    }  
                });  
                final MenuItem editMenuItem = new MenuItem(bundle.getString("button_edit"));
                editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						Stage s = new Stage();
						WindowManager.addStage(s, "addQuestionStage");
						Question q = tableView.getSelectionModel().getSelectedItem();
						
						s.setTitle(bundle.getString("title_editquestion") + ": " + q.getText());
						s.setResizable(true);
						s.setAlwaysOnTop(true);
						s.initModality(Modality.WINDOW_MODAL);
						s.initOwner(WindowManager.getStage("editorStage"));
						
						WindowManager.setBoxEffect("editorStage");
						WindowManager.bindStagePositionRelativeToParent(s, WindowManager.getStage("editorStage"));
						s.centerOnScreen();
						
						// Edit q by overwriting it with q1 if q1 is not null (meaning the dialog is not canceled)
						Question q1 = new Question();
						q1 = q;
						
						q1 = SceneAddQuestion.showScene(s,q1);
						if(q1 != null) {
							q = q1;
							q1 = null;
							refreshQuestionTable(selectedQC);
						}
						
					}
				});
     
                
                contextMenu.getItems().add(editMenuItem);  
                contextMenu.getItems().add(removeMenuItem);  
             
                
               // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                row.contextMenuProperty().bind(  
                        Bindings.when(row.emptyProperty())  
                        .then((ContextMenu)null)  
                        .otherwise(contextMenu)  
                );  
                
                // Set second click on selected item => unselect
                row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
                    @Override  
                    public void handle(MouseEvent event) {  
                    	if(event.isPrimaryButtonDown()) {
	                        final int index = row.getIndex();  
	                        if (index >= 0 && index < tableView.getItems().size() && tableView.getSelectionModel().isSelected(index)  ) {
	                            tableViewQuestion.getSelectionModel().clearSelection();
	                            event.consume();  
	                        }  
                    	}
                    }  
                });  
                
                return row ;  
            }  
        }); 
	}
	
	/**
	 * Refreshes the question container table
	 */
	private void refreshQCTable() {
		ObservableList<QuestionContainer> data = FXCollections.observableArrayList(Program.getProject().getQuiz().getQuestionContainerList());
		if(data.size() > 0) {
			tableViewQuestionPack.setItems(data);
			
			tableViewQuestionPack.setFixedCellSize(25);
			tableViewQuestionPack.prefHeightProperty().bind(Bindings.size(tableViewQuestionPack.getItems()).multiply(tableViewQuestionPack.getFixedCellSize()).add(30));
			
	        columnDescription.setCellValueFactory(new PropertyValueFactory<QuestionContainer, String>("description"));
	        Program.getProject().getQuiz().updateQuestionAmount();
	        columnQuestionAmount.setCellValueFactory(new PropertyValueFactory<QuestionContainer, Integer>("questionAmount"));
	        
	        tableViewQuestionPack.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	            if (newSelection != null) {
	            	selectQuestionContainer(tableViewQuestionPack.getSelectionModel().getSelectedItem());
	            }
	            else {
	            	selectQuestionContainer(null);
	            }
	        });
	        
	        tableViewQuestionPack.setOnKeyPressed( new EventHandler<KeyEvent>()
            {
				@Override
				public void handle(KeyEvent event) {
					if(event.getCode().equals(KeyCode.DELETE)) {
						if(tableViewQuestionPack.getSelectionModel().getSelectedItem() != null) {
							removeFromQCTable(tableViewQuestionPack.getSelectionModel().getSelectedItem());
						}
					}
				}
            } );

	        tableViewQuestionPack.setRowFactory(new Callback<TableView<QuestionContainer>, TableRow<QuestionContainer>>() {  
	            @Override  
	            public TableRow<QuestionContainer> call(TableView<QuestionContainer> tableView) { 
	            	
	                final TableRow<QuestionContainer> row = new TableRow<>();  
	                final ContextMenu contextMenu = new ContextMenu();  
	                final MenuItem removeMenuItem = new MenuItem(bundle.getString("button_remove"));  
	                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
	                    @Override  
	                    public void handle(ActionEvent event) {  
	                    	removeFromQCTable(row.getItem());
	                    }  
	                });  
	     
	                final MenuItem renameMenuItem = new MenuItem(bundle.getString("button_rename"));
	                renameMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
	                    @Override  
	                    public void handle(ActionEvent event) {  
	                    	showRenameQuestionPackStage(row.getItem());
	                    }  
	                });  
	                
	                contextMenu.getItems().add(renameMenuItem);  
	                contextMenu.getItems().add(removeMenuItem);  
	             
	                
	               // Set context menu on row, but use a binding to make it only show for non-empty rows:  
	                row.contextMenuProperty().bind(  
	                        Bindings.when(row.emptyProperty())  
	                        .then((ContextMenu)null)  
	                        .otherwise(contextMenu)  
	                );  
	                
	                // Set second click on selected item => unselect
	                row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
	                    @Override  
	                    public void handle(MouseEvent event) {  
	                    	if(event.isPrimaryButtonDown()) {
		                        final int index = row.getIndex();  
		                        if (index >= 0 && index < tableView.getItems().size() && tableView.getSelectionModel().isSelected(index)  ) {
		                            tableView.getSelectionModel().clearSelection();
		                            selectQuestionContainer(null);
		                            event.consume();  
		                        }  
	                    	}
	                    }  
	                });  
	                
	                return row ;  
	            }  
	        }); 
		}
		if(Program.DEBUG) System.out.println("Refreshed QC table: " + data);
	}
	
	/**
	 * Checks Regex .. returns a text that matches the regex
	 * @param text
	 * @return regex text
	 */
	private String checkRegex(String text) {
		if(text.length() > 48) {
			text = text.substring(0, 48);
		}
		if(!text.matches("[A-Za-z0-9äÄöÖüÜß,.@:/\\-][A-Za-z0-9äÄöÖüÜß,.@\\u0020:/\\-]*")) {
			if(text.length() > 0) {
				if(text.charAt(0) == ' ') {
					text=text.substring(1,text.length());
				}
				else {
					text=text.substring(0,text.length()-1);
				}
			}
			else { text=""; }
		}
		return text;
	}
	
	public BorderPane getPane() {
		try {
			// load FXML file
			FXMLLoader loader = new FXMLLoader(
			        getClass().getResource("PaneCreateQuestionPacks.fxml"), bundle
			);
			loader.setController(this);
			Scene scene = new Scene(loader.load());
			
			try {
				Image iconAdd = new Image(new FileInputStream("res/img/icon_add.png"));
				imageViewAddQuestion.setImage(iconAdd);
				imageViewAddQuestionPack.setImage(iconAdd);
			} catch (FileNotFoundException f) {
				System.out.println("Could not load image: res/img/icon_add.png");
			}

			
			questionPane.setVisible(false);
			tableViewQuestionPack.getSelectionModel().clearSelection();
			
			refreshQCTable();
			
			// return the root BorderPane
			root = (BorderPane) scene.getRoot();
			return root;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
