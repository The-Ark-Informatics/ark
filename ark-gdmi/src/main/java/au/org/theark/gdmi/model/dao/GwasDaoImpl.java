package au.org.theark.gdmi.model.dao;

//import java.util.List;
import java.util.*;
import java.io.*;
import java.sql.Blob;
import java.text.SimpleDateFormat;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.lob.BlobImpl;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.gdmi.model.entity.MetaData;
import au.org.theark.gdmi.model.entity.MetaDataField;
import au.org.theark.gdmi.model.entity.MetaDataType;
//import au.org.theark.gdmi.model.entity.Person;
import au.org.theark.gdmi.model.entity.Collection;
import au.org.theark.gdmi.model.entity.DecodeMask;
import au.org.theark.gdmi.model.entity.EncodedData;
import au.org.theark.gdmi.model.entity.Marker;
import au.org.theark.gdmi.model.entity.MarkerGroup;
import au.org.theark.gdmi.model.entity.MarkerType;
import au.org.theark.gdmi.model.entity.Status;
//TODO: Use logger (see Study)

@Repository("gwasDao")
public class GwasDaoImpl extends HibernateSessionDao implements IGwasDao
{

    public void create(MetaData metaData)
    {
        System.out.println("Create method invoked");
        // getSession().save(metaData);
        MetaDataType mdt = getMetaDataType("Number");
        System.out.println("Tried to get MetaDataType(\"Number\"): " + mdt);
        Date dateNow = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String nowStr = sdf.format(dateNow);

        MetaDataField mdf = new MetaDataField();
        mdf.setName("Mass");
        mdf.setDescription("kg");
        System.out.println("Now: " + nowStr);
        mdf.setStudyId(new Long(1));
        mdf.setUserId("elam");
        mdf.setInsertTime(dateNow);
        mdf.setMetaDataType(mdt);
        
        metaData.setUserId("elam");
        metaData.setInsertTime(dateNow);
        //TODO FIX STATUS AND COLLECTION
//        Status stat = ;
//        Collection colEn = new Collection();
//        colEn.setStudyId(100);
//        colEn.setStatus()
        metaData.setMetaDataField(mdf);
        System.out.println("metaData.setMetaDataField: "+ mdf);
//        Session s = getSession();
//        s.save(metaData);
//        System.out.println("Tried to create a MetaDataField(\"Number\", \"Mass\", \"kg\"): " + mdf);

//        Person p = new Person();
//        p.setFirstName("A");
//        p.setLastName("Name");
//        p.setMiddleName("M");
//        getSession().save(p);
//        s.delete(metaData);
//        System.out.println("Tried to delete the newly created MetaDataField(\"Number\", \"Mass\", \"kg\"): " + mdf);
    }

    public void createMetaData(MetaDataField mdf, String value)
    {
        System.out.println("Invoked createMetaData...");
        MetaData metaData = new MetaData();
        metaData.setMetaDataField(mdf);
        metaData.setValue(value);
        getSession().save(metaData);
        System.out.println("Created new MetaData");
    }

    public void createMetaDataField(String dataType, String name,
            String description)
    {
        System.out.println("Invoked createMetaDataField...");
        MetaDataField mdf = new MetaDataField();
        MetaDataType mdt = getMetaDataType(dataType);
        mdf.setMetaDataType(mdt);
        mdf.setName(name);
        mdf.setDescription(description);
        getSession().save(mdf);
        System.out.println("Created new MetaDataField");
    }

    public MetaDataField getMetaDataField(String name)
    {
        String hql = "from MetaDataField where name = :name";
        Query q = getSession().createQuery(hql);
        q.setString("name", name);
        q.setMaxResults(1);
        @SuppressWarnings("unchecked")
        List<MetaDataField> results = q.list();
        if (results.size() > 0)
        {
            return (results.get(0));
        }
        else
            return null;
    }

    public MetaDataType getMetaDataType(String dataType)
    {
//        String hql = "from MetaDataType where name=:dataType";
//        Query q = getSession().createQuery(hql);
        MetaDataType mdt = new MetaDataType();
        mdt.setName("Number");
        Example ex = Example.create(mdt);
        Criteria c = getSession().createCriteria(MetaDataType.class).add(ex);
        //@SuppressWarnings("unchecked")
        List<MetaDataType> mdtList = c.list();
        if (mdtList != null && mdtList.size() > 0)
        {
            mdt = mdtList.get(0);
        }
        return mdt;
//        q.setString("dataType", dataType);
//        q.setMaxResults(1);
//        @SuppressWarnings("unchecked")
//        List<MetaDataType> results = q.list();
//        if (results.size() > 0)
//        {
//            return (results.get(0));
//        }
//        else
//            return null;
    }
    
