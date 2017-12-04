package au.org.theark.genomics.web.component.computation;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import au.org.theark.core.exception.ArkCheckSumNotSameException;
import au.org.theark.core.exception.ArkFileNotFoundException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.genomics.model.vo.ComputationVo;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.computation.form.ContainerForm;

public class SearchResultListPanel extends Panel {
	private static final long	serialVersionUID	= 1L;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
	}

	public PageableListView<Computation> buildPageableListView(IModel iModel) {

		PageableListView<Computation> sitePageableListView = new PageableListView<Computation>("computationList", iModel, iArkCommonService.getRowsPerPage()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Computation> item) {

				Computation computation = item.getModelObject();
				
				if (computation.getId() != null) {
					item.add(new Label(Constants.COMPUTATION_ID, computation.getId().toString()));
				}
				else {
					item.add(new Label(Constants.COMPUTATION_ID, ""));
				}
				
				item.add(buildLink(computation));
				
				if (computation.getMicroService() != null) {
					item.add(new Label(Constants.COMPUTATION_MICROSERVICE, computation.getMicroService().getName()));
				}
				else {
					item.add(new Label(Constants.COMPUTATION_MICROSERVICE, ""));
				}			
				
				if (computation.getStatus()!= null) {
					item.add(new Label(Constants.COMPUTATION_STATUS, computation.getStatus()));
				}
				else {
					item.add(new Label(Constants.COMPUTATION_STATUS, ""));
				}
				
				item.add(buildDownloadButton(computation));
				
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return sitePageableListView;
	}

	@SuppressWarnings( { "serial" })
	private AjaxLink buildLink(final Computation computation) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.COMPUTATION_NAME) {

			@Override
			public void onClick(AjaxRequestTarget target) {

				ComputationVo computationVo = containerForm.getModelObject();
//				computationVo.setMode(Constants.MODE_EDIT);
				computationVo.setComputation(computation);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}
		};
		
		Label nameLinkLabel = new Label("nameLbl", computation.getName());
		link.add(nameLinkLabel);
		return link;
	}
	
	private AjaxButton buildDownloadButton(final Computation computation) {
		AjaxButton ajaxButton = new AjaxButton("downloadPackage") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Attempt to download the Blob as an array of bytes
				byte[] data = null;
				Long studyId =computation.getMicroService().getStudyId();
				String computationId = computation.getId().toString();
				String fileName = computation.getProgramName();
				String fileId = computation.getProgramId();

				String checksum = computation.getChecksum();
				try {
					
					data = iArkCommonService.retriveArkFileAttachmentByteArray(studyId,computationId,"computation",fileId,checksum);

					if (data != null) {
						
						System.out.println("----------------------- SIze of data ----------------------------");
						System.out.println("Size - "+data.length);
						System.out.println("-----------------------------------------------------------------");
						
						InputStream inputStream = new ByteArrayInputStream(data);
						OutputStream outputStream;

						final String tempDir = System.getProperty("java.io.tmpdir");
						final java.io.File file = new File(tempDir, computation.getProgramName());
						outputStream = new FileOutputStream(file);
						IOUtils.copy(inputStream, outputStream);

						IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(file));
						getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream) {
							@Override
							public void respond(IRequestCycle requestCycle) {
								super.respond(requestCycle);
								Files.remove(file);
							}
						}.setFileName(fileName).setContentDisposition(ContentDisposition.ATTACHMENT));
					}
				}
				catch(ArkSystemException e){
					this.error("An unexpected error occurred. Download request could not be fulfilled.");
				}catch (IOException e) {
					this.error("An unexpected error occurred. Download request could not be fulfilled.");
				} catch (ArkFileNotFoundException e) {
					this.error("An unexpected error occurred. Download request could not be fulfilled.");
				} catch (ArkCheckSumNotSameException e) {
					e.printStackTrace();
				}
				
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(containerForm);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("An unexpected error occurred. Download request could not be fulfilled.");
//				log.error("Unexpected error: Download request could not be fulfilled.");
			};
			
			@Override
			public boolean isVisible() {
				boolean visible=true;
				if(computation.getProgramId() == null){
					visible=false;
				}			
				return visible;
			}
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		//if (subjectFile.getPayload() == null)
		//ajaxButton.setVisible(false);

		return ajaxButton;
	}

}
