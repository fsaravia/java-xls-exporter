ExportaXLS is a small Java library to export a series of data to an MS Excel file
It depends on Apache POI Library (http://poi.apache.org/)

It's Usage is simple, just create an exporter class that implements the XLSExporter interface and complete the required methods
After that create a new ExcelBook with your exported class, instantiate some OutputSteam and call book.write(outputStream) and voila!

More documentation and samples coming later