package za.co.vodacom.vodacommft.service.impl;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.config.PropertiesFileSysConfig;
import za.co.vodacom.vodacommft.entity.sfg_cfg.AgeAnalysisConfigEntity;
import za.co.vodacom.vodacommft.entity.sfg_cfg.AgeAnalysisIgnoreEntity;
import za.co.vodacom.vodacommft.entity.sfg_rpt.AgeAnalysisEntity;
import za.co.vodacom.vodacommft.entity.sfg_rpt.ProcessedFileEntity;
import za.co.vodacom.vodacommft.exception.AgeAnalysisIgnoreEntityNotFoundException;
import za.co.vodacom.vodacommft.exception.AgeAnalysisRouteCodeNotFoundException;
import za.co.vodacom.vodacommft.exception.ProcessedFileEntityNotFoundException;
import za.co.vodacom.vodacommft.repository.sfg_cfg.AgeAnalysisConfigRepository;
import za.co.vodacom.vodacommft.repository.sfg_cfg.AgeAnalysisIgnoreEntityRepository;
import za.co.vodacom.vodacommft.repository.sfg_rpt.AgeAnalysisEntityRepository;
import za.co.vodacom.vodacommft.repository.sfg_rpt.ProcessedFileEntityRepository;
import za.co.vodacom.vodacommft.service.IAgeAnalysisService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("all")
@Service
public class AgeAnalysisService implements IAgeAnalysisService {

    @Autowired
    private ProcessedFileEntityRepository processed_file_entity_repository;

    @Autowired
    private AgeAnalysisIgnoreEntityRepository ageAnalysis_ignore_entity_repository;

    @Autowired
    private PropertiesFileSysConfig propertiesFileSysConfig;

    @Autowired
    private AgeAnalysisEntityRepository ageAnalysisEntityRepository;

    @Autowired
    private AgeAnalysisConfigRepository ageAnalysisConfigRepository;



    private static final Logger logger = LoggerFactory.getLogger(AgeAnalysisService.class);

    private long b1Tot = 0, b2Tot = 0, b3Tot = 0, b4Tot = 0, b5Tot = 0, b6Tot = 0, b7Tot = 0, b8Tot = 0,
            totEvnt = 0, totSec = 0, shortEvnt = 0, longEvnt = 0, medianFileTime = 0,
            b1ms = 0, b2ms = 0, b3ms = 0, b4ms = 0, b5ms = 0, b6ms = 0, b7ms = 0, excludeFirst = 0, excludeLast = 0,
            numberOfLines = 0, currentLine = 0, nrtdeInd = 0, ageMilSec = 0;;

    private int i = 0, j = 0, headerPos = 0, trailerPos = 0, positionIndex = 0, headerLen = 0, trailerLen = 0,
            durationPos = 0, offsetPos = 0, lookupPos = 0, startTimePos = 0, endTimePos = 0, eventTypePos = 0,
            networkPos = 0, startTimeLen = 0, endTimeLen = 0, durationLen = 0, offsetLen = 0, lookupLen = 0,
            eventTypeLen = 0, networkLen = 0;

    private String deliveredFileKey = "", nrtdeI = "", workflowId = "", networkName = "", error = "",
            logicalConsumerName = "", query = "", siTSFormat = "", returnCol = "", lookupTable = "", lookupCol = "", line1 = "",
            headerID = "", trailerID = "", eventID = "", var1 = "", fileType = "", delimiter = "",
            networkCode = "", currentNetworkCode = "", startTimeFormat = "", endTimeFormat = "", durationFormat = "";

    boolean excludeHeader = false, excludeTrailer = false, calcBucket = false, parsedAlready = false,
            gotDuration = false, gotOffset = false, gotEventDate = false, dbSuccess = false, calcBucketSuccess = false;

    Date eventDate = null, startDate = null, endDate = null, dtDate = null;
    private java.sql.Connection repCon = null, cfgCon = null, usrCon = null;
    private PreparedStatement repStmt = null, cfgStmt = null, usrStmt = null;
    private ResultSet res = null;

    private CharSequence emptyString = "", dataIDChar = "";
    List<String> excludeDetail = new ArrayList<String>();
    List<Long> excludeLines = new ArrayList<Long>();
    List<Integer> excludePosition = new ArrayList<Integer>();
    List<Integer> excludeLen = new ArrayList<Integer>();
    List<String> excludeOperator = new ArrayList<String>();

    String[] values;
    String[] eventIDArray;
    String allFileNames[] = {"","","","","","","","","","","","","","",""}; //Contains AgeAnalysis=y/n(0);WFID(1);SIname(2);SFGname(3);remoteName(4);ArrivedFileKey(5);DeliveredFileKey(6);DeliveryUID(7);"dataflowID,routeMetaData"(8);DeliveryReplayInd(9);DeliveredCnt(10);FileSize(11);DeliverTimeStamp(12);DeliveryStatus(13);Protocol(14)
    String eventType = "";

    public AgeAnalysisService() {
        super();
    }

