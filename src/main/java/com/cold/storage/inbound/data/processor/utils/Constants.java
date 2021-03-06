package com.cold.storage.inbound.data.processor.utils;

import org.springframework.stereotype.Component;

@Component
public class Constants {
    public static final String DOT_TRIG_EXT = ".trig";
    public static final String MDB_EXT = "mdb";
    public static final String DOT = ".";
    public static final String MDB_TRIG_EXT = ".mdb.trig";
    public static final String EMPTY_STRING = "";
    public static final String INPROGRESS = "InProgress";

    public static final class Header {
        public static final String CALLERAPP = "CALLERAPP";
        public static final String CALLERAPP_NAME = "inbound-data-processor";
    }

    public static final String MDB = "MDB";
    public static final String SUFFIX = "/";
    public static final String FILLER = ".";



    //File Detail
    public static final String FILE_ID = "FILE_ID";
    public static final String FILE_NAME = "FILE_NAME";
    public static final String COLD_SUBMITTER = "COLD_SUBMITTER";
    public static final String RECEIVED = "RECEIVED";
    public static final String COMEPLETED = "COMEPLETED";
    public static final String STATUS = "STATUS";

    //COLD_INFO
    public static final String COLD_ID = "COLD_ID";
    public static final String COLD_NAME = "COLD_NAME";
    public static final String FACEBOOK_URL = "FACEBOOK_URL";


    //Amad
    public static final String AMADNO = "AMADNO";
    public static final String ENTRY = "ENTRY";
    public static final String DATE = "DATE";
    public static final String PARTY = "PARTY";
    public static final String VILL = "VILL";
    public static final String PACKETS = "PACKETS";
    public static final String PKT3 = "PKT3";
    public static final String KISM = "KISM";
    public static final String YEAR = "YEAR";
    public static final String YR = "YR";
    public static final String MARK1 = "MARK1";
    public static final String MARK = "MARK";
    public static final String AMAD = "Amad";

    //grp
    public static final String GRP = "GRP";
    public static final String DESCRIP = "descrip";
    public static final String ADD1 = "add1";
    public static final String UNDER = "under";
    public static final String OPEN = "open";
    public static final String DR = "dr";
    public static final String CLOSE = "close";
    public static final String BALANCE = "balance";
    public static final String NATURE = "Nature";
    public static final String OPENOTHER = "openOTHER";
    public static final String ACC_NAME = "AccName";

    //PartyAcc
    public static final String PARTY_ACC = "PartyAcc";
    public static final String CODE = "Code";
    public static final String BANK_CODE = "BankCode";
    public static final String BRANCH = "Branch";
    public static final String ACC_NO = "AccNo";
    public static final String IFSC = "IFSC";
    public static final String CHQ1 = "Chq1";
    public static final String CHQ2 = "Chq2";
    public static final String CHQ3 = "Chq3";
    public static final String BANK_NAME = "BankName";
    public static final String WANT_2PRINT = "Want2Print";


}
