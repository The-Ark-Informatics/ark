<%
 // detect if this is a valid uPortal session
        org.jasig.portal.security.IPerson person = null;
        try
        {
            person =
				org.jasig.portal.security.PersonManagerFactory.getPersonManagerInstance().getPerson(request);

            if( !(person != null && person.getSecurityContext().isAuthenticated()))
            {
			 	response.sendRedirect(request.getContextPath() + "/");
            }
        }catch(Exception e){
			 response.sendRedirect(request.getContextPath() + "/");
		}
%>

