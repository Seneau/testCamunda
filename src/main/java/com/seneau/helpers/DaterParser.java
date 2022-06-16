package com.seneau.helpers;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
public class DaterParser {

    public LocalDate getDate(String type, String month_year){

       String day = type == "end" ? "31": "01";

        LocalDate dateOfMonth;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String textStartDate= day+"-"+month_year;

        return LocalDate.parse(textStartDate, df);
    }
    public LocalDate getDate(String type){
        Date current= new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(current);
        String currentMonth = ("0"+(calendar.get(Calendar.MONTH)+1)).substring(0,2);

        String day = type== "end"? calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+"" : "01";
        //System.out.println("le jour "+day);
        return LocalDate.parse(calendar.get(Calendar.YEAR)+"-"+ currentMonth +"-"+day);
    }


}
