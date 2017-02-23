package au.org.theark.core.web.component.settings;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkCheckSumNotSameException;
import au.org.theark.core.exception.ArkFileNotFoundException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.model.config.entity.SettingFile;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IArkSettingService;
import au.org.theark.core.util.EventPayload;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.button.AjaxDeleteButton;
import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import org.apache.commons.io.IOUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import java.io.*;

/**
 * Created by george on 16/2/17.
 */
public class SettingFilePanel extends Panel {

    @SpringBean(name = Constants.ARK_COMMON_SERVICE)
    private IArkCommonService iArkCommonService;

    @SpringBean(name = Constants.ARK_SETTING_SERVICE)
    private IArkSettingService iArkSettingService;

    private FileUploadField fileUploadField;
    private Label valueLabel;
    private Model<String> labelModel;
    private IModel<Setting> settingModel;
    private SettingFile settingFile;
    private AjaxButton downloadButton;
    private AjaxDeleteButton deleteButton;
    private WebMarkupContainer detailsContainer;

    public SettingFilePanel(String id, IModel<Setting> model) {
        super(id, model);
        this.setOutputMarkupId(true);
        this.settingModel = model;

        fileUploadField = new FileUploadField("propertyValue");
        fileUploadField.add(new ArkDefaultFormFocusBehavior());
        add(fileUploadField);

        detailsContainer = new WebMarkupContainer("detailsContainer");
        detailsContainer.setOutputMarkupId(true);

        labelModel = Model.of("");
        valueLabel = new Label("valueLabel", labelModel);
        detailsContainer.add(valueLabel);

        downloadButton = buildDownloadButton();
        deleteButton = buildDeleteButton(this);
        detailsContainer.add(downloadButton);
        detailsContainer.add(deleteButton);

        add(detailsContainer);
    }

    @Override
    protected void onBeforeRender() {
        if (settingModel.getObject().getPropertyValue() != null && !settingModel.getObject().getPropertyValue().isEmpty()) {
            settingFile = iArkSettingService.getSettingFileByFileId(settingModel.getObject().getPropertyValue());
            labelModel.setObject(settingFile.getFilename());

            detailsContainer.setVisible(true);
        } else {
            detailsContainer.setVisible(false);
        }
        super.onBeforeRender();
    }

    public String onSave() throws ArkSystemException {
        String fileId = null;
        FileUpload file = fileUploadField.getFileUpload();
        if (file != null) {
            String filename = file.getClientFileName();
            byte[] byteArray = file.getMD5();
            String checksum = getHex(byteArray);
            try {
                byte[] payload = IOUtils.toByteArray(file.getInputStream());
                fileId = iArkCommonService.generateArkFileId(filename);

                SettingFile sf = new SettingFile();
                sf.setChecksum(checksum);
                sf.setFileId(fileId);
                sf.setFilename(filename);

                iArkCommonService.saveArkFileAttachment(null, null, Constants.ARK_SETTINGS_DIR, filename, payload, fileId);
                iArkSettingService.createSettingFile(sf);
                settingFile = sf;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new ArkSystemException("Null file");
        }
        return fileId;
    }

    private static final String HEXES = "0123456789ABCDEF";

    private static String getHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    private AjaxButton buildDownloadButton() {
        AjaxButton ajaxButton = new AjaxButton("downloadFile") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                byte[] data = null;
                try {
                    String fileId = settingFile.getFileId();
                    String checksum = settingFile.getChecksum();
                    data = iArkCommonService.retriveArkFileAttachmentByteArray(null, null, Constants.ARK_SETTINGS_DIR, fileId, checksum);
                    if (data != null) {
                        InputStream inputStream = new ByteArrayInputStream(data);
                        OutputStream outputStream;

                        final String tempDir = System.getProperty("java.io.tmpdir");
                        final java.io.File file = new File(tempDir, settingFile.getFilename());
                        final String fileName = settingFile.getFilename();
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
                } catch (ArkSystemException e) {
                    this.error("Unexpected error: Download request could not be fulfilled.");
                    System.out.println("ArkSystemException" + e.getMessage());
                } catch (IOException e) {
                    this.error("Unexpected error: Download request could not be fulfilled.");
                    System.out.println("IOException" + e.getMessage());
                } catch (ArkFileNotFoundException e) {
                    this.error("Unexpected error: Download request could not be fulfilled.");
                    System.out.println("FileNotFoundException" + e.getMessage());
                } catch (ArkCheckSumNotSameException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                target.add(detailsContainer);
                target.add(this);
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                this.error("Unexpected error: Download request could not be fulfilled.");
                System.out.println("Unexpected error: Download request could not be fulfilled.");
            }
        };

        ajaxButton.setDefaultFormProcessing(false);
        return ajaxButton;
    }

    private AjaxDeleteButton buildDeleteButton(Panel panel) {

        AjaxDeleteButton ajaxButton = new AjaxDeleteButton("deleteButton", new StringResourceModel("confirmDelete", this, null), new StringResourceModel(Constants.DELETE, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (settingFile.getId() != null) {
                    try {
                        iArkSettingService.delete(settingFile, Constants.ARK_SETTINGS_DIR);
                        settingModel.getObject().setPropertyValue("");
                        System.out.println(settingModel.getObject());
                        iArkSettingService.saveSetting(settingModel.getObject());

                        this.info("Attachment " + settingFile.getFilename() + " was deleted successfully.");
                    } catch (ArkSystemException e) {
                        this.error("Unexpected error: Delete request could not be fulfilled.");
                        System.out.println("ArkSystemException" + e.getMessage());
                    } catch (ArkFileNotFoundException e) {
                        this.error("File not found:" + e.getMessage());
                    }
                }
                this.send(getWebPage(), Broadcast.DEPTH, new EventPayload(Constants.EVENT_RELOAD_LOGO_IMAGES, target));
                target.add(panel);
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                this.error("Unexpected error: Delete request could not be fulfilled.");
                System.out.println("Unexpected error: Delete request could not be fulfilled.");
            }
        };
        ajaxButton.setDefaultFormProcessing(false);
        return ajaxButton;
    }
}
