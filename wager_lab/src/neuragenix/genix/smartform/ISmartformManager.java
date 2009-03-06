/*
 * ISmartformManager.java
 *
 * Created on August 24, 2005, 12:18 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package neuragenix.genix.smartform;

/**
 *
 * @author renny
 */
import java.util.List;
import neuragenix.security.AuthToken;

public interface ISmartformManager 
{
    public ISmartformEntity getSmartformEntity(int intSmartformKey);
    public List getSmartformList(String strdomain);
    public List getSmartformList(String strdomain,String strpartcptkey);
    public List addSmartformEntities(List lstsmrtfrmEntity);
    public boolean saveSmartformResults(List lstsmrtfrmEntity, AuthToken authToken);
    public boolean deleteSmartformResults(ISmartformEntity smrtentity);
    public List getSmartformResults(String strdomain,String strpk);
    public List getSmartformResults(List lstsmrtfrmEntty);
    public ISmartformEntity getSmartformResults(ISmartformEntity smrtfrmEntity);
}
