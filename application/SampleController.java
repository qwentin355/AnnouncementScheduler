package application;

import java.net.URL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import dbConnection.dbHandler;

public class SampleController implements Initializable {
	@FXML
	private TextField dbstatus;
	@FXML
	private TextField Hour;
	@FXML
	private TextField Minute;
	@FXML
	private TextField File;
	@FXML
	private TextField Name;
	@FXML
	private RadioButton rb_Sun ;
	@FXML
	private RadioButton rb_Mon ;
	@FXML
	private RadioButton rb_Tue ;
	@FXML
	private RadioButton rb_Wed ;
	@FXML
	private RadioButton rb_Thr ;
	@FXML
	private RadioButton rb_Fri ;
	@FXML
	private RadioButton rb_Sat ;
	
	@FXML
	private TableView<AlarmData> alarmTable;

	@FXML
	private TableColumn<AlarmData, Integer> idcolumn;
	@FXML
	private TableColumn<AlarmData, Integer> hourcolumn;
	@FXML
	private TableColumn<AlarmData, Integer> minutecolumn;
	@FXML
	private TableColumn<AlarmData, String> filecolumn;
	@FXML
	private TableColumn<AlarmData, String> namecolumn;
	@FXML
	private TableColumn<AlarmData, String> hasplayedcolumn;
	@FXML
	private TableColumn<AlarmData, String> suncolumn;
	@FXML
	private TableColumn<AlarmData, String> moncolumn;
	@FXML
	private TableColumn<AlarmData, String> tuecolumn;
	@FXML
	private TableColumn<AlarmData, String> wedcolumn;
	@FXML
	private TableColumn<AlarmData, String> thrcolumn;
	@FXML
	private TableColumn<AlarmData, String> fricolumn;
	@FXML
	private TableColumn<AlarmData, String> satcolumn;
	
	// Variables
	
	private AudioPlayer ap;
	private dbHandler dc;
	
	private int selectedAlarmId;
	private AlarmData selectedAlarm;
	private ObservableList<AlarmData> data;
	
	private Date dt = new Date();
	private Calendar cal = GregorianCalendar.getInstance();

	/*
	 * initialize This Will make a Connection to the Database, Schedule the Alarms
	 * Check and Load the UI populating the table.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			this.dc = new dbHandler(); // Connect to Database
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		this.dbstatus.setText("Connected"); // Faked

		Runnable helloRunnable = new Runnable() {
			public void run() {
				dt = new Date(); // Update The Time
				cal.setTime(dt);

				PlayAlarms(); // PlayAlarms

				if (cal.HOUR_OF_DAY == 23 && cal.MINUTE == 59) // ClearFlags at the End of the day
				{
					ClearFlags();
				}
			}
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);
		LoadData();

	}

	// Clear HasPlayed Flags
	private void ClearFlags() {
		for (AlarmData E : data) {
			try {
				dc.UpdateAlarm(E.getId(), false);
				LoadData();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * PlayAlarms This Will Check our List of AlarmData and compare the current Hour
	 * and Minute to the data, It will attempt to play the associated data When it
	 * finds a match
	 */
	private void PlayAlarms() {
		if (data != null) {
			for (AlarmData E : data) {
				if (E.getHour() == cal.get(Calendar.HOUR_OF_DAY) && E.getMinute() == cal.get(Calendar.MINUTE) && dateCheck(E)) // OnlyPlayOnce
				{
					if (!E.getHasPlayed()) {
						File sound = new File(E.getFile());
						if (sound.exists()) {
							try {
								dc.UpdateAlarm(E.getId(), true);
								LoadData();
							} catch (ClassNotFoundException | SQLException e) {
								e.printStackTrace();
							}
							try {
								ap = new AudioPlayer(sound);
							} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	private boolean dateCheck(AlarmData E)
	{
		
		int currentDay = cal.get(Calendar.DAY_OF_WEEK);
		System.out.print(currentDay + " : " + E.getThr());
			switch(currentDay) {
			case 1:
			{
				if(E.getSun())
					return true;
			}
			break;
			case 2:
			{
				if(E.getMon())
					return true;
			}
			break;
			case 3:
			{
				if(E.getTue())
					return true;
			}
			break;
			case 4:
			{
				if(E.getWed())
					return true;
			}
			break;
			case 5:
			{
				if(E.getThr())
					return true;
			}
			break;
			case 6:
			{
				if(E.getFri())
					return true;
			}
			break;
			case 7:
			{
				if(E.getSat())
					return true;
			}
			break;
		}
		return false;
	}
	// region Description
	// These Functions are Used to populate the Table with AlarmData.

	@FXML
	private void loadAlarmData(ActionEvent event) {
		ResultSet rs;
		this.data = FXCollections.observableArrayList();

		try {
			rs = dc.getAll();// Get all Alarms
			while (rs.next()) {
				this.data.add(new AlarmData(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
						rs.getBoolean(6), rs.getBoolean(7),rs.getBoolean(8),rs.getBoolean(9),rs.getBoolean(10),rs.getBoolean(11),rs.getBoolean(12),rs.getBoolean(13)));
			
			}

			this.idcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, Integer>("Id"));
			this.hourcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, Integer>("Hour"));
			this.minutecolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, Integer>("Minute"));
			this.filecolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("File"));
			this.namecolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Name"));
			this.hasplayedcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("hasPlayed"));
			this.suncolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Sun"));
			this.moncolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Mon"));
			this.tuecolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Tue"));
			this.wedcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Wed"));
			this.thrcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Thr"));
			this.fricolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Fri"));
			this.satcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Sat"));
			// REFRESH TABLE
			this.alarmTable.setItems(null);
			this.alarmTable.setItems(this.data);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void LoadData() {
		ResultSet rs;
		this.data = FXCollections.observableArrayList();

		try {
			rs = dc.getAll();// Get all Alarms
			while (rs.next()) {
				this.data.add(new AlarmData(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
						rs.getBoolean(6), rs.getBoolean(7),rs.getBoolean(8),rs.getBoolean(9),rs.getBoolean(10),rs.getBoolean(11),rs.getBoolean(12),rs.getBoolean(13)));
			}

			this.idcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, Integer>("Id"));
			this.hourcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, Integer>("Hour"));
			this.minutecolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, Integer>("Minute"));
			this.filecolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("File"));
			this.namecolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Name"));
			this.hasplayedcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("hasPlayed"));
			this.suncolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Sun"));
			this.moncolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Mon"));
			this.tuecolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Tue"));
			this.wedcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Wed"));
			this.thrcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Thr"));
			this.fricolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Fri"));
			this.satcolumn.setCellValueFactory(new PropertyValueFactory<AlarmData, String>("Sat"));
			// REFRESH TABLE
			this.alarmTable.setItems(null);
			this.alarmTable.setItems(this.data);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	// endregion

