#include "Parser.h"
#include "CodeId.h"
#include <string>


Parser::Parser()
{
}

Parser::Parser(std::vector<std::string> lines) : _lines(lines) {}


Parser::~Parser()
{
}

void Parser::checkForExec() {
	// make sure there are lines
	if (_lines.empty()) return;

	int execStartIndex = -1;
	int execEndIndex = -1;

	// Find the beginning and end of the exec block
	for (int i = 0; i < _lines.size(); i++) {
		if (_isLineMarker(_lines[i])) {
			if (_getMarkerValue(_lines[i]) == codeId::execBegin) execStartIndex = i;
			else if (_getMarkerValue(_lines[i]) == codeId::execEnd) execEndIndex = i;
		}
	}

	// Check the indexes
	if (execStartIndex >= execEndIndex) return;
	if (execStartIndex < 0 || execEndIndex < 0) return;

	// Get the contents of the exec block
	std::vector<std::string> commands;
	for (int i = execStartIndex; i < execEndIndex; i++) {
		if (_getValueTitle(_lines[i]) == codeId::execCommandTitle) commands.push_back(_getValueValue(_lines[i]));
	}

	// Execute the commands if there are any
	if (commands.size() <= 0) return;
	for (int i = 0; i < commands.size(); i++) {
		system(commands[i].c_str());
	}
}

std::string Parser::convertToCSV() {
	// Check if _lines is empty
	if (_lines.empty()) return "";

	std::string CSVData;

	std::vector<Column> columns = _getColumns();
	for (int i = 0; i < columns.size(); i++) {
		CSVData += columns[i].getName() + ";";
	}

	// Get largest number of rows
	int numRows = -1;
	for (int i = 0; i < columns.size(); i++) {
		if (columns[i].getRowCount() > numRows) numRows = columns[i].getRowCount();
	}

	for (int i = 0; i < numRows; i++) {
		CSVData += "\n";
		for (int j = 0; j < columns.size(); j++) {
			if (columns[j].getRowCount() >= numRows) {
				CSVData += columns[j].getRowData(i) + ";";
			}
		}
	}

	return CSVData;
}

std::vector<Column> Parser::_getColumns() {
	std::vector<std::vector<std::string> > blocks = _getBlocks();
	std::vector<Column> columns;

	for (int i = 0; i < blocks.size(); i++) {
		for (int j = 0; j < blocks[i].size(); j++) {
			Column column;
			// Get the value title
			std::string title = _getValueTitle(blocks[i][j]);
			std::string data = _getValueValue(blocks[i][j]);
			if (title != "") {
				// check for existing columns with title
				int existingColumnIndex = -1;
				if (columns.size() > 0) {
					for (int c = 0; c < columns.size(); c++) {
						if (columns[c].getName() == title) existingColumnIndex = c;
					}
				}
				
				// add data to column if it exists
				if (existingColumnIndex >= 0) columns[existingColumnIndex].addData(data);
				else {
					// create new column if it does not
					column.setName(title);
					column.addData(data);
					columns.push_back(column);
				}
			}
		}
	}

	return columns;
}

std::vector<std::vector<std::string> > Parser::_getBlocks() {
	std::vector<std::vector<std::string> > blocks;
	if (_lines.size() <= 0) return blocks;

	int blockStartLine = -1, blockEndLine = -1;

	for (int i = 0; i < _lines.size(); i++) {
		// find blocks
		if (blockStartLine < 0 && _isLineStartBlock(_lines[i])) blockStartLine = i;
		if (blockEndLine < 0 && _isLineEndBlock(_lines[i])) blockEndLine = i;

		if (blockStartLine >= 0 && blockEndLine >= 0) {
			std::vector<std::string> block;
			// get the block text and add it to the list of blocks
			for (int j = blockStartLine + 1; j < blockEndLine; j++) {
				block.push_back(_lines[j]);
			}
			blocks.push_back(block);
			blockStartLine = -1;
			blockEndLine = -1;
		}
	}

	return blocks;
}

std::string Parser::_getMarkerValue(std::string line) {
	if (!_isLineMarker(line)) return "";
	
	// get the text between the markers
	int markerStart = -1, markerEnd = -1;
	for (int i = 0; i < line.length(); i++) {
		if (markerStart < 0 && line.at(i) == codeId::marker) markerStart = i;
		else if (markerEnd < 0 && line.at(i) == codeId::marker) markerEnd = i;

		if (markerStart >= 0 && markerEnd >= 0) break;
	}

	// check the marker indexes
	if (markerStart < 0 || markerEnd < 0) return "";
	if (markerStart >= markerEnd) return "";

	// make a string with the marker text
	std::string markerValue;
	for (int i = markerStart + 1; i < markerEnd; i++) {
		markerValue += line.at(i);
	}
	return markerValue;
}

std::string Parser::_getValueTitle(std::string line) {
	if (!_isLineValue(line)) return "";
	// get the title
	int separatorIndex = -1;
	for (int i = 0; i < line.length(); i++) {
		if (line.at(i) == codeId::separator) separatorIndex = i;
	}
	if (separatorIndex < 0) return "";
	std::string titleVal;
	for (int i = 0; i < separatorIndex; i++) {
		titleVal += line.at(i);
	}
	return titleVal;
}
std::string Parser::_getValueValue(std::string line) {
	if (!_isLineValue(line)) return "";
	// get the value
	int separatorIndex = -1;
	for (int i = 0; i < line.length(); i++) {
		if (line.at(i) == codeId::separator) separatorIndex = i;
	}
	if (separatorIndex < 0) return "";
	std::string valueVal;
	for (int i = separatorIndex + 1; i < line.length(); i++) {
		valueVal += line.at(i);
	}
	return valueVal;
}

bool Parser::_isLineMarker(std::string line) {
	if (line.length() <= 0) return false;
	for (int i = 0; i < line.length(); i++) {
		if (line.at(i) == codeId::marker) return true;
	}
	return false;
}
bool Parser::_isLineStartBlock(std::string line) {
	if (line.length() <= 0) return false;
	// get the marker value if the line contains a marker
	std::string markerValue;
	if (_isLineMarker(line)) markerValue = _getMarkerValue(line);
	// check if the marker is the beginning of a block
	if (markerValue == codeId::beginBlock) return true;
	return false;
}
bool Parser::_isLineEndBlock(std::string line) {
	if (line.length() <= 0) return false;
	// get the marker value if the line contains a marker
	std::string markerValue;
	if (_isLineMarker(line)) markerValue = _getMarkerValue(line);
	// check if the marker is the beginning of a block
	if (markerValue == codeId::endBlock) return true;
	return false;
}
bool Parser::_isLineValue(std::string line) {
	if (line.length() <= 0) return false;
	// check for the separator 
	for (int i = 0; i < line.length(); i++) {
		if (line.at(i) == codeId::separator) return true;
	}
}
