package com.javagui;

import java.io.IOException;
import javafx.fxml.FXML;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;
import com.database.*;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML private TextField xtaField;
    @FXML private TextField robotField;
    @FXML private TextField zoneField;
    @FXML private TextField nameField;
    @FXML private TextField customerNameField;
    @FXML private TextField projectField;
    @FXML private TextField toolNumberField;
    @FXML private TextField machineDescriptionField;
    @FXML private TextField stationField;
    @FXML private TextField sheetField;
    @FXML private TextField fdField;
    @FXML private TextField partNumberField;
    @FXML private TextField manufacturerField;
    @FXML private TextField startConditionField;
    @FXML private TextField functionDescriptionField;
    @FXML private TextField changeDescriptionField;
    @FXML private TextField changeDateField;
    @FXML private TextField changeCompanyField;
    @FXML private TextField changeNameField;
    @FXML private TextField partNumberFieldDB;
    @FXML private TextField unitNoDB;
    @FXML private TextField functionDescriptionDB;
    @FXML private TextField panelReferenceDB;
    @FXML private TextField toolManufacturerDB;
    @FXML private TextField CylinderBoreDB;
    @FXML private TextField sensorTypeDB;
    @FXML private TextField integralBrakeDB;
    @FXML private TextField openAngleDB;
    @FXML private TextField switchNumberDB;
    @FXML private TextField changeLevelDB;
    @FXML private ChoiceBox<String> componentDropdown;
    @FXML private ChoiceBox<String> componentStateDropdown;
    @FXML private ComboBox<String> componentImageDropdown;
    @FXML private Label componentInformation;
    @FXML private Label componentInformation1;
    @FXML private Label componentInformation2;
    @FXML private Button addComponent;
    @FXML private Shape rectangleShape;
    @FXML private AnchorPane layoutPane;
    @FXML private Pane imagePane;
    @FXML private ChoiceBox<String> choiceBoxDatabase;
    @FXML private Button updateChoicebox;
    
    
    private String selectedComponent;
    private String alphabetLetter;
    private int currentComponentIndex = 1;
    private char alphabet;
    private double x;
    private double y;

    String databaseUrl;
    String userName;
    String passWord;

    String[][] userComponentInputs;
    String[] componentState;

    FileChooser fileChooser;
    
    Window mainStage;
    String filePath;
    String imageFilePath;

    DataBaseController dbc;
    

    @FXML
    void initialize() {
        databaseUrl = "jdbc:mysql://localhost:3306/components_data";
        userName = "root";
        passWord = "Hawaii2020";
        //dbc = new DataBaseController(userName, databaseUrl, passWord);
        fileChooser  = new FileChooser();
    }

    public void readExcel() throws IOException
    {
        //String filePath = "C:/Users/Aaron.Perel/Desktop/Sequence chart new - Copy.xlsx";
        FileInputStream fis = new FileInputStream(filePath);

        XSSFWorkbook workBook = new XSSFWorkbook(fis);

        XSSFSheet contentSheet = workBook.getSheetAt(0);
        userComponentInputs = new String[contentSheet.getLastRowNum() + 1][12]; 

        //iterator for each cell and columns

        for(int componentRows = 4; componentRows <= contentSheet.getLastRowNum(); componentRows++)
        {
            XSSFRow iteratedRow =  contentSheet.getRow(componentRows);

            Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = iteratedRow.cellIterator();

            while(cellIterator.hasNext())
            {
                XSSFCell chosenCell = (XSSFCell) cellIterator.next();

                switch(chosenCell.getCellType()){
                    case STRING:
                        userComponentInputs[iteratedRow.getRowNum()][chosenCell.getColumnIndex()] = chosenCell.getStringCellValue();
                        System.out.print(userComponentInputs[iteratedRow.getRowNum()][chosenCell.getColumnIndex()]);
                    break;
                    case BOOLEAN:
                        //System.out.print(chosenCell.getBooleanCellValue());
                    break;
                    case NUMERIC:
                        //System.out.print(chosenCell.getNumericCellValue());
                    break;
                    case BLANK:
                        System.out.print(" ");
                    break;
                    default:
                    break;
                }
                System.out.print(" | ");
            }
            System.out.println(" ");
        }

        fis.close();
        workBook.close();
        for(int iterate = 4; iterate < userComponentInputs.length; iterate++)
        {
            componentDropdown.getItems().add(userComponentInputs[iterate][2]);
        }   
        System.out.println("");
    }

    public void writeBasicInfo() throws IOException
    {
        //String filePath = "C:/Users/Aaron.Perel/Desktop/Sequence chart new - Copy.xlsx";
        FileInputStream fis = new FileInputStream(filePath);

        Object[] columnHeaderNames = {customerNameField.getText(),xtaField.getText(),zoneField.getText(),stationField.getText(),toolNumberField.getText(),sheetField.getText(),projectField.getText(),
            machineDescriptionField.getText(),robotField.getText()
            }; 

        XSSFWorkbook workBook = new XSSFWorkbook(fis);
        for(int n = 0; n < 3; n++)
        {
            XSSFSheet sheet = workBook.getSheetAt(n);
            java.util.Iterator<Row> rowIterator = sheet.iterator();
            
            sheetField.setText(workBook.getSheetIndex(sheet.getSheetName()) + "Out of" + workBook.getNumberOfSheets()); 
            while(rowIterator.hasNext())
            {
                XSSFRow currentRow = (XSSFRow) rowIterator.next();
                if(currentRow.getFirstCellNum() != -1 && currentRow.getRowNum() == 2)
                {  
                    System.out.println("im populating the correct row");

                    
                    for(int i = 0; i < 9; i++)
                    {
                        XSSFCell currentCell = currentRow.getCell(i);
                        if(currentCell != null)
                        {
                            if(currentCell.getStringCellValue().contains("Out of"))
                            {
                                i++;
                            }
                            else
                            {
                                currentCell.setCellValue((String)columnHeaderNames[i]);
                            }
                        }
                        else
                        {
                            i = 9;
                        }
                        
                    }
                }
                else
                {
                    System.out.println(currentRow.getRowNum());
                }
            }

        }
        FileOutputStream fos = new FileOutputStream(filePath);
        workBook.write(fos);
        fos.close();
        workBook.close();

    
    }
    

    public void addComponent() throws IOException
    {
        //String filePath = "C:/Users/Aaron.Perel/Desktop/Sequence chart new - Copy.xlsx";

        FileInputStream fis = new FileInputStream(filePath);  

        XSSFWorkbook workBook = new XSSFWorkbook(fis);

        XSSFSheet sheet = workBook.getSheet("Content");
        XSSFSheet sheet2 = workBook.getSheet("Sequence");

        int rowCount = sheet.getLastRowNum() + 1;
        int rowCountSequence = sheet2.getLastRowNum() + 1;
        currentComponentIndex++;
        if(rowCount <= 5)
        {
            alphabet = 'A';
        }
        else if(alphabetLetter == null)
        {
            alphabet = 'A';
            alphabet += sheet.getLastRowNum() - 5;
            currentComponentIndex = sheet.getLastRowNum() - 5;
        }
        
        int alphabetInt = rowCount - sheet.getLastRowNum();
        alphabet += alphabetInt;
        alphabetLetter = Character.toString(alphabet);
        System.out.println(alphabetLetter);

        Object[] componentInputs = {alphabetLetter,Integer.toString(currentComponentIndex),fdField.getText(), partNumberField.getText(),"NULL","NULL","NULL", 
        manufacturerField.getText(),"NULL","NULL","NULL", startConditionField.getText(), };

        Object[] sequenceInputs = {alphabetLetter,Integer.toString(currentComponentIndex),"NULL","NULL","NULL","NULL","NULL"};

        XSSFRow currentRow = sheet.getRow(rowCount);
        XSSFRow currentSequenceRow = sheet2.getRow(rowCountSequence);

        if(currentRow == null)
        {  
            currentRow = sheet.createRow(rowCount);
            currentSequenceRow = sheet2.createRow(rowCountSequence);
        }

        System.out.println(currentRow.getRowNum());
        System.out.println("im populating correctly");

        for(int i = 0; i < 12; i++)
        {
            XSSFCell currentCell = currentRow.getCell(i);


            if(currentCell == null)
            {
                currentCell = currentRow.createCell(i);
            }

            currentCell.setCellValue((String)componentInputs[i]);
                    
        }

        for(int i = 0; i < 7; i++)
        {
            XSSFCell currentSequenceCell = currentSequenceRow.getCell(i);

            if(currentSequenceCell == null)
            {
                currentSequenceCell = currentSequenceRow.createCell(i);
            }

            currentSequenceCell.setCellValue((String) sequenceInputs[i]);       
        }
        
        FileOutputStream fos = new FileOutputStream(filePath);
        workBook.write(fos);
        fos.close();
        workBook.close();

    
    }

    public void chooseComponent() throws IOException
    {
        selectedComponent = componentDropdown.getValue();
        int selectedComponentIndex = componentDropdown.getSelectionModel().getSelectedIndex() + 4;

        componentInformation1.setText("Selected Component: " + selectedComponent);
        String result = String.join(" , \n", userComponentInputs[selectedComponentIndex]);
        //System.out.print(userComponentInputs[5][selectedComponentIndex]);
        componentInformation.setText(result);
        componentInformation.setVisible(true);
        componentInformation1.setVisible(true);
        componentInformation2.setVisible(false);
        
        if(componentStateDropdown.getValue() == null)
        {
            componentStateDropdown.getItems().addAll("POS 1", "POS 2");
            //System.out.println(componentDropdown.getSelectionModel().getSelectedIndex());
        }
    }

    public void addEvent() throws IOException
    {
        FileInputStream fis = new FileInputStream(filePath);

        XSSFWorkbook workBook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workBook.getSheet("Sequence");

        selectedComponent = componentStateDropdown.getSelectionModel().getSelectedItem();
        int selectedComponentIndex = componentDropdown.getSelectionModel().getSelectedIndex() + 5;

        XSSFRow row = sheet.getRow(selectedComponentIndex);

        int currentCellIndex = row.getLastCellNum();

        XSSFCell currentCell = row.getCell(currentCellIndex);

        if(currentCell == null)
        {
            currentCell = row.createCell(currentCellIndex);
            currentCell.setCellValue(selectedComponent);
            System.out.println(currentCellIndex);
        }
        else
        {   
            currentCell.setCellValue(selectedComponent);
            System.out.println("im in");
            System.out.println(currentCellIndex);
        }

        FileOutputStream fos = new FileOutputStream(filePath);
        workBook.write(fos);
        fos.close();
        workBook.close();
    }

    public void addChange() throws IOException
    {
        FileInputStream fis = new FileInputStream(filePath);

        Object[] componentInputs = {alphabetLetter, changeNameField.getText(), changeCompanyField.getText(),changeDateField.getText(), changeDescriptionField.getText()}; 
        XSSFWorkbook workBook = new XSSFWorkbook(fis);

        XSSFSheet sheet = workBook.getSheetAt(1);
        for(int j = 4; j < 1000;)
        {
            XSSFRow currentRow = sheet.getRow(j);

            if(currentRow == null)
            {  
                currentRow = sheet.createRow(j);
            }

            for(int i = 0; i < 5; i++)
            {
                XSSFCell currentCell = currentRow.getCell(i);
                if(currentCell == null)
                {
                    currentCell = currentRow.createCell(i);
                }

                currentCell.setCellValue((String)componentInputs[i]);
                System.out.println(currentRow.getRowNum());
                System.out.println("im populating correctly");
                    
            }
            j++;
            break;
        }

       

        FileOutputStream fos = new FileOutputStream(filePath);
        workBook.write(fos);
        fos.close();
        workBook.close();
    }

    public void database()
    {
        try 
        {
            Connection connection = DriverManager.getConnection(databaseUrl, userName, passWord);
            java.sql.Statement st = connection.createStatement();
            PreparedStatement pst;

            String query = "INSERT INTO components (ChangeLevel, UnitNo, FunctionDescription, PartNumber, PanelReference, CylinderBore, OpenAngle, ToolManufacturer, SensorType, SwitchNumber, IntegralBrake) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

            pst = connection.prepareStatement(query);
            pst.setString(1, changeLevelDB.getText());
            pst.setString(2, unitNoDB.getText());
            pst.setString(3, functionDescriptionDB.getText());
            pst.setString(4, partNumberFieldDB.getText());
            pst.setString(5,  panelReferenceDB.getText());
            pst.setString(6, CylinderBoreDB.getText());
            pst.setString(7, openAngleDB.getText());
            pst.setString(8, toolManufacturerDB.getText());
            pst.setString(9, sensorTypeDB.getText());
            pst.setString(10, switchNumberDB.getText());
            pst.setString(11, integralBrakeDB.getText());

            pst.executeUpdate();

            System.out.println("Connected And Pushed!");
        } 
        catch (SQLException e) 
        {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }

    @FXML
    public void handle(ActionEvent event) throws IOException
    {
        File chosenFile = fileChooser.showOpenDialog(mainStage);
        filePath = chosenFile.getAbsolutePath();
    }

    @FXML
    public void handleChooseComponentButton(ActionEvent event) throws IOException
    {
        chooseComponent();
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void dragAndDropImageView(ImageView image)
    {
    
        image.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                x = image.getLayoutX() - mouseEvent.getSceneX();
                y = image.getLayoutY() - mouseEvent.getSceneY();
            }
        });

        image.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                image.setLayoutX(mouseEvent.getSceneX() + x);
                image.setLayoutY(mouseEvent.getSceneY() + y);
                
            }
        });

        image.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                imagePane.getChildren().add(image);
                
            }
        });

    }

    public void handleImage() throws IOException
    {
        final double startingXPosition = 14;
        final double startingYPosition = 146;

        File chosenFile = fileChooser.showOpenDialog(mainStage);
        imageFilePath = chosenFile.getAbsolutePath();
        Image componentImage = new Image(imageFilePath);
        ImageView imageView = new ImageView();
        
        imageView.setImage(componentImage);

        imageView.setLayoutX(startingXPosition);
        imageView.setLayoutY(startingYPosition);
        imageView.setFitWidth(154);
        imageView.setFitHeight(135);

        layoutPane.getChildren().addAll(imageView);

        imageView.setVisible(true);

        dragAndDropImageView(imageView);

        
    }

    public void exportImage() throws IOException
    {
        FileInputStream fis = new FileInputStream(filePath);
        //String imageFilePath = imageFile.getAbsolutePath();

        XSSFWorkbook workBook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workBook.getSheet("Tooling Layout");

        Image finalImage = imagePane.snapshot(null, null);

        FileChooser saveFile = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files", "*.PNG");
        saveFile.getExtensionFilters().add(extFilter);
        File imageFile = saveFile.showSaveDialog(mainStage);

        ImageIO.write(SwingFXUtils.fromFXImage(finalImage, null), "png", imageFile);

        InputStream is = new FileInputStream(imageFile);
        
        byte[] bytes = IOUtils.toByteArray(is);
        int pictureId = workBook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        is.close();

        CreationHelper helper = workBook.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();

        ClientAnchor anchor = helper.createClientAnchor();

        anchor.setCol1(10);
        anchor.setCol2(20);
        anchor.setRow1(15);
        anchor.setRow2(30);

        Picture pict = drawing.createPicture(anchor, pictureId);

        FileOutputStream fos = new FileOutputStream(filePath);
        workBook.write(fos);
        fis.close();
        fos.close();
        workBook.close();
        
    }

    public void runUpdate() throws SQLException
    {
        String databaseUrl = "jdbc:mysql://localhost:3306/components_data";
        String userName = "root";
        String passWord = "Hawaii2020";
        dbc = new DataBaseController(userName, databaseUrl, passWord);

        try {
            dbc.pullComponentData();
            System.out.println("data pulled!");
        } catch (SQLException e) {
            System.out.println("Failed to pull component data: " + e.getMessage() + e.getSQLState());
            e.printStackTrace();
        }
        
    }

    public void updateComboBox(String result, ArrayList<String> list) throws SQLException
    {
        choiceBoxDatabase.getItems().add(result);
        System.out.println(result);
            
    }


    public void addComponentFromDatabase() throws IOException
    {
        ArrayList<String> list = dbc.getArrayList();
        FileInputStream fis = new FileInputStream(filePath);

        XSSFWorkbook workBook = new XSSFWorkbook(fis);

        XSSFSheet sheet = workBook.getSheet("Content");
        XSSFSheet sheet2 = workBook.getSheet("Sequence");

        int componentIndex = choiceBoxDatabase.getSelectionModel().getSelectedIndex();


        


        FileOutputStream fos = new FileOutputStream(filePath);
        workBook.write(fos);
        fos.close();
        workBook.close();
    
        
    }
    

}

