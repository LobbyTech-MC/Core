package me.dablakbandit.core.zip.io;

import java.io.IOException;
import java.io.InputStream;

import me.dablakbandit.core.zip.unzip.UnzipEngine;

public abstract class BaseInputStream extends InputStream{

	@Override
	public int read() throws IOException{
		return 0;
	}

	public void seek(long pos) throws IOException{}

	@Override
	public int available() throws IOException{
		return 0;
	}

	public UnzipEngine getUnzipEngine(){
		return null;
	}

}
