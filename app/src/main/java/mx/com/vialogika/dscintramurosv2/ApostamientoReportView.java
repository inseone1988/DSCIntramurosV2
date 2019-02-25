package mx.com.vialogika.dscintramurosv2;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.dscintramurosv2.Room.Apostamiento;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Person;

public class ApostamientoReportView {
    private Apostamiento apostamiento;
    private List<Person> persons = new ArrayList<>();
    private List<Guard> guards = new ArrayList<>();

    public ApostamientoReportView(Apostamiento apostamiento) {
    }

    public List<Guard> getGuards() {
        return guards;
    }

    public void setGuards(List<Guard> guards) {
        this.guards = guards;
    }

    public Apostamiento getApostamiento() {
        return apostamiento;
    }

    public void setApostamiento(Apostamiento apostamiento) {
        this.apostamiento = apostamiento;
    }

    public int apostamientoRequired(){
        return apostamiento.getPlantillaPlaceGuardsRequired();
    }

    public int apostamientoGuardCount(){
        return guards.size();
    }

    public String apostamientoName(){
        return apostamiento.getPlantillaPlaceApostamientoName();
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public void addGuard(Person person, Guard guard){
        persons.add(person);
        guards.add(guard);
    }
}
