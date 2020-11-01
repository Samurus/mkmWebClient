package ch.softridge.cardmarket.autopricing.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * @author Kevin Zellweger
 * @Date 31.10.20
 */
public class CSVUtil {

    private static final String DELIMITER = "\",\""; // Stupid format delivered by mkm
    private static final String EOL = "\n";

    public static List<String> parseLine(String line){
        if(line != null && !line.isEmpty()){
            return List.of(line.split(DELIMITER));
        } else {
            return Collections.emptyList();
        }
    }

    public static Integer parseIntegerColumn(String inColumn){
        inColumn = inColumn.replaceAll("\"","");
        if(!inColumn.isEmpty()){
            return Integer.valueOf(inColumn);
        }else {
            return -1;
        }
    }
    public static String  parseStringColumn(String inColumn){
        inColumn = inColumn.replaceAll("\"","");
        return inColumn;
    }
    public static LocalDate parseDateColumn(String inColumn){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        inColumn = inColumn.replaceAll("\"","");
        if(!inColumn.isEmpty()){
            return LocalDate.parse(inColumn,formatter);
        }else {
            return LocalDate.MIN;
        }
    }

}
