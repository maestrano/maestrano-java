package com.maestrano.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class MnoZipHelper {
	public static byte[] deflate(byte[] data) throws IOException {
		Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION,true);
		deflater.setInput(data);  

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   

		deflater.finish();  
		byte[] buffer = new byte[1024];   
		while (!deflater.finished()) {  
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);   
		}  
		outputStream.close();  
		byte[] output = outputStream.toByteArray();  

		deflater.end();

		return output;  
	}  

	public static byte[] inflate(byte[] data) throws IOException, DataFormatException {  
		Inflater inflater = new Inflater(true);   
		inflater.setInput(data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
		byte[] buffer = new byte[1024];  
		while (!inflater.finished()) {  
			int count = inflater.inflate(buffer);  
			outputStream.write(buffer, 0, count);  
		}  
		outputStream.close();  
		byte[] output = outputStream.toByteArray();  

		inflater.end();

		return output;  
	}  
}
