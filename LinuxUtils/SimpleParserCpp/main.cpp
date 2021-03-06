#include <iostream>
#include <fstream>
#include <sstream>

#include "Parser.h"

int main(int argc, char** argv) {
	std::string inputPath = "test.list";
	std::string outputPath = "converted.csv";
	std::string continString = "n";
	bool continuous = false;

	if (argc < 2) {
		std::cout << "Usage: " << argv[0] << " [Input Path] [Output Path]\n";
		return 0;
	}

	inputPath = argv[1];
	outputPath = argv[2];
	if (argc > 3) {
		continString = argv[3];
		std::cout << "Checking if continuous....\n";
		if (continString == "y") {
			continuous = true;
			std::cout << "Running contnuously....\n";
		}
	}

	std::vector<std::string> prevLines;
	bool run = true;
	while (run) {
		std::string inputText;
		std::vector<std::string> lines;
		std::ifstream readStream(inputPath.c_str());
		if (readStream.fail()) {
			std::cout << "Could not read file.\n";
			if (!continuous) return 0;
		}
		else {
			while (std::getline(readStream, inputText)) {
				lines.push_back(inputText);
			}
			readStream.close();
		}

		if (prevLines != lines) {
			Parser p(lines);
			prevLines = lines;

			std::ofstream writeStream;
			writeStream.open(outputPath.c_str());

			writeStream << p.convertToCSV().c_str();
			writeStream.close();

			std::cout << "Completed with no errors.\n";
		}

		if (!continuous) run = false;
	}

	return 0;
}
