package application;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AlarmData {

private final IntegerProperty Id;
private final IntegerProperty Hour;
private final IntegerProperty Minute;
private final StringProperty Name;
private final StringProperty File;
private final BooleanProperty HasPlayed;
private final BooleanProperty Sun;
private final BooleanProperty Mon;
private final BooleanProperty Tue;
private final BooleanProperty Wed;
private final BooleanProperty Thr;
private final BooleanProperty Fri;
private final BooleanProperty Sat;





public AlarmData(int Id, int Hour, int Minute, String Name,String File, Boolean hasPlayed, Boolean Sun, Boolean Mon, Boolean Tue, Boolean Wed, Boolean Thr, Boolean Fri, Boolean Sat) {
	this.Id = new SimpleIntegerProperty(Id);
	this.Hour = new SimpleIntegerProperty(Hour);
	this.Minute = new SimpleIntegerProperty(Minute);
	this.File = new SimpleStringProperty(File);
	this.Name = new SimpleStringProperty(Name);
	this.HasPlayed = new SimpleBooleanProperty(hasPlayed);
	this.Sun = new SimpleBooleanProperty(Sun);
	this.Mon = new SimpleBooleanProperty(Mon);
	this.Tue = new SimpleBooleanProperty(Tue);
	this.Wed = new SimpleBooleanProperty(Wed);
	this.Thr = new SimpleBooleanProperty(Thr);
	this.Fri = new SimpleBooleanProperty(Fri);
	this.Sat = new SimpleBooleanProperty(Sat);
}
public IntegerProperty getIdProperty() {
	return Id;
}

public int getId() {
	return Id.get();
}

public IntegerProperty getMinuteProperty() {
	return Minute;
}
public int getMinute() {
	return Minute.get();
}

public StringProperty getFileProperty() {
	return File;
}
public String getFile() {
	return File.get();
}
public StringProperty getNameProperty() {
	return File;
}

public String getName() {
	return Name.get();
}

public IntegerProperty getHourProperty() {
	return Hour;
}
public int getHour() {
	return Hour.get();
}

public BooleanProperty getHasPlayedProperty() {
	return HasPlayed;
}
public Boolean getHasPlayed() {
	return HasPlayed.get();
}

public BooleanProperty getSunProperty() {
	return Sun;
}
public Boolean getSun() {
	return Sun.get();
}

public BooleanProperty getMonProperty() {
	return Mon;
}
public Boolean getMon() {
	return Mon.get();
}

public BooleanProperty getWedProperty() {
	return Wed;
}
public Boolean getWed() {
	return Wed.get();
}

public BooleanProperty getTueProperty() {
	return Tue;
}
public Boolean getTue() {
	return Tue.get();
}

public BooleanProperty getThrProperty() {
	return Thr;
}
public Boolean getThr() {
	return Thr.get();
}

public BooleanProperty getSatProperty() {
	return Sat;
}

public Boolean getSat() {
	return Sat.get();
}
public BooleanProperty getFriProperty() {
	return Fri;
}
public Boolean getFri() {
	return Fri.get();
}

}
