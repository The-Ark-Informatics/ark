package au.org.theark.gdmi.web.component.gdmi;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.gdmi.model.entity.EncodedData;
import au.org.theark.gdmi.model.entity.MetaData;
import au.org.theark.gdmi.service.ServiceInterface;

public class TestContainerPanel extends Panel{

	@SpringBean(name = "gwasService")
	private ServiceInterface serviceInterface;

	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(TestContainerPanel.class);

	public TestContainerPanel(String id) {
		super(id);
		log.info("TestContainerPanel Constructor invoked.");
		Form testForm = new Form("testForm");
		testForm.add(new Button(au.org.theark.gdmi.web.Constants.FIRETEST, new StringResourceModel("page.fireTest", this, null))
		{
			public void onSubmit()
			{
				System.out.println("WTF you hit me");
				//EncodedData ed = new EncodedData();
				//serviceInterface.createEncodedData(ed);
				MetaData md = new MetaData();
				serviceInterface.create(md);
			}
		});
		add(testForm);
	}

}
