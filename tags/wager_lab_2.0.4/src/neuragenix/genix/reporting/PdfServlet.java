/*

 * ============================================================================

 *                   The JasperReports License, Version 1.0

 * ============================================================================

 * 

 * Copyright (C) 2001-2002 Teodor Danciu (teodord@hotmail.com). All rights reserved.

 * 

 * Redistribution and use in source and binary forms, with or without modification,

 * are permitted provided that the following conditions are met:

 * 

 * 1. Redistributions of source code must retain the above copyright notice,

 *    this list of conditions and the following disclaimer.

 * 

 * 2. Redistributions in binary form must reproduce the above copyright notice,

 *    this list of conditions and the following disclaimer in the documentation

 *    and/or other materials provided with the distribution.

 * 

 * 3. The end-user documentation included with the redistribution, if any, must

 *    include the following acknowledgment: "This product includes software

 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."

 *    Alternately, this acknowledgment may appear in the software itself, if

 *    and wherever such third-party acknowledgments normally appear.

 * 

 * 4. The name "JasperReports" must not be used to endorse or promote products 

 *    derived from this software without prior written permission. For written 

 *    permission, please contact teodord@hotmail.com.

 * 

 * 5. Products derived from this software may not be called "JasperReports", nor 

 *    may "JasperReports" appear in their name, without prior written permission

 *    of Teodor Danciu.

 * 

 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,

 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND

 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE

 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,

 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-

 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS

 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON

 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT

 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF

 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */



/*

 * ============================================================================

 *                   GNU Lesser General Public License

 * ============================================================================

 *

 * JasperReports - Free Java report-generating library.

 * Copyright (C) 2001-2002 Teodor Danciu teodord@hotmail.com

 * 

 * This library is free software; you can redistribute it and/or

 * modify it under the terms of the GNU Lesser General Public

 * License as published by the Free Software Foundation; either

 * version 2.1 of the License, or (at your option) any later version.

 * 

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU

 * Lesser General Public License for more details.

 * 

 * You should have received a copy of the GNU Lesser General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.

 * 

 * Teodor Danciu

 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18

 * Postal code 741181, Sector 3

 * Bucharest, ROMANIA

 * Email: teodord@hotmail.com

 */

package neuragenix.genix.reporting;



import net.sf.jasperreports.engine.*;



import java.io.*;

import java.util.*;

import javax.servlet.*;

import javax.servlet.http.*;

import java.sql.*;



/**

 *

 */

public class PdfServlet extends HttpServlet

{

   

   private static boolean bPropsLoaded = false;

   private static String sJdbcDriver;

   private static String sJdbcUrl;

   private static String sJdbcUser;

   private static String sJdbcPassword;







	/**

	 *

	 */

	public void doGet(

		HttpServletRequest request,

		HttpServletResponse response

		) throws IOException, ServletException

	{

		System.setProperty("java.awt.headless", "true");

		ServletContext context = this.getServletConfig().getServletContext();



		File reportFile = new File(context.getRealPath("/reporting/" + request.getPathInfo() + ".jasper" ));

	

		Map parameters = new HashMap();

		//parameters.put("ReportTitle", "Patients");

					

		byte[] bytes = null;



		try

		{

			bytes = 

				JasperRunManager.runReportToPdf(

					reportFile.getPath(), 

					parameters, 

					getConnection()

					);

		}

		catch (JRException e)

		{

			response.setContentType("text/html");

			PrintWriter out = response.getWriter();

			out.println("<html>");

			out.println("<head>");

			out.println("<title>JasperReports - Web Application Sample</title>");

			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");

			out.println("</head>");

			

			out.println("<body bgcolor=\"white\">");



			out.println("<span class=\"bnew\">JasperReports encountered this error :</span>");

			out.println("<pre>");



			e.printStackTrace(out);



			out.println("</pre>");



			out.println("</body>");

			out.println("</html>");



			return;

		}

		catch(Exception e)

		{

			response.setContentType("text/html");

			PrintWriter out = response.getWriter();

			out.println("<html>");

			out.println("<head>");

			out.println("<title>JasperReports - Web Application Sample</title>");

			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");

			out.println("</head>");

			

			out.println("<body bgcolor=\"white\">");



			out.println("<span class=\"bnew\">JasperReports encountered this error :</span>");

			out.println("<pre>");



			e.printStackTrace(out);



			out.println("</pre>");



			out.println("</body>");

			out.println("</html>");



			return;

			

		}



		if (bytes != null && bytes.length > 0)

		{

			response.setContentType("application/pdf");

			response.setContentLength(bytes.length);

			ServletOutputStream ouputStream = response.getOutputStream();

			ouputStream.write(bytes, 0, bytes.length);

			ouputStream.flush();

			ouputStream.close();

		}

		else

		{

			response.setContentType("text/html");

			PrintWriter out = response.getWriter();

			out.println("<html>");

			out.println("<head>");

			out.println("<title>JasperReports - Web Application Sample</title>");

			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");

			out.println("</head>");

			

			out.println("<body bgcolor=\"white\">");

	

			out.println("<span class=\"bold\">Empty response.</span>");

	

			out.println("</body>");

			out.println("</html>");

		}

	}









  /**

   * Loads the JDBC properties from rdbm.properties file.

   */

  private static void loadProps () throws Exception {

      if (!bPropsLoaded) {

        InputStream inStream = PdfServlet.class.getResourceAsStream("/properties/rdbm.properties");

        Properties jdbcProps = new Properties();

        jdbcProps.load(inStream);

        sJdbcDriver = jdbcProps.getProperty("jdbcDriver");

        sJdbcUrl = jdbcProps.getProperty("jdbcUrl");

        sJdbcUser = jdbcProps.getProperty("jdbcUser");

        sJdbcPassword = jdbcProps.getProperty("jdbcPassword");

        bPropsLoaded = true;

      }

  }



  /**

   * Gets a database connection

   * @return a database Connection object

   */

  private static Connection getConnection () throws ClassNotFoundException, SQLException, Exception

  {

    loadProps();

    Connection conn = null;

    Class.forName(sJdbcDriver).newInstance();

    conn = DriverManager.getConnection(sJdbcUrl, sJdbcUser, sJdbcPassword);

    return  conn;

  }







}