	/*
	 * OnAlarm Selection this will store the selected Alarm within the selectedAlarm
	 * Variable
	 */
	@FXML
	private void onAlarmSelect(MouseEvent event) {
		this.selectedAlarm = alarmTable.getSelectionModel().getSelectedItem();
		this.selectedAlarmId = selectedAlarm.getId();
	}

	/*
	 * addAlarm - OnAction this will Create An AlarmData object and pass it to the
	 * dbHandler class to store the data
	 */
	@FXML
	private void addAlarm(ActionEvent event) {
		int lHour = Integer.parseInt(Hour.getText());
		int lMinute = Integer.parseInt(Minute.getText());
		String lName = this.Name.getText();
		String lFile = this.File.getText();
		ArrayList<Boolean> days = new ArrayList<>();
		Boolean Sun = rb_Sun.selectedProperty().getValue();
		Boolean Mon = rb_Mon.selectedProperty().getValue();
		Boolean Tue = rb_Tue.selectedProperty().getValue();
		Boolean Wed = (rb_Wed.selectedProperty().getValue());
		Boolean Thr = (rb_Thr.selectedProperty().getValue());
		Boolean Fri = (rb_Fri.selectedProperty().getValue());
		Boolean Sat = (rb_Sat.selectedProperty().getValue());
		Boolean lhasPlayed = false;

		try {
			dc.AddAlarm(new AlarmData(0, lHour, lMinute, lName, lFile, lhasPlayed,Sun,Mon,Tue,Wed,Thr,Fri,Sat));
			LoadData();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoadData();
	}

	/*
	 * deleteAlarm - OnAction this will pass the selectedAlarm dataobject to the
	 * dbHandler to delete the alarm based on rowID
	 */
	@FXML
	private void deleteAlarm(ActionEvent event) {

		System.out.print("Click");
		try {
			if (selectedAlarmId != 0)
				dc.DeleteAlarm(this.selectedAlarmId);
			LoadData();

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	@FXML
	private void pickFile(ActionEvent event)
	{
		Node node = (Node) event.getSource();
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(node.getScene().getWindow());
		this.File.setText(file.getAbsolutePath().toString());
		System.out.println(file);
		
	}
	/*
	 * clearForm - OnAction this will clear all of the form feilds
	 */
	@FXML
	private void clearForm(ActionEvent event) {
		this.Hour.setText("");
		this.Minute.setText("");
		this.File.setText("");
		this.Name.setText("");
	}
}