    @Override
    public boolean ageAnalyseFileProcessing(String fileName, String routeCode) {

        allFileNames = fileName.split(";");
        this.deliveredFileKey = allFileNames[5].trim();
        this.workflowId = allFileNames[1].trim();

        if (allFileNames[7] == null){ //DeliveryUID
            allFileNames[7] = "0";
        }
        if (allFileNames[7].trim().equals("")){
            allFileNames[7] = "0";
        }
        if (allFileNames[12] == null){ //Delivery Timestamp
            allFileNames[12] = "0";
        }
        if (allFileNames[12].trim().equals("")){
            allFileNames[12] = "0";
        }
        long deliveryTime = 0;
        if (!allFileNames[12].trim().equals("null")){
            deliveryTime = Long.parseLong(allFileNames[12].trim());
        }
        long deliveryUID = Long.parseLong(allFileNames[7].trim());
        Date deliveryTS = new Date();
        DateFormat deliveryFormat = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        String deliveryDate = "";
        String tempStatus = allFileNames[13].trim(); //Status from delivery process
        if (allFileNames[9].trim().equalsIgnoreCase("yes")){ //Replay
            if (Integer.parseInt(tempStatus) < 90){ //90 and up are errors
                tempStatus = propertiesFileSysConfig.getReplayedDelivery();
            }else{ //Error on Replay
                tempStatus =  propertiesFileSysConfig.getErrorReplayDelivery();
            }
        }
        String[] dataflowRouteMetaData = allFileNames[8].split(","); //dataFlowID,SI_arrivedfile_key,SI_route_key,SI_delivery_key,producer,consumer,filename

        long masterAgeUID = 0;
        boolean foundIt = false;

        AgeAnalysisConfigEntity ageAnalysis_config_values = findAgeAnalysisConfigEntitiesByRoute_code(routeCode);
        this.siTSFormat = "yyyy-MM-dd HH:mm:ss";
        if(ageAnalysis_config_values != null){
            logger.info(" AGE_ANALYSIS_CONFIG exists for :-" + ageAnalysis_config_values.getAge_config_uid());
            masterAgeUID = ageAnalysis_config_values.getAge_config_uid();
            startTimePos = ageAnalysis_config_values.getEventStartTimePos() - 1;
            startTimeLen = ageAnalysis_config_values.getEventStartTimeLen();
            startTimeFormat = ageAnalysis_config_values.getEventStartTimeFormat();
            endTimePos = ageAnalysis_config_values.getEventEndTimePos() - 1;
            endTimeLen = ageAnalysis_config_values.getEventEndTimeLen();
            endTimeFormat = ageAnalysis_config_values.getEventEndTimeFormat();
            durationPos = ageAnalysis_config_values.getEventDurationPos() - 1;
            durationLen = ageAnalysis_config_values.getEventDurationLen();
            durationFormat = ageAnalysis_config_values.getEventDurationFormat();
            offsetPos = ageAnalysis_config_values.getTimezoneOffsetPos() - 1;
            offsetLen = ageAnalysis_config_values.getTimezoneOffsetLen();
            nrtdeI = ageAnalysis_config_values.getNrtdeIndicator();
            fileType = ageAnalysis_config_values.getFile_type();
            dataIDChar = ageAnalysis_config_values.getDataIdChar();
            delimiter = ageAnalysis_config_values.getFile_delimiter();
            headerID = ageAnalysis_config_values.getHdrId();
            headerPos = ageAnalysis_config_values.getHdrIdPos() - 1;
            headerLen = ageAnalysis_config_values.getHdrIdLen();
            trailerID = ageAnalysis_config_values.getTrailerId();
            trailerPos = ageAnalysis_config_values.getTrIdPos() - 1;
            trailerLen = ageAnalysis_config_values.getTrIdLen();
            String temp_event_id = ageAnalysis_config_values.getEventId();
            eventIDArray = temp_event_id.split(";");
            lookupPos = ageAnalysis_config_values.getLookupFieldPos() - 1;
            lookupLen = ageAnalysis_config_values.getLookupFieldLen();
            this.lookupTable = ageAnalysis_config_values.getLookupTableName(); //hahaha fuck!!!!!!// Lookup Table is here in AA_Config
            this.lookupCol = ageAnalysis_config_values.getLookupColumn();
            this.returnCol = ageAnalysis_config_values.getReturnColumn();
            eventTypePos = ageAnalysis_config_values.getEventTypePos() - 1;
            eventTypeLen = ageAnalysis_config_values.getEventTypeLen();
            logicalConsumerName = ageAnalysis_config_values.getLogicalConsumerName();
            //TODO Add  eventType = ageAnalysis_config_values.getDescription(); IF REQUIRED
            eventType = String.valueOf(ageAnalysis_config_values.getEventTypePos());
            //TODO Pliz investigate this.. one value of type int passed to both string & in Variable WHY ???:- EVENT_TYPE_POS

        }else {
            logger.info(" !!!!SANGOMA:- AGE_ANALYSIS_CONFIG Does Not Exist For :-" + ageAnalysis_config_values.getAge_config_uid());
        }

        logger.info("After the getProcessDataContents");
        String[] excludeRange;
        int j = 0;

        nrtdeInd = Long.parseLong(nrtdeI);
        BufferedReader br = null;
        DateFormat dT = new SimpleDateFormat(this.siTSFormat);
        dT.setTimeZone(TimeZone.getDefault());
        dtDate = deliveryTS;
        Date tempDate = new Date();
        tempDate.setTime(dtDate.getTime() - 60000); //Set temp time back one minute to act as stand-in received timestamp if actual received is not available. 1 minute because this process will run at least one minute after initially receiving the file
        logger.info("tempDate " + tempDate.toString());
        logger.info("dtDate " + dtDate.toString());
        Date rtDate = null;

//		Lookup PRODUCER_MBX_WRITE_TS using ARRIVED_FILE_KEY in RouteMetaData in process data *************************************
        foundIt = false;
        String receivedTS = "";
        receivedTS = dT.format(tempDate);
        logger.info("Interim receivedTS: " + receivedTS);
        ProcessedFileEntity processed_file_entity = findProcessedFileEntityByArrivedfilekey(allFileNames[5].trim());
        if(processed_file_entity != null){
            logger.info(":- VODACOM Age Analysis Processed_File_Entity Exist For :- "+ allFileNames[5].trim());
            receivedTS = processed_file_entity.getProducerMbxWriteTs().trim();
            if (receivedTS == "" || receivedTS == null){ // no need for this check... i will remove this shit .. but its cool
                receivedTS = dT.format(tempDate);
            }

        }else{
            logger.info("!!! SANGOMA : :- VODACOM Age Analysis Processed_File_Entity Does Not Exist For:- "+  allFileNames[5].trim() );
            receivedTS = dT.format(tempDate);
        }
        logger.info("Vodacom Age Analysis Final ReceivedTS:- " + receivedTS);


//		End of REPORT DB lookup *********************************************************************************************************

//		While we're busy, may as well get the exclusion records from AGE_ANALYSIS_IGNORE
//		String poolName = "vodaPool";
        String exclDet = "";
        List<AgeAnalysisIgnoreEntity> ageAnalysis_ignoreEntity_list_values = findAgeAnalysisIgnoreEntitiesByAge_uid(masterAgeUID);
        for(AgeAnalysisIgnoreEntity ageAnalysisIgnoreEntity_list : ageAnalysis_ignoreEntity_list_values){
            values = ageAnalysisIgnoreEntity_list.getLineDet().trim().split(",");
            for(i=0; i < values.length; i++){
                exclDet = values[i].trim();
                if (exclDet.equals("HD")){
                    excludeHeader = true;
                }
                if (exclDet.equals("TR")){
                    excludeTrailer = true;
                }
                if (exclDet.startsWith("F") && exclDet.substring(1,exclDet.length()).matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")){
                    excludeFirst = Long.parseLong(exclDet.substring(1,exclDet.length()));
                }
                if (exclDet.startsWith("L") && exclDet.substring(1,exclDet.length()).matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")){
                    excludeLast = Long.parseLong(exclDet.substring(1,exclDet.length()));
                }
                if (exclDet.trim().matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")){
                    excludeLines.add(Long.parseLong(exclDet.substring(1,exclDet.length())));
                }
                if (exclDet.contains("-")){
                    long startValue = 0, endValue = 0;
                    excludeRange = exclDet.split("-");
                    for (j=0; j < excludeRange.length; j++){
                        if (j == 0){
                            startValue = Long.parseLong(excludeRange[j].trim());
                        }
                        if (j == 1){
                            endValue = Long.parseLong(excludeRange[j].trim());
                        }
                    }
                    for (long k=startValue; k < endValue; k++) {
                        excludeLines.add(k);
                    }
                }
                if (ageAnalysisIgnoreEntity_list.getLineDet().trim().contains("\"")){
                    excludeDetail.add(ageAnalysisIgnoreEntity_list.getLineDet().trim());
                    excludePosition.add(ageAnalysisIgnoreEntity_list.getDetPosition()); //Herbie used Integer directly
                    excludeLen.add(ageAnalysisIgnoreEntity_list.getDetLen());
                    excludeOperator.add(ageAnalysisIgnoreEntity_list.getDetOperator().trim());
                }

            }

        }
        logger.info(">>>> Age Analysis logic continues>>>>>>");

        try {
            rtDate = dT.parse(receivedTS);
        } catch (ParseException e) {
            logger.error("ParseException:- Error Parsing Received_TS to Rt_Date:- "+ e.getMessage());

            return false;
        }

        medianFileTime = (dtDate.getTime() - rtDate.getTime()) / 1000;

//		if we have to exclude last x number of lines, we have to determine total number of lines in file; what a shocker
        String line = null;
        Map<String, List<String>> map = new TreeMap<String, List<String>>(); //Used in case we have to sort CSV file
//		String[] fileSubtype = {"",""};

//		Count number of lines to exclude last - hate this logic
        if (excludeLast > 0){
            logger.info(">>>>Count number of lines to exclude last - hate this logic");
            logger.info(">>>>FileName: - " + allFileNames[2].trim());
            try {
                br = new BufferedReader(new FileReader(allFileNames[2].trim())); //SI internal filename
                while ((line = br.readLine()) != null) {
                    numberOfLines++;
                }
                br.close();
                br = null;
            }catch(Exception ex){
                ex.printStackTrace();
                try{br.close();}catch(Exception brCL){}
                br = null;
            }
        }

        try {
            if (nrtdeI.trim().equals("2")){ //NRTDE needs to be sorted s
                logger.info("nrtdeI:" + nrtdeI);
                br = new BufferedReader(new FileReader(allFileNames[2].trim())); //SI internal filename
                boolean includeLine = false;
                while ((line = br.readLine()) != null) {
                    if (excludeLast < 1){
                        numberOfLines++; //ExcludeLast logic above would have already counted the number of lines
                    }

                    line1 = line;
                    currentLine++;
                    if (excludeFirst > 0 && currentLine <= excludeFirst){
                        logger.info("Excluding first line:" + line);
                        continue;
                    }
                    if (excludeLast > 0 && currentLine > (numberOfLines - excludeLast)){
                        logger.info("Excluding last line:" + line);
                        continue;
                    }

                    if (excludeLineByNumber(currentLine)){
                        continue;
                    }
                    if (fileType.equals("DEL")){
                        values = line1.split(delimiter);
                        for (i = 0; i < values.length; i++) {
                            var1 = values[i].trim();
                            if (!excludeDelimitedLineByType(var1, i)){
                                includeLine = true;
                                break;
                            }
                        }
                        if (!includeLine){
                            continue;
                        }
                    }
                    if (fileType.equals("POS")){
                        if (excludePositionalLineByType()){
                            continue;
                        }
                    }
                    //****************************************************** sort starts here
                    if (nrtdeI.trim().equals("2")){ //File must be sorted by network so may as well drop records while reading into memory so we don't have to read, sort, re-read, process - just read, sort, process
                        //************ actual sort starts now - up to here we've just been rejecting records
                        String key = getField(line);
                        List<String> l = map.get(key);
                        if (l == null) {
                            l = new LinkedList<String>();
                            map.put(key, l);
                        }
                        l.add(line);
                        parsedAlready = true;
                    }
                    //**************************************************** sort ends here
                }
                currentLine = 0;
            }

//			Now read through file again to start calculating.
            if (br == null && !parsedAlready){ //If parsedAlready, file is in memory and records already dropped and sorted ready for processing
                br = new BufferedReader(new FileReader(allFileNames[2].trim()));
            }
            line = null;
            if (nrtdeInd != 2) {
                while ((line = br.readLine()) != null) {
//					if (line.contains("File_subtype:")){
//						fileSubtype = line.split(":");
//						eventType = fileSubtype[1].trim();
//						age_analysis_service_logger.info("Assigned event type from file subtype:" + eventType + " " + fileSubtype);
//					}
                    calculateEvents(line);
                }
            }else{ //nrtdeInd = 2 files are already sorted in memory so read from memory map not physical file
                for (List<String> list : map.values()) {
                    for (String val : list) {
//						if (val.contains("File_subtype:")){
//							fileSubtype = val.split(":");
//							eventType = fileSubtype[1].trim();
//							age_analysis_service_logger.info("Assigned event type from file subtype:" + eventType + " " + fileSubtype);
//						}
                        calculateEvents(val);
                    }
                }
            }
//			SFG_WF_ID = workflowId;
//			if (nrtdeInd != 2) {
            if (totEvnt > 0){ //Stil more events to store
                if (nrtdeInd == 2) { //Lookup last record's network name
                    networkName = lookupTable(currentNetworkCode);
                    logger.info("networkCode: " + networkCode + " currentNetworkCode: " + currentNetworkCode);
//					if (!networkCode.trim().equalsIgnoreCase(currentNetworkCode.trim()) && currentNetworkCode.trim() != "") {
                }
                dbSuccess = storeAgeAnalysisRecord();
                if (!dbSuccess){
                    //TODO Proper Validation
                }
                networkName = "";
                error = "";
                totEvnt = 0;
                longEvnt = 0;
                shortEvnt = 0;
                b1Tot = 0;
                b2Tot = 0;
                b3Tot = 0;
                b4Tot = 0;
                b5Tot = 0;
                b6Tot = 0;
                b7Tot = 0;
                b8Tot = 0;
                totSec = 0;
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            logger.error("Error5: " + ex.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Error6: " + ex.toString());
        } catch (Exception genEx) {
            genEx.printStackTrace();
            logger.error("Error7: " + genEx.toString());
        } finally {
            try {
                if (br != null){
                    br.close();
                }
                //TODO Proper Validation
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.info("Error7: " + ex.toString());
                //TODO Proper Validation
                return false;
            }
        }
        try {
            if (br != null){
                br.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error7: " + ex.toString());
            return false;
        }
        return true;
    }
    private boolean storeAgeAnalysisRecord(){

        long medianEvntTime = (totSec / 1000) / totEvnt;
        longEvnt = longEvnt / 1000;
        shortEvnt = shortEvnt / 1000;
        String totalEvents = Long.toString(totEvnt);
        String longestDeliveryAge = Long.toString(longEvnt);
        String medianDeliveryAge = Long.toString(medianEvntTime);
        String shortestDeliveryAge = Long.toString(shortEvnt);
        String bucket1 = Long.toString(b1Tot);
        String bucket2 = Long.toString(b2Tot);
        String bucket3 = Long.toString(b3Tot);
        String bucket4 = Long.toString(b4Tot);
        String bucket5 = Long.toString(b5Tot);
        String bucket6 = Long.toString(b6Tot);
        String bucket7 = Long.toString(b7Tot);
        String bucket8 = Long.toString(b8Tot);
        String mediationTime = Long.toString(medianFileTime);

        String country = null;
        logger.info("Record to be inserted in DB");
        logger.info("===========================");
        logger.info("DELIVEREDFILE_KEY: " + deliveredFileKey);
        logger.info("EVENT_AGE_TYPE: " + nrtdeI);
        logger.info("TOTAL_EVENTS: " + totalEvents);
        logger.info("LONGEST_DELIVERY_AGE: " + longestDeliveryAge);
        logger.info("MEDIAN_DELIVERY_AGE: " + medianDeliveryAge);
        logger.info("SHORTEST_DELIVERY_AGE: "
                + shortestDeliveryAge);
        logger.info("BUCKET_1: " + bucket1);
        logger.info("BUCKET_2: " + bucket2);
        logger.info("BUCKET_3: " + bucket3);
        logger.info("BUCKET_4: " + bucket4);
        logger.info("BUCKET_5: " + bucket5);
        logger.info("BUCKET_6: " + bucket6);
        logger.info("BUCKET_7: " + bucket7);
        logger.info("BUCKET_8: " + bucket8);
        logger.info("MEDIATION_TIME: " + mediationTime);
        logger.info("SFG_WF_ID: " + workflowId);
        logger.info("NETWORK: " + networkName);
        logger.info("COUNTRY: " + country);
        logger.info("ERROR: " + error);
        logger.info("LOGICAL_CONSUMER_NAME: " + logicalConsumerName);
        logger.info("FILENAME: " + allFileNames[3]);
        logger.info("EVENT_TYPE: " + eventType);

        if(insertIntoAgeAnalysisEntity(medianEvntTime, country)){
            logger.info("VODACOM ....Age Analysis Insert Successful");
            return true;
        }else {
            logger.info("!!! SANGOMA  ....Age Analysis Insert FAILED !!!");
            return false;
        }


    }

    //@Query(value = "select  lt.returnCol  from  lookupTable lt where  lt.lookupCol  = ?1")
    private String lookupTable(String var1){ //TODO LOOKUP with Jan
        cfgStmt = null;
        res = null;
        boolean foundIt = false;
        try {
            query = "select " + returnCol + " from " + lookupTable + " where " + lookupCol + " = ?";
            cfgStmt = cfgCon.prepareStatement(query);
            cfgStmt.setString(1, var1);
            res = cfgStmt.executeQuery();
//			System.out.println("VODACOM Config pool.... " + lookupTable + " Select Successful");
            foundIt = res.next();
            if (foundIt) {
                var1 = res.getString(returnCol).trim();
//				System.out.println("VODACOM Config pool.... " + lookupTable + " record exists");
            }else{
                var1 = "UNKNOWN";
                System.out.println("VODACOM Config pool.... " + lookupTable + " record does not exist");
            }
        } catch (Exception e) {
            var1 = "UNKNOWN";
            System.out.println("VODACOM Config pool .... " + lookupTable + " Select Failed!");
            e.printStackTrace();

        }
        return var1;
//		End of lookup table lookup *********************************************************************************************************
    }
    private boolean calcBucket(long ageMilSec, String line1){
        this.b1ms = Long.parseLong(propertiesFileSysConfig.getBucket1()) * 1000;
        this.b2ms = Long.parseLong(propertiesFileSysConfig.getBucket2()) * 1000;
        this.b3ms = Long.parseLong(propertiesFileSysConfig.getBucket3()) * 1000;
        this.b4ms = Long.parseLong(propertiesFileSysConfig.getBucket4()) * 1000;
        this.b5ms = Long.parseLong(propertiesFileSysConfig.getBucket5()) * 1000;
        this.b6ms = Long.parseLong(propertiesFileSysConfig.getBucket6()) * 1000;
        this.b7ms = Long.parseLong(propertiesFileSysConfig.getBucket8()) * 1000;
        this.siTSFormat = "yyyy-MM-dd HH:mm:ss";
        boolean calcResult = false;
        while (!calcResult){
            if (ageMilSec < 0) {
                System.out.println("Age < 0: " + line1);
                calcResult = true;
//				calcResult = false;
                continue;
            }
            totSec = totSec + ageMilSec;
            if (shortEvnt == 0 || (ageMilSec < shortEvnt && ageMilSec > 0)) {
                shortEvnt = ageMilSec;
            }
            if (longEvnt == 0 || ageMilSec > longEvnt) {
                longEvnt = ageMilSec;
            }
            if (ageMilSec > 0 && ageMilSec <= b1ms) {
                b1Tot++;
                calcResult = true;
                continue;
            }
            if (ageMilSec > b1ms && ageMilSec <= b2ms) {
                b2Tot++;
                calcResult = true;
                continue;
            }
            if (ageMilSec > b2ms && ageMilSec <= b3ms) {
                b3Tot++;
                calcResult = true;
                continue;
            }
            if (ageMilSec > b3ms && ageMilSec <= b4ms) {
                b4Tot++;
                calcResult = true;
                continue;
            }
            if (ageMilSec > b4ms && ageMilSec <= b5ms) {
                b5Tot++;
                calcResult = true;
                continue;
            }
            if (ageMilSec > b5ms && ageMilSec <= b6ms) {
                b6Tot++;
                calcResult = true;
                continue;
            }
            if (ageMilSec > b6ms && ageMilSec <= b7ms) {
                b7Tot++;
                calcResult = true;
                continue;
            }
            if (ageMilSec > b7ms) {
                b8Tot++;
                calcResult = true;
                continue;
            }
            calcResult = false;
            continue;
        }
        return calcResult;
    }

    private Date getStartDate(String eventDT, String startTimeFormat){
        Date startDate = null;
        DateFormat startTime = new SimpleDateFormat(
                startTimeFormat);
        startTime.setTimeZone(TimeZone.getDefault());
        System.out.println("**************** SomeWhere*****");
        try {
            System.out.println(">>> Inside getStartDate :- "+ eventDT);
            startDate = startTime.parse(eventDT);
        } catch (ParseException ex) {
            System.out.println(">>> Inside Catch of getStartDate :- "+ eventDT);
            ex.printStackTrace();
            error = "Error3: " + ex.toString();
        }
        return startDate;
    }

    private Date getEndDate(String eventDT, String endTimeFormat){
        Date endDate = null;
        System.out.println(">>>>>> Inside getEndDate(endTimeFormat) :-  " + endTimeFormat);
        System.out.println(">>>>>> Inside getEndDate(eventDT) :-  " + eventDT);

        DateFormat endTime = new SimpleDateFormat(endTimeFormat);

        endTime.setTimeZone(TimeZone.getDefault());
        try {
            endDate = endTime.parse(eventDT);
        } catch (ParseException ex) {
            ex.printStackTrace();
            error = "Error3: " + ex.toString();
        }
        return endDate;
    }

    private Date getEventDate(String offSet, Date eventDate){
        long eDate = 0;
        long offMin = Long.parseLong(offSet.substring(3, 5)) * 60 * 1000;
        long offHr  = Long.parseLong(offSet.substring(1, 3)) * 60 * 60 * 1000;
        long offHour = offMin + offHr;
        if (offSet.substring(0, 1).equals("+")) {
            if (offSet.substring(0, 3).equals("+0200")){ //local time
                eDate = (eventDate.getTime() + offHour);
                return eventDate;
            }
        }
        if (offSet.substring(0, 1).equals("-")) {
            eDate = (eventDate.getTime() - offHour);
        }
        eventDate.setTime(eDate);
        return eventDate;
    }

    private Date calcEndDate(String duration, String durationFormat, Date eventDate){
        Date endDate = new Date();
        if (duration.equals("")) {
            duration = "00:00";
        }

//		Determine duration format, i.e. ms, ss, mm, hh, hh:mm:ss, or hh:mm:ss:ms
        if (duration == null || duration.equals("")){
            duration = "0";
        }
        String[] durationFormatValues = durationFormat.split("\\:");
        String[] durationValues = duration.split("\\:");
        long longDur = 0;
        for (int j=0; j < durationFormatValues.length; j++){
            if (durationFormatValues[j].equals("HH")){
                longDur = longDur + (Long.parseLong(durationValues[j]) * 1000 * 60 * 60);
            }
            if (durationFormatValues[j].equals("mm")){
                longDur = longDur + (Long.parseLong(durationValues[j]) * 1000 * 60);
            }
            if (durationFormatValues[j].equals("ss")){
                longDur = longDur + (Long.parseLong(durationValues[j]) * 1000);
            }
            if (durationFormatValues[j].equals("SS")){
                longDur = longDur + Long.parseLong(durationValues[j]);
            }
        }
//		Now that we have duration in milliseconds, add to event start date
        long eDate = (eventDate.getTime() + longDur);
        endDate.setTime(eDate);
        return endDate;
    }

    private String getField(String line) {
        return line.split(",")[1];// extract value you want to sort on
    }

    private boolean excludeLineByNumber(long currentLine){
//		Should we exclude lines by line number and if so, is this line one of them?
        if (excludeLines.contains(currentLine)){
            return true;
        }
//		Should we exclude first x number of lines and if so, is this line one of them?
        if (excludeFirst > 0 && currentLine <= excludeFirst){
            return true;
        }
        return false;
    }

    private boolean excludeDelimitedLineByType(String var1, int i){
//		Should we exclude headers and if so, is this a header?
        if (excludeHeader && i == headerPos && var1.equals(headerID)){
            return true;
        }
//		Should we exclude trailers and if so, is this a trailer?
        if (excludeTrailer && i == trailerPos && var1.equals(trailerID)){
            return true;
        }
//		Should we exclude certain records with certain values in certain positions (seriously??) and if so, is this line one of them?
//		Work through list of positions to see if current position is in the list (possible contender for exclusion)
//		Exclude lines that contain a certain string
        if (excludePosition.contains(i+1)){
            positionIndex = excludePosition.indexOf(i+1);
            if (excludeDetail.get(positionIndex).trim().equals(var1)){
                if (excludeOperator.get(positionIndex).trim().equals("=")){
                    return true;
                }
            }
        }
//		Exclude lines that does not contain a certain string
        if (excludePosition.contains(i+1)){
            positionIndex = excludePosition.indexOf(i+1);
            if (!excludeDetail.get(positionIndex).trim().equals(var1)){
                if (excludeOperator.get(positionIndex).trim().equals("!=")){
                    return true;
                }
            }
        }

        int eventIDNotMatched = 0;
        for (int eventIDIndex = 0; eventIDIndex < eventIDArray.length; eventIDIndex++){
            if (eventIDArray[eventIDIndex] != null && eventIDArray[eventIDIndex] != "" && !line1.contains(eventIDArray[eventIDIndex])) {  //eventID not in line
                eventIDNotMatched++; //if event ID array contains event IDs that are not matched to the record, then exclude the record
            }
        }
        if (eventIDNotMatched < eventIDArray.length){
            return false; //At least one of the event IDs match the record so do not exclude this record
        } else{
            return true; //None of the event IDs match the record detail, so exclude this record
        }
    }

    private boolean excludePositionalLineByType(){
//		Should we exclude headers and if so, is this a header?
        if (excludeHeader && line1.substring(headerPos, (headerPos + headerLen)).contains(headerID)){
            return true;
        }
//		Should we exclude trailers and if so, is this a trailer?
        if (excludeTrailer && line1.substring(trailerPos, (trailerPos + trailerLen)).contains(trailerID)){
            return true;
        }
//		Should we exclude certain records with certain values in certain positions (seriously??) and if so, is this line one of them?
//		Work through list of positions to see if current position is in the list (possible contender for exclusion)
//		Exclude lines that contain a certain string
//		excludeThisLine = false;
        for (int i = 0; i < excludeOperator.size(); i++){
            if (excludeOperator.get(i).equals("=") && line1.substring(excludePosition.get(i) - 1, ((excludePosition.get(i) - 1) + excludeLen.get(i))).contains(excludeDetail.get(i))){
//				excludeThisLine = true;
                return true;
            }
            if (excludeOperator.get(i).equals("!=") && !line1.substring(excludePosition.get(i) - 1, ((excludePosition.get(i) - 1) + excludeLen.get(i))).contains(excludeDetail.get(i))){
//				excludeThisLine = true;
                return true;
            }
        }

//		Exclude non-event records
        if (eventID != null && eventID.trim() != "" && !line1.contains(eventID)) {  //eventID not in line
            return true;
        }
        return false;
    }

    private void calculateEvents(String line){
        System.out.println(">>>>> Inside calculateEvents()  "+ line);
        var1 = "";
        i = 0;
        if (dataIDChar != null){
            line1 = line.replace(dataIDChar, emptyString);
        }else{
            line1 = line;
        }

        currentLine++;
//		Should we exclude lines by line number and if so, is this line one of them?
        if (!parsedAlready && excludeLineByNumber(currentLine)){
            return;
        }
//		Should we exclude last x number of lines and if so, is this line one of them?
//		System.out.println("We should get out here:" + parsedAlready + " " + excludeLast + " " + currentLine + " " + numberOfLines + " " + excludeLast);
        if (!parsedAlready && excludeLast > 0 && currentLine > (numberOfLines - excludeLast)){
            return;
        }

//		Set basic variables for processing - for each line need start time and duration, or end time, and possibly timezone offset
//		Can only perform age analysis once all the above is in place or determined as not applicable
        gotDuration = false;
        gotOffset = false;
        gotEventDate = false;
        eventDate = null;
        endDate = null;
        startDate = null;
        if (offsetPos < 0){ //no need to consider offset
            gotOffset = true;
        }
        if (durationPos < 0){ //no need to consider duration
            gotDuration = true;
        }

//		********************************************************************************************
//		Delimited file logic starts here...
//		********************************************************************************************
        if (fileType.equals("DEL")){
//			System.out.println("Still processing");
            values = line1.split(delimiter);
            calcBucket = false;
            for (i = 0; i < values.length; i++) {
                var1 = values[i].trim();
//				if (i == eventTypePos){
//					eventType = var1;
//				}
//				Determining record types and exclusions start here - the flexibility is a little OTT to be honest
                if (!parsedAlready && excludeDelimitedLineByType(var1, i)){
                    continue;
                }
//				Now that we've excluded a million lines, we can finally start working
//				Some weird lookup table logic to change certain fields in the file - used for NRTDE at the moment
                if (lookupPos == i && nrtdeInd != 2) {
                    var1 = lookupTable( var1); //TODO Lookup call properly
                }

                //Check NETWORK code change for type 2 files
//				System.out.println("About to check field 1 " + var1);
                if (i == 1 && nrtdeInd == 2) {
                    if (var1.trim().equalsIgnoreCase("")){
                        continue;
                    }
                    networkCode = var1.substring(0, 5);
                    networkName = lookupTable(currentNetworkCode); //TODO Lookup call properly
                    System.out.println("networkCode: " + networkCode + " currentNetworkCode: " + currentNetworkCode);
                    if (!networkCode.trim().equalsIgnoreCase(currentNetworkCode.trim()) && currentNetworkCode.trim() != "") {
                        //*********************************************************************************
                        // Store previous totals for NETWORK  *********************************************
                        //*********************************************************************************
                        dbSuccess = storeAgeAnalysisRecord();
                        if (!dbSuccess){
                            // TODO Add validation Message
                        }
                        //Reset variables to start next NETWORK's totals
                        networkName = "";
                        error = "";
                        totEvnt = 0;
                        longEvnt = 0;
                        shortEvnt = 0;
                        b1Tot = 0;
                        b2Tot = 0;
                        b3Tot = 0;
                        b4Tot = 0;
                        b5Tot = 0;
                        b6Tot = 0;
                        b7Tot = 0;
                        b8Tot = 0;
                        totSec = 0;
                    }
                    currentNetworkCode = networkCode;
                }
                //*********************************************************************************
                // End Of Store previous totals for NETWORK  **************************************
                //*********************************************************************************
                if (i == startTimePos) {
                    startDate = getStartDate(var1, startTimeFormat);
//					gotStartTime = true;
//					continue;
                }
                if (i == endTimePos) {
                    System.out.println(">>>>> endTimePos :- " + var1 + "&& endTimeFormat:- "+ endTimeFormat);
                    endDate = getEndDate(var1, endTimeFormat);
//					calcBucket = true;
                }
                if (endTimePos >= 0){ //Event End Time given in file
                    eventDate = endDate;
                } else { //Only start date provided so end date is calculated as start date + duration
                    eventDate = startDate;
                }

                if (i == offsetPos) { // && nrtdeInd == 1) {
                    eventDate = getEventDate(var1, eventDate);
                    gotOffset = true;
//					continue;
                }
                if (i == durationPos) {
                    if (endTimePos < 0){ //Event End Time not given in file so calculate using start date + duration
//						System.out.println("DEL about to calculate end date with var1:" + var1 + " durationformat:" + durationFormat + " eventDate:" + eventDate.toString());
                        endDate = calcEndDate(var1, durationFormat, eventDate);
                    }
                    gotDuration = true;
//					calcBucket = true;
//					continue;
                }
                if (eventDate != null){
                    gotEventDate = true;
                }
                if (gotEventDate && gotDuration && gotOffset) {
//					if (calcBucket) {
//					calcBucket = false;
                    totEvnt++;
                    ageMilSec = 0;
                    ageMilSec = dtDate.getTime() - endDate.getTime();
//					System.out.println("dtDate:" + dtDate.toString() + " endDate:" + endDate.toString());
//					ageMilSec = dtDate.getTime() - eventDate.getTime();
//					System.out.println("DEL ageMilSec before:" + ageMilSec);
                    calcBucketSuccess = calcBucket(ageMilSec, line1);
//					System.out.println("DEL ageMilSec after.:" + ageMilSec);
                    eventDate = null;
                    break; //done with calculation so may as well read next record
                }
                continue;
            }
        }
//		********************************************************************************************
//		Delimited file logic ends here...
//		********************************************************************************************

//		********************************************************************************************
//		Positional file logic starts here...
//		********************************************************************************************
        if (fileType.equals("POS")){
            calcBucket = false;
//			for (i = 0; i < values.length; i++) {
//			var1 = values[i].trim();
//			Determining record types and exclusions start here - the flexibility is a little OTT to be honest
            if (!parsedAlready && excludePositionalLineByType()){
                return;
            }
//			eventType = line1.substring(eventTypePos - 1, eventTypeLen);
//			Now that we've excluded a million lines, we can finally start working
//			Some weird lookup table logic to change certain fields in the file - used for NRTDE at the moment
            if (nrtdeInd != 2 && lookupPos >= 0) {
                var1 = lookupTable(line1.substring(lookupPos, (lookupPos + lookupLen))); //TODO Lookup call properly
                line1.replace(line1.substring(lookupPos, (lookupPos + lookupLen)), var1);
            }

            //Check NETWORK code change for type 2 files
//			System.out.println("About to check field 1 " + var1);
            if (i == 1 && nrtdeInd == 2) {
                if (line1.trim().equalsIgnoreCase("")){
                    return;
                }
                networkCode = line1.substring(networkPos - 1, networkLen);
                networkName = lookupTable(currentNetworkCode); //TODO Lookup call properly
//				System.out.println("networkCode: " + networkCode + " currentNetworkCode: " + currentNetworkCode);
                if (!networkCode.trim().equalsIgnoreCase(currentNetworkCode.trim()) && currentNetworkCode.trim() != "") {
                    //*********************************************************************************
                    // Store previous totals for NETWORK  *********************************************
                    //*********************************************************************************
                    dbSuccess = storeAgeAnalysisRecord();
                    if (!dbSuccess){
                        //TODO Proper Validation
                    }
                    //Reset variables to start next NETWORK's totals
                    networkName = "";
                    error = "";
                    totEvnt = 0;
                    longEvnt = 0;
                    shortEvnt = 0;
                    b1Tot = 0;
                    b2Tot = 0;
                    b3Tot = 0;
                    b4Tot = 0;
                    b5Tot = 0;
                    b6Tot = 0;
                    b7Tot = 0;
                    b8Tot = 0;
                    totSec = 0;
                }
                currentNetworkCode = networkCode;
            }
            //*********************************************************************************
            // End Of Store previous totals for NETWORK  **************************************
            //*********************************************************************************
            if (startTimePos >= 0){
                startDate = getStartDate(line1.substring(startTimePos, (startTimePos + startTimeLen)), startTimeFormat);
            }
            if (endTimePos >= 0){
                endDate = getEndDate(line1.substring(endTimePos, (endTimePos + endTimeLen)), endTimeFormat);
            }

            if (endTimePos >= 0){ //Event End Time given in file
                eventDate = endDate;
            } else { //Only start date provided so end date is calculated as start date + duration
                eventDate = startDate;
            }

            if (offsetPos >= 0){
                eventDate = getEventDate(line1.substring(offsetPos, (offsetPos + offsetLen)), eventDate);
            }
            if (endTimePos < 0){ //Event End Time not given in file so calculate using start date + duration
//				System.out.println("DEL about to calculate end date with duration:" + line1.substring(durationPos, (durationPos + durationLen)) + " durationformat:" + durationFormat + " eventDate:" + eventDate.toString());
                endDate = calcEndDate(line1.substring(durationPos, (durationPos + durationLen)), durationFormat, eventDate);
            }
            totEvnt++;
            ageMilSec = 0;
            ageMilSec = dtDate.getTime() - endDate.getTime();
//			System.out.println("POS ageMilSec before:" + ageMilSec);
            calcBucketSuccess = calcBucket(ageMilSec, line1);
//			System.out.println("POS ageMilSec after.:" + ageMilSec);
            eventDate = null;
//			}
        }

//		********************************************************************************************
//		Positional file logic ends here...
//		********************************************************************************************
    }

    @Override
    public AgeAnalysisConfigEntity findAgeAnalysisConfigEntitiesByRoute_code(String routeCode) throws AgeAnalysisRouteCodeNotFoundException {
        Optional <AgeAnalysisConfigEntity> ageAnalysisConfigEntity = ageAnalysisConfigRepository.findAgeAnalysisConfigEntitiesByRouteCode(routeCode);
        long masterAgeUID = 0;
        if(ageAnalysisConfigEntity.isPresent()){
            logger.info("VODACOM Config Pool.... AGE_ANALYSIS_CONFIG exists:" + routeCode+ "\n >>> Age Analysis Processing About to start ......");
            return ageAnalysisConfigEntity.get();
        }else{
            throw new AgeAnalysisRouteCodeNotFoundException(">>> Route Code Does not Exist For:-  "+ routeCode + " <<<");
        }
    }

    @Override
    public ProcessedFileEntity findProcessedFileEntityByArrivedfilekey(String arrivedFileKey) {
        if (!arrivedFileKey.isEmpty()) {
            Optional<ProcessedFileEntity> processed_file_entity = processed_file_entity_repository.findProcessedFileEntityByArrivedFileKey(arrivedFileKey);
            if (processed_file_entity.isPresent()) {
                logger.info(": Processed_File Entity value Found for Arrived Key Value :- + " + arrivedFileKey);
                return processed_file_entity.get();
            } else {
                throw new ProcessedFileEntityNotFoundException(">>> Arrived Key Value Does not Exist For:-  " + arrivedFileKey + " <<<");
            }
        }else {
            throw new ProcessedFileEntityNotFoundException(">>> Value Not working:-  " + arrivedFileKey + " <<<");
        }
    }

    @Override
    public AgeAnalysisIgnoreEntity findAgeAnalysisIgnoreEntityByAge_uid(long ageUid) {
        Optional<AgeAnalysisIgnoreEntity> ageAnalysis_ignore_entity = ageAnalysis_ignore_entity_repository.findAgeAnalysisIgnoreEntityByAgeUid(ageUid);
        if(ageAnalysis_ignore_entity.isPresent()){
            logger.info(":: AgeAnalysis_Ignore Entities Found for Age_Uid :- "+ ageUid );
            return ageAnalysis_ignore_entity.get();
        }else {
            throw new AgeAnalysisIgnoreEntityNotFoundException(">>> SANGOMA: AgeAnalysis_Ignore Entities Does Not Exist For for Age_Uid :-  "+ ageUid + " <<<");
        }
    }

    @Override
    public List<AgeAnalysisIgnoreEntity> findAgeAnalysisIgnoreEntitiesByAge_uid(long ageUid) {
        List<AgeAnalysisIgnoreEntity> ageAnalysis_ignore_entityList = ageAnalysis_ignore_entity_repository.findAgeAnalysisIgnoreEntitiesByAgeUid(ageUid);
        if(ageAnalysis_ignore_entityList.size() > 0){
            return ageAnalysis_ignore_entityList;
        }else {
            new ArrayList<AgeAnalysisIgnoreEntity>();
        }
        return null;
    }

    private boolean insertIntoAgeAnalysisEntity(long medianEvntTime, String country ){
        AgeAnalysisEntity age_analysis_entity =new AgeAnalysisEntity();
        age_analysis_entity.setDeliveredFileKey(deliveredFileKey);
        age_analysis_entity.setEventAgeType(nrtdeI);
        age_analysis_entity.setTotalEvents(totEvnt);
        age_analysis_entity.setLongestDeliveryAge(longEvnt);
        age_analysis_entity.setMedianDeliveryAge(medianEvntTime);
        age_analysis_entity.setShortestDeliveryAge(shortEvnt);

        age_analysis_entity.setBucket1(b1Tot);
        age_analysis_entity.setBucket2(b2Tot);
        age_analysis_entity.setBucket3(b3Tot);
        age_analysis_entity.setBucket4(b4Tot);
        age_analysis_entity.setBucket5(b5Tot);
        age_analysis_entity.setBucket6(b6Tot);
        age_analysis_entity.setBucket7(b7Tot);
        age_analysis_entity.setBucket8(b8Tot);
        age_analysis_entity.setMediationTime(medianFileTime);
        age_analysis_entity.setSfgWfId(workflowId);
        age_analysis_entity.setAgeANetwork(networkName);
        age_analysis_entity.setAgeACountry(country);
        age_analysis_entity.setAgeAError(error);
        age_analysis_entity.setLogicalConsumerName(logicalConsumerName);
        age_analysis_entity.setFileName(allFileNames[3]);
        age_analysis_entity.setEventType(eventType);

        AgeAnalysisEntity age_analysis_entity_tobe_saved = ageAnalysisEntityRepository.save(age_analysis_entity);

        return age_analysis_entity_tobe_saved != null;
    }
}
