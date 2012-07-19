/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 	
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.dao;

public class ArkShibbolethServiceProviderContextSource {
	public static String	handlerUrl;
	public static String	metadataGenerator;
	public static String	status;
	public static String	session;
	public static String	discoveryFeed;
	public static Boolean	useShibboleth;

	/**
	 * @return the handlerUrl
	 */
	public String getHandlerUrl() {
		return handlerUrl;
	}

	/**
	 * @param handlerUrl
	 *           the handlerUrl to set
	 */
	public void setHandlerUrl(String handlerUrl) {
		ArkShibbolethServiceProviderContextSource.handlerUrl = handlerUrl;
	}

	/**
	 * @return the metadataGenerator
	 */
	public static String getMetadataGenerator() {
		return metadataGenerator;
	}

	/**
	 * @param metadataGenerator
	 *           the metadataGenerator to set
	 */
	public static void setMetadataGenerator(String metadataGenerator) {
		ArkShibbolethServiceProviderContextSource.metadataGenerator = metadataGenerator;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(String status) {
		ArkShibbolethServiceProviderContextSource.status = status;
	}

	/**
	 * @return the session
	 */
	public String getSession() {
		return session;
	}

	/**
	 * @param session
	 *           the session to set
	 */
	public void setSession(String session) {
		ArkShibbolethServiceProviderContextSource.session = session;
	}

	/**
	 * @return the discoveryFeed
	 */
	public String getDiscoveryFeed() {
		return discoveryFeed;
	}

	/**
	 * @param discoveryFeed
	 *           the discoveryFeed to set
	 */
	public void setDiscoveryFeed(String discoveryFeed) {
		ArkShibbolethServiceProviderContextSource.discoveryFeed = discoveryFeed;
	}

	/**
	 * @return the useShibboleth
	 */
	public static Boolean getUseShibboleth() {
		return useShibboleth;
	}

	/**
	 * @param useShibboleth the useShibboleth to set
	 */
	public static void setUseShibboleth(Boolean useShibboleth) {
		ArkShibbolethServiceProviderContextSource.useShibboleth = useShibboleth;
	}
}
