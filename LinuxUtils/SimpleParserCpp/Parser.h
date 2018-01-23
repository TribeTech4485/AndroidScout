#pragma once
#include "Column.h"
#include <vector>
class Parser
{
public:
	enum {NO_LINES, SUCCESS, NO_CONTENT};	// Enum return values for the parse function

	Parser();
	Parser(std::vector<std::string>);
	~Parser();

	std::string convertToCSV();
private:

	std::string _getMarkerValue(std::string);
	std::string _getValueTitle(std::string);
	std::string _getValueValue(std::string);

	bool _isLineMarker(std::string);
	bool _isLineStartBlock(std::string);
	bool _isLineEndBlock(std::string);
	bool _isLineValue(std::string);

	std::vector<Column> _getColumns();
	std::vector<std::vector<std::string> > _getBlocks();

	std::vector<std::string> _lines;
};

