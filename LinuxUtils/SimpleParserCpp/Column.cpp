#include "Column.h"



Column::Column()
{
}

Column::Column(std::string name) : _name(name) {}


Column::~Column()
{
}


std::string Column::getRowData(int row) {
	// Check if the row number exists
	if (row < 0 || row > _data.size() - 1) return "";
	return _data[row];
}