    public void createEncodedData(EncodedData ed)
    {
    	Status s = new Status();
    	s.setName("OK");
    	
    	Collection col = new Collection();
    	col.setStudyId(0);
    	col.setStatus(s);
    	col.setUserId("test");
    	col.setInsertTime(new Date().toString());
    	
    	MarkerType mkType = new MarkerType();
    	mkType.setName("SNP");
    	
    	MarkerGroup mrkGrp = new MarkerGroup();
    	mrkGrp.setStudyId(0);
    	mrkGrp.setUploadId(0);
    	mrkGrp.setName("pretend dbSNP");
    	mrkGrp.setUserId("test");
    	mrkGrp.setInsertTime(new Date().toString());

    	Marker mrk1 = new Marker();
    	mrk1.setName("BRCA1");
    	mrk1.setMarkerGroup(mrkGrp);
    	mrk1.setChromosome("1");
    	mrk1.setUserId("test");
    	mrk1.setMajorAllele("G");
    	mrk1.setMinorAllele("A");
    	mrk1.setInsertTime(new Date().toString());

    	Marker mrk2 = new Marker();
    	mrk2.setName("BRCA2");
    	mrk2.setMarkerGroup(mrkGrp);
    	mrk2.setChromosome("1");
    	mrk2.setUserId("test");
    	mrk2.setMajorAllele("T");
    	mrk2.setMinorAllele("C");
    	mrk2.setInsertTime(new Date().toString());
    	
    	DecodeMask dm1 = new DecodeMask();
    	dm1.setBitPosition(0);
    	dm1.setMarker(mrk1);
    	dm1.setCollection(col);
    	
    	DecodeMask dm2 = new DecodeMask();
    	dm2.setBitPosition(1);
    	dm2.setMarker(mrk2);
    	dm2.setCollection(col);
    	
    	Vector<Marker> mask = new Vector<Marker>();
    	mask.add(mrk1);
    	mask.add(mrk2);
    	Vector<String> genotypeVec = new Vector<String>();
    	genotypeVec.add("AA");
    	genotypeVec.add("CT");

    	try {
    		BitSet[] encodedBitSets = createBitSet(genotypeVec, mask);
    	} catch (Exception ex) {
    		System.out.println("Internal code error: " + ex);
    	}

    	ed.setSubjectId(0);
    	ed.setCollection(col);
    	try
    	{
	    	InputStream in = new FileInputStream("/home/elam/w2003R2Entx32_disk1.iso");
        	Blob blobbo = Hibernate.createBlob(in);
	    	//ed.setEncodedBit1(blobbo);
	    	//ed.setEncodedBit2(blobbo);
	    	in.close();
    	} catch (Exception ex) {
    		System.out.println("Something went horribly wrong with the file storage...\n" + ex);
		}
    	getSession().save(ed);
    }
    
	public static BitSet[] createBitSet(Vector<String> genotypeVec, Vector<Marker> mask) throws Exception {
		BitSet[] b = new BitSet[3];
		b[0] = new BitSet(); // MSB
		b[1] = new BitSet();
		b[2] = new BitSet();
		
		for (int i = 1; i < 20; i++) {
			//BreakIterator bi = BreakIterator.getWordInstance();
			//bi.setText(genotype);
			int index = 0;
			//bi.next();
			// System.out.println(genotype);
			// System.out.println("Current index all1 : "+index+" " +
			// bi.current());
			try {
				String allele1 = genotypeVec.get(i).substring(0, 1);
				String allele2 = genotypeVec.get(i).substring(2, 3);
				String genotype = allele1+allele2;
				
				if (!genotype.contains(mask.get(i-1).getMajorAllele())
					&& !genotype.contains(mask.get(i-1).getMinorAllele()))
				{
					// genotype doesn't have the correct alleles for the marker
					throw new Exception("Incorrect genotype for the marker - \n" 
										+ "Valid alleles are: " + mask.get(i-1).getMajorAllele() 
										+ mask.get(i-1).getMinorAllele() + "\n"
										+ "Incorrect genotype input: " + allele1 + allele2);
				}
				if (allele1.equals(allele2)) {
					if (allele1.equals(mask.get(i-1).getMajorAllele())) {
						//do nothing					
					}
					else {
						b[1].set(i-1);
					}
				} else {
					b[2].set(i-1);
				}
			} catch (IndexOutOfBoundsException ie) {
				b[0].set(i-1);
				
			}
		}
		//b[0] = new BitSet(b[2].size());
		System.out.println(b[1]);
		return b;
	}
}
