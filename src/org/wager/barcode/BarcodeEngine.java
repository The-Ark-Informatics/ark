package org.wager.barcode;

import java.io.*;
import java.util.*;

import org.jasig.portal.ChannelRuntimeData;

import neuragenix.security.AuthToken;
public interface BarcodeEngine {

public InputStream getBarcode(ChannelRuntimeData params, AuthToken authtoken);

	
}
