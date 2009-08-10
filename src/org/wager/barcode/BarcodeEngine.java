package org.wager.barcode;

import java.io.*;
import java.sql.SQLException;


import neuragenix.security.AuthToken;
public interface BarcodeEngine {


public InputStream getBarcode(String strDomain , int domainKey, AuthToken authtoken) throws SQLException;


}