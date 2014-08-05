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

using namespace std;

void generatePedigree (const std::string &filename, const std::string &outputFilename, const std::string &columnList){
	cout << "Start Generating pedigree!" << endl;

	try {
		std::vector<std::string> showColumns; // vector containing field labels that need to be displayed on the pedigree
		CLP clp;
		Parser dataTableParser;
		dataTableParser.readFile(filename);
		DrawingMetrics::setDrawingFileNamePrefix(outputFilename);
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
				pedigreeSet.draw(dataTable);
			}
		}
	}catch(...){
		cout << "Pedigree Generation Error" << endl;
	}

	return;
}

