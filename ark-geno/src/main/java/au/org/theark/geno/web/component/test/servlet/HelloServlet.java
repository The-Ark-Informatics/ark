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
package au.org.theark.geno.web.component.test.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import au.org.theark.geno.service.IGenoService;

/**
 * @author elam
 *
 */

public class HelloServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private IGenoService genoService;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public void doGet (HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		// get an PrintWriter from the response object
		PrintWriter out = response.getWriter();

		out.println("Used doGet -> doPost<br>");
		doPost(request, response);
	}

	public void doPost (HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		// get an PrintWriter from the response object
		PrintWriter out = response.getWriter();
		// prepare the response's content type
		response.setContentType("text/html");
		// get the IP address of the client
		String remoteAddress = request.getRemoteAddr();
		// print to the output stream!
		out.println("Hello there, web surfer from <b>" + remoteAddress + "</b>");
		
//		String firstName = request.getParameter("firstName");
		String firstName = request.getParameter("moduleTabsList:panel:GenoSubMenus:panel:helloForm:firstName");
		out.println("First name was: " + firstName);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		
		Date dateNow = new Date(System.currentTimeMillis());
		File file = new File("/home/ark/TestData/helloServlet.txt");
		PrintWriter fout = new PrintWriter(file);
		System.out.println("Date/Time: " + sdf.format(dateNow));
		fout.println("Date/Time: " + sdf.format(dateNow));
		fout.close();
		genoService.testGWASImport();
	}
	
	
}
