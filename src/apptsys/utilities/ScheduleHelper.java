package apptsys.utilities;

import apptsys.Main;
import apptsys.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ScheduleHelper provides various utility functions to assist in appointment processing
 */

public class ScheduleHelper {

    /**
     * getUpcoming looks for appointments within 15 minutes of current time, filtering to show only appointments with matching userID to active user
     * Lambda utilized for filtering ObservableList
     * @param userID id of active user
     * @return list of all appointments within 15 minutes that match userID
     */
    public static List<Appointment> getUpcoming(int userID) {

        // Step 1: get all Appts
        List<Appointment> filteredAppointments = Main.appointmentsDAO.getAll();

        // Step 2: remove appointments for other users
        filteredAppointments.removeIf(a -> a.getApptUserID() != userID);

        // Step 3: remove appointments that are not within 15 minutes of current local time
        filteredAppointments = filteredAppointments.stream()
                .filter(a -> a.getApptStartDate().isAfter(LocalDateTime.now()))
                .filter(a -> a.getApptStartDate().isBefore(LocalDateTime.now().plusMinutes(15)))
                .collect(Collectors.toList());

        return filteredAppointments;
    }

    /**
     * checkOverlap will look through all appointments to check overlapping time between new appointment and any other appts with same customer ID
     * Lambda utilized for filtering ObservableList
     * @param appointmentToCheck new appointment
     * @return bool: true if there is overlap
     */
    public static boolean checkOverlap(Appointment appointmentToCheck) {
        List<Appointment> filteredAppointments = Main.appointmentsDAO.getAll();
        System.out.println("APPT TO CHECK: " + appointmentToCheck);

        // Step 1: Start with complete list of appts
        System.out.println("STEP 1:" + filteredAppointments);

        // Step 2: If updating, ignore overlap with appointment we are currently updating
        if (appointmentToCheck.getApptID().getValue() != null)
            filteredAppointments.removeIf(a -> a.getApptID().getValue().intValue() == appointmentToCheck.getApptID().getValue().intValue());
        System.out.println("STEP 2:" + filteredAppointments);


        // Step 3: Remove appointments for other customers
        filteredAppointments.removeIf(a -> a.getApptCustomerID() != appointmentToCheck.getApptCustomerID());
        System.out.println("STEP 3:" + filteredAppointments);


        // Step 4: Remove appointments that start after checked appointment ends
        filteredAppointments.removeIf(a -> a.getApptStartDate().isAfter(appointmentToCheck.getApptEndDate()));
        System.out.println("STEP 4:" + filteredAppointments);

        // Step 5: Remove appointments that end before checked appointment starts
        filteredAppointments.removeIf(a -> a.getApptEndDate().isBefore(appointmentToCheck.getApptStartDate()));
        System.out.println("STEP 5:" + filteredAppointments);

        // Step 6: Return true if there are any remaining appts in list, indicating overlap with checked appointment
        return !filteredAppointments.isEmpty();
    }

    /**
     * checks if given appointment start and end times are both within business hours, using some conversions and .getHour() combined with simple int comparisons
     * @param appointmentToCheck new appointment
     * @return true if within hours
     */
    public static boolean checkWithinBusinessHours(Appointment appointmentToCheck) {
        ZonedDateTime zonedStartTime = appointmentToCheck.getApptStartDate().atZone(ZoneId.systemDefault());
        ZonedDateTime zonedEndTime = appointmentToCheck.getApptEndDate().atZone(ZoneId.systemDefault());
        System.out.println("orig: " + zonedStartTime + " // " + zonedEndTime);

        ZonedDateTime estStartTime = zonedStartTime.withZoneSameInstant(ZoneOffset.ofHours(-4));
        ZonedDateTime estEndTime = zonedEndTime.withZoneSameInstant(ZoneOffset.ofHours(-4));
        System.out.println("EST: " + estStartTime + " // " + estEndTime);

        // Must be on the same day
        if (estStartTime.getHour() > 7 && estEndTime.getHour() < 23 && estStartTime.getDayOfYear() == estEndTime.getDayOfYear()) {
            return true;
        }

        return false;
    }

}
