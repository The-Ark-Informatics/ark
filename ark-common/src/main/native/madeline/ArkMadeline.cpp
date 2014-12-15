#include <iostream>
#include <fstream>
#include <sstream>

#include "ArkMadeline.h"
#include "CLP.h"
#include "Version.h"
#include "Debug.h"
#include "Parser.h"
#include "DataTable.h"
#include "Pedigree.h"
#include "PedigreeSet.h"
#include "Data.h"
#include "DrawingMetrics.h"
#include "utility.h"

//
// Test JNI connection to the Madeline:
//
void connect () {
    std::cout << "Madeline JNI Connected!" << std::endl;
    return;
}

//
// Generate and return the pedigree SVG message
//
std::string generatePedigree (const std::string &pedString, const std::string &columnList){
	std::cout << "Start Generating pedigree!" << std::endl;

	std::string outputSvg;

	try {
		std::vector<std::string> showColumns; // vector containing field labels that need to be displayed on the pedigree
		CLP clp;
		Parser dataTableParser;
		dataTableParser.readMessageString(pedString);
		DataTable *dataTable;
		for( int j=0 ; j < dataTableParser.getNumberOfTables() ; j++ ){

			PedigreeSet pedigreeSet;

			dataTable = dataTableParser.getTable(j);

			// DEBUG:
			//dataTable->display();
			//
			// Tell user the type of table:
			//
			std::cout << "Table " << (j+1) << " is a " << dataTable->getTableTypeAsString() << " table." << std::endl;

			//Set show columns
			showColumns = split(columnList);

			//
			// Draw pedigrees
			//
			if( dataTable->getTableType() == DataTable::PEDIGREE ){

				// Pedigree table
				dataTable->toggleColumnsForPedigree(showColumns);


				std::string sortField = "";

				if(clp.hasSwitchSet("-s")){
					sortField = clp.getSwitchArgument("-s",1);
				}

				pedigreeSet.addPedigreesFromDataTable(dataTable,j,sortField);
				//Return pedigree SVG message
				outputSvg = pedigreeSet.drawPedigreeImg(dataTable);
				std::cout << " Pedigree image is " << outputSvg << std::endl;
			}
		}
	}catch(...){
		std::cout << "Pedigree Generation Error" << std::endl;
	}

	return outputSvg;
}

