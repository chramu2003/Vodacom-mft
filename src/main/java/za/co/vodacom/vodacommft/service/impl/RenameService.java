package za.co.vodacom.vodacommft.service.impl;
/**
 * @author jan & modified by mz herbie on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.dto
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.service.IRenameService;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
@Service
public class RenameService implements IRenameService {

    private static final Logger rename_service_logger = LoggerFactory.getLogger(RenameService.class);

    @Override
    public String renameByRegEx(String fileName, String renameCases[], String consumerHost){

        rename_service_logger.info(">>>>>>>>>>>>>>>>>  Rename Case Value or Size :- "+ renameCases.length);
            for(String renameCase : renameCases){
                String[] renameOptions = renameCase.split("\\|");
                int caseNumber = Integer.parseInt(renameOptions[0]);
                renameCase = renameOptions[1];
                String[] renameParams = new String[3];
            switch(caseNumber){
                case 0: //Extention of Filename
                    switch (renameCase){
                        case "U": //Uppercase
                            fileName = fileName.substring(0, fileName.lastIndexOf('.') + 1) + fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase();
                            break;
                        case "L": //Lowercase
                            fileName = fileName.substring(0, fileName.lastIndexOf('.') + 1) + fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
                            break;
                    }
                    break;
                case 1: //Entire Filename
                    switch (renameCase){
                        case "U": //Uppercase
                            fileName = fileName.toUpperCase();
                            break;
                        case "L": //Lowercase
                            fileName = fileName.toLowerCase();
                            break;
                    }
                    break;
                case 2: //Do action on a Substring
                    renameParams = renameCase.split(";");
                    renameCase = renameParams[0];
                    int subStringNum1 = Integer.parseInt(renameParams[1]);
                    int subStringNum2 = Integer.parseInt(renameParams[2]);
                    switch(renameCase){
                        case "U": //Substring to Uppercase
                            fileName = fileName.substring(0, subStringNum1 - 1) + fileName.substring(subStringNum1 - 1, subStringNum2).toUpperCase() + fileName.substring(subStringNum2);
                            break;
                        case "L": //Substring to Lowercase
                            fileName = fileName.substring(0, subStringNum1 - 1) + fileName.substring(subStringNum1 - 1, subStringNum2).toLowerCase() + fileName.substring(subStringNum2);
                            break;
                        case "R": //Remove Substring from Filename
                            fileName = fileName.substring(0, subStringNum1 - 1) + fileName.substring(subStringNum2);
                            break;
                        case "C": //Use Substring as new Filename
                            fileName = fileName.substring(subStringNum1 - 1, subStringNum2);
                            break;
                    }
                    break;
                case 3: //Add Substring to Start of Filename
                    fileName = renameCase + fileName;
                    break;
                case 4: //Add Substring to End of Filename
                    fileName = fileName + renameCase;
                    break;
                case 5: //Add Substring to specified position in Filename
                    renameParams = renameCase.split(";");
                    int subStringNum = Integer.parseInt(renameParams[0]);
                    renameCase = renameParams[1];
                    fileName = fileName.substring(0, subStringNum - 1) + renameCase + fileName.substring(subStringNum - 1);
                    break;
                case 6: //Add Hostname to specified position in Filename
                    int insertHostnamePoint = Integer.parseInt(renameCase);
                    fileName = fileName.substring(0, insertHostnamePoint - 1) + consumerHost + fileName.substring(insertHostnamePoint - 1);
                    break;
                case 7: //Add variable value to specified position in Filename
                    renameParams = renameCase.split(";");
                    int insertPoint = Integer.parseInt(renameParams[0]);
                    String variableDefined = renameParams[1];
                    String formatDefined = renameParams[2];
                    Date dateNow = null;
                    if(variableDefined.equals("CurrDate") || variableDefined.equals("PrevDate")){
                        dateNow = new Date();
                        if(variableDefined.equals("PrevDate")){
                            long longDate = dateNow.getTime() - 86400000; // subtract 1 day in milliseconds
                            dateNow.setTime(longDate);
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat(formatDefined);
                        renameCase = sdf.format(dateNow);
                        fileName = fileName.substring(0, insertPoint - 1) + renameCase + fileName.substring(insertPoint - 1);
                    }
                    break;
                case 8: //Replace Extention of Filename with Substring
                    fileName = fileName.substring(0, fileName.lastIndexOf('.') + 1) + renameCase;
                    break;
                case 9:
                    renameParams = renameCase.split(";");

                    String start_end = renameParams[0];
                    String variable_defined = renameParams[1];
                    String format_defined = renameParams[2];
                    Date date_now = null;
                    if(variable_defined.equals("CurrDate") || variable_defined.equals("PrevDate")){
                        date_now = new Date();
                        if(variable_defined.equals("PrevDate")){
                            long longDate = date_now.getTime() - 86400000; // subtract 1 day in milliseconds
                            date_now.setTime(longDate);
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat(format_defined);
                        renameCase = sdf.format(date_now);
                        if (start_end.equalsIgnoreCase("start")){
                            fileName = renameCase + fileName;
                        }else if (start_end.equalsIgnoreCase("end")){
                            fileName =  fileName + renameCase;
                        }
                    }
                    break;
                default:
                    rename_service_logger.info("No pattern selected");
                    break;
            }
            rename_service_logger.info("New filename : " + fileName);
        }
        return fileName;
    }


    public  String [] renamePatternCases (String inputPattern){
        rename_service_logger.info(new Date().toString()+ ": Input Pattern For Rename  : " + inputPattern);
        String [] renamePatternCases = inputPattern.split(";");
        //RegexList of patterns.
        ArrayList<String> regexList = new ArrayList<>();
        regexList.add("[*][\\.]([UL])[*]");//0
        regexList.add("([UL])[*]");//1
        regexList.add("([ULRC])[\\[]([0-9]*)[\\-]([0-9]*)[\\]*]");//2
        regexList.add("[A][\\[]START][\\[][\\']([0-9a-zA-Z\\_\\-\\.]*)[\\']]");//3
        regexList.add("[A][\\[]END][\\[][\\']([0-9a-zA-Z\\_\\-\\.]*)[\\']]");//4
        regexList.add("[A][\\[]([0-9]*)[\\]*][\\[][\\']([0-9a-zA-Z\\_\\-\\.]*)[\\']]");//5
        regexList.add("[A][\\[]([0-9]*)[\\]*][\\[][$][Hostname]*]");//6
        regexList.add("[A][\\[]([0-9]*)[\\]*][\\[][$]([0-9a-zA-Z]*)[\\-]([0-9a-zA-Z-:\\_\\.]*)]");//7
        regexList.add("[X][\\[][\\']([0-9a-zA-Z\\_\\-\\.]*)[\\']]");//8
        regexList.add("[A][\\[]([END]*)[\\]*][\\[][$]([0-9a-zA-Z]*)[\\-]([0-9a-zA-Z-:\\_\\.]*)]");//9
        for (int i = 0; i < renamePatternCases.length; i++) {
            for(int j = 0; j < regexList.size() ; j++){
                //Regex Pattern
                Pattern pattern = Pattern.compile(regexList.get(j));
                //Regex Matcher
                Matcher matcher = pattern.matcher(renamePatternCases[i]);
                if (matcher.find()){
                    switch (j){
                        case 0:
                            renamePatternCases[i] = j + "|" + matcher.group(1);
                            break;
                        case 1:
                            renamePatternCases[i] = j + "|" + matcher.group(1);
                            break;
                        case 2:
                            renamePatternCases[i] = j + "|" + matcher.group(1) + ";" + matcher.group(2) + ";" + matcher.group(3);
                            break;
                        case 3:
                            renamePatternCases[i] = j + "|" + matcher.group(1);
                            break;
                        case 4:
                            renamePatternCases[i] = j + "|" + matcher.group(1);
                            break;
                        case 5:
                            renamePatternCases[i] = j + "|" + matcher.group(1) + ";" + matcher.group(2);
                            break;
                        case 6:
                            renamePatternCases[i] = j + "|" + matcher.group(1);
                            break;
                        case 7:
                            renamePatternCases[i] = j + "|" + matcher.group(1) + ";" + matcher.group(2) + ";" + matcher.group(3);
                            break;
                        case 8:
                            renamePatternCases[i] = j + "|" + matcher.group(1);
                            break;
                        case 9:
                            renamePatternCases[i] = j + "|" + matcher.group(1) + ";" + matcher.group(2) + ";" + matcher.group(3);
                            break;
                    }
                }
            }
        }
        return renamePatternCases;
    }

    public String renameByRegEx(String input, String regEx) {
        //String nameOfFile, CollectionDefinition colDef, String renameOn
        String[] renameCases = renamePatternCases(regEx);
        for(String renameCase : renameCases){
            String[] renameOptions = renameCase.split("\\|");
            int caseNumber = Integer.parseInt(renameOptions[0]);
            renameCase = renameOptions[1];
            String[] renameParams = new String[3];
            switch(caseNumber){
                case 0: //Extention of Filename
                    switch (renameCase){
                        case "U": //Uppercase
                            input = input.substring(0, input.lastIndexOf('.') + 1) + input.substring(input.lastIndexOf('.') + 1).toUpperCase();
                            break;
                        case "L": //Lowercase
                            input = input.substring(0, input.lastIndexOf('.') + 1) + input.substring(input.lastIndexOf('.') + 1).toLowerCase();
                            break;
                    }
                    break;
                case 1: //Entire Filename
                    switch (renameCase){
                        case "U": //Uppercase
                            input = input.toUpperCase();
                            break;
                        case "L": //Lowercase
                            input = input.toLowerCase();
                            break;
                    }
                    break;
                case 2: //Do action on a Substring
                    renameParams = renameCase.split(";");
                    renameCase = renameParams[0];
                    int subStringNum1 = Integer.parseInt(renameParams[1]);
                    int subStringNum2 = Integer.parseInt(renameParams[2]);
                    switch(renameCase){
                        case "U": //Substring to Uppercase
                            input = input.substring(0, subStringNum1 - 1) + input.substring(subStringNum1 - 1, subStringNum2).toUpperCase() + input.substring(subStringNum2);
                            break;
                        case "L": //Substring to Lowercase
                            input = input.substring(0, subStringNum1 - 1) + input.substring(subStringNum1 - 1, subStringNum2).toLowerCase() + input.substring(subStringNum2);
                            break;
                        case "R": //Remove Substring from Filename
                            input = input.substring(0, subStringNum1 - 1) + input.substring(subStringNum2);
                            break;
                        case "C": //Use Substring as new Filename
                            input = input.substring(subStringNum1 - 1, subStringNum2);
                            break;
                    }
                    break;
                case 3: //Add Substring to Start of Filename
                    input = renameCase + input;
                    break;
                case 4: //Add Substring to End of Filename
                    input = input + renameCase;
                    break;
                case 5: //Add Substring to specified position in Filename
                    renameParams = renameCase.split(";");
                    int subStringNum = Integer.parseInt(renameParams[0]);
                    renameCase = renameParams[1];
                    input = input.substring(0, subStringNum - 1) + renameCase + input.substring(subStringNum - 1);
                    break;
                case 6: //Add Hostname to specified position in Filename
                    int insertHostnamePoint = Integer.parseInt(renameCase);
                    input = input.substring(0, insertHostnamePoint - 1) + "HOSTNAME" + input.substring(insertHostnamePoint - 1);
                    break;
                case 7: //Add variable value to specified position in Filename
                    renameParams = renameCase.split(";");
                    int insertPoint = Integer.parseInt(renameParams[0]);
                    String variableDefined = renameParams[1];
                    String formatDefined = renameParams[2];
                    Date dateNow = null;
                    if(variableDefined.equals("CurrDate") || variableDefined.equals("PrevDate")){
                        dateNow = new Date();
                        if(variableDefined.equals("PrevDate")){
                            long longDate = dateNow.getTime() - 86400000; // subtract 1 day in milliseconds
                            dateNow.setTime(longDate);
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat(formatDefined);
                        renameCase = sdf.format(dateNow);
                        input = input.substring(0, insertPoint - 1) + renameCase + input.substring(insertPoint - 1);
                    }
                    break;
                case 8: //Replace Extention of Filename with Substring
                    input = input.substring(0, input.lastIndexOf('.') + 1) + renameCase;
                    break;
                default:
                    rename_service_logger.info("No pattern selected");
                    break;
            }
        }
        return input;
    }

    public static String replaceDynamicVariables(String serverFolder, String host) {
        String partString = ""; String dateString = "";
        int pos = 0; int startFormat = 0; int endFormat = 0;
        String token = ""; String dateFormat = "";
        Format dtFormat = null;

        if (serverFolder.contains("$CurrDate") || serverFolder.contains("$PrevDate")) {
            pos = serverFolder.contains("$CurrDate")? serverFolder.indexOf("[$CurrDate-") : serverFolder.indexOf("[$PrevDate-");
            partString = serverFolder.substring(pos, serverFolder.length());
            StringTokenizer st = new StringTokenizer(partString, "*");
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                startFormat = token.indexOf("-");
                endFormat = token.indexOf("]");
                dateFormat = token.substring(startFormat + 1, endFormat).trim();
            }
            Date dateNow = new Date();
            dtFormat = new SimpleDateFormat(dateFormat);
            if (serverFolder.contains("$PrevDate")) {
                long longDate = dateNow.getTime() - 86400000L;
                dateNow.setTime(longDate);
            }
            dateString = dtFormat.format(dateNow);
            serverFolder = serverFolder.replace(serverFolder.substring(pos, pos + endFormat + 1), dateString);
        }
        if(serverFolder.contains("[$Hostname]")) {
            serverFolder = serverFolder.replace("[$Hostname]", host);
        }
        return serverFolder;
    }

    public List<String> buildRenamePatternArray(String renamePattern) {
        List<String> renamePatternArray = new ArrayList<String>();
        String[] renamePatternValues = renamePattern.split(";");
        Pattern patt1 = Pattern.compile("((([UL][\\[][0-9]+[\\-][0-9]+[\\]][*]*))*)");
        Pattern patt2 = Pattern.compile("(([UL])([*]))");
        Pattern patt3 = Pattern.compile("(([UL])([*][\\.][*]))");
        Pattern patt4 = Pattern.compile("[*]");
        Pattern patt5 = Pattern.compile("(([*])([\\.])([UL])([*]))");
        Pattern patt6 = Pattern.compile("(([R])[\\[]([0-9])[\\-]([0-9])[\\]])");
        Pattern patt7 = Pattern.compile("(([A])[\\[](START)[\\]][\\']([0-9a-zA-Z \\_\\-\\.]*)[\\'])");
        Pattern patt8 = Pattern.compile("(([A])[\\[](END)[\\]][\\']([0-9a-zA-Z\\_\\-\\.]*)[\\'])");
        Pattern patt9 = Pattern.compile("(([A])[\\[]([0-9]+)[\\]][\\']([0-9a-zA-Z \\_\\-\\.]*)[\\'])");
        Pattern patt10 = Pattern.compile("(([X])[\\[][\\']([0-9a-zA-Z]*)[\\'][\\]])");

        Matcher matcher1 = null; Matcher matcher2 = null; Matcher matcher3 = null; Matcher matcher4 = null; Matcher matcher5 = null; Matcher matcher6 = null; Matcher matcher7 = null; Matcher matcher8 = null; Matcher matcher9 = null; Matcher matcher10 = null;
        if (renamePatternValues[0] != "") {
            for (int i = 0; i < renamePatternValues.length; i++) {
                if ((renamePatternValues[i].substring(0, 2).equalsIgnoreCase("C[")) || (renamePatternValues[i].contains("$Hostname")) || (renamePatternValues[i].contains("$CurrDate")) || (renamePatternValues[i].contains("$PrevDate"))) {
                    renamePatternArray.add("0|" + renamePatternValues[i]);
                }
                else {
                    matcher1 = patt1.matcher(renamePatternValues[i]);
                    matcher2 = patt2.matcher(renamePatternValues[i]);
                    matcher3 = patt3.matcher(renamePatternValues[i]);
                    matcher4 = patt4.matcher(renamePatternValues[i]);
                    matcher5 = patt5.matcher(renamePatternValues[i]);
                    matcher6 = patt6.matcher(renamePatternValues[i]);
                    matcher7 = patt7.matcher(renamePatternValues[i]);
                    matcher8 = patt8.matcher(renamePatternValues[i]);
                    matcher9 = patt9.matcher(renamePatternValues[i]);
                    matcher10 = patt10.matcher(renamePatternValues[i]);

                    if (matcher1.matches()) {
                        renamePatternArray.add("1|" + renamePatternValues[i]);
                    }
                    if (matcher2.matches()) {
                        renamePatternArray.add("2|" + renamePatternValues[i]);
                    }
                    if (matcher3.matches()) {
                        renamePatternArray.add("3|" + renamePatternValues[i]);
                    }
                    if (matcher4.matches()) {
                        renamePatternArray.add("4|" + renamePatternValues[i]);
                    }
                    if (matcher5.matches()) {
                        renamePatternArray.add("5|" + renamePatternValues[i]);
                    }
                    if (matcher6.matches()) {
                        renamePatternArray.add("6|" + renamePatternValues[i]);
                    }
                    if (matcher7.matches()) {
                        renamePatternArray.add("7|" + renamePatternValues[i]);
                    }
                    if (matcher8.matches()) {
                        renamePatternArray.add("8|" + renamePatternValues[i]);
                    }
                    if (matcher9.matches()) {
                        renamePatternArray.add("9|" + renamePatternValues[i]);
                    }
                    if (matcher10.matches())
                        renamePatternArray.add("10|" + renamePatternValues[i]);
                }
            }
        }
        return renamePatternArray;
    }
}
