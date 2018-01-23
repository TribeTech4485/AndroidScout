cmake_minimum_required (VERSION 2.6)
project (SimpleParser)

file(GLOB SimpleParser_SRC
	"*.h"
	"*.cpp"
)

add_executable(SimpleParser ${SimpleParser_SRC})
