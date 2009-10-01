/*
 * IBiospecimenIDGenerator.java
 *
 * Copyright (C) Neuragenix Pty Ltd, 2005
 *
 * Description : Interface for the generation of Biospecimen IDs
 *
 */

package neuragenix.bio.utilities;

import java.util.Vector;
import neuragenix.security.AuthToken;
import neuragenix.dao.*;

/**
 *
 * @author  Daniel Murley
 */
public interface IInventoryIDGenerator {
    
    /**
     *  In cases where a prefix is displayed to the user, this should be returned here
     */
    
    public String getInventoryIDPrefix();
    
    /**
     * Provides the Sequence Key and the data associated with the new biospecimen
     */
    
    public String getInventoryID(int intStudyKey,  AuthToken authToken);
    

  
    
    
}
