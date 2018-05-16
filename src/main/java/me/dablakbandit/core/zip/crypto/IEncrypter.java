package me.dablakbandit.core.zip.crypto;

import me.dablakbandit.core.zip.exception.ZipException;

public interface IEncrypter{

	public int encryptData(byte[] buff) throws ZipException;

	public int encryptData(byte[] buff, int start, int len) throws ZipException;

}
