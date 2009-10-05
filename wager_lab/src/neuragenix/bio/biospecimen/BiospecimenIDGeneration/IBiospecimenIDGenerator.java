/*
 * IBiospecimenIDGenerator.java
 *
 * Copyright (C) Neuragenix Pty Ltd, 2005
 *
 * Description : Interface for the generation of Biospecimen IDs
 *
 */

package neuragenix.bio.biospecimen.BiospecimenIDGeneration;

import java.util.Vector;
import neuragenix.security.AuthToken;

/**
 *
 * @author  Daniel Murley
 */
public interface IBiospecimenIDGenerator {
    
    /**
     *  In cases where a prefix is displayed to the user, this should be returned here
     */
    
    public String getBiospecimenIDPrefix();
    
    /**
     * Provides the Sequence Key and the data associated with the new biospecimen
     */
    
    public String getBiospecimenID(int intSequenceKey, AuthToken authToken);
    
    
    
}
