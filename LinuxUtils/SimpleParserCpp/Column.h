#pragma once
#include <string>
#include <vector>
class Column
{
public:
	Column();
	Column(std::string);
	~Column();

	void addData(std::string data) { _data.push_back(data); }
	void setName(std::string name) { _name = name; }

	int getRowCount() { return _data.size(); }
	std::string getRowData(int);
	std::string getName() { return _name; }

private:

	std::string _name;
	std::vector<std::string> _data;

};

