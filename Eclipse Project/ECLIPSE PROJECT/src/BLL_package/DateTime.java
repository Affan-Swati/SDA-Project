package BLL_package;

import java.time.*;


public class DateTime 
{
    public DateTime() 
    {
    	
    }

    public static String getCurrentDate() 
    {
        return LocalDateTime.now().toLocalDate().toString(); // Format: yyyy-MM-dd
    }

    public static String getCurrentTime() 
    {
        return LocalDateTime.now().toLocalTime().toString(); // Format: HH:mm:ss
    }
    

    public static LocalDate getDateAfterDays(int days) 
    {
        return LocalDate.now().plusDays(days); // Adds 'days' to the current date
    }

    public static LocalDate getDateAfterWeeks(int weeks) 
    {
        return LocalDate.now().plusWeeks(weeks); // Adds 'weeks' to the current date
    }

    public static LocalDate getDateAfterMonths(int months) 
    {
        return LocalDate.now().plusMonths(months); // Adds 'months' to the current date
    }

    public String toString() 
    {
        return LocalDateTime.now().toString(); // Format: yyyy-MM-ddTHH:mm:ss
    }
}
