package au.org.theark.web.rest.service;

import static au.org.theark.study.web.Constants.MADELINE_PEDIGREE_TEMPLATE;
import static au.org.theark.study.web.Constants.PEDIGREE_TEMPLATE_EXT;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.wicket.util.file.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.x5.template.Chunk;
import com.x5.template.Theme;

import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.jni.ArkMadelineProxy;
import au.org.theark.core.model.study.entity.LinkSubjectPedigree;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyPedigreeConfiguration;
import au.org.theark.core.model.study.entity.TwinType;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.model.capsule.RelativeCapsule;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.util.PedigreeUploadValidator;

@Service("pedigreeService")
@Transactional
public class PedigreeWebServiceRestImpl implements IPedigreeWebServiceRest {
	
	private IStudyService iStudyService;

	public IStudyService getiStudyService() {
		return iStudyService;
	}
	@Autowired
	public void setiStudyService(IStudyService iStudyService) {
		this.iStudyService = iStudyService;
	}
	@Override
	public Boolean createSubject(SubjectVO subjectVO) {
		try {
				iStudyService.createSubject(subjectVO);
				return true;
		} catch (ArkUniqueException | ArkSubjectInsertException e) {
			e.printStackTrace();
			return false;
			
		}
	}
	@Override
	public Boolean isSubjectUIDExist(Study study,String subjectUid) {
		return iStudyService.isSubjectUIDUnique( study,subjectUid,"insert");
	}
	@Override
	public Boolean isStudyExist(Study study) {
		return (iStudyService.getStudy(study.getId())!=null);
	}
	@Override
	public Boolean generatePedigree(SubjectVO subjectVO) {
	List<RelationshipVo>	relationshipVos=iStudyService.generateSubjectPedigreeRelativeList(subjectVO.getLinkSubjectStudy().getSubjectUID(), subjectVO.getLinkSubjectStudy().getStudy().getId());
	return (relationshipVos.size()>0);
	}
	@Override
	public void createRelationShip(SubjectVO subjectVO, SubjectVO relativeVO,Relationship relationship) {
		LinkSubjectPedigree linkSubjectPedigree =new LinkSubjectPedigree();
		linkSubjectPedigree.setSubject(subjectVO.getLinkSubjectStudy());
		linkSubjectPedigree.setRelative(relativeVO.getLinkSubjectStudy());
		linkSubjectPedigree.setRelationship(relationship);
		iStudyService.create(linkSubjectPedigree);
		
	}
	@Override
	public LinkSubjectStudy getLinkSubjectStudyBySubjectUidAndStudy(String subjectUid, Study study) {
			return iStudyService.getLinkSubjectStudyBySubjectUidAndStudy(subjectUid, study);
	}
	@Override
	public Study getStudyByID(Long id) {
		return iStudyService.getStudy(id);
	}
	@Override
	public Boolean createTwin(SubjectVO subjectVO, SubjectVO relativeVO, TwinType twinType) {
		Long studyId=subjectVO.getLinkSubjectStudy().getStudy().getId();
		String subjectUid=subjectVO.getLinkSubjectStudy().getSubjectUID();
		String relativeSubjectUid=relativeVO.getLinkSubjectStudy().getSubjectUID();
		Boolean isSetRelativeASiblings=false;
		//Check for the siblings are currently exists before assign as twins.
		List<RelationshipVo> relationshipVos=iStudyService.getSubjectPedigreeTwinList(subjectUid, studyId);
		
		for (RelationshipVo relationshipVo : relationshipVos) {
			if(relationshipVo.getIndividualId().equals(relativeSubjectUid)){
				isSetRelativeASiblings=true;
				break;
			}
		}
		if(isSetRelativeASiblings){
			RelationshipVo relationshipVo=new RelationshipVo();
			if(twinType.getName().equals("MZ")){
				relationshipVo.setMz("MZ");
			}else if(twinType.getName().equals("DZ")){
				relationshipVo.setDz("DZ");
			}
			relationshipVo.setIndividualId(relativeSubjectUid);
			iStudyService.processPedigreeTwinRelationship(relationshipVo,subjectUid,studyId);
			return true;
		}else{
			return false;
		}
		
		
	}
	@Override
	public Boolean isCircularValidationSuccessful(SubjectVO subjectVO, SubjectVO relativeVO) {
		
				String subjectUID=subjectVO.getLinkSubjectStudy().getSubjectUID();
				String parentUID=relativeVO.getLinkSubjectStudy().getSubjectUID();
				Long sessionStudyId=subjectVO.getLinkSubjectStudy().getStudy().getId();
		
				List<RelationshipVo> relatives = iStudyService.generateSubjectPedigreeRelativeList(subjectUID,sessionStudyId);
				// Circular validation
				StringBuilder pedigree = new StringBuilder();
				ArrayList<String> dummyParents = new ArrayList<String>();
				boolean firstLine = true;

				List<RelationshipVo> existingRelatives = new ArrayList<RelationshipVo>();
				existingRelatives.addAll(relatives);

				RelationshipVo proband = new RelationshipVo();
				proband.setIndividualId(subjectUID);
				for (RelationshipVo relative : existingRelatives) {
					if ("Father".equalsIgnoreCase(relative.getRelationship())) {
						proband.setFatherId(relative.getIndividualId());
					}

					if ("Mother".equalsIgnoreCase(relative.getRelationship())) {
						proband.setMotherId(relative.getIndividualId());
					}
				}

				if (subjectVO.getLinkSubjectStudy().getPerson().getGenderType().getName().startsWith("M")) {
					proband.setFatherId(parentUID);
				}
				else if (subjectVO.getLinkSubjectStudy().getPerson().getGenderType().getName().startsWith("F")) {
					proband.setMotherId(parentUID);
				}
				existingRelatives.add(proband);

				List<RelationshipVo> newRelatives = iStudyService.generateSubjectPedigreeRelativeList(parentUID, sessionStudyId);

				RelationshipVo parent = new RelationshipVo();
				parent.setIndividualId(parentUID);
				for (RelationshipVo relative : newRelatives) {
					if ("Father".equalsIgnoreCase(relative.getRelationship())) {
						parent.setFatherId(relative.getIndividualId());
					}

					if ("Mother".equalsIgnoreCase(relative.getRelationship())) {
						parent.setMotherId(relative.getIndividualId());
					}
				}

				newRelatives.add(parent);

				for (RelationshipVo relative : newRelatives) {
					if (!existingRelatives.contains(relative)) {
						existingRelatives.add(relative);
					}
					else {
						for (RelationshipVo existingRelative : existingRelatives) {
							if (relative.getIndividualId().equals(existingRelative.getIndividualId())) {
								if (existingRelative.getFatherId() == null) {
									existingRelative.setFatherId(relative.getFatherId());
								}
								if (existingRelative.getMotherId() == null) {
									existingRelative.setMotherId(relative.getMotherId());
								}
							}
						}
					}
				}

				for (RelationshipVo relative : existingRelatives) {
					String dummyParent = "D-";
					String father = relative.getFatherId();
					String mother = relative.getMotherId();
					String individual = relative.getIndividualId();

					if (father != null) {
						dummyParent = dummyParent + father;
					}

					if (mother != null) {
						dummyParent = dummyParent + mother;
					}

					if (!"D-".equals(dummyParent) && !dummyParents.contains(dummyParent)) {
						dummyParents.add(dummyParent);
						if (father != null) {
							if (firstLine) {
								pedigree.append(father + " " + dummyParent);
								firstLine = false;
							}
							else {
								pedigree.append("\n" + father + " " + dummyParent);
							}
						}
						if (mother != null) {
							if (firstLine) {
								pedigree.append(mother + " " + dummyParent);
								firstLine = false;
							}
							else {
								pedigree.append("\n" + mother + " " + dummyParent);
							}
						}
						pedigree.append("\n" + dummyParent + " " + individual);
					}
					else if (!"D-".equals(dummyParent)) {
						if (firstLine) {
							pedigree.append(dummyParent + " " + individual);
							firstLine = false;
						}
						else {
							pedigree.append("\n" + dummyParent + " " + individual);
						}
					}
				}
				Set<String> circularUIDs = PedigreeUploadValidator.getCircularUIDs(pedigree);
				return !(circularUIDs.size() > 0);
	}
	@Override
	public File getPedigreeView(String subjectUid, Long studyId) {
		final StringBuffer sb = new StringBuffer();
		sb.setLength(0);

		String familyId = null;
		StringBuffer columnList = new StringBuffer("IndividualId");
		Study study = iStudyService.getStudy(studyId);
		StudyPedigreeConfiguration config = study.getPedigreeConfiguration();

		if (config != null && config.isDobAllowed()) {
			columnList.append(" DOB");
		}

		if (config != null && config.isAgeAllowed()) {
			columnList.append(" Age");
		}

		RelativeCapsule[] relatives = iStudyService.generateSubjectPedigreeImageList(subjectUid, studyId);

		if (relatives.length > 2) {
			StringWriter out = null;
			familyId = relatives[0].getFamilyId();

			try {
				Theme theme = new Theme();
				Chunk chunk = theme.makeChunk(MADELINE_PEDIGREE_TEMPLATE, PEDIGREE_TEMPLATE_EXT);
				chunk.set("relatives", relatives);
				out = new StringWriter();
				chunk.render(out);
			}
			catch (IOException io) {
				io.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			ArkMadelineProxy madeline = new ArkMadelineProxy();
			String madelineOutput = null;

			try {
				madelineOutput = madeline.generatePedigree(out.toString(), columnList.toString());
			}
			catch (Error e) {
				e.printStackTrace();
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
			finally {
				try {
					out.flush();
					out.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Execute madeline by runtime
			// Madeline execute independent of the Ark program
			// Not recommend this option because this may causes unexpected errors. Ex: FileNotFound Exception
			// try{
			//
			// File file = new File("/tmp");
			// Runtime.getRuntime().exec("madeline2 /tmp/cs_009.data",null, file);
			//
			// }catch(IOException ioe){
			// ioe.printStackTrace();
			// }

			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new InputSource(new StringReader(madelineOutput)));

				XPath xpath = XPathFactory.newInstance().newXPath();

				String xPathExpression = "//text";

				NodeList nodes = (NodeList) xpath.evaluate(xPathExpression, doc, XPathConstants.NODESET);

				for (int idx = 0; idx < nodes.getLength(); idx++) {
					String nodeText = nodes.item(idx).getTextContent();
					if (nodeText != null) {
						// Replace family id and dummy subject uids by blank text
						if (nodeText.startsWith("_F") || nodeText.startsWith("!")) {
							nodes.item(idx).setTextContent("");
						}

						// Replace half generated Indiv.. by UID
						if (nodeText.startsWith("Indiv")) {
							nodes.item(idx).setTextContent("UID");
						}

					}
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new StringWriter());
				transformer.transform(source, result);
				sb.append(result.getWriter().toString());
			}
			catch (Exception e) {
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<svg width=\"640\" height=\"480\" xmlns=\"http://www.w3.org/2000/svg\">\r\n <!-- Created with SVG-edit - http://svg-edit.googlecode.com/ -->\r\n <g>\r\n  <title>Layer 1</title>\r\n  <text transform=\"rotate(-0.0100589, 317.008, 97.5)\" xml:space=\"preserve\" text-anchor=\"middle\" font-family=\"serif\" font-size=\"24\" id=\"svg_3\" y=\"106\" x=\"317\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#000000\">Pedigree Error</text>\r\n </g>\r\n</svg>");
				e.printStackTrace();
			}

		}
		else {
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<svg width=\"640\" height=\"480\" xmlns=\"http://www.w3.org/2000/svg\">\r\n <!-- Created with SVG-edit - http://svg-edit.googlecode.com/ -->\r\n <g>\r\n  <title>Layer 1</title>\r\n  <text transform=\"rotate(-0.0100589, 317.008, 97.5)\" xml:space=\"preserve\" text-anchor=\"middle\" font-family=\"serif\" font-size=\"24\" id=\"svg_3\" y=\"106\" x=\"317\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#000000\">No Pedigree History</text>\r\n </g>\r\n</svg>");
		}
		File tempFile = null;
		String tmpDir = System.getProperty("java.io.tmpdir");
		String pedFileName = "Ark_" + subjectUid + ".svg";
		tempFile = new File(tmpDir, pedFileName);					
		InputStream data = new ByteArrayInputStream(sb.toString().getBytes());
		try {
			Files.writeTo(tempFile, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempFile;

	}

}
