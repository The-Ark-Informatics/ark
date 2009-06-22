/**
 * 
 */
package neuragenix.bio.biospecimen.BioAnalysisReport;

import org.jasig.portal.ChannelRuntimeData;
import neuragenix.common.NGXRuntimeProperties;

/**
 * @author renny
 *
 */
public interface IDocPreProcessor 
{
	public void setNGXProperties(NGXRuntimeProperties rp);
	public ChannelRuntimeData createRuntimeData(ChannelRuntimeData rd);
	public ChannelRuntimeData processRuntimeData(ChannelRuntimeData rd);
	public ChannelRuntimeData saveComments(ChannelRuntimeData rd);
}
